### Almis Web Engine - **[Scheduler guide](scheduler-guide.md)**

---

# **Tasks**

## Table of Contents

* **[Definition](#definition)**
* **[Task types](#task-types)**
* **[Task configuration](#task-configuration)**
  * **[Schedule guide](schedule-configuration.md)**
  * **[Dependency guide](dependencies-guide.md)**
* **[Task management](#task-management)**

## Definition

A task consists on a job associated to a trigger that is executed by the Scheduler in the configured time / moment.

A task can also be concatenated with other tasks in order to create a workflow. This can be done by adding those other tasks as dependencies in the parent task configuration wizard.

## Task types

There are two type of tasks that the scheduler can work with, the maintain tasks and the command tasks.

| Maintain Task       | Command Task    |
| ------------- |:-------------:|
| A maintain task executes a public maintain with a defined schedule.| A command task executes a batch on the selected server with the defined schedule. |

## Task configuration

When creating a new task, a task creation wizard is used to personalize the task configuration.

The configuration wizard consists in 5 steps:

### 1. Basic task information ###

In this step we have to add the task basic configuration.

| Element       | Definition    | Use   |
| ------------- |:-------------:| -----:|
| Name          | Task name     | **Required** |
| Active        | Task status, if not active the task would not be launched   | **Required** |
| Description   | Task description |  Optional |
| Max. stored executions| Maximum number of executions to be stored in the database (Used to calculate the average time). The default value is 10. |  Optional|
| Timeout       | Maximum time for the task to finish. If the task execution time exceeds the timeout time (represented in milliseconds) the task will be interrupted |  Optional |
| Execute       | The task execution type (Command or Maintain) |  **Required** |
| Execute at    | Server in which the command task has to be launched |  Optional (Only needed in `Command` launch type) |
| Command    | Command to launch |  **Required** (Only needed in `Command` launch type) |
| Maintain   | Maintain to launch |  **Required** (Only needed in `Maintain` launch type) |
| Launch dependencies in case of warning | Launch the task dependencies in case of warning |  Optional |
| Launch dependencies in case of error| Launch the task dependencies in case of error |  Optional |
| Set execution as warning in case of error| Sets the parent execution as warning in case of dependency error |  Optional |

> **Note:** To add a new maintain to the Scheduler, the maintain must be set to `public="true"`.

### 2. Task parameters ###

This step allows to add the needed parameters to the maintain or command for its execution.

These parameters are loaded to the application context when the task is going to be executed. In this way, the task can get the parameters in the execution time.

| Element       | Definition    | Use   |
| ------------- |:-------------:| -----:|
| Name          | Parameter name   | **Required** |
| Source        | Parameter source, the place from which the parameter will take its value   | **Required** |
| Type   | The parameter type (Only used to give extra information to the user) |  **Required** |
| Value   | The parameter value, it can be directly the parameter value if the source is `Value`, or the parameter name to take the value from if the source is `Variable` |  Optional |


> **Note:** If the task launch type is `Maintain`, the needed parameters for the selected maintain will be automatically added to the task parameters screen.

### 3. Task launch ###

In this step we will configure the launch type for the task.

We can choose from three different options:

#### 1. Manual ####

The task will only be launched manually from the task list screen.

> **Note:** For a task to be added as a dependency, the launch type must be set to `Manual`.

#### 2. Scheduled ####

The task will be launched with a cron pattern based schedule.

See [schedule configuration guide](schedule-configuration.md) for more information about how to create schedules for a task.

#### 3. File ####

With this launch type, the task will launch a check in the selected file/s with the configured schedule.

To know how to create a schedule for the task see [schedule configuration guide](schedule-configuration.md).

The remaining fields are:

| Element       | Definition    | Use   |
| ------------- |:-------------:| -----:|
| Search at  | The server in which the scheduler has to check for the files  | **Required** |
| File path  | The path in which the file/s are located   | **Required** |
| File pattern  | The pattern that the files have to match with |  **Required** |
| User  | The user for the FTP connection |  Optional |
| Password  | The password for the FTP connection |  Optional |

### 4. Task dependencies ###

In this step we will configure which tasks have to be executed after the current task finishes.

Playing with these options, we can create a workflow.

| Element       | Definition    | Use   |
| ------------- |:-------------:| -----:|
| Task  | The task to be executed  | **Required** |
| Blocking  | Defines if the task is blocking or not. If it is, the task will be executed synchronously, and it will cancel the dependency launch stack if the task ends with an error. Otherwise, the dependency will be launched asynchronously  | **Required** |
| Order  | The order in which the synchronous task has to be launched in the synchronous dependency stack, the asynchronous dependencies will be launched as they come, with no defined order |  **Required** |

Go to the [dependencies guide](dependencies-guide.md) to see more about dependencies and their usage.

> **Note:** The dependencies can also have their own dependencies to create a workflow.

### 3. Task report ###

The last step is to choose a report type.

The report will give information about the task when it finishes.

We can choose one of these four options: 

#### 1. None ####

Used when we don't want to retrieve any report from the task.

This could be compared to the silent-action in AWE.

#### 2. Email ####

This option will send an email with the task information, and it will also add the dependencies information if any.

| Element       | Definition    | Use   |
| ------------- |:-------------:| -----:|
| Send in case of | Set the allowed status (task status when it finishes) for sending the email  | **Required** |
| Email server  | The email server form where the email is going to be sent | **Required** |
| Send to users | The list of users to send the email |  **Required** |
| Title | The title of the email |  **Required** |
| Message | The message to be added in the email |  **Required** |

> **Note:** The email will also add basic information about the task itself and its dependencies.

#### 3. Broadcast ####

This option will send a broadcast message with the given message to the selected users only.

| Element       | Definition    | Use   |
| ------------- |:-------------:| -----:|
| Send in case of | Set the allowed status (task status when it finishes) for sending the broadcast | **Required** |
| Send to users | The list of users to send the broadcast |  **Required** |
| Message | The message to be sent in the broadcast |  **Required** |

#### 4. Maintain####

This option will launch the selected maintain as a report.

| Element       | Definition    | Use   |
| ------------- |:-------------:| -----:|
| Send in case of | Set the allowed status (task status when it finishes) for executing the maintain | **Required** |
| Message | The message to be sent, it will be added to the context in order to be available for the selected maintain |  Optional |

> **Note:** The task data will be added to the context in order to be available for the selected maintain, to get the data, it is recommended to use the TaskConstants interface variable names from the Scheduler package.

## Task management

The existing tasks can be managed from the scheduler tasks screen, where we will have a list of the created tasks.

The list will show some basic information of each task, like the name, the launch type (icon), the last and next execution times, the task status and the execution average time.

When selecting one task, some options will be activated:

| Option       | Definition    | Multiple |
| ------------- |:-------------:| --------:|
| Update | Update the selected task | No |
| Delete | Delete the selected task/s | Yes |
| Start | Launch the selected task as a manual task. It doesn't need to be a manual task in order to launch an instance of the task manually | No |
| Activate/Deactivate | Activates or Deactivates the selected task, the option changes depending on the current task status | No |