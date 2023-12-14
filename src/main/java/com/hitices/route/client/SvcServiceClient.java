package com.hitices.route.client;

import com.hitices.route.bean.DependencyBean;
import com.hitices.route.bean.svcservicebeans.DependencyDescription;
import com.hitices.route.bean.svcservicebeans.Service;
import com.hitices.route.bean.svcservicebeans.ServiceIdBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "SvcService", url = "http://svc-service:80")
public interface SvcServiceClient {
    @RequestMapping(value = "/service/autoUpdateDependencies", method = RequestMethod.POST)
    void autoUpdateDependency(@RequestBody  List<DependencyBean> dependencyBeans);
    @RequestMapping(value = "/service/addDependencies", method = RequestMethod.POST)
    void addDependency(@RequestBody  List<DependencyDescription> descriptions);
    @RequestMapping(value = "/service/getById", method = RequestMethod.POST)
    List<Service> getServicesById(@RequestBody ServiceIdBean serviceId);
}
