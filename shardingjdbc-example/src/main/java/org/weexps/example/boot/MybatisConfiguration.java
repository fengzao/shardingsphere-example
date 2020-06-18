package org.weexps.example.boot;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.weexps.example.mapper.OrderMapper;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @date 2020/6/16 15:48
 */
@Configuration
@MapperScan(basePackageClasses = OrderMapper.class, annotationClass = Mapper.class)
public class MybatisConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Bean
    @ConfigurationProperties("spring.druid")
    public DataSource ds() {
        return new DruidDataSource();
    }


    @Bean
    @Primary
    public DataSource shardingDataSource() {
        final Map<String, DataSource> dsMap = new HashMap<String, DataSource>();
        dsMap.put("ds", ds());

        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration("tb_order", "ds.tb_order_${0..1}");
        orderTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("uid", "tb_order_${uid % 2}"));
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);

        try {
            Properties properties = new Properties();
            properties.put("sql.show", true);
            return ShardingDataSourceFactory.createDataSource(dsMap, shardingRuleConfig, properties);
        } catch (SQLException e) {
            throw new BeanCreationException("create sharding datasource failed for SQLException", e);
        }
    }

    @Bean
    @ConditionalOnBean(DataSource.class)
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource, @Autowired(required = false) List<Interceptor> interceptors) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/*.myb.xml"));
        if (interceptors != null && !interceptors.isEmpty()) {
            sqlSessionFactoryBean.setPlugins(interceptors.toArray(new Interceptor[interceptors.size()]));
        }
        return sqlSessionFactoryBean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
