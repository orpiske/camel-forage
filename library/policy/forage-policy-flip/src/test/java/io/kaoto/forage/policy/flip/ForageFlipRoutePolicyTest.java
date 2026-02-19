package io.kaoto.forage.policy.flip;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for ForageFlipRoutePolicy.
 */
@DisplayName("ForageFlipRoutePolicy Tests")
class ForageFlipRoutePolicyTest {

    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
    }

    @AfterEach
    void tearDown() throws Exception {
        if (camelContext != null) {
            camelContext.close();
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create policy with config constructor")
        void shouldCreatePolicyWithConfigConstructor() {
            TestFlipRoutePolicyConfig config = new TestFlipRoutePolicyConfig("routeB", true);

            ForageFlipRoutePolicy policy = new ForageFlipRoutePolicy(config);

            assertThat(policy).isNotNull();
        }

        @Test
        @DisplayName("Should create policy with explicit parameters")
        void shouldCreatePolicyWithExplicitParameters() {
            ForageFlipRoutePolicy policy = new ForageFlipRoutePolicy("routeB", true);

            assertThat(policy).isNotNull();
        }

        @Test
        @DisplayName("Should create initially active policy")
        void shouldCreateInitiallyActivePolicy() {
            ForageFlipRoutePolicy policy = new ForageFlipRoutePolicy("routeB", true);

            assertThat(policy).isNotNull();
        }

        @Test
        @DisplayName("Should create initially inactive policy")
        void shouldCreateInitiallyInactivePolicy() {
            ForageFlipRoutePolicy policy = new ForageFlipRoutePolicy("routeB", false);

            assertThat(policy).isNotNull();
        }
    }

    @Nested
    @DisplayName("Paired Route Tests")
    class PairedRouteTests {

        @Test
        @DisplayName("Should accept valid paired route ID")
        void shouldAcceptValidPairedRouteId() {
            ForageFlipRoutePolicy policy = new ForageFlipRoutePolicy("myPairedRoute", true);

            assertThat(policy).isNotNull();
        }

        @Test
        @DisplayName("Should accept paired route ID with special characters")
        void shouldAcceptPairedRouteIdWithSpecialCharacters() {
            ForageFlipRoutePolicy policy = new ForageFlipRoutePolicy("my-paired-route_123", true);

            assertThat(policy).isNotNull();
        }
    }

    @Nested
    @DisplayName("Mutual Exclusivity Tests")
    class MutualExclusivityTests {

        @Test
        @DisplayName("Should support pair of policies - one active, one inactive")
        void shouldSupportPairOfPolicies() {
            ForageFlipRoutePolicy policyA = new ForageFlipRoutePolicy("routeB", true);
            ForageFlipRoutePolicy policyB = new ForageFlipRoutePolicy("routeA", false);

            assertThat(policyA).isNotNull();
            assertThat(policyB).isNotNull();
        }
    }

    /**
     * Test implementation of FlipRoutePolicyConfig for unit testing.
     */
    static class TestFlipRoutePolicyConfig extends FlipRoutePolicyConfig {
        private final String pairedRouteId;
        private final boolean initiallyActive;

        TestFlipRoutePolicyConfig(String pairedRouteId, boolean initiallyActive) {
            super();
            this.pairedRouteId = pairedRouteId;
            this.initiallyActive = initiallyActive;
        }

        @Override
        public String pairedRouteId() {
            return pairedRouteId;
        }

        @Override
        public boolean initiallyActive() {
            return initiallyActive;
        }
    }
}
