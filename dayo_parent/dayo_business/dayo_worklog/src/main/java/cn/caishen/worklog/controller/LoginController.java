package cn.caishen.worklog.controller;

import cn.caishen.common.domain.po.User;
import cn.caishen.common.domain.vo.UserVo;
import cn.caishen.common.utils.CookieUtils;
import cn.caishen.common.utils.JSONUtil;
import cn.caishen.common.utils.LbMap;
import cn.caishen.serviceinterface.auth.AuthService;
import cn.caishen.worklog.config.RabbitProperties;
import cn.caishen.worklog.constant.MQConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录控制
 * @author LB
 */
@RestController
public class LoginController {

    protected static Logger logger = LogManager.getLogger(LoginController.class);

    @Autowired
    private RabbitProperties rabbitProperties;

    @DubboReference(lazy = true)
    private AuthService dayoAuthService;

    /**
     * 跳转登录页面
     * @return
     */
    @GetMapping(value = "/")
    public ModelAndView toLogin(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/login");
        return mv;
    }

    @PostMapping(value = "/login")
    public LbMap login(@RequestParam("telNo") String telNo, @RequestParam("password") String password, HttpServletRequest request, HttpServletResponse response){
        try {
            UserVo user = dayoAuthService.loginAuth(telNo, password);
            if (user!=null){
                logger.info("登录成功："+user.toString());

                LbMap rabbitMap = new LbMap();

                String rbHost = rabbitProperties.getHost();
                Integer rbPort = rabbitProperties.getPort();
                String rbUsername = rabbitProperties.getUsername();
                String rbPassword = rabbitProperties.getPassword();
                String rbVirtualHost = rabbitProperties.getVirtualhost();

                rabbitMap.put("rbHost", rbHost);
                rabbitMap.put("rbPort", rbPort);
                rabbitMap.put("rbUsername", rbUsername);
                rabbitMap.put("rbPassword", rbPassword);
                rabbitMap.put("rbVirtualHost", rbVirtualHost);

                HttpSession session = request.getSession();
                session.setAttribute(MQConstant.RABBIT_MQ_SETTING, rabbitMap.toString());
                session.setAttribute("user", user);

                CookieUtils.newCookieBuilder()
                        // response,用于写cookie
                        .response(response)
                        // 保证安全防止XSS攻击，不允许JS操作cookie
                        .httpOnly(true)
                        // 设置domain
                        .domain(user.getCookieDomain())
                        // 设置cookie名称和值
                        .name(user.getCookieName()).value(user.getDayoToken())
                        // 写cookie
                        .build();

            }else {
                LbMap.failResult("登录失败，未找到用户信息");
            }

            LbMap map = LbMap.successResult("登录成功");
            map.put("user", user);
            map.put("dayoToken", user.getDayoToken());
            return map;
        }catch (Exception e){
            logger.info("登录失败："+e.getMessage());
            return LbMap.failResult("登录失败："+e.getMessage());
        }
    }


    /*@PostMapping(value = "/login")
    public LbMap login(String telNo, String password, HttpServletRequest request) throws Exception {
        LbMap lbMap = dayoAuth2Service.loginAuth2(telNo, password, "password", "myapp", "all", "lxapp");
        return lbMap;
    }*/

    /**
     * 登出方法
     * @param request
     * @return
     */
    @GetMapping(value = "/logout")
    public ModelAndView logout(HttpServletRequest request){
        ModelAndView mv = new ModelAndView();
        /*HttpSession session = request.getSession();
        session.removeAttribute("user");*/
        mv.setViewName("system/login");
        return mv;
    }


}
