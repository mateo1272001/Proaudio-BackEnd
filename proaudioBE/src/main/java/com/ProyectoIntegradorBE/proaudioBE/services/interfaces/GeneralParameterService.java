package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.GeneralParmeters.GeneralParameterListReponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.GeneralParmeters.GeneralParameterRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.GeneralParmeters.GeneralParameterResponseDto;

public interface GeneralParameterService {
    GeneralParameterResponseDto CreateParameter(GeneralParameterRequestDto generalParameterRequestDto);

    GeneralParameterResponseDto UpdateParameter(Long id, GeneralParameterRequestDto generalParameterRequestDto);

    GeneralParameterResponseDto DeleteParameter(Long id);

    GeneralParameterResponseDto GetParameter(Long id);

    GeneralParameterListReponseDto GetAllParameters();

    GeneralParameterListReponseDto GetParametersByKey(String key);
}
