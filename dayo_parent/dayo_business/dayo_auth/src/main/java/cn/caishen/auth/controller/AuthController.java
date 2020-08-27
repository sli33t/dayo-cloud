package cn.caishen.auth.controller;

import cn.caishen.auth.config.JwtProperties;
import cn.caishen.common.auth.entity.Payload;
import cn.caishen.common.auth.jwtUtils.JwtUtil;
import cn.caishen.common.domain.po.User;
import cn.caishen.common.utils.CookieUtils;
import cn.caishen.common.utils.JSONUtil;
import cn.caishen.common.utils.LbMap;
import cn.caishen.common.utils.MD5Util;
import cn.caishen.serviceinterface.system.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author LB
 */
@RestController
public class AuthController {

    protected static Logger logger = LogManager.getLogger(AuthController.class);

    @DubboReference
    private UserService dayoUserService;

    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping(value = "/loginAuth")
    public LbMap loginAuth(@RequestParam("telNo") String telNo, @RequestParam("password") String password, HttpServletResponse response){
        try {
            User user =  dayoUserService.findByTelNo(telNo);
            if (!user.getPassword().equals(MD5Util.md5(password, telNo))){
                return LbMap.failResult("密码不正确");
            }

            String token = JwtUtil.generateTokenExpireInMinutes(user, jwtProperties.getPrivateKey(), jwtProperties.getUser().getExpire());
            // 写入cookie
            CookieUtils.newCookieBuilder()
                    // response,用于写cookie
                    .response(response)
                    // 保证安全防止XSS攻击，不允许JS操作cookie
                    .httpOnly(true)
                    // 设置domain
                    .domain(jwtProperties.getUser().getCookieDomain())
                    // 设置cookie名称和值
                    .name(jwtProperties.getUser().getCookieName()).value(token)
                    // 写cookie
                    .build();

            LbMap resultMap = LbMap.successResult("用户登录成功", JSONUtil.classToJsonString(user));
            resultMap.put(jwtProperties.getUser().getCookieName(), token);
            return resultMap;
        }catch (Exception e){
            return LbMap.failResult("用户登录异常");
        }
    }


    @PostMapping(value = "/setToken")
    public LbMap setToken(@RequestParam("userStr") String userStr, HttpServletResponse response){
        try {
            User user = JSONUtil.jsonStringToClass(userStr, User.class);
            String token = JwtUtil.generateTokenExpireInMinutes(user, jwtProperties.getPrivateKey(), jwtProperties.getUser().getExpire());
            // 写入cookie
            CookieUtils.newCookieBuilder()
                    // response,用于写cookie
                    .response(response)
                    // 保证安全防止XSS攻击，不允许JS操作cookie
                    .httpOnly(true)
                    // 设置domain
                    .domain(jwtProperties.getUser().getCookieDomain())
                    // 设置cookie名称和值
                    .name(jwtProperties.getUser().getCookieName()).value(token)
                    // 写cookie
                    .build();
            return LbMap.successResult("用户登录成功", user);
        }catch (Exception e){
            return LbMap.failResult("用户登录异常");
        }

    }


    @PostMapping(value = "/getUserByToken")
    public LbMap getUserByToken(@RequestParam("token") String token, HttpServletRequest request){
        try {
            // 读取cookie
            //String token = CookieUtils.getCookieValue(request, jwtProperties.getUser().getCookieName());
            if (token.equals("")){
                logger.info("token令牌为空");
                return LbMap.failResult("令牌不能为空");
            }
            // 获取token信息
            Payload<User> payLoad = JwtUtil.getInfoFromToken(token, jwtProperties.getPublicKey(), User.class);
            User user = payLoad.getUserInfo();
            if (user==null){
                logger.info("用户为空");
                return LbMap.failResult("没有找到用户，请重新登录");
            }
            logger.info("用户获取成功");
            return LbMap.successResult("用户获取成功", JSONUtil.classToJsonString(user));
        } catch (Exception e) {
            // 抛出异常，证明token无效，直接返回401
            return LbMap.failResult("用户获取失败："+e.getMessage());
        }
    }
}
