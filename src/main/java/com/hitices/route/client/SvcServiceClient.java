package com.hitices.route.client;

import com.hitices.route.bean.DependencyBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "SvcService", url = "http://svc-service:80")
public interface SvcServiceClient {
    @RequestMapping(value = "/service/autoUpdateDependencies", method = RequestMethod.POST)
    void addService(@RequestBody  List<DependencyBean> dependencyBeans);
}
