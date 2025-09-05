package com.ProyectoIntegradorBE.proaudioBE.repositories.customrepositories;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Analytics.ProjectMonthlyAvg;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProjectRepositoryCustomImpl implements ProjectRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    public List<ProjectMonthlyAvg> getMonthlyProjectsAvg(Integer years) {

        LocalDate now = LocalDate.now();
        LocalDate startMonth = now.minusYears(years).withDayOfMonth(1);
        LocalDate endMonth = now.withDayOfMonth(1);

        String sql = """
                WITH RECURSIVE all_months AS (
                    SELECT CAST(? AS DATE) AS month_start
                    UNION ALL
                    SELECT DATE_ADD(month_start, INTERVAL 1 MONTH)
                    FROM all_months
                    WHERE month_start < CAST(? AS DATE)
                ),
                projects_per_month AS (
                    SELECT
                        am.month_start,
                        MONTH(am.month_start) AS month_number,
                        COALESCE(COUNT(p.project_id), 0) AS project_count
                    FROM all_months am
                    LEFT JOIN project p
                      ON p.start_date <= LAST_DAY(am.month_start)
                     AND p.end_date   >= am.month_start
                     AND p.status NOT IN ('PLANNED', 'DISCARDED')
                    GROUP BY am.month_start
                ),
                monthly_avg AS (
                    SELECT
                        month_number,
                        ROUND(AVG(project_count), 2) AS monthly_average
                    FROM projects_per_month
                    GROUP BY month_number
                )
                SELECT
                    ELT(month_number,
                        'Enero','Febrero','Marzo','Abril','Mayo','Junio',
                        'Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre') AS month,
                    monthly_average
                FROM monthly_avg
                ORDER BY month_number
                """;

        return jdbcTemplate.query(sql, ps -> {
            ps.setObject(1, java.sql.Date.valueOf(startMonth));
            ps.setObject(2, java.sql.Date.valueOf(endMonth));
        }, (rs, rowNum) -> new ProjectMonthlyAvg(rs.getString("month"), rs.getBigDecimal("monthly_average")));
    }
}
