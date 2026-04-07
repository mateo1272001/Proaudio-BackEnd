package com.ProyectoIntegradorBE.proaudioBE.Utils;

import java.util.Objects;

public class StringUtils {

    public static Boolean isEmpty(String s) {

        return Objects.isNull(s) || s.isEmpty();

    }

    public static Boolean isNotEmpty(String s) {

        return Objects.nonNull(s) && !s.isEmpty();

    }

}
