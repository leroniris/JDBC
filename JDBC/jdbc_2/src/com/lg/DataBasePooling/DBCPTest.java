package com.lg.DataBasePooling;


import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBCPTest {

    /**
     * 测试DBCP数据库连接池技术
     */
    @Test
    public void testGetConnection() throws SQLException {
        //创建DBCP数据库连接池
        DataSource dataSource = new BasicDataSource();
        //设置基本信息
        ((BasicDataSource) dataSource).setDriverClassName("com.mysql.jdbc.Driver");
        ((BasicDataSource) dataSource).setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8&rewriteBatchedStatements=true");
        ((BasicDataSource) dataSource).setUsername("root");
        ((BasicDataSource) dataSource).setPassword("ligan1998");
        ((BasicDataSource) dataSource).setInitialSize(10);
        ((BasicDataSource) dataSource).setMaxActive(10);
        Connection conn = dataSource.getConnection();
        System.out.println(conn);


    }

    /**
     * 使用配置文件
     */
    @Test
    public void testGetConnection2() throws Exception {
        Properties properties = new Properties();
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
        properties.load(is);
        DataSource dataSource = BasicDataSourceFactory.createDataSource(properties);
        Connection conn = dataSource.getConnection();
        System.out.println(conn);
    }

}
