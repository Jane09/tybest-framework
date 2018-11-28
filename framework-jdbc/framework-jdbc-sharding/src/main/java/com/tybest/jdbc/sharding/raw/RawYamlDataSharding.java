package com.tybest.jdbc.sharding.raw;

import io.shardingsphere.shardingjdbc.api.yaml.YamlShardingDataSourceFactory;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author tb
 * @date 2018/11/28 11:02
 */
public class RawYamlDataSharding {

    public static void main(String[] args) throws IOException, SQLException {
        RawYamlDataSharding sharding = new RawYamlDataSharding();
        sharding.test();
    }

    private DataSource getDataSource() throws IOException, SQLException {
        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        if(classLoader != null){
            URL url = classLoader.getResource("application-sharding.yaml");
            if(url != null){
                return YamlShardingDataSourceFactory.createDataSource(new File(url.getPath()));
            }
        }
        throw new RuntimeException("create datasource failed");
    }

    private void test() throws IOException, SQLException {
        DataSource dataSource = getDataSource();
        Connection conn = dataSource.getConnection();
        String sql = "insert into t_order (order_id,user_id) values (9,9),(9,10)";
        PreparedStatement statement =conn.prepareStatement(sql);
        statement.execute();
    }
}
