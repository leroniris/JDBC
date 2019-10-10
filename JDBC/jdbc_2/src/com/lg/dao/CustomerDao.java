package com.lg.dao;

import com.lg.bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * 此接口用于规范针对于customers表的常用操作
 */
public interface CustomerDao {
    /**
     * 将customer对象添加到数据库中
     *
     * @param conn
     * @param customer
     */
    void insert(Connection conn, Customer customer);

    /**
     * 根据指定id删除表中记录
     *
     * @param conn
     * @param id
     */
    void deleteById(Connection conn, int id);

    /**
     * 修改数据库表中的记录
     * @param connection
     * @param customer
     */
    void update(Connection connection, Customer customer);

    /**
     * 根据指定的id，查询对应的customer
     * @param conn
     * @param id
     */
    Customer getCustomerById(Connection conn, int id);

    /**
     * 查询表中所有记录
     * @param conn
     * @return
     */
    List<Customer> getAll(Connection conn);

    /**
     * 返回数据表中数据总数
     * @param conn
     * @return
     */
    Long getCount(Connection conn);

    /**
     * 返回数据表中最大生日
     * @param conn
     * @return
     */
    Date getMaxBirth(Connection conn);
}
