package com.almis.awe.model.entities.actions;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLNode;
import com.almis.awe.model.type.AnswerType;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Action Class
 *
 * Used to parse the Actions.xml file with XStream
 * This class is used to parse a single action inside the file
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
public class Action implements XMLNode, Copyable {

  private static final long serialVersionUID = 8303124070775783625L;

  // Action id
  @XStreamAsAttribute
  private String id;

  // Action format
  @XStreamAsAttribute
  private String format;

  // Action call list
  @XStreamAlias("call")
  private Call call;

  // Action answer if output is OK
  @XStreamImplicit(itemFieldName = "answer")
  private List<Answer> answers;

  /**
   * Returns an answer given its type
   *
   * @param type answer type
   * @return List of action answers
   * @throws AWException AWE exception
   */
  public Answer getAnswer(AnswerType type) throws AWException {
    return getAnswer(type.toString());
  }

  /**
   * Returns an answer given its type
   *
   * @param type type
   * @return List of action answers
   * @throws AWException AWE exception
   */
  public Answer getAnswer(String type) throws AWException {

    // Variable definition
    Answer out = null;

    // Check if list is not empty
    if (this.getAnswers() != null) {
      for (Answer answer : this.getAnswers()) {
        if (answer.getType().equals(type)) {
          out = answer.copy();
        }
      }
    }

    return out;
  }

  @JsonIgnore
  @Override
  public String getElementKey() {
    return this.getId();
  }

  @Override
  public Action copy() throws AWException {
    return this.toBuilder()
      .call(ListUtil.copyElement(getCall()))
      .answers(ListUtil.copyList(getAnswers()))
      .build();
  }
}
