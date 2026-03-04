package io.kaoto.forage.core.util.config;

import java.util.Optional;
import java.util.Set;

/**
 * Service Provider Interface for resolving configuration values from different sources.
 *
 * <p>This interface abstracts the mechanism by which configuration values are looked up,
 * allowing different runtimes (plain Camel, Spring Boot, Quarkus) to plug in their own
 * configuration resolution strategy without modifying the core {@link ConfigStore}.
 *
 * <p>Implementations are registered with {@link ConfigStore#registerResolver(ConfigResolver)}
 * and are consulted in priority order (highest priority first) when resolving configuration values.
 *
 * <p><strong>Built-in implementations:</strong>
 * <ul>
 *   <li>{@code DefaultConfigResolver} (priority 0) — resolves from environment variables,
 *       system properties, and properties files (plain Camel behavior)</li>
 *   <li>{@code SpringConfigResolver} (priority 10) — wraps Spring {@code Environment.getProperty()},
 *       supporting profiles, YAML, placeholders, and Cloud Config</li>
 * </ul>
 *
 * @see ConfigStore
 * @since 1.1
 */
public interface ConfigResolver {

    /**
     * Attempts to resolve a configuration value for the given property name.
     *
     * @param propertyName the property name in dot notation (e.g., "forage.ds1.jdbc.url")
     * @return an Optional containing the resolved value, or empty if this resolver cannot provide it
     */
    Optional<String> resolve(String propertyName);

    /**
     * Discovers configuration prefixes matching the given regular expression pattern.
     *
     * <p>The regexp must contain exactly one capture group that extracts the prefix.
     * For example, with the pattern {@code "forage.(.+).jdbc..+"} and properties
     * {@code forage.ds1.jdbc.url} and {@code forage.ds2.jdbc.url}, this method
     * should return the set {@code {"ds1", "ds2"}}.
     *
     * @param regexp the regular expression pattern with one capture group for the prefix
     * @return a set of discovered prefixes, or an empty set if none found
     */
    Set<String> discoverPrefixes(String regexp);

    /**
     * Returns the priority of this resolver. Higher values indicate higher priority.
     * When multiple resolvers can provide a value, the one with the highest priority wins.
     *
     * <p>Standard priorities:
     * <ul>
     *   <li>0 — {@code DefaultConfigResolver} (env vars, system props, properties files)</li>
     *   <li>10 — {@code SpringConfigResolver} (Spring Environment)</li>
     * </ul>
     *
     * @return the priority value
     */
    int priority();
}
