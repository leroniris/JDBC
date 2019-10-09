package com.lg.statement.crud;

import com.lg.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

/**
 * 演示使用PreparedStatement代替Statement解决SQL注入问题
 * 除了解决Statement的拼串，sql注入问题之外，PreparedStatement还有哪些好处？
 * 1、PreparedStatement操作Blog的数据，而Statement做不到
 * 2、PreparedStatement可以实现更高效的批量操作
 */
public class PreparedStatementTest {


    @Test
    public void testLogin() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("请输入用户名：");
        String user = scanner.nextLine();
        System.out.print("请输入密码：");
        String password = scanner.nextLine();
        //SELECT user,password FROM user_table WHERE user = '1' or ' AND password = '=1 or '1' = '1'
        String sql = "SELECT user,password FROM user_table WHERE user = ? and password = ?";
        User returnUser = getInstance(User.class, sql, user, password);
        if (returnUser != null) {
            System.out.println("登录成功");
        } else {
            System.out.println("用户名不存在或密码错误");
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
}
