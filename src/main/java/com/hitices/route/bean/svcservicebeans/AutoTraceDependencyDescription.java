package com.hitices.route.bean.svcservicebeans;

import java.util.List;
import java.util.Map;

public record AutoTraceDependencyDescription(
        String CallerService,
        String CallerInterface,
        String CalleeService,
        String CalleeInterface,
        Double RequestSize,
        Double ResponseSize){}


