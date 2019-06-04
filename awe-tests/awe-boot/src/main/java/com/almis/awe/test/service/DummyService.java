package com.almis.awe.test.service;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.exception.AWException;
import com.almis.awe.service.data.builder.DataListBuilder;
import com.almis.awe.service.data.builder.EmailBuilder;
import com.almis.awe.model.type.AnswerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Dummy Service class to test the queries that call services
 * 
 * @author jbellon
 *
 */
@Service
public class DummyService extends ServiceConfig {

  // Autowired services
  private WebApplicationContext context;

  /**
   * Autowired constructor
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
    out.setData(new String[] { "a", "b", "c" });

    return out;
  }

  /**
   * Returns a simple String[] asking for two parameters (string)
   * 
   * @return String[]
   */
  public ServiceData returnStringArrayTwoStringParams(String name, List<String> fields) {
    ServiceData out = new ServiceData();
    out.setData(new String[] { name, fields.toString().replaceAll("[\\[\\]\\s]", "") });

    return out;
  }

  /**
   * Returns a simple String[] asking for a parameter (number)
   * 
   * @return String[]
   */
  public ServiceData returnStringArrayNumberParam(Integer value) {
    ServiceData out = new ServiceData();
    out.setData(new String[] { String.valueOf(value) });

    return out;
  }

  /**
   * Returns a simple String[] asking for a parameter (number)
   * 
   * @return String[]
   */
  public ServiceData returnStringArrayLongParam(Long value) {
    ServiceData out = new ServiceData();
    out.setData(new String[] { String.valueOf(value) });

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
    out.setData(new String[] { String.valueOf(value) });

    return out;
  }

  /**
   * Returns a simple String[] asking for a parameter (boolean)
   * 
   * @return String[]
   */
  public ServiceData returnStringArrayBooleanParam(Boolean value) {
    ServiceData out = new ServiceData();
    out.setData(new String[] { String.valueOf(value) });

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
      ((EmailBuilder) context.getBean(EmailBuilder.class)).setFrom(new InternetAddress("david.fuentes@almis.com"))
        .addTo(new InternetAddress("dfuentes.almis@gmail.com"))
        .addReplyTo(new InternetAddress("david.fuentes.other@almis.com"))
        .addCc(new InternetAddress("dovixman@gmail.com"))
        .addCco(new InternetAddress("dovixmancosas@gmail.com"))
        .setSubject("Test message")
        .setBody("<div style='background-color:red;'>Test div message</div>")
        .addAttachment("FileName.test", new File("C:\\Users\\dfuentes\\Pictures\\Saved Pictures\\tst.jpg"))
        .sendMail(true);
    } catch (AddressException e) {
      e.printStackTrace();
    } catch (AWException e) {
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
}
