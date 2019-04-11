package com.almis.awe.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * User bean class Mapping ope table
 *
 * @author pvidal
 */
@Getter
@Setter
@ToString
@Builder
public class User {

  // User ID (IdeOpe)
  private Integer userID;
  // User name (l1_nom)
  private String userName;
  // User password (l1_pas)
  private String userPassword;
  // Check if the user has ever connected (l1_con)
  private boolean userNotConected;
  // User enabled (l1_act)
  private boolean enabled;
  // User profile (column Pro of AwePro table)
  private String profile;
  // Date of last login (l1_dat)
  private Date lastLogin;
  // Printer name (imp_nom)
  private String printerName;
  // Update date (dat_mod)
  private Date updateDate;
  // Last changed password date (l1_psd)
  private Date lastChangedPasswordDate;
  // User language (l1_lan)
  private String language;
  // Email server (EmlSrv)
  private String emailServer;
  // Email (EmlAdr)
  private String email;
  // User full name (OpeNam)
  private String userFullName;
  // Profile ID (IdePro)
  private Integer profileID;
  // User theme
  private String userTheme;
  // Profile theme
  private String profileTheme;
  // User initial screen
  private String userInitialScreen;
  // Screen init (ScrIni)
  private String profileInitialScreen;
  // User file restriction (res)
  private String userFileRestriction;
  // Profile file restriction (res)
  private String profileFileRestriction;
  // Password lock
  private boolean passwordLocked;
  // Login attempts count (numLog)
  private Integer loginAttempts;
}