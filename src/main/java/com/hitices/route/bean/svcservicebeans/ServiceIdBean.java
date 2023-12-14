package com.hitices.route.bean.svcservicebeans;

public record ServiceIdBean(String serviceId) {
    public static final ServiceIdBean ALL = new ServiceIdBean("");
}
