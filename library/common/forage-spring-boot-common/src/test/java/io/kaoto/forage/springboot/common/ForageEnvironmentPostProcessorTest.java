package io.kaoto.forage.springboot.common;

import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ForageEnvironmentPostProcessorTest {

    @Test
    void classpathPropertiesAreLoadedIntoEnvironment() {
        ConfigurableEnvironment env = new StandardEnvironment();
        ForageEnvironmentPostProcessor processor = new ForageEnvironmentPostProcessor();

        processor.postProcessEnvironment(env, null);

        assertThat(env.getProperty("forage.ds1.jdbc.db.kind")).isEqualTo("postgresql");
        assertThat(env.getProperty("forage.ds1.jdbc.url")).isEqualTo("jdbc:postgresql://localhost:5432/testdb");
        assertThat(env.getProperty("forage.ds2.jdbc.db.kind")).isEqualTo("mysql");
    }

    @Test
    void multipleForagePropertyFilesAreLoaded() {
        ConfigurableEnvironment env = new StandardEnvironment();
        ForageEnvironmentPostProcessor processor = new ForageEnvironmentPostProcessor();

        processor.postProcessEnvironment(env, null);

        // From forage-test-module.properties
        assertThat(env.getProperty("forage.ds1.jdbc.db.kind")).isEqualTo("postgresql");
        // From forage-test-other.properties
        assertThat(env.getProperty("forage.openai.api.key")).isEqualTo("test-key-123");
    }

    @Test
    void loadedPropertiesAreEnumerableForPrefixDiscovery() {
        ConfigurableEnvironment env = new StandardEnvironment();
        ForageEnvironmentPostProcessor processor = new ForageEnvironmentPostProcessor();

        processor.postProcessEnvironment(env, null);

        // Simulate what ForageSpringBootModuleAdapter.discoverPrefixes() does
        Pattern pattern = Pattern.compile("forage\\.(.+)\\.jdbc\\..+");
        Set<String> prefixes = StreamSupport.stream(env.getPropertySources().spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource<?>)
                .flatMap(ps -> {
                    String[] names = ((EnumerablePropertySource<?>) ps).getPropertyNames();
                    return java.util.Arrays.stream(names);
                })
                .map(key -> {
                    Matcher m = pattern.matcher(key);
                    if (m.find()) {
                        return m.group(1);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        assertThat(prefixes).containsExactlyInAnyOrder("ds1", "ds2");
    }

    @Test
    void applicationPropertiesOverrideForageFiles() {
        ConfigurableEnvironment env = new StandardEnvironment();

        // Simulate application.properties having a higher-priority override
        env.getPropertySources()
                .addFirst(new MapPropertySource(
                        "applicationProperties", java.util.Map.of("forage.ds1.jdbc.db.kind", "h2")));

        ForageEnvironmentPostProcessor processor = new ForageEnvironmentPostProcessor();
        processor.postProcessEnvironment(env, null);

        // application.properties value wins over forage-test-module.properties
        assertThat(env.getProperty("forage.ds1.jdbc.db.kind")).isEqualTo("h2");
    }

    @Test
    void noExceptionWhenNoForagePropertiesExist() {
        // This test verifies graceful handling - even though our test classpath
        // does have forage-*.properties, we just verify no exception is thrown
        ConfigurableEnvironment env = new StandardEnvironment();
        ForageEnvironmentPostProcessor processor = new ForageEnvironmentPostProcessor();

        // Should not throw
        processor.postProcessEnvironment(env, null);
    }
}
