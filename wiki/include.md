### Almis Web Engine - [Basic Development Guide](basic-developer-guide.md) - **[Screen development](basic-screen-development.md)**

---

# **Include**

## Table of Contents

* **[Introduction](#introduction)**
* **[XML skeleton](#xml-skeleton)**
* **[Attributes](#attributes)**
* **[Examples](#examples)**

## Introduction
An `include` is a component which allows you to pick a screen piece and put it inside another screen. It's very useful to reuse a piece of code between screens.

## XML skeleton

```xml
<include target-screen="[screen-to-include]" target-source="[source-to-pick]" />
```

## Attributes

| Attribute     | Use          | Type   |  Description                                                                    |
| ------------- | ------------ | ------ | ------------------------------------------------------------------------------- |
| target-screen | **Required** | String | [Screen](screen.md) to retrieve the code fragment from                                       |
| target-source | **Required** | String | Screen [source](tags.md) to pick up the code fragment from                         |

## Examples

**Insert the print options dialog into a screen**
```xml
<include target-screen="PrnOpt" target-source="center" />
```

**Insert the screen help dialog into a screen**
```xml
<include target-screen="screen-help" target-source="center"/>
```