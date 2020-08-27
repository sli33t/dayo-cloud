package cn.caishen.common.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author LB
 */
@Data
//全部参数构造
@AllArgsConstructor
//无参数构造
@NoArgsConstructor
public class UserVo implements Serializable {

    private static final long serialVersionUID = 8935171055191313090L;

    private String userId;

    private String username;

    private String telNo;

    private String email;

    private String password;

    private Integer deleteFlag;

    private Date deleteDate;

    private Integer rowVersion;

    private Date createTime;

    /**
     * 用户类型：0-超级管理员，1-员工
     */
    private Integer userType;

    private String dayoToken;

    private String cookieName;

    private String cookieDomain;
}
