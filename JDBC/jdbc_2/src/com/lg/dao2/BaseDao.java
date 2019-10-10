package com.lg.dao2;

import com.lg.util.JDBCUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 封装了针对数据表的通用操作
 */
public abstract class BaseDao<T> {

    private Class<T> clazz = null;

    {
        //获取当前BaseDao的子类继承的父类中的泛型
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) genericSuperclass;
        Type[] typeArguments = paramType.getActualTypeArguments();
        clazz = (Class<T>) typeArguments[0];
    }

    //通用的增删改操作
    public void update(Connection connection, String sql, Object... args) {
        PreparedStatement ps = null;
        try {

            //2、预编译sql,返回PreparedStatement实例
            ps = connection.prepareStatement(sql);

            //3、填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            //4、执行
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps);
        }
    }

    /**
     * 针对于不同表的通用的查询一条记录操作
     * @param connection
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public  T getInstance(Connection connection, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
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
            JDBCUtils.closeResource(rs, null, ps);
        }
        return null;
    }


    /**
     * 针对于不同表的通用的查询多条记录操作
     * @param connection
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public List<T> getForList(Connection connection, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<T> list = null;
        try {
            //获取连接
            //预编译sql
            ps = connection.prepareStatement(sql);
            //设置占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            //得到结果集
            rs = ps.executeQuery();
            //得到元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //获取列数
            int columnCount = rsmd.getColumnCount();

            list = new ArrayList<>();
            while (rs.next()) {
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    //获取列值
                    Object columnValue = rs.getObject(i + 1);
                    //获取列名
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(rs, null, ps);
        }
        return null;
    }


    //查询特殊值的通用方法
    public <E> E getValue(Connection conn, String sql, Object...args){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for(int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            if(rs.next()) {
                E e = (E) rs.getObject(1);
                return e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(rs, null, ps);
        }
        return null;
    }

}
