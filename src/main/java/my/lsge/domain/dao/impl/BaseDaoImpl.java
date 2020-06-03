package my.lsge.domain.dao.impl;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class BaseDaoImpl<T> {

    @PersistenceContext
    protected EntityManager entityManager;

}
