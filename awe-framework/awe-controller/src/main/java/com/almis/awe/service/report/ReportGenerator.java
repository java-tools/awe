package com.almis.awe.service.report;

import com.almis.ade.api.ADE;
import com.almis.ade.api.bean.input.PrintBean;
import com.almis.ade.api.fluid.engine.generic.TemplateExporterBuilderService;
import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.FileData;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.entities.screen.Screen;
import com.almis.awe.model.type.OutputFormatType;
import com.almis.awe.model.util.data.DateUtil;
import com.almis.awe.model.util.data.StringUtil;
import com.almis.awe.model.util.file.FileUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sf.jasperreports.engine.JREmptyDataSource;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Generate the component controllers of the screen
 */
public class ReportGenerator extends ServiceConfig {

  // Autowired services
  private ReportDesigner designer;
  private ADE adeAPI;

  @Value("${settings.dataSuffix:.data}")
  private String dataSuffix;

  @Value("${application.base.path:/}")
  private String applicationBasePath;

  @Value("${application.paths.reports:@reports/}")
  private String reportsPath;

  @Value("${application.paths.reports.historic:@historicReports/}")
  private String historicReportsPath;

  /**
   * Autowired constructor
   *
   * @param reportDesigner Report designer
   * @param adeAPI         ADE API
   */
  @Autowired
  public ReportGenerator(ReportDesigner reportDesigner, ADE adeAPI) {
    this.designer = reportDesigner;
    this.adeAPI = adeAPI;
  }

  /**
   * Generate a report and return client actions to download it
   *
   * @param screen Screen to generate
   * @return Service data with the actions to download the generated reports
   * @throws AWException Error generating report
   */
  public ServiceData generateScreenReport(Screen screen) throws AWException {
    // Get screen parameters
    ObjectNode parameters = getRequest().getParametersSafe();

    // Retrieve print formats
    List<String> printFormats = StringUtil.asList(parameters.get(AweConstants.PRINT_FORMATS));

    // With screen parameters, generate the print bean
    PrintBean printBean = designReport(screen, parameters);

    // Get currentDate
    String currentDate = DateUtil.dat2WebTimestamp(new Date());

    // Generate file name
    String fileName = StringUtil.fixFileName(getLocale(screen.getLabel()) + "_" + currentDate);

    // Llamar a ADE con el bean creado
    TemplateExporterBuilderService builderService = buildReport(printBean, fileName);


    // Generar los formatos que haya definido el usuario y crear las acciones de descarga de los ficheros
    return generateReportFormats(builderService, printFormats, fileName);
  }

  /**
   * Design the report
   *
   * @param screen     Screen to design
   * @param parameters Screen parameters
   * @return Print bean designed
   * @throws AWException Error designing report
   */
  private PrintBean designReport(Screen screen, ObjectNode parameters) throws AWException {
    // Generate report structure
    List<Element> reportStructure = screen.getReportStructure(new ArrayList<Element>(), null, parameters, dataSuffix);

    // Generate print bean
    return designer.getPrintDesign(reportStructure, parameters);
  }

  /**
   * Build report
   *
   * @param printBean Print bean
   * @return Report exporter
   * @throws AWException Error buiding report
   */
  private TemplateExporterBuilderService buildReport(PrintBean printBean, String fileName) throws AWException {
    try {
      // Generate file
      return adeAPI
        .printBean()
        .withJasper()
        .buildAndExport(printBean)
        .withName(fileName)
        .withPath(StringUtil.getAbsolutePath(reportsPath, applicationBasePath))
        .withDataSource(new JREmptyDataSource());
    } catch (Exception exc) {
      throw new AWException(getLocale("ERROR_TITLE_GENERATING_DOCUMENT_DATA"),
        getLocale("ERROR_MESSAGE_GENERATING_DOCUMENT_DATA"), exc);
    }
  }

  /**
   * Generate report formats
   *
   * @param builderService Report builder service
   * @return Service data with output formats
   * @throws AWException Error generating output formats
   */
  private ServiceData generateReportFormats(TemplateExporterBuilderService builderService, List<String> formats, String fileName) throws AWException {
    ServiceData serviceData = new ServiceData();
    String basePath = StringUtil.getAbsolutePath(reportsPath, applicationBasePath);
    for (String format : formats) {
      serviceData.addClientAction(generateReportFormat(builderService, format, fileName, basePath));
    }

    return serviceData;
  }

  /**
   * Generate report format (Async)
   *
   * @param builderService template export builder
   * @param format         format
   * @param fileName       file name
   * @param basePath       base path
   * @return future with generate report action
   * @throws AWException AWE exception
   */
  public ClientAction generateReportFormat(TemplateExporterBuilderService builderService, String format, String fileName, String basePath) throws AWException {
    String mimeType;
    String fullFileName = fileName;

    try {
      switch (OutputFormatType.valueOf(format.toUpperCase())) {
        case XLS:
          builderService.toXls();
          mimeType = AweConstants.APPLICATION_EXCEL;
          fullFileName += ".xls";
          break;
        case XLSX:
          builderService.toXlsx();
          mimeType = AweConstants.APPLICATION_EXCEL;
          fullFileName += ".xlsx";
          break;
        case CSV:
          builderService.toCsv();
          mimeType = AweConstants.APPLICATION_EXCEL;
          fullFileName += ".csv";
          break;
        case RTF:
          builderService.toRtf();
          mimeType = AweConstants.APPLICATION_RTF;
          fullFileName += ".rtf";
          break;
        case DOCX:
          builderService.toDocx();
          mimeType = AweConstants.APPLICATION_WORD;
          fullFileName += ".docx";
          break;
        case TEXT:
          builderService.toText();
          mimeType = MediaType.TEXT_PLAIN_VALUE;
          fullFileName += ".txt";
          break;
        case PDF:
        default:
          builderService.toPDF();
          mimeType = MediaType.APPLICATION_PDF_VALUE;
          fullFileName += ".pdf";
          break;
      }
    } catch (Exception exc) {
      getLogger().log(ReportGenerator.class, Level.ERROR, "Error generating report file ({0}): {1}{2}", exc, format, basePath, fullFileName);
      return new ClientAction("message")
        .addParameter("type", "error")
        .addParameter("title", getLocale("ERROR_TITLE_GENERATING_DOCUMENT"))
        .addParameter("message", "ERROR_MESSAGE_GENERATING_DOCUMENT");
    }

    // Generate file data
    File reportFile = new File(basePath + fullFileName);
    FileData fileData = new FileData(fullFileName, reportFile.length(), mimeType);
    storeHistoricReport(reportFile);
    fileData.setBasePath(basePath);

    // Log report
    getLogger().log(ReportGenerator.class, Level.DEBUG, "Report file ({0}) generated: {1}{2}", mimeType, basePath, fullFileName);

    // Generate client action with file data
    return new ClientAction("get-file")
      .addParameter("filename", FileUtil.fileDataToString(fileData));

  }

  /**
   * Store historic report in historic report path
   *
   * @param reportFile Report to store
   */
  private void storeHistoricReport(File reportFile) {
    // Retrieve file date and database
    Date reportDate = new Date(reportFile.lastModified());
    String database = getSessionDatabase();
    String reportDateFormatted = DateUtil.dat2SqlDateString(reportDate);

    // Generate historic directory
    File historicPath = Paths.get(StringUtil.getAbsolutePath(historicReportsPath, applicationBasePath), reportDateFormatted, Optional.ofNullable(database).orElse("")).toFile();
    try {
      Files.createDirectories(historicPath.toPath());
      Files.copy(reportFile.toPath(), Paths.get(historicPath.getAbsolutePath(), reportFile.getName()));
    } catch (IOException exc) {
      // Log report
      getLogger().log(ReportGenerator.class, Level.ERROR, "Historic report file ({0}) NOT generated on {1}", reportFile.getAbsolutePath(), historicPath.getAbsolutePath(), exc);
    }
  }

  /**
   * Retrieve session database (safely)
   *
   * @return Session database
   */
  private String getSessionDatabase() {
    try {
      return getSession().getParameter(String.class, AweConstants.SESSION_DATABASE);
    } catch (Exception exc) {
      return "";
    }
  }
}