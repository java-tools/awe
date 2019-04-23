package com.almis.awe.security.authentication.encoder;

import com.almis.awe.model.util.security.EncodeUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by dfuentes on 19/06/2017.
 */
public class Ripemd160PasswordEncoder implements PasswordEncoder {
  @Override
  public String encode(CharSequence rawPassword) {
    return EncodeUtil.encodeRipEmd160(rawPassword.toString());
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    return EncodeUtil.encodeRipEmd160(rawPassword.toString()).equalsIgnoreCase(encodedPassword);
  }
}
