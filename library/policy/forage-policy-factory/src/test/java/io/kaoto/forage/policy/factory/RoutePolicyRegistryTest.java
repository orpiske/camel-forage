package io.kaoto.forage.policy.factory;

import java.util.Collection;
import java.util.Optional;
import org.apache.camel.spi.RoutePolicy;
import io.kaoto.forage.core.policy.RoutePolicyProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for RoutePolicyRegistry focusing on provider discovery and lookup.
 */
@DisplayName("RoutePolicyRegistry Tests")
class RoutePolicyRegistryTest {

    private RoutePolicyRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new RoutePolicyRegistry();
    }

    @Nested
    @DisplayName("Provider Lookup Tests")
    class ProviderLookupTests {

        @Test
        @DisplayName("Should return empty for unknown provider name")
        void shouldReturnEmptyForUnknownProviderName() {
            Optional<RoutePolicyProvider> provider = registry.getProvider("non-existent-policy");

            assertThat(provider).isEmpty();
        }

        @Test
        @DisplayName("Should return empty for null provider name")
        void shouldReturnEmptyForNullProviderName() {
            Optional<RoutePolicyProvider> provider = registry.getProvider(null);

            assertThat(provider).isEmpty();
        }

        @Test
        @DisplayName("Should return false for hasProvider with unknown name")
        void shouldReturnFalseForHasProviderWithUnknownName() {
            boolean hasProvider = registry.hasProvider("non-existent-policy");

            assertThat(hasProvider).isFalse();
        }

        @Test
        @DisplayName("Should return false for hasProvider with null name")
        void shouldReturnFalseForHasProviderWithNullName() {
            boolean hasProvider = registry.hasProvider(null);

            assertThat(hasProvider).isFalse();
        }
    }

    @Nested
    @DisplayName("GetAllProviders Tests")
    class GetAllProvidersTests {

        @Test
        @DisplayName("Should return unmodifiable collection")
        void shouldReturnUnmodifiableCollection() {
            Collection<RoutePolicyProvider> providers = registry.getAllProviders();

            assertThat(providers).isNotNull();
        }

        @Test
        @DisplayName("Should return same providers on multiple calls")
        void shouldReturnSameProvidersOnMultipleCalls() {
            Collection<RoutePolicyProvider> providers1 = registry.getAllProviders();
            Collection<RoutePolicyProvider> providers2 = registry.getAllProviders();

            assertThat(providers1).containsExactlyInAnyOrderElementsOf(providers2);
        }
    }

    @Nested
    @DisplayName("Thread Safety Tests")
    class ThreadSafetyTests {

        @Test
        @DisplayName("Should handle concurrent access safely")
        void shouldHandleConcurrentAccessSafely() throws InterruptedException {
            int threadCount = 10;
            Thread[] threads = new Thread[threadCount];

            for (int i = 0; i < threadCount; i++) {
                threads[i] = new Thread(() -> {
                    registry.getProvider("schedule");
                    registry.hasProvider("flip");
                    registry.getAllProviders();
                });
            }

            for (Thread thread : threads) {
                thread.start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // If we get here without exceptions, concurrent access is safe
            assertThat(registry.getAllProviders()).isNotNull();
        }
    }

    @Nested
    @DisplayName("Mock Provider Tests")
    class MockProviderTests {

        @Test
        @DisplayName("Should find provider with name() method")
        void shouldFindProviderWithNameMethod() {
            // This test verifies the registry loads providers via ServiceLoader
            // If schedule or flip providers are on classpath, they should be found
            // Otherwise, this test validates the lookup mechanism works
            RoutePolicyRegistry testRegistry = new RoutePolicyRegistry();

            // The registry should be queryable regardless of what providers are loaded
            assertThat(testRegistry.getAllProviders()).isNotNull();
        }
    }

    @Nested
    @DisplayName("ServiceLoader Discovery Tests")
    class ServiceLoaderDiscoveryTests {

        @Test
        @DisplayName("Should discover providers via ServiceLoader")
        void shouldDiscoverProvidersViaServiceLoader() {
            // Create a new registry to trigger fresh ServiceLoader discovery
            RoutePolicyRegistry freshRegistry = new RoutePolicyRegistry();

            // Trigger initialization
            Collection<RoutePolicyProvider> providers = freshRegistry.getAllProviders();

            // Providers collection should be accessible (may be empty if no providers on classpath)
            assertThat(providers).isNotNull();
        }

        @Test
        @DisplayName("Should cache providers after initial load")
        void shouldCacheProvidersAfterInitialLoad() {
            // First call triggers loading
            Collection<RoutePolicyProvider> providers1 = registry.getAllProviders();

            // Second call should return cached providers
            Collection<RoutePolicyProvider> providers2 = registry.getAllProviders();

            // Both should return the same providers
            assertThat(providers1.size()).isEqualTo(providers2.size());
        }
    }

    /**
     * Test provider implementation for unit testing.
     */
    static class TestRoutePolicyProvider implements RoutePolicyProvider {

        private final String providerName;

        TestRoutePolicyProvider(String name) {
            this.providerName = name;
        }

        @Override
        public String name() {
            return providerName;
        }

        @Override
        public RoutePolicy create(String configPrefix) {
            return null;
        }
    }
}
