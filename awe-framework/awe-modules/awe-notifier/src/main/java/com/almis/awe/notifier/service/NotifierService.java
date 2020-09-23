package com.almis.awe.notifier.service;

import com.almis.awe.builder.client.FilterActionBuilder;
import com.almis.awe.builder.client.ScreenActionBuilder;
import com.almis.awe.builder.client.UpdateControllerActionBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.actions.ComponentAddress;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.notifier.dto.InterestedUsersDto;
import com.almis.awe.notifier.dto.NotificationDto;
import com.almis.awe.service.BroadcastService;
import com.almis.awe.service.MaintainService;
import com.almis.awe.service.QueryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.stream.Collectors;

public class NotifierService {

  // Queries
  private static final String USER_NOTIFICATIONS = "user-notifications";
  private static final String GET_SUBSCRIPTION = "get-subscription";
  private static final String GET_NOTIFICATION = "get-notification";
  private static final String INSERT_SUBSCRIPTION = "insert-user-subscription";
  private static final String UPDATE_SUBSCRIPTION = "update-user-subscription";
  private static final String INSERT_NOTIFICATION = "new-notification";
  private static final String INTERESTED_USERS = "get-interested-users";
  private static final String NOTIFY_EMAIL_USERS = "notify-email-users";

  // Constants
  private static final String SUBSCRIPTION = "subscription";
  private static final String EMAIL = "email";
  private static final String USER = "user";
  private static final String WEB = "web";

  // Autowired services
  private final QueryService queryService;
  private final MaintainService maintainService;
  private final BroadcastService broadcastService;

  /**
   * Autowired constructor
   *
   * @param queryService     Query service
   * @param maintainService  Maintain service
   * @param broadcastService Broadcast service
   */
  public NotifierService(QueryService queryService, MaintainService maintainService, BroadcastService broadcastService) {
    this.queryService = queryService;
    this.maintainService = maintainService;
    this.broadcastService = broadcastService;
  }

  /**
   * Get notifications and update notification bell unit
   *
   * @return Notifications
   */
  public ServiceData getNotifications() throws AWException {
    ServiceData serviceData = queryService.launchQuery(USER_NOTIFICATIONS);

    // Add client action to update bell controller
    return serviceData.addClientAction(new UpdateControllerActionBuilder("notifications", "unit", serviceData.getDataList().getRecords()).build());
  }

  /**
   * Toggle web subscription
   *
   * @param user         User name
   * @param subscription Subscription
   * @return Service data
   * @throws AWException
   */
  public ServiceData toggleWebSubscription(String user, Integer subscription) throws AWException {
    DataList dataList = queryService.launchQuery(GET_SUBSCRIPTION).getDataList();
    ObjectNode parameters = JsonNodeFactory.instance.objectNode();
    if (dataList.getRows().isEmpty()) {
      // Set insert parameters
      parameters.put(SUBSCRIPTION, subscription);
      parameters.put(USER, user);
      parameters.put(WEB, 1);
      parameters.put(EMAIL, 0);
      return maintainService.launchMaintain(INSERT_SUBSCRIPTION, parameters);
    } else {
      // Set update parameters
      parameters.put(SUBSCRIPTION, subscription);
      parameters.put(USER, DataListUtil.getData(dataList, 0, USER));
      parameters.put(WEB, (Integer.parseInt(DataListUtil.getData(dataList, 0, WEB)) + 1) % 2);
      parameters.put(EMAIL, DataListUtil.getData(dataList, 0, EMAIL));
      return maintainService.launchMaintain(UPDATE_SUBSCRIPTION, parameters);
    }
  }

  /**
   * Toggle email subscription
   *
   * @param user         User name
   * @param subscription Subscription
   * @return Service data
   * @throws AWException
   */
  public ServiceData toggleEmailSubscription(String user, Integer subscription) throws AWException {
    DataList dataList = queryService.launchQuery(GET_SUBSCRIPTION).getDataList();
    ObjectNode parameters = JsonNodeFactory.instance.objectNode();
    parameters.put(SUBSCRIPTION, subscription);
    if (dataList.getRows().isEmpty()) {
      // Set insert parameters
      parameters.put(USER, user);
      parameters.put(WEB, 0);
      parameters.put(EMAIL, 1);
      return maintainService.launchMaintain(INSERT_SUBSCRIPTION, parameters);
    } else {
      // Set update parameters
      parameters.put(USER, DataListUtil.getData(dataList, 0, USER));
      parameters.put(WEB, DataListUtil.getData(dataList, 0, WEB));
      parameters.put(EMAIL, (Integer.parseInt(DataListUtil.getData(dataList, 0, EMAIL)) + 1) % 2);
      return maintainService.launchMaintain(UPDATE_SUBSCRIPTION, parameters);
    }
  }

  /**
   * Go to notification screen
   *
   * @param notificationId Notification id
   * @return Service data
   */
  public ServiceData goToNotificationScreen(Integer notificationId) throws AWException {
    DataList dataList = queryService.launchQuery(GET_NOTIFICATION, JsonNodeFactory.instance.objectNode().put("notification", notificationId)).getDataList();
    List<NotificationDto> notificationDtoList = DataListUtil.asBeanList(dataList, NotificationDto.class);
    return new ServiceData().addClientAction(new ScreenActionBuilder(notificationDtoList.get(0).getScreen(), true).setContext("screen/private/home").setAsync(true).setSilent(true).build());
  }

  /**
   * Send a notification
   *
   * @param notification Notification to send
   */
  public void notify(NotificationDto notification) throws AWException {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode parameters = mapper.valueToTree(notification);

    // Store notification
    maintainService.launchPrivateMaintain(INSERT_NOTIFICATION, parameters);

    // Get users interested in notification
    DataList dataList = queryService.launchPrivateQuery(INTERESTED_USERS, parameters).getDataList();
    List<InterestedUsersDto> userList = DataListUtil.asBeanList(dataList, InterestedUsersDto.class);

    // Refresh connected users
    broadcastService.broadcastMessageToUsers(
      new FilterActionBuilder(new ComponentAddress().setComponent("notification-bulletin").setView("base")).build(),
      userList.stream().filter(InterestedUsersDto::isByWeb).map(InterestedUsersDto::getUser).map(String::trim).toArray(String[]::new));

    // Send email notifications
    List<String> interestedEmailUsers = userList.stream().filter(InterestedUsersDto::isByEmail).map(InterestedUsersDto::getUser).map(String::trim).collect(Collectors.toList());
    if (!interestedEmailUsers.isEmpty()) {
      parameters.set("UsrPrn", mapper.valueToTree(interestedEmailUsers));
      maintainService.launchPrivateMaintain(NOTIFY_EMAIL_USERS, parameters);
    }
  }
}
