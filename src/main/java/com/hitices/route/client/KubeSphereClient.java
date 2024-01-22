package com.hitices.route.client;


import com.hitices.route.config.KubeSphereConfig;
import com.hitices.route.json.PodList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author wangteng
 * @e-mail 1638235292@qq.com
 * @date 2023/5/6
 */
@Slf4j
@Component
public class KubeSphereClient {

    public static RestTemplate restTemplate = new RestTemplate();

    public PodList getPodByName(String podName){
        return restTemplate.getForEntity(KubeSphereConfig.url+String.format(KubeSphereConfig.pod_name ,podName), PodList.class).getBody();
    }
}
