package com.pu.universityprojectfirsttrimester.repository;

import com.pu.universityprojectfirsttrimester.dto.Filter;
import com.pu.universityprojectfirsttrimester.entity.Course;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CourseFilterRepository {

    @PersistenceContext
    EntityManager entityManager;

    public List<Course> getAll(Filter courseFilter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Course> cr = cb.createQuery(Course.class);
        Root<Course> root = cr.from(Course.class);

        List<Predicate> predicate = new ArrayList<>();
        if (courseFilter.getTeacherId() != null)
            predicate.add(cb.equal(root.get("teacher"), courseFilter.getTeacherId()));

        if (courseFilter.getPriceGreaterThan() != null)
            predicate.add(cb.gt(root.get("price"), courseFilter.getPriceGreaterThan()));

        if (courseFilter.getPriceLessThan() != null)
            predicate.add(cb.lt(root.get("price"), courseFilter.getPriceLessThan()));

        cr.select(root).where(predicate.toArray(new Predicate[0]));

        TypedQuery<Course> query = entityManager.createQuery(cr);
        return query.getResultList();
    }
}
