package com.darcytech.transfer.dao;

import java.util.Date;
import java.util.List;

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

/**
 * User: dixi
 * Time: 2015/12/17 16:12
 */
@Repository
public class CampaignInstanceDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public List<CampaignInstance> findCampaignInstanceByResultIdIsNull(Date endTimeLine, int pageNo, int pageSize) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CampaignInstance.class);
        detachedCriteria.add(Restrictions.isNull("resultId"));
        detachedCriteria.add(Restrictions.le("createDate", endTimeLine));

        Session session = entityManager.unwrap(Session.class);
        int pageFrom = pageSize * (pageNo - 1);
        Criteria criteria = detachedCriteria.getExecutableCriteria(session)
                .setFirstResult(pageFrom).setMaxResults(pageSize);

        return criteria.list();
    }

    public int countCampaignInstanceByResultIdIsNull(Date endTimeLine) {
        String sql = "select count(id) from campaign_instance where result_id is null and create_date <= ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, endTimeLine);
    }
}
