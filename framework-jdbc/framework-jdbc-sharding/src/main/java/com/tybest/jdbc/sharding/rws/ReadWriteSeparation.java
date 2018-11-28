package com.tybest.jdbc.sharding.rws;

import com.alibaba.druid.pool.DruidDataSource;
import io.shardingsphere.api.config.MasterSlaveRuleConfiguration;
import io.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
import io.shardingsphere.shardingjdbc.api.yaml.YamlMasterSlaveDataSourceFactory;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tb
 * @date 2018/11/28 13:37
 */
public class ReadWriteSeparation {

    public static void main(String[] args) throws SQLException, IOException {
        ReadWriteSeparation separation = new ReadWriteSeparation();
        separation.test();
    }

    private DataSource dataSource() throws SQLException {
        Map<String,DataSource> dataSourceMap = new HashMap<>(16);
        DruidDataSource master = new DruidDataSource();
        master.setDriverClassName("com.mysql.jdbc.Driver");
        master.setUrl("jdbc:mysql://localhost:3306/ds_master?useSSL=false");
        master.setPassword("123456");
        master.setUsername("root");
        dataSourceMap.put("ds_master",master);

        DruidDataSource slave0 = new DruidDataSource();
        slave0.setDriverClassName("com.mysql.jdbc.Driver");
        slave0.setUrl("jdbc:mysql://localhost:3306/ds_slave0?useSSL=false");
        slave0.setPassword("123456");
        slave0.setUsername("root");
        dataSourceMap.put("ds_slave0",slave0);

        DruidDataSource slave1 = new DruidDataSource();
        slave1.setDriverClassName("com.mysql.jdbc.Driver");
        slave1.setUrl("jdbc:mysql://localhost:3306/ds_slave1?useSSL=false");
        slave1.setPassword("123456");
        slave1.setUsername("root");
        dataSourceMap.put("ds_slave1",slave1);

        MasterSlaveRuleConfiguration configuration = new MasterSlaveRuleConfiguration("ds_master_slave","ds_master", Arrays.asList("ds_slave0","ds_slave1"));
        return MasterSlaveDataSourceFactory.createDataSource(dataSourceMap,configuration,new ConcurrentHashMap<>(0),new Properties());
    }


    private DataSource yamlDataSource() throws IOException, SQLException {
        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        if(classLoader != null) {
            URL url = classLoader.getResource("application-rwseparation.yaml");
            if(url != null){
                return YamlMasterSlaveDataSourceFactory.createDataSource(new File(url.getPath()));
            }
        }
        throw new RuntimeException("create datasource failed");
    }

    private void test() throws SQLException, IOException {
        DataSource dataSource = dataSource();
        Connection conn = dataSource.getConnection();
        String query = "select order_id,user_id from t_order";
        PreparedStatement queryStat = conn.prepareStatement(query);
        try(ResultSet rs = queryStat.executeQuery()){
            while (rs.next()){
                System.out.println("orderId="+rs.getLong(1)+" userId="+rs.getLong(2));
            }
        }finally {
            conn.close();
        }
        dataSource = yamlDataSource();
        conn = dataSource.getConnection();
        queryStat = conn.prepareStatement(query);
        try(ResultSet rs = queryStat.executeQuery()){
            while (rs.next()){
                System.out.println("yaml orderId="+rs.getLong(1)+" userId="+rs.getLong(2));
            }
        }finally {
            conn.close();
        }
    }

}
