/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen;

import com.almis.awe.builder.enumerates.InitialLoad;
import com.almis.awe.builder.enumerates.ServerAction;
import com.almis.awe.builder.screen.accordion.AccordionBuilder;
import com.almis.awe.builder.screen.button.ButtonBuilder;
import com.almis.awe.builder.screen.chart.ChartBuilder;
import com.almis.awe.builder.screen.criteria.CriteriaBuilder;
import com.almis.awe.builder.screen.grid.GridBuilder;
import com.almis.awe.builder.screen.info.InfoBuilder;
import com.almis.awe.builder.screen.info.InfoButtonBuilder;
import com.almis.awe.builder.screen.info.InfoCriteriaBuilder;
import com.almis.awe.builder.screen.tab.TabBuilder;
import com.almis.awe.builder.screen.widget.WidgetBuilder;
import com.almis.awe.builder.screen.wizard.WizardBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.TagList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Build a tag list
 * @author dfuentes
 */
public class TagListBuilder extends AweBuilder<TagListBuilder> {

  private InitialLoad initialLoad;
  private ServerAction serverAction;
  private Boolean autoload, autorefresh;
  private Integer max;
  private String style, targetAction, type;
  private List<AweBuilder> elements;

  /**
   * Constructor
   *
   * @throws AWException Error in construction
   */
  public TagListBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    elements = new ArrayList<>();
  }

  @Override
  public TagListBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    TagList tagList = new TagList();

    tagList.setId(getId());

    if (initialLoad != null) {
      tagList.setInitialLoad(getInitialLoad().toString());
    }

    if (getServerAction() != null) {
      tagList.setServerAction(getServerAction().toString());
    }

    if (isAutoload() != null) {
      tagList.setAutoload(String.valueOf(isAutoload()));
    }

    if (isAutorefresh() != null) {
      tagList.setAutorefresh(String.valueOf(isAutorefresh()));
    }

    if (getMax() != null) {
      tagList.setMax(String.valueOf(getMax()));
    }

    if (getStyle() != null) {
      tagList.setStyle(getStyle());
    }

    if (getTargetAction() != null) {
      tagList.setTargetAction(getTargetAction());
    }

    if (getType() != null) {
      tagList.setType(getType());
    }

    for (AweBuilder aweBuilder : getElementList()) {
      addElement(tagList, aweBuilder.build(tagList));
    }

    return tagList;
  }

  /**
   * Get initial load
   *
   * @return Initial load
   */
  public InitialLoad getInitialLoad() {
    return initialLoad;
  }

  /**
   * Set initial load
   *
   * @param initialLoad Initial load
   * @return this
   */
  public TagListBuilder setInitialLoad(InitialLoad initialLoad) {
    this.initialLoad = initialLoad;
    return this;
  }

  /**
   * Get server action
   *
   * @return Server action
   */
  public ServerAction getServerAction() {
    return serverAction;
  }

  /**
   * Set server Action
   *
   * @param serverAction Server action
   * @return this
   */
  public TagListBuilder setServerAction(ServerAction serverAction) {
    this.serverAction = serverAction;
    return this;
  }

  /**
   * Is Autoload
   *
   * @return Is autoload
   */
  public Boolean isAutoload() {
    return autoload;
  }

  /**
   * Set autoload
   *
   * @param autoload Autoload
   * @return this
   */
  public TagListBuilder setAutoload(Boolean autoload) {
    this.autoload = autoload;
    return this;
  }

  /**
   * Is autorefresh
   *
   * @return Is autorefresh
   */
  public Boolean isAutorefresh() {
    return autorefresh;
  }

  /**
   * Set autorefresh
   *
   * @param autorefresh Autorefresh
   * @return this
   */
  public TagListBuilder setAutorefresh(Boolean autorefresh) {
    this.autorefresh = autorefresh;
    return this;
  }

  /**
   * Get Max
   *
   * @return Max
   */
  public Integer getMax() {
    return max;
  }

  /**
   * Set Max
   *
   * @param max Max
   * @return this
   */
  public TagListBuilder setMax(Integer max) {
    this.max = max;
    return this;
  }

  /**
   * Get style
   *
   * @return Style
   */
  public String getStyle() {
    return style;
  }

  /**
   * Set style
   *
   * @param style Style
   * @return this
   */
  public TagListBuilder setStyle(String style) {
    this.style = style;
    return this;
  }

  /**
   * Get target action
   *
   * @return this
   */
  public String getTargetAction() {
    return targetAction;
  }

  /**
   * Set target action
   *
   * @param targetAction Target action
   * @return this
   */
  public TagListBuilder setTargetAction(String targetAction) {
    this.targetAction = targetAction;
    return this;
  }

  /**
   * Get type
   *
   * @return Type
   */
  public String getType() {
    return type;
  }

  /**
   * Set type
   *
   * @param type Type
   * @return this
   */
  public TagListBuilder setType(String type) {
    this.type = type;
    return this;
  }

  /**
   * Add accordion
   *
   * @param accordion AccordionBuilder list
   * @return this
   */
  public TagListBuilder addAccordion(AccordionBuilder... accordion) {
    if (accordion != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
      this.elements.addAll(Arrays.asList(accordion));
    }
    return this;
  }

  /**
   * Add button
   *
   * @param button ButtonBuilder list
   * @return this
   */
  public TagListBuilder addButton(ButtonBuilder... button) {
    if (button != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
      this.elements.addAll(Arrays.asList(button));
    }
    return this;
  }

  /**
   * Add chart
   *
   * @param chart ChartBuilder list
   * @return this
   */
  public TagListBuilder addChart(ChartBuilder... chart) {
    if (chart != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
      this.elements.addAll(Arrays.asList(chart));
    }
    return this;
  }

  /**
   * Add criteria
   *
   * @param criteria CriteriaBuilder list
   * @return this
   */
  public TagListBuilder addCriteria(CriteriaBuilder... criteria) {
    if (criteria != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
      this.elements.addAll(Arrays.asList(criteria));
    }
    return this;
  }

  /**
   * Add dialog
   *
   * @param dialog DialogBuilder list
   * @return this
   */
  public TagListBuilder addDialog(DialogBuilder... dialog) {
    if (dialog != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
      this.elements.addAll(Arrays.asList(dialog));
    }
    return this;
  }

  /**
   * Add frame
   *
   * @param frame FrameBuilder list
   * @return this
   */
  public TagListBuilder addFrame(FrameBuilder... frame) {
    if (frame != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
      this.elements.addAll(Arrays.asList(frame));
    }
    return this;
  }

  /**
   * Add grid
   * @param grid GridBuilder list
   * @return this
   */
  public TagListBuilder addGrid(GridBuilder... grid) {
    if (grid != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
      this.elements.addAll(Arrays.asList(grid));
    }
    return this;
  }

  /**
   * Add include
   *
   * @param include IncludeBuilder list
   * @return this
   */
  public TagListBuilder addInclude(IncludeBuilder... include) {
    if (include != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
      this.elements.addAll(Arrays.asList(include));
    }
    return this;
  }

  /**
   * Add info
   *
   * @param info InfoBuilder list
   * @return this
   */
  public TagListBuilder addInfo(InfoBuilder... info) {
    if (info != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
      this.elements.addAll(Arrays.asList(info));
    }
    return this;
  }

  /**
   * Add info button
   *
   * @param infoButton InfoButtonBuilder list
   * @return this
   */
  public TagListBuilder addInfoButton(InfoButtonBuilder... infoButton) {
    if (infoButton != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
      this.elements.addAll(Arrays.asList(infoButton));
    }
    return this;
  }

  /**
   * Add info criteria
   *
   * @param infoCriteria InfoCriteriaBuilder list
   * @return this
   */
  public TagListBuilder addInfoCriteria(InfoCriteriaBuilder... infoCriteria) {
    if (infoCriteria != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
      this.elements.addAll(Arrays.asList(infoCriteria));
    }
    return this;
  }

  /**
   * Add window
   *
   * @param windowBuilder WindowBuilder list
   * @return this
   */
  public TagListBuilder addWindow(WindowBuilder... windowBuilder) {
    if (windowBuilder != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
      this.elements.addAll(Arrays.asList(windowBuilder));
    }
    return this;
  }

  /**
   * Add tag list
   *
   * @param tagListBuilder TagListBuilder list
   * @return this
   */
  public TagListBuilder addTagList(TagListBuilder... tagListBuilder) {
    if (tagListBuilder != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
      this.elements.addAll(Arrays.asList(tagListBuilder));
    }
    return this;
  }

  /**
   * Add menu container
   *
   * @param menuContainerBuilder MenuContainerBuilder list
   * @return this
   */
  public TagListBuilder addMenuContainer(MenuContainerBuilder... menuContainerBuilder) {
    if (menuContainerBuilder != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
      this.elements.addAll(Arrays.asList(menuContainerBuilder));
    }
    return this;
  }

  /**
   * Add message
   *
   * @param messageBuilder MessageBuilder list
   * @return this
   */
  public TagListBuilder addMessage(MessageBuilder... messageBuilder) {
    if (messageBuilder != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
      this.elements.addAll(Arrays.asList(messageBuilder));
    }
    return this;
  }

  /**
   * Add pivot table
   *
   * @param pivotTableBuilder PivotTableBuilder list
   * @return this
   */
  public TagListBuilder addPivotTable(PivotTableBuilder... pivotTableBuilder) {
    if (pivotTableBuilder != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
      this.elements.addAll(Arrays.asList(pivotTableBuilder));
    }
    return this;
  }

  /**
   * Add resizable
   *
   * @param resizableBuilder ResizableBuilder list
   * @return this
   */
  public TagListBuilder addResizable(ResizableBuilder... resizableBuilder) {
    if (resizableBuilder != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
      this.elements.addAll(Arrays.asList(resizableBuilder));
    }
    return this;
  }

  /**
   * Add tab
   *
   * @param tabBuilder TabBuilder list
   * @return this
   */
  public TagListBuilder addTab(TabBuilder... tabBuilder) {
    if (tabBuilder != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
      this.elements.addAll(Arrays.asList(tabBuilder));
    }
    return this;
  }

  /**
   * Add view
   *
   * @param viewBuilder ViewBuilder list
   * @return this
   */
  public TagListBuilder addView(ViewBuilder... viewBuilder) {
    if (viewBuilder != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
      this.elements.addAll(Arrays.asList(viewBuilder));
    }
    return this;
  }

  /**
   * Add widget
   *
   * @param widgetBuilder WidgetBuilder list
   * @return this
   */
  public TagListBuilder addWidget(WidgetBuilder... widgetBuilder) {
    if (widgetBuilder != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
      this.elements.addAll(Arrays.asList(widgetBuilder));
    }
    return this;
  }

  /**
   * Add wizard
   *
   * @param wizardBuilder WizardBuilder list
   * @return this
   */
  public TagListBuilder addWizard(WizardBuilder... wizardBuilder) {
    if (wizardBuilder != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
      this.elements.addAll(Arrays.asList(wizardBuilder));
    }
    return this;
  }

  /**
   * Get element list
   *
   * @return Element list
   */
  public List<AweBuilder> getElementList() {
    return this.elements;
  }
}
