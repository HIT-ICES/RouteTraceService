package com.hitices.route.bean.svcservicebeans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MResponse <T>{
    public T data;
    public int code;
    public String message;
}
