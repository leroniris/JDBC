package com.lg.batch;

import com.lg.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * 使用PreparedStatement实现批量数据的操作
 */
public class BatchInsertTest {

    /**
     * 方式一：使用Statement
     * 花费时间：19748ms
     */
    @Test
    public void testStatement() throws Exception {
        long start = System.currentTimeMillis();
        Connection conn = JDBCUtils.getConnection();
        Statement st = conn.createStatement();
        for(int i = 1;i <= 20000;i++){
            String sql = "insert into goods(name) values(concat('name_'," + i +"))";
            st.executeUpdate(sql);
        }
        long end = System.currentTimeMillis();
        System.out.println("花费时间为：" + (end - start));
        JDBCUtils.closeResource(conn, st);
    }

    /**
     * 方式二：使用PreparedStatement
     * 花费时间：19645
     */
    @Test
    public void testPreparedStatement() throws Exception {

        long start = System.currentTimeMillis();
        Connection conn = JDBCUtils.getConnection();
        String sql = "insert into goods(name) values(?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        for(int i = 0; i < 20000; i++) {
            ps.setObject(1, "name_" + i);
            ps.execute();
        }

        long end = System.currentTimeMillis();
        System.out.println("花费时间为：" + (end - start));
        JDBCUtils.closeResource(conn, ps);

    }


    /**
     * 方式三：
     * 1、addBatch()、executeBatch()、clearBatch()
     * 20000花费时间为：431ms
     * 1000000花费时间为：6284ms
     */
    @Test
    public void testPreparedStatement2() throws Exception {

        long start = System.currentTimeMillis();
        Connection conn = JDBCUtils.getConnection();
        String sql = "insert into goods(name) values(?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        for(int i = 1; i <= 1000000; i++) {
            ps.setObject(1, "name_" + i);
            //1、攒sql
            ps.addBatch();
            if(i % 500 == 0) {
                //2、批量插入
                ps.executeBatch();
                //3、清空
                ps.clearBatch();

            }
        }

        long end = System.currentTimeMillis();
        System.out.println("花费时间为：" + (end - start));
        JDBCUtils.closeResource(conn, ps);

    }

    /**
     * 方式四：
     * 1、addBatch()、executeBatch()、clearBatch()
     * 2、使用Connection 的 setAutoCommit(false)  /  commit()

     * 1000000花费时间为：3788ms
     */
    @Test
    public void testPreparedStatement3() throws Exception {

        long start = System.currentTimeMillis();
        Connection conn = JDBCUtils.getConnection();
        conn.setAutoCommit(false);
        String sql = "insert into goods(name) values(?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        for(int i = 1; i <= 1000000; i++) {
            ps.setObject(1, "name_" + i);
            //1、攒sql
            ps.addBatch();
            if(i % 500 == 0) {
                //2、批量插入
                ps.executeBatch();
                //3、清空
                ps.clearBatch();

            }
        }
        //2、提交
        conn.commit();
        long end = System.currentTimeMillis();
        System.out.println("花费时间为：" + (end - start));
        JDBCUtils.closeResource(conn, ps);

    }
}
