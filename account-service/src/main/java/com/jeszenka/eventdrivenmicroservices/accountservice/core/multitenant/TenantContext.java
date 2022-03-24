package com.jeszenka.eventdrivenmicroservices.accountservice.core.multitenant;

import java.util.HashSet;
import java.util.Set;

public class TenantContext {

	private static InheritableThreadLocal<String> CONTEXT = new InheritableThreadLocal<>();
	private static Set<String> TENANTS = new HashSet<>();

	public static void set(String tenant) {
		CONTEXT.set(tenant);
	}

	public static String getContext() {
		return CONTEXT.get();
	}

	public static void clear() {
		CONTEXT.remove();
	}

	public static void add(String tenant) {
		TENANTS.add(tenant);
	}
}
