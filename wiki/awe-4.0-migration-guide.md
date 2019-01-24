### Almis Web Engine - **[Home](../readme.md)**

---

# **AWE 4.0 Migration guide**

##  **Application Structure**

* Change project structure to a Spring Architecture:

> * **[application-name]** -> Project files (pom.xml, package.json...)
>   * src
>      * main
>          * **java** -> ApplicationBoot + Java classes
>          * **resources** -> Properties
>              * application/[application-name] -> XML files
>              * config -> AWE Properties overwritten
>              * js -> Javascript files
>              * css -> CSS files
>              * less -> LESS files
>              * schemas -> XSD Schemas
>              * static -> Images/Fonts
>              * webpack -> Webpack configuration
>              * sql -> SQL Initialization files
>      * test
>          * *java* -> JUnit Tests
>          * *resources* -> Test properties
>          * selenium -> Selenium suites

##  **Maven**

* Change POM file:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <artifactId>[project-name]</artifactId>
  <groupId>[project-group]</groupId>
  <version>[project-version]</version>

  <name>[Project name]</name>
  <description>[Project description]</description>

  <properties>
    <awe.version>[awe-version]</awe.version>
    <spring-boot.version>1.5.10.RELEASE</spring-boot.version>
    <application.acronym>[project-acronym]</application.acronym>
    <start-class>[project-group].AppBootApplication</start-class>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <project.build.frontend>${project.build.directory}/classes/static/</project.build.frontend>
    <java.version>1.8</java.version>
    <awe.schemas>../../../static/schemas/awe</awe.schemas>
  </properties>

  <dependencyManagement>
    <dependencies>
      <!-- AWE -->
      <dependency>
        <groupId>com.almis.awe</groupId>
        <artifactId>awe-dependencies</artifactId>
        <version>${awe.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>

  <dependencies>

    <!-- AWE -->
    <dependency>
      <groupId>com.almis.awe</groupId>
      <artifactId>awe-spring-boot-starter</artifactId>
    </dependency>

    <dependency>
      <groupId>com.almis.awe</groupId>
      <artifactId>awe-client-angular</artifactId>
    </dependency>

    <!-- JDBC Drivers (ADD ONLY WHAT YOU NEED) -->
    
    <!-- ORACLE -->
    <dependency>
      <groupId>com.oracle</groupId>
      <artifactId>ojdbc6</artifactId>
      <version>11.2.0.3</version>
      <scope>runtime</scope>
    </dependency>

    <!-- SQL SERVER -->
    <dependency>
      <groupId>com.microsoft.sqlserver</groupId>
      <artifactId>sqljdbc4</artifactId>
      <version>4.0.0</version>
      <scope>runtime</scope>
    </dependency>

    <!-- HSQL -->
    <dependency>
      <groupId>org.hsqldb</groupId>
      <artifactId>hsqldb</artifactId>
      <version>2.3.3</version>
      <scope>runtime</scope>
    </dependency>

  </dependencies>

  <build>
    <finalName>[project-name]</finalName>
    <resources>
      <resource>
        <directory>src/main/resources</directory>
        <filtering>true</filtering>
      </resource>
    </resources>
    <plugins>
    
      <!-- Copy static files -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-dependency-plugin</artifactId>
        <executions>
          <execution>
            <phase>generate-resources</phase>
            <id>unpack awe-generic-screens</id>
            <goals>
              <goal>unpack-dependencies</goal>
            </goals>
            <configuration>
              <includeGroupIds>com.almis.awe</includeGroupIds>
              <includeArtifactIds>awe-generic-screens</includeArtifactIds>
              <includes>schemas/**,docs/**</includes>
              <outputDirectory>${project.build.frontend}</outputDirectory>
            </configuration>
          </execution>
          <execution>
            <phase>generate-resources</phase>
            <id>unpack awe-client-angular</id>
            <goals>
              <goal>unpack-dependencies</goal>
            </goals>
            <configuration>
              <includeGroupIds>com.almis.awe</includeGroupIds>
              <includeArtifactIds>awe-client-angular</includeArtifactIds>
              <includes>images/**,fonts/**,js/**,css/**,less/**</includes>
              <outputDirectory>${project.build.frontend}</outputDirectory>
            </configuration>
          </execution>
        </executions>
      </plugin>
      
      <!-- Copy images -->
      <plugin>
        <artifactId>maven-resources-plugin</artifactId>
        <version>3.0.2</version>
        <executions>
          <execution>
            <id>copy-images</id>
            <phase>prepare-package</phase>
            <goals>
              <goal>copy-resources</goal>
            </goals>
            <configuration>
              <outputDirectory>${project.build.directory}/classes/static/images/</outputDirectory>
              <resources>
                <resource>
                  <directory>${project.basedir}/src/main/resources/images/</directory>
                </resource>
              </resources>
            </configuration>
          </execution>
        </executions>
      </plugin>
      
      <!-- Spring boot -->
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <version>${spring-boot.version}</version>
      </plugin>
      
      <!-- Frontend generation -->
      <plugin>
        <groupId>com.github.eirslett</groupId>
        <artifactId>frontend-maven-plugin</artifactId>
        <version>1.6</version>
        <executions>
          <execution>
            <id>install node and yarn</id>
            <goals>
              <goal>install-node-and-yarn</goal>
            </goals>
            <configuration>
              <nodeVersion>v6.9.1</nodeVersion>
              <yarnVersion>v1.6.0</yarnVersion>
            </configuration>
          </execution>
          <execution>
            <id>yarn install</id>
            <goals>
              <goal>yarn</goal>
            </goals>
            <configuration>
              <arguments>install</arguments>
            </configuration>
          </execution>
          <execution>
            <id>webpack</id>
            <goals>
              <goal>webpack</goal>
            </goals>
            <configuration>
              <arguments>--output-path "${project.build.frontend}"</arguments>
            </configuration>
          </execution>
        </executions>
      </plugin>

      <!-- Validate XML files -->
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>xml-maven-plugin</artifactId>
        <version>1.0.2</version>
        <executions>
          <execution>
            <goals>
              <goal>validate</goal>
            </goals>
          </execution>
        </executions>
        <configuration>
          <validationSets>
            <validationSet>
              <dir>${project.build.directory}/classes/application/${application.acronym}/</dir>
              <validating>true</validating>
            </validationSet>
          </validationSets>
        </configuration>
      </plugin>
      
      <!-- Build an executable JAR -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jar-plugin</artifactId>
        <version>3.0.2</version>
        <configuration>
          <archive>
            <manifest>
              <addClasspath>true</addClasspath>
              <classpathPrefix>lib/</classpathPrefix>
              <mainClass>${start-class}</mainClass>
            </manifest>
          </archive>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
```

##  **Javascript & CSS generation**

* Define a webpack file to generate custom javascript and css

##  **XML files**

### **Screens**

* Regular expressions

  * Dependency `source-type="action"` is not needed anymore. Just add your dependency actions and they will be launched on valid conditions. 
  * `source-type="none"` and `target-type="none"` attributes are default values, so you don't need to set them.
  * Remove `source-type="action"`, `source-type="none"` and `target-type="none"`:

```regexp
^(.*)source\-type\s*=\s*["']action["']\s*(\S+.*)$ => $1$2
^(.*)source\-type\s*=\s*["']none["']\s*(\S+.*)$ => $1$2
^(.*)target\-type\s*=\s*["']none["']\s*(\S+.*)$ => $1$2
```

* Direct replacement:

  * Fix dependency conditions:

```regexp
condition="lte" => condition="le"
condition="gte" => condition="ge"
```

  * Grid pagination to grid managed pagination:

```regexp
pagination="true" => managed-pagination="true"
```

###  **Query & Maintain**

* Regular expressions

  * Fix query and maintain filters:

```regexp
^(\s*<filter.*\s+)value(.*/>.*)$ => $1left-variable$2 -> Replace by left-variable and add the variable name
^(\s*<filter.*\s+)variable(.*/>.*)$ => $1right-variable$2
^(\s*<filter.*\s+)counterfield(.*/>.*)$ => $1right-field$2
^(\s*<filter.*\s+)countertable(.*/>.*)$ => $1right-table$2
^(\s*<filter.*\s+)field(.*/>.*)$ => $1left-field$2
^(\s*<filter.*\s+)table(.*/>.*)$ => $1left-table$2
```

* Direct replacement:

  * Fix query conditions:

```regexp
condition="=" => condition="eq"
condition="!=" => condition="ne"
condition="LIKE" => condition="like"
condition="NOT LIKE" => condition="not like"
condition="&gt;" => condition="gt"
condition="&gt;=" => condition="ge"
condition="&lt;" => condition="lt"
condition="&lt;=" => condition="le"
condition="IS NULL" => condition="is null"
condition="IS NOT NULL" => condition="is not null"
condition="IN" => condition="in"
condition="NOT IN" => condition="not in"
```

* Variable identifiers can't be used in field aliases.
* CASE and CONCAT definitions

###  **Services**

* Web services calls have been changed to microservices calls:

Examples:
```xml
  <service id="simpleGETMicroservice">
    <microservice name="alu-microservice" method="GET" endpoint="/invoke" />
  </service>
  
  <service id="simpleGETMicroservice2">
    <microservice name="alu-microservice2" method="GET" endpoint="/invoke" />
  </service>
  
  <service id="simpleGETMicroserviceWithWrapper">
    <microservice name="alu-microservice" method="GET" endpoint="/invoke" wrapper="com.almis.awe.test.service.dto.ServiceDataWrapper" />
  </service>
  
  <service id="simpleGETMicroserviceWithParameter">
    <microservice name="alu-microservice" method="GET" endpoint="/invoke">
      <service-parameter name="param1" type="STRING"/>
    </microservice>
  </service>
  
  <service id="simpleGETMicroserviceWithWildcard">
    <microservice name="alu-microservice" method="GET" endpoint="/invoke/{param1}">
      <service-parameter name="param1" type="STRING"/>
    </microservice>
  </service>
  
  <service id="simpleGETMicroserviceWithWildcardAndParameter">
    <microservice name="alu-microservice" method="GET" endpoint="/invoke/{param1}">
      <service-parameter name="param1" type="STRING"/>
      <service-parameter name="param2" type="STRING"/>
    </microservice>
  </service>
  
  <service id="simplePOSTMicroserviceWithParameters">
    <microservice name="alu-microservice" method="POST" endpoint="/invoke">
      <service-parameter name="param1" type="STRING"/>
      <service-parameter name="param2" type="STRING"/>
    </microservice>
  </service>
  
  <service id="simplePUTMicroserviceWithParameters">
    <microservice name="alu-microservice" method="PUT" endpoint="/invoke">
      <service-parameter name="param1" type="STRING"/>
      <service-parameter name="param2" type="STRING"/>
    </microservice>
  </service>
  
  <service id="simpleDELETEMicroserviceWithWildcard">
    <microservice name="alu-microservice" method="DELETE" endpoint="/invoke/{param1}">
      <service-parameter name="param1" type="STRING"/>
    </microservice>
  </service>
```

###  **Locales**

* **Rename** Local-XX.xml files to Locale-XX.xml
* Direct replacement:

```regexp
<locals => <locales
</locals> => </locales>
<local => <locale
</local> => </locale>
```

##  **Properties**

Encode again the properties encoded with `ENC(xxxx)` at the Encrypt util screen.

##  **Java files**

### Package refactorization

* Direct replacement:
  * Packages
  
```regexp
com.almis.awe.core.services.data.global.XMLWrapper => com.almis.awe.model.entities.XMLWrapper
com.almis.awe.core.services.data.global.XMLElement => com.almis.awe.model.entities.XMLWrapper
com.almis.awe.core.services.data.service.ServiceData => com.almis.awe.model.dto.ServiceData
com.almis.awe.core.services.controller.DataController => com.almis.awe.service.QueryService
com.almis.awe.core.services.controller.MaintainController => com.almis.awe.service.MaintainService
com.almis.awe.core.util.DateUtil => com.almis.awe.model.util.data.DateUtil
com.almis.awe.core.beans.ComponentAddress => com.almis.awe.model.entities.actions.ComponentAddress
com.almis.awe.core.services.data.action.ClientAction => com.almis.awe.model.entities.actions.ClientAction
com.almis.awe.dto => com.almis.awe.model.dto
com.almis.awe.core.services.data.global => com.almis.awe.model.dto
com.almis.awe.type => com.almis.awe.model.type
com.almis.awe.core.services.data.query => com.almis.awe.model.entities.queries
com.almis.awe.core.services.data.maintain => com.almis.awe.model.entities.maintain
com.almis.awe.core.exception => com.almis.awe.exception
XMLElement => XMLWrapper
AWEConstants => AweConstants
AweConstants.PARAMETER_MAX => AweConstants.COMPONENT_MAX
```
    
* Logging
  1. Remove com.almis.awe.core.util.LogUtil
  2. Import `org.apache.logging.log4j.LogManager` and `org.apache.logging.log4j.Logger`
  3. Create a static logger field:
  
```java
  // Logger
  private static Logger logger = LogManager.getLogger(MyClass.class);
```
    
  4. Use static logger. For example:
  
```java
  logger.log(Level.INFO, "[{0}] No books defined for this treatment", new Object[]{treatment.getID()});
```

### AWE packages

* There are **two main packages** in **AWE 4.0**: `awe-spring-boot-starter` and `awe-model`. 
  * **awe-spring-boot-starter** is the **core package** of AWE. AWE based web applications must import this package.
     * To call AWE services, autowire services from `com.almis.awe.services` (No more Controller calls)
  * **awe-model** is the **interface package** of AWE. AWE related applications (communication modules, microservices, etc) can import this package to gain access to interface classes.

### Java services must be migrated to Spring architecture

* Remove all controllers if they don't do anything
* Class structure in AWE has been changed radically. Check your Java imports
* Move `manager` package to `service` package
* Rename all **Xxx**Manager.java classes to **Xxx**Service.java classes
* Add `@Service` annotation to **Xxx**Service.java classes
* Use Spring methodology (`@Autowired` constructors, `@Value` to retrieve properties, etc)
* Extend all **Xxx**Service classes from `ServiceConfig` if they are using:
  * `com.almis.awe.core.singleton.LocalSingleton` => Extend from `com.almis.awe.config.ServiceConfig` and call `getLocale` methods
  * `com.almis.awe.core.singleton.PropertySingleton` => Extend from `com.almis.awe.config.ServiceConfig` and call `getProperty` methods (And best of all use `@Value` instead of `getProperty` methods)
  * `com.almis.awe.core.services.controller.SessionController` => Extend from `com.almis.awe.config.ServiceConfig` and call `getSession`
  * `com.almis.awe.core.services.data.global.Context` => Extend from `com.almis.awe.config.ServiceConfig` and call `getRequest` to retrieve request parameters
  * Remove also ContextUtil.getContext() access
  * Use also `getRequest().getTargetAction()` to retrieve the action target called.
  * More information on [Locale retrieval](#locale-retrieval), [Property retrieval](#property-retrieval), [Session retrieval](#session-retrieval) and [Request retrieval](#request-retrieval).
* Adapt custom authentication if overwritten in application
* Use `QueryService` instead of `DataController`. All `launchQuery` methods now return `ServiceData` beans instead of `DataList`. 
  You can retrieve the `DataList` with `serviceData.getDataList()` method. 
* Use `MaintainService` instead of `MaintainController`.

### Locale retrieval

* Extending from `ServiceConfig` you get access to `getLocale` methods:

```java
  getLocale("ERROR_TITLE_LAUNCHING_MAINTAIN");
```
  
* You can pass variables to replace on locale simply by adding them as arguments:

```java
  getLocale("ERROR_TITLE_LAUNCHING_MAINTAIN", treatment.getID(), task.getID());
```
  
### Property retrieval

* Extending from `ServiceConfig` you get access to `getProperty` methods:

```java
  getProperty("var.trt.thd.sug.tim", 100);
```

* Anyway it's more legible to retrive properties the Spring way:

```java
  @Value("var.trt.thd.sug.tim:100")
  private Integer suggestTime;
```
  
### Session retrieval

* Extending from `ServiceConfig` you get access to `getSession` methods:

```java
  getSession().getParameter(AweConstants.SESSION_DATABASE);
```
  
### Request retrieval

* Extending from `ServiceConfig` you get access to `getRequest` methods instead of retrieving them from `Context`:

```java
  getRequest().getParameter(AweConstants.PARAMETER_MAX).textValue();
```

* You can also add some variables to the request:

```java
  getRequest().setParameter("someList", someList);
```

### Datalist type

* DataList `getRows` method has changed its' signature from `ArrayList<HashMap<String, CellData>>` to a more generic signature: `List<Map<String, CellData>>`.

### Beans

* Use copy constructor instead of Cloneable interface:

```java
public class MyClass implements Copyable {

  private String myProp1;
  private String myProp2;

  /**
   * Default constructor 
   */
  public MyClass() {}
  
  /**
   * Copy constructor 
   */
  public MyClass(MyClass other) {
    this.myProp1 = other.myProp1;
    this.myProp2 = other.myProp2;
  }
  
  /**
   * Copy method
   * @return Copy of this object
   */
  public MyClass copy() {
    return new MyClass(this);
  }
}
```

* Remove from beans all methods which uses any external class. 
  A bean only should have methods which interact over their own fields.


### `fill` client action now has only one parameter: `datalist`, which will contain the full DataList:

```java
serviceData.addClientAction(new ClientAction("fill")
   .setAddress(address)
   .addParameter("datalist", datalist)
   .setAsync(true));
```

### `FileData` bean has now a new implementation:

Use `FileUtil.fileDataToString` to generate a token:

```java
public String fileDataToString(FileData fileData)
```

Use `FileUtil.stringToFileData` to retrieve a FileData bean from a token:

```java
public FileData stringToFileData(String fileStringEncoded)
```

> **Note:** FileUtil is an autowireable @Component

##  **Web services** :arrow_right_hook: **Microservices**

* Adapt web service interface as microservice