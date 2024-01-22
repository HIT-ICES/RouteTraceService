package com.hitices.route.json;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author wangteng
 * @e-mail 1638235292@qq.com
 * @date 2023/5/26
 */
@Getter
@Setter
@NoArgsConstructor
public class PodStatus {
    private String phase;
    private String hostIP;
    private String podIP;
    private Date startTime;
}
