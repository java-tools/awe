package com.almis.awe.security.authentication.encoder;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.log.LogUtil;
import com.almis.awe.model.util.security.EncodeUtil;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by dfuentes on 19/06/2017.
 */
public class Sha512PasswordEncoder implements PasswordEncoder {

  @Autowired
  private LogUtil logger;

  @Override
  public String encode(CharSequence rawPassword) {
    try {
      return EncodeUtil.hash(EncodeUtil.HashingAlgorithms.SHA_512, rawPassword.toString());
    } catch (AWException e) {
      logger.log(this.getClass(), Level.ERROR, "Error authenticating, could not hash given password");
      return rawPassword.toString();
    }
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    try {
      return EncodeUtil.hash(EncodeUtil.HashingAlgorithms.SHA_256, rawPassword.toString()).equalsIgnoreCase(encodedPassword);
    } catch (AWException e) {
      logger.log(this.getClass(), Level.ERROR, "Error authenticating, could not hash given password");
    }
    return false;
  }
}
