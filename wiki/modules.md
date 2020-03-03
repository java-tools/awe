Almis Web Engine > **[Configuration guide](configuration-guide.md)**

---

# **AWE Modules**

## Table of Contents

* **[Introduction](#introduction)**
* **[Annotations module](#annotations-module)**
* **[Builder module](#builder-module)**
* **[Developer module](#developer-module)**
* **[Tools module](#tools-module)**
* **[Scheduler module](#scheduler-module)**
* **[Notifier module](#notifier-module)**

# **Introduction**

AWE has a number of modules or plugins available to integrate with different systems so that your application can use them to get new features. These modules are completely optional so that if the web application does not require them, needless to add to your `pom.xml`

# **Annotations module**

The annotations module includes a set of Spring-oriented annotations to make it easier to manage some AWE functionalities.

To include this module, simply add the dependency to the pom descriptor:

```xml
<dependencies>
...
  <dependency>
    <groupId>com.almis.awe</groupId>
    <artifactId>awe-annotations-spring-boot-starter</artifactId>
  </dependency>
...
</dependencies>
```

# **Builder module**

This module offers some builders to generate dynamically screen components and client actions. 

To include this module, simply add the dependency to the pom descriptor:

```xml
<dependencies>
...
  <dependency>
    <groupId>com.almis.awe</groupId>
    <artifactId>awe-builder-spring-boot-starter</artifactId>
  </dependency>
...
</dependencies>
```

# **Developer module**

This module includes some useful tools to help on the [application development](developer-tools.md).

To use this module, the following steps are necessary:

- Add **awe developer dependencies** to pom.xml descriptor.

```xml
<dependencies>
...
  <dependency>
    <groupId>com.almis.awe</groupId>
    <artifactId>awe-developer-spring-boot-starter</artifactId>
  </dependency>
...
</dependencies>
```

- Add the developer module screens into your `private.xml` file:

```xml
<option name="developer" label="MENU_DEVELOPER" icon="paint-brush">
  <option name="path-manager" label="MENU_PATH" screen="path-manager" icon="terminal"/>
  <option name="local-manager" label="MENU_LANGUAGES" screen="local-manager" icon="language"/>
</option>
```

- Add the developer module to your `config/library.properties` file:

```properties
# Utilities list
modules.list=...,developer,...,awe
module.awe=awe
...
module.developer=awe-developer
...
```

# **Tools module**

This module has some management tools very useful to manage server resources: [File Manager](filemanager.md) and [SQL extractor](sql-extractor-engine.md).

To use this module, the following steps are necessary:

- Add **awe tools dependencies** to pom.xml descriptor.

```xml
<dependencies>
...
  <dependency>
    <groupId>com.almis.awe</groupId>
    <artifactId>awe-tools-spring-boot-starter</artifactId>
  </dependency>
...
</dependencies>
```

- Add the tools module screens into your `private.xml` file:

```xml
<option name="sql-extractor" label="MENU_SQL_EXTRACTOR" screen="sqlExtractor" icon="database"/>
<option name="file-manager" label="MENU_TEST_FILEMANAGER" screen="filemanager-test" icon="folder"/>
```

- Add the tools module to your `config/library.properties` file:

```properties
# Utilities list
modules.list=...,tools,...,awe
module.awe=awe
...
module.tools=awe-tools
...
```

# **Scheduler module**

The scheduler module adds a powerful scheduling tool to your application

To use this module, the following steps are necessary:

- Add **awe scheduler dependencies** to pom.xml descriptor.

```xml
<dependencies>
...
  <dependency>
    <groupId>com.almis.awe</groupId>
    <artifactId>awe-scheduler-spring-boot-starter</artifactId>
  </dependency>
...
</dependencies>
```

- Add the scheduler screens into your `private.xml` file:

```xml
<option name="scheduler" label="MENU_SCHEDULER" icon="clock-o">
  <option name="scheduler-management" label="MENU_SCHEDULER_MANAGEMENT" screen="scheduler-management" icon="cogs"/>
  <option name="scheduler-tasks" label="MENU_SCHEDULER_TASKS" screen="scheduler-tasks" icon="tasks">
    <option name="new-scheduler-task" screen="new-scheduler-task" invisible="true" />
    <option name="update-scheduler-task" screen="update-scheduler-task" invisible="true" />
  </option>
  <option name="scheduler-servers" label="MENU_SCHEDULER_SERVERS" screen="scheduler-server" icon="server">
    <option name="new-scheduler-server" screen="new-scheduler-server" invisible="true" />
    <option name="update-scheduler-server" screen="update-scheduler-server" invisible="true" />
  </option>
  <option name="scheduler-calendars" label="MENU_SCHEDULER_CALENDARS" screen="scheduler-calendars" icon="calendar">
    <option name="new-scheduler-calendar" screen="new-scheduler-calendar" invisible="true" />
    <option name="update-scheduler-calendar" screen="update-scheduler-calendar" invisible="true" />
  </option>
</option>
```

- Add the scheduler module to your `config/library.properties` file:

```properties
# Utilities list
modules.list=...,scheduler,...,awe
module.awe=awe
...
module.scheduler=awe-scheduler
...
```

- Finally, if you are using `flyway`, add the scheduler tables into the migration module:

```properties
awe.database.migration.modules=AWE,...,SCHEDULER,...
```

# **Notifier module**

The notifier module allows your users to subscribe to notifications in your application.

To use this module, the following steps are necessary:

- Add **awe notifier dependencies** to pom.xml descriptor.

```xml
<dependencies>
...
  <dependency>
    <groupId>com.almis.awe</groupId>
    <artifactId>awe-notifier-spring-boot-starter</artifactId>
  </dependency>
...
</dependencies>
```

- Add the notifier screens into your `private.xml` file:

```xml
<option name="user-settings" screen="user-settings" invisible="true"/>
<option name="notifier" label="MENU_NOTIFIER" icon="flash">
  <option name="subscriptions" screen="subscriptions" label="MENU_NOTIFIER_SUBSCRIPTIONS" icon="ticket">
    <option name="new-subscription" screen="new-subscription" invisible="true" />
    <option name="update-subscription" screen="update-subscription" invisible="true" />
  </option>
  <option name="notifications" screen="notifications" label="MENU_NOTIFIER_NOTIFICATIONS" icon="bell" />
</option>
```

- Include the notifier tool into the `home_navbar.xml` file:

```xml
<tag type="ul" style="nav navbar-nav pull-right right-navbar-nav">
  ...
  <include target-screen="notification-panel" target-source="notification-panel"/>
  ...
</tag>
...
<info id="ButUsrAct" icon="user" initial-load="query" target-action="connectedUser">
  ...
  <include target-screen="notification-panel" target-source="user-settings"/>
  ...
</info>
```

- Add the notifier module to your `config/library.properties` file:

```properties
# Utilities list
modules.list=...,notifier,...,awe
module.awe=awe
...
module.notifier=awe-notifier
...
```

- Finally, if you are using `flyway`, add the notifier tables into the migration module:

```properties
awe.database.migration.modules=AWE,...,NOTIFIER,...
```