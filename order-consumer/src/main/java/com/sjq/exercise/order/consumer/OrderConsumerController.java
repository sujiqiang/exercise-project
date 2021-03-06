package com.sjq.exercise.order.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@RestController
@RequestMapping("/order")
public class OrderConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/getUserServerInfo")
    public String getUserServerInfo() {
        // 注意：使用了 @LoadBalanced 之后，这里不能用 ip地址，需要使用注册的服务名
        //return restTemplate.getForObject("http://127.0.0.1:9999/user/serverInfo", String.class);
        return restTemplate.getForObject("http://USER-SERVICE:9999/user/serverInfo", String.class);
    }

    @GetMapping("/getUserServerInfo2")
    public String getUserSerInfo2() {
        //获取注册中心注册的服务实例
        List<ServiceInstance> instances = discoveryClient.getInstances("user-service");
        if (!CollectionUtils.isEmpty(instances)) {
            ServiceInstance instance = instances.get(0);
            // 注意：使用了 @LoadBalanced 之后，这里不能用 ip地址，需要使用注册的服务名
            String url = "http://" + instance.getServiceId() + ":" + instance.getPort() + "/user/serverInfo";
            System.out.println("url = " + url);
            return restTemplate.getForObject(url, String.class);
        } else {
            return "获取服务实例失败";
        }
    }


}
