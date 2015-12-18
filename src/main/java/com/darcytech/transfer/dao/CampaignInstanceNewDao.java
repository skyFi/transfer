package com.darcytech.transfer.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.darcytech.transfer.model.CampaignInstanceNew;

/**
 * User: dixi
 * Time: 2015/12/17 18:07
 */
@Repository
@Transactional
public class CampaignInstanceNewDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void multiSave(List<CampaignInstanceNew> newList) {
        for (CampaignInstanceNew instanceNew : newList) {
            save(instanceNew);
        }
    }

    public void save(CampaignInstanceNew instanceNew) {
        entityManager.persist(instanceNew);
    }
}
