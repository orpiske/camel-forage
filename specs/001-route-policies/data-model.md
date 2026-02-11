# Data Model: Route Policy Library

**Feature**: 001-route-policies
**Date**: 2026-02-11

## Entities

### Core Interfaces

#### RoutePolicyProvider

**Purpose**: Core interface for providing RoutePolicy instances via ServiceLoader discovery.

```java
package io.kaoto.forage.core.policy;

import org.apache.camel.spi.RoutePolicy;
import io.kaoto.forage.core.common.BeanProvider;

/**
 * Provider interface for creating RoutePolicy instances.
 * Implementations are discovered via ServiceLoader and registered
 * in RoutePolicyRegistry by their @ForageBean name.
 */
public interface RoutePolicyProvider extends BeanProvider<RoutePolicy> {

    /**
     * @return The policy name (should match @ForageBean value)
     */
    String name();

    /**
     * Create a RoutePolicy with the given configuration prefix.
     * @param configPrefix The configuration prefix (e.g., "camel.forage.route.policy.myRoute.schedule")
     * @return Configured RoutePolicy instance
     */
    @Override
    RoutePolicy create(String configPrefix);
}
```

**Fields**: None (interface)

**Relationships**: Discovered by RoutePolicyRegistry

---

### Factory Components

#### DefaultCamelForageRoutePolicyFactory

**Purpose**: Main entry point implementing Camel's RoutePolicyFactory SPI.

| Field | Type | Description |
|-------|------|-------------|
| registry | RoutePolicyRegistry | Provider registry (ServiceLoader-backed) |
| configStore | ConfigStore | Configuration access |

**Behaviors**:
- `createRoutePolicy(CamelContext, String routeId, NamedNode)` → RoutePolicy or null
- Reads `camel.forage.route.policy.<routeId>.name` to determine policy names
- Delegates to RoutePolicyRegistry for provider lookup
- Returns null if no policy configured for route

---

#### RoutePolicyRegistry

**Purpose**: Registry of RoutePolicyProvider instances discovered via ServiceLoader.

| Field | Type | Description |
|-------|------|-------------|
| providers | Map<String, RoutePolicyProvider> | Providers keyed by name |

**Behaviors**:
- `getProvider(String name)` → Optional<RoutePolicyProvider>
- `getAllProviders()` → Collection<RoutePolicyProvider>
- Lazy initialization via ServiceLoader
- Caches providers for performance

---

### Policy Implementations

#### ForageScheduleRoutePolicy

**Purpose**: Time-based route activation/deactivation policy.

| Field | Type | Description | Validation |
|-------|------|-------------|------------|
| startTime | LocalTime | Daily start time | Required unless cron set |
| stopTime | LocalTime | Daily stop time | Required unless cron set |
| timezone | ZoneId | Timezone for scheduling | Default: system default |
| cronExpression | String | Cron expression (alternative to fixed times) | Mutually exclusive with startTime/stopTime |
| daysOfWeek | Set<DayOfWeek> | Active days | Default: all days |

**State Transitions**:
```
STOPPED → STARTING → STARTED → STOPPING → STOPPED
         (within schedule window)    (outside window)
```

**Behaviors**:
- `onInit(Route)` - Register schedule checks
- `onStart(Route)` - Check if within time window
- `onStop(Route)` - Cleanup scheduled tasks
- Periodic check: evaluate time window, start/stop route accordingly

---

#### ForageFlipRoutePolicy

**Purpose**: Mutually exclusive route toggling policy.

| Field | Type | Description | Validation |
|-------|------|-------------|------------|
| pairedRouteId | String | ID of the paired route | Required |
| initiallyActive | boolean | Should this route start first | Default: true |

**State Transitions**:
```
ACTIVE (this route) ←→ INACTIVE (this route)
        ↓                      ↑
     Exchange                  |
     completes                 |
        ↓                      |
  Flip to paired ─────────────>
```

**Behaviors**:
- `onExchangeDone(Route, Exchange)` - Trigger flip after exchange completes
- `flipRoutes()` - Stop current route, start paired route (async thread)
- Coordinates with paired route's FlipRoutePolicy instance

---

### Configuration Classes

#### ScheduleRoutePolicyConfigEntries

| ConfigModule | Property Path | Type | Default | Required |
|--------------|---------------|------|---------|----------|
| START_TIME | `schedule.start-time` | string (HH:mm) | null | Yes* |
| STOP_TIME | `schedule.stop-time` | string (HH:mm) | null | Yes* |
| TIMEZONE | `schedule.timezone` | string (ZoneId) | system | No |
| CRON | `schedule.cron` | string | null | No* |
| DAYS_OF_WEEK | `schedule.days-of-week` | string (MON,TUE,...) | all | No |

*Either startTime+stopTime OR cron required, not both.

---

#### FlipRoutePolicyConfigEntries

| ConfigModule | Property Path | Type | Default | Required |
|--------------|---------------|------|---------|----------|
| PAIRED_ROUTE | `flip.paired-route` | string | null | Yes |
| INITIALLY_ACTIVE | `flip.initially-active` | boolean | true | No |

---

### Factory Configuration

#### RoutePolicyFactoryConfigEntries

| ConfigModule | Property Path | Type | Default | Required |
|--------------|---------------|------|---------|----------|
| POLICY_NAME | `<routeId>.name` | string | null | No |

**Note**: This is a dynamic configuration where `<routeId>` is substituted at runtime.

---

## Configuration Examples

### Schedule Policy

```properties
# Fixed time window
camel.forage.route.policy.batchRoute.name=schedule
camel.forage.route.policy.batchRoute.schedule.start-time=09:00
camel.forage.route.policy.batchRoute.schedule.stop-time=17:00
camel.forage.route.policy.batchRoute.schedule.timezone=America/New_York
camel.forage.route.policy.batchRoute.schedule.days-of-week=MON,TUE,WED,THU,FRI

# Cron-based
camel.forage.route.policy.nightlyJob.name=schedule
camel.forage.route.policy.nightlyJob.schedule.cron=0 0 2 * * ?
```

### Flip Policy

```properties
# Primary route (starts first)
camel.forage.route.policy.primaryRoute.name=flip
camel.forage.route.policy.primaryRoute.flip.paired-route=backupRoute
camel.forage.route.policy.primaryRoute.flip.initially-active=true

# Backup route (starts inactive)
camel.forage.route.policy.backupRoute.name=flip
camel.forage.route.policy.backupRoute.flip.paired-route=primaryRoute
camel.forage.route.policy.backupRoute.flip.initially-active=false
```

### Multiple Policies

```properties
# Route with both schedule and custom policy
camel.forage.route.policy.myRoute.name=schedule,custom-policy
camel.forage.route.policy.myRoute.schedule.start-time=08:00
camel.forage.route.policy.myRoute.schedule.stop-time=20:00
camel.forage.route.policy.myRoute.custom-policy.option1=value1
```

---

## Behavior Rules (from Clarifications)

| Scenario | Behavior | Implementation |
|----------|----------|----------------|
| Conflicting policies | Last policy wins | Process policies in `name` order; later policy state overrides earlier |
| Invalid config value | Log WARNING, use default | Catch parse exceptions, log at WARN, apply default if available |
| Missing required config | Throw exception | Throw `MissingConfigException` with clear message |
| Unknown policy name | Log WARNING, skip | Continue processing remaining policies; route starts without unknown |
| State changes | Log at INFO | Use `LOG.info()` for start/stop/suspend/resume events |
| Config details | Log at DEBUG | Use `LOG.debug()` for parsing, resolution, internal decisions |

---

## ServiceLoader Registration

### forage-core-policy

No ServiceLoader (defines interface only).

### forage-policy-schedule

File: `META-INF/services/io.kaoto.forage.core.policy.RoutePolicyProvider`
```
io.kaoto.forage.policy.schedule.ScheduleRoutePolicyProvider
```

### forage-policy-flip

File: `META-INF/services/io.kaoto.forage.core.policy.RoutePolicyProvider`
```
io.kaoto.forage.policy.flip.FlipRoutePolicyProvider
```
