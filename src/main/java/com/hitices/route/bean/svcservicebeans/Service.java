package com.hitices.route.bean.svcservicebeans;

import java.util.List;
import java.util.Objects;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class Service {
    private String Id;
    private String Name;
    private String Repo;
    private String ImageUrl;
    private Version Version;
    private List<Interface> Interfaces;
    private Resource IdleResource;
    private Resource DesiredResource;
    private int DesiredCapability;


    public String Id() {
        return Id;
    }

    public String Name() {
        return Name;
    }

    public String Repo() {
        return Repo;
    }

    public String ImageUrl() {
        return ImageUrl;
    }

    public Version Version() {
        return Version;
    }

    public List<Interface> Interfaces() {
        return Interfaces;
    }

    public Resource IdleResource() {
        return IdleResource;
    }

    public Resource DesiredResource() {
        return DesiredResource;
    }

    public int DesiredCapability() {
        return DesiredCapability;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Service) obj;
        return Objects.equals(this.Id, that.Id) &&
                Objects.equals(this.Name, that.Name) &&
                Objects.equals(this.Repo, that.Repo) &&
                Objects.equals(this.ImageUrl, that.ImageUrl) &&
                Objects.equals(this.Version, that.Version) &&
                Objects.equals(this.Interfaces, that.Interfaces) &&
                Objects.equals(this.IdleResource, that.IdleResource) &&
                Objects.equals(this.DesiredResource, that.DesiredResource) &&
                this.DesiredCapability == that.DesiredCapability;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, Name, Repo, ImageUrl, Version, Interfaces, IdleResource, DesiredResource, DesiredCapability);
    }

    @Override
    public String toString() {
        return "Service[" +
                "Id=" + Id + ", " +
                "Name=" + Name + ", " +
                "Repo=" + Repo + ", " +
                "ImageUrl=" + ImageUrl + ", " +
                "Version=" + Version + ", " +
                "Interfaces=" + Interfaces + ", " +
                "IdleResource=" + IdleResource + ", " +
                "DesiredResource=" + DesiredResource + ", " +
                "DesiredCapability=" + DesiredCapability + ']';
    }

}
