package io.kaoto.forage.policy.flip;

import java.util.Optional;
import io.kaoto.forage.core.util.config.Config;
import io.kaoto.forage.core.util.config.ConfigModule;
import io.kaoto.forage.core.util.config.ConfigStore;
import io.kaoto.forage.core.util.config.MissingConfigException;

/**
 * Configuration class for the flip route policy.
 *
 * <p>Supports configuration of:
 * <ul>
 *   <li>paired-route: ID of the paired route to flip with (required)</li>
 *   <li>initially-active: Whether this route should be active initially (default: true)</li>
 * </ul>
 *
 * @since 1.0
 */
public class FlipRoutePolicyConfig implements Config {

    private final String prefix;

    public FlipRoutePolicyConfig() {
        this(null);
    }

    public FlipRoutePolicyConfig(String prefix) {
        this.prefix = prefix;

        FlipRoutePolicyConfigEntries.register(prefix);
        ConfigStore.getInstance().load(FlipRoutePolicyConfig.class, this, this::register);
        FlipRoutePolicyConfigEntries.loadOverrides(prefix);
    }

    @Override
    public String name() {
        return "forage-policy-flip";
    }

    @Override
    public void register(String name, String value) {
        Optional<ConfigModule> config = FlipRoutePolicyConfigEntries.find(prefix, name);
        config.ifPresent(module -> ConfigStore.getInstance().set(module, value));
    }

    /**
     * Returns the ID of the paired route.
     *
     * @return the paired route ID
     * @throws MissingConfigException if not configured
     */
    public String pairedRouteId() {
        ConfigModule module = FlipRoutePolicyConfigEntries.pairedRoute(prefix);
        ConfigStore.getInstance().load(module);
        return ConfigStore.getInstance()
                .get(module)
                .orElseThrow(() -> new MissingConfigException("Missing paired-route configuration for flip policy"));
    }

    /**
     * Returns whether this route should be active initially.
     *
     * @return true if this route should start active, false otherwise
     */
    public boolean initiallyActive() {
        ConfigModule module = FlipRoutePolicyConfigEntries.initiallyActive(prefix);
        ConfigStore.getInstance().load(module);
        return ConfigStore.getInstance().get(module).map(Boolean::parseBoolean).orElse(true);
    }
}
