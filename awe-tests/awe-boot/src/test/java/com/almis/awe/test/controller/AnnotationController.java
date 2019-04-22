package com.almis.awe.test.controller;

import com.almis.awe.annotation.entities.audit.Audit;
import com.almis.awe.annotation.entities.audit.AuditParams;
import com.almis.awe.annotation.entities.locale.Locale;
import com.almis.awe.annotation.entities.security.Crypto;
import com.almis.awe.annotation.entities.security.Hash;
import com.almis.awe.annotation.entities.session.FromSession;
import com.almis.awe.annotation.entities.session.ToSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by dfuentes on 29/05/2017.
 */
@Controller
@RequestMapping ("/annotations")
@Audit(
  @AuditParams(
    privateMethods = true,
    returnValues = true
  )
)
public class AnnotationController {
  @Hash
  String a = "true";

  //@Crypto(action = Crypto.ActionType.ENCRYPT, password = "abcdefghi1234")
  String b = "askfdnasdakndsa";

  @GetMapping ("/classloadAnnotations")
  public @ResponseBody String getClassLoadAnnotationValues(){
    return "Hash: " + a + "<br>"+ "Crypto: " + b;
  }


  @GetMapping ("/fromSession")
  @FromSession(name = "a")
  public @ResponseBody String getFromSession(@RequestParam String id){
    return id;
  }

  @GetMapping ("/toSession")
  public @ResponseBody String setToSession(
    @ToSession(name = "a")
    @RequestParam
      String id){
    return id;
  }

  @Hash
  @GetMapping ("/hashMethod")
  public @ResponseBody String hashMethod(@RequestParam String txt){
    return txt;
  }

  @GetMapping ("/hashParam")
  private @ResponseBody String hashParam(
    @Hash
    @RequestParam
      String txt){
    return txt;
  }

  @Crypto(action = Crypto.ActionType.ENCRYPT, password = "abcdefghi1234")
  @GetMapping ("/encryptMethod")
  public @ResponseBody String cryptoMethod(@RequestParam String txt){
    return txt;
  }

  @GetMapping ("/encryptParam")
  public @ResponseBody String cryptoParam(
    @Crypto(action = Crypto.ActionType.ENCRYPT, password = "abcdefghi1234")
    @RequestParam
      String txt){
    return txt;
  }

  @Locale
  @GetMapping ("/localeMethod")
  public @ResponseBody String localeMethod(@RequestParam String locale){
    return locale;
  }

  @GetMapping ("/localeParam")
  public @ResponseBody String localeParam(
    @Locale(params = {"Test locale param"})
    @RequestParam
      String locale){
    return locale;
  }
}
