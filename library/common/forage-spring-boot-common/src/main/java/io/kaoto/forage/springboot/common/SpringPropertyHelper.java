package io.kaoto.forage.springboot.common;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;

/**
 * Shared utility for discovering Forage configuration prefixes from the Spring {@link Environment}.
 *
 * @since 1.1
 */
public final class SpringPropertyHelper {

    private SpringPropertyHelper() {}

    /**
     * Extracts distinct prefix groups matching {@code regexp} from all enumerable property sources.
     *
     * @param env    the Spring environment
     * @param regexp a regex with one capture group for the prefix
     * @return the set of matched prefixes, or empty if the environment is not configurable
     */
    public static Set<String> discoverPrefixes(Environment env, String regexp) {
        if (!(env instanceof ConfigurableEnvironment configurableEnv)) {
            return Collections.emptySet();
        }

        Pattern pattern = Pattern.compile(regexp);
        return StreamSupport.stream(configurableEnv.getPropertySources().spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource<?>)
                .flatMap(ps -> java.util.Arrays.stream(((EnumerablePropertySource<?>) ps).getPropertyNames()))
                .map(key -> {
                    Matcher m = pattern.matcher(key);
                    if (m.find()) {
                        return m.group(1);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * Returns {@code true} if any enumerable property source contains a key matching {@code regexp}.
     *
     * @param env    the Spring environment
     * @param regexp a regex to match against property names
     * @return true if at least one property matches
     */
    public static boolean hasProperties(Environment env, String regexp) {
        if (!(env instanceof ConfigurableEnvironment configurableEnv)) {
            return false;
        }
        Pattern pattern = Pattern.compile(regexp);
        return StreamSupport.stream(configurableEnv.getPropertySources().spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource<?>)
                .flatMap(ps -> java.util.Arrays.stream(((EnumerablePropertySource<?>) ps).getPropertyNames()))
                .anyMatch(key -> pattern.matcher(key).find());
    }
}
