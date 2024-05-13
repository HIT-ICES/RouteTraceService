package com.hitices.route.service;

import com.google.gson.Gson;
import com.hitices.route.bean.*;
import com.hitices.route.bean.svcservicebeans.DependencyDescription;
import com.hitices.route.bean.svcservicebeans.Interface;
import com.hitices.route.bean.svcservicebeans.ServiceIdBean;
import com.hitices.route.client.JaegerClient;
import com.hitices.route.client.SvcServiceClient;
import com.hitices.route.entity.TraceEntity;
import com.hitices.route.json.Span;
import com.hitices.route.json.Tag;
import com.hitices.route.json.Trace;
import com.hitices.route.repository.TraceRepository;
import lombok.var;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author: wangteng
 * @e-mail: Willtynn@outlook.com
 * @date: 2023/8/7 19:35
 */
@Slf4j
@Component
public class TraceAnalyzer {

    @Autowired
    private JaegerClient jaegerClient;

    @Autowired
    private TraceRepository traceRepository;

    @Autowired
    private SvcServiceClient svcServiceClient;

    private static ExecutorService executorService = Executors.newFixedThreadPool(20);

    /**
     * 每分钟执行一次
     */
    @Scheduled(cron = "*/30 * * * * ?")
    private void analyzeTrace() {
        long end = System.currentTimeMillis();
        long start = end - 30000;
        List<String> services = jaegerClient.getService();
        for (String service : services) {
            List<Trace> traces = jaegerClient.getTrace(service, start, end);
            for (Trace trace : traces) {
                trace.removeUseless();
                try {
                    saveTrace(service, trace);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    /**
     * @author Ferdinand Su
     * 每分钟执行一次
     */
    @Scheduled(cron = "*/30 * * * * ?")
    private void analyzeTraceAndPush() {
        long end = System.currentTimeMillis();
        long start = end - 30000;
        List<String> services = jaegerClient.getService();
        Map<String, Map<String, Map<String, String>>> graph = new HashMap<>();
        var hostApiMap = new HashMap<String, Interface>();
        var ifDict = new StoredServiceManager(svcServiceClient.getServicesById(ServiceIdBean.ALL).getData());

        // Fetch Step

        for (String service : services) {
            List<Trace> traces = jaegerClient.getTrace(service, start, end);
            RecordTraces(traces, ifDict, hostApiMap, graph);
        }

        // Push Step

        var targets = graph.entrySet().stream()
                .flatMap(entry ->
                        entry.getValue().entrySet().stream()
                                .map(depd ->
                                        new DependencyDescription(entry.getKey(), depd.getKey(), depd.getValue())
                                )
                ).collect(Collectors.toList());
        log.info(targets.size() + " dependencies recorded.");
        svcServiceClient.addDependency(targets);

    }

    /**
     * @param traces
     * @author Ferdinand Su
     */
    void RecordTraces(List<Trace> traces,
                      StoredServiceManager ifDict,
                      Map<String, Interface> hostApiMap,
                      Map<String, Map<String, Map<String, String>>> graph) {
        //
        //log.info("Recording ${traces.Count} Traces");
        for (var trace : traces) {
            trace.removeUseless();
            try {
                for (var span : trace.getSpans()) {
                    var calleeService = span.getTag("istio.canonical_service");
                    if (calleeService == null) continue;
                    var urlTag = span.getTag("http.url");
                    var calleePath = urlTag.startsWith("/") ? urlTag : new URL(urlTag).getPath();
                    var callee = ifDict.getInterface(calleeService, calleePath, span.getTag("http.method"));
                    if (callee == null) continue;
                    var calleeIp = span.getTag("node_id").split("~")[1]; // callee-ip
                    hostApiMap.put(calleeIp, callee);
                    var callerIp = span.getTag("peer.address");// caller-ip
                    var caller = hostApiMap.getOrDefault(callerIp, null);
                    if (caller == null || caller == callee) continue;
                    var callees = graph.getOrDefault(caller.Id(), null);
                    if (callees == null) {
                        callees = new HashMap<>();
                        graph.put(caller.Id(), callees);
                    }
                    if (callees.containsKey(callee.Id())) continue;
                    var map = new HashMap<String, String>();
                    map.put("requestSize", span.getTag("request_size"));
                    map.put("responseSize", span.getTag("response_size"));
                    callees.put(callee.Id(), map);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                continue;
            }
        }
    }


    private void saveTrace(String service, Trace trace) throws MalformedURLException {
        Gson gson = new Gson();
        TraceEntity traceEntity = new TraceEntity();
        traceEntity.setService(service);
        traceEntity.setTraceId(trace.getTraceID());
        traceEntity.setData(gson.toJson(trace));
        traceEntity.setTime(new Date(trace.getSpans().get(0).getStartTime() / 1000));
        for (Span span : trace.getSpans()) {
            if (service.equals(span.getTag("istio.canonical_service") + "." + span.getTag("istio.namespace"))) {
                URL url = new URL(span.getTag("http.url"));
                traceEntity.setApi(url.getPath());
                break;
            }
        }
        log.info(traceEntity.getData());
        traceRepository.save(traceEntity);
        analyzeDependency(traceEntity);
    }

    private void analyzeDependency(TraceEntity entity) throws MalformedURLException {
        Gson gson = new Gson();
        List<DependencyBean> dependencyBeans = new ArrayList<>();
        Map<String, String> svc = new HashMap<>();
        Map<String, String> api = new HashMap<>();
        Trace trace = gson.fromJson(entity.getData(), Trace.class);
        for (Span span : trace.getSpans()) {
            String peer = span.getTag("peer.address");
            String local = span.getTag("node_id").split("~")[1];
            String info = new URL(span.getTag("http.url")).getPath();
            String service = span.getTag("istio.canonical_service");
            Double request = Double.valueOf(span.getTag("request_size"));
            Double response = Double.valueOf(span.getTag("response_size"));
            svc.put(local, service);
            api.put(local, info);
            if (!peer.equals(local)) {
                if (svc.containsKey(peer)) {
                    DependencyBean dependencyBean = new DependencyBean(svc.get(peer), api.get(peer), svc.get(local), api.get(local), request, response);
                    dependencyBeans.add(dependencyBean);
                }
            }
        }
        if (!dependencyBeans.isEmpty()) {
            log.info(gson.toJson(dependencyBeans));
            svcServiceClient.autoUpdateDependency(dependencyBeans);
        }
    }

    public List<ServiceBean> getTraceService(Long start, Long end) {
        List<ServiceBean> serviceBeans = new ArrayList<>();
        for (String service : jaegerClient.getService()) {
            List<TraceBean> traceBeans = getTrace(start, end, service);
            HashMap<String, Integer> apiCountMap = new HashMap<>();
            HashMap<String, List<Long>> apiTimeMap = new HashMap<>();
            for (TraceBean traceBean : traceBeans) {
                if (apiCountMap.containsKey(traceBean.getApi())) {
                    apiCountMap.put(traceBean.getApi(), apiCountMap.get(traceBean.getApi()) + 1);
                } else {
                    apiCountMap.put(traceBean.getApi(), 1);
                }
                if (apiTimeMap.containsKey(traceBean.getApi())) {
                    apiTimeMap.get(traceBean.getApi()).add(traceBean.getTrace().getSpans().get(0).getDuration());
                } else {
                    List<Long> time = new ArrayList<>();
                    time.add(traceBean.getTrace().getSpans().get(0).getDuration());
                    apiTimeMap.put(traceBean.getApi(), time);
                }
            }
            for (String api : apiCountMap.keySet()) {
                List<Long> time = apiTimeMap.get(api);
                Collections.sort(time);
                serviceBeans.add(new ServiceBean(service, api, apiCountMap.get(api), time.get(0)
                        , getPercentile(time, 0.5)
                        , getPercentile(time, 0.95)
                        , getPercentile(time, 0.99)
                        , time.get(time.size() - 1)));
            }

        }
        return serviceBeans;
    }

    private Long getPercentile(List<Long> time, double percent) {
        int percentileIndex = (int) Math.ceil(time.size() * percent);
        return time.get(percentileIndex - 1);
    }


    public List<TraceBean> getTrace(Long start, Long end, String service) {
        Gson gson = new Gson();
        List<TraceBean> traces = new ArrayList<>();
        for (TraceEntity entity : traceRepository.findAllByTimeBetweenAndServiceIsOrderByDataDesc(new Date(start), new Date(end), service)) {
            Trace trace = gson.fromJson(entity.getData(), Trace.class);
            traces.add(new TraceBean(entity.getId(), entity.getService(), entity.getApi(), entity.getTime(), trace));
        }
        return traces;
    }

    public List<TraceBean> getApiTrace(Long start, Long end, String service, String api) {
        Gson gson = new Gson();
        List<TraceBean> traces = new ArrayList<>();
        for (TraceEntity entity : traceRepository.findAllByTimeBetweenAndServiceIsAndApiIsOrderByDataDesc(new Date(start), new Date(end), service, api)) {
            Trace trace = gson.fromJson(entity.getData(), Trace.class);
            traces.add(new TraceBean(entity.getId(), entity.getService(), entity.getApi(), entity.getTime(), trace));
        }
        return traces;
    }

    public GroupBean getTraceGraph(Long id) {
        Gson gson = new Gson();
        TraceEntity entity = traceRepository.findById(id);
        List<EdgeBean> edges = new ArrayList<>();
        HashSet<NodeBean> nodes = new HashSet<>();
        Trace trace = gson.fromJson(entity.getData(), Trace.class);
        for (Span span : trace.getSpans()) {
            String peer = "", local = "", info = "", node = "";
            for (Tag tag : span.getTags()) {
                if (tag.getKey().equals("peer.address")) {
                    peer = tag.getValue();
                }
                if (tag.getKey().equals("node_id")) {
                    local = tag.getValue().split("~")[1];
                    node = tag.getValue().split("~")[2];
                }
                if (tag.getKey().equals("http.url")) {
                    info = tag.getValue();
                }
            }
            if (!peer.equals(local)) {
                EdgeBean edgeBean = new EdgeBean(peer, local, info);
                edges.add(edgeBean);
                nodes.add(new NodeBean(peer, ""));
                nodes.add(new NodeBean(local, node));
            }
        }
        return new GroupBean(edges, nodes);
    }

}
