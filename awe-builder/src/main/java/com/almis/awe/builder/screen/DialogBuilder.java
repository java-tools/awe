/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen;

import com.almis.awe.builder.enumerates.Expandible;
import com.almis.awe.builder.enumerates.OnClose;
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
import com.almis.awe.model.entities.screen.component.Dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author dfuentes
 */
public class DialogBuilder extends AweBuilder<DialogBuilder> {

  private List<AweBuilder> elements;
  private Expandible expandible;
  private OnClose onClose;
  private Boolean modal;
  private String help, helpImage;
  private String icon, label, source, style, type;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public DialogBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    elements = new ArrayList<>();
  }

  @Override
  public DialogBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    Dialog dialog = new Dialog();

    dialog.setId(getId());

    if (getExpandible() != null) {
      dialog.setExpand(getExpandible().toString());
    }

    if (getOnClose() != null) {
      dialog.setOnClose(getOnClose().toString());
    }

    if (isModal() != null) {
      //TODO check where to add this value
    }

    if (getHelp() != null) {
      dialog.setHelp(getHelp());
    }

    if (getHelpImage() != null) {
      dialog.setHelpImage(getHelpImage());
    }

    if (getIcon() != null) {
      dialog.setIcon(getIcon());
    }

    if (getLabel() != null) {
      dialog.setLabel(getLabel());
    }

    if (getSource() != null) {
      dialog.setSource(getSource());
    }

    if (getStyle() != null) {
      dialog.setStyle(getStyle());
    }

    if (getType() != null) {
      dialog.setType(getType());
    }

    for (AweBuilder aweBuilder : getElementList()) {
      addElement(dialog, aweBuilder.build(dialog));
    }

    return dialog;
  }

  /**
   * Get on close
   *
   * @return
   */
  public OnClose getOnClose() {
    return onClose;
  }

  /**
   * Set on close
   *
   * @param onClose
   * @return
   */
  public DialogBuilder setOnClose(OnClose onClose) {
    this.onClose = onClose;
    return this;
  }

  /**
   * Is modal
   *
   * @return
   */
  public Boolean isModal() {
    return modal;
  }

  /**
   * Set modal
   *
   * @param modal
   * @return
   */
  public DialogBuilder setModal(Boolean modal) {
    this.modal = modal;
    return this;
  }

  /**
   * Set help
   *
   * @return
   */
  public String getHelp() {
    return help;
  }

  /**
   * Set help
   *
   * @param help
   * @return
   */
  public DialogBuilder setHelp(String help) {
    this.help = help;
    return this;
  }

  /**
   * Get help image
   *
   * @return
   */
  public String getHelpImage() {
    return helpImage;
  }

  /**
   * Set help image
   *
   * @param helpImage
   * @return
   */
  public DialogBuilder setHelpImage(String helpImage) {
    this.helpImage = helpImage;
    return this;
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
  public DialogBuilder setIcon(String icon) {
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
  public DialogBuilder setLabel(String label) {
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
  public DialogBuilder setSource(String source) {
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
  public DialogBuilder setStyle(String style) {
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
  public DialogBuilder setType(String type) {
    this.type = type;
    return this;
  }

  /**
   * Add tag
   *
   * @param tag
   * @return
   */
  public DialogBuilder addTag(TagBuilder... tag) {
    if (tag != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
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
  public DialogBuilder addAccordion(AccordionBuilder... accordion) {
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
   * @param button
   * @return
   */
  public DialogBuilder addButton(ButtonBuilder... button) {
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
   * @param chart
   * @return
   */
  public DialogBuilder addChart(ChartBuilder... chart) {
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
   * @param criteria
   * @return
   */
  public DialogBuilder addCriteria(CriteriaBuilder... criteria) {
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
   * @param dialog
   * @return
   */
  public DialogBuilder addDialog(DialogBuilder... dialog) {
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
   * @param frame
   * @return
   */
  public DialogBuilder addFrame(FrameBuilder... frame) {
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
   *
   * @param grid
   * @return
   */
  public DialogBuilder addGrid(GridBuilder... grid) {
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
   * @param include
   * @return
   */
  public DialogBuilder addInclude(IncludeBuilder... include) {
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
   * @param info
   * @return
   */
  public DialogBuilder addInfo(InfoBuilder... info) {
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
   * @param infoButton
   * @return
   */
  public DialogBuilder addInfoButton(InfoButtonBuilder... infoButton) {
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
   * @param infoCriteria
   * @return
   */
  public DialogBuilder addInfoCriteria(InfoCriteriaBuilder... infoCriteria) {
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
   * @param windowBuilder
   * @return
   */
  public DialogBuilder addWindow(WindowBuilder... windowBuilder) {
    if (windowBuilder != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
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
  public DialogBuilder addTagList(TagListBuilder... tagListBuilder) {
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
   * @param menuContainerBuilder
   * @return
   */
  public DialogBuilder addMenuContainer(MenuContainerBuilder... menuContainerBuilder) {
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
   * @param messageBuilder
   * @return
   */
  public DialogBuilder addMessage(MessageBuilder... messageBuilder) {
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
   * @param pivotTableBuilder
   * @return
   */
  public DialogBuilder addPivotTable(PivotTableBuilder... pivotTableBuilder) {
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
   * @param resizableBuilder
   * @return
   */
  public DialogBuilder addResizable(ResizableBuilder... resizableBuilder) {
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
   * @param tabBuilder
   * @return
   */
  public DialogBuilder addTab(TabBuilder... tabBuilder) {
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
   * @param viewBuilder
   * @return
   */
  public DialogBuilder addView(ViewBuilder... viewBuilder) {
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
   * @param widgetBuilder
   * @return
   */
  public DialogBuilder addWidget(WidgetBuilder... widgetBuilder) {
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
   * @param wizardBuilder
   * @return
   */
  public DialogBuilder addWizard(WizardBuilder... wizardBuilder) {
    if (wizardBuilder != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
      this.elements.addAll(Arrays.asList(wizardBuilder));
    }
    return this;
  }


  /**
   * Get elements
   *
   * @return
   */
  public List<AweBuilder> getElementList() {
    return elements;
  }
}
