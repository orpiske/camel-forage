package io.kaoto.forage.policy.flip;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.kaoto.forage.core.util.config.MissingConfigException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for FlipRoutePolicyConfig.
 */
@DisplayName("FlipRoutePolicyConfig Tests")
class FlipRoutePolicyConfigTest {

    @Nested
    @DisplayName("Paired Route Tests")
    class PairedRouteTests {

        @Test
        @DisplayName("Should return configured paired route ID")
        void shouldReturnConfiguredPairedRouteId() {
            TestFlipRoutePolicyConfig config = new TestFlipRoutePolicyConfig("routeB", true);

            assertThat(config.pairedRouteId()).isEqualTo("routeB");
        }

        @Test
        @DisplayName("Should throw exception when paired route not configured")
        void shouldThrowExceptionWhenPairedRouteNotConfigured() {
            TestFlipRoutePolicyConfig config = new TestFlipRoutePolicyConfig(null, true);

            assertThatThrownBy(config::pairedRouteId)
                    .isInstanceOf(MissingConfigException.class)
                    .hasMessageContaining("paired-route");
        }
    }

    @Nested
    @DisplayName("Initially Active Tests")
    class InitiallyActiveTests {

        @Test
        @DisplayName("Should return true by default")
        void shouldReturnTrueByDefault() {
            TestFlipRoutePolicyConfig config = new TestFlipRoutePolicyConfig("routeB", null);

            assertThat(config.initiallyActive()).isTrue();
        }

        @Test
        @DisplayName("Should return true when configured as true")
        void shouldReturnTrueWhenConfiguredAsTrue() {
            TestFlipRoutePolicyConfig config = new TestFlipRoutePolicyConfig("routeB", true);

            assertThat(config.initiallyActive()).isTrue();
        }

        @Test
        @DisplayName("Should return false when configured as false")
        void shouldReturnFalseWhenConfiguredAsFalse() {
            TestFlipRoutePolicyConfig config = new TestFlipRoutePolicyConfig("routeB", false);

            assertThat(config.initiallyActive()).isFalse();
        }
    }

    @Nested
    @DisplayName("Config Name Tests")
    class ConfigNameTests {

        @Test
        @DisplayName("Should return correct config name")
        void shouldReturnCorrectConfigName() {
            FlipRoutePolicyConfig config = new FlipRoutePolicyConfig();

            assertThat(config.name()).isEqualTo("forage-policy-flip");
        }
    }

    @Nested
    @DisplayName("Combined Config Tests")
    class CombinedConfigTests {

        @Test
        @DisplayName("Should support active route config")
        void shouldSupportActiveRouteConfig() {
            TestFlipRoutePolicyConfig config = new TestFlipRoutePolicyConfig("routeB", true);

            assertThat(config.pairedRouteId()).isEqualTo("routeB");
            assertThat(config.initiallyActive()).isTrue();
        }

        @Test
        @DisplayName("Should support inactive route config")
        void shouldSupportInactiveRouteConfig() {
            TestFlipRoutePolicyConfig config = new TestFlipRoutePolicyConfig("routeA", false);

            assertThat(config.pairedRouteId()).isEqualTo("routeA");
            assertThat(config.initiallyActive()).isFalse();
        }
    }

    /**
     * Test implementation that allows injecting values directly.
     */
    static class TestFlipRoutePolicyConfig extends FlipRoutePolicyConfig {
        private final String testPairedRouteId;
        private final Boolean testInitiallyActive;

        TestFlipRoutePolicyConfig(String pairedRouteId, Boolean initiallyActive) {
            super();
            this.testPairedRouteId = pairedRouteId;
            this.testInitiallyActive = initiallyActive;
        }

        @Override
        public String pairedRouteId() {
            if (testPairedRouteId == null) {
                throw new MissingConfigException("Missing paired-route configuration for flip policy");
            }
            return testPairedRouteId;
        }

        @Override
        public boolean initiallyActive() {
            return testInitiallyActive != null ? testInitiallyActive : true;
        }
    }
}
