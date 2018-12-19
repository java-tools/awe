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
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Generate the component controllers of the screen
 */
public class ReportGenerator extends ServiceConfig {

  // Autowired services
  private ReportDesigner designer;
  private ADE adeAPI;
  private FileUtil fileUtil;

  @Value("${settings.dataSuffix:.data}")
  private String dataSuffix;

  @Value("${application.base.path:/}")
  private String applicationBasePath;

  @Value("${application.paths.reports:@reports/}")
  private String reportsPath;

  /**
   * Autowired constructor
   * @param reportDesigner Report designer
   * @param adeAPI ADE API
   * @param fileUtil File util
   */
  @Autowired
  public ReportGenerator(ReportDesigner reportDesigner, ADE adeAPI, FileUtil fileUtil) {
    this.designer = reportDesigner;
    this.adeAPI = adeAPI;
    this.fileUtil = fileUtil;
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
    List<Future<ClientAction>> resultList = new ArrayList<>();
    for (String format : formats) {
      resultList.add(generateReportFormat(builderService, format, fileName, serviceData));
    }

    // Retrieve results
    for (Future<ClientAction> result : resultList) {
      while (true) {
        if (result.isDone()) {
          try {
            serviceData.addClientAction(result.get());
          } catch (Exception exc) {
            serviceData.addClientAction(new ClientAction("message")
                    .addParameter(AweConstants.ACTION_MESSAGE_TYPE_ATTRIBUTE, "error")
                    .addParameter(AweConstants.ACTION_MESSAGE_TITLE_ATTRIBUTE, getLocale("ERROR_TITLE_GENERATING_DOCUMENT"))
                    .addParameter(AweConstants.ACTION_MESSAGE_DESCRIPTION_ATTRIBUTE, exc.getMessage()));
          }
          break;
        }
      }
    }

    return serviceData;
  }

  /**
   * Generate report format
   *
   * @param builderService
   * @param format
   * @param fileName
   * @param serviceData
   * @return 
   * @throws com.almis.awe.exception.AWException 
   */
  @Async("threadPoolTaskExecutor")
  public Future<ClientAction> generateReportFormat(TemplateExporterBuilderService builderService, String format, String fileName, ServiceData serviceData) throws AWException {
    String mimeType;
    String basePath = StringUtil.getAbsolutePath(reportsPath, applicationBasePath);
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
      throw new AWException(getLocale("ERROR_TITLE_GENERATING_DOCUMENT"),
              getLocale("ERROR_MESSAGE_GENERATING_DOCUMENT"), exc);
    }

    // Generate file data
    FileData fileData = new FileData(fullFileName, new File(basePath + fullFileName).length(), mimeType);
    fileData.setBasePath(basePath);

    // Log report
    getLogger().log(ReportGenerator.class, Level.DEBUG, "Report file ({0}) generated: {1}{2}", mimeType, basePath, fullFileName);

    // Generate client action with file data
    return new AsyncResult<>(new ClientAction("get-file")
            .addParameter("filename", fileUtil.fileDataToString(fileData)));

  }
}