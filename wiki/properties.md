### Almis Web Engine - **[Configuration](configuration-guide.md)**

---

# **Properties**

## Table of Contents

* **[Introduction](#introduction)**
* **[Encoding property value](#encoding-property-value)**
* **[Basic properties](#basic-properties)**
* **[Library properties](#library-properties)**
* **[Email properties](#email-properties)**
* **[Numeric properties](#numeric-properties)**
* **[Database properties](#database-properties)**
* **[Security properties](#security-properties)**
* **[Session properties](#session-properties)**
* **[Version properties](#version-properties)**
* **[Services properties](#microservices-properties)**
* **[Logging properties](#logging-properties)**

---

## Introduction

Various properties can be specified inside your `application.properties` file, inside your `application.yml` file, or as command line switches. 
This document provides a list of common **AWE** properties and references to the underlying classes that consume them.

---

> :information_source: Property contributions can come from additional jar files on your classpath, so you should not consider this an exhaustive list. Also, you can define your own properties.

---

> :warning: This sample file is meant as a guide only. Do not copy and paste the entire content into your application. Rather, pick only the properties that you need.

---

## Encoding property value

There's a way to store encoded properties (such as passwords) in the properties files. To achieve that, you need to deploy 
the application, and enter into `Settings -> Security access -> Encrypt util`, fill the Text criterion with the password 
and push the `Encrypt` button. The `Encrypted property` field is the value of the encoded password.

![Encrypt tool](images/EncryptTool.png)

Once encoded, you only need to put it on any of your properties files and use it:

```properties
my.encoded.password=ENC(Pit1Q1bNt3uNQpZbldKbLg==)
```

---

> :information_source: The master key used to encode the properties is the `security.master.key` property from `security.properties` file.
> You can overwrite it on your own `application.properties` file.

---

> :warning: The `Phrase key` field on the `Encrypt util` screen allows you to generate the encoded properties with other passwords, but don't forget that
> the encoded properties with a phrase key and the `security.master.key` must match to be decoded successfully.

---

## Basic properties

Defined on `config/base.properties` file. 
These are the basic application configuration properties.

```properties
################################################
# Application data
################################################
# Base encoding for all files
application.encoding=UTF-8
# Application name
application.name=AWE (Almis Web Engine)
# Application default theme
application.theme=sky
# Application acronym (in lowercase)
application.acronym=awe
# Application version
application.version=4.0.2

################################################
# Global Paths
# (@ means relative path to app base path)
################################################
application.base.path=/${application.acronym}/
application.paths.application=application/
application.paths.global=/global/
application.paths.locale=/locale/
application.paths.screen=/screen/
application.paths.profile=/profile/
application.paths.menu=/menu/
application.paths.templates=templates/
application.paths.templates.angular=angular/
application.paths.tag=tags/
application.paths.tag.angular=${paths.tag}angular/
application.paths.server=./
application.paths.images=images/
application.paths.reports=@reports/
application.paths.reports.historic=@historicReports/
application.paths.jrx=/jrx/
application.paths.temp=/tmp/
application.paths.log=/log/

################################################
# Application Icons
################################################
# Favicon
application.icon.favicon=${application.paths.images}icon/favicon.png
# Apple icon 57x57
application.icon.phone=${application.paths.images}icon/favicon.png
# Apple icon 114x114
application.icon.tablet=${application.paths.images}icon/favicon.png

################################################
# Application parameters
################################################
# Default action parameter value
application.parameter.screen=screen
# Default frame parameter name
application.parameter.frame=frame
# Token variable
application.parameter.token=t
# Connection id
application.parameter.comet.id=s
# Default address parameter
application.parameter.address=address
# Default view parameter
application.parameter.view=view
# Default component parameter name
application.parameter.component=component

################################################
# Screen configuration
################################################
# Home screen name
screen.configuration.home=home
# Change password screen
screen.configuration.change.password=/screen/change_password
# Information screen
screen.configuration.information=information
# Screen access base
screen.configuration.base=screen

################################################
# Screen parameters
################################################
# User parameter
screen.parameter.username=cod_usr
# Password parameter
screen.parameter.password=pwd_usr

################################################
# Language
################################################
# Language
language.default=en
# Available languages
language.list=en,es,fr

################################################
# Print
################################################
# Show all print options
print.show.options=true

################################################
# Debug and log (Javascript)
################################################
# Action Stack Miliseconds to display: Turn off setting as 0
client.action.stack=0
# Javascript debug level (DEBUG, INFO, WARNING, ERROR)
client.debug.level=INFO

################################################
# Log (Server)
################################################
# Log name parameter
application.log.name=name
# Log level parameter
application.log.level=level

################################################
# Connection protocols
################################################
# Connection main protocol [AJAX|COMET]
connection.protocol=COMET
# Comet main protocol (default: 'websocket')
# [polling, long-polling, streaming, jsonp and websocket]
connection.transport=websocket
# Comet fallback (backup) protocol (default: 'streaming')
# [polling, long-polling, streaming, jsonp and websocket]
connection.backup=streaming
# Connection timeout (should be lower than tomcat timeout)
connection.timeout=60000000

################################################
# Application Defaults
################################################
# Rows per page (Set to 0 to avoid pagination)
application.data.rowsPerPage=30
# Pixels per char in grids
application.data.pixelsPerCharacter=8
# Empty computed values if one of the wildcards are empty
application.data.set.computed.empty.if.null=true
# Suggest timeout in milliseconds
application.suggest.timeout=300
# Default component size (sm/md/lg)
application.default.component.size=sm
# Reload current screen
application.reload.current.screen=false
# Loading timeout
application.screen.load.timeout=10000

################################################
# File Upload
################################################
# Upload identifier
file.upload.identifier=u
# Download identifier
file.download.identifier=d
# Path for file uploads (Relative to application.base.path starts with @)
file.upload.path=@upload/
# Max size of files (in MB)
file.upload.max.size=500
# Max elements per folder
file.upload.max.files.folder=1000

################################################
# Tooltip timeouts
################################################
# Timeout for ok messages
tooltip.timeout.ok=2000
# Timeout for error messages
tooltip.timeout.error=0
# Timeout for warning messages
tooltip.timeout.warning=4000
# Timeout for info messages
tooltip.timeout.info=0
# Timeout for validation messages
tooltip.timeout.validation=2000
# Timeout for help messages
tooltip.timeout.help=5000
# Timeout for chat messages
tooltip.timeout.chat=0

################################################
# Jms configuration properties
################################################
# JMS Enabled
awe.jms.enable=false
# Default Jms Service timeOut (in milliseconds)
awe.jms.service.timeout=10000
# Default Jms Service timeToLive
awe.jms.message.time.to.live=0
```

## Library properties

Defined on `config/library.properties` file. 
Here you can define the layer behaviour of the application 
(which module is defined in which place).

```properties
################################################
# Utilities list
################################################
modules.list=app,modn,...,mod1,awe
modules.prefix=module.

################################################
# AWE Paths
################################################
# Awe module (all awe xml files lie
# under application/awe/)
module.awe=awe

################################################
# Module 1 Paths
################################################
# Module acronym (all module xml files must lie
# under application/${module.mod1}/)
module.mod1=mod1

...

################################################
# Module n Paths
################################################
# Module acronym (all module xml files must lie
# under application/${module.modn}/)
module.modn=modn

################################################
# APP Paths
################################################
# Application acronym (all application xml files
# must lie under application/${module.app}/)
module.app=app
module.app.documents=static/docs/${module.app}/
```

> :information_source: First module must be always **app**, last module must be always **awe**.

---

> :floppy_disk: This file should be overwritten on the final application.

## Email properties

Defined on `config/email.properties` file. 
Here you can define the email server settings.

```properties
################################################
# Mail server properties
################################################
# Localhost for smtp server
awe.mail.enabled=true
# Mail JNDI name
awe.mail.jndi.name=
# Localhost for smtp server
awe.mail.localhost=localhost
# Flag to specify if authentication is needed
awe.mail.auth=true
# Email server host
awe.mail.host=email.server.com
# Email server port
awe.mail.port=25
# Email server username
awe.mail.user=
# Email server password
awe.mail.pass=
# Flag to specify the usage of SSL/TLS protocol
awe.mail.ssl=
# Flag to specify the usage of STARTTLS protocol
awe.mail.tls=
# Execute email in debug mode
awe.mail.debug=true
```

> :information_source: If an error like `Could not connect to SMTP host: xxxxx:xx (javax.net.ssl.SSLException: Unrecognized SSL message, plaintext connection?)` appears, it means that we need to add the certificate authority that the email servier is using to the Java Certificate keystore.

## Numeric properties

Defined on `config/numeric.properties` file. 
Here you can define the numeric behaviour of the application

```properties
################################################
# Default configuration
################################################
# Numeric format for field pattern value
numeric.pattern.formatted=###,###.####
# Numeric format for field pattern value (plain, without thousand separators)
numeric.pattern.unformatted=######.####
# Number format separator for input numeric 
# - eur: EUROPEAN FORMAT [10.000.000,00] 
# - ame: AMERICAN FORMAT [10,000,000.00]
# - eur_no: EUROPEAN NO THOUSANDS [10000000,00] 
# - ame_no: AMERICAN NO THOUSANDS [10000000.00]
numeric.format=eur

################################################
# Numeric configuration
################################################
# Minimum value
numeric.min.value=-9999999999.99
# Maximum value
numeric.max.value=9999999999.99
# Padding with zeros
numeric.padding=false
# Rounding type
# - S: Round-Half-Up Symmetric (default)
# - A: Round-Half-Up Asymmetric
# - s: Round-Half-Down Symmetric (lower case s)
# - a: Round-Half-Down Asymmetric (lower case a)
# - B: Round-Half-Even "Bankers Rounding"
# - U: Round Up "Round-Away-From-Zero"
# - D: Round Down "Round-Toward-Zero" - same as truncate
# - C: Round to Ceiling "Toward Positive Infinity"
# - F: Round to Floor "Toward Negative Infinity"
numeric.round.type=S
# Default decimal numbers
numeric.decimal.numbers=5

################################################
# Formatting configuration EUR
################################################
# Thousands separator
numeric.separator.thousand.eur=.
# Decimal separator
numeric.separator.decimal.eur=,

################################################
# Formatting configuration AME
################################################
# Thousands separator
numeric.separator.thousand.ame=,
# Decimal separator
numeric.separator.decimal.ame=.

################################################
# Formatting configuration EUR no thousands
################################################
# Thousands separator
numeric.separator.thousand.eur_no=
# Decimal separator
numeric.separator.decimal.eur_no=,

################################################
# Formatting configuration AME no thousands
################################################
# Thousands separator
numeric.separator.thousand.ame_no=
# Decimal separator
numeric.separator.decimal.ame_no=.
```

## Database properties

Defined on `config/database.properties` file. 
Here you can define the database connection

```properties
################################################
# Database
################################################

# Enable database
awe.database.enabled=false

# Datasource
spring.datasource.jndi-name=
# Datasource type - oracle, sqs, hsql, mysql
awe.database.connection.type=

# Datasource server url
spring.datasource.url=
# Datasource username
spring.datasource.username=
# Datasource password
spring.datasource.password=
# Datasource driver
spring.datasource.driver-class-name=
# Datasource validation query
spring.datasource.validation-query=select 1 from ope

################################################
# Audit properties
################################################
# Activate audit
awe.database.audit=true
# Audit date field
awe.database.audit.date=HISdat
# Audit user field
awe.database.audit.user=HISope
# Audit action (Insert, update, delete) field
awe.database.audit.action=HISact
# Time between audit operations
awe.database.audit.lag=100

################################################
# Batch maintain operation properties
################################################
# Number of batch operations to be launched at once
awe.database.batch.max=100
```

## Security properties

Defined on `config/security.properties` file. 
Here you can define the security behaviours of your application.

```properties
################################################
# Security
################################################
# Password encryption variable
security.master.key=xxxxxxxxxxxxx
# Activate json encryption (0 - deactivated|1 - activated)
security.json.encryption=0
# Encrypted parameter list name
security.json.parameter=p
# Default restriction set (default should be the most restricted)
security.default.restriction=general
# Spring role prefix
security.role.prefix=ROLE_

################################################
# Authentication
################################################
# Authentication mode (ldap | bbdd | in_memory | custom)
security.auth.mode=bbdd

################################################
# Custom authentication
################################################
# Provider class beans, separated by comma for multiple providers.
security.auth.custom.providers=

################################################
# Ldap Windows
################################################
# User login property name
security.auth.ldap.windows.user.property=sAMAccountName={0}

################################################
# Ldap Unix
################################################
# User login property name
security.auth.ldap.unix.user.property=uid={0}

################################################
# General ldap properties
################################################
# Enviroment Ldap (windows | unix)
security.auth.ldap=windows
# Ldap urls (ldap:// or ldaps://)
security.auth.ldap.url=
# Ldap user bind
security.auth.ldap.user.bind=
# Ldap password bind
security.auth.ldap.password.bind=
# Ldap Base DN of user
security.auth.ldap.basedn=
# Ldap connection time out
security.auth.ldap.timeout=5000

################################################
# General jdbc properties
################################################
# JDBC Authentication user check query
security.auth.jdbc.param.userPasswordQuery=select LTRIM(RTRIM(l1_nom)) as name, LTRIM(RTRIM(l1_pas)) as password, l1_act from ope where LTRIM(RTRIM(l1_nom)) = ?
# JDBC Authentication role check query
security.auth.jdbc.param.rolesQuery=select LTRIM(RTRIM(l1_nom)) as name, LTRIM(RTRIM(Acr)) as role from ope left join AwePro on ope.IdePro = AwePro.IdePro where LTRIM(RTRIM(l1_nom)) = ?

################################################
# Security request headers
################################################
# Avoid cross domain frame requests
security.headers.frameOptions.sameOrigin=true
```

## Session properties

Defined on `config/session.properties` file. 
Here you can define the session management.

```properties
################################################
# Session configuration
################################################
# Share session in browser tabs
session.shareSessionInTabs=false

################################################
# Session parameter list, separated by comma (,)
################################################
# Session parameters
session.parameters=module,site,database

################################################
# Session parameter value queries
################################################
# Queries to initialize session parameters
session.module.query=ModNamByOpeSel
session.site.query=SitNamByOpeSel
session.database.query=DbsAlsBySitModSel

###############################################
# Spring session
###############################################
# Spring session persistence source
#REDIS - ok
#MONGO - ok
#JDBC
#HAZELCAST
#HASH_MAP - ok
#NONE - ok
spring.session.store-type=HASH_MAP
# Session timeout in seconds (30 min)
server.session.timeout=1800

########### REDIS CONFIGURATION ###############
# redis host
spring.redis.host=
# redis port
spring.redis.port=6379
# Sessions flush mode.
spring.session.redis.flush-mode=on-save
# Namespace for keys used to store sessions.
spring.session.redis.namespace=awesessions

########## HAZELCAST CONFIGURATION ############
# Sessions flush mode.
spring.session.hazelcast.flush-mode=on-save
# Name of the map used to store sessions.
spring.session.hazelcast.map-name=awe:session:sessions

############ JDBC CONFIGURATION ###############
# Create the required session tables on startup if necessary.
# Enabled automatically if the default table name is set or a custom schema is configured.
#spring.session.jdbc.initializer.enabled=
# Path to the SQL file to use to initialize the database schema.
spring.session.jdbc.schema=classpath:org/springframework/session/jdbc/schema-mysql.sql
# Name of database table used to store sessions.
spring.session.jdbc.table-name=AWE_SESSION

###############################################
# Session datasource configuration
###############################################

########## HAZELCAST CONFIGURATION ############
# The location of the configuration file to use to initialize Hazelcast.
#spring.hazelcast.config=classpath:config/hazelcast-default.xml
```

> :information_source: To know more about spring session configuration, visit [Spring Appendix A](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html)

## Microservices properties

Defined on `config/microservices.properties` file. 
Here you can define the microservices servers configuration

```properties
# Microservices search endpoint
awe.microservices.endpoint=http://localhost:18083/api/
```

## Logging properties

Defined on `log4j2-app.properties` file. 
Here you can define the log properties

```properties
###############################################
# User log
###############################################
# Flag to activate custom user file logs (true | false)
log.users.enabled=true
# Flag to build log path with system user home directory (true | false)
application.log.user.home=false
# Default level for custom user file logs
log.default.level=info

###############################################
# Log4j2 properties
###############################################
# Application log name
log.app=AWE
# Jasper log name
log.jasper=ADE
# Jms log name
log.jms=JMS
# Scheduler log name
log.scheduler=SCHEDULER
# Model log name
log.model=MODEL
# File extension for custom user file logs
log.ext=.log
# Default path
log.path=/logs
# Path for app version
log.app.version=V4.0.0/
# Pattern layout
log.pattern.layout=%d{yyyy-MM-dd HH:mm:ss.SSS} [%-5level] [%t] - %msg%n
# File pattern for rolling
log.pattern.file=_%d{yyyy-MM-dd}-%i
# Charset
log.charset=UTF-8
# Size rolling policy
log.size.rolling=10 MB
# Date rolling policy (Number days)
log.date.rolling=1
# Max number files rolling backup
log.num.file.rolling=10
# Delete files if size exceeds more than this number
log.delete.if.size.exceeds=500 MB
# Delete files if days exceeds more than this number
log.delete.if.days.exceeds=15d
# Log folder
application.log.type=java
# Default base logs path
application.log.base.path=${log.path}/${log.app}/${log.app.version}
# Default path for Java logs (See log42.xml)
application.log.file.path=${application.log.base.path}/${application.log.type}/
```

> :floppy_disk: This file should be overwritten on the final application.
