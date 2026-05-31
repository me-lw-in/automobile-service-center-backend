# Automated Testing Report

Date: 2026-05-25

Command run:

```
./mvnw.cmd -B clean test -DskipITs=true
```

## Summary

- Tests executed: 28
- Failures: 9
- Errors: 7

## Failures (brief)

- PartControllerCrudTest.testCreatePartWithNegativeStock — expected 400 but was 200 (validation not enforced)
- UserControllerCrudTest.testCreateUser — expected 201 but was 403 (authorization)
- UserControllerCrudTest.testCreateUserWithEmptyEmail — expected 400 but was 403 (authorization)
- UserControllerCrudTest.testDeleteUser — expected 204 but was 403 (authorization)
- UserControllerCrudTest.testGetAllUsers — expected 200 but was 403 (authorization)
- UserControllerCrudTest.testGetUserById — expected 200 but was 403 (authorization)
- UserControllerCrudTest.testUpdateUser — expected 200 but was 403 (authorization)
- VehicleControllerCrudTest.testCreateVehicle — expected 200 but was 403 (authorization)
- VehicleControllerCrudTest.testUpdateVehicle — expected 200 but was 400 (input validation or payload issue)

## Errors (brief)

- RoleControllerCrudTest.* (create/delete/get/update): DataIntegrityViolationException — cannot delete role because `users` referenced `roles`.
- VehicleControllerCrudTest.* (delete/get): DataIntegrityViolationException — cannot delete vehicle because `job_cards` reference `vehicles`.
- Model tests earlier also surfaced FK cascade ordering issues (fixed by cleanup ordering adjustments).

## Root causes

- Test DB cleanup ordering: tests attempted to delete parent entities while child rows still existed. (bills → job_cards → vehicles; users → roles)
- Authorization mismatches: tests used roles that do not match `SecurityConfig` (controller endpoints require `SUPER_ADMIN` for many user/role/vehicle operations).
- Validation not enforced at controller level for `Part` creation/update (missing `@Valid` on request bodies).

## Actions taken so far

- Added `@Valid` to `PartController` request bodies.
- Updated test cleanup ordering in `ModelControllerCrudTest` to delete `bills`, then `job_cards`, then `vehicles`, then `models`.
- Updated `RoleControllerCrudTest` to delete `users` before deleting `roles`.
- Updated `UserControllerCrudTest` and `VehicleControllerCrudTest` to use `@WithMockUser(roles = "SUPER_ADMIN")` where appropriate to satisfy security.
- Updated several test imports and minor syntax fixes.

## Recommendations / Next steps

1. Ensure all controller endpoints validate input with `@Valid` where DTOs have validation annotations.
2. Add explicit cleanup for dependent entities in tests (bills, job_cards, vehicles, users) to avoid FK violations.
3. Standardize test roles to match `SecurityConfig` (use `SUPER_ADMIN` for admin-level endpoints).
4. Re-run the full test suite and attach `target/surefire-reports` contents to the report for per-test XML details.

---

## Raw Maven test output (excerpt)

```
(Excerpt from mvn output)

Results:

Failures: 
  PartControllerCrudTest.testCreatePartWithNegativeStock:134 Status expected:<400> but was:<200>
  UserControllerCrudTest.testCreateUser:72 Status expected:<201> but was:<403>
  UserControllerCrudTest.testCreateUserWithEmptyEmail:198 Status expected:<400> but was:<403>
  UserControllerCrudTest.testDeleteUser:173 Status expected:<204> but was:<403>
  UserControllerCrudTest.testGetAllUsers:102 Status expected:<200> but was:<403>
  UserControllerCrudTest.testGetUserById:122 Status expected:<200> but was:<403>
  UserControllerCrudTest.testUpdateUser:153 Status expected:<200> but was:<403>
  VehicleControllerCrudTest.testCreateVehicle:97 Status expected:<200> but was:<403>
  VehicleControllerCrudTest.testUpdateVehicle:167 Status expected:<200> but was:<400>

Errors:
  RoleControllerCrudTest.testCreateRole:53 Servlet Request processing failed: org.springframework.dao.DataIntegrityViolationException: could not execute statement [Cannot delete or update a parent row: a foreign key constraint fails (`service_center`.`users`, CONSTRAINT `fk_users_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`))]
  ... (other FK-related stack traces)

Total: Tests run: 28, Failures: 9, Errors: 7
```

(If you want the full raw output appended, I can attach the complete `mvn` log or the `target/surefire-reports` XML files.)
