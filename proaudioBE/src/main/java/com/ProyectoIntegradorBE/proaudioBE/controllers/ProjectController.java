package com.ProyectoIntegradorBE.proaudioBE.controllers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.*;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ProductProjectService;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;

    private final ProductProjectService productProjectService;

    @PostMapping()
    private ProjectResponseDto createProject(@RequestBody ProjectRequestDto request) {

        return projectService.createProject(request);
    }

    @PutMapping("{id}")
    private ProjectSimpleReponseDto updateProject(@PathVariable Long id, @RequestBody ProjectRequestDto request) {

        return projectService.updateProject(id, request);
    }

    @GetMapping("{id}")
    private ProjectSimpleReponseDto getProject(@PathVariable Long id) {

        return projectService.getProject(id);
    }

    @GetMapping("/types")
    private ProjectTypesResponseDto getProjectTypes() {
        return projectService.getProjectTypes();
    }

    @GetMapping("/possible/status/{id}")
    private ProjectStatusesDto getPossibleStatusByProjectId(@PathVariable Long id) {
        return projectService.getPossibleStatusByProjectId(id);
    }

    @GetMapping("/possible/status")
    private ProjectStatusesDto getPossibleStatusForStartingProject() {
        return projectService.getPossibleStatusForStartingProject();
    }

    @GetMapping("/status/all")
    private ProjectStatusesDto getAllStatus() {
        return projectService.getAllStatus();
    }

    @GetMapping("/possible/payment/status/{id}")
    private ProjectPaymentStatusesDto getPossiblePaymentStatusByProjectId(@PathVariable Long id) {
        return projectService.getPossiblePaymentStatusByProjectId(id);
    }

    @GetMapping("/payment/status/all")
    private ProjectPaymentStatusesDto getAllPaymentStatus() {
        return projectService.getAllPaymentStatus();
    }

    @GetMapping("/running/status/all")
    private RunningStatusResponseDto getAllRunningStatus() {
        return projectService.getAllRunningEnum();
    }


    @GetMapping("/details/{id}")
    private ProjectDetailsResponseDto getProjectDetails(@PathVariable Long id) {
        return projectService.getProjectDetails(id);
    }

    @GetMapping("/all")
    private ProjectListResponseDto getProjectList(@RequestParam(defaultValue = "0") Integer page,
                                                  @RequestParam(defaultValue = "10") Integer size,
                                                  @RequestParam(required = false, defaultValue = "start_date")
                                                  String sortBy, @RequestParam(required = false) String direction,
                                                  @RequestParam(required = false) List<String> filterStatus,
                                                  @RequestParam(required = false) String filterPaymentStatus,
                                                  @RequestParam(required = false) String name) {
        return projectService.getProjectList(page, size, sortBy, direction, filterStatus, filterPaymentStatus, name);
    }

    @GetMapping("{id}/products")
    private ProductsInProjectResponseDto getProductsInProject(@PathVariable Long id) {
        projectService.getProject(id);
        return productProjectService.getProductsInProject(id);
    }


    @PostMapping("/{id}/budget")
    public ResponseEntity<byte[]> makeProjectBudget(@PathVariable Long id) {
        byte[] pdfBytes = projectService.generateBudget(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename("presupuesto.pdf").build());

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
