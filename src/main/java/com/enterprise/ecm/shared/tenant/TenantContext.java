package com.enterprise.ecm.shared.tenant;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class TenantContext {
    
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();
    private static final ConcurrentHashMap<String, TenantInfo> tenantCache = new ConcurrentHashMap<>();
    
    public static void setCurrentTenant(String tenantId) {
        currentTenant.set(tenantId);
    }
    
    public static String getCurrentTenant() {
        return currentTenant.get();
    }
    
    public static void clear() {
        currentTenant.remove();
    }
    
    public static void cacheTenantInfo(String tenantId, TenantInfo tenantInfo) {
        tenantCache.put(tenantId, tenantInfo);
    }
    
    public static TenantInfo getTenantInfo(String tenantId) {
        return tenantCache.get(tenantId);
    }
    
    public static TenantInfo getCurrentTenantInfo() {
        String tenantId = getCurrentTenant();
        return tenantId != null ? getTenantInfo(tenantId) : null;
    }
} 