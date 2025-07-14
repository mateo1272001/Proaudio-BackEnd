package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.GeneralParmeters.GeneralParameterListReponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.GeneralParmeters.GeneralParameterRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.GeneralParmeters.GeneralParameterResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.GeneralParameterEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.ParameterNotFoundException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.GeneralParameterMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.GeneralParameterRepository;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.GeneralParameterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class GeneralParameterServiceImpl implements GeneralParameterService {

    private final GeneralParameterRepository generalParameterRepository;

    private final GeneralParameterMapper generalParameterMapper;

    @Override
    public GeneralParameterResponseDto CreateParameter(GeneralParameterRequestDto generalParameterRequestDto) {

        if (!this.GetParametersByKey(generalParameterRequestDto.getParameterKey()).getList().isEmpty()) {
            throw new BadRequestException("¡Ya existe un parametro con esa key!");
        }

        GeneralParameterEntity generalParameterEntity = new GeneralParameterEntity();
        generalParameterEntity.setParameterKey(generalParameterRequestDto.getParameterKey());
        generalParameterEntity.setValue(generalParameterRequestDto.getValue());
        generalParameterEntity.setStatus(BasicEnumStatus.ENABLED);

        generalParameterEntity = generalParameterRepository.save(generalParameterEntity);

        return generalParameterMapper.toDto(generalParameterEntity);
    }

    @Override
    public GeneralParameterResponseDto UpdateParameter(Long id, GeneralParameterRequestDto generalParameterRequestDto) {

        GeneralParameterEntity generalParameterEntity =
                generalParameterRepository.findByGeneralParametersIdAndStatus(id, BasicEnumStatus.ENABLED)
                        .orElseThrow(() -> new ParameterNotFoundException(id));

        generalParameterEntity.setParameterKey(generalParameterRequestDto.getParameterKey());
        generalParameterEntity.setValue(generalParameterRequestDto.getValue());
        generalParameterEntity.setStatus(BasicEnumStatus.ENABLED);

        generalParameterEntity = generalParameterRepository.save(generalParameterEntity);

        return generalParameterMapper.toDto(generalParameterEntity);
    }

    @Override
    public GeneralParameterResponseDto DeleteParameter(Long id) {

        GeneralParameterEntity generalParameterEntity =
                generalParameterRepository.findByGeneralParametersIdAndStatus(id, BasicEnumStatus.ENABLED)
                        .orElseThrow(() -> new ParameterNotFoundException(id));

        generalParameterEntity.setStatus(BasicEnumStatus.DISABLED);
        generalParameterRepository.save(generalParameterEntity);

        return generalParameterMapper.toDto(generalParameterEntity);
    }

    @Override
    public GeneralParameterResponseDto GetParameter(Long id) {

        GeneralParameterEntity generalParameterEntity =
                generalParameterRepository.findByGeneralParametersIdAndStatus(id, BasicEnumStatus.ENABLED)
                        .orElseThrow(() -> new ParameterNotFoundException(id));

        return generalParameterMapper.toDto(generalParameterEntity);
    }

    @Override
    public GeneralParameterListReponseDto GetAllParameters() {

        List<GeneralParameterEntity> generalParameterEntities =
                StreamSupport.stream(generalParameterRepository.findAll().spliterator(), false).toList();

        GeneralParameterListReponseDto generalParameterListReponseDto = new GeneralParameterListReponseDto();
        generalParameterListReponseDto.setList(generalParameterMapper.toDtoList(generalParameterEntities));

        return generalParameterListReponseDto;
    }

    public GeneralParameterListReponseDto GetParametersByKey(String key) {

        List<GeneralParameterEntity> generalParameterEntities =
                generalParameterRepository.findByParameterKeyAndStatus(key, BasicEnumStatus.ENABLED);

        return new GeneralParameterListReponseDto(generalParameterMapper.toDtoList(generalParameterEntities));

    }

}
