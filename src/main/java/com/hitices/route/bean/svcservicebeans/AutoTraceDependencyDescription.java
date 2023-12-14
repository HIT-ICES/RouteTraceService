package com.hitices.route.bean.svcservicebeans;

import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class AutoTraceDependencyDescription {
    private String CallerService;
    private String CallerInterface;
    private String CalleeService;
    private String CalleeInterface;
    private Double RequestSize;
    private Double ResponseSize;

    public String CallerService() {
        return CallerService;
    }

    public String CallerInterface() {
        return CallerInterface;
    }

    public String CalleeService() {
        return CalleeService;
    }

    public String CalleeInterface() {
        return CalleeInterface;
    }

    public Double RequestSize() {
        return RequestSize;
    }

    public Double ResponseSize() {
        return ResponseSize;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (AutoTraceDependencyDescription) obj;
        return Objects.equals(this.CallerService, that.CallerService) &&
                Objects.equals(this.CallerInterface, that.CallerInterface) &&
                Objects.equals(this.CalleeService, that.CalleeService) &&
                Objects.equals(this.CalleeInterface, that.CalleeInterface) &&
                Objects.equals(this.RequestSize, that.RequestSize) &&
                Objects.equals(this.ResponseSize, that.ResponseSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(CallerService, CallerInterface, CalleeService, CalleeInterface, RequestSize, ResponseSize);
    }

    @Override
    public String toString() {
        return "AutoTraceDependencyDescription[" +
                "CallerService=" + CallerService + ", " +
                "CallerInterface=" + CallerInterface + ", " +
                "CalleeService=" + CalleeService + ", " +
                "CalleeInterface=" + CalleeInterface + ", " +
                "RequestSize=" + RequestSize + ", " +
                "ResponseSize=" + ResponseSize + ']';
    }
}


