package com.hitices.route.json;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author: wangteng
 * @e-mail: Willtynn@outlook.com
 * @date: 2023/8/25 9:59
 */
@Getter
@Setter
@NoArgsConstructor
public class Graph {
    private Long duration;
    private Elements elements;
    private String graphType;
    private Long timestamp;
}
