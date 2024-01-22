package com.hitices.route.json;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author wangteng
 * @e-mail 1638235292@qq.com
 * @date 2023/5/26
 */
@Getter
@Setter
@NoArgsConstructor
public class PodItem {
    private PodMetadata metadata;
    private PodStatus status;
}
