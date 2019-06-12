package com.almis.awe.builder.screen.component;

import com.almis.awe.builder.enumerates.Expandible;
import com.almis.awe.builder.screen.base.AbstractAttributes;
import com.almis.awe.builder.screen.base.AweBuilder;
import com.almis.awe.model.entities.Element;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ElementAttributes<B extends AweBuilder> extends AbstractAttributes<B> {
  private String label;
  private String title;
  private String style;
  private String type;
  private String help;
  private String helpImage;
  private Expandible expandible;

  public ElementAttributes(B builder) {
    super(builder);
  }

  /**
   * Build attributes in element
   * @param element Element
   * @param <E>
   * @return Element with attributes
   */
  public <E extends Element> E asElement(E element) {
    element
      .setLabel(getLabel())
      .setTitle(getTitle())
      .setStyle(getStyle())
      .setType(getType())
      .setHelp(getHelp())
      .setHelpImage(getHelpImage());

    if (getExpandible() != null) {
      element.setExpand(getExpandible().toString());
    }

    return element;
  }
}
