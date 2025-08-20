package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Client.ClientResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Client.ProjectParticipatedResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Event.EventResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Expense.ExpenseRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Expense.ExpenseResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.GeneralParmeters.GeneralParameterResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.ItemResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ItemProject.ItemProjectResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Notifications.NotificationRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PageableDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PriceReponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ProductProject.ProductProjectRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ProductProject.ProductProjectWithModelResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.*;
import com.ProyectoIntegradorBE.proaudioBE.dtos.User.UserResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ItemEntity;
import com.ProyectoIntegradorBE.proaudioBE.entities.ItemProjectEntity;
import com.ProyectoIntegradorBE.proaudioBE.entities.NotificationEntity;
import com.ProyectoIntegradorBE.proaudioBE.entities.ProjectEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.*;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.ItemProjectMapper;
import com.ProyectoIntegradorBE.proaudioBE.mappers.ProjectMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.ProjectRepository;
import com.ProyectoIntegradorBE.proaudioBE.repositories.specifications.ProjectSpecification;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.*;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.ProyectoIntegradorBE.proaudioBE.Utils.AppConstants.*;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final EventService eventService;

    private final ItemService itemService;

    private final ItemProjectService itemProjectService;

    private final ProductService productService;

    private final ExpenseService expenseService;

    private final ProductProjectService productProjectService;

    private final RentPriceService rentPriceService;

    private final UtilService utilService;

    private final UserService userService;

    private final GeneralParameterService generalParameterService;

    private final PdfService pdfService;

    private final ClientService clientService;

    private final NotificationService notificationService;


    private final ProjectRepository projectRepository;


    private final ProjectMapper projectMapper;

    private final ItemProjectMapper itemProjectMapper;


    private static ProjectResponseDto makeProjectResponseDto(ProjectRequestDto request, ProjectEntity projectEntity,
                                                             EventResponseDto eventResponseDto,
                                                             List<ProductProjectResponseForProjectDto> products,
                                                             List<ExpenseResponseDto> expenses,
                                                             ClientResponseDto clientResponseDto) {
        ProjectResponseDto projectResponseDto = new ProjectResponseDto();
        projectResponseDto.setProjectId(projectEntity.getProjectId());
        projectResponseDto.setName(projectEntity.getName());
        projectResponseDto.setDescription(projectEntity.getDescription());
        projectResponseDto.setStartDate(projectEntity.getStartDate());
        projectResponseDto.setEndDate(projectEntity.getEndDate());
        projectResponseDto.setEvent(eventResponseDto);
        projectResponseDto.setClient(clientResponseDto);
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

        ClientResponseDto clientResponseDto = getOrCreateClient(request);

        ProjectEntity projectEntity = setProjectEntity(request, eventResponseDto, clientResponseDto);

        projectEntity = projectRepository.save(projectEntity);

        List<ProductProjectResponseForProjectDto> products = new ArrayList<>();
        if (Objects.nonNull(request.getProducts())) {
            products = setProducts(request.getProducts(), projectEntity.getProjectId());
        }

        List<ExpenseResponseDto> expenses = new ArrayList<>();
        if (Objects.nonNull(request.getExpenses())) {
            expenses = setExpenses(request.getExpenses(), projectEntity.getProjectId());
        }

        return makeProjectResponseDto(request, projectEntity, eventResponseDto, products, expenses, clientResponseDto);
    }

    private ClientResponseDto getOrCreateClient(ProjectRequestDto request) {

        if (Objects.nonNull(request.getClient().getClientId())) {

            ClientResponseDto clientResponseDto = clientService.getClientById(request.getClient().getClientId());

            if (clientResponseDto.getStatus().equals(BasicEnumStatus.DISABLED)) {
                throw new BadRequestException("¡Este cliente no está activo!");
            }

            return clientResponseDto;

        } else {
            return clientService.createClient(request.getClient());
        }

    }

    private EventResponseDto getOrCreateEvent(ProjectRequestDto request) {
        return Objects.nonNull(request.getEvent().getEventId()) ?
                eventService.GetEvent(request.getEvent().getEventId()) : eventService.CreateEvent(request.getEvent());
    }

    @Override
    public ProjectSimpleReponseDto updateProject(Long id, ProjectRequestDto request) {

        ProjectSimpleReponseDto projectResponseDto = getProject(id);

        ProjectStatusEnum statusEnum = projectResponseDto.getStatus();
        boolean statusIsUpdatable = MANUALLY_UPDATETABLE_STATUSES.contains(statusEnum);

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

        //Event validation
        if (Objects.isNull(request.getEvent())) {
            throw new BadRequestException("¡El proyecto debe tener un evento!");
        }

        if (Objects.isNull(request.getEvent().getEventId())) {

            if (!statusIsUpdatable) {
                throw new BadRequestException("¡El evento solo se puede modificar si el projecto aún no empieza!");
            }

            EventResponseDto eventResponseDto = eventService.CreateEvent(request.getEvent());
            entityResponse.setEventId(eventResponseDto.getEventId());

        } else {
            Long requestEventId = request.getEvent().getEventId();

            if (!requestEventId.equals(projectResponseDto.getEventId())) {
                if (!statusIsUpdatable) {
                    throw new BadRequestException("El evento solo se puede modificar si el projecto aún no empieza!");
                }
                EventResponseDto eventResponseDto = eventService.GetEvent(requestEventId);
                entityResponse.setEventId(eventResponseDto.getEventId());
            }
        }

        //client validation
        if (Objects.isNull(request.getClient())) {
            throw new BadRequestException("¡El proyecto debe tener un cliente!");
        }

        if (Objects.isNull(request.getClient().getClientId())) {

            if (!statusIsUpdatable) {
                throw new BadRequestException("El evento solo se puede modificar si el projecto aún no empieza!");
            }

            ClientResponseDto clientResponseDto = clientService.createClient(request.getClient());
            entityResponse.setClientId(clientResponseDto.getClientId());

        } else {
            Long requestClientId = request.getClient().getClientId();

            if (!requestClientId.equals(projectResponseDto.getClientId())) {
                if (!statusIsUpdatable) {
                    throw new BadRequestException("El cliente solo se puede modificar si el projecto aún no empieza!");
                }
                ClientResponseDto clientResponseDto = clientService.getClientById(requestClientId);
                if (clientResponseDto.getStatus().equals(BasicEnumStatus.DISABLED)) {
                    throw new BadRequestException("¡Este cliente no está disponible!");
                }
                entityResponse.setClientId(clientResponseDto.getClientId());
            }
        }

        ProjectEntity projectEntity = projectRepository.save(entityResponse);

        solvePaymentNeededNotification(projectEntity);

        return projectMapper.toDto(entityResponse);
    }

    @Override
    public ProjectSimpleReponseDto getProject(Long id) {

        ProjectEntity projectEntity = projectRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Project con ID no encontrado: " + id));

        EventResponseDto event = eventService.GetEvent(projectEntity.getEventId());
        ClientResponseDto client = getClientById(projectEntity);

        ProjectSimpleReponseDto projectSimpleReponseDto = projectMapper.toDto(projectEntity);
        projectSimpleReponseDto.setEvent(event);
        projectSimpleReponseDto.setClient(client);

        return projectSimpleReponseDto;
    }

    private ClientResponseDto getClientById(ProjectEntity projectEntity) {

        try {

            return clientService.getClientById(projectEntity.getClientId());

        } catch (BadRequestException ex) {

            return null;

        }
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
            //todo [ITEM PROJECT] alter item project status when status ends
            projectRepository.save(projectEntity);
        }
    }

    private static boolean hasProjectStarted(ProjectSimpleReponseDto projectSimpleReponseDto) {
        boolean projectStarted;
        projectStarted = PROJECT_STARTED_STATUS.contains(projectSimpleReponseDto.getStatus());
        return projectStarted;
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
        if (allItemsReturned(projectEntity.getProjectId())) {

            return actionsOnSettingStatusCompleted(projectEntity);

        } else {
            return ProjectStatusEnum.EXPIRED;
        }
    }

    private ProjectStatusEnum actionsOnSettingStatusCompleted(ProjectEntity projectEntity) {
        sendPaymentNeededNotification(projectEntity);
        sendProjectNotificationCompleted(projectEntity);

        return ProjectStatusEnum.COMPLETED;
    }


    private ProjectStatusEnum calculateFromStatusOnCourse(ProjectEntity projectEntity) {
        if (LocalDateTime.now().isAfter(projectEntity.getEndDate())) {

            if (allItemsReturned(projectEntity.getProjectId())) {

                return actionsOnSettingStatusCompleted(projectEntity);

            } else {

                sendPaymentNeededNotification(projectEntity);
                sendProjectNotification(PROJECT_EXPIRED_TITLE, PROJECT_EXPIRED_BODY.formatted(projectEntity.getName()),
                        false, null, NotificationTypeEnum.PROJECT, projectEntity.getProjectId(),
                        RETURN_ITEMS_NOTIFICATION_ACTION);

                return ProjectStatusEnum.EXPIRED;
            }

        } else {
            return ProjectStatusEnum.ON_COURSE;
        }
    }

    private ProjectStatusEnum calculateFromStatusConfirmed(ProjectEntity projectEntity) {
        if (LocalDateTime.now().isAfter(projectEntity.getStartDate())) {

            sendProjectNotification(PROJECT_STARTED_TITLE, PROJECT_STARTED_BODY.formatted(projectEntity.getName()),
                    false, null, NotificationTypeEnum.PROJECT, projectEntity.getProjectId(),
                    SEND_ITEMS_NOTIFICATION_ACTION);

            return ProjectStatusEnum.ON_COURSE;

        } else {
            return ProjectStatusEnum.CONFIRMED;
        }
    }

    private ProjectStatusEnum calculateFromStatusPlanned(ProjectEntity projectEntity) {
        if (LocalDateTime.now().isAfter(projectEntity.getStartDate())) {

            sendProjectNotification(PROJECT_DISCARDED_TITLE, PROJECT_DISCARDED_BODY.formatted(projectEntity.getName()),
                    true, null, NotificationTypeEnum.PROJECT, projectEntity.getProjectId(),
                    NOT_REQUIRED_NOTIFICATION_ACTION);

            return ProjectStatusEnum.DISCARDED;
        } else {
            return ProjectStatusEnum.PLANNED;
        }
    }

    private ProjectEntity setProjectEntity(ProjectRequestDto request, EventResponseDto eventResponseDto,
                                           ClientResponseDto clientResponseDto) {

        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new BadRequestException("¡La fecha de fin no puede ser anterior a la fecha de inicio!");
        }

        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setName(request.getName());
        projectEntity.setDescription(request.getDescription());
        projectEntity.setStartDate(request.getStartDate());
        projectEntity.setEndDate(request.getEndDate());
        projectEntity.setEventId(eventResponseDto.getEventId());
        projectEntity.setClientId(clientResponseDto.getClientId());
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

    private static void validateExitAvailability(ProjectSimpleReponseDto projectSimpleReponseDto) {
        if (!EXIT_POSSIBLE_STATUS.contains(projectSimpleReponseDto.getStatus())) {
            throw new BadRequestException("El proyecto debe estar confirmado o en curso para enviar artículos");
        }
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

    private ProductInProjectResponseDtoImpl createProductProject(ProjectProductRequestDto productRequest,
                                                                 Long projectId,
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

    private static BigDecimal getExpensesBudget(List<ExpenseResponseDto> expenseResponseDto) {
        BigDecimal totalExpenses = BigDecimal.valueOf(0);

        if (!expenseResponseDto.isEmpty()) {
            for (ExpenseResponseDto expense : expenseResponseDto) {
                totalExpenses = totalExpenses.add(expense.getValue());
            }
        }
        return totalExpenses;
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
        projectDetailsResponseDto.setClient(clientService.getClientById(projectSimpleReponseDto.getClientId()));
        projectDetailsResponseDto.setStatus(projectSimpleReponseDto.getStatus());
        projectDetailsResponseDto.setPaymentStatus(projectSimpleReponseDto.getPaymentStatus());
        projectDetailsResponseDto.setProjectType(projectSimpleReponseDto.getProjectType());
        projectDetailsResponseDto.setProducts(productProjectService.getProductsInProject(id).getProducts());
        projectDetailsResponseDto.setExpenses(expenseService.GetExpensesByProject(id).getExpenses());
        projectDetailsResponseDto.setCostAddition(projectSimpleReponseDto.getCostAddition());
        projectDetailsResponseDto.setItems(itemProjectService.getItemsInProject(id).getItems());

        return projectDetailsResponseDto;
    }

    @Override
    public byte[] generateBudget(Long id) {

        ProjectSimpleReponseDto projectResponseDto = getProject(id);

        List<ExpenseResponseDto> expenseResponseDto = expenseService.GetExpensesByProject(id).getExpenses();
        BigDecimal totalExpenses = getExpensesBudget(expenseResponseDto);

        BigDecimal totalTransportCost = getTransportationBudget(projectResponseDto);

        List<ProductInProjectResponseDto> productsInProject =
                productProjectService.getProductsInProject(id).getProducts();

        BigDecimal totalInProductsInProject = getProductsBudget(projectResponseDto, productsInProject);

        BigDecimal percentageValue =
                projectResponseDto.getCostAddition().divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);

        BigDecimal totalBudget = totalInProductsInProject.add(totalExpenses).add(totalTransportCost)
                .multiply(percentageValue);

        UserResponseDto userResponseDto = userService.findUserByEmail(AuthUtilsSerivce.getLoggedUserEmail());

        ClientResponseDto clientResponseDto = clientService.getClientById(projectResponseDto.getClientId());

        return pdfService.generateProjectPdf(projectResponseDto, productsInProject, totalBudget, userResponseDto,
                clientResponseDto);
    }

    private List<ProductProjectResponseForProjectDto> setProducts(List<ProjectProductRequestDto> productRequests,
                                                                  Long projectId) {

        List<ProductProjectResponseForProjectDto> productsResponse = new ArrayList<>();

        List<Long> productIds = productRequests.stream().map(ProjectProductRequestDto::getProductId).toList();
        List<ItemResponseDto> items = itemService.getByProductIds(productIds);

        for (ProjectProductRequestDto productRequest : productRequests) {

            List<ItemResponseDto> itemsOfProduct =
                    items.stream().filter(i -> i.getProductId().equals(productRequest.getProductId())).toList();

            createProductProject(productRequest, projectId, itemsOfProduct);

            productsResponse.add(makeProductResponse(productRequest));

        }

        return productsResponse;
    }

    @Override
    @Transactional
    public ItemProjectResponseDto singleItemExit(Long idProject, Long idItem) {

        ProjectSimpleReponseDto projectSimpleReponseDto = this.getProject(idProject);
        validateExitAvailability(projectSimpleReponseDto);
        boolean projectStarted = hasProjectStarted(projectSimpleReponseDto);

        ItemResponseDto itemResponseDto = itemService.getItem(idItem);
        LocationEnum itemLocation = itemResponseDto.getLocation();

        ItemProjectResponseDto itemProjectResponseDto =
                itemProjectService.getItemProjectByProjectIdAndItemId(idProject, idItem);

        itemProjectResponseDto =
                actOnItemLocation(itemLocation, itemProjectResponseDto, projectStarted, itemResponseDto,
                        projectSimpleReponseDto);

        itemProjectResponseDto.setItemBoughtAt(itemResponseDto.getBoughtAt());
        itemProjectResponseDto.setItemPriceBought(itemResponseDto.getPriceBought());
        itemProjectResponseDto.setItemDescription(itemResponseDto.getDescription());
        itemProjectResponseDto.setItemStatus(itemResponseDto.getStatus());
        itemProjectResponseDto.setItemRange(itemProjectResponseDto.getItemRange());
        itemProjectResponseDto.setItemSerialNumber(itemResponseDto.getSerialNumber());
        itemProjectResponseDto.setProductId(itemResponseDto.getProductId());
        itemProjectResponseDto.setProductModel(itemProjectResponseDto.getProductModel());

        solveSendNotification(idProject);

        return itemProjectResponseDto;
    }

    private ItemProjectResponseDto actOnItemLocation(LocationEnum itemLocation,
                                                     ItemProjectResponseDto itemProjectResponseDto,
                                                     boolean projectStarted, ItemResponseDto itemResponseDto,
                                                     ProjectSimpleReponseDto projectSimpleReponseDto) {

        boolean itemInProject = Boolean.FALSE;
        if (Objects.nonNull(itemProjectResponseDto) &&
                itemProjectResponseDto.getStatus().equals(ItemProjectStatus.ENABLED)) {
            itemInProject = Boolean.TRUE;
        }

        switch (itemLocation) {
            case IN_DEPOSIT -> {
                itemProjectResponseDto =
                        actionsForItemInDeposit(itemResponseDto, projectSimpleReponseDto, itemProjectResponseDto);
            }
            case USING -> {
                itemProjectResponseDto =
                        actionsIfItemIsBeingUsed(itemInProject, projectStarted, projectSimpleReponseDto,
                                itemResponseDto, itemProjectResponseDto);
            }
            case NOT_RETURNED -> {
                if (itemInProject && !projectStarted) {
                    throw new InternalException("Acción imposible");
                }
                throw new BadRequestException("¡El artículo ya está en otro proyecto!");
            }
        }
        return itemProjectResponseDto;
    }

    private ItemProjectResponseDto actionsIfItemIsBeingUsed(boolean itemInProject, boolean projectStarted,
                                                            ProjectSimpleReponseDto projectSimpleReponseDto,
                                                            ItemResponseDto itemResponseDto,
                                                            ItemProjectResponseDto itemProjectResponseDto) {
        if (itemInProject && !projectStarted) {

            itemService.updateLocation(LocationEnum.IN_DEPOSIT, itemResponseDto);

            itemProjectResponseDto =
                    itemProjectService.changeStatusItemProject(itemResponseDto, projectSimpleReponseDto,
                            ItemProjectStatus.DISABLED, itemProjectResponseDto);

        } else {
            throw new BadRequestException("¡El artículo ya está en otro proyecto!");
        }
        return itemProjectResponseDto;
    }

    private ItemProjectResponseDto actionsForItemInDeposit(ItemResponseDto itemResponseDto,
                                                           ProjectSimpleReponseDto projectSimpleReponseDto,
                                                           ItemProjectResponseDto itemProjectResponseDto) {

        itemService.updateLocation(LocationEnum.USING, itemResponseDto);

        List<ProductProjectWithModelResponseDto> productProjectWithModelResponseDtos =
                productProjectService.getByProductAndProjectId(itemResponseDto.getProductId(),
                        projectSimpleReponseDto.getProjectId());

        if (Objects.isNull(itemProjectResponseDto)) {
            return itemProjectService.createItemProject(itemResponseDto, projectSimpleReponseDto.getProjectId(),
                    productProjectWithModelResponseDtos);
        }

        ItemProjectEntity itemProjectEntity = itemProjectMapper.toEntity(itemProjectResponseDto);

        itemProjectService.checkIfItemLimitIsSurpassed(itemResponseDto, projectSimpleReponseDto.getProjectId(),
                productProjectWithModelResponseDtos, itemProjectEntity);
        return itemProjectService.changeStatusItemProject(itemResponseDto, projectSimpleReponseDto,
                ItemProjectStatus.ENABLED, itemProjectResponseDto);
    }

    @Override
    @Transactional
    public ItemProjectResponseDto singleItemReturn(Long idProject, Long idItem) {

        ItemProjectResponseDto itemProjectResponseDto =
                itemProjectService.getItemProjectByProjectIdAndItemId(idProject, idItem);

        if (Objects.isNull(itemProjectResponseDto) ||
                itemProjectResponseDto.getStatus().equals(ItemProjectStatus.DISABLED)) {
            throw new BadRequestException("¡El artículo no está en el proyecto!");
        }

        ItemResponseDto itemResponseDto = itemService.getItem(idItem);

        if (itemResponseDto.getLocation().equals(LocationEnum.IN_DEPOSIT)) {
            throw new BadRequestException("¡El artículo ya está en el depósito!");
        }

        itemResponseDto = itemService.updateLocation(LocationEnum.IN_DEPOSIT, itemResponseDto);

        itemProjectResponseDto.setItemBoughtAt(itemResponseDto.getBoughtAt());
        itemProjectResponseDto.setItemPriceBought(itemResponseDto.getPriceBought());
        itemProjectResponseDto.setItemDescription(itemResponseDto.getDescription());
        itemProjectResponseDto.setItemStatus(itemResponseDto.getStatus());
        itemProjectResponseDto.setProductId(itemResponseDto.getProductId());
        ProductResponseDto productResponseDto = productService.GetProduct(itemResponseDto.getProductId());
        itemProjectResponseDto.setProductModel(productResponseDto.getModel());

        solveReturnNotification(idProject);

        return itemProjectResponseDto;
    }

    @Override
    public List<ProjectParticipatedResponseDto> getProjectsByClient(Long id) {

        return projectRepository.findByClientId(id);
    }

    private BigDecimal getProductsBudget(ProjectSimpleReponseDto projectResponseDto,
                                         List<ProductInProjectResponseDto> productsInProject) {
        LocalDateTime startDate = projectResponseDto.getStartDate();
        LocalDateTime endDate = projectResponseDto.getEndDate();

        long days = ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate());
        BigDecimal totalInProductsPerDay = BigDecimal.valueOf(0);

        for (ProductInProjectResponseDto product : productsInProject) {
            totalInProductsPerDay = totalInProductsPerDay.add(product.getRentPrice());
        }

        return totalInProductsPerDay.multiply(BigDecimal.valueOf(days));
    }

    private BigDecimal getTransportationBudget(ProjectSimpleReponseDto projectResponseDto) {
        List<GeneralParameterResponseDto> generalParameterListReponseDto =
                generalParameterService.GetParametersByKey(KM_PARAMETER).getList();

        if (generalParameterListReponseDto.isEmpty()) {
            throw new InternalException("¡No hay un parámetro con ésta key!");
        }

        if (Objects.isNull(projectResponseDto.getEvent().getDistance())) {
            throw new InternalException("¡El evento no tiene distancia asignada!");
        }

        GeneralParameterResponseDto parameterCost = generalParameterListReponseDto.getLast();
        BigDecimal costPerKm = new BigDecimal(parameterCost.getValue());

        Double distance = projectResponseDto.getEvent().getDistance();

        return costPerKm.multiply(BigDecimal.valueOf(distance));
    }

    private boolean allItemsReturned(Long projectId) {

        List<ItemEntity> itemEntities = itemService.getByItemProject(projectId);

        return itemEntities.stream().allMatch(i -> i.getLocation().equals(LocationEnum.IN_DEPOSIT));
    }

    private boolean allItemsExited(Long projectId) {

        List<ItemEntity> itemEntities = itemService.getByItemProject(projectId);

        List<ProductInProjectResponseDto> productsInProject =
                productProjectService.getProductsInProject(projectId).getProducts();

        Integer amountOfProducts = 0;

        for (ProductInProjectResponseDto pp : productsInProject) {
            amountOfProducts += pp.getAmount();
        }

        boolean itemsOutOfDeposit =
                itemEntities.stream().noneMatch(i -> i.getLocation().equals(LocationEnum.IN_DEPOSIT));

        return amountOfProducts.equals(itemEntities.size()) && itemsOutOfDeposit;
    }

    //notification methods -----------------------------------------------------------------

    private void sendProjectNotification(String title, String body, Boolean isSolved, LocalDateTime expiresAt,
                                         NotificationTypeEnum type, Long entityId, String action) {

        NotificationRequestDto notificationRequestDto =
                new NotificationRequestDto(title, body, isSolved, expiresAt, type, entityId, action);

        List<UserResponseDto> users = userService.findActiveUsers();

        notificationService.createNotificationAndLinkUsers(notificationRequestDto, users);
    }

    private List<NotificationEntity> getProjectNotificationsByIdAndAction(Long idProject, String action,
                                                                          Boolean isSolved) {
        return notificationService.findNotificationByActionAndEntity(NotificationTypeEnum.PROJECT, idProject, action,
                isSolved);
    }

    private void sendPaymentNeededNotification(ProjectEntity projectEntity) {
        if (!projectEntity.getPaymentStatus().equals(PaymentStatusEnum.PAID)) {
            sendProjectNotification(PAYMENT_NEEDED_TITLE, PAYMENT_NEEDED_BODY.formatted(projectEntity.getName()), false,
                    projectEntity.getEndDate(), NotificationTypeEnum.PROJECT, projectEntity.getProjectId(),
                    PAY_PROJECT_NOTIFICATION_ACTION);
        }
    }

    private void sendProjectNotificationCompleted(ProjectEntity projectEntity) {
        sendProjectNotification(PROJECT_ENDED_TITLE, PROJECT_ENDED_CORRECTLY_BODY.formatted(projectEntity.getName()),
                true, null, NotificationTypeEnum.PROJECT, projectEntity.getProjectId(),
                NOT_REQUIRED_NOTIFICATION_ACTION);
    }

    private void solvePaymentNeededNotification(ProjectEntity projectEntity) {
        List<NotificationEntity> notifications =
                getProjectNotificationsByIdAndAction(projectEntity.getProjectId(), PAY_PROJECT_NOTIFICATION_ACTION,
                        false);

        if (!notifications.isEmpty() && projectEntity.getPaymentStatus().equals(PaymentStatusEnum.PAID)) {
            notifications.forEach(n -> n.setIsSolved(true));
            notificationService.update(notifications);
        }
    }

    private void solveReturnNotification(Long idProject) {

        List<NotificationEntity> notifications =
                getProjectNotificationsByIdAndAction(idProject, RETURN_ITEMS_NOTIFICATION_ACTION, false);

        if (!notifications.isEmpty() && allItemsReturned(idProject)) {
            notifications.forEach(n -> n.setIsSolved(true));
            notificationService.update(notifications);
        }

    }

    private void solveSendNotification(Long idProject) {

        List<NotificationEntity> notifications =
                getProjectNotificationsByIdAndAction(idProject, SEND_ITEMS_NOTIFICATION_ACTION, false);

        if (!notifications.isEmpty() && allItemsExited(idProject)) {
            notifications.forEach(n -> n.setIsSolved(true));
            notificationService.update(notifications);
        }

    }

}
