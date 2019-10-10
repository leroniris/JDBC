package com.lg.transaction;

import com.lg.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionTest {

    @Test
    public void testUpdate() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            //设置关闭自动提交
            connection.setAutoCommit(false);
            String sql1 = "update user_table set balance = balance - 100 where user = ?";
            update(connection, sql1, "AA");

            System.out.println(10 / 0);

            String sql2 = "update user_table set balance = balance + 100 where user = ?";
            update(connection, sql2, "BB");

            //提交事务
            connection.commit();
            System.out.println("转账成功");
        } catch (Exception e) {
            e.printStackTrace();
            //回滚数据
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            JDBCUtils.closeResource(null, connection, null);
        }


    }

    //通用的增删改操作
    public void update(Connection connection, String sql, Object... args) {
        PreparedStatement ps = null;
        try {

            //2、预编译sql,返回PreparedStatement实例
            ps = connection.prepareStatement(sql);

            //3、填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            //4、执行
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps);
        }
    }



    //=========================================================================

    @Test
    public void testTransactionSelect() throws Exception {
        Connection conn = JDBCUtils.getConnection();

        System.out.println(conn.getTransactionIsolation());
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

        conn.setAutoCommit(false);
        String sql = "select user,password,balance from user_table where user=?";
        User user = getInstance(conn, User.class, sql, "CC");
        System.out.println(user);

    }

    @Test
    public void testTransactionUpdate() throws  Exception {

        Connection conn = JDBCUtils.getConnection();
        conn.setAutoCommit(false);
        String sql = "update user_table set balance = ? where user = ?";
        update(conn, sql, 4000, "CC");
        Thread.sleep(15000);
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
    public <T> T getInstance(Connection connection, Class<T> clazz, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
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
            JDBCUtils.closeResource(rs, null, ps);
        }
        return null;
    }




}
