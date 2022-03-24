package com.jeszenka.eventdrivenmicroservices.userservice.core.multitenant;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class ClientDataSourceRouter extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		String tenant = "tenant2";
		TenantContext.set(tenant);
		return TenantContext.getContext();
	}
}