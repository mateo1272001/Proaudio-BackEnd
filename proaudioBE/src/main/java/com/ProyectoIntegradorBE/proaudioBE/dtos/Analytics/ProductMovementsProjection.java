package com.ProyectoIntegradorBE.proaudioBE.dtos.Analytics;

import java.math.BigDecimal;

public interface ProductMovementsProjection {

    Long getItemId();

    BigDecimal getAmount();

    java.sql.Date getDate();

    String getAction();

}
