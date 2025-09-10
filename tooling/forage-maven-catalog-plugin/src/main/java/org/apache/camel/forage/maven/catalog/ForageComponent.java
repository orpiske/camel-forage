package org.apache.camel.forage.maven.catalog;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

/**
 * Represents a single Camel Forage component in the catalog.
 */
public class ForageComponent {

    @JsonProperty("artifactId")
    private String artifactId;

    @JsonProperty("groupId")
    private String groupId;

    @JsonProperty("version")
    private String version;

    @JsonProperty("configurationProperties")
    private List<ConfigurationProperty> configurationProperties;

    @JsonProperty("capabilities")
    private Map<String, Object> capabilities;

    @JsonProperty("beans")
    private List<ForgeBeanInfo> beans;

    @JsonProperty("factories")
    private List<FactoryInfo> factories;

    public ForageComponent() {}

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<ConfigurationProperty> getConfigurationProperties() {
        return configurationProperties;
    }

    public void setConfigurationProperties(List<ConfigurationProperty> configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    public Map<String, Object> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Map<String, Object> capabilities) {
        this.capabilities = capabilities;
    }

    public List<ForgeBeanInfo> getBeans() {
        return beans;
    }

    public void setBeans(List<ForgeBeanInfo> beans) {
        this.beans = beans;
    }

    public List<FactoryInfo> getFactories() {
        return factories;
    }

    public void setFactories(List<FactoryInfo> factories) {
        this.factories = factories;
    }
}
