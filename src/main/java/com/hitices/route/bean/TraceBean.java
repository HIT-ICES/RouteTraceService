package com.hitices.route.bean;

import com.hitices.route.json.Trace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author: wangteng
 * @e-mail: Willtynn@outlook.com
 * @date: 2023/9/7 8:32
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TraceBean {
    private Long id;
    private String service;
    private String api;
    private Date time;
    private Trace trace;
}
