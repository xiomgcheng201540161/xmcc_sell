package com.xmcc.wechatorder.dao;

import java.util.List;

/**
 * 批量插入接口开发
 * @param <T>
 */
public interface BatchDao<T> {
    void batchInsert(List<T> list);
}
