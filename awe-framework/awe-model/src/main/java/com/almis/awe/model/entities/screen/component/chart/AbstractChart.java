package com.almis.awe.model.entities.screen.component.chart;

import com.almis.awe.model.entities.screen.component.Component;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

/**
 * ChartAxis Class
 * <p>
 * Used to parse a chart Axis tag with XStream
 * Generates an Chart widget
 *
 * @author Pablo VIDAL - 21/OCT/2014
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamInclude({Chart.class, ChartAxis.class, ChartLegend.class, ChartSerie.class, ChartTooltip.class})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public abstract class AbstractChart extends Component {

  private static final long serialVersionUID = 5791670373535530051L;

  // Chart Axis parameter list
  @JsonIgnore
  @XStreamImplicit
  private List<ChartParameter> parameterList;

  /**
   * Update node with chart parameters
   *
   * @param model Json node of element
   */
  public void addParameters(Map<String, Object> model) {
    if (this.getParameterList() != null) {
      for (ChartParameter chartParameter : this.getParameterList()) {
        chartParameter.addParameterModel(model);
      }
    }
  }
}
