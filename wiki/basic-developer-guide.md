### Almis Web Engine - **[Home](../readme.md)**

---

# **Basic development guide**

## Table of Contents

* **[Introduction](#introduction)**
* **[Project structure](#project-structure)**
* **[Screen development](basic-screen-development.md)**
* **[Menu definition](menu-definition.md)**
* **[Query definition](query-definition.md)**
* **[Maintain definition](maintain-definition.md)**
* **[Enumerate definition](enumerate-definition.md)**
* **[Service definition](service-definition.md)**
* **[JMS queues definition](jms-queues-definition.md)**
* **[Chart development](chart-development.md)**
* **[I18N (internationalization)](i18n-internationalization.md)**
* **[Security management](security-management.md)**
* **[Default screens](default-screens.md)**

---

## Introduction

AWE (Almis Web Engine) is a web application development tool which allows the developer to create web application screens by describing the screen behaviour on a XML file.

AWE also allows the developer to design application resources, such as database queries and actions (insert, update and delete), JMS queues call and launches, email sending, report generation, batch launching and any other action by calling external services developed in Java and C.

## Project structure

AWE defines a folder structure for XML files to ensure the project compatibility:

```
{PROJECT-ACRONYM}
|- global
|- local
|- menu
|- profile
|- screen
```

### Global folder

The *global* folder contains the following files:

* **Actions.xml** - Which contains the specific actions of the application. To be used in complex applications with specific components.
* **Email.xml** - With the definitions of email contents and targets. To be used for sending automatic emails.
* **Enumerated.xml** - Which contains groups of small values to be used in select boxes or to translate values.
* **Queries.xml** - With the definition of database and service data requests.
* **Queues.xml** - Interface file to JMS queues.
* **Maintain.xml** - With the definition of database and service process calls.
* **Services.xml** - Interface file to external services.

### Local folder

The *local* folder contains a file for each language defined on the application. All files are named Local-*{language}*.xml, where **language** is the language acronym in two letters (ES, EN, FR...).

### Menu folder

The *menu* folder contains the following files:

* **private.xml** - With the menu definition for the private section (with login) of the application.
* **public.xml** - With the menu definition for the public section of the application.

### Profile folder

The *profile* folder contains a file for each restriction group defined on the application. Each file contains a ruleset to allow or deny access to menu options.

### Screen folder

The *screen* folder contains a file for each screen defined in the application. These files define a structure and a behaviour for all the components you want to show in the screen.
