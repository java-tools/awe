/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen.wizard;

import com.almis.awe.builder.screen.*;
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
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.container.WizardPanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author dfuentes
 */
public class WizardPanelBuilder extends AweBuilder<WizardPanelBuilder> {

  private String label;
  private String source;
  private String style;
  private String type;
  private List<AweBuilder> elements;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public WizardPanelBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    elements = new ArrayList<>();
  }

  @Override
  public WizardPanelBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    WizardPanel wizardPanel = new WizardPanel();

    wizardPanel.setId(getId());

    if (getExpandible() != null) {
      wizardPanel.setExpand(getExpandible().toString());
    }

    if (getLabel() != null) {
      wizardPanel.setLabel(getLabel());
    }

    if (getSource() != null) {
      wizardPanel.setSource(getSource());
    }

    if (getStyle() != null) {
      wizardPanel.setStyle(getStyle());
    }

    if (getType() != null) {
      wizardPanel.setType(getType());
    }

    for (AweBuilder aweBuilder : getElementList()) {
      addElement(wizardPanel, aweBuilder.build(wizardPanel));
    }

    return wizardPanel;
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
  public WizardPanelBuilder setLabel(String label) {
    this.label = label;
    return this;
  }

  /**
   * Get source
   *
   * @return
   */
  public String getSource() {
    return source;
  }

  /**
   * Set source
   *
   * @param source
   * @return
   */
  public WizardPanelBuilder setSource(String source) {
    this.source = source;
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
  public WizardPanelBuilder setStyle(String style) {
    this.style = style;
    return this;
  }

  /**
   * Get type
   *
   * @return
   */
  public String getType() {
    return type;
  }

  /**
   * Set type
   *
   * @param type
   * @return
   */
  public WizardPanelBuilder setType(String type) {
    this.type = type;
    return this;
  }

  /**
   * Add tag
   *
   * @param tag
   * @return
   */
  public WizardPanelBuilder addTag(TagBuilder... tag) {
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
  public WizardPanelBuilder addAccordion(AccordionBuilder... accordion) {
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
  public WizardPanelBuilder addButton(ButtonBuilder... button) {
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
  public WizardPanelBuilder addChart(ChartBuilder... chart) {
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
  public WizardPanelBuilder addCriteria(CriteriaBuilder... criteria) {
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
  public WizardPanelBuilder addDialog(DialogBuilder... dialog) {
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
  public WizardPanelBuilder addFrame(FrameBuilder... frame) {
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
  public WizardPanelBuilder addGrid(GridBuilder... grid) {
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
  public WizardPanelBuilder addInclude(IncludeBuilder... include) {
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
  public WizardPanelBuilder addInfo(InfoBuilder... info) {
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
  public WizardPanelBuilder addInfoButton(InfoButtonBuilder... infoButton) {
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
  public WizardPanelBuilder addInfoCriteria(InfoCriteriaBuilder... infoCriteria) {
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
  public WizardPanelBuilder addWindow(WindowBuilder... windowBuilder) {
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
  public WizardPanelBuilder addTagList(TagListBuilder... tagListBuilder) {
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
  public WizardPanelBuilder addMenuContainer(MenuContainerBuilder... menuContainerBuilder) {
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
  public WizardPanelBuilder addMessage(MessageBuilder... messageBuilder) {
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
  public WizardPanelBuilder addPivotTable(PivotTableBuilder... pivotTableBuilder) {
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
  public WizardPanelBuilder addResizable(ResizableBuilder... resizableBuilder) {
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
  public WizardPanelBuilder addTab(TabBuilder... tabBuilder) {
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
  public WizardPanelBuilder addView(ViewBuilder... viewBuilder) {
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
  public WizardPanelBuilder addWidget(WidgetBuilder... widgetBuilder) {
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
  public WizardPanelBuilder addWizard(WizardBuilder... wizardBuilder) {
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
