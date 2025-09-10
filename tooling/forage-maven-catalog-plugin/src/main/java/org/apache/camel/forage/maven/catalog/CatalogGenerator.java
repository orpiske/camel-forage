package org.apache.camel.forage.maven.catalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

/**
 * Core catalog generation logic for Camel Forage components.
 *
 * This class is responsible for scanning Maven project dependencies,
 * discovering Forage components, and generating various catalog formats.
 */
public class CatalogGenerator {

    private final Log log;
    private final ObjectMapper objectMapper;

    private String format = "json";

    public CatalogGenerator(Log log) {
        this.log = log;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Generate a catalog from the given Maven project.
     *
     * @param project the Maven project to scan
     * @param outputDirectory the directory to write catalog files
     * @return the catalog generation result
     * @throws IOException if file operations fail
     */
    public CatalogResult generateCatalog(MavenProject project, File outputDirectory) throws IOException {
        log.info("Scanning project dependencies for Forage components...");

        // Discover components
        List<ForageComponent> components = discoverComponents(project);

        log.info("Found " + components.size() + " Forage components");

        // Filter components based on include/exclude criteria
        List<ForageComponent> filteredComponents = filterComponents(components);

        log.info("Generating catalog with " + filteredComponents.size() + " components");

        // Create catalog data structure
        ForageCatalog catalog = new ForageCatalog();
        catalog.setVersion("1.0");
        catalog.setComponents(filteredComponents);
        catalog.setGeneratedBy("forage-maven-catalog-plugin");
        catalog.setTimestamp(System.currentTimeMillis());

        // Generate output files
        List<File> generatedFiles = new ArrayList<>();

        switch (format.toLowerCase()) {
            case "json":
                generatedFiles.add(generateJsonCatalog(catalog, outputDirectory));
                break;
            case "yaml":
                generatedFiles.add(generateYamlCatalog(catalog, outputDirectory));
                break;
            default:
                // Generate all formats
                generatedFiles.add(generateJsonCatalog(catalog, outputDirectory));
                generatedFiles.add(generateYamlCatalog(catalog, outputDirectory));
        }

        return new CatalogResult(components.size(), generatedFiles);
    }

    private List<ForageComponent> discoverComponents(MavenProject project) {
        List<ForageComponent> components = new ArrayList<>();

        //        Set<Artifact> artifacts = scanTransitive ? project.getArtifacts() : project.getDependencyArtifacts();
        Set<Artifact> artifacts = project.getArtifacts();

        if (artifacts == null) {
            log.warn("No artifacts found in project");
            return components;
        }

        for (Artifact artifact : artifacts) {
            if (isForageComponent(artifact)) {
                log.debug("Processing Forage component: " + artifact.getArtifactId());
                ForageComponent component = createComponentFromArtifact(artifact, project.getParent());
                components.add(component);
            }
        }

        return components;
    }

    private boolean isForageComponent(Artifact artifact) {
        return artifact.getGroupId().startsWith("org.apache.camel.forage")
                && artifact.getArtifactId().startsWith("forage-");
    }

    private ForageComponent createComponentFromArtifact(Artifact artifact, MavenProject rootProject) {
        ForageComponent component = new ForageComponent();
        component.setArtifactId(artifact.getArtifactId());
        component.setGroupId(artifact.getGroupId());
        component.setVersion(artifact.getVersion());

        // Scan for ForageBean annotations
        CodeScanner codeScanner = new CodeScanner(log);

        List<ForgeBeanInfo> beans = codeScanner.scanForForageBeans(artifact, rootProject);
        component.setBeans(beans);

        // Scan for ForageFactory annotations
        List<FactoryInfo> factories = codeScanner.scanForForageFactories(artifact, rootProject);
        component.setFactories(factories);

        // Scan for configuration properties from ConfigEntries classes
        List<ConfigurationProperty> configProperties =
                codeScanner.scanForConfigurationProperties(artifact, rootProject);
        component.setConfigurationProperties(configProperties);

        return component;
    }

    private List<ForageComponent> filterComponents(List<ForageComponent> components) {
        return components.stream().filter(this::shouldIncludeComponent).collect(Collectors.toList());
    }

    private boolean shouldIncludeComponent(ForageComponent component) {
        return true;
    }

    private File generateJsonCatalog(ForageCatalog catalog, File outputDirectory) throws IOException {
        File outputFile = new File(outputDirectory, "forage-catalog.json");
        objectMapper.writeValue(outputFile, catalog);
        return outputFile;
    }

    private File generateYamlCatalog(ForageCatalog catalog, File outputDirectory) throws IOException {
        // For now, generate YAML as formatted JSON (would use YAML mapper in production)
        File outputFile = new File(outputDirectory, "forage-catalog.yaml");
        objectMapper.writeValue(outputFile, catalog);
        return outputFile;
    }

    // Setters for configuration
    public void setFormat(String format) {
        this.format = format;
    }
}
