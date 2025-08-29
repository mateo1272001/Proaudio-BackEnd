package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Analytics.AmountRentedResponseDto;

import java.time.LocalDate;

public interface AnalyticsService {
    AmountRentedResponseDto getMostRentedProducts(LocalDate start, LocalDate end, Integer limit);
}
