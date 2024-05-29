package com.hitices.route.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author: wangteng
 * @e-mail: Willtynn@outlook.com
 * @date: 2023/9/11 9:40
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceBean {
    private String service;
    private String api;
    private int count;
    private Long low;
    private Long percentile50;
    private Long percentile95;
    private Long percentile99;
    private Long high;
    private Date time;
}
