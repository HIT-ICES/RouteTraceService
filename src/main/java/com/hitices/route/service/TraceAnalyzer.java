package com.hitices.route.service;

import com.google.gson.Gson;
import com.hitices.route.bean.*;
import com.hitices.route.bean.svcservicebeans.DependencyDescription;
import com.hitices.route.bean.svcservicebeans.Interface;
import com.hitices.route.bean.svcservicebeans.Service;
import com.hitices.route.bean.svcservicebeans.ServiceIdBean;
import com.hitices.route.client.JaegerClient;
import com.hitices.route.client.KubeSphereClient;
import com.hitices.route.client.SvcServiceClient;
import com.hitices.route.entity.TraceEntity;
import com.hitices.route.json.*;
import com.hitices.route.repository.TraceRepository;
import lombok.var;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
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

    @Autowired
    private KubeSphereClient kubeSphereClient;

    Configuration configuration = new Configuration().configure();

    // 创建 SessionFactory
    SessionFactory sessionFactory = configuration.buildSessionFactory();

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
//    @Scheduled(cron = "*/30 * * * * ?")
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
                    var calleePath = new URL(span.getTag("http.url")).getPath();
                    var callee = ifDict.getInterface(calleeService, calleePath, span.getTag("http.method"));
                    if (callee == null) continue;
                    var callerIp = span.getTag("peer.address");// caller-ip
                    var calleeIp = span.getTag("node_id").split("~")[1]; // callee-ip
                    hostApiMap.put(calleeIp, callee);
                    var caller = hostApiMap.getOrDefault(calleeIp, null);
                    if (caller == null) continue;
                    var callees = graph.getOrDefault(caller.Id(), null);
                    if (callees == null) {
                        callees = new HashMap<>();
                        graph.put(caller.Id(), callees);
                    }
                    if (callees.containsKey(callee.Id())) continue;
                    var map=new HashMap<String,String>();
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
        traceEntity.setRequestSize(Long.parseLong(trace.getSpans().get(0).getTag("request_size")));
        traceEntity.setResponseSize(Long.parseLong(trace.getSpans().get(0).getTag("response_size")));
        traceEntity.setTime(new Date(trace.getSpans().get(0).getStartTime() / 1000));
        for (Span span : trace.getSpans()) {
            if (service.equals(span.getTag("istio.canonical_service") + "." + span.getTag("istio.namespace"))) {
                URL url = new URL(span.getTag("http.url"));
                traceEntity.setApi(url.getPath());
                break;
            }
        }
        traceEntity.setGraph(gson.toJson(analyzeTraceGraph(trace)));
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
        Gson gson = new Gson();
        List<ServiceBean> serviceBeans = new ArrayList<>();
        // 获取各个服务的数
        HashMap<String, List<TraceBean>> serviceTraceMap = new HashMap<>();
        for (TraceEntity entity : traceRepository.findAllByTimeBetween(new Date(start), new Date(end))) {
            Trace trace = gson.fromJson(entity.getData(), Trace.class);
            if (!serviceTraceMap.containsKey(entity.getService())){
                serviceTraceMap.put(entity.getService(),new ArrayList<>());
            }
            serviceTraceMap.get(entity.getService()).add(new TraceBean(entity.getId(), entity.getService(), entity.getApi(), entity.getTime(),0, trace));

        }
        // 统计各个服务的数据
        for (String service : serviceTraceMap.keySet()) {
            List<TraceBean> traceBeans = serviceTraceMap.get(service);
            HashMap<String, Integer> apiCountMap = new HashMap<>();
            HashMap<String, Date> apiDateMap = new HashMap<>();
            HashMap<String, List<Long>> apiTimeMap = new HashMap<>();
            for (TraceBean traceBean : traceBeans) {
                if (apiCountMap.containsKey(traceBean.getApi())) {
                    apiCountMap.put(traceBean.getApi(), apiCountMap.get(traceBean.getApi()) + 1);
                } else {
                    apiCountMap.put(traceBean.getApi(), 1);
                }
                if (!apiDateMap.containsKey(traceBean.getApi())) {
                    apiDateMap.put(traceBean.getApi(), traceBean.getTime());
                } else if (apiDateMap.get(traceBean.getApi()).compareTo(traceBean.getTime())<0){
                    apiDateMap.put(traceBean.getApi(), traceBean.getTime());
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
                        , time.get(time.size() - 1),apiDateMap.get(api)));
            }

        }
        return serviceBeans;
    }

    private Long getPercentile(List<Long> time, double percent) {
        int percentileIndex = (int) Math.ceil(time.size() * percent);
        return time.get(percentileIndex - 1);
    }

    public List<TraceBean> getApiTrace(Long start, Long end, String service, String api) {
        Gson gson = new Gson();
        List<TraceBean> traces = new ArrayList<>();
        for (TraceEntity entity : traceRepository.findAllByTimeBetweenAndServiceIsAndApiIsOrderByDataDesc(new Date(start), new Date(end), service, api)) {
            Trace trace = gson.fromJson(entity.getData(), Trace.class);
            GraphBean graphBean = gson.fromJson(entity.getGraph(), GraphBean.class);
            traces.add(new TraceBean(entity.getId(), entity.getService(), entity.getApi(), entity.getTime(), graphBean.getNodes().size(), trace));
        }
        return traces;
    }

    public GraphBean analyzeTraceGraph(Trace trace) throws MalformedURLException {
        List<EdgeBean> edges = new ArrayList<>();
        HashSet<NodeBean> nodes = new HashSet<>();
        for (Span span:trace.getSpans()){
            String peer = "", local = "", info = "", node = "";
            for (Tag tag:span.getTags()){
                if (tag.getKey().equals("peer.address")){
                    peer = tag.getValue();
                }
                if (tag.getKey().equals("node_id")){
                    local = tag.getValue().split("~")[1];
                    node = tag.getValue().split("~")[2];
                }
                if (tag.getKey().equals("http.url")){
                    info = tag.getValue();
                }
            }
            if (!peer.equals(local)){
                URL url = new URL(info);
                EdgeBean edgeBean = new EdgeBean(peer,local,url.getPath());
                edges.add(edgeBean);
                PodItem item = kubeSphereClient.getPodByName(node.split("\\.")[0]).getItems().get(0);
                nodes.add(new NodeBean(local, node, item.getMetadata().getLabels().get("app"), span.getDuration(),item.getStatus().getHostIP()));
            }else {
                PodItem item = kubeSphereClient.getPodByName(node.split("\\.")[0]).getItems().get(0);
                nodes.add(new NodeBean(peer, node, item.getMetadata().getLabels().get("app"), span.getDuration(), item.getStatus().getHostIP()));
            }
        }
        return new GraphBean(edges,nodes);
    }

    public GraphBean getTraceGraph(Long id){
        Gson gson = new Gson();
        TraceEntity entity = traceRepository.findById(id);
        return gson.fromJson(entity.getGraph(), GraphBean.class);
    }
}
