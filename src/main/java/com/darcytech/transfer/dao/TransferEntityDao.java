package com.darcytech.transfer.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

/**
 * Created by darcy on 2015/12/19.
 */
@Repository
@Transactional
public class TransferEntityDao {

    @PersistenceContext
    private EntityManager entityManager;

    public <T> void multiSave(List<T> ts) {
        for (T t : ts) {
            entityManager.merge(t);
        }
    }

    public <T> void merge(T t) {
        entityManager.merge(t);
    }

    public <T> void persist(T t) {
        entityManager.persist(t);
    }

}
