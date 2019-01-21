/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen;

import com.almis.awe.builder.enumerates.Expandible;
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
import com.almis.awe.model.entities.screen.component.Window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author dfuentes
 */
public class WindowBuilder extends AweBuilder<WindowBuilder> {
  private String icon;
  private String label;
  private String style;
  private Boolean maximize;
  private List<AweBuilder> elements;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public WindowBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    elements = new ArrayList<>();
  }

  @Override
  public WindowBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    Window window = new Window();

    window.setId(getId());

    if (getExpandible() != null) {
      window.setExpand(getExpandible().toString());
    }

    if (getIcon() != null) {
      window.setIcon(getIcon());
    }

    if (getLabel() != null) {
      window.setLabel(getLabel());
    }

    if (getStyle() != null) {
      window.setStyle(getStyle());
    }

    if (isMaximize() != null) {
      window.setMaximize(String.valueOf(isMaximize()));
    }

    for (AweBuilder aweBuilder : getElementList()){
      addElement(window, aweBuilder.build(window));
    }
    
    return window;
  }

  /**
   * Get icon
   *
   * @return
   */
  public String getIcon() {
    return icon;
  }

  /**
   * Set icon
   *
   * @param icon
   * @return
   */
  public WindowBuilder setIcon(String icon) {
    this.icon = icon;
    return this;
  }

  /**
   * Get label
   *
   * @return
   */
  public String getLabel() {
    return label;
  }

  /**
   * Set label
   *
   * @param label
   * @return
   */
  public WindowBuilder setLabel(String label) {
    this.label = label;
    return this;
  }

  /**
   * Get style
   *
   * @return
   */
  public String getStyle() {
    return style;
  }

  /**
   * Set style
   *
   * @param style
   * @return
   */
  public WindowBuilder setStyle(String style) {
    this.style = style;
    return this;
  }

  /**
   * Is maximize
   *
   * @return
   */
  public Boolean isMaximize() {
    return maximize;
  }

  /**
   * Set maximize
   *
   * @param maximize
   * @return
   */
  public WindowBuilder setMaximize(Boolean maximize) {
    this.maximize = maximize;
    return this;
  }

  /**
   * Add tag
   *
   * @param tag
   * @return
   */
  public WindowBuilder addTag(TagBuilder... tag) {
    if (tag != null) {
      this.elements.addAll(Arrays.asList(tag));
    }
    return this;
  }

  /**
   * Add accordion
   *
   * @param accordion
   * @return
   */
  public WindowBuilder addAccordion(AccordionBuilder... accordion) {
    if (accordion != null) {
      this.elements.addAll(Arrays.asList(accordion));
    }
    return this;
  }

  /**
   * Add button
   *
   * @param button
   * @return
   */
  public WindowBuilder addButton(ButtonBuilder... button) {
    if (button != null) {
      this.elements.addAll(Arrays.asList(button));
    }
    return this;
  }

  /**
   * Add chart
   *
   * @param chart
   * @return
   */
  public WindowBuilder addChart(ChartBuilder... chart) {
    if (chart != null) {
      this.elements.addAll(Arrays.asList(chart));
    }
    return this;
  }

  /**
   * Add criteria
   *
   * @param criteria
   * @return
   */
  public WindowBuilder addCriteria(CriteriaBuilder... criteria) {
    if (criteria != null) {
      this.elements.addAll(Arrays.asList(criteria));
    }
    return this;
  }

  /**
   * Add dialog
   *
   * @param dialog
   * @return
   */
  public WindowBuilder addDialog(DialogBuilder... dialog) {
    if (dialog != null) {
      this.elements.addAll(Arrays.asList(dialog));
    }
    return this;
  }

  /**
   * Add frame
   *
   * @param frame
   * @return
   */
  public WindowBuilder addFrame(FrameBuilder... frame) {
    if (frame != null) {
      this.elements.addAll(Arrays.asList(frame));
    }
    return this;
  }

  /**
   * Add grid
   *
   * @param grid
   * @return
   */
  public WindowBuilder addGrid(GridBuilder... grid) {
    if (grid != null) {
      this.elements.addAll(Arrays.asList(grid));
    }
    return this;
  }

  /**
   * Add include
   *
   * @param include
   * @return
   */
  public WindowBuilder addInclude(IncludeBuilder... include) {
    if (include != null) {
      this.elements.addAll(Arrays.asList(include));
    }
    return this;
  }

  /**
   * Add info
   *
   * @param info
   * @return
   */
  public WindowBuilder addInfo(InfoBuilder... info) {
    if (info != null) {
      this.elements.addAll(Arrays.asList(info));
    }
    return this;
  }

  /**
   * Add info button
   *
   * @param infoButton
   * @return
   */
  public WindowBuilder addInfoButton(InfoButtonBuilder... infoButton) {
    if (infoButton != null) {
      this.elements.addAll(Arrays.asList(infoButton));
    }
    return this;
  }

  /**
   * Add info criteria
   *
   * @param infoCriteria
   * @return
   */
  public WindowBuilder addInfoCriteria(InfoCriteriaBuilder... infoCriteria) {
    if (infoCriteria != null) {
      this.elements.addAll(Arrays.asList(infoCriteria));
    }
    return this;
  }

  /**
   * Add window
   *
   * @param windowBuilder
   * @return
   */
  public WindowBuilder addWindow(WindowBuilder... windowBuilder) {
    if (windowBuilder != null) {
      this.elements.addAll(Arrays.asList(windowBuilder));
    }
    return this;
  }

  /**
   * Add tag list
   *
   * @param tagListBuilder
   * @return
   */
  public WindowBuilder addTagList(TagListBuilder... tagListBuilder) {
    if (tagListBuilder != null) {
      this.elements.addAll(Arrays.asList(tagListBuilder));
    }
    return this;
  }

  /**
   * Add menu container
   *
   * @param menuContainerBuilder
   * @return
   */
  public WindowBuilder addMenuContainer(MenuContainerBuilder... menuContainerBuilder) {
    if (menuContainerBuilder != null) {
      this.elements.addAll(Arrays.asList(menuContainerBuilder));
    }
    return this;
  }

  /**
   * Add message
   *
   * @param messageBuilder
   * @return
   */
  public WindowBuilder addMessage(MessageBuilder... messageBuilder) {
    if (messageBuilder != null) {
      this.elements.addAll(Arrays.asList(messageBuilder));
    }
    return this;
  }

  /**
   * Add pivot table
   *
   * @param pivotTableBuilder
   * @return
   */
  public WindowBuilder addPivotTable(PivotTableBuilder... pivotTableBuilder) {
    if (pivotTableBuilder != null) {
      this.elements.addAll(Arrays.asList(pivotTableBuilder));
    }
    return this;
  }

  /**
   * Add resizable
   *
   * @param resizableBuilder
   * @return
   */
  public WindowBuilder addResizable(ResizableBuilder... resizableBuilder) {
    if (resizableBuilder != null) {
      this.elements.addAll(Arrays.asList(resizableBuilder));
    }
    return this;
  }

  /**
   * Add tab
   *
   * @param tabBuilder
   * @return
   */
  public WindowBuilder addTab(TabBuilder... tabBuilder) {
    if (tabBuilder != null) {
      this.elements.addAll(Arrays.asList(tabBuilder));
    }
    return this;
  }

  /**
   * Add view
   *
   * @param viewBuilder
   * @return
   */
  public WindowBuilder addView(ViewBuilder... viewBuilder) {
    if (viewBuilder != null) {
      this.elements.addAll(Arrays.asList(viewBuilder));
    }
    return this;
  }

  /**
   * Add widget
   *
   * @param widgetBuilder
   * @return
   */
  public WindowBuilder addWidget(WidgetBuilder... widgetBuilder) {
    if (widgetBuilder != null) {
      this.elements.addAll(Arrays.asList(widgetBuilder));
    }
    return this;
  }

  /**
   * Add wizard
   *
   * @param wizardBuilder
   * @return
   */
  public WindowBuilder addWizard(WizardBuilder... wizardBuilder) {
    if (wizardBuilder != null) {
      this.elements.addAll(Arrays.asList(wizardBuilder));
    }
    return this;
  }

  /**
   * Get element list
   *
   * @return
   */
  public List<AweBuilder> getElementList() {
    return this.elements;
  }
}
