package io.kaoto.forage.core.policy;

import io.kaoto.forage.core.common.BeanProvider;
import org.apache.camel.spi.RoutePolicy;

/**
 * Provider interface for creating RoutePolicy instances.
 *
 * <p>Implementations are discovered via ServiceLoader and registered
 * in RoutePolicyRegistry by their @ForageBean name. Each provider is
 * responsible for creating configured RoutePolicy instances based on
 * the configuration prefix passed to the create method.
 *
 * <p><strong>Configuration:</strong>
 * The configuration prefix follows the pattern:
 * {@code camel.forage.route.policy.<routeId>.<policyName>}
 *
 * <p><strong>Usage:</strong>
 * <pre>{@code
 * @ForageBean(value = "schedule", components = {"camel-core"}, ...)
 * public class ScheduleRoutePolicyProvider implements RoutePolicyProvider {
 *     @Override
 *     public String name() {
 *         return "schedule";
 *     }
 *
 *     @Override
 *     public RoutePolicy create(String configPrefix) {
 *         ScheduleRoutePolicyConfig config = new ScheduleRoutePolicyConfig(configPrefix);
 *         return new ForageScheduleRoutePolicy(config);
 *     }
 * }
 * }</pre>
 *
 * @see BeanProvider
 * @see RoutePolicy
 * @since 1.0
 */
public interface RoutePolicyProvider extends BeanProvider<RoutePolicy> {

    /**
     * Returns the policy name.
     *
     * <p>This name is used to identify the provider in configuration
     * and should match the value specified in the @ForageBean annotation.
     *
     * @return the policy name (e.g., "schedule", "flip")
     */
    String name();

    /**
     * Creates a RoutePolicy with the given configuration prefix.
     *
     * <p>The configuration prefix is used to load policy-specific
     * configuration from the ConfigStore. The prefix follows the pattern:
     * {@code camel.forage.route.policy.<routeId>.<policyName>}
     *
     * @param configPrefix the configuration prefix for this policy instance
     * @return a configured RoutePolicy instance
     */
    @Override
    RoutePolicy create(String configPrefix);
}
