package com.almis.awe.builder.screen.base;

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
import com.almis.awe.builder.screen.wizard.WizardBuilder;
import com.almis.awe.model.entities.Element;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *
 * @author dfuentes
 */
@Getter
@Setter
@Accessors(chain = true)
public abstract class AbstractTagBuilder<T, I extends Element> extends AbstractElementBuilder<T, I> {

  /**
   * Add tag
   *
   * @param tag
   * @return
   */
  public T addTag(TagBuilder... tag) {
    addAllElements(tag);
    return (T) this;
  }

  /**
   * Add accordion
   *
   * @param accordion
   * @return
   */
  public T addAccordion(AccordionBuilder... accordion) {
    addAllElements(accordion);
    return (T) this;
  }

  /**
   * Add button
   *
   * @param button
   * @return
   */
  public T addButton(ButtonBuilder... button) {
    addAllElements(button);
    return (T) this;
  }

  /**
   * Add chart
   *
   * @param chart
   * @return
   */
  public T addChart(ChartBuilder... chart) {
    addAllElements(chart);
    return (T) this;
  }

  /**
   * Add criteria
   *
   * @param criteria
   * @return
   */
  public T addCriteria(CriteriaBuilder... criteria) {
    addAllElements(criteria);
    return (T) this;
  }

  /**
   * Add dialog
   *
   * @param dialog
   * @return
   */
  public T addDialog(DialogBuilder... dialog) {
    addAllElements(dialog);
    return (T) this;
  }

  /**
   * Add frame
   *
   * @param frame
   * @return
   */
  public T addFrame(FrameBuilder... frame) {
    addAllElements(frame);
    return (T) this;
  }

  /**
   * Add grid
   *
   * @param grid
   * @return
   */
  public T addGrid(GridBuilder... grid) {
    addAllElements(grid);
    return (T) this;
  }

  /**
   * Add include
   *
   * @param include
   * @return
   */
  public T addInclude(IncludeBuilder... include) {
    addAllElements(include);
    return (T) this;
  }

  /**
   * Add info
   *
   * @param info
   * @return
   */
  public T addInfo(InfoBuilder... info) {
    addAllElements(info);
    return (T) this;
  }

  /**
   * Add info button
   *
   * @param infoButton
   * @return
   */
  public T addInfoButton(InfoButtonBuilder... infoButton) {
    addAllElements(infoButton);
    return (T) this;
  }

  /**
   * Add info criteria
   *
   * @param infoCriteria
   * @return
   */
  public T addInfoCriteria(InfoCriteriaBuilder... infoCriteria) {
    addAllElements(infoCriteria);
    return (T) this;
  }

  /**
   * Add window
   *
   * @param windowBuilder
   * @return
   */
  public T addWindow(WindowBuilder... windowBuilder) {
    addAllElements(windowBuilder);
    return (T) this;
  }

  /**
   * ADd tag list
   *
   * @param tagListBuilder
   * @return
   */
  public T addTagList(TagListBuilder... tagListBuilder) {
    addAllElements(tagListBuilder);
    return (T) this;
  }

  /**
   * Add menu container
   *
   * @param menuContainerBuilder
   * @return
   */
  public T addMenuContainer(MenuContainerBuilder... menuContainerBuilder) {
    addAllElements(menuContainerBuilder);
    return (T) this;
  }

  /**
   * Add message
   *
   * @param messageBuilder
   * @return
   */
  public T addMessage(MessageBuilder... messageBuilder) {
    addAllElements(messageBuilder);
    return (T) this;
  }

  /**
   * Add pivot table
   *
   * @param pivotTableBuilder
   * @return
   */
  public T addPivotTable(PivotTableBuilder... pivotTableBuilder) {
    addAllElements(pivotTableBuilder);
    return (T) this;
  }

  /**
   * Add resizable
   *
   * @param resizableBuilder
   * @return
   */
  public T addResizable(ResizableBuilder... resizableBuilder) {
    addAllElements(resizableBuilder);
    return (T) this;
  }

  /**
   * Add tab
   *
   * @param tabBuilder
   * @return
   */
  public T addTab(TabBuilder... tabBuilder) {
    addAllElements(tabBuilder);
    return (T) this;
  }

  /**
   * Add view
   *
   * @param viewBuilder
   * @return
   */
  public T addView(ViewBuilder... viewBuilder) {
    addAllElements(viewBuilder);
    return (T) this;
  }

  /**
   * Add widget
   *
   * @param widgetBuilder
   * @return
   */
  public T addWidget(WidgetBuilder... widgetBuilder) {
    addAllElements(widgetBuilder);
    return (T) this;
  }

  /**
   * Add wizard
   *
   * @param wizardBuilder
   * @return
   */
  public T addWizard(WizardBuilder... wizardBuilder) {
    addAllElements(wizardBuilder);
    return (T) this;
  }
}
