package com.tybest.jdbc.sharding.raw;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author tb
 * @date 2018/11/28 11:55
 */
@Component
@RequiredArgsConstructor
public class SpringBootDbSharding {

    @Resource
    private DataSource dataSource;


    public void test() throws SQLException {
        Connection conn = dataSource.getConnection();
        String query = "select order_id,user_id from t_order";
        PreparedStatement queryStat = conn.prepareStatement(query);
        try(ResultSet rs = queryStat.executeQuery()){
            while (rs.next()){
                System.out.println(rs.getLong(1));
            }
        }finally {
            conn.close();
        }
    }
}
