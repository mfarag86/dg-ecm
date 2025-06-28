package com.enterprise.ecm.shared.tenant;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TenantInterceptor implements HandlerInterceptor {
    
    @Value("${multi-tenant.header-name:X-TenantID}")
    private String headerName;
    
    @Value("${multi-tenant.default-tenant:default}")
    private String defaultTenant;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantId = request.getHeader(headerName);
        
        if (tenantId == null || tenantId.trim().isEmpty()) {
            tenantId = defaultTenant;
        }
        
        TenantContext.setCurrentTenant(tenantId);
        
        // Cache tenant info if not already cached
        if (TenantContext.getTenantInfo(tenantId) == null) {
            TenantInfo tenantInfo = new TenantInfo(tenantId, "Tenant " + tenantId, "Auto-generated tenant");
            TenantContext.cacheTenantInfo(tenantId, tenantInfo);
        }
        
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        TenantContext.clear();
    }
} 