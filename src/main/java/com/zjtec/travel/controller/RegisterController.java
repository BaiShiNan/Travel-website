package com.zjtec.travel.controller;

import com.zjtec.travel.constant.Const;
import com.zjtec.travel.dao.UserDao;
import com.zjtec.travel.domain.User;
import com.zjtec.travel.service.UserService;
import com.zjtec.travel.service.impl.EmailServiceImpl;
import com.zjtec.travel.vo.ResMsg;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;




@Controller
public class RegisterController {

  private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

  @Autowired
  private UserService userService;

  @Autowired
  public HttpSession session;
  @Autowired
  private UserDao userDao;

  @Autowired
  public EmailServiceImpl emailService;

  @RequestMapping(value = "/signup", method = RequestMethod.POST)
  @ResponseBody
  public ResMsg signup(@RequestBody User ue){
    ResMsg resmsg=new ResMsg();
    String captcha=(String)session.getAttribute(Const.SESSION_KEY_CAPTCHA);
    if(captcha==null || !captcha.equalsIgnoreCase(ue.getCode())){
      resmsg.setErrcode("4");
      resmsg.setErrmsg("验证码不正确");
      return resmsg;
    }
    //TODO:完成注册功能
    if (ue.getTelephone() != null && ue.getUsername() != null && ue.getCode() != null &&
        ue.getBirthday() != null && ue.getSex() != null && ue.getEmail() != null &&
            ue.getPassword() != null && ue.getName() != null) {
      if (!userDao.existUserNameOrEmail(ue.getUsername(), ue.getEmail())) {
        ue.setStatus(Const.USER_STATUS_INACTIVE);
        ue.setCode(RandomStringUtils.random(20,Const.CHARSET_ALPHA));
        ue.setRole(Const.USER_ROLE_MEMBER);//注册默认都是会员角色
        ue.setSalt(RandomStringUtils.random(8, Const.CHARSET_ALPHA));
        String pwdEncrypt = userService.pwdEncrypt(ue.getPassword(), ue.getSalt());
        ue.setPassword(pwdEncrypt);
        if (userDao.save(ue) > 0) {
          resmsg.setErrcode("0");
          resmsg.setErrmsg("注册成功");
          logger.info("激活链接 -> "+String.format("http://localhost:8082/activation?username=%s&code=%s",
                  ue.getUsername(),ue.getCode()));
          emailService.sendEmail(ue.getEmail(),"激活邮件",
                  "激活链接 -> "+String.format("http://localhost:8082/activation?username=%s&code=%s",
                          ue.getUsername(),ue.getCode()));
        } else {
          resmsg.setErrcode("1");
          resmsg.setErrmsg("注册失败");
        }
      } else {
        resmsg.setErrcode("2");
        resmsg.setErrmsg("用户名或Email已存在");
      }
    } else {
      resmsg.setErrcode("3");
      resmsg.setErrmsg("注册表格输入框均不能为空");
    }
    return resmsg;
  }

  // TODO:激活功能
@RequestMapping(value = "/activation")

public String activation(ModelMap map, String username, String code){
  User dbuser = userService.getUserByUsername(username);
  if(username != null && code != null){
    if(userService.activeUser(username, code) && dbuser.getStatus().equals("N")){
      map.put("msg", "激活成功，请点击跳转链接登录");
      map.put("redirect","/login.html");
    }else{
      map.put("msg", "激活失败，点击跳转链接返回首页");
      map.put("redirect","/");
    }
  }else{
    map.put("msg", "激活失败，点击跳转链接返回首页");
    map.put("redirect","/");
  }
  map.put("title","用户激活");
  return "msg";
}

}
