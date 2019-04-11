Almis Web Engine > **[Scheduler guide](scheduler-guide.md)**

---

# **Servers**

## Table of Contents

* **[Definition](#definition)**
* **[Server configuration](#server-configuration)**
* **[Server management](#server-management)**

## Definition

The servers created for the scheduler are mainly used with two purposes, to launch a batch on a remote server, and to check for file modifications in an FTP server.

Regarding to the FTP servers, the same server can be used as many times as needed, in different tasks, with different credentials.

## Server configuration

When creating a new server, the next fields have to be filled.

| Element       | Definition    | Use   |
| ------------- |:-------------:| -----:|
| Name          | Server name     | **Required** |
| Server        | Server IP address     | **Required** |
| Port          | Server port     | **Required** |
| Type of connection   | The protocol to be used to connect to the server     | **Required** |
| Active          | Server status     | **Required** |

> **Note:** If a server is deactivated, the task using it won't even try to connect to it.

## Server management

The scheduler server list will show a list of server with their basic information: name, server ip, connection protocol and status.

When selecting one of the servers from the list, some options will be enabled:

| Option       | Definition    | Multiple |
| ------------- |:-------------:| ----------- |
| Update          | Update the selected server  | No |
| Delete          | Deletes the selected server/s  | Yes |
| Activate / Deactivate   | Activates / deactivates the selected server, the label changes depending on the selected servers current status  | No |

