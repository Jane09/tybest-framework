package com.tybest.jdbc.sharding.raw;

import com.alibaba.druid.pool.DruidDataSource;
import io.shardingsphere.api.HintManager;
import io.shardingsphere.api.algorithm.sharding.ShardingValue;
import io.shardingsphere.api.algorithm.sharding.hint.HintShardingAlgorithm;
import io.shardingsphere.api.config.ShardingRuleConfiguration;
import io.shardingsphere.api.config.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.HintShardingStrategyConfiguration;
import io.shardingsphere.api.config.strategy.InlineShardingStrategyConfiguration;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tb
 * @date 2018/11/28 10:20
 */
public class RawDataSharding {

    public static void main(String[] args) throws SQLException {
        RawDataSharding sharding = new RawDataSharding();
        sharding.test();
    }

    /**
     * 获取数据源
     * @return
     * @throws SQLException
     */
    public DataSource getDataSource() throws SQLException {
        Map<String,DataSource> dataSourceMap = new HashMap<>(16);
        DruidDataSource ds0 = new DruidDataSource();
        ds0.setDriverClassName("com.mysql.jdbc.Driver");
        ds0.setUrl("jdbc:mysql://localhost:3306/ds0?useSSL=false");
        ds0.setUsername("root");
        ds0.setPassword("123456");
        dataSourceMap.put("ds0",ds0);

        DruidDataSource ds1 = new DruidDataSource();
        ds1.setDriverClassName("com.mysql.jdbc.Driver");
        ds1.setUrl("jdbc:mysql://localhost:3306/ds1?useSSL=false");
        ds1.setUsername("root");
        ds1.setPassword("123456");
        dataSourceMap.put("ds1",ds1);

        TableRuleConfiguration orderTableRuleConfiguration = new TableRuleConfiguration();
        orderTableRuleConfiguration.setLogicTable("t_order");
        orderTableRuleConfiguration.setActualDataNodes("ds${0..1}.t_order${0..1}");
        //强制路由
//        HintShardingStrategyConfiguration hint = new HintShardingStrategyConfiguration(new HintShardingAlgorithm() {
//            @Override
//            public Collection<String> doSharding(Collection<String> collection, ShardingValue shardingValue) {
//                return null;
//            }
//        });
//        HintManager.getInstance().setMasterRouteOnly();

//        orderTableRuleConfiguration.setDatabaseShardingStrategyConfig();
        //分表分库策略
        orderTableRuleConfiguration.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id","ds${user_id % 2}"));
        orderTableRuleConfiguration.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("order_id","t_order${order_id % 2}"));

        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();
        shardingRuleConfiguration.getTableRuleConfigs().add(orderTableRuleConfiguration);

        return ShardingDataSourceFactory.createDataSource(dataSourceMap,shardingRuleConfiguration,new ConcurrentHashMap<>(0),new Properties());
    }

    public void test() throws SQLException {
        DataSource dataSource = getDataSource();
        Connection conn = dataSource.getConnection();
//        String sql = "insert into t_order (order_id,user_id) values (7,7),(7,8),(8,8),(8,9)";
//        PreparedStatement statement =conn.prepareStatement(sql);
//        statement.execute();

        String query = "select order_id,user_id from t_order";
        PreparedStatement queryStat = conn.prepareStatement(query);
        try(ResultSet rs = queryStat.executeQuery()){
            while (rs.next()){
                System.out.println(rs.getLong(1)+" "+rs.getLong(2));
            }
        }finally {
            conn.close();
        }
    }

}
