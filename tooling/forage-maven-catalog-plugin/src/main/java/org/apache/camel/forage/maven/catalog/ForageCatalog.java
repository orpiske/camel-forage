package org.apache.camel.forage.maven.catalog;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Root catalog data structure containing all discovered Forage components.
 */
public class ForageCatalog {

    @JsonProperty("version")
    private String version;

    @JsonProperty("generatedBy")
    private String generatedBy;

    @JsonProperty("timestamp")
    private long timestamp;

    @JsonProperty("components")
    private List<ForageComponent> components;

    public ForageCatalog() {}

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<ForageComponent> getComponents() {
        return components;
    }

    public void setComponents(List<ForageComponent> components) {
        this.components = components;
    }
}
