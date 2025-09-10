package org.apache.camel.forage.maven.catalog;

/**
 * Represents information about a ForageBean annotation found in the codebase.
 */
public class ForgeBeanInfo {
    private String name;
    private String component;
    private String description;
    private String className;

    public ForgeBeanInfo() {}

    public ForgeBeanInfo(String name, String component, String description, String className) {
        this.name = name;
        this.component = component;
        this.description = description;
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "ForgeBeanInfo{" + "name='"
                + name + '\'' + ", component='"
                + component + '\'' + ", description='"
                + description + '\'' + ", className='"
                + className + '\'' + '}';
    }
}
