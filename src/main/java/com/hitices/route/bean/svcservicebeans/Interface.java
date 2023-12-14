package com.hitices.route.bean.svcservicebeans;

import java.util.Objects;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class Interface {
    private String Id;
    private String Path;
    private Double InputSize;
    private Double OutputSize;
    private String Method;
    private String Info;
    

    public String Id() {
        return Id;
    }

    public String Path() {
        return Path;
    }

    public Double InputSize() {
        return InputSize;
    }

    public Double OutputSize() {
        return OutputSize;
    }

    public String Method() {
        return Method;
    }

    public String Info() {
        return Info;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Interface) obj;
        return Objects.equals(this.Id, that.Id) &&
                Objects.equals(this.Path, that.Path) &&
                Objects.equals(this.InputSize, that.InputSize) &&
                Objects.equals(this.OutputSize, that.OutputSize) &&
                Objects.equals(this.Method, that.Method) &&
                Objects.equals(this.Info, that.Info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, Path, InputSize, OutputSize, Method, Info);
    }

    @Override
    public String toString() {
        return "Interface[" +
                "Id=" + Id + ", " +
                "Path=" + Path + ", " +
                "InputSize=" + InputSize + ", " +
                "OutputSize=" + OutputSize + ", " +
                "Method=" + Method + ", " +
                "Info=" + Info + ']';
    }

}
