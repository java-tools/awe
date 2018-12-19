### Almis Web Engine - **[Home](../readme.md)**

---

# **Advanced development file**

## Table of Contents

* **[Introduction](#introduction)**
* **[Complex screens](#complex-screens)**
* **[Asynchronous actions with Batch Thread](#async-actions)**
* **[Advanced dependencies](#advanced-dependencies)**
* **[Specific reports](#specific-reports)**
* **[Email management](email-definition.md)**
* **[Overwriting actions](#overwriting-actions)**
* **[New component development](#new-component-development)**
* **[Style customization](#style-customization)**
* **[AWE REST api](rest-api.md)**
* **[F.A.Q.](#faq)**

---

## Introduction

## Complex screens

## Asynchronous actions with Batch Thread

In AWE we are able to execute any maintain in an asynchronous mode using a Batch Thread object (available in AWE API). This class creates a new context while launching the maintain received as parameter ("process").

```java
  /**
   * Constructor BatchThread
   *
   * @param process Process name
   * @param parameterList Parameter list in JSON
   */
  public BatchThread(String process, JsonNode parameterList) {
    this.process = process;
    this.parameters = parameterList;
  }
```

We have two ways of using this class: as a synchronous thread executing "call" method directly and as an asynchronous thread  by the use of Executors.

Example:

```java

private ExecutorService executor = Executors.newFixedThreadPool(5);

  /**
   * 
   * @param globalIdeOpl
   * @param unitFactor
   * @param printType
   * @param priceTypeDesc
   * @param productType
   * @return 
   * @return
   * @throws Exception 
   */
  public ServiceData checkBreakdownMethodAsync(Integer globalIdeOpl, Integer unitFactor, Integer printType,
	String priceTypeDesc, String productType) throws Exception {
	 
	  
	ObjectNode parameterList = JsonNodeFactory.instance.objectNode();

	// Save all mandatory parameters
        ....
    this.getContext().getParameter(PropertySingleton.getInstance().getProperty(PropertyType.TOKEN)));
	
	// Call new background process
    BatchThread confirmThread = new BatchThread("Maintain_Name", parameterList);
    executor.submit(confirmThread);
	
	ServiceData output = new ServiceData();
	output.setType(AnswerType.OK);
	output.setMessage(LocalSingleton.getInstance().getLocal(FmbConstants.MESSAGE_START_BREAKDOWN_PROCESS));
	
	return output;

  }

```

## Advanced dependencies

## Specific reports

## Overwriting actions

## New component development

## Style customization

## F.A.Q.