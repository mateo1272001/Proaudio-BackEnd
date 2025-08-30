package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Analytics.*;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.repositories.ProductRepository;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.AnalyticsService;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final ProductService productService;

    private final ProductRepository productRepository;

    @Override
    public AmountRentedResponseDto getMostRentedProducts(LocalDate start, LocalDate end, Integer limit) {

        if (Objects.isNull(limit) || limit <= 0) {
            limit = 20;
        }

        List<RentedProductsAmountDto>
                rentedProducts = productRepository.findMostUsedProducts(start, end, limit);

        RangeDto rangeDto = new RangeDto(start, end);

        return new AmountRentedResponseDto(rangeDto, rentedProducts);
    }

    @Override
    public BalanceAnalyticsResponseDto getProductBalance(Long productId) {

        if (Objects.isNull(productId)) {
            throw new BadRequestException("Debes seleccionar un product para ver su balance");
        }

        ProductResponseDto product = productService.GetProduct(productId);

        RequestedProductDto requestedProductDto = new RequestedProductDto(product.getProductId(), product.getModel());

        List<ProductMovementsProjection> movementsProjection =
                productRepository.findProductMovements(product.getProductId());

        List<ProductMovementsDto> movementsResponse = new ArrayList<>();

        BalanceDto balance = new BalanceDto();

        for (ProductMovementsProjection movement : movementsProjection) {

            ProductMovementsDto productMovementsDto =
                    new ProductMovementsDto(movement.getItemId(), movement.getAmount(),
                            movement.getDate().toLocalDate(), movement.getAction());

            movementsResponse.add(productMovementsDto);

            if (movement.getAmount().compareTo(BigDecimal.ZERO) >= 0) {
                balance.setEarnings(balance.getEarnings().add(movement.getAmount()));
                balance.setBalance(balance.getBalance().add(movement.getAmount()));
            } else {
                balance.setExpenses(balance.getExpenses().add(movement.getAmount()));
                balance.setBalance(balance.getBalance().add(movement.getAmount()));
            }

        }

        return new BalanceAnalyticsResponseDto(requestedProductDto, movementsResponse, balance);
    }
}
