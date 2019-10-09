package com.lg.blob;

import com.lg.bean.Customer;
import com.lg.util.JDBCUtils;
import org.junit.Test;

import java.io.*;
import java.sql.*;

/**
 * 使用PreparedStatement插入Blog数据
 */
public class BlogTest {

    /**
     * 向数据表customers中插入Blog类型字段
     */
    @Test
    public void testInsert() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        String sql = "insert into customers(name, email, birth, photo) values(?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setObject(1, "李淦1");
        ps.setObject(2, "13475804@qq.com");
        ps.setObject(3, "1998-09-08");
        FileInputStream is = new FileInputStream(new File("E.jpg"));
        ps.setBlob(4, is);

        ps.execute();
        JDBCUtils.closeResource(connection, ps);

    }

    /**
     * 查询数据表customers的Blog字段
     */
    @Test
    public void testQuery(){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth,photo from customers where id = ?";
            ps = connection.prepareStatement(sql);
            ps.setObject(1, 14);
            rs = ps.executeQuery();
            if(rs.next()) {
                //方式一：
    //            int id = rs.getInt(1);
    //            String name = rs.getString(2);
    //            String email = rs.getString(3);
    //            Date birth = rs.getDate(4);
                //方式二
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                Date birth = rs.getDate("birth");

                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);

                //将blob下载下来，以文件的形式保存在本地
                Blob photo = rs.getBlob("photo");
                is = photo.getBinaryStream();
                os = new FileOutputStream("李.jpg");
                byte[] buffer = new byte[1024];
                int len;
                while((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(rs, connection, ps);
            try {
                if(os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if( is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
