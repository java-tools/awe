package com.almis.awe.test;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.type.AnswerType;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validation test service
 * @author pgarcia
 */
@Service
public class Validation extends ServiceConfig {

  // PATTERNS
  private static final String BANK_ACOUNT = "^[0-9]{4}\\-[0-9]{4}\\-[0-9]{2}\\-[0-9]{10}$";
  private static final Pattern BANK_ACCOUNT_PATTERN = Pattern.compile(BANK_ACOUNT, 0);

  /**
   * Validate bank account
   * @param bankAccount Bank account
   * @return Validation
   * @throws AWException Error validating
   */
  public ServiceData validateBankAccount(String bankAccount) throws AWException {
    ServiceData check = new ServiceData();
    Matcher matchAccount = BANK_ACCOUNT_PATTERN.matcher(bankAccount);
    if (!matchAccount.matches()) {
      check.setType(AnswerType.WARNING);
      check.setMessage(getElements().getLocale("ERROR_MESSAGE_WRONG_BANK_ACCOUNT", bankAccount));
    }
    return check;
  }
}
