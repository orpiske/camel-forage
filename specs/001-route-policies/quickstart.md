# Quickstart: Forage Route Policy Library

## Overview

The Forage Route Policy Library provides configurable route policies for Apache Camel without requiring Java code. Configure policies via properties files, environment variables, or system properties.

## Dependencies

Add to your `pom.xml`:

```xml
<!-- Core factory (required) -->
<dependency>
    <groupId>io.kaoto.forage</groupId>
    <artifactId>forage-policy-factory</artifactId>
    <version>${forage.version}</version>
</dependency>

<!-- Schedule policy (optional) -->
<dependency>
    <groupId>io.kaoto.forage</groupId>
    <artifactId>forage-policy-schedule</artifactId>
    <version>${forage.version}</version>
</dependency>

<!-- Flip policy (optional) -->
<dependency>
    <groupId>io.kaoto.forage</groupId>
    <artifactId>forage-policy-flip</artifactId>
    <version>${forage.version}</version>
</dependency>
```

## Basic Usage

### 1. Schedule Policy (Time-Based Route Control)

Run a route only during business hours:

```properties
# application.properties
camel.forage.route.policy.orderProcessor.name=schedule
camel.forage.route.policy.orderProcessor.schedule.start-time=09:00
camel.forage.route.policy.orderProcessor.schedule.stop-time=17:00
camel.forage.route.policy.orderProcessor.schedule.timezone=America/New_York
camel.forage.route.policy.orderProcessor.schedule.days-of-week=MON,TUE,WED,THU,FRI
```

Route definition (YAML):
```yaml
- route:
    id: orderProcessor
    from:
      uri: jms:queue:orders
    steps:
      - to: direct:processOrder
```

### 2. Schedule Policy (Cron Expression)

Run a route based on cron schedule:

```properties
camel.forage.route.policy.nightlySync.name=schedule
camel.forage.route.policy.nightlySync.schedule.cron=0 0 2 * * ?
```

### 3. Flip Policy (Mutually Exclusive Routes)

Toggle between primary and backup routes:

```properties
# Primary route starts active
camel.forage.route.policy.primaryRoute.name=flip
camel.forage.route.policy.primaryRoute.flip.paired-route=backupRoute
camel.forage.route.policy.primaryRoute.flip.initially-active=true

# Backup route starts inactive
camel.forage.route.policy.backupRoute.name=flip
camel.forage.route.policy.backupRoute.flip.paired-route=primaryRoute
camel.forage.route.policy.backupRoute.flip.initially-active=false
```

After each exchange completes, routes flip automatically.

### 4. Multiple Policies

Apply multiple policies to a single route:

```properties
camel.forage.route.policy.myRoute.name=schedule,custom-policy
camel.forage.route.policy.myRoute.schedule.start-time=08:00
camel.forage.route.policy.myRoute.schedule.stop-time=20:00
```

## Environment Variable Configuration

Override any property via environment variables:

```bash
# Pattern: CAMEL_FORAGE_ROUTE_POLICY_<ROUTEID>_<OPTION>
export CAMEL_FORAGE_ROUTE_POLICY_ORDERPROCESSOR_NAME=schedule
export CAMEL_FORAGE_ROUTE_POLICY_ORDERPROCESSOR_SCHEDULE_START_TIME=08:00
export CAMEL_FORAGE_ROUTE_POLICY_ORDERPROCESSOR_SCHEDULE_STOP_TIME=18:00
```

## Camel JBang Usage

```bash
# Run with schedule policy
camel run route.yaml --properties=forage-policy-factory.properties

# Or use environment variables
CAMEL_FORAGE_ROUTE_POLICY_MYROUTE_NAME=schedule \
CAMEL_FORAGE_ROUTE_POLICY_MYROUTE_SCHEDULE_START_TIME=09:00 \
CAMEL_FORAGE_ROUTE_POLICY_MYROUTE_SCHEDULE_STOP_TIME=17:00 \
camel run route.yaml
```

## Creating Custom Policies

Implement `RoutePolicyProvider` and register via ServiceLoader:

```java
@ForageBean(
    value = "my-custom-policy",
    components = {"camel-core"},
    feature = "Route Policy",
    description = "My custom route policy")
public class MyCustomPolicyProvider implements RoutePolicyProvider {

    @Override
    public String name() {
        return "my-custom-policy";
    }

    @Override
    public RoutePolicy create(String configPrefix) {
        MyCustomPolicyConfig config = new MyCustomPolicyConfig(configPrefix);
        return new MyCustomRoutePolicy(config);
    }
}
```

Register in `META-INF/services/io.kaoto.forage.core.policy.RoutePolicyProvider`:
```
com.example.MyCustomPolicyProvider
```

## Behavior Notes

- **Policy order matters**: When multiple policies are applied (`name=schedule,flip`), the last policy wins on conflicts
- **Invalid config**: If a configuration value is invalid, a warning is logged and defaults are used where possible
- **Unknown policy**: If a policy name is not found (typo, missing JAR), a warning is logged and the route starts without it
- **Logging**: State changes (start/stop/suspend) log at INFO level; configuration details log at DEBUG level

## Troubleshooting

### Policy Not Applied

1. Verify route ID matches configuration exactly (case-sensitive)
2. Check that policy JAR is on classpath
3. Enable debug logging: `logging.level.io.kaoto.forage.policy=DEBUG`

### Schedule Not Working

1. Verify timezone is correct
2. Check server time vs. configured times
3. Ensure start-time is before stop-time

### Flip Not Triggering

1. Verify both routes reference each other
2. Check that paired-route ID exists
3. Ensure only one route has `initially-active=true`
