package cn.caishen.auth.service.impl;

import cn.caishen.auth.config.JwtProperties;
import cn.caishen.common.auth.entity.Payload;
import cn.caishen.common.auth.jwtUtils.JwtUtil;
import cn.caishen.common.domain.po.User;
import cn.caishen.common.domain.vo.UserVo;
import cn.caishen.common.utils.CookieUtils;
import cn.caishen.common.utils.JSONUtil;
import cn.caishen.common.utils.LbMap;
import cn.caishen.common.utils.MD5Util;
import cn.caishen.serviceinterface.auth.AuthService;
import cn.caishen.serviceinterface.system.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author LB
 */
@DubboService
@Slf4j
public class AuthServiceImpl implements AuthService {

    @DubboReference(lazy = true)
    private UserService dayoUserService;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     * @param telNo 电话号码
     * @param password 明文密码
     * @return 登录是否成功
     */
    @Override
    public UserVo loginAuth(String telNo, String password) {
        try {
            User user =  dayoUserService.findByTelNo(telNo);
            if (!user.getPassword().equals(MD5Util.md5(password, telNo))){
                return new UserVo();
            }

            String token = JwtUtil.generateTokenExpireInMinutes(user, jwtProperties.getPrivateKey(), jwtProperties.getUser().getExpire());
            // 写入cookie
            /*CookieUtils.newCookieBuilder()
                    // response,用于写cookie
                    .response(response)
                    // 保证安全防止XSS攻击，不允许JS操作cookie
                    .httpOnly(true)
                    // 设置domain
                    .domain(jwtProperties.getUser().getCookieDomain())
                    // 设置cookie名称和值
                    .name(jwtProperties.getUser().getCookieName()).value(token)
                    // 写cookie
                    .build();*/

            //LbMap resultMap = LbMap.successResult("用户登录成功", JSONUtil.classToJsonString(user));
            //resultMap.put(jwtProperties.getUser().getCookieName(), token);

            user.setDayoToken(token);

            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            //cookie名称
            userVo.setCookieName(jwtProperties.getUser().getCookieName());
            //cookie作用域
            userVo.setCookieDomain(jwtProperties.getUser().getCookieDomain());
            return userVo;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public LbMap setCookie(User user, HttpServletResponse response) {
        try {
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

    @Override
    public LbMap getUserByToken(String token) {
        try {
            if (token.equals("")){
                log.info("token令牌为空");
                return LbMap.failResult("令牌不能为空");
            }
            // 获取token信息
            Payload<User> payLoad = JwtUtil.getInfoFromToken(token, jwtProperties.getPublicKey(), User.class);
            User user = payLoad.getUserInfo();
            if (user==null){
                log.info("用户为空");
                return LbMap.failResult("没有找到用户，请重新登录");
            }
            log.info("用户获取成功");
            return LbMap.successResult("用户获取成功", JSONUtil.classToJsonString(user));
        } catch (Exception e) {
            // 抛出异常，证明token无效，直接返回401
            return LbMap.failResult("用户获取失败："+e.getMessage());
        }
    }
}
