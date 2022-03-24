package com.jeszenka.eventdrivenmicroservices.accountservice.core.multitenant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MultiTenantConfig {

	@Value("#{${tenants.datasource}}")
	private Map<String, Map<String, String>> tenantDatasources;

	@Bean
	public DataSource clientDatasource() {
		Map<Object, Object> targetDataSources = new HashMap<>();

		for (Map.Entry<String, Map<String, String>> tenant : tenantDatasources.entrySet()) {

			DataSource dataSource = DataSourceBuilder.create()
					.url(tenant.getValue().get("url"))
					.username(tenant.getValue().get("username"))
					.password(tenant.getValue().get("password"))
					.driverClassName(tenant.getValue().get("driver"))
					.build();

			targetDataSources.put(tenant.getKey(), dataSource);
			TenantContext.add(tenant.getKey());
		}

		ClientDataSourceRouter clientRoutingDatasource
				= new ClientDataSourceRouter();
		clientRoutingDatasource.setTargetDataSources(targetDataSources);
		return clientRoutingDatasource;
	}

}
