package com.hitices.route.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;

/**
 * @author: wangteng
 * @e-mail: Willtynn@outlook.com
 * @date: 2023/9/7 9:17
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupBean {
    private List<EdgeBean> edges;
    private HashSet<NodeBean> nodes;
}
