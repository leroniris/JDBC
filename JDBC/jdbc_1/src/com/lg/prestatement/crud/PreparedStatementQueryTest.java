package com.lg.prestatement.crud;

import com.lg.bean.Customer;
import com.lg.bean.Order;
import com.lg.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用PreparedStatement实现针对于不同表的通用的查询操作
 */
public class PreparedStatementQueryTest {


    @Test
    public void testGetInstance() {
        String sql = "select id,name,email from customers where id=?";
        Customer customer = getInstance(Customer.class, sql, 12);
        System.out.println(customer);

        String sql2 = "select order_id orderId,order_name orderName from `order` where order_id = ?";
        Order order = getInstance(Order.class, sql2, 1);
        System.out.println(order);
    }


    @Test
    public void testGetListInstance() {
        String sql = "select id,name,email from customers where id < ?";
        List<Customer> customers = getForList(Customer.class, sql, 12);
        for(Customer customer : customers) {
            System.out.println(customer);
        }

        String sql2 = "select order_id orderId,order_name orderName from `order` where order_id < ?";
        List<Order> orders = getForList(Order.class, sql2, 3);
        for(Order order : orders) {
            System.out.println(order);
        }
    }

    /**
     * 针对于不同表的通用的查询操作
     *
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public <T> T getInstance(Class<T> clazz, String sql, Object... args) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //获取连接
            connection = JDBCUtils.getConnection();
            //获取预编译SQL语句
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                //设置占位符
                ps.setObject(i + 1, args[i]);
            }
            //获取结果集
            rs = ps.executeQuery();
            //获取结果集元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //获取元数据的列数
            int columnCount = rsmd.getColumnCount();
            if (rs.next()) {
                T t = clazz.newInstance();
                //获取列值
                for (int i = 0; i < columnCount; i++) {
                    //获取每个列的列值
                    Object columnValue = rs.getObject(i + 1);

                    //获取每个列的列名
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);

                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(rs, connection, ps);
        }
        return null;
    }


    public <T> List<T> getForList(Class<T> clazz, String sql, Object... args){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<T> list = null;
        try {
            //获取连接
            connection = JDBCUtils.getConnection();
            //预编译sql
            ps = connection.prepareStatement(sql);
            //设置占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            //得到结果集
            rs = ps.executeQuery();
            //得到元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //获取列数
            int columnCount = rsmd.getColumnCount();

            list = new ArrayList<>();
            while (rs.next()) {
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    //获取列值
                    Object columnValue = rs.getObject(i + 1);
                    //获取列名
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(rs,connection,ps);
        }
        return null;
    }

}
