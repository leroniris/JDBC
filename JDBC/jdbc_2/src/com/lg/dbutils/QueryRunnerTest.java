package com.lg.dbutils;

import com.lg.DataBasePooling.util.JDBCUtils;
import com.lg.bean.Customer;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * commons-dbutils 是 Apache 组织提供的一个开源 JDBC工具类库，
 * 它是对JDBC的简单封装，学习成本极低，并且使用dbutils能极大简化jdbc编码的工作量，同时也不会影响程序的性能。
 */
public class QueryRunnerTest {

    @Test
    public void testInsert() {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection3();
            String sql = "insert into customers(name, email, birth) values(?, ?, ?)";

            int insertCount = runner.update(conn, sql, "菜徐坤", "cxk@qq.com", "1992-03-02");
            System.out.println("添加了" + insertCount + "条记录");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    /**
     * 查询测试
     * 1、BeanHandler:是ResultSetHandler接口的实现类，用于封装表中的一条记录
     * @throws SQLException
     */
    @Test
    public void testQuery() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection3();
            QueryRunner runner = new QueryRunner();
            String sql = "select id, name, email, birth from customers where id=?";
            BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);
            Customer customer = runner.query(conn, sql, handler, 2);
            System.out.println(customer);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }


    /**
     * 查询测试
     * 2、BeanListHandler:是ResultSetHandler接口的实现类，用于封装表中的多条记录
     * @throws SQLException
     */
    @Test
    public void testQuery2() throws SQLException {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection3();
            QueryRunner runner = new QueryRunner();
            String sql = "select id, name, email, birth from customers where id < ?";
            BeanListHandler<Customer> handler = new BeanListHandler<>(Customer.class);
            List<Customer> customers = runner.query(conn, sql, handler, 12);
            customers.forEach(System.out::println);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }


    /**
     * 查询测试
     * 3、MapHandler:是ResultSetHandler接口的实现类，用于封装表中的一条记录
     * 将字段及相应字段的值作为map中的key和value
     * @throws SQLException
     */
    @Test
    public void testQuery3() throws SQLException {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection3();
            QueryRunner runner = new QueryRunner();
            String sql = "select id, name, email, birth from customers where id = ?";
            MapHandler handler = new MapHandler();
            Map<String, Object> map = runner.query(conn, sql, handler, 12);
            System.out.println(map);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }


    /**
     * 查询测试
     * 4、MapHandler:是ResultSetHandler接口的实现类，用于封装表中的多条记录
     * 将字段及相应字段的值作为map中的key和value
     * @throws SQLException
     */
    @Test
    public void testQuery4() throws SQLException {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection3();
            QueryRunner runner = new QueryRunner();
            String sql = "select id, name, email, birth from customers where id < ?";
            MapListHandler handler = new MapListHandler();
            List<Map<String, Object>> map = runner.query(conn, sql, handler, 12);
            map.forEach(System.out::println);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    /**
     * 查询特殊测试
     * 5、ScalaHandler:是ResultSetHandler接口的实现类，用于查询表中特殊字段
     * @throws SQLException
     */
    @Test
    public void testQuery5() throws SQLException {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection3();
            QueryRunner runner = new QueryRunner();
            String sql = "select count(*) from customers";
            ScalarHandler handler = new ScalarHandler();
            Object counts = runner.query(conn, sql, handler);
            System.out.println(counts);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

}
