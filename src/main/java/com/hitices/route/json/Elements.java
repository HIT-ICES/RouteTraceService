package com.hitices.route.json;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author: wangteng
 * @e-mail: Willtynn@outlook.com
 * @date: 2023/8/25 10:00
 */
@Getter
@Setter
@NoArgsConstructor
public class Elements {
    private List<Edge> edges;
    private List<Node> nodes;
}
