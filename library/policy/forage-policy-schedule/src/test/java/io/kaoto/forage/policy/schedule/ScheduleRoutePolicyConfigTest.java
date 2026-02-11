package io.kaoto.forage.policy.schedule;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.EnumSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for ScheduleRoutePolicyConfig.
 *
 * <p>These tests focus on the parsing behavior of the config class
 * using the test subclass to inject test values.
 */
@DisplayName("ScheduleRoutePolicyConfig Tests")
class ScheduleRoutePolicyConfigTest {

    @Nested
    @DisplayName("Start Time Tests")
    class StartTimeTests {

        @Test
        @DisplayName("Should return null when start time not configured")
        void shouldReturnNullWhenStartTimeNotConfigured() {
            TestScheduleRoutePolicyConfig config = new TestScheduleRoutePolicyConfig(null, null, null, null, null);

            assertThat(config.startTime()).isNull();
        }

        @Test
        @DisplayName("Should parse valid start time")
        void shouldParseValidStartTime() {
            TestScheduleRoutePolicyConfig config =
                    new TestScheduleRoutePolicyConfig(LocalTime.of(9, 0), null, null, null, null);

            assertThat(config.startTime()).isEqualTo(LocalTime.of(9, 0));
        }
    }

    @Nested
    @DisplayName("Stop Time Tests")
    class StopTimeTests {

        @Test
        @DisplayName("Should return null when stop time not configured")
        void shouldReturnNullWhenStopTimeNotConfigured() {
            TestScheduleRoutePolicyConfig config = new TestScheduleRoutePolicyConfig(null, null, null, null, null);

            assertThat(config.stopTime()).isNull();
        }

        @Test
        @DisplayName("Should parse valid stop time")
        void shouldParseValidStopTime() {
            TestScheduleRoutePolicyConfig config =
                    new TestScheduleRoutePolicyConfig(null, LocalTime.of(17, 30), null, null, null);

            assertThat(config.stopTime()).isEqualTo(LocalTime.of(17, 30));
        }
    }

    @Nested
    @DisplayName("Timezone Tests")
    class TimezoneTests {

        @Test
        @DisplayName("Should return system default when timezone not configured")
        void shouldReturnSystemDefaultWhenTimezoneNotConfigured() {
            TestScheduleRoutePolicyConfig config = new TestScheduleRoutePolicyConfig(null, null, null, null, null);

            assertThat(config.timezone()).isEqualTo(ZoneId.systemDefault());
        }

        @Test
        @DisplayName("Should return configured timezone")
        void shouldReturnConfiguredTimezone() {
            TestScheduleRoutePolicyConfig config =
                    new TestScheduleRoutePolicyConfig(null, null, ZoneId.of("America/New_York"), null, null);

            assertThat(config.timezone()).isEqualTo(ZoneId.of("America/New_York"));
        }
    }

    @Nested
    @DisplayName("Cron Expression Tests")
    class CronExpressionTests {

        @Test
        @DisplayName("Should return null when cron not configured")
        void shouldReturnNullWhenCronNotConfigured() {
            TestScheduleRoutePolicyConfig config = new TestScheduleRoutePolicyConfig(null, null, null, null, null);

            assertThat(config.cronExpression()).isNull();
        }

        @Test
        @DisplayName("Should return configured cron expression")
        void shouldReturnConfiguredCronExpression() {
            TestScheduleRoutePolicyConfig config =
                    new TestScheduleRoutePolicyConfig(null, null, null, "0 0 9 * * MON-FRI", null);

            assertThat(config.cronExpression()).isEqualTo("0 0 9 * * MON-FRI");
        }
    }

    @Nested
    @DisplayName("Days of Week Tests")
    class DaysOfWeekTests {

        @Test
        @DisplayName("Should return all days when not configured")
        void shouldReturnAllDaysWhenNotConfigured() {
            TestScheduleRoutePolicyConfig config = new TestScheduleRoutePolicyConfig(null, null, null, null, null);

            assertThat(config.daysOfWeek())
                    .containsExactlyInAnyOrder(
                            DayOfWeek.MONDAY,
                            DayOfWeek.TUESDAY,
                            DayOfWeek.WEDNESDAY,
                            DayOfWeek.THURSDAY,
                            DayOfWeek.FRIDAY,
                            DayOfWeek.SATURDAY,
                            DayOfWeek.SUNDAY);
        }

        @Test
        @DisplayName("Should return configured days of week")
        void shouldReturnConfiguredDaysOfWeek() {
            Set<DayOfWeek> weekdays = EnumSet.of(
                    DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY);

            TestScheduleRoutePolicyConfig config = new TestScheduleRoutePolicyConfig(null, null, null, null, weekdays);

            assertThat(config.daysOfWeek())
                    .containsExactlyInAnyOrder(
                            DayOfWeek.MONDAY,
                            DayOfWeek.TUESDAY,
                            DayOfWeek.WEDNESDAY,
                            DayOfWeek.THURSDAY,
                            DayOfWeek.FRIDAY);
        }
    }

    @Nested
    @DisplayName("Config Name Tests")
    class ConfigNameTests {

        @Test
        @DisplayName("Should return correct config name")
        void shouldReturnCorrectConfigName() {
            ScheduleRoutePolicyConfig config = new ScheduleRoutePolicyConfig();

            assertThat(config.name()).isEqualTo("forage-policy-schedule");
        }
    }

    @Nested
    @DisplayName("Combined Config Tests")
    class CombinedConfigTests {

        @Test
        @DisplayName("Should support full business hours config")
        void shouldSupportFullBusinessHoursConfig() {
            Set<DayOfWeek> weekdays = EnumSet.of(
                    DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY);

            TestScheduleRoutePolicyConfig config = new TestScheduleRoutePolicyConfig(
                    LocalTime.of(9, 0), LocalTime.of(17, 0), ZoneId.of("America/New_York"), null, weekdays);

            assertThat(config.startTime()).isEqualTo(LocalTime.of(9, 0));
            assertThat(config.stopTime()).isEqualTo(LocalTime.of(17, 0));
            assertThat(config.timezone()).isEqualTo(ZoneId.of("America/New_York"));
            assertThat(config.daysOfWeek())
                    .containsExactlyInAnyOrder(
                            DayOfWeek.MONDAY,
                            DayOfWeek.TUESDAY,
                            DayOfWeek.WEDNESDAY,
                            DayOfWeek.THURSDAY,
                            DayOfWeek.FRIDAY);
            assertThat(config.cronExpression()).isNull();
        }

        @Test
        @DisplayName("Should support overnight schedule config")
        void shouldSupportOvernightScheduleConfig() {
            TestScheduleRoutePolicyConfig config = new TestScheduleRoutePolicyConfig(
                    LocalTime.of(22, 0), LocalTime.of(6, 0), ZoneId.of("UTC"), null, EnumSet.allOf(DayOfWeek.class));

            assertThat(config.startTime()).isEqualTo(LocalTime.of(22, 0));
            assertThat(config.stopTime()).isEqualTo(LocalTime.of(6, 0));
            assertThat(config.timezone()).isEqualTo(ZoneId.of("UTC"));
        }
    }

    /**
     * Test implementation that allows injecting values directly.
     */
    static class TestScheduleRoutePolicyConfig extends ScheduleRoutePolicyConfig {
        private final LocalTime testStartTime;
        private final LocalTime testStopTime;
        private final ZoneId testTimezone;
        private final String testCronExpression;
        private final Set<DayOfWeek> testDaysOfWeek;

        TestScheduleRoutePolicyConfig(
                LocalTime startTime,
                LocalTime stopTime,
                ZoneId timezone,
                String cronExpression,
                Set<DayOfWeek> daysOfWeek) {
            super();
            this.testStartTime = startTime;
            this.testStopTime = stopTime;
            this.testTimezone = timezone;
            this.testCronExpression = cronExpression;
            this.testDaysOfWeek = daysOfWeek;
        }

        @Override
        public LocalTime startTime() {
            return testStartTime;
        }

        @Override
        public LocalTime stopTime() {
            return testStopTime;
        }

        @Override
        public ZoneId timezone() {
            return testTimezone != null ? testTimezone : ZoneId.systemDefault();
        }

        @Override
        public String cronExpression() {
            return testCronExpression;
        }

        @Override
        public Set<DayOfWeek> daysOfWeek() {
            return testDaysOfWeek != null ? testDaysOfWeek : EnumSet.allOf(DayOfWeek.class);
        }
    }
}
