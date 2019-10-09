package com.lg.util;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * 操作数据库的工具类
 */
public class JDBCUtils {

    /**
     * 获取数据库的连接
     * @return
     * @throws Exception
     */
    public static Connection getConnection() throws Exception{
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
        Properties pros = new Properties();
        pros.load(is);

        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverName = pros.getProperty("driverName");

        //2、加载驱动
        Class.forName(driverName);

        //3、获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
    }

    /**
     * 关闭资源操作
     * @param connection
     * @param ps
     */
    public static void closeResource(Connection connection, Statement ps) {
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
    }

    public static void closeResource(ResultSet resultSet, Connection connection, Statement ps) {

        //7、资源关闭
        if(resultSet != null) {
            try {
                resultSet.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    }
}
