package com.almis.awe.service;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.service.report.ReportGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * QueryService Class
 *
 * AWE Data Engine
 * Provides generate function to get application data
 *
 * @author Pablo GARCIA
 */
public class ReportService extends ServiceConfig {

  // Autowired services
  private QueryService queryService;
  private MenuService menuService;
  private ReportGenerator reportGenerator;

  @Value("${print.show.options:true}")
  private boolean showPrintOptions;

  /**
   * Autowired constructor
   * @param queryService
   * @param menuService
   * @param reportGenerator
   */
  @Autowired
  public ReportService(QueryService queryService, MenuService menuService, ReportGenerator reportGenerator) {
    this.queryService = queryService;
    this.menuService = menuService;
    this.reportGenerator = reportGenerator;
  }

  /**
   * Retrieve print actions
   * @return Print actions as service data
   * @throws AWException Error retrieving print actions
   */
  public ServiceData getPrintActions() throws AWException {
    ServiceData serviceData;

    // Get print options (based on print.show.options)
    if (showPrintOptions) {
      serviceData = queryService.launchQuery("PrnActAll");
    } else {
      serviceData = queryService.launchQuery("PrnActNotPrn");
    }

    return serviceData;
  }

  /**
   * Print current screen
   * @return Screen print status
   * @throws AWException Error generating reports
   */
  public ServiceData printScreen() throws AWException {
    // Retrieve current screen id
    String screenName = getRequest().getParameterAsString(AweConstants.PRINT_SCREEN);

    // Generate a screen report with the screen
    return reportGenerator.generateScreenReport(menuService.getScreen(screenName));
  }
}
