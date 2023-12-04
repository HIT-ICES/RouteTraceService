package com.hitices.route.json;

import com.hitices.route.config.JaegerConfig;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

/**
 * @author: wangteng
 * @e-mail: Willtynn@outlook.com
 * @date: 2023/8/7 19:15
 */
@Getter
@Setter
@NoArgsConstructor
public class Span {
    private String traceID;
    private String spanID;
    private Long startTime;
    private Long duration;
    private List<Tag> tags;

    public void removeUselessTag(){
        tags.removeIf(tag -> !JaegerConfig.tags.contains(tag.getKey()));
    }

    public String getTag(String key){
        Optional<Tag> firstTag = tags.stream()
                .filter(tag -> tag.getKey().equals(key))
                .findFirst();
        if (firstTag.isPresent()){
            return firstTag.get().getValue();
        }else {
            return "0";
        }
    }
}
