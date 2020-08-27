---
id: debugging
title: Debugging
---

## Debugging tools

There are some useful tools 

### Server debug

To debug the server-side of a web application project, we usually work with the IDE (*Netbeans*, *Eclipse*, *IntelliJ*) embedded debugger.

To start the server in debug mode we can right click on the server, and pick the debug option.

<img alt="Debug mode" src={require('@docusaurus/useBaseUrl').default('img/debug_mode.png')} />

We can add a breakpoint anywhere in the code by right clicking at the side, and choosing toggle/add breakpoint.

For more information about debugging see eclipse documentation:

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

<Tabs
  defaultValue="IntelliJ"
  values={[
    {label: 'IntelliJ', value: 'IntelliJ'},
    {label: 'Eclipse',  value: 'Eclipse'},
    {label: 'Netbeans', value: 'Netbeans'},
  ]}>
  <TabItem value="IntelliJ"><a target="_blank" rel="noopener noreferrer" href="https://www.jetbrains.com/help/idea/debugging-code.html">IntelliJ debugging</a></TabItem>
  <TabItem value="Eclipse"><a target="_blank" rel="noopener noreferrer" href="https://www.eclipse.org/community/eclipse_newsletter/2017/june/article1.php">Eclipse debugging</a></TabItem>
  <TabItem value="Netbeans"><a target="_blank" rel="noopener noreferrer" href="https://netbeans.org/features/java/debugger.html">Netbeans debugging</a></TabItem>
</Tabs>

### Browser debug
We can see the information flow between the server and the client by pressing F12 key and entering the network tab. 
There are a few useful options there but we will focus on the WS (WebSocket) 

In the following example the interactions between the server and the client are displayed, and we can search through the JSON structure to see all the information and variables.

<img alt="Web sockets" src={require('@docusaurus/useBaseUrl').default('img/websockets.png')} />

We can see the action that the server sent to the client, in this case is a type fill action with the parameters attached to it

### Angular JS Batarang
Angular JS Batarang is an extension for browsers that adds tools for debugging and profiling AngularJS applications.

 [Angular JS Batarang Chrome extension](https://chrome.google.com/webstore/detail/angularjs-batarang/ighdmehidhipcmcojjgiloacoafjmpfk)
 
This extension allows us to see the angular scope info. The scope is the binding part between the HTML (view) and the JavaScript (controller).
Once the extension is installed, by pressing F12 and accessing the elements tab we can see the new $scope option.

The information displayed there contains all JavaScript methods and parameters, in JSON format.

We can select a window element by clicking the inspect option to see all the scope information regarding only that element.
In chrome the option is the following:

<img alt="Inspect Chrome" src={require('@docusaurus/useBaseUrl').default('img/inspect_chrome.png')} />

In this expample, we select a grid containing users information. Between all the options we can see the `controller` info, like the grid attributes, variable types...

<img alt="Scope controller" src={require('@docusaurus/useBaseUrl').default('img/scope_controller.png')} />

It is also interesting to see the `model` option, as it contains the data itself.

<img alt="Scope model" src={require('@docusaurus/useBaseUrl').default('img/model_scope.png')} />

## Logs

### Server logs
To search through the server logs we can use the tools that Eclipse provides. 
Once the server is running, the logs will appear in the Console tab at the bottom of the screen.
Every action that the server executes will be displayed here, such as the SQL queries.

In the following example we have a grid that displays information from the users table, and a criteria that filters that grid by executing a SQL query attached to it:

<img alt="Grid logs" src={require('@docusaurus/useBaseUrl').default('img/window_grid_log.png')} />

Once we hit the search button, the server will execute the SQL query, and the console log will display it:

<img alt="SQl log example" src={require('@docusaurus/useBaseUrl').default('img/log_query.png')} />

In this case we can see the date and time that the action was executed as well as the user, the screen, the query ID, and the full SQL query.

### Browser console

To search through the browser console, we press the F12 key, and we look in the console tab.

#### Actions

We can see there all the actions that take place at the client side, along with the time it takes and the parameters that are sent to the server.
In this case there is a type fill action that is used to fill a grid with query data.

<img alt="Browser log example" src={require('@docusaurus/useBaseUrl').default('img/browser_log.png')} />

#### Dependencies

In the following example we have a grid, and a view button with a dependendy attached to it. The dependency checks if the number of selected rows is not equal to 1. If the condition is true (that is that 0 or more than 1 row are selected), the view button is hidden; otherwise the button is activated.

<img alt="Depencency log example" src={require('@docusaurus/useBaseUrl').default('img/dependency_log.png')} />

As only one row is selected, the condition result is false, therefore, the button is not hidden.

<img alt="Depencency condition example" src={require('@docusaurus/useBaseUrl').default('img/dependency_condition.png')} />