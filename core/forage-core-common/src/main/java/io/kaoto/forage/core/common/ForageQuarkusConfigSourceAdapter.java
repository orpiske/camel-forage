package io.kaoto.forage.core.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.kaoto.forage.core.util.config.Config;
import io.kaoto.forage.core.util.config.ConfigHelper;
import io.kaoto.forage.core.util.config.ConfigStore;
import io.smallrye.config.ConfigSourceContext;
import io.smallrye.config.ConfigSourceFactory;

/**
 * Generic SmallRye {@link ConfigSourceFactory} adapter that translates Forage properties
 * into Quarkus-native properties using a {@link ForageModuleDescriptor}.
 *
 * <p>Subclasses need only provide the descriptor — all prefix discovery, config creation,
 * and property translation logic is handled by this base class.
 *
 * <p>Uses ordinal 275 (below system properties at 400), so {@code -D} overrides work.
 *
 * @param <C> the module's configuration type
 * @since 1.1
 */
public abstract class ForageQuarkusConfigSourceAdapter<C extends Config> implements ConfigSourceFactory {

    private static final Logger LOG = LoggerFactory.getLogger(ForageQuarkusConfigSourceAdapter.class);

    /**
     * Returns the module descriptor that provides module-specific knowledge.
     */
    protected abstract ForageModuleDescriptor<C, ?> descriptor();

    @Override
    public Iterable<ConfigSource> getConfigSources(ConfigSourceContext context) {
        ForageModuleDescriptor<C, ?> desc = descriptor();
        ConfigStore.getInstance().setClassLoader(Thread.currentThread().getContextClassLoader());

        C defaultConfig = desc.createConfig(null);
        Set<String> prefixes = ConfigStore.getInstance()
                .readPrefixes(defaultConfig, ConfigHelper.getNamedPropertyRegexp(desc.modulePrefix()));

        Map<String, String> configuration = new HashMap<>();

        if (!prefixes.isEmpty()) {
            for (String name : prefixes) {
                C config = desc.createConfig(name);
                configuration.putAll(desc.translateProperties(name, config));
            }
        } else if (!ConfigStore.getInstance()
                .readPrefixes(defaultConfig, ConfigHelper.getDefaultPropertyRegexp(desc.modulePrefix()))
                .isEmpty()) {
            configuration.putAll(desc.translateProperties(null, defaultConfig));
        } else {
            LOG.trace("No {} config found.", desc.modulePrefix());
        }

        if (configuration.isEmpty()) {
            return Collections.emptyList();
        }

        String sourceName = "Forage" + capitalize(desc.modulePrefix()) + "TranslatedConfigSource";
        return Collections.singletonList(new ForageTranslatedConfigSource(sourceName, configuration));
    }

    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * Internal ConfigSource with ordinal 275 (below system properties at 400,
     * at the same level as InMemoryConfigSource).
     */
    private static class ForageTranslatedConfigSource implements ConfigSource {

        private final String name;
        private final Map<String, String> configuration;

        ForageTranslatedConfigSource(String name, Map<String, String> configuration) {
            this.name = name;
            this.configuration = Collections.unmodifiableMap(configuration);
        }

        @Override
        public int getOrdinal() {
            return 275;
        }

        @Override
        public Set<String> getPropertyNames() {
            return configuration.keySet();
        }

        @Override
        public String getValue(String propertyName) {
            return configuration.get(propertyName);
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
