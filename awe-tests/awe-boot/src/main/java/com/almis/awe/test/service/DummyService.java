package com.almis.awe.test.service;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.dto.SortColumn;
import com.almis.awe.model.entities.email.ParsedEmail;
import com.almis.awe.model.type.AnswerType;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.service.EmailService;
import com.almis.awe.service.data.builder.DataListBuilder;
import com.almis.awe.test.bean.Planet;
import com.almis.awe.test.bean.Planets;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Thread.sleep;

/**
 * Dummy Service class to test the queries that call services
 *
 * @author jbellon
 */
@Log4j2
@Service
public class DummyService extends ServiceConfig {

  // Autowired services
  private WebApplicationContext context;
  private Random random = new Random();

  /**
   * Autowired constructor
   *
   * @param context Context
   */
  @Autowired
  public DummyService(WebApplicationContext context) {
    this.context = context;
  }

  /**
   * Returns a list of values to test pagination
   *
   * @return
   * @throws AWException
   */
  public ServiceData paginate() throws AWException {
    ServiceData out = new ServiceData();
    String[] data = new String[65];
    for (int i = 0; i < data.length; i++) {
      data[i] = i + "";
    }

    DataListBuilder builder = context.getBean(DataListBuilder.class);
    out.setDataList(builder.setServiceQueryResult(data).build());

    return out;
  }

  /**
   * Returns a list of values to test pagination
   *
   * @return
   * @throws AWException
   */
  public ServiceData paginate(Long page, Long max) throws AWException {
    ServiceData out = new ServiceData();
    String[] data = new String[65];
    List<String> subset = new ArrayList<String>();
    for (int i = 0; i < data.length; i++) {
      data[i] = i + "";
    }

    int offset = (int) ((page - 1) * max);
    for (int i = offset; i < offset + max; i++) {
      subset.add(data[i]);
    }

    DataListBuilder builder = context.getBean(DataListBuilder.class);
    out.setDataList(
      builder.setServiceQueryResult(subset.toArray(new String[subset.size()]))
        .setRecords((long) data.length)
        .setPage(page)
        .setMax(max)
        .build());

    return out;
  }

  /**
   * Returns a simple DataList without post processing
   *
   * @return DataList
   * @throws AWException
   */
  public ServiceData getDummyUnprocessedData() throws AWException {
    ServiceData out = new ServiceData();
    String[] data = new String[]{"Toyota", null, "Mercedes", null, "BMW", "Volkswagen", "Skoda"};

    DataListBuilder builder = context.getBean(DataListBuilder.class);
    DataList dataList = builder.setServiceQueryResult(data).build();
    DataListUtil.sort(dataList, Collections.singletonList(new SortColumn("value", "asc")), true);
    out.setDataList(dataList);

    return out;
  }

  /**
   * Returns a simple DataList asking for no parameters
   *
   * @return DataList
   * @throws AWException
   */
  public ServiceData returnDatalistNoParams() throws AWException {
    ServiceData out = new ServiceData();
    String[] data = new String[3];
    for (int i = 0; i < data.length; i++) {
      data[i] = i + "";
    }

    DataListBuilder builder = context.getBean(DataListBuilder.class);
    out.setDataList(builder.setServiceQueryResult(data).build());

    return out;
  }

  /**
   * Returns a simple String[] asking for no parameters
   *
   * @return String[]
   */
  public ServiceData returnStringArrayNoParams() {
    ServiceData out = new ServiceData();
    out.setData(new String[]{"a", "b", "c"});

    return out;
  }

  /**
   * Returns a simple String[] asking for two parameters (string)
   *
   * @return String[]
   */
  public ServiceData returnStringArrayTwoStringParams(String name, List<String> fields) {
    ServiceData out = new ServiceData();
    out.setData(new String[]{name, fields.toString().replaceAll("[\\[\\]\\s]", "")});

    return out;
  }

  /**
   * Returns a simple String[] asking for a parameter (number)
   *
   * @return String[]
   */
  public ServiceData returnStringArrayNumberParam(Integer value) {
    ServiceData out = new ServiceData();
    out.setData(new String[]{String.valueOf(value)});

    return out;
  }

  /**
   * Returns a simple String[] asking for a parameter (number)
   *
   * @return String[]
   */
  public ServiceData returnStringArrayLongParam(Long value) {
    ServiceData out = new ServiceData();
    out.setData(new String[]{String.valueOf(value)});

    return out;
  }

  /**
   * Returns a simple String[] asking for a parameter (number)
   *
   * @return String[]
   */
  public ServiceData returnStringArrayDoubleParam(Double value) {
    ServiceData out = new ServiceData();
    out.setData(new String[]{String.valueOf(value)});
    return out;
  }

  /**
   * Returns a simple String[] asking for a parameter (number)
   *
   * @return String[]
   */
  public ServiceData returnStringArrayFloatParam(Float value) {
    ServiceData out = new ServiceData();
    out.setData(new String[]{String.valueOf(value)});

    return out;
  }

  /**
   * Returns a simple String[] asking for a parameter (boolean)
   *
   * @return String[]
   */
  public ServiceData returnStringArrayBooleanParam(Boolean value) {
    ServiceData out = new ServiceData();
    out.setData(new String[]{String.valueOf(value)});

    return out;
  }

  /**
   * Pretends to answer an OK from a maintain
   *
   * @return ServiceData
   */
  public ServiceData returnMaintainOkNoParams() {
    ServiceData out = new ServiceData();

    out.setType(AnswerType.OK);
    out.setTitle("Operation successful");
    out.setMessage("The selected maintain operation has been successfully performed");

    return out;
  }

  /**
   * Pretends to answer an OK from a maintain
   *
   * @return ServiceData
   */
  public ServiceData returnMaintainOkMessageParam(String message) {
    ServiceData out = new ServiceData();

    out.setType(AnswerType.OK);
    out.setTitle("Operation successful");
    out.setMessage(message);

    return out;
  }

  /**
   * Pretends to answer an OK from a maintain
   *
   * @return ServiceData
   */
  public ServiceData returnMaintainOkTitleMessageParam(String title, String message) {
    ServiceData out = new ServiceData();

    out.setType(AnswerType.OK);
    out.setTitle(title);
    out.setMessage(message);

    return out;
  }

  public ServiceData sendMail() {
    ServiceData out = new ServiceData();
    try {
      ParsedEmail email = new ParsedEmail()
        .setFrom(new InternetAddress("david.fuentes@almis.com"))
        .setTo(Arrays.asList(new InternetAddress("dfuentes.almis@gmail.com")))
        .setReplyTo(Arrays.asList(new InternetAddress("david.fuentes.other@almis.com")))
        .setCc(Arrays.asList(new InternetAddress("dovixman@gmail.com")))
        .setCco(Arrays.asList(new InternetAddress("dovixmancosas@gmail.com")))
        .setSubject("Test message")
        .setBody("<div style='background-color:red;'>Test div message</div>")
        .addAttachment("FileName.test", new File("C:\\Users\\dfuentes\\Pictures\\Saved Pictures\\tst.jpg"));
      getBean(EmailService.class).sendEmail(email);
    } catch (AddressException e) {
      e.printStackTrace();
    }
    return out;
  }

  /**
   * Returns the system date
   *
   * @return Service Data
   */
  public ServiceData getDate() {

    ServiceData serviceData = new ServiceData();
    try {
      // Generate date
      DateFormat df = new SimpleDateFormat("mm/dd/yyyy HH:mm:ss.S");
      Date date = df.parse("23/10/1978 15:06:23.232");

      // Get system version
      serviceData.setDataList(new DataList());
      DataListUtil.addColumnWithOneRow(serviceData.getDataList(), "value", date);
    } catch (Exception exc) {
    }

    return serviceData;
  }

  /**
   * Returns the system date
   *
   * @return Service Data
   */
  public ServiceData getDateList() {

    ServiceData serviceData = new ServiceData();
    try {
      // Generate date
      List<Date> dates = new ArrayList<>();
      DateFormat df = new SimpleDateFormat("mm/dd/yyyy HH:mm:ss.S");
      dates.add(df.parse("23/10/1978 15:06:23.232"));
      dates.add(df.parse("11/02/2015 03:30:12.123"));
      dates.add(df.parse("01/08/2020 13:26:55.111"));

      // Get system version
      serviceData.setDataList(new DataList());
      DataListUtil.addColumn(serviceData.getDataList(), "date1", dates);
      DataListUtil.addColumn(serviceData.getDataList(), "date2", dates);
      DataListUtil.addColumn(serviceData.getDataList(), "date3", dates);
      DataListUtil.addColumn(serviceData.getDataList(), "date4", dates);
      DataListUtil.addColumn(serviceData.getDataList(), "date5", dates);
      DataListUtil.addColumn(serviceData.getDataList(), "date6", dates);
      DataListUtil.addColumn(serviceData.getDataList(), "date7", dates);
    } catch (Exception exc) {
    }

    return serviceData;
  }

  /**
   * Returns the system date
   *
   * @return Service Data
   */
  public ServiceData getElapsedTimeList() {

    ServiceData serviceData = new ServiceData();

    // Get system version
    serviceData.setDataList(new DataList());
    DataListUtil.addColumn(serviceData.getDataList(), "ms1", Collections.singletonList(1200 * 24 * 60 * 60 * 1000L));
    DataListUtil.addColumn(serviceData.getDataList(), "ms2", Collections.singletonList(400 * 24 * 60 * 60 * 1000L));
    DataListUtil.addColumn(serviceData.getDataList(), "ms3", Collections.singletonList(70 * 24 * 60 * 60 * 1000L));
    DataListUtil.addColumn(serviceData.getDataList(), "ms4", Collections.singletonList(35 * 24 * 60 * 60 * 1000L));
    DataListUtil.addColumn(serviceData.getDataList(), "ms5", Collections.singletonList(18 * 24 * 60 * 60 * 1000L));
    DataListUtil.addColumn(serviceData.getDataList(), "ms6", Collections.singletonList(9 * 24 * 60 * 60 * 1000L));
    DataListUtil.addColumn(serviceData.getDataList(), "ms7", Collections.singletonList(3 * 24 * 60 * 60 * 1000L));
    DataListUtil.addColumn(serviceData.getDataList(), "ms8", Collections.singletonList(8 * 60 * 60 * 1000L));
    DataListUtil.addColumn(serviceData.getDataList(), "ms9", Collections.singletonList(5 * 60 * 1000L));
    DataListUtil.addColumn(serviceData.getDataList(), "ms10", Collections.singletonList(7 * 1000L));
    DataListUtil.addColumn(serviceData.getDataList(), "ms11", Collections.singletonList(222L));

    // Get calendar 3 years ago
    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    calendar.add(Calendar.YEAR, -3);
    DataListUtil.addColumn(serviceData.getDataList(), "dateSince", Collections.singletonList(calendar.getTime()));

    return serviceData;
  }

  /**
   * Retrieve dummy data
   *
   * @param planet Planet bean
   * @return Service data
   */
  public ServiceData getDummyData(Planet planet) {
    return new ServiceData();
  }

  /**
   * Retrieve dummy data
   *
   * @param planet Planet bean
   * @return Service data
   */
  public ServiceData getDummyData(JsonNode planet) {
    return new ServiceData()
      .setTitle("tutu")
      .setMessage("lala");
  }

  /**
   * Retrieve dummy data
   *
   * @param planetList Planet bean list
   * @return Service data
   */
  public ServiceData getDummyData(List<Planet> planetList) {
    return new ServiceData();
  }

  /**
   * Retrieve dummy data
   *
   * @param planets Planets bean
   * @return Service data
   */
  public ServiceData getDummyData(Planets planets) {
    return new ServiceData();
  }

  /**
   * Wait some seconds and retrieve a screen data
   *
   * @return
   */
  public ServiceData waitSomeSeconds(Integer seconds) throws AWException {
    try {
      int secondsToWait = random.nextInt(4) - 2 + seconds;
      logger.info("Waiting {} seconds", secondsToWait);
      sleep(secondsToWait * 1000L);
      logger.info("Waiting finished!");
    } catch (Exception exc) {
      throw new AWException("Interrupted thread exception", exc);
    }
    return new ServiceData();
  }

  /**
   * Do nothing
   *
   * @return
   */
  public ServiceData doNothing() {
    logger.info("Launching a test service");
    return new ServiceData();
  }
}
