package com.lg.prestatement.crud;

import com.lg.util.JDBCUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class PreparedStatementUpdateTest {

    //向customers表中添加一条记录
    @Test
    public void testInsert(){
        InputStream is = null;
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            //1、读取配置文件中的4个基本信息
            is = PreparedStatementUpdateTest.class.getClassLoader().getResourceAsStream("jdbc.properties");

            Properties pro = new Properties();
            pro.load(is);

            String user = pro.getProperty("user");
            String password = pro.getProperty("password");
            String url = pro.getProperty("url");
            String driverName = pro.getProperty("driverName");

            //2、加载驱动
            Class.forName(driverName);

            //3、获取连接
            connection = DriverManager.getConnection(url, user, password);

            System.out.println(connection);

            //4、预编译sql语句，返回PreparedStatement的实例
            String sql = "insert into customers(name, email, birth) values(?,?,?);";
            ps = connection.prepareStatement(sql);

            //5、填充占位符
            ps.setString(1, "哪吒");
            ps.setString(2, "nezha@gmail.com");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse("1000-01-01");
            ps.setDate(3, new Date(date.getTime()));

            //6、执行操作
            ps.execute();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            //7、资源关闭
            if(ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    //修改customers表中的一条记录
    @Test
    public void testUpdate(){
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            //1、获取数据库的连接
            connection = JDBCUtils.getConnection();

            //2、预编译sql语句，返回PreparedStatement的实例
            String sql = "update customers set name=? where id=?";
            ps = connection.prepareStatement(sql);

            //3、填充占位符
            ps.setString(1, "木咋");
            ps.setInt(2, 19);

            //4、执行
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5、资源的关闭
            JDBCUtils.closeResource(connection, ps);
        }
    }

    //通用的增删改操作
    public void update(String sql, Object... args){
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            //1、获取数据库连接
            connection = JDBCUtils.getConnection();

            //2、预编译sql,返回PreparedStatement实例
            ps = connection.prepareStatement(sql);

            //3、填充占位符
            for(int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            //4、执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5、关闭连接
            JDBCUtils.closeResource(connection, ps);
        }

    }

    @Test
    public void testCommonUpdate() {
        String sql = "delete from customers where id = ?";
        update(sql, 19);
    }



}
