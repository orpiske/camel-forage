package io.kaoto.forage.policy.schedule;

import org.apache.camel.spi.RoutePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.kaoto.forage.core.annotations.ForageBean;
import io.kaoto.forage.core.policy.RoutePolicyProvider;

/**
 * Provider for creating schedule route policies.
 *
 * <p>This provider creates instances of {@link ForageScheduleRoutePolicy} using
 * configuration values managed by {@link ScheduleRoutePolicyConfig}.
 *
 * <p><strong>Configuration:</strong>
 * <ul>
 *   <li>start-time: Daily start time (HH:mm format)</li>
 *   <li>stop-time: Daily stop time (HH:mm format)</li>
 *   <li>timezone: Timezone for scheduling</li>
 *   <li>cron: Cron expression (alternative)</li>
 *   <li>days-of-week: Active days</li>
 * </ul>
 *
 * @see ScheduleRoutePolicyConfig
 * @see ForageScheduleRoutePolicy
 * @since 1.0
 */
@ForageBean(
        value = "schedule",
        components = {"camel-core"},
        feature = "Route Policy",
        description = "Time-based route scheduling policy")
public class ScheduleRoutePolicyProvider implements RoutePolicyProvider {
    private static final Logger LOG = LoggerFactory.getLogger(ScheduleRoutePolicyProvider.class);

    @Override
    public String name() {
        return "schedule";
    }

    @Override
    public RoutePolicy create(String configPrefix) {
        LOG.debug("Creating schedule route policy with config prefix: {}", configPrefix);

        ScheduleRoutePolicyConfig config = new ScheduleRoutePolicyConfig(configPrefix);
        return new ForageScheduleRoutePolicy(config);
    }
}
