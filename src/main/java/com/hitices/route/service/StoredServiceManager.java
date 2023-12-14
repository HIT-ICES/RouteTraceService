package com.hitices.route.service;


import com.hitices.route.bean.svcservicebeans.Interface;
import com.hitices.route.bean.svcservicebeans.Service;
import lombok.var;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StoredServiceManager {
    private final Map<String, Service> _services;

    public StoredServiceManager(List<Service> storedServices) {
        _services = storedServices.stream()
                .collect(Collectors.toMap(
                        s -> Arrays.asList(s.Name().split("/")).get(s.Name().split("/").length - 1),
                        s -> s
                ));
    }

    public Interface getInterface(String serviceName, String api, String method) {
        var service = _services.getOrDefault(serviceName, null);
        if (service == null) return null;
        return service.Interfaces().stream()
                .filter(iff ->
                        0 == iff.Method().compareToIgnoreCase(method) &&
                                api.startsWith(iff.Path())
                )
                .findFirst().orElse(null);
    }
}