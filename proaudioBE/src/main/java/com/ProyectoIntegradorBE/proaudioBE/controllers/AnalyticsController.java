package com.ProyectoIntegradorBE.proaudioBE.controllers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Analytics.AmountRentedResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Analytics.BalanceAnalyticsResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Analytics.RentedMonthlyAvgResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping({"/balance", "/balance/{productId}"})
    BalanceAnalyticsResponseDto getMostRentedProducts(@PathVariable(required = false) Long productId) {
        return analyticsService.getProductBalance(productId);
    }

    @GetMapping("monthly/projects")
    RentedMonthlyAvgResponseDto getMonthlyProjectAverage(@RequestParam(required = false) Integer years) {
        return analyticsService.getMonthlyProjectAverage(years);
    }


}
