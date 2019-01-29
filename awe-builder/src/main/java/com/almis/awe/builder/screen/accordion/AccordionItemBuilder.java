/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen.accordion;

import com.almis.awe.builder.screen.*;
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
import com.almis.awe.model.entities.screen.component.container.AccordionItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author dfuentes
 */
public class AccordionItemBuilder extends AweBuilder<AccordionItemBuilder> {

  private String label, style;
  private List<AweBuilder> elements;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public AccordionItemBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    elements = new ArrayList<>();
  }

  @Override
  public AccordionItemBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    AccordionItem accordionItem = new AccordionItem();

    accordionItem.setId(getId());

    if (getLabel() != null) {
      accordionItem.setLabel(getLabel());
    }

    if (getStyle() != null) {
      accordionItem.setStyle(getStyle());
    }

    for (AweBuilder aweBuilder : getElementList()) {
      addElement(accordionItem, aweBuilder.build(accordionItem));
    }

    return accordionItem;
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
  public AccordionItemBuilder setLabel(String label) {
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
  public AccordionItemBuilder setStyle(String style) {
    this.style = style;
    return this;
  }

  /**
   * Add tag
   *
   * @param tag
   * @return
   */
  public AccordionItemBuilder addTag(TagBuilder... tag) {
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
  public AccordionItemBuilder addAccordion(AccordionBuilder... accordion) {
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
  public AccordionItemBuilder addButton(ButtonBuilder... button) {
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
  public AccordionItemBuilder addChart(ChartBuilder... chart) {
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
  public AccordionItemBuilder addCriteria(CriteriaBuilder... criteria) {
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
  public AccordionItemBuilder addDialog(DialogBuilder... dialog) {
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
  public AccordionItemBuilder addFrame(FrameBuilder... frame) {
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
  public AccordionItemBuilder addGrid(GridBuilder... grid) {
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
  public AccordionItemBuilder addInclude(IncludeBuilder... include) {
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
  public AccordionItemBuilder addInfo(InfoBuilder... info) {
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
  public AccordionItemBuilder addInfoButton(InfoButtonBuilder... infoButton) {
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
  public AccordionItemBuilder addInfoCriteria(InfoCriteriaBuilder... infoCriteria) {
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
  public AccordionItemBuilder addWindow(WindowBuilder... windowBuilder) {
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
  public AccordionItemBuilder addTagList(TagListBuilder... tagListBuilder) {
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
  public AccordionItemBuilder addMenuContainer(MenuContainerBuilder... menuContainerBuilder) {
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
  public AccordionItemBuilder addMessage(MessageBuilder... messageBuilder) {
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
  public AccordionItemBuilder addPivotTable(PivotTableBuilder... pivotTableBuilder) {
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
  public AccordionItemBuilder addResizable(ResizableBuilder... resizableBuilder) {
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
  public AccordionItemBuilder addTab(TabBuilder... tabBuilder) {
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
  public AccordionItemBuilder addView(ViewBuilder... viewBuilder) {
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
  public AccordionItemBuilder addWidget(WidgetBuilder... widgetBuilder) {
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
  public AccordionItemBuilder addWizard(WizardBuilder... wizardBuilder) {
    if (wizardBuilder != null) {
      this.elements.addAll(Arrays.asList(wizardBuilder));
    }
    return this;
  }

  /**
   * get element list
   *
   * @return
   */
  public List<AweBuilder> getElementList() {
    return this.elements;
  }
}
