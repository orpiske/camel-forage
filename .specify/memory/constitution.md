<!--
Sync Impact Report
==================
Version change: N/A → 1.0.0 (initial constitution)
Modified principles: N/A (initial creation)
Added sections:
  - Core Principles (5 principles)
  - Technology Stack Constraints
  - Development Workflow
  - Governance
Removed sections: N/A
Templates requiring updates:
  - .specify/templates/plan-template.md (✅ compatible - Constitution Check placeholder aligns)
  - .specify/templates/spec-template.md (✅ compatible - requirements structure aligns)
  - .specify/templates/tasks-template.md (✅ compatible - testing/phase structure aligns)
Follow-up TODOs: None
-->

# Forage Constitution

## Core Principles

### I. Dependency Management & Version Alignment

Forage integrates Apache Camel, Camel Extensions for Quarkus, Quarkus, LangChain4j, and Spring Boot.
Version misalignment causes runtime failures that are difficult to diagnose.

**Non-Negotiable Rules:**

- All dependency versions MUST be declared in the parent `pom.xml` properties section
- Camel, Camel-Quarkus, and Quarkus versions MUST be tested together before adoption
- LangChain4j versions MUST support the same minimum Java version as Camel (Java 17+)
- Spring Boot version MUST align with Spring Framework version used by Camel
- Jackson version MUST match the version used by Apache Camel to prevent serialization conflicts
- Any version upgrade MUST be validated against all three runtimes: plain Camel, Quarkus, Spring Boot
- Version alignment MUST be documented in PR descriptions when dependencies change

**Rationale:** The project supports multiple runtime environments. A version mismatch in one dependency can cascade into subtle bugs that only manifest in specific runtimes.

### II. Code Quality Standards

All code MUST adhere to consistent quality standards to maintain readability and reduce maintenance burden.

**Non-Negotiable Rules:**

- All provider classes MUST have `@ForageBean` annotation with value, component, and description
- All factory classes MUST have `@ForageFactory` annotation with value, component, description, and factoryType
- Every module MUST follow the two-class configuration pattern: `*ConfigEntries` and `*Config`
- Configuration precedence MUST be: environment variables > system properties > properties files > defaults
- ServiceLoader registration files MUST be present in `META-INF/services/` for all providers
- Code formatting via Spotless (Palantir Java Format) MUST pass before merge
- No `TODO` comments in production code without associated GitHub issue reference
- Exception handling MUST use `MissingConfigException` for required missing configuration

**Rationale:** Consistent patterns enable developers to understand any module quickly. Annotation requirements ensure catalog generation works correctly.

### III. Testing Standards

Testing MUST provide confidence that changes work across all supported runtimes and integrations.

**Non-Negotiable Rules:**

- Unit tests MUST use JUnit 5 and AssertJ for assertions
- Integration tests MUST use Citrus Test Framework with Testcontainers
- Integration tests MUST validate all three runtimes: plain Camel, Quarkus, Spring Boot
- New providers MUST include tests demonstrating basic create/configure functionality
- Configuration loading MUST be tested for all three sources: env vars, system props, files
- Tests MUST NOT rely on external services (use Testcontainers for databases, etc.)
- Test containers MUST use mirror images where available (e.g., `mirror.gcr.io/`)

**Rationale:** The project's value proposition depends on seamless runtime switching. Tests must prove this works.

### IV. User Experience Consistency

All Forage modules MUST provide a consistent configuration and usage experience.

**Non-Negotiable Rules:**

- Naming convention: artifacts `forage-<category>-<technology>`, packages `io.kaoto.forage.<category>.<technology>`
- Environment variables: `FORAGE_<TECHNOLOGY>_<PROPERTY>` (uppercase, underscores)
- Properties: `forage.<technology>.<property>` (lowercase, dots)
- All providers MUST support unnamed (default) and named/prefixed configurations
- Properties files MUST be named `<module-name>.properties`
- Error messages MUST clearly indicate which configuration is missing and how to provide it
- README files MUST include Quick Start section with minimal working configuration

**Rationale:** Users should be able to apply knowledge from one Forage module to any other. Predictable naming reduces documentation needs.

### V. Performance Requirements

Forage components MUST not introduce unnecessary overhead or resource leaks.

**Non-Negotiable Rules:**

- Connection pools (JDBC, JMS) MUST have configurable sizing with sensible defaults
- Provider instances MUST be reusable; avoid creating new instances per request
- Configuration loading MUST happen once during initialization, not per operation
- Memory-backed caches MUST have configurable limits to prevent unbounded growth
- Long-running operations MUST support timeout configuration
- Resource cleanup MUST be explicit; avoid relying solely on garbage collection

**Rationale:** Forage is infrastructure code that runs in production. Resource efficiency directly impacts application performance.

## Technology Stack Constraints

**Required Compatibility Matrix:**

| Component | Minimum Version | Alignment Requirement |
|-----------|-----------------|----------------------|
| Java | 17+ | All dependencies MUST support Java 17 |
| Apache Camel | 4.16.0+ | Base version for all integrations |
| Camel-Quarkus | 3.x | MUST align with Camel major version |
| Quarkus | 3.x | MUST align with Camel-Quarkus requirements |
| LangChain4j | 1.7.1+ | MUST support Camel's minimum Java version |
| Spring Boot | 3.x | MUST align with Spring Framework in Camel |

**Build Tools:**

- Maven 3.9+ for builds
- Spotless plugin for formatting (Palantir Java Format)
- No Gradle support (Maven-only for consistency)

**Testing Stack:**

- JUnit 5 for all tests
- AssertJ for fluent assertions
- Citrus Test Framework for integration tests
- Testcontainers for external service dependencies

## Development Workflow

**Before Starting Work:**

1. Verify dependency versions in parent `pom.xml` are current
2. Run `mvn spotless:check` to ensure formatting compliance
3. Check for existing patterns in similar modules

**During Development:**

1. Follow BeanProvider/Config two-class pattern
2. Add ServiceLoader registration for new providers
3. Include `@ForageBean` or `@ForageFactory` annotation
4. Write tests covering all configuration sources

**Before Submitting PR:**

1. Run `mvn clean install` (full build with tests)
2. Test against all three runtimes if integration changes
3. Document any version changes in PR description
4. Ensure all CI checks pass

**Code Review Gates:**

- Annotation presence verified
- Configuration pattern followed
- Tests cover new functionality
- No version alignment violations
- Formatting compliance (Spotless)

## Governance

**Constitution Authority:**

- This constitution supersedes conflicting practices in documentation or existing code
- Amendments require documented justification and team review
- Migration plans required for breaking changes to existing principles

**Compliance Verification:**

- PRs MUST pass automated formatting checks (Spotless)
- Code reviews MUST verify annotation and pattern compliance
- Integration test failures on any runtime MUST block merge
- Version alignment violations MUST be justified or resolved before merge

**Amendment Process:**

1. Propose change with rationale
2. Document impact on existing code
3. Create migration plan if needed
4. Update constitution version (MAJOR for removals/redefinitions, MINOR for additions, PATCH for clarifications)

**Version**: 1.0.0 | **Ratified**: 2026-02-11 | **Last Amended**: 2026-02-11
