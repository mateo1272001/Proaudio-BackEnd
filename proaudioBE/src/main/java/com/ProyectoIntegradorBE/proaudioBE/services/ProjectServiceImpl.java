package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Event.EventResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Expense.ExpenseRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Expense.ExpenseResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.ItemResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ProductProject.ProductProjectRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ProductProject.ProductProjectResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProductsProjectResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProjectProductRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProjectRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProjectResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ProjectEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.*;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.EventMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.ProjectRepository;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final EventService eventService;

    private final ItemService itemService;

    private final ProductService productService;

    private final ExpenseService expenseService;

    private final ProductProjectService productProjectService;

    private final EventMapper eventMapper;

    private final ProjectRepository projectRepository;

    private static ProjectResponseDto makeProjectResponseDto(ProjectRequestDto request, ProjectEntity projectEntity,
                                                             EventResponseDto eventResponseDto,
                                                             List<ProductsProjectResponseDto> products,
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
    public ProjectResponseDto CreateProject(ProjectRequestDto request) {

        EventResponseDto eventResponseDto = Objects.nonNull(request.getEvent().getEventId()) ?
                eventService.GetEvent(request.getEvent().getEventId()) : eventService.CreateEvent(request.getEvent());

        ProjectEntity projectEntity = setProjectEntity(request, eventResponseDto);

        projectEntity = projectRepository.save(projectEntity);

        List<ProductsProjectResponseDto> products = new ArrayList<>();
        if (Objects.nonNull(request.getProducts())) {
            products = setProducts(request.getProducts(), projectEntity.getProjectId());
        }

        List<ExpenseResponseDto> expenses = new ArrayList<>();
        if (Objects.nonNull(request.getExpenses())) {
            expenses = setExpenses(request.getExpenses(), projectEntity.getProjectId());
        }

        return makeProjectResponseDto(request, projectEntity, eventResponseDto, products, expenses);
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

    private List<ProductsProjectResponseDto> setProducts(List<ProjectProductRequestDto> productRequests,
                                                         Long projectId) {

        List<ProductsProjectResponseDto> productsResponse = new ArrayList<>();

        List<Long> productIds = productRequests.stream().map(ProjectProductRequestDto::getProductId).toList();
        List<ItemResponseDto> items = itemService.GetByProductIds(productIds);

        for (ProjectProductRequestDto productRequest : productRequests) {

            List<ItemResponseDto> itemsOfProduct =
                    items.stream().filter(i -> i.getProductId().equals(productRequest.getProductId())).toList();

            if (itemsOfProduct.size() < productRequest.getAmount()) {
                throw new BadRequestException("¡No hay suficientes artículos disponibles!");
            }

            createProductProject(productRequest, projectId);

            productsResponse.add(makeProductResponse(productRequest, projectId));

        }

        return productsResponse;
    }

    private ProductsProjectResponseDto makeProductResponse(ProjectProductRequestDto productRequest, Long projectId) {
        ProductResponseDto product = productService.GetProduct(productRequest.getProductId());

        if (!product.getStatus().equals(ProductStatus.ACTIVE)) {
            throw new BadRequestException("¡Este producto no está disponible!");
        }

        ProductsProjectResponseDto productsProjectResponseDto = new ProductsProjectResponseDto();
        productsProjectResponseDto.setProductId(product.getProductId());
        productsProjectResponseDto.setModel(product.getModel());
        productsProjectResponseDto.setComments(productsProjectResponseDto.getComments());
        productsProjectResponseDto.setStatus(product.getStatus());
        productsProjectResponseDto.setCreatedAt(product.getCreatedAt());
        productsProjectResponseDto.setUpdatedAt(product.getUpdatedAt());
        productsProjectResponseDto.setReplacementValue(product.getReplacementValue());
        productsProjectResponseDto.setAmount(productRequest.getAmount());
        return productsProjectResponseDto;
    }

    private ProductProjectResponseDto createProductProject(ProjectProductRequestDto productRequest, Long projectId) {
        ProductProjectRequestDto productProjectRequestDto = new ProductProjectRequestDto();
        productProjectRequestDto.setProductId(productRequest.getProductId());
        productProjectRequestDto.setProjectId(projectId);
        productProjectRequestDto.setAmount(productRequest.getAmount());
        productProjectRequestDto.setStatus(BasicEnumStatus.ENABLED);

        return productProjectService.createProductProject(productProjectRequestDto);
    }

    private BigDecimal CalculateCostAddition(BigDecimal costAddition) {
        return costAddition;
    }
}
