package org.apache.camel.forage.core.util.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.BiConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Centralized configuration store for the Camel Forage framework that manages configuration values
 * from multiple sources with a defined precedence hierarchy.
 *
 * <p>The ConfigStore implements a singleton pattern and serves as the central repository for all
 * configuration values in the application. It supports loading configuration from multiple sources
 * and provides a consistent API for accessing configuration values.
 *
 * <p><strong>Configuration Source Precedence (highest to lowest):</strong>
 * <ol>
 *   <li>Environment variables</li>
 *   <li>System properties</li>
 *   <li>Configuration files (loaded via URL or classpath)</li>
 * </ol>
 *
 * <p>The store automatically resolves configuration values by checking sources in the above order,
 * returning the first non-null value found. This allows for flexible configuration management where
 * environment-specific values can override defaults without code changes.
 *
 * <p><strong>Usage Example:</strong>
 * <pre>{@code
 * // Register configuration entries
 * ConfigModule apiKey = ConfigModule.of(MyConfig.class, "api-key");
 * ConfigEntry entry = ConfigEntry.fromEnv("MY_API_KEY");
 * ConfigStore.getInstance().add(apiKey, entry);
 *
 * // Retrieve configuration values
 * String value = ConfigStore.getInstance().get(apiKey)
 *         .orElseThrow(() -> new MissingConfigException("API key not configured"));
 * }</pre>
 *
 * <p><strong>Thread Safety:</strong>
 * This class is thread-safe for concurrent reads after initialization. However, configuration
 * registration (add methods) should typically be performed during application startup before
 * concurrent access begins.
 *
 * @see Config
 * @see ConfigModule
 * @see ConfigEntry
 * @since 1.0
 */
public final class ConfigStore {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigStore.class);

    private static ConfigStore INSTANCE;
    private final Properties properties = new Properties();

    /**
     * Private constructor to enforce singleton pattern.
     */
    private ConfigStore() {}

    /**
     * Returns the singleton instance of the ConfigStore.
     *
     * <p>This method is thread-safe and implements lazy initialization. The same instance
     * will be returned for all calls within the same JVM.
     *
     * @return the singleton ConfigStore instance
     */
    public static synchronized ConfigStore getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }

        INSTANCE = new ConfigStore();
        return INSTANCE;
    }

    /**
     * Adds multiple configuration entries and properties to the store.
     *
     * <p>This method provides a bulk operation for registering multiple configuration entries
     * at once. The provided properties are merged with the existing properties, and each
     * ConfigModule-ConfigEntry pair is processed to resolve values from their respective sources.
     *
     * @param entries a map of ConfigModule to ConfigEntry pairs to register
     * @param properties additional properties to add to the store
     */
    public void add(Map<ConfigModule, ConfigEntry> entries, Properties properties) {
        this.properties.putAll(properties);

        for (Map.Entry<ConfigModule, ConfigEntry> entry : entries.entrySet()) {
            add(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Registers a configuration module with its corresponding configuration entry.
     *
     * <p>This method attempts to resolve a value for the given ConfigEntry by checking
     * environment variables and system properties in order of precedence. If a value
     * is found, it is stored in the internal properties using the ConfigModule as the key.
     *
     * <p>If no value is found from any source, nothing is stored, and subsequent calls
     * to {@link #get(ConfigModule)} will return an empty Optional.
     *
     * @param module the configuration module that serves as the key
     * @param entry the configuration entry that defines where to look for values
     */
    public void add(ConfigModule module, ConfigEntry entry) {
        final Optional<String> read = read(entry);

        read.ifPresent(s -> properties.put(module, s));
    }

    /**
     * Registers a configuration instance and attempts to load its associated properties file.
     *
     * <p>This method looks for a properties file named after the configuration instance's
     * {@link Config#name()} method in the same package as the configuration class. If found,
     * the properties are loaded and added to the store.
     *
     * <p>For example, if the config name is "my-module", it will look for "my-module.properties"
     * in the classpath relative to the configuration class.
     *
     * @param clazz the configuration class
     * @param instance the configuration instance
     * @param <T> the type of the configuration class
     */
    public <T extends Config> void add(Class<T> clazz, T instance, BiConsumer<String, String> registerFunction) {
        LOG.info("Adding {} to {}", clazz, asProperties(instance));

        File file = Paths.get("", asProperties(instance)).toAbsolutePath().toFile();
        if (file.exists()) {
            LOG.info("Found existing config file {}", file);

            try (FileInputStream fis = new FileInputStream(file)) {
                Properties props = new Properties();

                props.load(fis);

                props.forEach((k, v) -> registerFunction.accept((String) k, (String) v));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static <T extends Config> String asProperties(T instance) {
        return "./" + instance.name() + ".properties";
    }

    /**
     * Loads properties from the specified URL and adds them to the store.
     *
     * <p>This method provides a way to load configuration from external sources such as
     * files, classpath resources, or network locations. The properties are loaded using
     * standard Java Properties format and merged with existing properties in the store.
     *
     * <p>If the URL is null, this method does nothing. If an I/O error occurs while
     * reading from the URL, a RuntimeException is thrown.
     *
     * @param url the URL to load properties from, may be null
     * @throws RuntimeException if an I/O error occurs while loading properties
     */
    public void add(URL url) {
        if (url == null) {
            LOG.warn("URL is null");
            return;
        }

        try (InputStream is = url.openStream()) {
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads a configuration value from the sources defined in the ConfigEntry.
     *
     * <p>This method implements the configuration source precedence by checking:
     * <ol>
     *   <li>Environment variables (via {@link System#getenv(String)})</li>
     *   <li>System properties (via {@link System#getProperty(String)})</li>
     * </ol>
     *
     * <p>The first non-null value found is returned. If no value is found from any source,
     * an empty Optional is returned.
     *
     * @param entry the configuration entry defining where to look for values
     * @return an Optional containing the configuration value, or empty if not found
     */
    private Optional<String> read(ConfigEntry entry) {
        if (entry != null) {
            if (entry.envName() != null && !entry.envName().isEmpty()) {
                final String environmentValue = System.getenv(entry.envName());
                if (environmentValue != null) {
                    return Optional.of(environmentValue);
                }
            }

            if (entry.propertyName() != null && !entry.propertyName().isEmpty()) {
                final String propertyValue = System.getProperty(entry.propertyName());
                if (propertyValue != null) {
                    return Optional.of(propertyValue);
                }
            }
        }

        return Optional.empty();
    }

    /**
     * Retrieves a configuration value for the specified ConfigModule.
     *
     * <p>This method returns the configuration value that was previously stored for the
     * given ConfigModule, either through direct registration via {@link #add(ConfigModule, ConfigEntry)}
     * or through properties loaded from files.
     *
     * <p>If no value was found during registration or if the ConfigModule was never registered,
     * an empty Optional is returned.
     *
     * @param entry the configuration module to look up
     * @return an Optional containing the configuration value, or empty if not found
     */
    public Optional<String> get(ConfigModule entry) {
        return Optional.ofNullable((String) properties.get(entry));
    }

    /**
     * Sets a configuration value directly for the specified ConfigModule.
     *
     * <p>This method allows direct assignment of configuration values, bypassing the normal
     * configuration source resolution process. It immediately stores the provided value in
     * the internal properties store, overriding any previously stored value for the same
     * ConfigModule.
     *
     * <p>This method is primarily used by:
     * <ul>
     *   <li>Dynamic configuration registration through {@link Config#register(String, String)}</li>
     *   <li>Configuration loading from property files during startup</li>
     *   <li>Runtime configuration updates in specific scenarios</li>
     *   <li>Testing scenarios where configuration values need to be controlled directly</li>
     * </ul>
     *
     * <p><strong>Usage Context:</strong>
     * Unlike the {@link #add(ConfigModule, ConfigEntry)} method which resolves values from
     * environment variables and system properties, this method directly sets the value without
     * any source resolution. This makes it suitable for scenarios where the value has already
     * been resolved or comes from a different source (like configuration files).
     *
     * <p><strong>Example Usage:</strong>
     * <pre>{@code
     * // Direct value assignment (typically from Config.register implementations)
     * ConfigModule apiKey = ConfigModule.of(MyConfig.class, "api.key");
     * ConfigStore.getInstance().set(apiKey, "resolved-api-key-value");
     *
     * // The value is immediately available for retrieval
     * String value = ConfigStore.getInstance().get(apiKey).orElse("default");
     * }</pre>
     *
     * <p><strong>Precedence Override:</strong>
     * Values set through this method will override any values that might have been previously
     * registered through environment variables or system properties for the same ConfigModule.
     * Subsequent calls to {@link #get(ConfigModule)} will return the value set by this method.
     *
     * <p><strong>Thread Safety:</strong>
     * This method is not thread-safe. If concurrent access is required, external synchronization
     * should be used. In typical usage, configuration values are set during application startup
     * before concurrent access begins.
     *
     * @param module the configuration module that serves as the key for storing the value
     * @param value the configuration value to store; may be {@code null} to remove the configuration
     * @see #add(ConfigModule, ConfigEntry)
     * @see #get(ConfigModule)
     * @see Config#register(String, String)
     * @since 1.0
     */
    public void set(ConfigModule module, String value) {
        properties.put(module, value);
    }
}
