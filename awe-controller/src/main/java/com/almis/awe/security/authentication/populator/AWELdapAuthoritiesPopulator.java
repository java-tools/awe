package com.almis.awe.security.authentication.populator;

import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.exception.AWException;
import com.almis.awe.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by dfuentes on 03/07/2017.
 */
public class AWELdapAuthoritiesPopulator implements LdapAuthoritiesPopulator {

  @Value("${security.role.prefix:ROLE_}")
  private String rolePrefix;

  // Autowired services
  private QueryService queryService;

  /**
   * Autowired constructor
   * @param queryService
   */
  @Autowired
  public AWELdapAuthoritiesPopulator(QueryService queryService) {
    this.queryService = queryService;
  }

  @Override
  public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData, String username) {

    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

    try {
      ServiceData serviceData = queryService.launchQuery(AweConstants.USER_DETAIL_QUERY);
      DataList dataList = serviceData.getDataList();

      if (dataList.getRows().size() > 0) {
        authorities.add(new SimpleGrantedAuthority(rolePrefix + DataListUtil.getData(dataList, 0, AweConstants.USER_PROFILE)));
      }
    } catch (AWException exc) {
      // TODO: Añadir log
    }

    return authorities;
  }
}
