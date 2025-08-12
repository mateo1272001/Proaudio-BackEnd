package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Client.ClientResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProductInProjectResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProjectSimpleReponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.User.UserResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProjectTypeEnum;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService {

    private final TemplateEngine templateEngine;

    @Override
    public byte[] generateProjectPdf(ProjectSimpleReponseDto projectResponseDto,
                                     List<ProductInProjectResponseDto> productsInProject, BigDecimal totalBudget,
                                     UserResponseDto userResponseDto, ClientResponseDto clientResponseDto) {

        //company information
        //todo MAKE INFORMATION DYNAMIC
        Map<String, Object> companyInfo = new HashMap<>();
        companyInfo.put("name", "BARDIER MORAÑA SANTIAGO Y LUJAMBIO JUAN DANIEL");
        companyInfo.put("address", "CHUY 3437 AP 501");
        companyInfo.put("phone", "098 740585 / 098 672279");
        companyInfo.put("email", "santi@proaudiochannels.com");
        companyInfo.put("rut", "219549720018. / LITERAL E");

        //project data
        Map<String, Object> project = new HashMap<>();
        project.put("clientName", clientResponseDto.getName());
        project.put("eventName", projectResponseDto.getEvent().getName());
        project.put("ubication", projectResponseDto.getEvent().getAddress());
        project.put("paymentMethod", "TRANSFERENCIA");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startDate = projectResponseDto.getStartDate();
        LocalDateTime endDate = projectResponseDto.getEndDate();
        project.put("eventDateStart", startDate.format(formatter));
        project.put("eventDateEnd", endDate.format(formatter));
        project.put("dateValidUntil", endDate.plusDays(10).format(formatter));
        project.put("hoursWorked", ChronoUnit.HOURS.between(startDate, endDate));


        //product list
        List<Map<String, Object>> products = new ArrayList<>();
        for (ProductInProjectResponseDto product : productsInProject) {
            Map<String, Object> prod = new HashMap<>();
            prod.put("modelName", product.getModel());
            prod.put("amount", product.getAmount());
            prod.put("replacementValue",
                    projectResponseDto.getProjectType().equals(ProjectTypeEnum.RENT) ? product.getReplacementValue() :
                            "");

            products.add(prod);

        }
        project.put("products", products);

        //total budget
        project.put("totalBudget", totalBudget);

        //user info
        project.put("user", userResponseDto.getName());
        project.put("userPhone", userResponseDto.getPhone_number());
        project.put("email", userResponseDto.getEmail());

        //dynamic titles
        Map<String, Object> dynamicTitles = new HashMap<>();
        dynamicTitles.put("replacementValueTitle",
                projectResponseDto.getProjectType().equals(ProjectTypeEnum.RENT) ? "VALOR DE REEMPLAZO" : "");


        // Cargar el HTML con Thymeleaf
        Context context = new Context();
        context.setVariable("project", project);
        context.setVariable("companyInfo", companyInfo);
        context.setVariable("dynamicTitles", dynamicTitles);


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

    private String convertirImagenABase64(String path) throws IOException {
        ClassPathResource imgFile = new ClassPathResource(path);
        byte[] bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
        return Base64.getEncoder().encodeToString(bytes);
    }


}
