package com.xmcc.wechatorder.config;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.google.common.collect.Lists;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Druidconfig {
    @Bean
    //加载配置文件（prefix = "spring.druid"  写出druid的前缀就可以）
    @ConfigurationProperties(prefix = "spring.druid")
    public DruidDataSource druidDataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setProxyFilters(Lists.newArrayList(statFilter()));
        return druidDataSource;
    }

    //配置过滤的数据
    @Bean
    public StatFilter statFilter(){
        StatFilter statFilter = new StatFilter();
        //设置日志显示sql
        statFilter.setLogSlowSql(true);
        //设置超过5秒就为慢sql
        statFilter.setSlowSqlMillis(5);
        //设置合并sql
        statFilter.setMergeSql(true);
        return statFilter;
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        return new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
    }
}
