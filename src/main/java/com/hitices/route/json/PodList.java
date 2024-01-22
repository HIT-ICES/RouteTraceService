package com.hitices.route.json;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author wangteng
 * @e-mail 1638235292@qq.com
 * @date 2023/5/26
 */
@Getter
@Setter
@NoArgsConstructor
public class PodList {
    private List<PodItem> items;
    private Integer totalItems;
}
