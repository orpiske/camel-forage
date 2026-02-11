# Feature Specification: Route Policy Library

**Feature Branch**: `001-route-policies`
**Created**: 2026-02-11
**Status**: Draft
**Input**: User description: "Add support for a library of factories and beans for custom route policies."

## Clarifications

### Session 2026-02-11

- Q: Which policies should be in-scope for the initial implementation? → A: Schedule + Flip only (throttling deferred to follow-up)
- Q: How should conflicting policies be resolved? → A: Last policy wins (later policies in configuration order override earlier ones)
- Q: How should invalid configuration values be handled? → A: Log warning and use defaults where possible
- Q: What happens when a referenced policy provider is not found? → A: Log warning and skip unknown policy (route starts without it)
- Q: What log levels should be used for policy events? → A: State changes (start/stop/suspend) at INFO, details at DEBUG

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Configure Scheduled Route Policy (Priority: P1)

A Camel developer wants routes to run only during specific time windows (e.g., business hours, maintenance windows). They configure a schedule policy through properties files without writing Java code.

**Why this priority**: Time-based scheduling is a foundational policy for batch processing, business hours operations, and maintenance windows.

**Independent Test**: Can be fully tested by configuring schedule windows and verifying routes start/stop according to schedule.

**Acceptance Scenarios**:

1. **Given** a schedule policy configured for 9 AM to 5 PM, **When** current time is within the window, **Then** the route is active.
2. **Given** a schedule policy configured for 9 AM to 5 PM, **When** current time is outside the window, **Then** the route is suspended.
3. **Given** a schedule policy with cron expression, **When** the cron expression matches, **Then** the route activates accordingly.
4. **Given** a schedule policy with timezone configuration, **When** evaluating the schedule, **Then** the timezone is respected.

---

### User Story 2 - Configure Flip Route Policy (Priority: P1)

A Camel developer wants to toggle between two mutually exclusive routes, where completing an exchange on one route causes it to stop and the paired route to start.

**Why this priority**: Flip routing enables failover patterns and alternating processing strategies critical for resilient systems.

**Independent Test**: Can be fully tested by configuring two paired routes and verifying they flip after each exchange completion.

**Acceptance Scenarios**:

1. **Given** two routes configured as a flip pair, **When** an exchange completes on the active route, **Then** that route stops and the paired route starts.
2. **Given** a flip policy with initially-active=true on one route, **When** the Camel context starts, **Then** only that route is active.
3. **Given** flip policies on both routes referencing each other, **When** routes are deployed, **Then** only one route is active at any time.

---

### User Story 3 - Configure Custom Route Policy via Factory (Priority: P2)

A Camel developer wants to register and use custom route policy implementations through the Forage factory pattern.

**Why this priority**: Extensibility is important but most users will use built-in policies.

**Independent Test**: Can be fully tested by implementing a custom policy provider and verifying it's discoverable via ServiceLoader.

**Acceptance Scenarios**:

1. **Given** a custom route policy implementation with @ForageBean annotation, **When** the Camel context initializes, **Then** the policy is discoverable via ServiceLoader.
2. **Given** a custom policy with configuration entries, **When** properties are set, **Then** the policy uses configured values.

---

### User Story 4 - Use Multiple Route Policies Together (Priority: P3)

A Camel developer wants to combine multiple route policies on a single route (e.g., schedule + flip).

**Why this priority**: Combining policies is an advanced use case that builds on basic policy usage.

**Independent Test**: Can be fully tested by configuring multiple policies on one route and verifying both apply.

**Acceptance Scenarios**:

1. **Given** a route with both schedule and flip policies, **When** both conditions are satisfied, **Then** both policies apply their constraints.
2. **Given** multiple policies configured with different IDs, **When** loading configurations, **Then** each policy loads its respective configuration using its ID prefix.

---

### Edge Cases

- **Conflicting policies**: When multiple policies conflict (e.g., one suspends, another activates), the last policy in configuration order wins. Users control precedence via policy order in the `name` property (e.g., `name=schedule,flip` means flip overrides schedule).
- **Invalid configuration**: When configuration values are invalid (malformed cron, invalid time format), log a warning and use sensible defaults where possible. If no default exists for a required field, throw `MissingConfigException`.
- **Unknown policy**: When a policy name is referenced but no matching provider is found via ServiceLoader, log a warning and skip that policy. The route starts without the unknown policy applied.
- **Logging visibility**: Policy state changes (start, stop, suspend, resume) are logged at INFO level for operational visibility. Configuration parsing and internal decisions are logged at DEBUG level for troubleshooting.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST provide a `RoutePolicyProvider` interface extending `BeanProvider<RoutePolicy>` for ServiceLoader discovery.
- **FR-002**: System MUST provide `ScheduleRoutePolicyProvider` implementing time-based route activation with configurable start/stop times, timezone, and cron expressions.
- **FR-002a**: System MUST provide `FlipRoutePolicyProvider` implementing mutually exclusive route toggling with configurable paired-route and initially-active settings.
- **FR-003**: System MUST provide configuration classes following the Two-Class Pattern (ConfigEntries + Config) for each policy type.
- **FR-004**: System MUST support named/prefixed configurations to allow multiple instances of the same policy type.
- **FR-005**: All policy provider classes MUST be annotated with `@ForageBean` specifying the policy name, component, and description.
- **FR-006**: System MUST register providers via `META-INF/services/io.kaoto.forage.core.policy.RoutePolicyProvider` for ServiceLoader discovery.
- **FR-007**: Configuration MUST support environment variables, system properties, and properties files with standard Forage precedence.
- **FR-008**: System MUST provide clear error messages via `MissingConfigException` when required configuration is absent.
- **FR-009**: System MUST log policy lifecycle events: state changes (creation, activation, suspension, resumption) at INFO level; configuration details and internal decisions at DEBUG level.
- **FR-010**: System MUST support factory pattern via `RoutePolicyFactory` interface with `@ForageFactory` annotation for policy instantiation.

### Key Entities

- **RoutePolicyProvider**: Core interface for providing RoutePolicy instances; extends BeanProvider pattern; discovered via ServiceLoader.
- **RoutePolicyFactory**: Factory interface for creating RoutePolicy instances with complex initialization; annotated with @ForageFactory.
- **ScheduleRoutePolicyProvider**: Concrete provider for time-based route scheduling; configures start-time, stop-time, timezone, cron expression, and days-of-week.
- **FlipRoutePolicyProvider**: Concrete provider for mutually exclusive route toggling; configures paired-route ID and initially-active flag.
- **DefaultCamelForageRoutePolicyFactory**: Factory implementing Camel's RoutePolicyFactory SPI; reads configuration to determine which policies apply to each route.
- **Policy Configuration**: Config and ConfigEntries classes for each policy type; follows naming convention `camel.forage.route.policy.<routeId>.<policyName>.<option>`.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Developers can configure a route policy without writing any Java code beyond route definition.
- **SC-002**: Policy configuration via properties file takes under 2 minutes for a new developer following documentation.
- **SC-003**: All provided policy providers are discoverable via ServiceLoader without additional classpath configuration.
- **SC-004**: Configuration changes to policies are applied without code recompilation (properties/environment-based).
- **SC-005**: Policy behavior matches documented configuration parameters with 100% accuracy.
- **SC-006**: Error messages for missing or invalid configuration clearly identify the problem and suggested fix.

## Assumptions

- Route policies will follow existing Forage patterns for BeanProvider and configuration.
- Initial implementation focuses on Schedule and Flip policies as the core use cases.
- Additional policy types (throttling, suspend/resume) may be added in subsequent iterations.
- External components can provide custom policies via ServiceLoader without modifying this library.
- Integration with Camel JBang plugin follows existing Forage catalog patterns.
- Policy naming follows convention: `camel.forage.route.policy.<routeId>.<policyName>.<option>`.
