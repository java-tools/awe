
# Changelog for awe 4.3.3
*02/02/2021*

- Change notification system. [MR #274](https://gitlab.com/aweframework/awe/-/merge_requests/274) (Pablo Javier García Mora)
- Problem with /startup actuator endpoint. [MR #273](https://gitlab.com/aweframework/awe/-/merge_requests/273) (Pablo Vidal Otero)
- **[HAS IMPACTS]** Make database variable allowed to be configured in properties. [MR #272](https://gitlab.com/aweframework/awe/-/merge_requests/272) (Pablo Javier García Mora)

# Changelog for awe 4.3.2
*25/01/2021*

- Return AWException when service data retrieved by Rest or Microservice is ERROR or WARNING. [MR #271](https://gitlab.com/aweframework/awe/-/merge_requests/271) (Pablo Javier García Mora)

# Changelog for awe 4.3.1
*18/01/2021*

- Update awe to Spring Boot 2.4.2. [MR #270](https://gitlab.com/aweframework/awe/-/merge_requests/270) (Pablo Vidal Otero)
- Fix AWE email template. [MR #269](https://gitlab.com/aweframework/awe/-/merge_requests/269) (Pablo Javier García Mora)
- Scheduler executions are not stored on the same database as tasks. [MR #268](https://gitlab.com/aweframework/awe/-/merge_requests/268) (Pablo Javier García Mora)
- Error with sql logging. [MR #267](https://gitlab.com/aweframework/awe/-/merge_requests/267) (Pablo Vidal Otero)

# Changelog for awe 4.3.0
*14/12/2020*

- Generate AWE doc to version 4.3.0. [MR #266](https://gitlab.com/aweframework/awe/-/merge_requests/266) (Pablo Vidal Otero)
- Several downloads at once when a grid is filtered.. [MR #265](https://gitlab.com/aweframework/awe/-/merge_requests/265) (Pablo Javier García Mora)
- Icon in text-view component inside a grid dissapears when clicked. [MR #264](https://gitlab.com/aweframework/awe/-/merge_requests/264) (Pablo Javier García Mora)
- Add CLOB compatibility for queries/maintains. [MR #263](https://gitlab.com/aweframework/awe/-/merge_requests/263) (Pablo Javier García Mora)
- Add ROUND operation. [MR #262](https://gitlab.com/aweframework/awe/-/merge_requests/262) (Pablo Vidal Otero)
- Filter data without x coordinate when generating a chart in server side. [MR #261](https://gitlab.com/aweframework/awe/-/merge_requests/261) (Pablo Javier García Mora)
- **[HAS IMPACTS]** Update AWE to Spring Boot 2.4. [MR #260](https://gitlab.com/aweframework/awe/-/merge_requests/260) (Pablo Vidal Otero)
- Add a new interface in ChartService to allow sending a Chart object instead of a predefined chart inside a screen. [MR #259](https://gitlab.com/aweframework/awe/-/merge_requests/259) (Pablo Javier García Mora)
- Method writeText() of SeleniumUtilities clicks on the body after inserting the value in the criteria. [MR #258](https://gitlab.com/aweframework/awe/-/merge_requests/258) (Pablo Javier García Mora)

# Changelog for awe 4.2.9
*09/11/2020*

- Add common deployment scenarios to documentation. [MR #257](https://gitlab.com/aweframework/awe/-/merge_requests/257) (Pablo Vidal Otero)
- Activate csrf protection by default. [MR #256](https://gitlab.com/aweframework/awe/-/merge_requests/256) (Pablo Javier García Mora)
- Explain in detail security meassures that AWE has.. [MR #255](https://gitlab.com/aweframework/awe/-/merge_requests/255) (Pablo Vidal Otero)
- Update QueryDSL lib. [MR #254](https://gitlab.com/aweframework/awe/-/merge_requests/254) (Pablo Vidal Otero)
- Add a service to generate Highcharts charts from server. [MR #253](https://gitlab.com/aweframework/awe/-/merge_requests/253) (Pablo Javier García Mora)
- Add a new method isWebDate to check date format dd/MM/yyyy. [MR #252](https://gitlab.com/aweframework/awe/-/merge_requests/252) (Pablo Javier García Mora)
- Update spring-boot dependency to 3.3.5. [MR #251](https://gitlab.com/aweframework/awe/-/merge_requests/251) (Pablo Vidal Otero)
- Allow to put names of the alias in the fieds of the querys equal to the id with uppercase. [MR #250](https://gitlab.com/aweframework/awe/-/merge_requests/250) (Pablo Javier García Mora)
- Add repo AWE Training. [MR #249](https://gitlab.com/aweframework/awe/-/merge_requests/249) (Pablo Vidal Otero)
- Allow to retrieve screen data in an endpoint. [MR #248](https://gitlab.com/aweframework/awe/-/merge_requests/248) (Pablo Javier García Mora)
- Assure that sequence retrieving is thread safe. [MR #247](https://gitlab.com/aweframework/awe/-/merge_requests/247) (Pablo Javier García Mora)
- Move template files (stg) to front-end client to allow working with different front-end clients. [MR #246](https://gitlab.com/aweframework/awe/-/merge_requests/246) (Pablo Javier García Mora)
- Dependencies not working after a restore action. [MR #245](https://gitlab.com/aweframework/awe/-/merge_requests/245) (Pablo Javier García Mora)

# Changelog for awe 4.2.8
*23/09/2020*

- Set `none` as default cache as `ehcache` has JDK8 dependencies. [MR #242](https://gitlab.com/aweframework/awe/-/merge_requests/242) (Pablo Vidal Otero)
- Remove `@Component` from already initialized beans like WebSettings. [MR #241](https://gitlab.com/aweframework/awe/-/merge_requests/241) (Pablo Javier García Mora)
- Load-all grid doesn't draw the rows well when page is defined and it's not the first one. [MR #240](https://gitlab.com/aweframework/awe/-/merge_requests/240) (Pablo Javier García Mora)
- Update Spring Boot version to 2.2. [MR #239](https://gitlab.com/aweframework/awe/-/merge_requests/239) (Pablo Vidal Otero)
- Review documentation. [MR #238](https://gitlab.com/aweframework/awe/-/merge_requests/238) (Mario Bastardo)
- Bug in SeleniumUtilities.selectDate(). [MR #237](https://gitlab.com/aweframework/awe/-/merge_requests/237) (Pablo Javier García Mora)
- Bug in SeleniumUtilities.selectDate(). [MR #236](https://gitlab.com/aweframework/awe/-/merge_requests/236) (Pablo Javier García Mora)
- Update version in docusaurus framework. [MR #235](https://gitlab.com/aweframework/awe/-/merge_requests/235) (Pablo Vidal Otero)
- Yandex translation API v1 has been deprecated. [MR #234](https://gitlab.com/aweframework/awe/-/merge_requests/234) (Pablo Vidal Otero)
- Add filter version in search tool bar of website. [MR #233](https://gitlab.com/aweframework/awe/-/merge_requests/233) (Pablo Vidal Otero)
- Build a new doc website with docusaurus. [MR #232](https://gitlab.com/aweframework/awe/-/merge_requests/232) (Pablo Vidal Otero)
- Compile error with Jdk 11. [MR #231](https://gitlab.com/aweframework/awe/-/merge_requests/231) (Pablo Vidal Otero)

# Changelog for awe 4.2.7
*09/07/2020*

- Add a method to select all rows from a Grid in SeleniumUtilities. [MR #230](https://gitlab.com/aweframework/awe/-/merge_requests/230) (Pablo Vidal Otero)
- Add server attribute to microservices copying the functionality of rest connector. [MR #229](https://gitlab.com/aweframework/awe/-/merge_requests/229) (Pablo Vidal Otero)
- EncodeUtil is not mockeable. [MR #228](https://gitlab.com/aweframework/awe/-/merge_requests/228) (Pablo Javier García Mora)
- Exclude spring-boot-starter-logging. [MR #227](https://gitlab.com/aweframework/awe/-/merge_requests/227) (Pablo Vidal Otero)
- Placeholder into select doesn´t change language. [MR #226](https://gitlab.com/aweframework/awe/-/merge_requests/226) (Pablo Javier García Mora)
- Logo and background image paths doesn't work right when a context path is defined. [MR #225](https://gitlab.com/aweframework/awe/-/merge_requests/225) (Pablo Javier García Mora)

# Changelog for awe 4.2.6
*17/06/2020*

- Add a null check in the parameter name of getRequest().setParameter(....). [MR #224](https://gitlab.com/aweframework/awe/-/merge_requests/224) (Pablo Javier García Mora)
- Add PARAMETER_SELECT locale to awe-tools module. [MR #223](https://gitlab.com/aweframework/awe/-/merge_requests/223) (Pablo Javier García Mora)
- Print grid total row. [MR #222](https://gitlab.com/aweframework/awe/-/merge_requests/222) (Pablo Javier García Mora)

# Changelog for awe 4.2.5
*11/06/2020*

- `gotoScreen` doesn't wait for not disabled option, causing misclick on option. [MR #221](https://gitlab.com/aweframework/awe/-/merge_requests/221) (Pablo Javier García Mora)
- Error filling a radio criteria using the target attribute of the screen. [MR #220](https://gitlab.com/aweframework/awe/-/merge_requests/220) (Pablo Javier García Mora)
- Access to changelog MR is not being generated right. [MR #219](https://gitlab.com/aweframework/awe/-/merge_requests/219) (Pablo Javier García Mora)
- Show currentOption on screenshot name when failing in selenium tests. [MR #218](https://gitlab.com/aweframework/awe/-/merge_requests/218) (Pablo Javier García Mora)

# Changelog for AWE 4.2.4
*08/06/2020*

- Another cast exception in generic print for grids with numeric values. [MR #217](https://gitlab.com/aweframework/awe/merge_requests/217) (Pablo Javier García Mora)
- **[HAS IMPACTS]** Load all screens on startup. [MR #216](https://gitlab.com/aweframework/awe/merge_requests/216) (Pablo Javier García Mora)
- Fix redirect issue when user is logged. [MR #215](https://gitlab.com/aweframework/awe/merge_requests/215) (Pablo Javier García Mora)

# Changelog for AWE 4.2.3
*08/05/2020*

- Removing a row without parent in a treegrid causes error. [MR #211](https://gitlab.com/aweframework/awe/merge_requests/211) (Pablo Javier García Mora)
- Delete row doesn't allow a rowId even if it's defined. [MR #210](https://gitlab.com/aweframework/awe/merge_requests/210) (Pablo Javier García Mora)

# Changelog for AWE 4.2.2
*01/04/2020*

- Property `file.upload.max.size` is read in bytes instead of mega bytes. [MR #209](https://gitlab.com/aweframework/awe/merge_requests/209) (Pablo Javier García Mora)
- Allow to define * as column width. [MR #208](https://gitlab.com/aweframework/awe/merge_requests/208) (Pablo Javier García Mora)
- Application parameters not recoverd with getProperty. [MR #207](https://gitlab.com/aweframework/awe/merge_requests/207) (Pablo Javier García Mora)
- Cast exception in generic print for grids with numeric values. [MR #206](https://gitlab.com/aweframework/awe/merge_requests/206) (Pablo Javier García Mora)
- Make a Reports historic copy. [MR #205](https://gitlab.com/aweframework/awe/merge_requests/205) (Pablo Javier García Mora)
- Treegrid doesn't open expanded branches on defined initial level if loaded with `autoload` and `load-all`. [MR #204](https://gitlab.com/aweframework/awe/merge_requests/204) (Pablo Javier García Mora)
- Destroy help timeout when clicking a button. [MR #203](https://gitlab.com/aweframework/awe/merge_requests/203) (Pablo Javier García Mora)
- Improve `screen` client action to reload the page if it is the same. [MR #202](https://gitlab.com/aweframework/awe/merge_requests/202) (Pablo Javier García Mora)
- Add a new action to redirect a specific screen (broadcast to specific screen). [MR #201](https://gitlab.com/aweframework/awe/merge_requests/201) (Pablo Javier García Mora)
- **[HAS IMPACTS]** Notification module. [MR #200](https://gitlab.com/aweframework/awe/merge_requests/200) (Pablo Javier García Mora)
- Add function SQL RANK. [MR #199](https://gitlab.com/aweframework/awe/merge_requests/199) (Pablo Javier García Mora)
- Add a broadcasting method to send actions to a concrete screen. [MR #198](https://gitlab.com/aweframework/awe/merge_requests/198) (Pablo Javier García Mora)
- Allow to define component values for grids, wizards, tabs and charts when generating them with builder module. [MR #197](https://gitlab.com/aweframework/awe/merge_requests/197) (Pablo Javier García Mora)

# Changelog for AWE 4.2.1
*24/02/2020*

- Add a redirect action to go outside AWE. [MR #196](https://gitlab.com/aweframework/awe/merge_requests/196) (Pablo Javier García Mora)
- Allow sending beans as json values in REST requests. [MR #193](https://gitlab.com/aweframework/awe/merge_requests/193) (Pablo Javier García Mora)
- Docs - How to use a dependency just in the selected row. [MR #192](https://gitlab.com/aweframework/awe/merge_requests/192) (Mario Bastardo)
- When a date is pasted into a date criterion, it doesn't take the date value. [MR #191](https://gitlab.com/aweframework/awe/merge_requests/191) (Pablo Javier García Mora)
- Retrieve ServiceData even if rest response is null. [MR #190](https://gitlab.com/aweframework/awe/merge_requests/190) (Pablo Javier García Mora)
- Allow to define security for REST Servers. [MR #189](https://gitlab.com/aweframework/awe/merge_requests/189) (Pablo Javier García Mora)
- Fix menu visibility if started as minimized (still not working). [MR #188](https://gitlab.com/aweframework/awe/merge_requests/188) (Pablo Javier García Mora)
- Missing request header exception using view-pdf-file action. [MR #187](https://gitlab.com/aweframework/awe/merge_requests/187) (Pablo Javier García Mora)
- Fix menu visibility if started as minimized. [MR #186](https://gitlab.com/aweframework/awe/merge_requests/186) (Pablo Javier García Mora)
- .SELECTED parameters are not being sent when there is a dialog and a request for print data. [MR #185](https://gitlab.com/aweframework/awe/merge_requests/185) (Pablo Javier García Mora)
- Increase the version of frontend-maven-plugin to latest. [MR #184](https://gitlab.com/aweframework/awe/merge_requests/184) (Pablo Javier García Mora)
- Cast exception in ReportDesigner.getGridDataParameters when a Parameter is null. [MR #183](https://gitlab.com/aweframework/awe/merge_requests/183) (Pablo Javier García Mora)
- Panelable.getReportStructure tries to read dependencies as elements, and throws a null pointer. [MR #182](https://gitlab.com/aweframework/awe/merge_requests/182) (Pablo Javier García Mora)
- Some Scheduler locales are not defined in the right module. [MR #181](https://gitlab.com/aweframework/awe/merge_requests/181) (Pablo Javier García Mora)
- Define some performance tests over query computed components. [MR #180](https://gitlab.com/aweframework/awe/merge_requests/180) (Pablo Javier García Mora)
- Error printing with awe-boot docker image. [MR #179](https://gitlab.com/aweframework/awe/merge_requests/179) (Pablo Vidal Otero)
- Charts with  datetime type axis are not working correctly. [MR #178](https://gitlab.com/aweframework/awe/merge_requests/178) (Pablo Vidal Otero)
- Remove RepGenPdf screen and all its queries. [MR #177](https://gitlab.com/aweframework/awe/merge_requests/177) (Pablo Vidal Otero)
- Use sequence in `insert multiple=true`. [MR #176](https://gitlab.com/aweframework/awe/merge_requests/176) (Pablo Javier García Mora)
- **[HAS IMPACTS]** Fix cell-style documentation in grid definitions. [MR #175](https://gitlab.com/aweframework/awe/merge_requests/175) (Pablo Javier García Mora)
- getRequest() returns null when used in a Scheduler Task. [MR #174](https://gitlab.com/aweframework/awe/merge_requests/174) (Pablo Javier García Mora)
- Task list grid in scheduler-tasks screen is only showing 30 results. [MR #173](https://gitlab.com/aweframework/awe/merge_requests/173) (Pablo Javier García Mora)
- Exception in ServerDAO.findServer when there are no defined servers. [MR #172](https://gitlab.com/aweframework/awe/merge_requests/172) (Pablo Javier García Mora)
- getRequest().setParameter POJO object. [MR #171](https://gitlab.com/aweframework/awe/merge_requests/171) (Pablo Javier García Mora)
- getTaskExecutionList query adding fields in id instead of concat in SQS. [MR #170](https://gitlab.com/aweframework/awe/merge_requests/170) (Pablo Javier García Mora)
- Scheduler Task list only retrieves 30 results at application start. [MR #169](https://gitlab.com/aweframework/awe/merge_requests/169) (Pablo Javier García Mora)
- Server-print action error. [MR #168](https://gitlab.com/aweframework/awe/merge_requests/168) (Pablo Javier García Mora)
- There are some warning in compilation process. [MR #167](https://gitlab.com/aweframework/awe/merge_requests/167) (Pablo Vidal Otero)
- File Manager block. [MR #166](https://gitlab.com/aweframework/awe/merge_requests/166) (Pablo Vidal Otero)
- bug awe builder criteria are hidden by default. [MR #165](https://gitlab.com/aweframework/awe/merge_requests/165) (Pablo Javier García Mora)
- Invalid CronExpression when reading some scheduler tasks. [MR #164](https://gitlab.com/aweframework/awe/merge_requests/164) (Pablo Javier García Mora)
- Fix hash tests. [MR #163](https://gitlab.com/aweframework/awe/merge_requests/163) (Pablo Vidal Otero)
- Function ROW_NUMBER in over fields in SQL SERVER not working properly. [MR #162](https://gitlab.com/aweframework/awe/merge_requests/162) (Pablo Javier García Mora)
- Pick a new ADE version. [MR #161](https://gitlab.com/aweframework/awe/merge_requests/161) (Pablo Javier García Mora)
- DataSource Map doesn't initialize when FlyWay is not active. [MR #160](https://gitlab.com/aweframework/awe/merge_requests/160) (Pablo Vidal Otero)

# Changelog for AWE 4.2.0
*20/12/2019*

- Error with ArrayNode and TextNode from SEQUENCE. [MR #159](https://gitlab.com/aweframework/awe/merge_requests/159) (Pablo Javier García Mora)
- Fix selenium tests issue on chrome when deleting an email server. [MR #158](https://gitlab.com/aweframework/awe/merge_requests/158) (Pablo Javier García Mora)
- TextCriteriaBuilder doesn't have all required methods. [MR #157](https://gitlab.com/aweframework/awe/merge_requests/157) (Pablo Javier García Mora)
- Add missing dependency action `change-language` in Actions. [MR #156](https://gitlab.com/aweframework/awe/merge_requests/156) (Pablo Javier García Mora)
- Add a parameter to avoid AWE DataList post-processing when returns a DataList. [MR #155](https://gitlab.com/aweframework/awe/merge_requests/155) (Pablo Vidal Otero)
- **[HAS IMPACTS]** Scheduler module. [MR #123](https://gitlab.com/aweframework/awe/merge_requests/123) (Pablo Javier García Mora)

# Changelog for AWE 4.1.7
*05/12/2019*

- Add `not in` in dependencies conditions. [MR #153](https://gitlab.com/aweframework/awe/merge_requests/153) (Pablo Javier García Mora)
- Added QueryData to future query management. [MR #152](https://gitlab.com/aweframework/awe/merge_requests/152) (Pablo Javier García Mora)
- Tooltip timeout validation zero meaning. [MR #151](https://gitlab.com/aweframework/awe/merge_requests/151) (Pablo Javier García Mora)
- `show-column` and `hide-column` dependencies are not working. [MR #149](https://gitlab.com/aweframework/awe/merge_requests/149) (Pablo Javier García Mora)
- `over` tag should be allowed inside `operand` tag. [MR #148](https://gitlab.com/aweframework/awe/merge_requests/148) (Pablo Javier García Mora)
- Computeds show nulls when they shouldn't. [MR #147](https://gitlab.com/aweframework/awe/merge_requests/147) (Pablo Javier García Mora)
- Dependencies that check the `visible` attribute of a criteria throw a js error. [MR #146](https://gitlab.com/aweframework/awe/merge_requests/146) (Pablo Javier García Mora)
- **[HAS IMPACTS]** Overwrite properties system don´t work properly. [MR #145](https://gitlab.com/aweframework/awe/merge_requests/145) (Pablo Vidal Otero)
- initDatasourceConnections throws NoSuchMethodException at window load. [MR #144](https://gitlab.com/aweframework/awe/merge_requests/144) (Pablo Vidal Otero)
- APPLICATION START SERVICES throw exceptions when using datasources. [MR #142](https://gitlab.com/aweframework/awe/merge_requests/142) (Pablo Vidal Otero)
- Sort DataList with DataListUtil allowing to specify if nulls should be listed at first or at last. [MR #141](https://gitlab.com/aweframework/awe/merge_requests/141) (Pablo Vidal Otero)
- Allow to sort by DECIMAL type CellDatas comparing them as numbers, not as strings.. [MR #140](https://gitlab.com/aweframework/awe/merge_requests/140) (mbastardo)
- Problem with DIFF functions in ORACLE. [MR #139](https://gitlab.com/aweframework/awe/merge_requests/139) (Pablo Vidal Otero)

# Changelog for AWE 4.1.6
*10/11/2019*

- Retrieving session parameters with type DATE returns a null value. [MR #138](https://gitlab.com/aweframework/awe/merge_requests/138) (Pablo Javier García Mora)
- Error retrieving `ServiceData` messages. [MR #136](https://gitlab.com/aweframework/awe/merge_requests/136) (Pablo Javier García Mora)
- ABS function in query fields not working properly. [MR #135](https://gitlab.com/aweframework/awe/merge_requests/135) (Pablo Javier García Mora)
- Tree is not working when data is not sorted by id. [MR #134](https://gitlab.com/aweframework/awe/merge_requests/134) (Pablo Javier García Mora)
- **[HAS IMPACTS]** Possibility to put tags: case, field, constant and operation in the left-operand and right-operand tags. [MR #133](https://gitlab.com/aweframework/awe/merge_requests/133) (Pablo Javier García Mora)
- Update documentation: show actions stack in browser. [MR #132](https://gitlab.com/aweframework/awe/merge_requests/132) (Pablo Javier García Mora)
- Update-model server action shows an error message and cancels the action when service fails. [MR #131](https://gitlab.com/aweframework/awe/merge_requests/131) (Pablo Javier García Mora)
- Dependency-elements are treated as components by getElementsById(). [MR #130](https://gitlab.com/aweframework/awe/merge_requests/130) (Pablo Javier García Mora)
- Cast into case. [MR #129](https://gitlab.com/aweframework/awe/merge_requests/129) (Pablo Javier García Mora)
- Bug in addNoPrint() method from DataListBuilder. [MR #128](https://gitlab.com/aweframework/awe/merge_requests/128) (Pablo Javier García Mora)
- nullValue attribute in computeds should also check for empty values. [MR #127](https://gitlab.com/aweframework/awe/merge_requests/127) (Pablo Javier García Mora)
- ABS function in query fields not working properly. [MR #126](https://gitlab.com/aweframework/awe/merge_requests/126) (Pablo Javier García Mora)
- Component numeric not storing values on Internet Explorer. [MR #125](https://gitlab.com/aweframework/awe/merge_requests/125) (Pablo Javier García Mora)
- GridEvents.sendGridMessage not working properly. [MR #124](https://gitlab.com/aweframework/awe/merge_requests/124) (Pablo Vidal Otero)
- **[HAS IMPACTS]** Add sorting field to modules table [MR #100](https://gitlab.com/aweframework/awe/merge_requests/100) (Fernando Burillo)

# Changelog for AWE 4.1.5
*11/10/2019*

- Make audit with sequences simpler. [MR #122](https://gitlab.com/aweframework/awe/merge_requests/122) (Pablo Javier García Mora)
- Invalid property 'component' generating component map. [MR #121](https://gitlab.com/aweframework/awe/merge_requests/121) (Pablo Javier García Mora)
- Two-line Menu overlaps with the screen title/name. [MR #120](https://gitlab.com/aweframework/awe/merge_requests/120) (Pablo Javier García Mora)
- Function `FIRST_VALUE` and `LAST_VALUE` in fields not working and add function `TRIM` to queries. [MR #119](https://gitlab.com/aweframework/awe/merge_requests/119) (Pablo Javier García Mora)
- Test autoincrement fields in AWE. [MR #118](https://gitlab.com/aweframework/awe/merge_requests/118) (Pablo Javier García Mora)
- Audit in multiple maintain not working. [MR #117](https://gitlab.com/aweframework/awe/merge_requests/117) (Pablo Javier García Mora)
- Improve the validation documentation (add type of criteria and more description). [MR #116](https://gitlab.com/aweframework/awe/merge_requests/116) (Pablo Javier García Mora)
- Error generating component map in some screens caused by Invalid property.... [MR #115](https://gitlab.com/aweframework/awe/merge_requests/115) (Pablo Javier García Mora)
- Add a method to list all queries. [MR #114](https://gitlab.com/aweframework/awe/merge_requests/114) (Pablo Javier García Mora)
- AWE throws exception when launching a query without tables. [MR #113](https://gitlab.com/aweframework/awe/merge_requests/113) (Pablo Vidal Otero)
- **[HAS IMPACTS]** Component dialog is not storing data on its model. [MR #112](https://gitlab.com/aweframework/awe/merge_requests/112) (Pablo Javier García Mora)
- Filtered calendar doesn't show which months are allowed on month selection. [MR #111](https://gitlab.com/aweframework/awe/merge_requests/111) (Pablo Javier García Mora)
- Allow to define database used on logs. [MR #110](https://gitlab.com/aweframework/awe/merge_requests/110) (Pablo Javier García Mora)
- Allow to cast a number to varchar. [MR #109](https://gitlab.com/aweframework/awe/merge_requests/109) (Pablo Javier García Mora)
- Retrieve current datasource when calling `getDataSource(String alias)` with a null value. [MR #108](https://gitlab.com/aweframework/awe/merge_requests/108) (Pablo Javier García Mora)
- Launch asynchronously help template generation in application help. [MR #107](https://gitlab.com/aweframework/awe/merge_requests/107) (Pablo Javier García Mora)
- Show a dev-friendly message when filtering a grid without target-action and initial-load defined. [MR #106](https://gitlab.com/aweframework/awe/merge_requests/106) (Pablo Vidal Otero)
- Launching a `fill` action over a `select` component with values doesn't reset the component first. [MR #105](https://gitlab.com/aweframework/awe/merge_requests/105) (Pablo Javier García Mora)
- Manage error message when the query/maintain is not defined. [MR #104](https://gitlab.com/aweframework/awe/merge_requests/104) (Pablo Vidal Otero)
- Add power value to operator attribute inside operation. [MR #103](https://gitlab.com/aweframework/awe/merge_requests/103) (Pablo Vidal Otero)

# Changelog for AWE 4.1.4
*16/08/2019*

- Suggest showing several same options due to not refreshing model. [MR #94](https://gitlab.com/aweframework/awe/merge_requests/94) (Pablo Javier García Mora)
- Add functions to retrieve parts from dates in SQL (`YEAR`, `MONTH`, `DAY`, `HOUR`, `MINUTE`, `SECOND`). [MR #93](https://gitlab.com/aweframework/awe/merge_requests/93) (Pablo Javier García Mora)
- Add a new field function: `CNT_DISTINCT`. [MR #92](https://gitlab.com/aweframework/awe/merge_requests/92) (Pablo Javier García Mora)
- Unique action not working correctly. [MR #91](https://gitlab.com/aweframework/awe/merge_requests/91) (Pablo Javier García Mora)
- Add a method to retrieve a sequence value once updated. [MR #90](https://gitlab.com/aweframework/awe/merge_requests/90) (Pablo Javier García Mora)

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
- **[HAS IMPACTS]** Change `static` field name to `constant`. [MR #58](https://gitlab.com/aweframework/awe/merge_requests/58) (Pablo Javier García Mora)
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
- **[HAS IMPACTS]** Improve SQL fields to allow operations (improve also where clauses). [MR #49](https://gitlab.com/aweframework/awe/merge_requests/49) (Pablo Javier García Mora)
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

