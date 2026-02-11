package io.kaoto.forage.policy.schedule;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.EnumSet;
import java.util.Set;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for ForageScheduleRoutePolicy.
 */
@DisplayName("ForageScheduleRoutePolicy Tests")
class ForageScheduleRoutePolicyTest {

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
            TestScheduleRoutePolicyConfig config = new TestScheduleRoutePolicyConfig(
                    LocalTime.of(9, 0),
                    LocalTime.of(17, 0),
                    ZoneId.systemDefault(),
                    EnumSet.allOf(DayOfWeek.class),
                    null);

            ForageScheduleRoutePolicy policy = new ForageScheduleRoutePolicy(config);

            assertThat(policy).isNotNull();
        }

        @Test
        @DisplayName("Should create policy with explicit parameters")
        void shouldCreatePolicyWithExplicitParameters() {
            LocalTime startTime = LocalTime.of(9, 0);
            LocalTime stopTime = LocalTime.of(17, 0);
            ZoneId timezone = ZoneId.of("UTC");
            Set<DayOfWeek> daysOfWeek = EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY);

            ForageScheduleRoutePolicy policy = new ForageScheduleRoutePolicy(startTime, stopTime, timezone, daysOfWeek);

            assertThat(policy).isNotNull();
        }

        @Test
        @DisplayName("Should handle null start/stop times")
        void shouldHandleNullStartStopTimes() {
            ForageScheduleRoutePolicy policy = new ForageScheduleRoutePolicy(null, null, ZoneId.systemDefault(), null);

            assertThat(policy).isNotNull();
        }
    }

    @Nested
    @DisplayName("Schedule Window Tests")
    class ScheduleWindowTests {

        @Test
        @DisplayName("Should create policy for normal time window")
        void shouldCreatePolicyForNormalTimeWindow() {
            // Normal case: 09:00 to 17:00
            ForageScheduleRoutePolicy policy = new ForageScheduleRoutePolicy(
                    LocalTime.of(9, 0), LocalTime.of(17, 0), ZoneId.systemDefault(), EnumSet.allOf(DayOfWeek.class));

            assertThat(policy).isNotNull();
        }

        @Test
        @DisplayName("Should create policy for overnight time window")
        void shouldCreatePolicyForOvernightTimeWindow() {
            // Overnight case: 22:00 to 06:00
            ForageScheduleRoutePolicy policy = new ForageScheduleRoutePolicy(
                    LocalTime.of(22, 0), LocalTime.of(6, 0), ZoneId.systemDefault(), EnumSet.allOf(DayOfWeek.class));

            assertThat(policy).isNotNull();
        }

        @Test
        @DisplayName("Should create policy for weekday only schedule")
        void shouldCreatePolicyForWeekdayOnlySchedule() {
            Set<DayOfWeek> weekdays = EnumSet.of(
                    DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY);

            ForageScheduleRoutePolicy policy = new ForageScheduleRoutePolicy(
                    LocalTime.of(9, 0), LocalTime.of(17, 0), ZoneId.systemDefault(), weekdays);

            assertThat(policy).isNotNull();
        }
    }

    @Nested
    @DisplayName("Timezone Tests")
    class TimezoneTests {

        @Test
        @DisplayName("Should create policy with UTC timezone")
        void shouldCreatePolicyWithUtcTimezone() {
            ForageScheduleRoutePolicy policy = new ForageScheduleRoutePolicy(
                    LocalTime.of(9, 0), LocalTime.of(17, 0), ZoneId.of("UTC"), EnumSet.allOf(DayOfWeek.class));

            assertThat(policy).isNotNull();
        }

        @Test
        @DisplayName("Should create policy with specific timezone")
        void shouldCreatePolicyWithSpecificTimezone() {
            ForageScheduleRoutePolicy policy = new ForageScheduleRoutePolicy(
                    LocalTime.of(9, 0),
                    LocalTime.of(17, 0),
                    ZoneId.of("America/New_York"),
                    EnumSet.allOf(DayOfWeek.class));

            assertThat(policy).isNotNull();
        }
    }

    /**
     * Test implementation of ScheduleRoutePolicyConfig for unit testing.
     */
    static class TestScheduleRoutePolicyConfig extends ScheduleRoutePolicyConfig {
        private final LocalTime startTime;
        private final LocalTime stopTime;
        private final ZoneId timezone;
        private final Set<DayOfWeek> daysOfWeek;
        private final String cronExpression;

        TestScheduleRoutePolicyConfig(
                LocalTime startTime,
                LocalTime stopTime,
                ZoneId timezone,
                Set<DayOfWeek> daysOfWeek,
                String cronExpression) {
            this.startTime = startTime;
            this.stopTime = stopTime;
            this.timezone = timezone;
            this.daysOfWeek = daysOfWeek;
            this.cronExpression = cronExpression;
        }

        @Override
        public LocalTime startTime() {
            return startTime;
        }

        @Override
        public LocalTime stopTime() {
            return stopTime;
        }

        @Override
        public ZoneId timezone() {
            return timezone != null ? timezone : ZoneId.systemDefault();
        }

        @Override
        public Set<DayOfWeek> daysOfWeek() {
            return daysOfWeek != null ? daysOfWeek : EnumSet.allOf(DayOfWeek.class);
        }

        @Override
        public String cronExpression() {
            return cronExpression;
        }
    }
}
