package io.kaoto.forage.policy.factory;

import static org.assertj.core.api.Assertions.assertThat;

import io.kaoto.forage.core.policy.RoutePolicyProvider;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spi.RoutePolicy;
import org.apache.camel.support.RoutePolicySupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for DefaultCamelForageRoutePolicyFactory.
 */
@DisplayName("DefaultCamelForageRoutePolicyFactory Tests")
class DefaultCamelForageRoutePolicyFactoryTest {

    private CamelContext camelContext;
    private TestRoutePolicyRegistry testRegistry;
    private TestRoutePolicyFactoryConfig testConfig;
    private DefaultCamelForageRoutePolicyFactory factory;

    @BeforeEach
    void setUp() {
        camelContext = new DefaultCamelContext();
        testRegistry = new TestRoutePolicyRegistry();
        testConfig = new TestRoutePolicyFactoryConfig();
        factory = new DefaultCamelForageRoutePolicyFactory(testRegistry, testConfig);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (camelContext != null) {
            camelContext.close();
        }
    }

    @Nested
    @DisplayName("No Policy Configured Tests")
    class NoPolicyConfiguredTests {

        @Test
        @DisplayName("Should return null when no policy configured for route")
        void shouldReturnNullWhenNoPolicyConfigured() {
            RoutePolicy result = factory.createRoutePolicy(camelContext, "myRoute", null);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should return null when policy names is empty string")
        void shouldReturnNullWhenPolicyNamesEmpty() {
            testConfig.setPolicyNames("myRoute", "");

            RoutePolicy result = factory.createRoutePolicy(camelContext, "myRoute", null);

            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("Single Policy Tests")
    class SinglePolicyTests {

        @Test
        @DisplayName("Should create and return single policy")
        void shouldCreateAndReturnSinglePolicy() {
            TestRoutePolicy testPolicy = new TestRoutePolicy("test-policy");
            testRegistry.registerProvider("schedule", new TestRoutePolicyProvider("schedule", testPolicy));
            testConfig.setPolicyNames("myRoute", "schedule");

            RoutePolicy result = factory.createRoutePolicy(camelContext, "myRoute", null);

            assertThat(result).isSameAs(testPolicy);
        }

        @Test
        @DisplayName("Should handle policy with whitespace in name")
        void shouldHandlePolicyWithWhitespace() {
            TestRoutePolicy testPolicy = new TestRoutePolicy("test-policy");
            testRegistry.registerProvider("schedule", new TestRoutePolicyProvider("schedule", testPolicy));
            testConfig.setPolicyNames("myRoute", "  schedule  ");

            RoutePolicy result = factory.createRoutePolicy(camelContext, "myRoute", null);

            assertThat(result).isSameAs(testPolicy);
        }
    }

    @Nested
    @DisplayName("Unknown Policy Tests")
    class UnknownPolicyTests {

        @Test
        @DisplayName("Should return null for unknown policy")
        void shouldReturnNullForUnknownPolicy() {
            testConfig.setPolicyNames("myRoute", "unknown-policy");

            RoutePolicy result = factory.createRoutePolicy(camelContext, "myRoute", null);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should skip unknown policy and continue with known policies")
        void shouldSkipUnknownAndContinueWithKnown() {
            TestRoutePolicy testPolicy = new TestRoutePolicy("test-policy");
            testRegistry.registerProvider("schedule", new TestRoutePolicyProvider("schedule", testPolicy));
            testConfig.setPolicyNames("myRoute", "unknown,schedule");

            RoutePolicy result = factory.createRoutePolicy(camelContext, "myRoute", null);

            assertThat(result).isSameAs(testPolicy);
        }
    }

    @Nested
    @DisplayName("Multiple Policies Tests")
    class MultiplePoliciesTests {

        @Test
        @DisplayName("Should apply last-wins semantics for multiple policies")
        void shouldApplyLastWinsSemanticsForMultiplePolicies() {
            TestRoutePolicy firstPolicy = new TestRoutePolicy("first");
            TestRoutePolicy lastPolicy = new TestRoutePolicy("last");
            testRegistry.registerProvider("schedule", new TestRoutePolicyProvider("schedule", firstPolicy));
            testRegistry.registerProvider("flip", new TestRoutePolicyProvider("flip", lastPolicy));
            testConfig.setPolicyNames("myRoute", "schedule,flip");

            RoutePolicy result = factory.createRoutePolicy(camelContext, "myRoute", null);

            assertThat(result).isSameAs(lastPolicy);
        }

        @Test
        @DisplayName("Should handle comma-separated list with spaces")
        void shouldHandleCommaSeparatedListWithSpaces() {
            TestRoutePolicy firstPolicy = new TestRoutePolicy("first");
            TestRoutePolicy lastPolicy = new TestRoutePolicy("last");
            testRegistry.registerProvider("schedule", new TestRoutePolicyProvider("schedule", firstPolicy));
            testRegistry.registerProvider("flip", new TestRoutePolicyProvider("flip", lastPolicy));
            testConfig.setPolicyNames("myRoute", "schedule , flip");

            RoutePolicy result = factory.createRoutePolicy(camelContext, "myRoute", null);

            assertThat(result).isSameAs(lastPolicy);
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle provider that returns null policy")
        void shouldHandleProviderReturningNullPolicy() {
            testRegistry.registerProvider("schedule", new TestRoutePolicyProvider("schedule", null));
            testConfig.setPolicyNames("myRoute", "schedule");

            RoutePolicy result = factory.createRoutePolicy(camelContext, "myRoute", null);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle provider that throws exception")
        void shouldHandleProviderThrowingException() {
            testRegistry.registerProvider("schedule", new FailingRoutePolicyProvider("schedule"));
            testConfig.setPolicyNames("myRoute", "schedule");

            RoutePolicy result = factory.createRoutePolicy(camelContext, "myRoute", null);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should continue with other policies when one fails")
        void shouldContinueWhenOnePolicyFails() {
            TestRoutePolicy goodPolicy = new TestRoutePolicy("good");
            testRegistry.registerProvider("bad", new FailingRoutePolicyProvider("bad"));
            testRegistry.registerProvider("good", new TestRoutePolicyProvider("good", goodPolicy));
            testConfig.setPolicyNames("myRoute", "bad,good");

            RoutePolicy result = factory.createRoutePolicy(camelContext, "myRoute", null);

            assertThat(result).isSameAs(goodPolicy);
        }
    }

    @Nested
    @DisplayName("Default Constructor Tests")
    class DefaultConstructorTests {

        @Test
        @DisplayName("Should create factory with default registry and config")
        void shouldCreateFactoryWithDefaults() {
            DefaultCamelForageRoutePolicyFactory defaultFactory = new DefaultCamelForageRoutePolicyFactory();

            // Should not throw - factory is properly initialized
            RoutePolicy result = defaultFactory.createRoutePolicy(camelContext, "unknownRoute", null);

            // No policy configured, should return null
            assertThat(result).isNull();
        }
    }

    /**
     * Test implementation of RoutePolicyRegistry for unit testing.
     */
    static class TestRoutePolicyRegistry extends RoutePolicyRegistry {
        private final Map<String, RoutePolicyProvider> testProviders = new HashMap<>();

        void registerProvider(String name, RoutePolicyProvider provider) {
            testProviders.put(name, provider);
        }

        @Override
        public Optional<RoutePolicyProvider> getProvider(String name) {
            return Optional.ofNullable(testProviders.get(name));
        }

        @Override
        public Collection<RoutePolicyProvider> getAllProviders() {
            return Collections.unmodifiableCollection(testProviders.values());
        }

        @Override
        public boolean hasProvider(String name) {
            return testProviders.containsKey(name);
        }
    }

    /**
     * Test implementation of RoutePolicyFactoryConfig for unit testing.
     */
    static class TestRoutePolicyFactoryConfig extends RoutePolicyFactoryConfig {
        private final Map<String, String> policyNames = new HashMap<>();
        private Boolean enabled = null;

        void setPolicyNames(String routeId, String names) {
            policyNames.put(routeId, names);
        }

        void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        public boolean isEnabled() {
            return enabled != null ? enabled : true;
        }

        @Override
        public Optional<String> getPolicyNames(String routeId) {
            return Optional.ofNullable(policyNames.get(routeId));
        }
    }

    /**
     * Test implementation of RoutePolicyProvider for unit testing.
     */
    static class TestRoutePolicyProvider implements RoutePolicyProvider {
        private final String providerName;
        private final RoutePolicy policy;

        TestRoutePolicyProvider(String name, RoutePolicy policy) {
            this.providerName = name;
            this.policy = policy;
        }

        @Override
        public String name() {
            return providerName;
        }

        @Override
        public RoutePolicy create(String configPrefix) {
            return policy;
        }
    }

    /**
     * Test provider that always throws an exception.
     */
    static class FailingRoutePolicyProvider implements RoutePolicyProvider {
        private final String providerName;

        FailingRoutePolicyProvider(String name) {
            this.providerName = name;
        }

        @Override
        public String name() {
            return providerName;
        }

        @Override
        public RoutePolicy create(String configPrefix) {
            throw new RuntimeException("Simulated failure");
        }
    }

    /**
     * Simple RoutePolicy implementation for testing.
     */
    static class TestRoutePolicy extends RoutePolicySupport {
        private final String name;

        TestRoutePolicy(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }
    }
}
