package com.almis.awe.model.entities.actions;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Answer Class
 * Used to parse the Actions.xml file with XStream
 * This class is used to parse an answer
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
public class Answer implements Copyable {

  private static final long serialVersionUID = -3977488792390703003L;

  // Client action list
  @XStreamImplicit(itemFieldName = "response")
  private List<ClientAction> responseList;

  // Answer target
  @XStreamAlias("target")
  @XStreamAsAttribute
  private String target;

  // Answer type
  @XStreamAlias("type")
  @XStreamAsAttribute
  private String type;

  @Override
  public Answer copy() throws AWException {
    return this.toBuilder()
      .responseList(ListUtil.copyList(getResponseList()))
      .build();
  }
}
