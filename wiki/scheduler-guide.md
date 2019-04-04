Almis Web Engine > **[Home](../README.md)**

---

# **Scheduler guide**

## Table of Contents

* **[Introduction](#introduction)**
  * **[Prerequisites](#prerequisites)**
* **[Tasks](tasks.md)**
* **[Servers](servers.md)**
* **[Calendars](calendars.md)**

## Introduction

This document gives a minimum base to start with the AWE Scheduler module, and explains how to automate and schedule tasks inside AWE in a simple way.

The Scheduler module is based on the Quartz Scheduler library. 

All documentation related to the Quartz Scheduler library can be found on its [web page](https://quartz-scheduler.org/documentation).

### Prerequisites

The scheduler module needs to be configure before it is used. 

To see how to configure the scheduler module for your application see the **[Configuration guide](modules.md#scheduler-module)**.    

## Tasks

A task consists on a job associated to a trigger that is executed by the Scheduler in the configured time / moment.

A task can also be concatenated with other tasks to create a workflow. This can be done by adding those other tasks as dependencies in the parent task configuration wizard.

To know more about tasks go to the **[Tasks guide](tasks.md)**.

## Servers

The servers created for the Scheduler module are mainly used to execute tasks, and in tasks that needs to check if a file has changed.

The servers can be instantiated multiple times, and each instantiation can use its own user and password to connect to the server with the selected protocol.

To know more about servers go to the **[Servers guide](servers.md)**.

## Calendars

The task inside the scheduler can be modified to ignore some dates by using holiday calendars.

Those calendars contains the dates that have to be ignored by the scheduler in the task schedule.

Each of the tasks can only be associated with one calendar.

To know more about calendars go to the **[Calendars guide](calendars.md)**.