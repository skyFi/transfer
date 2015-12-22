package com.darcytech.transfer.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by darcy on 2015/12/19.
 */
@Repository
@Transactional
public class TransferEntityDao {

    @PersistenceContext
    private EntityManager entityManager;

    public <T> void save(List<T> ts) {
        for (T t : ts) {
            entityManager.merge(t);
        }
    }
}
