package com.hitices.route.service;


import com.hitices.route.bean.svcservicebeans.Interface;
import com.hitices.route.bean.svcservicebeans.Service;
import lombok.var;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StoredServiceManager {
    private final Map<String, Service> _services;

    public StoredServiceManager(List<Service> storedServices) {
        var services = new HashMap<String, Service>();
        for (Service s : storedServices) {
            var key=Arrays.asList(s.Name().split("/")).get(s.Name().split("/").length - 1);
            services.put(key, s);
        }
        _services = services;
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