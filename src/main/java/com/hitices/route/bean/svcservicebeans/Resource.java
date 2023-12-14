package com.hitices.route.bean.svcservicebeans;

import java.util.Objects;
import lombok.var;

public final class Resource {
    public static final Resource Null = new Resource(0.0, 0.0, 0.0, 0.0, 0.0);
    private final Double Cpu;
    private final Double Ram;
    private final Double Disk;
    private final Double GpuCore;
    private final Double GpuMem;

    public Resource(Double Cpu, Double Ram, Double Disk, Double GpuCore, Double GpuMem) {
        this.Cpu = Cpu;
        this.Ram = Ram;
        this.Disk = Disk;
        this.GpuCore = GpuCore;
        this.GpuMem = GpuMem;
    }

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
