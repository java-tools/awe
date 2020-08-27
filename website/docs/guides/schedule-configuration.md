---
id: schedule-configuration
title: Schedule Tasks Configuration 
---

One of the main features of the scheduler, is the powerful tool that allows to create any kind of schedule in a simple way. 

For this purpose, and with the simplicity in mind, the schedule creation has been separated into three main schedule options:

## Repetitive schedules

Schedules that are launched every `X` time. We can choose the time that has to pass between launches, and the time unit that is going to be used.

The repetitive schedule, as its name suggest, allows to create schedules that are launched every `X` time, where `X` is the time between one launch to the next one.


Depending on the time unit we choose, some other options will appear into the configuration screen to help the user to create a more advance schedule.

But first, the next fields are common for all the possible options.

| Option       | Definition    | Use | Disables |
| ------------- |:-------------:| -------- |:-------------: | 
| Calendar | A holiday calendar to use for the current task, the task won't be launched in the dates contained by the calendar | Optional | `Execution date` |
| From ^1 | The start date / time for the current task schedule | Optional | None |
| To ^2 | The end date / time for the current task schedule | Optional | None |
| Repeat each | The time that has to pass between executions | **Required** | None |


***

^1: If the `From` criterion is empty, the task will be launched at the moment it's created.

^2: If the `To` criterion is empty, the task will be launched indefinitely until its configuration changes, or the task is removed.

### Seconds

| Option       | Definition    | Use | Disables |
| ------------- |:-------------:| -------- |:-------------: | 
| Months | The months that the schedule will be launch. | Optional | None |
| Days | The days of the month that the schedule will be launch. | Optional | `Days of the week` |
| Days of the week | The days of the week that the schedule will be launch. | Optional | `Days` |
| Hours | The hours where the schedule will be launch. | Optional | None |
| Minutes | The minutes where the schedule will be launch. | Optional | None |

### Minutes

| Option       | Definition    | Use | Disables |
| ------------- |:-------------:| -------- |:-------------: | 
| Months | The months that the schedule will be launch. | Optional | None |
| Days | The days of the month that the schedule will be launch. | Optional | `Days of the week` |
| Days of the week | The days of the week that the schedule will be launch. | Optional | `Days` |
| Hours | The hours where the schedule will be launch. | Optional | None |

### Hours

| Option       | Definition    | Use | Disables |
| ------------- |:-------------:| -------- |:-------------: | 
| Months | The months that the schedule will be launch. | Optional | None |
| Days | The days of the month that the schedule will be launch. | Optional | `Days of the week` |
| Days of the week | The days of the week that the schedule will be launch. | Optional | `Days` |

### Days

| Option       | Definition    | Use | Disables |
| ------------- |:-------------:| -------- |:-------------: | 
| Months | The months that the schedule will be launch. | Optional | None |
| Hours | The hours where the schedule will be launch. | Optional | `Execution time`|
| Minutes | The minutes where the schedule will be launch. | Optional | `Execution time` |
| Seconds | The seconds where the schedule will be launch. | Optional | `Execution time` |
| Execution time | The time where the schedule will be launch. | Optional | `Hours`,`Minutes`,`Seconds` |

### Months

| Option       | Definition    | Use | Disables |
| ------------- |:-------------:| -------- |:-------------: | 
| Days | The days of the month that the schedule will be launch. | Optional | `Days of the week` |
| Days of the week | The days of the week that the schedule will be launch. | Optional | `Days` |
| Hours | The hours where the schedule will be launch. | Optional | `Execution time`|
| Minutes | The minutes where the schedule will be launch. | Optional | `Execution time` |
| Seconds | The seconds where the schedule will be launch. | Optional | `Execution time` |
| Execution time | The time where the schedule will be launch. | Optional | `Hours`,`Minutes`,`Seconds` |

### Years

| Option       | Definition    | Use | Disables |
| ------------- |:-------------:| -------- |:-------------: | 
| Months | The months that the schedule will be launch. | Optional | None |
| Days | The days of the month that the schedule will be launch. | Optional | `Days of the week` |
| Days of the week | The days of the week that the schedule will be launch. | Optional | `Days` |
| Hours | The hours where the schedule will be launch. | Optional | `Execution time`|
| Minutes | The minutes where the schedule will be launch. | Optional | `Execution time` |
| Seconds | The seconds where the schedule will be launch. | Optional | `Execution time` |
| Execution time | The time where the schedule will be launch. | Optional | `Hours`,`Minutes`,`Seconds` |


> **Note:** If one of the optional values inside the configuration is left empty, the default `All` value will be applied.


## One time Schedules

This kind of schedule will launch a task just once.

This schedule type launches a task only once, by creating a cron pattern with an specific date and time.

The available fields to create this kind of task are:

| Option       | Definition    | Use | Disables |
| ------------- |:-------------:| -------- |:-------------: | 
| Execution date | The launch date. | **Required** | None |
| Execution time | The launch time. | **Required** | None |

> **Note:** At least one of the two criterions needs to be filled.

## Custom schedules

The custom schedule allows to create every possible configuration that the Quartz Scheduler can work with. The custom schedule configuration screen uses dependencies in order to help the user with the correct creation of the schedule by enabling / disabling criterions when needed.

The custom schedule offers all the available fields that can be customized to create the cron pattern.


| Option       | Definition    | Use | Disables |
| ------------- |:-------------:| -------- |:-------------: | 
| Calendar | A holiday calendar to use for the current task, the task won't be launched in the dates contained by the calendar | Optional | `Execution date` |
| From ^1 | The start date / time for the current task schedule | Optional | `Execution date`,`Execution time` |
| To ^2 | The end date / time for the current task schedule | Optional | `Execution date`,`Execution time` |
| Years | The years that the schedule will be launch. | Optional | `Execution date` |
| Months | The months that the schedule will be launch. | Optional | `Execution date` |
| Days | The days of the month that the schedule will be launch. | Optional | `Execution date`,`Days of the week` |
| Days of the week | The days of the week that the schedule will be launch. | Optional | `Execution date`,`Days` |
| Hours | The hours where the schedule will be launch. | Optional | `Execution time`|
| Minutes | The minutes where the schedule will be launch. | Optional | `Execution time` |
| Seconds | The seconds where the schedule will be launch. | Optional | `Execution time` |
| Execution date | The date where the schedule will be launch. | Optional | `Calendar`,`From`,`To`,`Years`,`Months`,`Days`,`Days of the week` |
| Execution time | The time where the schedule will be launch. | Optional | `Hours`,`Minutes`,`Seconds` |

***

^1: If the `From` criterion is empty, the task will be launched at the moment it's created.

^2: If the `To` criterion is empty, the task will be launched indefinitely until its configuration changes, or the task is removed.