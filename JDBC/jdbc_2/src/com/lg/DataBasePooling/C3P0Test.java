package com.lg.DataBasePooling;

import com.lg.DataBasePooling.util.JDBCUtils;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class C3P0Test {
    @Test
    public void testGetConnection() throws Exception {
        //获取C3P0数据库连接池
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass("com.mysql.jdbc.Driver"); //loads the jdbc driver
        cpds.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        cpds.setUser("root");
        cpds.setPassword("ligan1998");
        //通过设置参数来设置数据库连接池
        //设置初始时数据库连接池的连接数
        cpds.setInitialPoolSize(10);

        Connection conn = cpds.getConnection();
        System.out.println(conn);
    }


    @Test
    public void testGetConnection1() throws SQLException {
        Connection conn = JDBCUtils.getConnection1();

    }



}
