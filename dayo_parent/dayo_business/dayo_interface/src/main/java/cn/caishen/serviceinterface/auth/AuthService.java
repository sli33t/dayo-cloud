package cn.caishen.serviceinterface.auth;

import cn.caishen.common.domain.po.User;
import cn.caishen.common.utils.LbMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录授权
 * @author LB
 */
public interface AuthService {

    /**
     * 登录
     * @param telNo 电话号码
     * @param password 明文密码
     * @param response 写入cookie
     * @return 登录是否成功
     */
    User loginAuth(String telNo, String password, HttpServletResponse response);

    /**
     * 设置token
     * @param user 用户信息
     * @param response 响应
     * @return 设置成功
     */
    LbMap setCookie(User user, HttpServletResponse response);

    /**
     * 通过用户获取token
     * @param token token
     * @param request 请求
     * @return 用户信息
     */
    LbMap getUserByToken(String token, HttpServletRequest request);
}
