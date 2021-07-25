//package com.hr.repository;
//
//import com.hr.domain.Attend;
//import com.hr.domain.AttendType;
//import com.hr.domain.Employee;
//import org.springframework.stereotype.Repository;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Root;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.List;
//
//@Repository
//public class AttendRepositoryImpl implements AttendRepositoryCustom {
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Override
//    public List<Attend> findByEmployeeUnAttend(Employee employee, AttendType type) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Attend> query = cb.createQuery(Attend.class);
//        Root<Attend> root = query.from(Attend.class);
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Calendar c = Calendar.getInstance();
//        String end = sdf.format(c.getTime());
//        c.add(Calendar.DAY_OF_MONTH, -3);
//        String start = sdf.format(c.getTime());
//
//        query.select(root).where(
//                cb.equal(root.get("employee"), employee),
//                cb.notEqual(root.get("type"), type),
//                cb.between(root.get("dutyDay"), start, end));
//        return entityManager.createQuery(query).getResultList();
//    }
//}
