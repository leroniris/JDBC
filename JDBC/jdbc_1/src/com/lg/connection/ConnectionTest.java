package com.lg.connection;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionTest {

    //方式一：上述代码中显式出现了第三方数据库的API
    @Test
    public void testConnection1() throws SQLException {
        //1.提供java.sql.Driver接口实现类的对象
        Driver driver = new com.mysql.jdbc.Driver();

        //2.提供url，指明具体操作的数据
        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8";

        //3.提供Properties的对象，指明用户名和密码
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "ligan1998");

        //4.调用driver的connect()，获取连接
        Connection connect = driver.connect(url, info);

        System.out.println(connect);
    }

    //方式二：相较于方式一，这里使用反射实例化Driver，不在代码中体现第三方数据库的API。体现了面向接口编程思想。
    @Test
    public void testConnection2() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        String driverName = "com.mysql.jdbc.Driver";
        Class clazz = Class.forName(driverName);
        Driver driver = (Driver) clazz.newInstance();

        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8";

        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "ligan1998");

        Connection connect = driver.connect(url, info);

        System.out.println(connect);
    }

    //方式三：使用DriverManager实现数据库的连接。体会获取连接必要的4个基本要素。
    @Test
    public void testConnection3() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {

        //1、获取 Driver实现类的对象
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();

        //2、提供另外三个连接的基本信息
        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8";
        String user = "root";
        String password = "ligan1998";


        //3、注册驱动
        DriverManager.registerDriver(driver);

        //4、获取连接
        Connection connection = DriverManager.getConnection(url, user, password);

        System.out.println(connection);
    }

    //方式四：不必显式的注册驱动了。因为在DriverManager的源码中已经存在静态代码块，实现了驱动的注册。
    @Test
    public void testConnection4() throws ClassNotFoundException, SQLException {
        //1、提供四个连接的基本信息
        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8";
        String user = "root";
        String password = "ligan1998";
        String driverName = "com.mysql.jdbc.Driver";

        //2、加载Driver
        Class.forName(driverName);

        //3、获取连接
        Connection connection = DriverManager.getConnection(url, user, password);

        System.out.println(connection);

    }

    //方式五：使用配置文件的方式保存配置信息，在代码中加载配置文件

    /**
     * 使用配置文件的好处：
     * ①实现了代码和数据的分离，如果需要修改配置信息，直接在配置文件中修改，不需要深入代码
     * ②如果修改了配置信息，省去重新编译的过程。
     */
    @Test
    public void testConnection5() throws IOException, ClassNotFoundException, SQLException {
        //1、读取配置文件中的4个基本信息
        InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
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
        System.out.println(connection);

    }
}
