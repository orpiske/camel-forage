package org.apache.camel.forage.core.util.config;

/**
 * Base interface for module configuration in the Camel Forage framework.
 *
 * <p>This interface defines the fundamental contract that all module configurations must implement.
 * It serves as the foundation for a hierarchical configuration system where each module can define
 * its own configuration parameters and access them through a centralized {@link ConfigStore}.
 *
 * <p>Implementations of this interface are typically used to:
 * <ul>
 *   <li>Define module-specific configuration parameters</li>
 *   <li>Provide a unique identifier for the module</li>
 *   <li>Register configuration entries with the {@link ConfigStore}</li>
 *   <li>Access configuration values from environment variables or system properties</li>
 * </ul>
 *
 * <p>Configuration values can be sourced from multiple locations in order of precedence:
 * <ol>
 *   <li>Environment variables</li>
 *   <li>System properties</li>
 *   <li>Configuration files</li>
 * </ol>
 *
 * <p><strong>Example implementation:</strong>
 * <pre>{@code
 * public class MyModuleConfig implements Config {
 *     private static final ConfigModule API_KEY = ConfigModule.of(MyModuleConfig.class, "api-key");
 *
 *     public MyModuleConfig() {
 *         ConfigStore.getInstance().add(API_KEY, ConfigEntry.fromEnv("MY_MODULE_API_KEY"));
 *         ConfigStore.getInstance().add(MyModuleConfig.class, this);
 *     }
 *
 *     @Override
 *     public String name() {
 *         return "my-module";
 *     }
 *
 *     public String apiKey() {
 *         return ConfigStore.getInstance()
 *                 .get(API_KEY)
 *                 .orElseThrow(() -> new MissingConfigException("Missing API key"));
 *     }
 * }
 * }</pre>
 *
 * @see ConfigStore
 * @see ConfigEntry
 * @see ConfigModule
 * @since 1.0
 */
public interface Config {

    /**
     * Returns the unique name identifier for this module configuration.
     *
     * <p>This name is used to identify the module and typically corresponds to the
     * module's artifact name or a logical module identifier. It should be unique
     * across all modules in the system to avoid configuration conflicts.
     *
     * <p>The name is also used when loading configuration files, where a file
     * named "{@code <name>.properties}" will be automatically loaded if present
     * in the classpath.
     *
     * @return the unique name of this module configuration, never {@code null}
     */
    String name();
}
