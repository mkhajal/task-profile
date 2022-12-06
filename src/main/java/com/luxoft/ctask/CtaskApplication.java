package com.luxoft.ctask;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
public class CtaskApplication {
	
	private final String INIT_SQL = "classpath:flightdb-schema.sql";
	
	@Autowired
    private ApplicationContext applicationContext;
	
	@Autowired
    private DataSource datasource;
	
	public static void main(String[] args) {
		SpringApplication.run(CtaskApplication.class, args);
	}	
	
	@PostConstruct
	public void loadIfInMemory() throws Exception {
        Resource resource = applicationContext.getResource(INIT_SQL);
        ScriptUtils.executeSqlScript(datasource.getConnection(), resource);
    }
	
	@Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean<ShallowEtagHeaderFilter> registration
                = new FilterRegistrationBean<>(new ShallowEtagHeaderFilter());
        registration.addUrlPatterns("/*");
        registration.setName("etagFilter");
        return registration;
    }

    @Bean(name = "etagFilter")
    public Filter etagFilter() {
        return new ShallowEtagHeaderFilter();
    }

}
