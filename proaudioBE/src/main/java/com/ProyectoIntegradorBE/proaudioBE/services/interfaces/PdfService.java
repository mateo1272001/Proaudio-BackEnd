package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProductInProjectResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProjectSimpleReponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.User.UserResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface PdfService {
    byte[] generateProjectPdf(ProjectSimpleReponseDto projectResponseDto,
                              List<ProductInProjectResponseDto> productsInProject, BigDecimal totalBudget,
                              UserResponseDto userResponseDto);
}
