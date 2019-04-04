Almis Web Engine > **[Configuration guide](configuration-guide.md)**

---

# **AWE Modules**

## Table of Contents

* **[Introduction](#introduction)**
* **[Developer module](#developer-module)**
* **[Tools module](#tools-module)**

# **Introduction**

AWE has a number of modules or plugins available to integrate with different systems so that your application can use them to get new features. These modules are completely optional so that if the web application does not require them, needless to add to your `pom.xml`

# **Developer module**

This module includes some useful tools to help on the [application development](developer-tools.md).

To use this module, the following steps are necessary:

- Add **awe developer dependencies** to pom.xml descriptor.

```xml
<dependencies>
...
  <dependency>
    <groupId>com.almis.awe</groupId>
    <artifactId>awe-developer</artifactId>
    <version>${awe.version}</version>
  </dependency>
...
</dependencies>
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
    <artifactId>awe-tools</artifactId>
    <version>${awe.version}</version>
  </dependency>
...
</dependencies>
```
