package com.hitices.route.controller;

import com.hitices.route.bean.GroupBean;
import com.hitices.route.bean.ServiceBean;
import com.hitices.route.bean.TraceBean;
import com.hitices.route.client.JaegerClient;
import com.hitices.route.json.Graph;
import com.hitices.route.json.Trace;
import com.hitices.route.service.TraceAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: wangteng
 * @e-mail: Willtynn@outlook.com
 * @date: 2023/8/25 10:07
 */
@CrossOrigin
@RestController
public class MController {

    @Autowired
    private JaegerClient jaegerClient;

    @Autowired
    private TraceAnalyzer traceAnalyzer;

    @GetMapping("/graph")
    public Graph getGraph(@RequestParam("duration") Long duration, @RequestParam("namespace") String namespace){
        return jaegerClient.getGraph(duration,namespace);
    }

    @GetMapping("/trace")
    public List<TraceBean> getTrace(@RequestParam("start") Long start, @RequestParam("end") Long end, @RequestParam("service") String service, @RequestParam("api") String api){
        return traceAnalyzer.getApiTrace(start,end,service,api);
    }

    @GetMapping("/trace/detail")
    public GroupBean getTraceGraph(@RequestParam("id") Long id){
        return traceAnalyzer.getTraceGraph(id);
    }

    @GetMapping("/trace/service")
    public List<ServiceBean> getTraceService(@RequestParam("start") Long start, @RequestParam("end") Long end){
        return traceAnalyzer.getTraceService(start, end);
    }

}
