package com.hitices.route.client;

import com.hitices.route.config.JaegerConfig;
import com.hitices.route.json.Graph;
import com.hitices.route.json.Trace;
import com.hitices.route.json.TraceData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: wangteng
 * @e-mail: Willtynn@outlook.com
 * @date: 2023/8/7 18:43
 */
@Slf4j
@Component
public class JaegerClient {
    public static RestTemplate restTemplate = new RestTemplate();

    public List<String> getService(){
        List<String> services = JaegerClient.objToList(restTemplate.getForEntity(JaegerConfig.url+JaegerConfig.getService, LinkedHashMap.class).getBody().get("data"),String.class);
        services.remove("jagger.observability");
        services.remove("jaeger-query");
        return services;
    }

    public List<Trace> getTrace(String service, Long start, Long end){
        return restTemplate.getForEntity(String.format("http://192.168.1.104:30284/api/traces?end=%d000&limit=1500&lookback=15m&maxDuration=&minDuration=&service=%s&start=%d000",
                        end, service,start),
                TraceData.class).getBody().getData();
    }

    public Graph getGraph(Long duration, String namespace){
        return restTemplate.getForEntity(String.format(JaegerConfig.kiali+JaegerConfig.getGraph,duration,namespace),
                Graph.class).getBody();
    }


    public static <T> List<T> objToList(Object obj, Class<T> cla){
        List<T> list = new ArrayList<T>();
        if (obj instanceof ArrayList<?>) {
            for (Object o : (List<?>) obj) {
                list.add(cla.cast(o));
            }
            return list;
        }
        return null;
    }

    public static void main(String[] args) {
        JaegerClient jaegerClient = new JaegerClient();
        System.out.println(jaegerClient.getGraph(604800L, "default").getElements().toString());
    }
}
