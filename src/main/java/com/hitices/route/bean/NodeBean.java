package com.hitices.route.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * @author: wangteng
 * @e-mail: Willtynn@outlook.com
 * @date: 2023/9/7 9:17
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NodeBean {
    private String ip;
    private String name;
    private String service;
    private Long duration;
    private String host_ip;

    @Override
    public String toString() {
        return "NodeBean{" +
                "ip='" + ip + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeBean nodeBean = (NodeBean) o;
        return Objects.equals(ip, nodeBean.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip);
    }
}
