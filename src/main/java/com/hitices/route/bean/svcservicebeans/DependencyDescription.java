package com.hitices.route.bean.svcservicebeans;

import lombok.*;

import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class DependencyDescription {
    private String Caller;
    private String Callee;
    private Map<String, String> ExtraData;


    public String Caller() {
        return Caller;
    }

    public String Callee() {
        return Callee;
    }

    public Map<String, String> ExtraData() {
        return ExtraData;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (DependencyDescription) obj;
        return Objects.equals(this.Caller, that.Caller) &&
                Objects.equals(this.Callee, that.Callee) &&
                Objects.equals(this.ExtraData, that.ExtraData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Caller, Callee, ExtraData);
    }

    @Override
    public String toString() {
        return "DependencyDescription[" +
                "Caller=" + Caller + ", " +
                "Callee=" + Callee + ", " +
                "ExtraData=" + ExtraData + ']';
    }

}
