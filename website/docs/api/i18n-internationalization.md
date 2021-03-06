---
id: i18n-internationalization
Title: I18N (Internationalization)
---

AWE implements a i18n system for internationalization of web applications. For this reason, AWE uses locale files containing literals in different languages.

Also you can add `CDATA` tag inside local in **markdown** language to show rich text. Very useful when you want to show a lot of formatting information. For example in the help screen application. You can view all markdown syntax in [this](https://wiki.almis.com/help/markdown/markdown) page.

> **Note:** All locals are defined in the `Local-[Country code].xml` files at **local folder**. The country codes must be two capital letters.  View [project structure](../guides/project-structure.md#global-folder)  for more info.

## Locals XML structure

The full local structure is the next one:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<locals xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="https://aweframework.gitlab.io/awe/docs/schemas/locale.xsd">
  <local name="[local-name]" value=[local-value]" />
    <![CDATA[ Markdown content ]]>
  <local name="[local-name]" value=[local-value]">
  </local>
  ... (More <local>)
</locals>
```

### Locals structure


| Element     | Use      | Multiples instances    | Description                                        |
| ----------- | ---------|------------------------|----------------------------------------------------|
| locals| **Required** | No | Root node of locals structure |
| [local](#local-element) | **Required** | Yes | Used to define a local translate |


### Local element

Local element has the following attributes:

| Attribute   | Use      | Type      |  Description                    |   Values                                           |
| ----------- | ---------|-----------|---------------------------------|----------------------------------------------------|
| name | **Required** | String | The name of the local           |   |
| value | Optional | String | The value of the local, the language translation of the text          | **Note:** You can set values as variables with syntax {0} {1} ...  |
| markdown | Optional  | String | The value of the local. It will be translated as markdown | |

## Examples

Some examples of locals in differents languages:



**File Local-EN.xml**
```xml
<locals xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="https://aweframework.gitlab.io/awe/docs/schemas/locale.xsd">
  <local name="BUTTON_ACCEPT" value="Accept" />
  <local name="CONFIRM_MESSAGE_DELETE" value="You will delete the selected records. Do you agree?" />
  <local name="ERROR_MESSAGE_BAD_QUEUE_REQUEST_DEFINITION_FORMAT" value="Bad request definition format for queue {0}" />
  ...
</locals>
```



**File Local-ES.xml**
```xml
<locals xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="https://aweframework.gitlab.io/awe/docs/schemas/locale.xsd">
  <local name="BUTTON_ACCEPT" value="Aceptar" />
  <local name="CONFIRM_MESSAGE_DELETE" value="Vas a borrar el registro seleccionado. ??Est??s de acuerdo?" />
  <local name="ERROR_MESSAGE_BAD_QUEUE_REQUEST_DEFINITION_FORMAT" value="El formato de la petici??n a la cola {0} es err??neo" />
  ...
</locals>
```



**File Local-FR.xml**
```xml
<locals xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="https://aweframework.gitlab.io/awe/docs/schemas/locale.xsd">
  <local name="BUTTON_ACCEPT" value="Accepter" />
  <local name="CONFIRM_MESSAGE_DELETE" value="Donnees selectionees vont etre effacees. Etes vous d&apos;accord?" />
  <local name="ERROR_MESSAGE_BAD_QUEUE_REQUEST_DEFINITION_FORMAT" value="Le format du message pour l&apos;envoy ?? la queue {0} n&apos;a pas ??t?? d??finie" />
  ...
</locals>
```



**Local example with markdown CDATA used in help text criteria**
```xml
<local name="HELP_SCREEN_TITLE_SIT">
<![CDATA[
Manage application sites. Here you can view, add, delete and modify all application sites.

A site is defined as a *separated application node*, with it's own databases and modules.
]]>
</local>
```