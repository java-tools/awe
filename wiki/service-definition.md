### Almis Web Engine - **[Basic Development Guide](basic-developer-guide.md)**

---

# **Service definition**

## Table of Contents

* **[Introduction](#introduction)**
* **[Global service structure](#global-service-structure)**
  * **[Java services](#java-services)**
      * [How to set parameter values from Java](#how-to-set-parameter-values-from-java)
  * **[Microservices](#microservices)**
  * **[REST services](#rest-services)**

---

## **Introduction**

Service operations are valid to do specific treatments and calculations out of the AWE utilities.

Currently there are two types of services declared in the AWE Engine: **Java Services** and **Web Services**

> **Note:** All services are defined in the `Services.xml` file at **global folder**. View [project structure](basic-developer-guide.md#global-folder)  for more info.

## **Global service structure**

The xml structure of services is:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<services xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation = "../../sch/services.xsd">
  <service id="[Service Id]">
    <java classname="[Java class]" method="[Java method]">
      <service-parameter type="[Type]" name="[Parameter name]" />
      ... (More <service_parameter>)
    </java>
  </service>
  <service id="[Service Id]">
    <microservice name="[Microservice name]" method="[REST method]" endpoint="[Service endpoint]">
      <service-parameter type="[Type]" name="[Parameter name]" />
      ... (More <service_parameter>)
    </microservice>
  </service>
  <service id="[Service Id]">
    <rest method="[REST method]" endpoint="[Service endpoint]" wrapper="[REST service wrapper]">
      <service-parameter type="[Type]" name="[Parameter name]" />
      ... (More <service_parameter>)
    </rest>
  </service>  
  ... (More <service>)
</services>
```

For even more simplify the development of services, not all elements are required.


| Element     | Use      | Multiples instances    | Description                                        |
| ----------- | ---------|------------------------|----------------------------------------------------|
| services | **Required**| No | Root element of services xml file|
| [service](#service-element) | **Required**| Yes |It outlines the service. Also describes the **kind of service** (java service or web service)  |
| [java](#java-element) | **Optional**| No | Used to define java services  |
| [microservice](#microservice-element) | **Optional**| No | Used to define microservices  |
| [rest](#rest-service-element) | **Optional**| No | Used to define rest services  |
| [service-parameter](#service-parameter-element) | **Optional**| No | Used to pass parameters to service  |

### Service element

Service element has the following attributes:

| Attribute    | Use          | Type      |  Description                                       |   Values                                |
| ------------ | -------------|-----------|----------------------------------------------------|-----------------------------------------|
| id           | **Required** | String    | Name of service                                    |  **Note:** The id must be unique        |
| launch-phase | Optional     | String    | Launch the service in some application points      | `APPLICATION_START`, `APPLICATION_END`, 
`CLIENT_START`, `CLIENT_END` |

### Java element

Java element has the following attributes:

| Attribute   | Use          | Type      |  Description                        |   Values                                           |
| ----------- | -------------|-----------|-------------------------------------|----------------------------------------------------|
| classname   | **Required** | String    | Class name of java service          |  Ex.: `classname="com.almis.awe.core.services.controller.AccessController"`
| method      | **Required** | String    | Method name of class to be executed | Ex.: `method="login"` |

### Microservice element

Microservice element has the following attributes:

| Attribute    | Use          | Type      |  Description                         |   Values                                           |
| ------------ | -------------|-----------|------------------------------------- |----------------------------------------------------|
| name         | **Required** | String    | Name of web service                  | **Note:** Must be unique                           |
| method       | **Required** | String    | REST method.                         | **GET**: Send the parameters as part of the endpoint
 - **POST**: Send the parameters in the request body |
| endpoint     | **Required** | String    | Path to REST call                    | Ej: /data/ServiceData or /maintain/ServiceMaintain |
| wrapper      | Optional     | String    | Classname to wrap REST call response | Ej: com.almis.awe.test.bean.Postman                     |
| content-type | Optional     | String    | Way to send the parameters           | `URLENCODED` (default), `JSON`                     |
| cacheable    | Optional     | Boolean   | Used to set service as cacheable     | Default value is `false`                           |

### REST service element

REST service element has the following attributes:

| Attribute    | Use          | Type      |  Description                         |   Values                                             |
| ------------ | ------------ |-----------|--------------------------------------|----------------------------------------------------- |
| server       | Optional     | String    | REST server property                 | Used to retrieve the `rest.server.[server]` property |
| method       | **Required** | String    | REST method.                         | **GET**: Send the parameters as part of the endpoint 
- **POST**: Send the parameters in the request body |
| endpoint     | **Required** | String    | Path to REST call                    | Ej: /data/ServiceData or /maintain/ServiceMaintain   |
| wrapper      | Optional     | String    | Classname to wrap REST call response | Ej: com.almis.awe.test.bean.Postman                       |
| content-type | Optional     | String    | Way to send the parameters           | `URLENCODED` (default), `JSON`                       |
| cacheable    | Optional     | Boolean   | Used to set service as cacheable     | Default value is `false`                             |

### Service parameter element

The service parameter element are parameters passed from query or maintain to the service. Has the following attributes:

| Attribute   | Use          | Type      |  Description                             |   Values                                      |
| ----------- | ------------ |-----------|------------------------------------------|---------------------------------------------- |
| name        | **Required** | String    | Name of service parameter                | **Note:** Must be unique                      |
| type        | **Required** | String    | Type of service parameter                | The possible values are: `STRING`, `INTEGER`, 
`FLOAT`, `DOUBLE`, `DATE`, `TIME` or `TIMESTAMP` |
| value       | Optional     | String    | To set the parameter with a static value |                                               |

## **Java services**

Are services to execute `java` code. Its xml structure is:

```xml
<service id="[service_name]">
  <java classname="[service_classname]" method="[service_method]">
    <service-parameter type="String" name="[parameter_name]"/>
    ... (more service parameters)
  </java>
</service>
```

### Java service examples

Service definition with parameters

```xml
<!-- Store a session variable -->
<service id="insertSchedulerTask">
  <java classname="com.almis.awe.scheduler.controller.SchedulerController" method="insertSchedulerTask">
    <service-parameter name="IdeTsk" type="INTEGER" />
    <service-parameter name="SendStatus" type="INTEGER" list="true" />
    <service-parameter name="SendDestination" type="INTEGER" list="true" />
  </java>
</service>

```

Java class definition with parameters

```java
package com.almis.awe.scheduler.controller;

public class SchedulerController extends AWEController {
  /**
   * Insert and schedule a new task
   *
   * @param taskId Task identifier
   * @param sendStatus Status to send list
   * @param sendDestination Destination target list
   * @return ServiceData
   * @throws AWException
   */
  public ServiceData insertSchedulerTask(Integer taskId, List<Integer> sendStatus, List<Integer> sendDestination) throws AWException {
    return manager.insertSchedulerTask(taskId, sendStatus, sendDestination);
  } 
}
```

Service definition without parameters

```xml
<!-- Get screen configuration at begining-->
<service id="LoaScrCfg" launch-phase="APPLICATION_START">
  <java classname="com.almis.awe.core.services.controller.ScreenController" method="initScreenConfigurations"/>
</service>
```

Java class definition without parameters

```java
package com.almis.awe.core.services.controller;

public class ScreenController extends AWEController {
  /**
   * Initialize singleton with screens configurations info
   *
   */
  public void initScreenConfigurations() {
    /* Variable definition */
    ScreenManager mgr = new ScreenManager();

    /* Launch function call */
    mgr.initScreenConfigurations();
  } 
}
````

### How to set parameter values from Java

There is the procedure to set set query and maintain variable values in Java.

**Java Service**

```java
  import com.almis.awe.core.util.ContextUtil;

  public class UserManager extends AWEManager { 

    /**
     * Get user information
     *
     * @param nif
     * @return ServiceData
     */
    private ServiceData getUserData(String id) throws AWException {
      DataList userData = null;
      ServiceData srvDat = null;

      ContextUtil.getContext().setParameter("opeId", id);
      DataList userData = new DataController().launchQuery("getUserData");
      
      return userData.getServiceData();
    }
  }

```

**Query definition**

```xml
<query id="getUserData">
  <table id="ope" alias="o" />
  <field id="l1_nom" alias="nom" table="o" />
  <field id="l1_lan" alias="lan" table="o" />
  <field id="EmlSrv" alias="eml" table="o" />
  <field id="OpeNam" alias="nam" table="o" />
  <where>
    <and>
      <filter field="IdeOpe" table="o" condition="=" variable="OpeId">
    </and>
  </where>
  <variable id="OpeId" type="STRING" name="opeId" />
</query>

```

>**Note:** This method of setting values of query and maintain variables is not recommended. It only has to be used in exceptional cases.

## **Microservices**

Microservices are connectors to REST-defined services. It's xml structure is:

```xml
<service id="[service_name]">
  <microservice name="alu-microservice" method="GET" endpoint="/[data/maintain]/[service-name]/{param1}">
    <service-parameter name="param1" type="STRING"/>
  </microservice>
</service>
```

### Microservice examples

```xml
<!-- GET BACKOFFICE NUMBER -->
<service id="BocNum">
  <microservice name="alu-microservice" method="POST" endpoint="/data/BilGetBoc">
    <service-parameter type="STRING" name="ent" />
    <service-parameter type="STRING" name="suc" />
    <service-parameter type="STRING" name="sns" />
    <service-parameter type="STRING" name="cap" />
    <service-parameter type="STRING" name="prd" />
  </microservice>
</service>
```

```xml
<!-- CONTROL CORRESPONSAL -->
<service id="BilCtlCrr">
  <microservice name="alu-microservice" method="POST" endpoint="/maintain/BilCtlCrr">
    <service-parameter type="STRING" name="Ent" />
    <service-parameter type="STRING" name="Liq" />
    <service-parameter type="STRING" name="Crr" />
  </web>
</service>
```

## **REST services**

REST services are very useful to connect to REST API's. Their xml structure is:

```xml
<service id="[service_name]">
  <rest server="server" method="GET" endpoint="/[data/maintain]/[service-name]/{param1}" wrapper="com.almis.test.ServiceDataWrapper" content-type="URLENCODED">
    <service-parameter name="param1" type="STRING"/>
  </rest>
</service>
```

> **Note:** `server` and `wrapper` attributes are optional. `server` attribute is used to retrieve the `rest.server.[server]` property, 
> which is appended to the `endpoint` defined url. `wrapper` attribute defines a class name which will be used to manage the REST call response
> and translate it into a `ServiceData` class, suitable for AWE.  

### REST services examples

* **Call to a local URL with GET method without parameters**
```xml
<service id="testSimpleRestGet">
  <rest method="GET" endpoint="http://localhost:18089/testapi/simple"/>
</service>
```

* **Call to a local URL with POST method without parameters**
```xml
<service id="testSimpleRestPost">
  <rest method="POST" endpoint="http://localhost:18089/testapi/simple"/>
</service>
```

* **Call to a local URL with GET method without parameters**
```xml
<service id="testComplexRestGet">
  <rest method="GET" endpoint="http://localhost:18089/testapi/complex/QtyTst"/>
</service>
```

* **Call to a local URL with POST method without parameters**
```xml
<service id="testComplexRestPost">
  <rest method="POST" endpoint="http://localhost:18089/testapi/complex/testInclude"/>
</service>
```

* **Call to a local URL with GET method with url parameters**
```xml
<service id="testComplexRestGetParameters">
  <rest server="local" method="GET" endpoint="/testapi/complex/QtyTst{name}/{value}">
    <service-parameter type="STRING" name="name" />
    <service-parameter type="STRING" name="value" />
  </rest>
</service>
```

* **Call to a local URL with POST method with url encoded parameters (default)**
```xml
<service id="testComplexRestPostParameters">
  <rest method="POST" endpoint="http://localhost:18089/testapi/complex/parameters/testRestParameters">
    <service-parameter type="INTEGER" name="value" />
  </rest>
</service>
```

* **Call to a local URL with POST method with json encoded parameters**
```xml
<service id="testComplexRestPostParametersJson">
  <rest server="local" method="POST" endpoint="/testapi/complex/parameters/json/testRestParameters" content-type="JSON">
    <service-parameter type="INTEGER" name="value" />
  </rest>
</service>
```

* **Call to a external URL with GET method without parameters and a wrapper**
```xml
<service id="testExternalRest">
  <rest server="islandia" method="GET" endpoint="/concerts" wrapper="com.almis.awe.test.bean.Concerts"/>
</service>
```

* **Call to a external URL with GET method without parameters and a wrapper**
```xml
<service id="testPostman">
  <rest server="postman" method="GET" endpoint="/gzip" wrapper="com.almis.awe.test.bean.Postman"/>
</service>
```