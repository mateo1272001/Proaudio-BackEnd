package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Analytics.AmountRentedResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Analytics.BalanceAnalyticsResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Analytics.RentedMonthlyAvgResponseDto;

import java.time.LocalDate;

public interface AnalyticsService {
    AmountRentedResponseDto getMostRentedProducts(LocalDate start, LocalDate end, Integer limit);

    BalanceAnalyticsResponseDto getProductBalance(Long productId);

    RentedMonthlyAvgResponseDto getMonthlyProjectAverage(Integer years);
}
