package org.apache.camel.forage.core.util.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ConfigEntryTest {

    @Test
    void shouldCreateFromEnvNameWithCorrectTransformation() {
        ConfigEntry entry = ConfigEntry.fromEnv("MY_CONFIG_VALUE");

        assertThat(entry.envName()).isEqualTo("MY_CONFIG_VALUE");
        assertThat(entry.propertyName()).isEqualTo("my.config.value");
    }

    @Test
    void shouldCreateFromEnvNameWithSingleWord() {
        ConfigEntry entry = ConfigEntry.fromEnv("CONFIG");

        assertThat(entry.envName()).isEqualTo("CONFIG");
        assertThat(entry.propertyName()).isEqualTo("config");
    }

    @Test
    void shouldCreateFromEnvNameWithMultipleUnderscores() {
        ConfigEntry entry = ConfigEntry.fromEnv("A_B_C_D");

        assertThat(entry.envName()).isEqualTo("A_B_C_D");
        assertThat(entry.propertyName()).isEqualTo("a.b.c.d");
    }

    @Test
    void shouldCreateFromPropertyNameWithCorrectTransformation() {
        ConfigEntry entry = ConfigEntry.fromProperty("my.config.value");

        assertThat(entry.envName()).isEqualTo("my.config.value");
        assertThat(entry.propertyName()).isEqualTo("MY.CONFIG.VALUE");
    }

    @Test
    void shouldCreateFromPropertyNameWithSingleWord() {
        ConfigEntry entry = ConfigEntry.fromProperty("config");

        assertThat(entry.envName()).isEqualTo("config");
        assertThat(entry.propertyName()).isEqualTo("CONFIG");
    }

    @Test
    void shouldCreateFromPropertyNameWithUnderscores() {
        ConfigEntry entry = ConfigEntry.fromProperty("my_property_name");

        assertThat(entry.envName()).isEqualTo("my_property_name");
        assertThat(entry.propertyName()).isEqualTo("MY.PROPERTY.NAME");
    }

    @Test
    void shouldThrowNullPointerExceptionWhenEnvNameIsNull() {
        assertThatThrownBy(() -> ConfigEntry.fromEnv(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("envName");
    }

    @Test
    void shouldThrowNullPointerExceptionWhenPropertyNameIsNull() {
        assertThatThrownBy(() -> ConfigEntry.fromProperty(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("propertyName");
    }

    @Test
    void shouldNotHandleEmptyPropertyStrings() {
        assertThatThrownBy(() -> ConfigEntry.fromProperty(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Property name cannot be empty");
    }

    @Test
    void shouldNotHandleEmptyEnvStrings() {
        assertThatThrownBy(() -> ConfigEntry.fromEnv(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Environment name cannot be empty");
    }
}
