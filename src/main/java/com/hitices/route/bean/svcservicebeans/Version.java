package com.hitices.route.bean.svcservicebeans;

import java.util.Objects;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class Version {
    private String Major;
    private String Minor;
    private String Patch;

    public String Major() {
        return Major;
    }

    public String Minor() {
        return Minor;
    }

    public String Patch() {
        return Patch;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Version) obj;
        return Objects.equals(this.Major, that.Major) &&
                Objects.equals(this.Minor, that.Minor) &&
                Objects.equals(this.Patch, that.Patch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Major, Minor, Patch);
    }

    @Override
    public String toString() {
        return "Version[" +
                "Major=" + Major + ", " +
                "Minor=" + Minor + ", " +
                "Patch=" + Patch + ']';
    }

}
