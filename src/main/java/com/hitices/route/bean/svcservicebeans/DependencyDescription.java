package com.hitices.route.bean.svcservicebeans;

import java.util.Map;

public record DependencyDescription(String Caller, String Callee, Map<String, String> ExtraData) {
}
