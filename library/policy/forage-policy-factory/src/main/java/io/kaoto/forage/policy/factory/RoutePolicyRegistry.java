package io.kaoto.forage.policy.factory;

import io.kaoto.forage.core.annotations.ForageBean;
import io.kaoto.forage.core.policy.RoutePolicyProvider;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Registry of RoutePolicyProvider instances discovered via ServiceLoader.
 *
 * <p>This registry lazily loads all available RoutePolicyProvider implementations
 * and caches them by their name (from the @ForageBean annotation). Providers can
 * then be looked up by name when creating route policies.
 *
 * <p><strong>Thread Safety:</strong>
 * This class is thread-safe. The provider map is lazily initialized on first
 * access and uses a ConcurrentHashMap for thread-safe reads.
 *
 * @see RoutePolicyProvider
 * @since 1.0
 */
public class RoutePolicyRegistry {
    private static final Logger LOG = LoggerFactory.getLogger(RoutePolicyRegistry.class);

    private final Map<String, RoutePolicyProvider> providers = new ConcurrentHashMap<>();
    private volatile boolean initialized = false;

    /**
     * Creates a new RoutePolicyRegistry.
     */
    public RoutePolicyRegistry() {
        // Lazy initialization
    }

    /**
     * Returns the provider with the given name.
     *
     * @param name the policy name (e.g., "schedule", "flip")
     * @return an Optional containing the provider, or empty if not found
     */
    public Optional<RoutePolicyProvider> getProvider(String name) {
        if (name == null) {
            return Optional.empty();
        }
        ensureInitialized();
        return Optional.ofNullable(providers.get(name));
    }

    /**
     * Returns all registered providers.
     *
     * @return an unmodifiable collection of all providers
     */
    public Collection<RoutePolicyProvider> getAllProviders() {
        ensureInitialized();
        return Collections.unmodifiableCollection(providers.values());
    }

    /**
     * Checks if a provider with the given name exists.
     *
     * @param name the policy name
     * @return true if a provider with the given name exists
     */
    public boolean hasProvider(String name) {
        if (name == null) {
            return false;
        }
        ensureInitialized();
        return providers.containsKey(name);
    }

    private void ensureInitialized() {
        if (!initialized) {
            synchronized (this) {
                if (!initialized) {
                    loadProviders();
                    initialized = true;
                }
            }
        }
    }

    private void loadProviders() {
        LOG.debug("Loading RoutePolicyProvider implementations via ServiceLoader");

        ServiceLoader<RoutePolicyProvider> loader = ServiceLoader.load(RoutePolicyProvider.class);

        for (RoutePolicyProvider provider : loader) {
            String name = getProviderName(provider);
            if (name != null) {
                if (providers.containsKey(name)) {
                    LOG.warn(
                            "Duplicate RoutePolicyProvider for name '{}': {} will override {}",
                            name,
                            provider.getClass().getName(),
                            providers.get(name).getClass().getName());
                }
                providers.put(name, provider);
                LOG.debug(
                        "Registered RoutePolicyProvider '{}': {}",
                        name,
                        provider.getClass().getName());
            } else {
                LOG.warn(
                        "RoutePolicyProvider {} does not have @ForageBean annotation or name() method, skipping",
                        provider.getClass().getName());
            }
        }

        LOG.info("Loaded {} RoutePolicyProvider implementations", providers.size());
    }

    private String getProviderName(RoutePolicyProvider provider) {
        // First try the name() method
        try {
            String name = provider.name();
            if (name != null && !name.isEmpty()) {
                return name;
            }
        } catch (Exception e) {
            LOG.debug(
                    "Failed to get name from provider {}: {}",
                    provider.getClass().getName(),
                    e.getMessage());
        }

        // Fall back to @ForageBean annotation
        ForageBean annotation = provider.getClass().getAnnotation(ForageBean.class);
        if (annotation != null) {
            return annotation.value();
        }

        return null;
    }
}
