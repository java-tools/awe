Almis Web Engine > **[Scheduler guide](scheduler-guide.md)**

---

# **Calendars**

## Table of Contents

* **[Definition](#definition)**
* **[Calendar configuration](#server-configuration)**
* **[Calendar management](#server-management)**

## Definition

The calendars inside Scheduler module are used to set the dates where the tasks won't have to be executed, like for example, holidays, or weekends. 

Each task can only have associated one calendar, but there can be created as many calendars as needed, and then just change the calendar associated to the task.

## Calendar configuration

The calendar configuration procedure consists in two steps:

#### 1. Create calendar ####

The first step is to create the calendar itself, which will have the next basic information.

| Element       | Definition    | Use   |
| ------------- |:-------------:| -----:|
| Name          | The calendar name     | **Required** |
| Description   | The calendar description     | **Required** |
| Active  | The calendar status     | **Required** |

> **Note:** If the calendar status is set to `Active = No` the task will ignore the calendar, and it will be launch as if it wasn't associated with it.

#### 2. Add dates ####

Once the calendar is created, from the calendar configuration screen, we can add new dates by selecting the option edit on the top right side of the screen.

When we get to the edition screen we will have to fill the next fields for each date.

| Element       | Definition    | Use   |
| ------------- |:-------------:| -----:|
| Date          | The date to add to the calendar    | **Required** |
| Name   | A name to assign to the date, for example, a holiday name     | Optional |

## Calendar management

On the calendar list screen, when selecting one of them, the next options will be available:

| Option        | Definition    | Multiple   |
| ------------- |:-------------:| ----------:|
| Edit          | Redirects to the edition screen where we can change the calendar data, and add/remove/update calendar dates    | No |
| Delete   | Delete calendar and all its associated dates     | Yes |
| Activate/Deactivate   | Activates / deactivates the selected calendar, the label changes depending on the selected calendars current status  | No |