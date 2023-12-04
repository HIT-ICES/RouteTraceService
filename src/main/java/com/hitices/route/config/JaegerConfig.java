package com.hitices.route.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: wangteng
 * @e-mail: Willtynn@outlook.com
 * @date: 2023/8/7 18:48
 */
@Configuration
public class JaegerConfig {
    /**
     * The Jaeger url
     */
    public static String url = "http://192.168.1.104:30284";

    /**
     * The kiali url
     */
    public static String kiali = "http://192.168.1.104:30594";

    /**
     * Jaeger get service api path
     */
    public static String getService = "/api/services";

    /**
     * kiali get graph api path
     */
    public static String getGraph = "/kiali/api/namespaces/graph" +
            "?duration=%ds&graphType=service&includeIdleEdges=false&injectServiceNodes=false" +
            "&boxBy=cluster,namespace&appenders=deadNode,istio,serviceEntry,sidecarsCheck,workloadEntry,health" +
            "&rateGrpc=requests&rateHttp=requests&rateTcp=sent&namespaces=%s";

    public static List<String> tags = new ArrayList<>(Arrays.asList(
            "http.url",
            "istio.namespace",
            "http.method",
            "peer.address",
            "node_id",
            "http.status_code",
            "istio.canonical_service",
            "istio.namespace",
            "request_size",
            "response_size"));

    @Value("${JaegerConfig.url}")
    public void setUrl(String url) {
        JaegerConfig.url = url;
    }

    @Value("${JaegerConfig.kiali}")
    public void setKiali(String kiali) {
        JaegerConfig.kiali = kiali;
    }
}
