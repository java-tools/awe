package com.almis.awe.model.entities.queues;

import com.almis.awe.model.entities.XMLFile;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Queues Class
 *
 * Used to parse the tag 'queues' in file Queues.xml with XStream
 * This file contains the list of application queues
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
@Data
@Accessors(chain = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamAlias("queues")
public class Queues implements XMLFile {

  private static final long serialVersionUID = -1137206257271832250L;

  // Queue list
  @XStreamImplicit(itemFieldName = "queue")
  private List<Queue> queueList;

  @Override
  public List<Queue> getBaseElementList() {
    return queueList == null ? new ArrayList<>() : queueList;
  }
}
