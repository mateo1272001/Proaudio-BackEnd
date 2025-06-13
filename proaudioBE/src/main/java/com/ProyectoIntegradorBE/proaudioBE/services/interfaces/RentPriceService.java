package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PriceReponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PriceRequestDto;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface RentPriceService {

    PriceReponseDto createPrice(PriceRequestDto priceRequestDto);

    List<PriceReponseDto> createPrices(List<PriceRequestDto> priceRequestDto, Long productId);

    List<PriceReponseDto> updatePrices(List<PriceRequestDto> priceRequestDto, Long productId) throws BadRequestException;

    PriceReponseDto DeletePrice(Long id) throws BadRequestException;
}
