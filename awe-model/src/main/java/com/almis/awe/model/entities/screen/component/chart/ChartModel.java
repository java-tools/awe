package com.almis.awe.model.entities.screen.component.chart;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.screen.component.Component;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamInclude;

import java.util.List;

/**
 * ChartAxis Class
 *
 * Used to parse a chart Axis tag with XStream
 *
 *
 * Generates an Chart widget
 *
 *
 * @author Pablo VIDAL - 21/OCT/2014
 */
@XStreamInclude({Chart.class, ChartAxis.class, ChartLegend.class, ChartSerie.class, ChartTooltip.class})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public abstract class ChartModel extends Component {

  private static final long serialVersionUID = 5791670373535530051L;
  // Chart Axis parameter list
  @XStreamImplicit
  private List<ChartParameter> parameterList;

  /**
   * Default constructor
   */
  public ChartModel() {
  }

  public ChartModel(ChartModel other) throws AWException {
    super(other);
    this.parameterList = ListUtil.copyList(other.parameterList);
  }

  /**
   * Retrieve chart parameter list for axis element
   *
   * @return list with chart parameter list
   */
  @JsonIgnore
  public List<ChartParameter> getParameterList() {
    return parameterList;
  }

  /**
   * Store chart parameter list to element
   *
   * @param parameterList Parameter list
   */
  public void setParameterList(List<ChartParameter> parameterList) {
    this.parameterList = parameterList;
  }

  /**
   * Update node with chart parameters
   *
   * @param model Json node of element
   */
  public void addParameters(ObjectNode model) {
    if (this.getParameterList() != null) {
      for (ChartParameter chartParameter : this.getParameterList()) {
        chartParameter.addParameterModel(model);
      }
    }
  }
}
