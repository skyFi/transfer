package com.darcytech.transfer.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.darcytech.transfer.model.CampaignResultNew;

/**
 * User: dixi
 * Time: 2015/12/26 10:33
 */
@Repository
@Transactional
public class CampaignResultNewDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void multiSave(List<CampaignResultNew> newList) {
        for (CampaignResultNew resultNew : newList) {
            save(resultNew);
        }
    }

    public void save(CampaignResultNew resultNew) {
        entityManager.persist(resultNew);
    }

    public void saveOrUpdate(CampaignResultNew resultNew) {
        entityManager.merge(resultNew);
    }

    public CampaignResultNew findByOldResultId(String oldResultId) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CampaignResultNew.class);
        detachedCriteria.add(Restrictions.eq("oldResultId", oldResultId));

        Session session = entityManager.unwrap(Session.class);

        Criteria criteria = detachedCriteria.getExecutableCriteria(session);
        List<CampaignResultNew> list = criteria.list();

        return list != null && list.size() > 0 ? list.get(0) : null;
    }
}
