package com.hitices.route.bean.svcservicebeans;

import java.util.List;

public record Service(String Id, String Name, String Repo, String ImageUrl, Version Version,
                      List<Interface> Interfaces,
                      Resource IdleResource,
                      Resource DesiredResource, int DesiredCapability) {
}
