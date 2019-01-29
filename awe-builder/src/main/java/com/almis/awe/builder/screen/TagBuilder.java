/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen;

import com.almis.awe.builder.screen.criteria.CriteriaBuilder;
import com.almis.awe.builder.enumerates.Expandible;
import com.almis.awe.builder.enumerates.Source;
import com.almis.awe.builder.screen.info.InfoBuilder;
import com.almis.awe.builder.screen.info.InfoButtonBuilder;
import com.almis.awe.builder.screen.info.InfoCriteriaBuilder;
import com.almis.awe.builder.screen.accordion.AccordionBuilder;
import com.almis.awe.builder.screen.button.ButtonBuilder;
import com.almis.awe.builder.screen.chart.ChartBuilder;
import com.almis.awe.builder.screen.grid.GridBuilder;
import com.almis.awe.builder.screen.tab.TabBuilder;
import com.almis.awe.builder.screen.widget.WidgetBuilder;
import com.almis.awe.builder.screen.wizard.WizardBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.Tag;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author dfuentes
 */
public class TagBuilder extends AweBuilder<TagBuilder> {

  private String source;
  private String label;
  private String style;
  private String type;
  private String text;
  private List<AweBuilder> elements;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public TagBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    this.elements = new ArrayList<>();
  }

  @Override
  public TagBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    Tag tag = new Tag();
    
    tag.setId(getId());

    if (getSource() != null) {
      tag.setSource(getSource());
    }
    if (getExpandible() != null) {
      tag.setExpand(getExpandible().toString());
    }
    if (getLabel() != null) {
      tag.setLabel(getLabel());
    }
    if (getStyle() != null) {
      tag.setStyle(getStyle());
    }
    if (getType() != null) {
      tag.setType(getType());
    }

    if (getText() != null) {
      tag.setValue(getText());
    }

    for (AweBuilder aweBuilder : getElementList()) {
      addElement(tag, aweBuilder.build(tag));
    }

    return tag;
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
  public TagBuilder setSource(String source) {
    this.source = source;
    return this;
  }

  /**
   * Set source
   *
   * @param source
   * @return
   */
  public TagBuilder setSource(Source source) {
    this.source = source.toString();
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
  public TagBuilder setLabel(String label) {
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
  public TagBuilder setStyle(String style) {
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
  public TagBuilder setType(String type) {
    this.type = type;
    return this;
  }

  /**
   * Get text
   *
   * @return
   */
  public String getText() {
    return text;
  }

  /**
   * Set text
   *
   * @param text
   * @return this
   */
  public TagBuilder setText(String text) {
    this.text = text;
    return this;
  }

  /**
   * Add tag
   *
   * @param tag
   * @return
   */
  public TagBuilder addTag(TagBuilder... tag) {
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
  public TagBuilder addAccordion(AccordionBuilder... accordion) {
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
  public TagBuilder addButton(ButtonBuilder... button) {
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
  public TagBuilder addChart(ChartBuilder... chart) {
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
  public TagBuilder addCriteria(CriteriaBuilder... criteria) {
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
  public TagBuilder addDialog(DialogBuilder... dialog) {
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
  public TagBuilder addFrame(FrameBuilder... frame) {
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
  public TagBuilder addGrid(GridBuilder... grid) {
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
  public TagBuilder addInclude(IncludeBuilder... include) {
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
  public TagBuilder addInfo(InfoBuilder... info) {
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
  public TagBuilder addInfoButton(InfoButtonBuilder... infoButton) {
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
  public TagBuilder addInfoCriteria(InfoCriteriaBuilder... infoCriteria) {
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
  public TagBuilder addWindow(WindowBuilder... windowBuilder) {
    if (windowBuilder != null) {
      this.elements.addAll(Arrays.asList(windowBuilder));
    }
    return this;
  }

  /**
   * ADd tag list
   *
   * @param tagListBuilder
   * @return
   */
  public TagBuilder addTagList(TagListBuilder... tagListBuilder) {
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
  public TagBuilder addMenuContainer(MenuContainerBuilder... menuContainerBuilder) {
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
  public TagBuilder addMessage(MessageBuilder... messageBuilder) {
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
  public TagBuilder addPivotTable(PivotTableBuilder... pivotTableBuilder) {
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
  public TagBuilder addResizable(ResizableBuilder... resizableBuilder) {
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
  public TagBuilder addTab(TabBuilder... tabBuilder) {
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
  public TagBuilder addView(ViewBuilder... viewBuilder) {
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
  public TagBuilder addWidget(WidgetBuilder... widgetBuilder) {
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
  public TagBuilder addWizard(WizardBuilder... wizardBuilder) {
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
