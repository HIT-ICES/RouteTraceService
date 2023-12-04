package com.hitices.route.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author: wangteng
 * @e-mail: Willtynn@outlook.com
 * @date: 2023/9/7 9:01
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EdgeBean {
    private String start;
    private String end;
    private String info;

    @Override
    public String toString() {
        return "Edge{" +
                "start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", info='" + info + '\'' +
                '}';
    }
}
