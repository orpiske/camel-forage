# Research: Route Policy Library

**Feature**: 001-route-policies
**Date**: 2026-02-11

## Research Questions

### 1. RoutePolicyFactory SPI Interface

**Question**: How does Camel's RoutePolicyFactory work and what must we implement?

**Decision**: Implement `org.apache.camel.spi.RoutePolicyFactory` interface

**Rationale**:
- RoutePolicyFactory is Camel's SPI for providing route policies at runtime
- Interface has single method: `RoutePolicy createRoutePolicy(CamelContext camelContext, String routeId, NamedNode route)`
- Factory is called for each route during context initialization
- Returns null if no policy applies to that route

**Implementation Notes**:
- Register via `camelContext.addRoutePolicyFactory(factory)` or auto-configuration
- Factory can return CompositeRoutePolicy for multiple policies on one route
- Must handle cases where route has no policy configured (return null)

### 2. FlipRoutePolicy Implementation

**Question**: How does FlipRoutePolicy work in Camel core?

**Decision**: Create `ForageFlipRoutePolicy` extending `RoutePolicySupport`

**Rationale**:
- Reference: Camel's FlipRoutePolicy in test package (`camel-core/src/test/java/org/apache/camel/processor/FlipRoutePolicy.java`)
- Uses `onExchangeDone` callback to trigger route swap
- Maintains two route names (`name1`, `name2`)
- Route swapping via `RouteController.stopRoute()` and `startRoute()`
- Thread-based execution to avoid blocking exchange processing

**Implementation Notes**:
- Configuration: `flip.paired-route=<otherRouteId>`
- Both routes must reference each other for mutual exclusion
- Use Camel's `RouteController` for route lifecycle management
- Handle edge cases: missing paired route, both routes stopped, startup order

### 3. Schedule Policy Implementation

**Question**: What scheduling options should the time-based policy support?

**Decision**: Support both fixed time windows and cron expressions

**Alternatives Considered**:
| Option | Pros | Cons |
|--------|------|------|
| Fixed time only | Simpler | Limited flexibility |
| Cron only | Powerful | Overkill for simple use cases |
| Both (chosen) | Flexibility + simplicity | Slightly more code |

**Implementation Notes**:
- Fixed time: `start-time`, `stop-time`, `timezone`
- Cron: `cron` expression (mutually exclusive with fixed time)
- Use Camel's built-in `ScheduledRoutePolicy` as base or inspiration
- Consider `SimpleScheduledRoutePolicy` for fixed windows
- Quartz integration optional (heavier dependency)

### 4. Configuration Prefix Pattern

**Question**: How should configuration be structured for per-route policies?

**Decision**: Use pattern `camel.forage.route.policy.<routeId>.<policyName>.<option>`

**Rationale**:
- Follows user's specified format
- Route ID creates natural namespace
- Policy name allows multiple policies per route
- Option suffix provides policy-specific configuration

**Alternatives Considered**:
| Pattern | Pros | Cons |
|---------|------|------|
| `forage.policy.<policyName>.<routeId>` | Groups by policy type | Hard to see all policies for a route |
| `camel.forage.route.policy.<routeId>.*` (chosen) | Route-centric view | Longer property names |

### 5. ServiceLoader Discovery

**Question**: How should external policy providers be discovered?

**Decision**: Use `ServiceLoader<RoutePolicyProvider>` pattern

**Rationale**:
- Follows existing Forage patterns (ModelProvider, ChatMemoryBeanProvider)
- Allows external JARs to provide policies
- No code changes needed for new providers
- @ForageBean annotation enables catalog discovery

**Implementation Notes**:
- Interface: `io.kaoto.forage.core.policy.RoutePolicyProvider extends BeanProvider<RoutePolicy>`
- ServiceLoader file: `META-INF/services/io.kaoto.forage.core.policy.RoutePolicyProvider`
- Provider receives config prefix for its policy instance
- RoutePolicyRegistry caches providers by name (from @ForageBean value)

### 6. Multiple Policies per Route

**Question**: How to apply multiple policies to a single route?

**Decision**: Use comma-separated policy names with CompositeRoutePolicy

**Rationale**:
- Camel supports multiple policies via separate RoutePolicyFactory calls
- Can wrap multiple policies in custom composite for atomic application
- Order may matter (e.g., schedule check before flip)

**Implementation Notes**:
- Config: `camel.forage.route.policy.myRoute.name=schedule,flip`
- Parse comma-separated, resolve each provider, combine results
- Consider priority/order if conflicts possible
- Log warning for conflicting policies (suspend vs. force-start)

## Clarified Behaviors

Based on spec clarification session (2026-02-11):

| Behavior | Decision | Rationale |
|----------|----------|-----------|
| Conflicting policies | Last policy wins | Configuration order provides explicit, predictable precedence users control |
| Invalid config values | Log WARNING, use defaults | Graceful degradation; fail only if no default for required field |
| Unknown policy name | Log WARNING, skip | Route starts without unknown policy; avoids blocking deployment for typos |
| Log levels | INFO for state changes, DEBUG for details | Standard Java logging convention; operators see state, developers see details |

## Technology Decisions

| Aspect | Decision | Rationale |
|--------|----------|-----------|
| Base class | `RoutePolicySupport` | Camel's standard base with lifecycle hooks |
| Scheduling | Java time API + optional Quartz | Lightweight by default, powerful when needed |
| Thread safety | Immutable configs, thread-safe policy state | Multiple routes may share policy instances |
| Logging | SLF4J via Camel | Consistent with Camel ecosystem |
| Testing | Citrus + Testcontainers | Constitution requirement |

## Risk Assessment

| Risk | Likelihood | Impact | Mitigation |
|------|------------|--------|------------|
| Route ID contains dots | Medium | Config parsing breaks | Use escaped/quoted keys or last-segment heuristics |
| Timing issues in flip | Medium | Both routes running | Add mutex or state checks |
| Timezone edge cases | Low | Incorrect scheduling | Use java.time.ZonedDateTime throughout |
| External provider conflict | Low | Duplicate policy names | Log warning, use first found |

## References

- Apache Camel RoutePolicyFactory: `org.apache.camel.spi.RoutePolicyFactory`
- Camel FlipRoutePolicy test: `camel-core/src/test/java/org/apache/camel/processor/FlipRoutePolicy.java`
- Camel ScheduledRoutePolicy: `org.apache.camel.support.RoutePolicySupport`
- Forage BeanProvider pattern: `io.kaoto.forage.core.common.BeanProvider`
