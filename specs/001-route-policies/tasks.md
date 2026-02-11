# Tasks: Route Policy Library

**Input**: Design documents from `/specs/001-route-policies/`
**Prerequisites**: plan.md, spec.md, data-model.md, research.md, quickstart.md

**Tests**: Unit tests and integration tests included per Forage constitution requirements.

**Organization**: Tasks grouped by user story for independent implementation and testing.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (US1, US2, US3, US4)
- Exact file paths included in descriptions

## Path Conventions

Multi-module Maven project:
- `core/forage-core-policy/` - Core interface module
- `library/policy/forage-policy-factory/` - Factory module
- `library/policy/forage-policy-schedule/` - Schedule policy module
- `library/policy/forage-policy-flip/` - Flip policy module
- `integration-tests/policy/` - Integration tests

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Create Maven module structure and core interface

- [x] T001 Create core module directory structure at core/forage-core-policy/
- [x] T002 Create core module pom.xml at core/forage-core-policy/pom.xml with forage-core-common dependency
- [x] T003 [P] Create library/policy/ parent directory and pom.xml at library/policy/pom.xml
- [x] T004 [P] Create forage-policy-factory module directory at library/policy/forage-policy-factory/
- [x] T005 [P] Create forage-policy-schedule module directory at library/policy/forage-policy-schedule/
- [x] T006 [P] Create forage-policy-flip module directory at library/policy/forage-policy-flip/
- [x] T007 Create integration-tests/policy module directory at integration-tests/policy/
- [x] T008 Update parent pom.xml to include new modules in reactor build

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core interfaces and factory that ALL policies depend on

**CRITICAL**: No policy implementation can begin until this phase is complete

- [x] T009 Create RoutePolicyProvider interface at core/forage-core-policy/src/main/java/io/kaoto/forage/core/policy/RoutePolicyProvider.java
- [x] T010 Create package-info.java at core/forage-core-policy/src/main/java/io/kaoto/forage/core/policy/package-info.java
- [x] T011 Create forage-policy-factory pom.xml at library/policy/forage-policy-factory/pom.xml with camel-api dependency
- [x] T012 Create RoutePolicyRegistry at library/policy/forage-policy-factory/src/main/java/io/kaoto/forage/policy/factory/RoutePolicyRegistry.java
- [x] T013 Create RoutePolicyFactoryConfigEntries at library/policy/forage-policy-factory/src/main/java/io/kaoto/forage/policy/factory/RoutePolicyFactoryConfigEntries.java
- [x] T014 Create RoutePolicyFactoryConfig at library/policy/forage-policy-factory/src/main/java/io/kaoto/forage/policy/factory/RoutePolicyFactoryConfig.java
- [x] T015 Create DefaultCamelForageRoutePolicyFactory at library/policy/forage-policy-factory/src/main/java/io/kaoto/forage/policy/factory/DefaultCamelForageRoutePolicyFactory.java
- [x] T016 [P] Create unit test for RoutePolicyRegistry at library/policy/forage-policy-factory/src/test/java/io/kaoto/forage/policy/factory/RoutePolicyRegistryTest.java
- [x] T017 [P] Create unit test for DefaultCamelForageRoutePolicyFactory at library/policy/forage-policy-factory/src/test/java/io/kaoto/forage/policy/factory/DefaultCamelForageRoutePolicyFactoryTest.java

**Checkpoint**: Foundation ready - policy implementation can now begin

---

## Phase 3: User Story 1 - Configure Scheduled Route Policy (Priority: P1)

**Goal**: Camel developers can configure time-based route activation via properties without Java code

**Independent Test**: Configure schedule window in properties, verify route starts/stops according to schedule

### Implementation for User Story 1

- [x] T018 [US1] Create forage-policy-schedule pom.xml at library/policy/forage-policy-schedule/pom.xml
- [x] T019 [P] [US1] Create ScheduleRoutePolicyConfigEntries at library/policy/forage-policy-schedule/src/main/java/io/kaoto/forage/policy/schedule/ScheduleRoutePolicyConfigEntries.java
- [x] T020 [P] [US1] Create ScheduleRoutePolicyConfig at library/policy/forage-policy-schedule/src/main/java/io/kaoto/forage/policy/schedule/ScheduleRoutePolicyConfig.java
- [x] T021 [US1] Create ForageScheduleRoutePolicy extending RoutePolicySupport at library/policy/forage-policy-schedule/src/main/java/io/kaoto/forage/policy/schedule/ForageScheduleRoutePolicy.java
- [x] T022 [US1] Create ScheduleRoutePolicyProvider with @ForageBean at library/policy/forage-policy-schedule/src/main/java/io/kaoto/forage/policy/schedule/ScheduleRoutePolicyProvider.java
- [x] T023 [US1] Create ServiceLoader registration at library/policy/forage-policy-schedule/src/main/resources/META-INF/services/io.kaoto.forage.core.policy.RoutePolicyProvider
- [x] T024 [P] [US1] Create unit test for ScheduleRoutePolicyConfig at library/policy/forage-policy-schedule/src/test/java/io/kaoto/forage/policy/schedule/ScheduleRoutePolicyConfigTest.java
- [x] T025 [P] [US1] Create unit test for ForageScheduleRoutePolicy at library/policy/forage-policy-schedule/src/test/java/io/kaoto/forage/policy/schedule/ForageScheduleRoutePolicyTest.java
- [ ] T026 [US1] Create integration test for schedule policy at integration-tests/policy/src/test/java/io/kaoto/forage/policy/it/SchedulePolicyTest.java

**Checkpoint**: Schedule policy fully functional and independently testable

---

## Phase 4: User Story 2 - Configure Flip Route Policy (Priority: P1)

**Goal**: Camel developers can configure mutually exclusive route toggling via properties

**Independent Test**: Configure two paired routes, verify they flip after each exchange completion

### Implementation for User Story 2

- [x] T027 [US2] Create forage-policy-flip pom.xml at library/policy/forage-policy-flip/pom.xml
- [x] T028 [P] [US2] Create FlipRoutePolicyConfigEntries at library/policy/forage-policy-flip/src/main/java/io/kaoto/forage/policy/flip/FlipRoutePolicyConfigEntries.java
- [x] T029 [P] [US2] Create FlipRoutePolicyConfig at library/policy/forage-policy-flip/src/main/java/io/kaoto/forage/policy/flip/FlipRoutePolicyConfig.java
- [x] T030 [US2] Create ForageFlipRoutePolicy extending RoutePolicySupport at library/policy/forage-policy-flip/src/main/java/io/kaoto/forage/policy/flip/ForageFlipRoutePolicy.java
- [x] T031 [US2] Create FlipRoutePolicyProvider with @ForageBean at library/policy/forage-policy-flip/src/main/java/io/kaoto/forage/policy/flip/FlipRoutePolicyProvider.java
- [x] T032 [US2] Create ServiceLoader registration at library/policy/forage-policy-flip/src/main/resources/META-INF/services/io.kaoto.forage.core.policy.RoutePolicyProvider
- [x] T033 [P] [US2] Create unit test for FlipRoutePolicyConfig at library/policy/forage-policy-flip/src/test/java/io/kaoto/forage/policy/flip/FlipRoutePolicyConfigTest.java
- [x] T034 [P] [US2] Create unit test for ForageFlipRoutePolicy at library/policy/forage-policy-flip/src/test/java/io/kaoto/forage/policy/flip/ForageFlipRoutePolicyTest.java
- [ ] T035 [US2] Create integration test for flip policy at integration-tests/policy/src/test/java/io/kaoto/forage/policy/it/FlipPolicyTest.java

**Checkpoint**: Flip policy fully functional and independently testable

---

## Phase 5: User Story 3 - Configure Custom Route Policy via Factory (Priority: P2)

**Goal**: External developers can create custom policies discoverable via ServiceLoader

**Independent Test**: Implement a mock custom policy provider, verify it's discovered and used

### Implementation for User Story 3

- [ ] T036 [US3] Add extensibility documentation to forage-policy-factory module README at library/policy/forage-policy-factory/README.md
- [ ] T037 [US3] Create example custom policy provider in test scope at library/policy/forage-policy-factory/src/test/java/io/kaoto/forage/policy/factory/ExampleCustomPolicyProvider.java
- [ ] T038 [US3] Create test ServiceLoader registration for example provider at library/policy/forage-policy-factory/src/test/resources/META-INF/services/io.kaoto.forage.core.policy.RoutePolicyProvider
- [ ] T039 [US3] Create integration test verifying custom provider discovery at library/policy/forage-policy-factory/src/test/java/io/kaoto/forage/policy/factory/CustomProviderDiscoveryTest.java

**Checkpoint**: Custom policy extensibility verified

---

## Phase 6: User Story 4 - Use Multiple Route Policies Together (Priority: P3)

**Goal**: Developers can apply multiple policies to a single route with predictable conflict resolution

**Independent Test**: Configure multiple policies on one route, verify both apply with last-wins semantics

### Implementation for User Story 4

- [ ] T040 [US4] Enhance DefaultCamelForageRoutePolicyFactory to support comma-separated policy names at library/policy/forage-policy-factory/src/main/java/io/kaoto/forage/policy/factory/DefaultCamelForageRoutePolicyFactory.java
- [ ] T041 [US4] Add conflict resolution logging (last policy wins) at library/policy/forage-policy-factory/src/main/java/io/kaoto/forage/policy/factory/DefaultCamelForageRoutePolicyFactory.java
- [ ] T042 [US4] Create unit test for multiple policy resolution at library/policy/forage-policy-factory/src/test/java/io/kaoto/forage/policy/factory/MultiplePolicyResolutionTest.java
- [ ] T043 [US4] Create integration test for combined schedule+flip at integration-tests/policy/src/test/java/io/kaoto/forage/policy/it/MultiplePoliciesTest.java

**Checkpoint**: Multiple policies work together with predictable behavior

---

## Phase 7: Polish & Cross-Cutting Concerns

**Purpose**: Documentation, error handling, and cross-story improvements

- [ ] T044 [P] Add INFO logging for policy state changes (start/stop/suspend/resume) across all policies
- [ ] T045 [P] Add DEBUG logging for configuration parsing and internal decisions
- [ ] T046 [P] Implement graceful handling for invalid config values (log WARNING, use defaults)
- [ ] T047 [P] Implement graceful handling for unknown policy names (log WARNING, skip)
- [ ] T048 [P] Create forage-core-policy README at core/forage-core-policy/README.md
- [ ] T049 [P] Create forage-policy-schedule README at library/policy/forage-policy-schedule/README.md
- [ ] T050 [P] Create forage-policy-flip README at library/policy/forage-policy-flip/README.md
- [ ] T051 Run mvn spotless:apply to format all code
- [ ] T052 Run mvn clean install to verify full build
- [ ] T053 Validate quickstart.md examples work end-to-end

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup - BLOCKS all user stories
- **User Story 1 (Phase 3)**: Depends on Foundational
- **User Story 2 (Phase 4)**: Depends on Foundational (can parallel with US1)
- **User Story 3 (Phase 5)**: Depends on Foundational
- **User Story 4 (Phase 6)**: Depends on US1 + US2 (needs both policies to test combination)
- **Polish (Phase 7)**: Depends on all user stories

### User Story Dependencies

```
Phase 1: Setup
    ↓
Phase 2: Foundational (RoutePolicyProvider, Factory, Registry)
    ↓
    ├──→ Phase 3: US1 (Schedule Policy) ──┐
    │                                      │
    └──→ Phase 4: US2 (Flip Policy) ───────┼──→ Phase 6: US4 (Multiple Policies)
                                           │
         Phase 5: US3 (Custom Extensibility)┘
                        ↓
                 Phase 7: Polish
```

### Parallel Opportunities

**Within Phase 1 (Setup)**:
- T003, T004, T005, T006 can run in parallel (different directories)

**Within Phase 2 (Foundational)**:
- T016, T017 can run in parallel (different test files)

**User Stories US1 and US2**:
- Can run entirely in parallel once Foundational is complete
- Different modules, no dependencies on each other

**Within Each User Story**:
- ConfigEntries and Config classes can be created in parallel
- Unit tests can be created in parallel

---

## Parallel Example: US1 + US2 Simultaneously

```bash
# After Foundational phase completes, launch both stories in parallel:

# Developer A: User Story 1 (Schedule)
Task: "Create forage-policy-schedule pom.xml"
Task: "Create ScheduleRoutePolicyConfigEntries"
Task: "Create ScheduleRoutePolicyConfig"
# ...continues US1

# Developer B: User Story 2 (Flip)
Task: "Create forage-policy-flip pom.xml"
Task: "Create FlipRoutePolicyConfigEntries"
Task: "Create FlipRoutePolicyConfig"
# ...continues US2
```

---

## Implementation Strategy

### MVP First (User Stories 1 + 2)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational (CRITICAL - blocks all stories)
3. Complete Phase 3: User Story 1 (Schedule Policy)
4. Complete Phase 4: User Story 2 (Flip Policy)
5. **STOP and VALIDATE**: Both policies work independently
6. Deploy/demo as MVP

### Incremental Delivery

1. Setup + Foundational → Core framework ready
2. Add US1 (Schedule) → Test independently → Deliver
3. Add US2 (Flip) → Test independently → Deliver
4. Add US3 (Extensibility) → Verify with example → Deliver
5. Add US4 (Multiple) → Test combinations → Deliver
6. Polish → Final release

### Single Developer Strategy

Execute phases sequentially in priority order:
1. Phase 1 → Phase 2 → Phase 3 (US1) → Phase 4 (US2) → Phase 5 (US3) → Phase 6 (US4) → Phase 7

---

## Notes

- All provider classes MUST have @ForageBean annotation
- All config classes MUST follow Two-Class Pattern (ConfigEntries + Config)
- All ServiceLoader registrations MUST be in META-INF/services/
- Log state changes at INFO, config details at DEBUG
- Invalid config: log WARNING, use defaults
- Unknown policy: log WARNING, skip
- Commit after each task or logical group
- Run mvn spotless:apply before commits
