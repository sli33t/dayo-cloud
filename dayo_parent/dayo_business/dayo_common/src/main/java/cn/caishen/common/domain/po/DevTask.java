package cn.caishen.common.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName(value = "TB_DEVTASK")
@Data
public class DevTask implements Serializable {

    @TableId(value = "DEVTASK_ID", type = IdType.ID_WORKER_STR)
    private String devtaskId;

    @TableField(value = "FEEDBACK_ID")
    private Integer feedbackId;

    @TableField(value = "DEVELOP_USER_ID")
    private String developUserId;

    @TableField(value = "RECEIVED")
    private Integer received;

    @TableField(value = "RECEIVE_TIME")
    private Date receiveTime;

    @TableField(value = "RECEIVE_DATE")
    private Date receiveDate;

    @TableField(value = "FINISHED")
    private Integer finished;

    @TableField(value = "FINISH_DATE")
    private Date finishDate;

    @TableField(value = "FINISH_TIME")
    private Date finishTime;

    @TableField(value = "FEEDBACK_TIME")
    private Date feedbackTime;

    @TableField(value = "TASK_DATE")
    private Date taskDate;

    @TableField(value = "TASK_TIME")
    private Date taskTime;

    @TableField(value = "CREATE_USER_ID")
    private String createUserId;

    @TableField(value = "PLAN_HOUR")
    private Double planHour;

    @TableField(value = "REAL_HOUR")
    private Double realHour;

    @TableField(value = "TASK_TEXT")
    private String taskText;

    @TableField(value = "FINISH_TEXT")
    private String finishText;

    //错误引入人
    @TableField(value = "IMPORT_USER_ID")
    private String importUserId;

    //是否是问题，0-不是，1-是
    @TableField(value = "IS_PROBLEM")
    private Integer isProblem;

    //前端传入的是 on off
    @TableField(value = "IS_PROBLEM_TEXT", exist = false)
    private String isProblemText;

    @TableField(value = "NEED_TEST")
    private Integer needTest;

    @TableField(value = "NEED_TEST_TEXT", exist = false)
    private String needTestText;

    @TableField(value = "TEST_RECEIVED")
    private Integer testReceived;
}
