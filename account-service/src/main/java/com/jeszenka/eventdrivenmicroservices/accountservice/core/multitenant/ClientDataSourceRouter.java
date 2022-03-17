package com.jeszenka.eventdrivenmicroservices.accountservice.core.multitenant;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class ClientDataSourceRouter extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		//String tenant = System.nanoTime() % 2 == 1 ? "tenant1" : "tenant2";
		String tenant = "tenant1";
		TenantContext.set(tenant);
		return TenantContext.getContext();
	}
}