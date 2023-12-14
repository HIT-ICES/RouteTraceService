package com.hitices.route.bean.svcservicebeans;

import java.util.Objects;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class Resource {
    public static final Resource Null = new Resource(0.0, 0.0, 0.0, 0.0, 0.0);
    private Double Cpu;
    private Double Ram;
    private Double Disk;
    private Double GpuCore;
    private Double GpuMem;


    public Double Cpu() {
        return Cpu;
    }

    public Double Ram() {
        return Ram;
    }

    public Double Disk() {
        return Disk;
    }

    public Double GpuCore() {
        return GpuCore;
    }

    public Double GpuMem() {
        return GpuMem;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Resource) obj;
        return Objects.equals(this.Cpu, that.Cpu) &&
                Objects.equals(this.Ram, that.Ram) &&
                Objects.equals(this.Disk, that.Disk) &&
                Objects.equals(this.GpuCore, that.GpuCore) &&
                Objects.equals(this.GpuMem, that.GpuMem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Cpu, Ram, Disk, GpuCore, GpuMem);
    }

    @Override
    public String toString() {
        return "Resource[" +
                "Cpu=" + Cpu + ", " +
                "Ram=" + Ram + ", " +
                "Disk=" + Disk + ", " +
                "GpuCore=" + GpuCore + ", " +
                "GpuMem=" + GpuMem + ']';
    }

}
