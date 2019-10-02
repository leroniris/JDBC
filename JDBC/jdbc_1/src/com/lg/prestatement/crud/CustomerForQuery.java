package com.lg.prestatement.crud;

import com.lg.bean.Customer;
import com.lg.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * 针对Customers表的查询操作
 */
public class CustomerForQuery {

    @Test
    public void testQuery1(){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();

            String sql = "select id,name,email,birth from customers where id = ?";
            ps = connection.prepareStatement(sql);

            ps.setObject(1, 1);

            //执行，并返回结果集
            resultSet = ps.executeQuery();
            //处理结果集
            if(resultSet.next()) { //判断结果集的下一条是否有数据，如果有数据返回true，并指针下移,否则返回false
                //获取当前这条数据额各个字段的值
                final int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date birth = resultSet.getDate(4);
                //方式一：
    //            System.out.println("id = " + id + ",name = " + name + ",email = " + email + ",birth = " + birth);

                //方式二：
    //            Object[] data = new Object[]{id, name, email, birth};
    //            System.out.println(Arrays.toString(data));

                //方式三：
                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(resultSet, connection, ps);
        }

    }


    /**
     * 针对customers这个表查询操作
     */
    public Customer queryForCustomers(String sql, Object... args){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtils.getConnection();

            ps = connection.prepareStatement(sql);
            for(int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();

            if(rs.next()) {
                Customer customer = new Customer();
                //处理结果集一行数据中的每一列
                for(int i = 0; i < columnCount; i++) {
                    //获取列值
                    Object columnValue = rs.getObject(i + 1);

                    //获取每个列的列名
//                    String columnName = rsmd.getColumnName(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //给customer对象指定的某个属性赋值为columnValue，通过反射
                    Field field = Customer.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(customer, columnValue);


                }
                return customer;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(rs, connection, ps);
        }
        return null;
    }

    @Test
    public void testQueryForCustomers() {
        String sql = "select id,name,email,birth from customers where id = ?";
        Customer customer = queryForCustomers(sql, "13");
        System.out.println(customer);

        String sql2 = "select name,email from customers where name = ?";
        Customer customer2 = queryForCustomers(sql2, "周杰伦");
        System.out.println(customer2);
    }
}
