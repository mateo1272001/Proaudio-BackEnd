package com.ProyectoIntegradorBE.proaudioBE.controllers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Analytics.AmountRentedResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/rented")
    AmountRentedResponseDto getMostRentedProducts(@RequestParam(required = false) LocalDate start,
                                                  @RequestParam(required = false) LocalDate end,
                                                  @RequestParam(required = false) Integer limit) {
        return analyticsService.getMostRentedProducts(start, end, limit);
    }


}
