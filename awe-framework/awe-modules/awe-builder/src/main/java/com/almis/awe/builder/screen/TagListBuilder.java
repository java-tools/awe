/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen;

import com.almis.awe.builder.screen.accordion.AccordionBuilder;
import com.almis.awe.builder.screen.button.ButtonBuilder;
import com.almis.awe.builder.screen.chart.ChartBuilder;
import com.almis.awe.builder.screen.base.AbstractComponentBuilder;
import com.almis.awe.builder.screen.criteria.CriteriaBuilder;
import com.almis.awe.builder.screen.grid.GridBuilder;
import com.almis.awe.builder.screen.info.InfoBuilder;
import com.almis.awe.builder.screen.info.InfoButtonBuilder;
import com.almis.awe.builder.screen.info.InfoCriteriaBuilder;
import com.almis.awe.builder.screen.tab.TabBuilder;
import com.almis.awe.builder.screen.widget.WidgetBuilder;
import com.almis.awe.builder.screen.wizard.WizardBuilder;
import com.almis.awe.model.entities.screen.component.TagList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Build a tag list
 * @author dfuentes
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class TagListBuilder extends AbstractComponentBuilder<TagListBuilder, TagList> {

  @Override
  public TagList build() {
    return build(new TagList());
  }

  /**
   * Add tag
   *
   * @param tag
   * @return
   */
  public TagListBuilder addTag(TagBuilder... tag) {
    addAllElements(tag);
    return this;
  }

  /**
   * Add accordion
   *
   * @param accordion
   * @return
   */
  public TagListBuilder addAccordion(AccordionBuilder... accordion) {
    addAllElements(accordion);
    return this;
  }

  /**
   * Add button
   *
   * @param button
   * @return
   */
  public TagListBuilder addButton(ButtonBuilder... button) {
    addAllElements(button);
    return this;
  }

  /**
   * Add chart
   *
   * @param chart
   * @return
   */
  public TagListBuilder addChart(ChartBuilder... chart) {
    addAllElements(chart);
    return this;
  }

  /**
   * Add criteria
   *
   * @param criteria
   * @return
   */
  public TagListBuilder addCriteria(CriteriaBuilder... criteria) {
    addAllElements(criteria);
    return this;
  }

  /**
   * Add dialog
   *
   * @param dialog
   * @return
   */
  public TagListBuilder addDialog(DialogBuilder... dialog) {
    addAllElements(dialog);
    return this;
  }

  /**
   * Add frame
   *
   * @param frame
   * @return
   */
  public TagListBuilder addFrame(FrameBuilder... frame) {
    addAllElements(frame);
    return this;
  }

  /**
   * Add grid
   *
   * @param grid
   * @return
   */
  public TagListBuilder addGrid(GridBuilder... grid) {
    addAllElements(grid);
    return this;
  }

  /**
   * Add include
   *
   * @param include
   * @return
   */
  public TagListBuilder addInclude(IncludeBuilder... include) {
    addAllElements(include);
    return this;
  }

  /**
   * Add info
   *
   * @param info
   * @return
   */
  public TagListBuilder addInfo(InfoBuilder... info) {
    addAllElements(info);
    return this;
  }

  /**
   * Add info button
   *
   * @param infoButton
   * @return
   */
  public TagListBuilder addInfoButton(InfoButtonBuilder... infoButton) {
    addAllElements(infoButton);
    return this;
  }

  /**
   * Add info criteria
   *
   * @param infoCriteria
   * @return
   */
  public TagListBuilder addInfoCriteria(InfoCriteriaBuilder... infoCriteria) {
    addAllElements(infoCriteria);
    return this;
  }

  /**
   * Add window
   *
   * @param windowBuilder
   * @return
   */
  public TagListBuilder addWindow(WindowBuilder... windowBuilder) {
    addAllElements(windowBuilder);
    return this;
  }

  /**
   * ADd tag list
   *
   * @param tagListBuilder
   * @return
   */
  public TagListBuilder addTagList(TagListBuilder... tagListBuilder) {
    addAllElements(tagListBuilder);
    return this;
  }

  /**
   * Add menu container
   *
   * @param menuContainerBuilder
   * @return
   */
  public TagListBuilder addMenuContainer(MenuContainerBuilder... menuContainerBuilder) {
    addAllElements(menuContainerBuilder);
    return this;
  }

  /**
   * Add message
   *
   * @param messageBuilder
   * @return
   */
  public TagListBuilder addMessage(MessageBuilder... messageBuilder) {
    addAllElements(messageBuilder);
    return this;
  }

  /**
   * Add pivot table
   *
   * @param pivotTableBuilder
   * @return
   */
  public TagListBuilder addPivotTable(PivotTableBuilder... pivotTableBuilder) {
    addAllElements(pivotTableBuilder);
    return this;
  }

  /**
   * Add resizable
   *
   * @param resizableBuilder
   * @return
   */
  public TagListBuilder addResizable(ResizableBuilder... resizableBuilder) {
    addAllElements(resizableBuilder);
    return this;
  }

  /**
   * Add tab
   *
   * @param tabBuilder
   * @return
   */
  public TagListBuilder addTab(TabBuilder... tabBuilder) {
    addAllElements(tabBuilder);
    return this;
  }

  /**
   * Add view
   *
   * @param viewBuilder
   * @return
   */
  public TagListBuilder addView(ViewBuilder... viewBuilder) {
    addAllElements(viewBuilder);
    return this;
  }

  /**
   * Add widget
   *
   * @param widgetBuilder
   * @return
   */
  public TagListBuilder addWidget(WidgetBuilder... widgetBuilder) {
    addAllElements(widgetBuilder);
    return this;
  }

  /**
   * Add wizard
   *
   * @param wizardBuilder
   * @return
   */
  public TagListBuilder addWizard(WizardBuilder... wizardBuilder) {
    addAllElements(wizardBuilder);
    return this;
  }
}
