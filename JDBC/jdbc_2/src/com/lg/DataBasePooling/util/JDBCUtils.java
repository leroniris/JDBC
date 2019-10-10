package com.lg.DataBasePooling.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

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


    private static ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
    /**
     * 获取c3p0连接
     */
    public static Connection getConnection1() throws SQLException {
        Connection conn = cpds.getConnection();
        return conn;
    }

    private static DataSource dataSource;
    static {
        Properties properties = new Properties();
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
        try {
            properties.load(is);
            dataSource = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用dbcp连接
     */
    public static Connection getConnection2() throws Exception {

        Connection conn = dataSource.getConnection();
        return conn;
    }

    private static DataSource dataSource2;
    static {
        Properties properties = new Properties();
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
        try {
            properties.load(is);
            dataSource2 = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用druid连接
     */
    public static  Connection getConnection3() throws SQLException {
        Connection conn = dataSource2.getConnection();
        return conn;
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

    /**
     * 使用DBUtils关闭资源操作
     * @param connection
     * @param ps
     */
    public static void closeResource2(Connection connection, Statement ps, ResultSet rs){
//        try {
//            DbUtils.close(connection);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        try {
//            DbUtils.close(ps);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        try {
//            DbUtils.close(rs);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        DbUtils.closeQuietly(connection);
        DbUtils.closeQuietly(ps);
        DbUtils.closeQuietly(rs);
    }
}
