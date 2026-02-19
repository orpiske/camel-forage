package io.kaoto.forage.policy.flip;

import org.apache.camel.spi.RoutePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.kaoto.forage.core.annotations.ForageBean;
import io.kaoto.forage.core.policy.RoutePolicyProvider;

/**
 * Provider for creating flip route policies.
 *
 * <p>This provider creates instances of {@link ForageFlipRoutePolicy} using
 * configuration values managed by {@link FlipRoutePolicyConfig}.
 *
 * <p><strong>Configuration:</strong>
 * <ul>
 *   <li>paired-route: ID of the paired route (required)</li>
 *   <li>initially-active: Whether this route starts active (default: true)</li>
 * </ul>
 *
 * @see FlipRoutePolicyConfig
 * @see ForageFlipRoutePolicy
 * @since 1.0
 */
@ForageBean(
        value = "flip",
        components = {"camel-core"},
        feature = "Route Policy",
        description = "Mutually exclusive route flipping policy")
public class FlipRoutePolicyProvider implements RoutePolicyProvider {
    private static final Logger LOG = LoggerFactory.getLogger(FlipRoutePolicyProvider.class);

    @Override
    public String name() {
        return "flip";
    }

    @Override
    public RoutePolicy create(String configPrefix) {
        LOG.debug("Creating flip route policy with config prefix: {}", configPrefix);

        FlipRoutePolicyConfig config = new FlipRoutePolicyConfig(configPrefix);
        return new ForageFlipRoutePolicy(config);
    }
}
