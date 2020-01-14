package com.almis.awe.model.entities.services;

import com.almis.awe.model.util.data.ListUtil;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ServicePK Class
 *
 * Service Key class to make a service call cacheable
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Data
@Accessors(chain = true)
public class ServicePK implements Serializable {

  private static final long serialVersionUID = -662538310201242257L;

  // Service name
  private String name;

  // Variable list
  private transient List<String> variableList = null;

  /**
   * Constructor
   *
   * @param nam Service name
   */
  ServicePK(String nam) {
    this.name = nam;
  }

  /**
   * Constructor
   *
   * @param nam    Service name
   * @param varLst Service variable list
   */
  ServicePK(String nam, List<String> varLst) {
    this.name = nam;
    this.variableList = varLst;
  }

  /**
   * Stores the service input parameter list
   *
   * @param parameterList service input parameter list
   */
  public void setParameterList(List<ServiceInputParameter> parameterList) {

    // Variable definition
    List<String> newVariableList = new ArrayList<>();

    // Convert parameter list
    if (parameterList != null) {
      for (ServiceInputParameter parameter : parameterList) {
        newVariableList.add(parameter.getValue());
      }
    }

    // Assign as arraylist of strings
    this.variableList = newVariableList;
  }

  @Override
  public String toString() {

    // Add service name to the serialized data
    String out = this.getName();

    if (this.getVariableList() != null) {
      out += this.getVariableList().toString().replace("[", "(").replace("]", ")");
    } else {
      out += "()";
    }

    return out;
  }

  private void writeObject(@NonNull ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    ListUtil.writeList(stream, this.variableList);
  }

  private void readObject(@NonNull ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    this.variableList = ListUtil.readList(stream, String.class);
  }
}
