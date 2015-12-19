package com.darcytech.transfer.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.darcytech.transfer.model.CampaignInstance;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * User: dixi
 * Time: 2015/12/17 16:12
 */
@Repository
@Transactional
public class CampaignInstanceDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public List<CampaignInstance> findCampaignInstanceByResultIdIsNull(Date startTime,
                                                                       Date endTime, int pageNo, int pageSize) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CampaignInstance.class);
        detachedCriteria.add(Restrictions.isNull("resultId"));
        if (startTime != null) {
            detachedCriteria.add(Restrictions.ge("createDate", startTime));
        }
        if (endTime != null) {
            detachedCriteria.add(Restrictions.le("createDate", endTime));
        }

        Session session = entityManager.unwrap(Session.class);
        int pageFrom = pageSize * (pageNo - 1);
        Criteria criteria = detachedCriteria.getExecutableCriteria(session)
                .setFirstResult(pageFrom).setMaxResults(pageSize);

        return criteria.list();
    }

    public int countCampaignInstanceByResultIdIsNull(Date startTime, Date endTime) {
        StringBuffer sql = new StringBuffer("select count(id) from campaign_instance where result_id is null");

        List param = Lists.newArrayList();
        if (startTime != null) {
            sql.append(" and create_date >= ? ");
            param.add(startTime);
        }
        if (endTime != null) {
            sql.append(" and create_date <= ? ");
            param.add(endTime);
        }

        return jdbcTemplate.queryForObject(sql.toString(), param.toArray(), Integer.class);
    }

    public Map<String, CampaignInstance> findByResultIds(Set<String> resultIds) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CampaignInstance.class);
        detachedCriteria.add(Restrictions.in("resultId", resultIds));

        Session session = entityManager.unwrap(Session.class);
        List<CampaignInstance> instances = detachedCriteria.getExecutableCriteria(session).list();

        Map<String, CampaignInstance> map = Maps.newHashMap();
        for (CampaignInstance instance : instances) {
            map.put(instance.getResultId(), instance);
        }
        return map;
    }
}
