package com.hitices.route.json;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author: wangteng
 * @e-mail: Willtynn@outlook.com
 * @date: 2023/8/7 19:29
 */

@Getter
@Setter
@NoArgsConstructor
public class TraceData {
    private List<Trace> data;
}
