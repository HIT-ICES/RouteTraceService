package com.hitices.route.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author: wangteng
 * @e-mail: Willtynn@outlook.com
 * @date: 2023/10/23 15:17
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DependencyBean {
    private String callerService;
    private String callerInterface;
    private String calleeService;
    private String calleeInterface;
    private Double requestSize;

    private Double responseSize;
}
