package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Analytics.AmountRentedResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Analytics.RangeResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Analytics.RentedProductsAmountResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.repositories.ProductRepository;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final ProductRepository productRepository;

    @Override
    public AmountRentedResponseDto getMostRentedProducts(LocalDate start, LocalDate end, Integer limit) {

        if (Objects.isNull(limit) || limit <= 0) {
            limit = 20;
        }

        List<RentedProductsAmountResponseDto>
                rentedProducts = productRepository.findMostUsedProducts(start, end, limit);

        RangeResponseDto rangeResponseDto = new RangeResponseDto(start, end);

        return new AmountRentedResponseDto(rangeResponseDto, rentedProducts);
    }
}
