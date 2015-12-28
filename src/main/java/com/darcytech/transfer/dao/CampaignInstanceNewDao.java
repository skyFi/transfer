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

import com.darcytech.transfer.enumeration.InstanceStatus;
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

    public void multiSaveOrUpdate(List<CampaignInstanceNew> newList) {
        for (CampaignInstanceNew instanceNew : newList) {
            saveOrUpdate(instanceNew);
        }
    }

    public void save(CampaignInstanceNew instanceNew) {
        entityManager.persist(instanceNew);
    }

    public CampaignInstanceNew findByOldInstanceId(Long oldInstanceId) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CampaignInstanceNew.class);
        detachedCriteria.add(Restrictions.eq("oldInstanceId", oldInstanceId));

        Session session = entityManager.unwrap(Session.class);

        Criteria criteria = detachedCriteria.getExecutableCriteria(session);
        List<CampaignInstanceNew> list = criteria.list();

        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    public CampaignInstanceNew findByConditions(Long userId, Long campaignId, String buyerNick) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CampaignInstanceNew.class);
        detachedCriteria.add(Restrictions.eq("userId", userId));
        detachedCriteria.add(Restrictions.eq("campaignId", campaignId));
        detachedCriteria.add(Restrictions.eq("buyerNick", buyerNick));
        detachedCriteria.add(Restrictions.eq("status", InstanceStatus.NORMAL));

        Session session = entityManager.unwrap(Session.class);

        Criteria criteria = detachedCriteria.getExecutableCriteria(session);
        List<CampaignInstanceNew> list = criteria.list();

        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    public void saveOrUpdate(CampaignInstanceNew instanceNew) {
        entityManager.merge(instanceNew);
    }
}
