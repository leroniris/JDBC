package com.lg.transaction;

import com.lg.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;

public class TestConnection {
    @Test
    public void  test() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        System.out.println(conn);

    }
}
