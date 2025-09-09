package com.ProyectoIntegradorBE.proaudioBE.Utils;

import com.ProyectoIntegradorBE.proaudioBE.enums.ItemStatusEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProjectStatusEnum;

import java.util.List;

public final class AppConstants {

    //projects
    public static final List<ProjectStatusEnum> EXIT_POSSIBLE_STATUS =
            List.of(ProjectStatusEnum.ON_COURSE, ProjectStatusEnum.CONFIRMED);

    public static final List<ProjectStatusEnum> CLIENT_CURRENTLY_PRESENT =
            List.of(ProjectStatusEnum.PLANNED, ProjectStatusEnum.CONFIRMED, ProjectStatusEnum.ON_COURSE);

    public static final List<ProjectStatusEnum> PROJECT_STARTED_STATUS =
            List.of(ProjectStatusEnum.ON_COURSE, ProjectStatusEnum.EXPIRED, ProjectStatusEnum.COMPLETED);

    public static final List<ProjectStatusEnum> MANUALLY_UPDATETABLE_STATUSES =
            List.of(ProjectStatusEnum.PLANNED, ProjectStatusEnum.CONFIRMED, ProjectStatusEnum.DISCARDED);

    public static final String KM_PARAMETER = "km_cost";

    //notifications -----------------------------------------------------------------------------------------
    //------actions
    public static final String NOT_REQUIRED_NOTIFICATION_ACTION = "NOT_REQUIRED";

    public static final String RETURN_ITEMS_NOTIFICATION_ACTION = "RETURN_ITEMS";

    public static final String PAY_PROJECT_NOTIFICATION_ACTION = "PAY_PROJECT";

    public static final String SEND_ITEMS_NOTIFICATION_ACTION = "SEND_ITEMS";

    //titles
    public static final String PROJECT_STARTED_TITLE = "El proyecto ha empezado";

    public static final String PROJECT_ENDED_TITLE = "El proyecto ha terminado";

    public static final String PROJECT_EXPIRED_TITLE = "El proyecto ha expirado";

    public static final String PAYMENT_NEEDED_TITLE = "El proyecto debe ser pagado";

    public static final String PROJECT_DISCARDED_TITLE = "El proyecto fue descartado";

    //bodies
    public static final String PROJECT_STARTED_BODY =
            "El proyecto %s acaba de empezar. No olvides enviar los artículos correspondientes.";

    public static final String PROJECT_ENDED_CORRECTLY_BODY = "Felicidades. El proyecto %s ha terminado correctamente.";

    public static final String PROJECT_EXPIRED_BODY =
            "El proyecto %s llegó a su fin y aun no se han retornado todos los artículos. Retornalos todos al depósito para solucionar esta alerta.";

    public static final String PAYMENT_NEEDED_BODY =
            "El proyecto %s ha terminado y aún no se ha cobrado. Asegúrate de que éste sea pagado para solucionar esta alerta.";

    public static final String PROJECT_DISCARDED_BODY =
            "Llegó la fecha de inicio del proyecto %s y no se confirmó. Si se desea ejecutar, cambiá las fechas del proyecto y pasalo a estado \"CONFIRMADO\".";

    // ------------------------------------------------------------------------------------------------------

    //items
    public static final List<ItemStatusEnum> ITEM_AVAILABLE_STATUS =
            List.of(ItemStatusEnum.CREATED, ItemStatusEnum.GOOD, ItemStatusEnum.WITH_DETAILS);


    //products
    public static final Long BRAND_TAG_FATHER = 1L;

    public static final String BRAND_TAG_KEY = "Marca";

}
