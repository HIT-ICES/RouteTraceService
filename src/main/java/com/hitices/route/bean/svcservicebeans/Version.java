package com.hitices.route.bean.svcservicebeans;

import java.util.Objects;
import lombok.var;

public final class Version {
    private final String Major;
    private final String Minor;
    private final String Patch;

    public Version(String Major, String Minor, String Patch) {
        this.Major = Major;
        this.Minor = Minor;
        this.Patch = Patch;
    }

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
