package com.ProyectoIntegradorBE.proaudioBE.repositories.customrepositories;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Analytics.ProjectMonthlyAvg;

import java.util.List;

public interface ProjectRepositoryCustom {

    List<ProjectMonthlyAvg> getMonthlyProjectsAvg(Integer years);

}
