package com.hitices.route.bean.svcservicebeans;

public record Resource(Double Cpu, Double Ram, Double Disk, Double GpuCore, Double GpuMem) {
    public static final Resource Null = new Resource(0.0, 0.0, 0.0, 0.0, 0.0);
}
