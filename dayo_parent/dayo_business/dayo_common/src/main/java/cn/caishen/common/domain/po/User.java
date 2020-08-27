package cn.caishen.common.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author LB
 */
@TableName(value = "TB_USER")
@Data
public class User implements Serializable{

    private static final long serialVersionUID = 8935171055191313090L;

    @TableId(value = "USER_ID", type = IdType.ID_WORKER_STR)
    private String userId;

    @TableField(value = "USER_NAME")
    private String username;

    @TableField(value = "TEL_NO")
    private String telNo;

    @TableField(value = "EMAIL")
    private String email;

    @TableField(value = "PASSWORD")
    private String password;

    @TableField(value = "DELETE_FLAG")
    private Integer deleteFlag;

    @TableField(value = "DELETE_DATE")
    private Date deleteDate;

    @TableField(value = "ROW_VERSION")
    private Integer rowVersion;

    @TableField(value = "CREATE_TIME")
    private Date createTime;

    /**
     * 用户类型：0-超级管理员，1-员工
     */
    @TableField(value = "USER_TYPE")
    private Integer userType;

    @TableField(value = "DAYO_TOKEN", exist = false)
    private String dayoToken;
}
