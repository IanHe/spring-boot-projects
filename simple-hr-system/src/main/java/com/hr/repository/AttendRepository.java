package com.hr.repository;

import com.hr.domain.Attend;
import com.hr.domain.AttendType;
import com.hr.domain.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendRepository extends JpaRepositoryImplementation<Attend, Long> {
    @Query("select a from Attend as a where a.employee = (:employee) and substring(a.dutyDay, 0, 7) = (:month)")
    List<Attend> findByEmployeeAndMonth(@Param("employee") Employee employee, String month);

    List<Attend> findByEmployeeAndDutyDay(Employee employee, String dutyDay);

    Optional<Attend> findByEmployeeAndDutyDayAndIsCome(Employee employee, String dutyDay, boolean isCome);

    default List<Attend> findByEmployeeUnAttendOnLastThreeDays(Employee employee, AttendType type) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return findAll(((root, query, criteriaBuilder) -> {
            Calendar c = Calendar.getInstance();
            String end = dateFormat.format(c.getTime());
            c.add(Calendar.DAY_OF_MONTH, -3);
            String start = dateFormat.format(c.getTime());
            return query.where(
                    criteriaBuilder.equal(root.get("employee"), employee),
                    criteriaBuilder.notEqual(root.get("type"), type),
                    criteriaBuilder.between(root.get("dutyDay"), start, end)
            ).getRestriction();
        }));
    }
}
