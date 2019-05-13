package com.almis.awe.model.entities.services;

import com.almis.awe.exception.AWException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * ServiceOutputParameter Class
 *
 * Used to parse the tag 'response' in file Actions.xml with XStream
 * This file contains the list of system actions
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("response")
public class ServiceOutputParameter extends ServiceParameter {

  @Override
  public ServiceOutputParameter copy() throws AWException {
    return this.toBuilder().build();
  }
}
