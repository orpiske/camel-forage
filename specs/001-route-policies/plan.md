# Implementation Plan: Route Policy Library

**Branch**: `001-route-policies` | **Date**: 2026-02-11 | **Spec**: [spec.md](spec.md)
**Input**: Feature specification from `/specs/001-route-policies/spec.md`

## Summary

Implement a library of configurable route policies for Apache Camel using Forage patterns. The core is `DefaultCamelForageRoutePolicyFactory` implementing Camel's `RoutePolicyFactory` SPI, which delegates to pluggable `RoutePolicyProvider` implementations discovered via ServiceLoader. Initial policies: **Schedule** (time-based) and **Flip** (mutually exclusive routes). Configuration follows `camel.forage.route.policy.<route-id>.<policy-name>.<option>`.

### Clarifications Applied

| Decision | Resolution |
|----------|------------|
| Initial scope | Schedule + Flip only (throttling deferred) |
| Conflict resolution | Last policy wins (configuration order) |
| Invalid config handling | Log warning, use defaults where possible |
| Unknown policy | Log warning, skip policy, route starts |
| Log levels | State changes at INFO, details at DEBUG |

## Technical Context

**Language/Version**: Java 17+
**Primary Dependencies**: Apache Camel 4.16+ (camel-api: RoutePolicyFactory, RoutePolicy, RoutePolicySupport)
**Storage**: N/A (stateless policy configuration)
**Testing**: JUnit 5, AssertJ, Citrus Test Framework, Testcontainers
**Target Platform**: JVM (plain Camel, Quarkus, Spring Boot runtimes)
**Project Type**: Library (multi-module Maven project)
**Performance Goals**: Policy resolution <1ms; no runtime overhead beyond policy logic
**Constraints**: No external dependencies beyond Camel core; config loading at initialization only
**Scale/Scope**: 2 initial policy types; extensible for unlimited external providers

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| Principle | Status | Notes |
|-----------|--------|-------|
| I. Dependency Management | PASS | Only Apache Camel core; version in parent pom |
| II. Code Quality Standards | PASS | @ForageBean on providers; Two-Class Pattern; ServiceLoader registration |
| III. Testing Standards | PASS | JUnit 5/AssertJ unit tests; Citrus integration tests for 3 runtimes |
| IV. User Experience Consistency | PASS | Naming: `forage-policy-*`; config: `camel.forage.route.policy.*` |
| V. Performance Requirements | PASS | Stateless providers; one-time config load; reusable instances |

## Project Structure

### Documentation (this feature)

```text
specs/001-route-policies/
├── plan.md              # This file
├── research.md          # Phase 0 output
├── data-model.md        # Phase 1 output
├── quickstart.md        # Phase 1 output
├── contracts/           # N/A (library, not API)
└── tasks.md             # Phase 2 output (/speckit.tasks)
```

### Source Code (repository root)

```text
core/
└── forage-core-policy/                         # Core interfaces
    └── src/main/java/io/kaoto/forage/core/policy/
        ├── RoutePolicyProvider.java            # Provider interface
        └── package-info.java

library/
└── policy/                                     # Policy implementations
    ├── forage-policy-factory/                  # Core factory
    │   └── src/main/java/io/kaoto/forage/policy/factory/
    │       ├── DefaultCamelForageRoutePolicyFactory.java
    │       ├── RoutePolicyFactoryConfig.java
    │       ├── RoutePolicyFactoryConfigEntries.java
    │       └── RoutePolicyRegistry.java
    │
    ├── forage-policy-schedule/                 # Schedule policy
    │   └── src/main/java/io/kaoto/forage/policy/schedule/
    │       ├── ScheduleRoutePolicyProvider.java
    │       ├── ScheduleRoutePolicyConfig.java
    │       ├── ScheduleRoutePolicyConfigEntries.java
    │       └── ForageScheduleRoutePolicy.java
    │
    └── forage-policy-flip/                     # Flip policy
        └── src/main/java/io/kaoto/forage/policy/flip/
            ├── FlipRoutePolicyProvider.java
            ├── FlipRoutePolicyConfig.java
            ├── FlipRoutePolicyConfigEntries.java
            └── ForageFlipRoutePolicy.java

integration-tests/
└── policy/                                     # Integration tests
    └── src/test/java/io/kaoto/forage/policy/it/
        ├── SchedulePolicyTest.java
        └── FlipPolicyTest.java
```

**Structure Decision**: Multi-module Maven structure following existing Forage patterns. Core interface in `forage-core-policy`, factory in `forage-policy-factory`, individual policies in separate modules for independent versioning.

## Architecture Design

### Component Diagram

```
┌─────────────────────────────────────────────────────────────────────────┐
│                           Camel Context                                  │
│  ┌─────────────────────────────────────────────────────────────────────┐│
│  │ RoutePolicyFactory (SPI)                                            ││
│  │  └── DefaultCamelForageRoutePolicyFactory                           ││
│  │       │                                                             ││
│  │       ├── reads: camel.forage.route.policy.<routeId>.name           ││
│  │       │                                                             ││
│  │       └── delegates to RoutePolicyRegistry                          ││
│  │             │                                                       ││
│  │             └── ServiceLoader<RoutePolicyProvider>                  ││
│  │                   ├── ScheduleRoutePolicyProvider                   ││
│  │                   ├── FlipRoutePolicyProvider                       ││
│  │                   └── [External providers...]                       ││
│  └─────────────────────────────────────────────────────────────────────┘│
└─────────────────────────────────────────────────────────────────────────┘
```

### Configuration Flow

```
1. Camel calls RoutePolicyFactory.createRoutePolicy(camelContext, routeId, route)
2. DefaultCamelForageRoutePolicyFactory reads:
   - camel.forage.route.policy.<routeId>.name = "schedule,flip" (comma-separated)
3. For each policy name (in order):
   - Look up RoutePolicyProvider by name from RoutePolicyRegistry
   - If not found: log WARNING, skip policy
   - Pass config prefix: camel.forage.route.policy.<routeId>.<policyName>
   - Provider creates configured RoutePolicy instance
4. Apply policies in order (last wins on conflict)
5. Return policies (or null if none configured)
```

### Behavior Rules (from Clarifications)

| Scenario | Behavior |
|----------|----------|
| Conflicting policies | Last policy in `name` list wins |
| Invalid config value | Log WARNING, use default if available |
| Missing required config | Throw `MissingConfigException` |
| Unknown policy name | Log WARNING, skip, continue with others |
| State changes | Log at INFO level |
| Config details | Log at DEBUG level |

### Configuration Schema

```properties
# Per-route policy assignment (comma-separated, order matters for conflicts)
camel.forage.route.policy.myRoute.name=schedule

# Schedule policy options
camel.forage.route.policy.myRoute.schedule.start-time=09:00
camel.forage.route.policy.myRoute.schedule.stop-time=17:00
camel.forage.route.policy.myRoute.schedule.timezone=America/New_York
camel.forage.route.policy.myRoute.schedule.cron=0 0 9 * * MON-FRI
camel.forage.route.policy.myRoute.schedule.days-of-week=MON,TUE,WED,THU,FRI

# Flip policy options
camel.forage.route.policy.routeA.name=flip
camel.forage.route.policy.routeA.flip.paired-route=routeB
camel.forage.route.policy.routeA.flip.initially-active=true

camel.forage.route.policy.routeB.name=flip
camel.forage.route.policy.routeB.flip.paired-route=routeA
camel.forage.route.policy.routeB.flip.initially-active=false
```

## Complexity Tracking

No constitution violations. Design follows existing Forage patterns.
