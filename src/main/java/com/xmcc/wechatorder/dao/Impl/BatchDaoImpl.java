package com.xmcc.wechatorder.dao.Impl;

import com.xmcc.wechatorder.dao.BatchDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class BatchDaoImpl<T> implements BatchDao<T> {

    @PersistenceContext               //用于标识批量处理
    protected EntityManager em;      //用于批量处理
    @Override
    public void batchInsert(List<T> list) {
        for (int i = 0; i < list.size(); i++) {
            em.persist(list.get(i));
            if(i%100==0||i==list.size()-1){
                em.flush();
                em.clear();
            }
        }
    }
}
