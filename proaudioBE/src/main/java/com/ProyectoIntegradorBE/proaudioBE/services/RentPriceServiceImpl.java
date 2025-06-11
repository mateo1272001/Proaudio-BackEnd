package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.Utils.CollectionUtils;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PriceReponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PriceRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.RentPriceEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.mappers.RentPriceMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.RentPriceRepository;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.RentPriceService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class RentPriceServiceImpl implements RentPriceService {

    private final RentPriceRepository rentPriceRepository;

    private final RentPriceMapper rentPriceMapper;

    @Override
    public List<PriceReponseDto> createPrices(List<PriceRequestDto> priceRequestListDto, Long productId) {

        List <RentPriceEntity> rentPriceEntities = rentPriceMapper.toEntityList(priceRequestListDto);

        rentPriceEntities.forEach(price -> {
            price.setProductId(productId);
            price.setStatus(BasicEnumStatus.ENABLED);
        });

        rentPriceEntities = CollectionUtils.toList(rentPriceRepository.saveAll(rentPriceEntities));

        return rentPriceMapper.toDtoList(rentPriceEntities);
    }

    @Override
    public List<PriceReponseDto> updatePrices(List<PriceRequestDto> priceRequestListDto, Long productId)
            throws BadRequestException {

        List<Long> rentPriceIds = priceRequestListDto.stream().map(PriceRequestDto::getRentPriceId).toList();

        List <RentPriceEntity> rentPriceEntities =
                CollectionUtils.toList(rentPriceRepository.findAllById(rentPriceIds));

        for(PriceRequestDto priceRequestDto : priceRequestListDto) {

            RentPriceEntity rentPriceEntity = rentPriceEntities
                    .stream()
                    .filter(rpe -> rpe
                            .getRentPriceId()
                            .equals(priceRequestDto.getRentPriceId()))
                    .findFirst()
                    .orElseThrow(() ->
                            new BadRequestException(String.format("Rent Price con ID %s no encontrado: ", productId)));

            rentPriceEntity.setStatus(priceRequestDto.getStatus());

        }

        rentPriceEntities = CollectionUtils.toList(rentPriceRepository.saveAll(rentPriceEntities));

        return rentPriceMapper.toDtoList(rentPriceEntities);
    }

    @Override
    public PriceReponseDto createPrice(PriceRequestDto priceRequestDto, Long productId) {

        RentPriceEntity rentPriceEntity = new RentPriceEntity();
        rentPriceEntity.setProductId(productId);
        rentPriceEntity.setValue(priceRequestDto.getValue());
        rentPriceEntity.setDescription(priceRequestDto.getDescription());
        rentPriceEntity.setStatus(BasicEnumStatus.ENABLED);

        rentPriceEntity = rentPriceRepository.save(rentPriceEntity);
        return rentPriceMapper.toDto(rentPriceEntity);
    }

    public List<PriceReponseDto> findRentPriceByProductId(Long id) throws BadRequestException {
        List<RentPriceEntity> rentPriceEntities = rentPriceRepository.findByProductId(id).stream().toList();
        return rentPriceMapper.toDtoList(rentPriceEntities);
    }

}
