package com.almis.awe.test.unit.notifier;

import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.notifier.dto.InterestedUsersDto;
import com.almis.awe.notifier.dto.NotificationDto;
import com.almis.awe.notifier.service.NotifierService;
import com.almis.awe.notifier.type.NotificationType;
import com.almis.awe.service.BroadcastService;
import com.almis.awe.service.MaintainService;
import com.almis.awe.service.QueryService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class NotifierServiceTest {

  @InjectMocks
  private NotifierService notifierService;

  @Mock
  private QueryService queryService;

  @Mock
  private MaintainService maintainService;

  @Mock
  private BroadcastService broadcastService;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void getNotifications() throws Exception {
    when(queryService.launchQuery(anyString())).thenReturn(new ServiceData().setDataList(new DataList()));

    // Run
    notifierService.getNotifications();

    // Assert
    verify(queryService, times(1)).launchQuery(anyString());
  }

  @Test
  public void toggleWebSubscriptionInit() throws Exception {
    when(queryService.launchQuery(anyString())).thenReturn(new ServiceData().setDataList(new DataList()));

    // Run
    notifierService.toggleWebSubscription("test", 1);

    // Assert
    verify(queryService, times(1)).launchQuery(anyString());
    verify(maintainService, times(1)).launchMaintain(eq("insert-user-subscription"), any(ObjectNode.class));
  }

  @Test
  public void toggleWebSubscriptionUpdate() throws Exception {
    DataList dataList = new DataList();
    Map<String, CellData> row = new HashMap<>();
    row.put("web", new CellData("1"));
    row.put("email", new CellData("1"));
    dataList.addRow(row);
    when(queryService.launchQuery(anyString())).thenReturn(new ServiceData().setDataList(dataList));

    // Run
    notifierService.toggleWebSubscription("test", 1);

    // Assert
    verify(queryService, times(1)).launchQuery(anyString());
    verify(maintainService, times(1)).launchMaintain(eq("update-user-subscription"), any(ObjectNode.class));
  }

  @Test
  public void toggleEmailSubscriptionInit() throws Exception {
    when(queryService.launchQuery(anyString())).thenReturn(new ServiceData().setDataList(new DataList()));

    // Run
    notifierService.toggleEmailSubscription("test", 1);

    // Assert
    verify(queryService, times(1)).launchQuery(anyString());
    verify(maintainService, times(1)).launchMaintain(eq("insert-user-subscription"), any(ObjectNode.class));
  }

  @Test
  public void toggleEmailSubscriptionUpdate() throws Exception {
    DataList dataList = new DataList();
    Map<String, CellData> row = new HashMap<>();
    row.put("web", new CellData("1"));
    row.put("email", new CellData("1"));
    dataList.addRow(row);
    when(queryService.launchQuery(anyString())).thenReturn(new ServiceData().setDataList(dataList));

    // Run
    notifierService.toggleEmailSubscription("test", 1);

    // Assert
    verify(queryService, times(1)).launchQuery(anyString());
    verify(maintainService, times(1)).launchMaintain(eq("update-user-subscription"), any(ObjectNode.class));
  }

  @Test
  public void testNotify() throws Exception {
    when(queryService.launchPrivateQuery(anyString())).thenReturn(new ServiceData().setDataList(DataListUtil.fromBeanList(Arrays.asList(
      new InterestedUsersDto().setUser("tutu").setByEmail(true).setByWeb(false),
      new InterestedUsersDto().setUser("lala").setByEmail(false).setByWeb(false),
      new InterestedUsersDto().setUser("trololo").setByEmail(true).setByWeb(true),
      new InterestedUsersDto().setUser("lerele").setByEmail(false).setByWeb(true)
    ))));

    // Run
    notifierService.notify(new NotificationDto().setType(NotificationType.NORMAL));

    // Assert
    verify(queryService, times(1)).launchPrivateQuery(anyString());
    verify(broadcastService, times(1)).broadcastMessageToUsers(any(ClientAction.class), anyString(), anyString());
    verify(maintainService, times(1)).launchPrivateMaintain(eq("new-notification"), any(ObjectNode.class));
    verify(maintainService, times(1)).launchPrivateMaintain(eq("notify-email-users"), any(ObjectNode.class));
  }
}
