package com.almis.awe.test.unit.session;

import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.component.AweRequest;
import com.almis.awe.model.component.AweSession;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.dto.User;
import com.almis.awe.model.entities.Global;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.tracker.AweClientTracker;
import com.almis.awe.model.tracker.AweConnectionTracker;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.service.BroadcastService;
import com.almis.awe.service.QueryService;
import com.almis.awe.session.AweSessionDetails;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.almis.awe.model.constant.AweConstants.SESSION_FAILURE;
import static com.almis.awe.model.constant.AweConstants.SESSION_USER_DETAILS;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AweSessionDetailsTest {

  @InjectMocks
  private AweSessionDetails aweSessionDetails;

  @Mock
  private Authentication authentication;

  @Mock
  private AuthenticationException authenticationException;

  @Mock
  private ApplicationContext applicationContext;

  @Mock
  private AweSession aweSession;

  @Mock
  private AweRequest aweRequest;

  @Mock
  private AweElements aweElements;

  @Mock
  private AweClientTracker clientTracker;

  @Mock
  private AweConnectionTracker connectionTracker;

  @Mock
  private User userDetails;

  @Mock
  private QueryService queryService;

  @Mock
  private BroadcastService broadcastService;

  @Before
  public void setUp() {
    aweSessionDetails.setApplicationContext(applicationContext);
    when(applicationContext.getBean(AweSession.class)).thenReturn(aweSession);
    when(applicationContext.getBean(AweRequest.class)).thenReturn(aweRequest);
    when(applicationContext.getBean(AweElements.class)).thenReturn(aweElements);
    when(aweSession.setAuthentication(any())).thenReturn(aweSession);
    when(aweSession.getUser()).thenReturn("user");
    when(aweSession.getSessionId()).thenReturn("session-id");
    when(aweSession.getParameter(eq(User.class), eq(SESSION_USER_DETAILS))).thenReturn(userDetails);
    when(userDetails.getLanguage()).thenReturn("ES");
    when(userDetails.getUserTheme()).thenReturn("sunset");
    when(userDetails.getProfileTheme()).thenReturn("sky");

    ReflectionTestUtils.setField(aweSessionDetails, "sessionParameters", Arrays.asList("module", null, "site", "", "database"));
  }

  @Test
  public void onLoginSuccess() throws Exception {
    when(queryService.launchQuery(eq(null), anyString(), anyString())).thenReturn(new ServiceData().setDataList(DataListUtil.fromBeanList(Collections.singletonList(new Global()))));
    aweSessionDetails.onLoginSuccess(authentication);
    verify(aweSession, times(12)).setParameter(anyString(), any());
  }

  @Test
  public void onLoginSuccessErrorSessionParameters() {
    aweSessionDetails.onLoginSuccess(authentication);
    verify(aweSession, times(3)).setParameter(eq(SESSION_FAILURE), any());
  }

  @Test
  public void onLoginSuccessNullDataList() throws Exception {
    when(queryService.launchQuery(eq(null), anyString(), anyString())).thenReturn(new ServiceData());
    aweSessionDetails.onLoginSuccess(authentication);
    verify(aweSession, times(9)).setParameter(anyString(), any());
  }

  @Test
  public void onLoginFailure() {
    aweSessionDetails.onLoginFailure(authenticationException);
    verify(aweSession, times(1)).setParameter(eq(SESSION_FAILURE), any());
  }

  @Test
  public void onLoginFailureUsernameNotFound() {
    aweSessionDetails.onLoginFailure(mock(UsernameNotFoundException.class));
    verify(aweSession, times(1)).setParameter(eq(SESSION_FAILURE), any());
  }

  @Test
  public void onLoginFailureBadCredentials() {
    aweSessionDetails.onLoginFailure(mock(BadCredentialsException.class));
    verify(aweSession, times(1)).setParameter(eq(SESSION_FAILURE), any());
  }

  @Test
  public void onLogoutSuccess() {
    when(connectionTracker.getUserConnectionsFromSession(anyString(), anyString())).thenReturn(Collections.emptySet());
    aweSessionDetails.onLogoutSuccess();
    verify(aweSession, times(10)).removeParameter(anyString());
  }

  @Test
  public void onLogoutSuccessCloseSessions() {
    when(aweRequest.getToken()).thenReturn("3");
    when(connectionTracker.getUserConnectionsFromSession(anyString(), anyString())).thenReturn(Stream.of("1", "2", "3", null, "", "5").collect(Collectors.toSet()));
    aweSessionDetails.onLogoutSuccess();
    verify(aweSession, times(10)).removeParameter(anyString());
    verify(broadcastService, times(3)).broadcastMessageToUID(anyString(), any(ClientAction.class));
  }
}