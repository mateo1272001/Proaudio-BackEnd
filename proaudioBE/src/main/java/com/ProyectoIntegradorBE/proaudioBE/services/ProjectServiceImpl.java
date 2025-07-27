package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Event.EventResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Expense.ExpenseRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Expense.ExpenseResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.ItemResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PageableDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PriceReponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ProductProject.ProductProjectRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ProductProject.ProductProjectResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.*;
import com.ProyectoIntegradorBE.proaudioBE.entities.ProjectEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.*;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.EventMapper;
import com.ProyectoIntegradorBE.proaudioBE.mappers.ProjectMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.ProjectRepository;
import com.ProyectoIntegradorBE.proaudioBE.repositories.specifications.ProjectSpecification;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.*;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final TemplateEngine templateEngine;

    private final EventService eventService;

    private final ItemService itemService;

    private final ProductService productService;

    private final ExpenseService expenseService;

    private final ProductProjectService productProjectService;

    private final RentPriceService rentPriceService;

    private final UtilService utilService;

    private final EventMapper eventMapper;

    private final ProjectMapper projectMapper;

    private final ProjectRepository projectRepository;

    private final List<ProjectStatusEnum> UPDATE_STATUSES =
            List.of(ProjectStatusEnum.PLANNED, ProjectStatusEnum.CONFIRMED, ProjectStatusEnum.DISCARDED);


    private static ProjectResponseDto makeProjectResponseDto(ProjectRequestDto request, ProjectEntity projectEntity,
                                                             EventResponseDto eventResponseDto,
                                                             List<ProductProjectResponseForProjectDto> products,
                                                             List<ExpenseResponseDto> expenses) {
        ProjectResponseDto projectResponseDto = new ProjectResponseDto();
        projectResponseDto.setProjectId(projectEntity.getProjectId());
        projectResponseDto.setName(projectEntity.getName());
        projectResponseDto.setDescription(projectEntity.getDescription());
        projectResponseDto.setStartDate(projectEntity.getStartDate());
        projectResponseDto.setEndDate(projectEntity.getEndDate());
        projectResponseDto.setEvent(eventResponseDto);
        projectResponseDto.setClient(null); //todo [CLIENT] add client
        projectResponseDto.setStatus(projectEntity.getStatus());
        projectResponseDto.setPaymentStatus(projectEntity.getPaymentStatus());
        projectResponseDto.setProjectType(projectEntity.getProjectType());
        projectResponseDto.setCostAddition(request.getCostAddition());
        projectResponseDto.setProducts(products);
        projectResponseDto.setExpenses(expenses);

        return projectResponseDto;
    }

    @Override
    @Transactional
    public ProjectResponseDto createProject(ProjectRequestDto request) {

        EventResponseDto eventResponseDto = getOrCreateEvent(request);

        ProjectEntity projectEntity = setProjectEntity(request, eventResponseDto);

        projectEntity = projectRepository.save(projectEntity);

        List<ProductProjectResponseForProjectDto> products = new ArrayList<>();
        if (Objects.nonNull(request.getProducts())) {
            products = setProducts(request.getProducts(), projectEntity.getProjectId());
        }

        List<ExpenseResponseDto> expenses = new ArrayList<>();
        if (Objects.nonNull(request.getExpenses())) {
            expenses = setExpenses(request.getExpenses(), projectEntity.getProjectId());
        }

        return makeProjectResponseDto(request, projectEntity, eventResponseDto, products, expenses);
    }

    private EventResponseDto getOrCreateEvent(ProjectRequestDto request) {
        return Objects.nonNull(request.getEvent().getEventId()) ?
                eventService.GetEvent(request.getEvent().getEventId()) : eventService.CreateEvent(request.getEvent());
    }

    @Override
    public ProjectSimpleReponseDto updateProject(Long id, ProjectRequestDto request) {

        ProjectSimpleReponseDto projectResponseDto = getProject(id);

        ProjectStatusEnum statusEnum = projectResponseDto.getStatus();
        boolean statusIsUpdatable = UPDATE_STATUSES.contains(statusEnum);

        ProjectEntity entityResponse = new ProjectEntity();

        //always updatable
        entityResponse.setProjectId(projectResponseDto.getProjectId());
        entityResponse.setName(request.getName());
        entityResponse.setDescription(Objects.nonNull(request.getDescription()) ? request.getDescription() : null);
        entityResponse.setStatus(request.getStatus());
        entityResponse.setPaymentStatus(request.getPaymentStatus());
        entityResponse.setProjectType(request.getProjectType());
        entityResponse.setCostAddition(request.getCostAddition());

        entityResponse.setStartDate(projectResponseDto.getStartDate());
        entityResponse.setEndDate(projectResponseDto.getEndDate());
        entityResponse.setClientId(projectResponseDto.getClientId());
        entityResponse.setEventId(projectResponseDto.getEventId());

        //only on CONFIRMED, PLANNED or DISCARDED
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new BadRequestException("¡La fecha de fin no puede ser anterior a la fecha de inicio!");
        }

        if (!request.getStartDate().equals(projectResponseDto.getStartDate())) {
            if (!statusIsUpdatable) {
                throw new BadRequestException(
                        "La fecha de inicio solo se puede modificar si el projecto aún no empieza!");
            }
            entityResponse.setStartDate(request.getStartDate());
        }

        if (!request.getEndDate().equals(projectResponseDto.getEndDate())) {
            if (!statusIsUpdatable) {
                throw new BadRequestException(
                        "La fecha de inicio solo se puede modificar si el projecto aún no empieza!");
            }
            entityResponse.setEndDate(request.getEndDate());
        }

        if (Objects.nonNull(request.getEvent().getEventId())) {
            entityResponse.setEventId(projectResponseDto.getEventId());
            Long requestEventId = request.getEvent().getEventId();

            if (!requestEventId.equals(projectResponseDto.getEventId())) {
                if (!statusIsUpdatable) {
                    throw new BadRequestException("El evento solo se puede modificar si el projecto aún no empieza!");
                }
                EventResponseDto eventResponseDto = eventService.GetEvent(requestEventId);
                entityResponse.setEventId(eventResponseDto.getEventId());
            }

        } else {
            if (!statusIsUpdatable) {
                throw new BadRequestException("El evento solo se puede modificar si el projecto aún no empieza!");
            }
            EventResponseDto eventResponseDto = eventService.CreateEvent(request.getEvent());
            entityResponse.setEventId(eventResponseDto.getEventId());
        }

        entityResponse.setClientId(projectResponseDto.getClientId()); //todo [CLIENT] add client update

        projectRepository.save(entityResponse);

        return projectMapper.toDto(entityResponse);
    }

    @Override
    public ProjectSimpleReponseDto getProject(Long id) {

        ProjectEntity projectEntity = projectRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Project con ID no encontrado: " + id));

        EventResponseDto event = eventService.GetEvent(projectEntity.getEventId());

        ProjectSimpleReponseDto projectSimpleReponseDto = projectMapper.toDto(projectEntity);
        projectSimpleReponseDto.setEvent(event);

        return projectSimpleReponseDto;
    }

    @Override
    public ProjectTypesResponseDto getProjectTypes() {

        return new ProjectTypesResponseDto(Arrays.stream(ProjectTypeEnum.values()).toList());
    }

    @Override
    public ProjectStatusesDto getPossibleStatusByProjectId(Long id) {
        ProjectEntity projectEntity = projectRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(String.format("Proyecto con ID %s no encontrado: ", id)));

        List<ProjectStatusEnum> possibleStatus = switch (projectEntity.getStatus()) {
            case PLANNED, DISCARDED -> List.of(ProjectStatusEnum.CONFIRMED);
            case CONFIRMED -> List.of(ProjectStatusEnum.DISCARDED, ProjectStatusEnum.PLANNED);
            case ON_COURSE -> List.of(ProjectStatusEnum.DISCARDED);
            case EXPIRED -> List.of(ProjectStatusEnum.COMPLETED);
            case COMPLETED -> List.of();
        };

        return new ProjectStatusesDto(possibleStatus);
    }

    @Override
    public ProjectStatusesDto getPossibleStatusForStartingProject() {
        return new ProjectStatusesDto(List.of(ProjectStatusEnum.PLANNED, ProjectStatusEnum.CONFIRMED));
    }

    @Override
    public void updateProjectStatusAutomatically() {

        List<ProjectEntity> projectEntities = projectRepository.findByStatusIn(
                List.of(ProjectStatusEnum.PLANNED, ProjectStatusEnum.CONFIRMED, ProjectStatusEnum.ON_COURSE,
                        ProjectStatusEnum.EXPIRED));

        for (ProjectEntity projectEntity : projectEntities) {

            projectEntity.setStatus(switch (projectEntity.getStatus()) {
                case PLANNED -> calculateFromStatusPlanned(projectEntity);
                case CONFIRMED -> calculateFromStatusConfirmed(projectEntity);
                case ON_COURSE -> calculateFromStatusOnCourse(projectEntity);
                case EXPIRED -> calculateFromStatusExpired(projectEntity);
                case DISCARDED -> ProjectStatusEnum.DISCARDED;
                case COMPLETED -> ProjectStatusEnum.COMPLETED;
            });

            projectRepository.save(projectEntity);
        }
    }

    @Override
    public ProjectDetailsResponseDto getProjectDetails(Long id) {

        ProjectSimpleReponseDto projectSimpleReponseDto = getProject(id);

        ProjectDetailsResponseDto projectDetailsResponseDto = new ProjectDetailsResponseDto();
        projectDetailsResponseDto.setProjectId(projectSimpleReponseDto.getProjectId());
        projectDetailsResponseDto.setName(projectSimpleReponseDto.getName());
        projectDetailsResponseDto.setStartDate(projectSimpleReponseDto.getStartDate());
        projectDetailsResponseDto.setEndDate(projectSimpleReponseDto.getEndDate());
        projectDetailsResponseDto.setEvent(eventService.GetEvent(projectSimpleReponseDto.getEventId()));
        //        projectDetailsResponseDto.setClient(); //todo [CLIENT] add when clients are included
        projectDetailsResponseDto.setStatus(projectSimpleReponseDto.getStatus());
        projectDetailsResponseDto.setPaymentStatus(projectSimpleReponseDto.getPaymentStatus());
        projectDetailsResponseDto.setProjectType(projectSimpleReponseDto.getProjectType());
        projectDetailsResponseDto.setProducts(productProjectService.getProductsInProject(id).getProducts());
        projectDetailsResponseDto.setExpenses(expenseService.GetExpensesByProject(id).getExpenses());
        //        projectDetailsResponseDto.setItems(); //todo [PROJECTS] add when items are included to projects


        return projectDetailsResponseDto;
    }

    @Override
    public ProjectPaymentStatusesDto getPossiblePaymentStatusByProjectId(Long id) {

        ProjectEntity projectEntity = projectRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(String.format("Proyecto con ID %s no encontrado: ", id)));

        List<PaymentStatusEnum> possibleStatus = switch (projectEntity.getPaymentStatus()) {
            case BUDGETED -> List.of(PaymentStatusEnum.BILL_CREATED);
            case BILL_CREATED -> List.of(PaymentStatusEnum.PARTIALLY_PAID, PaymentStatusEnum.PAID);
            case PARTIALLY_PAID -> List.of(PaymentStatusEnum.PAID);
            case PAID -> List.of();
        };

        return new ProjectPaymentStatusesDto(possibleStatus);

    }

    private static Specification<ProjectEntity> getProjectEntitySpecification(String filterPaymentStatus, String name,
                                                                              Boolean paymentIsFiltered,
                                                                              Boolean nameIsFiltered,
                                                                              List<ProjectStatusEnum> statuses) {
        if (paymentIsFiltered && nameIsFiltered) {
            return ProjectSpecification.filterBy(statuses, filterPaymentStatus, name);
        } else {
            if (paymentIsFiltered) {
                return ProjectSpecification.filterByStatusAndPaymentStatus(statuses, filterPaymentStatus);
            }
            if (nameIsFiltered) {
                return ProjectSpecification.filterByStatusAndName(statuses, name);
            }
            return ProjectSpecification.filterBy(statuses);
        }
    }

    @Override
    public ProjectListResponseDto getProjectList(Integer page, Integer size, String sortBy, String direction,
                                                 List<String> filterStatus, String filterPaymentStatus, String name) {

        page = (page != null ? page : 0);
        DirectionEnum dir =
                Objects.isNull(direction) ? DirectionEnum.DESC : DirectionEnum.valueOf(direction.toUpperCase());

        size = Objects.nonNull(size) ? size : 10;

        ProjectSortByEnum sortByEnum =
                Objects.isNull(sortBy) ? ProjectSortByEnum.START_DATE : ProjectSortByEnum.valueOf(sortBy.toUpperCase());

        String sortColumn = switch (sortByEnum) {
            case START_DATE -> "startDate";
            case END_DATE -> "endDate";
            case NAME -> "name";
        };

        List<ProjectStatusEnum> statuses = new ArrayList<>();

        if (Objects.isNull(filterStatus) || filterStatus.isEmpty()) {
            statuses = Arrays.stream(ProjectStatusEnum.values()).toList();
        } else {
            for (String status : filterStatus) {
                statuses.add(ProjectStatusEnum.valueOf(status.toUpperCase()));
            }
        }

        Boolean paymentIsFiltered = !StringUtils.isBlank(filterPaymentStatus);
        Boolean nameIsFiltered = !StringUtils.isBlank(name);

        Specification<ProjectEntity> spec =
                getProjectEntitySpecification(filterPaymentStatus, name, paymentIsFiltered, nameIsFiltered, statuses);

        Sort sort = Sort.by(Sort.Direction.fromString(dir.name()), sortColumn);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ProjectEntity> pages = projectRepository.findAll(spec, pageable);

        List<ProjectRowResponseDto> responseDtos = createRows(pages.getContent());

        PageableDto pagination = utilService.buildPageableDto(pages);

        return new ProjectListResponseDto(responseDtos, pagination);
    }

    @Override
    public ProjectStatusesDto getAllStatus() {
        return new ProjectStatusesDto(Arrays.stream(ProjectStatusEnum.values()).toList());
    }

    @Override
    public ProjectPaymentStatusesDto getAllPaymentStatus() {
        return new ProjectPaymentStatusesDto(Arrays.stream(PaymentStatusEnum.values()).toList());
    }

    @Override
    public RunningStatusResponseDto getAllRunningEnum() {
        return new RunningStatusResponseDto(Arrays.stream(ProjectRunningStatusEnum.values()).toList());
    }

    private List<ProjectRowResponseDto> createRows(List<ProjectEntity> content) {

        List<ProjectRowResponseDto> responseDtos = new ArrayList<>();

        for (ProjectEntity project : content) {
            ProjectRowResponseDto projectRowResponseDto = new ProjectRowResponseDto();
            projectRowResponseDto.setProjectId(project.getProjectId());
            projectRowResponseDto.setName(project.getName());
            projectRowResponseDto.setStatus(project.getStatus());
            projectRowResponseDto.setPaymentStatus(project.getPaymentStatus());
            projectRowResponseDto.setStartDate(project.getStartDate());
            projectRowResponseDto.setEndDate(project.getEndDate());
            projectRowResponseDto.setRunningStatus(
                    project.getStartDate().isAfter(LocalDateTime.now()) ? ProjectRunningStatusEnum.RUNNING :
                            project.getStartDate().isAfter(LocalDateTime.now().minusWeeks(1L)) ?
                                    ProjectRunningStatusEnum.PREPARING : ProjectRunningStatusEnum.NONE);
            responseDtos.add(projectRowResponseDto);
        }

        return responseDtos;

    }

    private ProjectStatusEnum calculateFromStatusExpired(ProjectEntity projectEntity) {
        if (projectEntity.getStartDate().isAfter(LocalDateTime.now())) {
            return ProjectStatusEnum.COMPLETED;
        } else {
            return ProjectStatusEnum.EXPIRED;
        }
    }


    private ProjectStatusEnum calculateFromStatusOnCourse(ProjectEntity projectEntity) {
        if (LocalDateTime.now().isAfter(projectEntity.getEndDate())) {
            if (true == true) { //todo [PROJECT] add validation for returned items
                return ProjectStatusEnum.EXPIRED;
            } else {
                return ProjectStatusEnum.COMPLETED;
            }
        } else {
            return ProjectStatusEnum.ON_COURSE;
        }
    }

    private ProjectStatusEnum calculateFromStatusConfirmed(ProjectEntity projectEntity) {
        if (LocalDateTime.now().isAfter(projectEntity.getStartDate())) {
            return ProjectStatusEnum.ON_COURSE;
        } else {
            return ProjectStatusEnum.CONFIRMED;
        }
    }

    private ProjectStatusEnum calculateFromStatusPlanned(ProjectEntity projectEntity) {
        if (LocalDateTime.now().isAfter(projectEntity.getStartDate())) {
            return ProjectStatusEnum.DISCARDED;
        } else {
            return ProjectStatusEnum.PLANNED;
        }
    }

    private ProjectEntity setProjectEntity(ProjectRequestDto request, EventResponseDto eventResponseDto) {

        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new BadRequestException("¡La fecha de fin no puede ser anterior a la fecha de inicio!");
        }

        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setName(request.getName());
        projectEntity.setDescription(request.getDescription());
        projectEntity.setStartDate(request.getStartDate());
        projectEntity.setEndDate(request.getEndDate());
        projectEntity.setEventId(eventResponseDto.getEventId());
        projectEntity.setClientId(1L);
        //  todo [CLIENT] add client to projects
        projectEntity.setStatus(Objects.nonNull(request.getStatus()) ? request.getStatus() : ProjectStatusEnum.PLANNED);

        projectEntity.setPaymentStatus(
                Objects.nonNull(request.getPaymentStatus()) ? request.getPaymentStatus() : PaymentStatusEnum.BUDGETED);

        projectEntity.setProjectType(
                Objects.nonNull(request.getProjectType()) ? request.getProjectType() : ProjectTypeEnum.SERVICE);

        projectEntity.setCostAddition(
                Objects.nonNull(request.getCostAddition()) ? CalculateCostAddition(request.getCostAddition()) :
                        BigDecimal.valueOf(1));
        return projectEntity;
    }

    private List<ExpenseResponseDto> setExpenses(List<ExpenseRequestDto> expenses, Long projectId) {

        List<ExpenseResponseDto> response = new ArrayList<>();

        for (ExpenseRequestDto expense : expenses) {
            expense.setProjectId(projectId);
            response.add(expenseService.CreateExpense(expense));
        }

        return response;
    }

    private List<ProductProjectResponseForProjectDto> setProducts(List<ProjectProductRequestDto> productRequests,
                                                                  Long projectId) {

        List<ProductProjectResponseForProjectDto> productsResponse = new ArrayList<>();

        List<Long> productIds = productRequests.stream().map(ProjectProductRequestDto::getProductId).toList();
        List<ItemResponseDto> items = itemService.GetByProductIds(productIds);

        for (ProjectProductRequestDto productRequest : productRequests) {

            List<ItemResponseDto> itemsOfProduct =
                    items.stream().filter(i -> i.getProductId().equals(productRequest.getProductId())).toList();

            createProductProject(productRequest, projectId, itemsOfProduct);

            productsResponse.add(makeProductResponse(productRequest));

        }

        return productsResponse;
    }

    private ProductProjectResponseForProjectDto makeProductResponse(ProjectProductRequestDto productRequest) {
        ProductResponseDto product = productService.GetProduct(productRequest.getProductId());

        if (!product.getStatus().equals(ProductStatus.ACTIVE)) {
            throw new BadRequestException("¡Este producto no está disponible!");
        }

        ProductProjectResponseForProjectDto productProjectResponseForProjectDto =
                new ProductProjectResponseForProjectDto();
        productProjectResponseForProjectDto.setProductId(product.getProductId());
        productProjectResponseForProjectDto.setModel(product.getModel());
        productProjectResponseForProjectDto.setComments(productProjectResponseForProjectDto.getComments());
        productProjectResponseForProjectDto.setStatus(product.getStatus());
        productProjectResponseForProjectDto.setCreatedAt(product.getCreatedAt());
        productProjectResponseForProjectDto.setUpdatedAt(product.getUpdatedAt());
        productProjectResponseForProjectDto.setReplacementValue(product.getReplacementValue());
        productProjectResponseForProjectDto.setAmount(productRequest.getAmount());
        return productProjectResponseForProjectDto;
    }

    private ProductProjectResponseDto createProductProject(ProjectProductRequestDto productRequest, Long projectId,
                                                           List<ItemResponseDto> itemsOfProduct) {

        PriceReponseDto priceReponseDto = rentPriceService.getPrice(productRequest.getPriceId());

        if (!priceReponseDto.getProductId().equals(productRequest.getProductId())) {
            throw new BadRequestException("¡Este precio no corresponde al producto!");
        }

        ProductProjectRequestDto productProjectRequestDto = new ProductProjectRequestDto();
        productProjectRequestDto.setProductId(productRequest.getProductId());
        productProjectRequestDto.setProjectId(projectId);
        productProjectRequestDto.setRentPriceId(productRequest.getPriceId());
        productProjectRequestDto.setAmount(productRequest.getAmount());
        productProjectRequestDto.setStatus(BasicEnumStatus.ENABLED);

        return productProjectService.createProductProject(productProjectRequestDto, itemsOfProduct);
    }

    private BigDecimal CalculateCostAddition(BigDecimal costAddition) {
        return costAddition;
    }


    private String convertirImagenABase64(String path) throws IOException {
        ClassPathResource imgFile = new ClassPathResource(path);
        byte[] bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
        return Base64.getEncoder().encodeToString(bytes);
    }

    public byte[] generateProjectPdf(Long projectId) {
        //ProjectSimpleReponseDto project = getProject(projectId);
        // Simulamos los datos del proyecto
        Map<String, Object> project = new HashMap<>();
        project.put("clientName", "Empresa XYZ S.A.");
        project.put("eventName", "Lanzamiento de Producto 2025");
        project.put("ubication", "Montevideo, Uruguay");
        project.put("eventDateStart", "10/08/2025");
        project.put("eventDateEnd", "12/08/2025");
        project.put("dateValidUntil", "30/08/2025");

        // Lista de productos (modelName, amount)
        List<Map<String, Object>> products = new ArrayList<>();
        Map<String, Object> prod1 = new HashMap<>();
        prod1.put("modelName", "Consola de Sonido Behringer X32");
        prod1.put("amount", 2);
        products.add(prod1);

        Map<String, Object> prod2 = new HashMap<>();
        prod2.put("modelName", "Sistema de Parlantes JBL PRX815");
        prod2.put("amount", 4);
        products.add(prod2);

        project.put("products", products);

        project.put("totalBudget", "$4,200 USD");
        project.put("user", "Juan Pérez");
        project.put("userPhone", "+598 91 234 567");
        project.put("email", "juan.perez@proaudio.com.uy");


        // Cargar el HTML con Thymeleaf
        Context context = new Context();
        context.setVariable("project", project);

        try {
            String logoBase64 = convertirImagenABase64("static/logo-lettering.png");
            context.setVariable("logoBase64", logoBase64);
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar imágenes para el PDF", e);
        }

        String htmlContent = templateEngine.process("project-pdf.html", context);

        // Convertir HTML a PDF
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF del proyecto", e);
        }
    }
}
