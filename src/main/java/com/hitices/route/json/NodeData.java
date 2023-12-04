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
public class NodeData {
    private String app;
    private String cluster;
    private String id;
    private String namespace;
    private String nodeType;
    private String service;
}
