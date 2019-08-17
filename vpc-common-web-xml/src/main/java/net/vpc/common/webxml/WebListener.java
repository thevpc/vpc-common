package net.vpc.common.webxml;

import java.util.Objects;

public class WebListener {
    private String icon;
    private String displayName;
    private String description;
    private String listenerClass;

    public WebListener() {
    }

    public WebListener(String listenerClass, String displayName, String description, String icon) {
        this.icon = icon;
        this.displayName = displayName;
        this.description = description;
        this.listenerClass = listenerClass;
    }

    public String getIcon() {
        return icon;
    }

    public WebListener setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public WebListener setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public WebListener setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getListenerClass() {
        return listenerClass;
    }

    public WebListener setListenerClass(String listenerClass) {
        this.listenerClass = listenerClass;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebListener that = (WebListener) o;
        return Objects.equals(icon, that.icon) &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(listenerClass, that.listenerClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(icon, displayName, description, listenerClass);
    }
}
