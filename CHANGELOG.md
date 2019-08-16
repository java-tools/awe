
# Changelog for AWE 4.1.3
*31/07/2019*

- Sorting for a different sort-field than the one in the column name doesn't work on a non load-all grid with component. [MR #89](https://gitlab.com/aweframework/awe/merge_requests/89) (Pablo Javier García Mora)
- Allow to configure xml parser allowed paths. [MR #88](https://gitlab.com/aweframework/awe/merge_requests/88) (Pablo Javier García Mora)
- Operands doesn't allow functions nor cast attributes. [MR #87](https://gitlab.com/aweframework/awe/merge_requests/87) (Pablo Javier García Mora)
- Fix XStream security. [MR #86](https://gitlab.com/aweframework/awe/merge_requests/86) (Pablo Javier García Mora)
- Allow to configure REST requests timeout. [MR #85](https://gitlab.com/aweframework/awe/merge_requests/85) (Pablo Javier García Mora)
- Add new client action builders on grid, such as `add-row` or `remove-row`. [MR #84](https://gitlab.com/aweframework/awe/merge_requests/84) (Pablo Javier García Mora)
- Allow to cast fields on SQL queries. [MR #83](https://gitlab.com/aweframework/awe/merge_requests/83) (Pablo Javier García Mora)
- SQL - Add `COALESCE` operation. [MR #82](https://gitlab.com/aweframework/awe/merge_requests/82) (Pablo Javier García Mora)
- Add `exists` and `not exists` conditions to query filters and add `ABS` function. [MR #81](https://gitlab.com/aweframework/awe/merge_requests/81) (Pablo Javier García Mora)

# Changelog for AWE 4.1.2
*09/07/2019*

- Error in editable grids when you modify a cell with empty value.. [MR #80](https://gitlab.com/aweframework/awe/merge_requests/80) (Pablo Vidal Otero)
- Upgrade spring boot version to last 1.x. [MR #79](https://gitlab.com/aweframework/awe/merge_requests/79) (Pablo Javier García Mora)
- Fix some sonar issues. [MR #78](https://gitlab.com/aweframework/awe/merge_requests/78) (Pablo Javier García Mora)
- Remove actuator libraries from AWE starters. [MR #77](https://gitlab.com/aweframework/awe/merge_requests/77) (Pablo Javier García Mora)

# Changelog for AWE 4.1.1
*26/06/2019*

- Add more ClientActionBuilders. [MR #76](https://gitlab.com/aweframework/awe/merge_requests/76) (Pablo Javier García Mora)
- Allow to use spring-session or not. Document it. [MR #75](https://gitlab.com/aweframework/awe/merge_requests/75) (Pablo Javier García Mora)
- Fix application cookie issue. [MR #74](https://gitlab.com/aweframework/awe/merge_requests/74) (Pablo Javier García Mora)
- Maintain with audit table. [MR #73](https://gitlab.com/aweframework/awe/merge_requests/73) (Pablo Javier García Mora)
- Add `first-step`, `last-step` and `nth-step` actions to `wizard` component. [MR #72](https://gitlab.com/aweframework/awe/merge_requests/72) (Pablo Javier García Mora)
- Generate a global cookie for clustered environments. [MR #71](https://gitlab.com/aweframework/awe/merge_requests/71) (Pablo Javier García Mora)
- Add karma tests for grid services. [MR #70](https://gitlab.com/aweframework/awe/merge_requests/70) (Pablo Javier García Mora)
- Define session cookie even if user is not logged in. [MR #69](https://gitlab.com/aweframework/awe/merge_requests/69) (Pablo Javier García Mora)

# Changelog for AWE 4.1.0
*12/06/2019*

- Allow to configure the session cookie. [MR #68](https://gitlab.com/aweframework/awe/merge_requests/68) (Pablo Javier García Mora)
- Add new specific criteria builders for each criteria type, such as `TextBuilder` or `SuggestBuilder`. [MR #67](https://gitlab.com/aweframework/awe/merge_requests/67) (Pablo Javier García Mora)
- Allow subqueries on a maintain process. [MR #66](https://gitlab.com/aweframework/awe/merge_requests/66) (Pablo Javier García Mora)
- Add client actions utilities. [MR #65](https://gitlab.com/aweframework/awe/merge_requests/65) (Pablo Javier García Mora)
- Allow sending object beans as parameters to Java Services. [MR #64](https://gitlab.com/aweframework/awe/merge_requests/64) (Pablo Javier García Mora)
- Add a contains in conditions for dependencies.. [MR #63](https://gitlab.com/aweframework/awe/merge_requests/63) (Pablo Javier García Mora)
- Add a method to DataListUtil to convert rows from `List<Map<String, CellData>>` into `List<Bean>`. [MR #62](https://gitlab.com/aweframework/awe/merge_requests/62) (Pablo Javier García Mora)
- Fix `select-all-rows` action on grid element. [MR #61](https://gitlab.com/aweframework/awe/merge_requests/61) (Pablo Javier García Mora)
- Improve pipeline process parallelling tasks. [MR #60](https://gitlab.com/aweframework/awe/merge_requests/60) (Pablo Javier García Mora)
- Add windows functions in SQL AWE 4.0. [MR #59](https://gitlab.com/aweframework/awe/merge_requests/59) (Pablo Javier García Mora)
- Change `static` field name to `constant`. [MR #58](https://gitlab.com/aweframework/awe/merge_requests/58) (Pablo Javier García Mora)
- Row Number. [MR #57](https://gitlab.com/aweframework/awe/merge_requests/57) (Pablo Javier García Mora)
- Inherit all Spring boot tests from one class with `@SpringBootTests` to improve test speed. [MR #56](https://gitlab.com/aweframework/awe/merge_requests/56) (Pablo Javier García Mora)
- The replace-columns client action is not working correctly. [MR #55](https://gitlab.com/aweframework/awe/merge_requests/55) (Pablo Javier García Mora)
- Add a nullif operation to SQL engine. [MR #54](https://gitlab.com/aweframework/awe/merge_requests/54) (Pablo Javier García Mora)

# Changelog for AWE 4.0.8
*22/05/2019*

- Add an attribute to services which allows to call beans from its qualifier name. [MR #53](https://gitlab.com/aweframework/awe/merge_requests/53) (Pablo Vidal Otero)
- Add spring boot dev tools to AWE. [MR #52](https://gitlab.com/aweframework/awe/merge_requests/52) (Pablo Vidal Otero)
- Remove limit to suggest and select components queries if there's no `max` attribute defined. [MR #51](https://gitlab.com/aweframework/awe/merge_requests/51) (Pablo Javier García Mora)
- The replace-columns client action is not working correctly. [MR #50](https://gitlab.com/aweframework/awe/merge_requests/50) (Pablo Javier García Mora)
- Improve SQL fields to allow operations (improve also where clauses) [HAS IMPACTS]. [MR #49](https://gitlab.com/aweframework/awe/merge_requests/49) (Pablo Javier García Mora)
- Manage beans with lombok. [MR #48](https://gitlab.com/aweframework/awe/merge_requests/48) (Pablo Javier García Mora)

# Changelog for AWE 4.0.7
*29/04/2019*

- Limit must be greater than 0 trying to execute queryService.launchQuery(queryID, 1, 0) with managed-pagination=true. [MR #47](https://gitlab.com/aweframework/awe/merge_requests/47) (Pablo Javier García Mora)
- Fix deployment stages. [MR #46](https://gitlab.com/aweframework/awe/merge_requests/46) (Pablo Javier García Mora)

# Changelog for AWE 4.0.6
*26/04/2019*

- Organize project in submodules. [MR #45](https://gitlab.com/aweframework/awe/merge_requests/45) (Pablo Javier García Mora)
- When an numeric input parameter is empty, it is sending 0 instead of null. [MR #44](https://gitlab.com/aweframework/awe/merge_requests/44) (Pablo Javier García Mora)
- Error store user session with LDAP as auth mode. [MR #43](https://gitlab.com/aweframework/awe/merge_requests/43) (Pablo Vidal Otero)
- Pagination error on load all attribute. [MR #42](https://gitlab.com/aweframework/awe/merge_requests/42) (Pablo Javier García Mora)
- Join with query failed. [MR #41](https://gitlab.com/aweframework/awe/merge_requests/41) (Pablo Javier García Mora)
- Fix IN condition in queries and maintains. [MR #40](https://gitlab.com/aweframework/awe/merge_requests/40) (Pablo Javier García Mora)
- Fix sonar issues. [MR #39](https://gitlab.com/aweframework/awe/merge_requests/39) (Pablo Javier García Mora)
- Remove karma phase from general frontend build options. [MR #37](https://gitlab.com/aweframework/awe/merge_requests/37) (Pablo Javier García Mora)
- Launch a private query to retrieve user details instead of expecting to be authenticated. [MR #36](https://gitlab.com/aweframework/awe/merge_requests/36) (Pablo Javier García Mora)
- Remove old cookies processor for embedded tomcat. [MR #35](https://gitlab.com/aweframework/awe/merge_requests/35) (Pablo Javier García Mora)
- Fix awe-boot-archetype archetype-resources pom. [MR #34](https://gitlab.com/aweframework/awe/merge_requests/34) (Marcos)
- Pager-values grid attribute doesn't work. [MR #33](https://gitlab.com/aweframework/awe/merge_requests/33) (Pablo Javier García Mora)
- Improve jdbc authentication with a DAO. [MR #32](https://gitlab.com/aweframework/awe/merge_requests/32) (Pablo Javier García Mora)
- awe-starter-parent. [MR #31](https://gitlab.com/aweframework/awe/merge_requests/31) (Marcos)
- Clean image, icons, ... resources in build process. [MR #30](https://gitlab.com/aweframework/awe/merge_requests/30) (Pablo Javier García Mora)
- Logs by user doesn´t work. [MR #29](https://gitlab.com/aweframework/awe/merge_requests/29) (Pablo Javier García Mora)
- Pagination does not work if max attribute is not defined in the grid. [MR #28](https://gitlab.com/aweframework/awe/merge_requests/28) (Pablo Javier García Mora)
- Sometimes, clicking on the screen option you are currently, causes to reload the screen with some components not found. [MR #27](https://gitlab.com/aweframework/awe/merge_requests/27) (Pablo Javier García Mora)
- Fix Crypto tests. [MR #26](https://gitlab.com/aweframework/awe/merge_requests/26) (Pablo Javier García Mora)

# Changelog for AWE 4.0.5
*04/03/2019*

- Generate a CHANGELOG. [MR #8](https://gitlab.com/aweframework/awe/merge_requests/8) (Pablo Javier García Mora)
- Put SeleniumUtilities on awe-developer as part of development help tools. [MR #7](https://gitlab.com/aweframework/awe/merge_requests/7) (Pablo Javier García Mora)
- Trying to enter the application after being kicked by session expiration always returns a Bad credentials warning. [MR #6](https://gitlab.com/aweframework/awe/merge_requests/6) (Pablo Javier García Mora)
- Generate selenium print tests. [MR #5](https://gitlab.com/aweframework/awe/merge_requests/5) (Pablo Javier García Mora)
- Generate selenium integration tests. [MR #4](https://gitlab.com/aweframework/awe/merge_requests/4) (Pablo Javier García Mora)
- Generate selenium regression tests. [MR #3](https://gitlab.com/aweframework/awe/merge_requests/3) (Pablo Javier García Mora)
- When a session has expired the broadcasting channel still remains opened. [MR #2](https://gitlab.com/aweframework/awe/merge_requests/2) (Pablo Javier García Mora)
- Test a merge request. [MR #1](https://gitlab.com/aweframework/awe/merge_requests/1) (Pablo Vidal Otero)

