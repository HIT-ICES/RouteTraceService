package com.hitices.route.bean.svcservicebeans;

import java.util.Objects;
import lombok.var;

public final class ServiceIdBean {
    public static final ServiceIdBean ALL = new ServiceIdBean("");
    private final String serviceId;

    public ServiceIdBean(String serviceId) {
        this.serviceId = serviceId;
    }

    public String serviceId() {
        return serviceId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ServiceIdBean) obj;
        return Objects.equals(this.serviceId, that.serviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceId);
    }

    @Override
    public String toString() {
        return "ServiceIdBean[" +
                "serviceId=" + serviceId + ']';
    }

}
