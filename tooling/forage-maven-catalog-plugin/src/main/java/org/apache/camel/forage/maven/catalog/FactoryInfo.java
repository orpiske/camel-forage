package org.apache.camel.forage.maven.catalog;

/**
 * Represents information about a ForageFactory annotation found in the codebase.
 */
public class FactoryInfo {
    private String name;
    private String component;
    private String description;
    private String factoryType;
    private String className;

    public FactoryInfo() {}

    public FactoryInfo(String name, String component, String description, String factoryType, String className) {
        this.name = name;
        this.component = component;
        this.description = description;
        this.factoryType = factoryType;
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

    public String getFactoryType() {
        return factoryType;
    }

    public void setFactoryType(String factoryType) {
        this.factoryType = factoryType;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "FactoryInfo{" + "name='"
                + name + '\'' + ", component='"
                + component + '\'' + ", description='"
                + description + '\'' + ", factoryType='"
                + factoryType + '\'' + ", className='"
                + className + '\'' + '}';
    }
}
