package io.kaoto.forage.core.util.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ConfigEntries {

    private static final Map<Class<? extends ConfigEntries>, Map<ConfigModule, ConfigEntry>> REGISTRY =
            new ConcurrentHashMap<>();

    private static final Map<Class<? extends ConfigEntries>, List<ConfigModule>> BASE_MODULES =
            new ConcurrentHashMap<>();

    protected static void initModules(Class<? extends ConfigEntries> clazz, ConfigModule... modules) {
        Map<ConfigModule, ConfigEntry> map = REGISTRY.computeIfAbsent(clazz, k -> new ConcurrentHashMap<>());
        for (ConfigModule module : modules) {
            map.put(module, ConfigEntry.fromModule());
        }
        BASE_MODULES.put(clazz, Arrays.asList(modules));
    }

    public static Map<ConfigModule, ConfigEntry> getModules(Class<? extends ConfigEntries> clazz) {
        return REGISTRY.computeIfAbsent(clazz, k -> new ConcurrentHashMap<>());
    }

    public static Map<ConfigModule, ConfigEntry> entriesOf(Class<? extends ConfigEntries> clazz) {
        return Collections.unmodifiableMap(getModules(clazz));
    }

    public static void registerPrefix(Class<? extends ConfigEntries> clazz, String prefix) {
        if (prefix != null) {
            Map<ConfigModule, ConfigEntry> modules = getModules(clazz);
            List<ConfigModule> base = BASE_MODULES.get(clazz);
            if (base != null) {
                for (ConfigModule module : base) {
                    modules.put(module.asNamed(prefix), ConfigEntry.fromModule());
                }
            }
        }
    }

    public static void loadOverridesFor(Class<? extends ConfigEntries> clazz, String prefix) {
        List<ConfigModule> base = BASE_MODULES.get(clazz);
        if (base != null) {
            for (ConfigModule module : base) {
                ConfigStore.getInstance().load(module.asNamed(prefix));
            }
        }
    }

    public static Optional<ConfigModule> find(
            Map<ConfigModule, ConfigEntry> configModules, String prefix, String name) {
        return configModules.entrySet().stream()
                .filter(e -> e.getKey().match(name))
                .findFirst()
                .map(Map.Entry::getKey);
    }

    /**
     * Tries loading the configuration represented by the set of configuration modules into the store
     * @param configModules the set of configuration modules to try loading into the configuration store
     * @param prefix an optional prefix for the configuration
     */
    public static void load(Map<ConfigModule, ConfigEntry> configModules, String prefix) {
        configModules.forEach((k, v) -> ConfigStore.getInstance().load(k.asNamed(prefix)));
    }
}
