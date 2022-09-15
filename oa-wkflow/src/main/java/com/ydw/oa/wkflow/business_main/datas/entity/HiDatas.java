package com.ydw.oa.wkflow.business_main.datas.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 参考 https://www.devdoc.cn/activiti-table-act_ru_variable.html 获取activiti表结构
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_hi_datas")
@ApiModel(value="HiDatas对象", description="")
public class HiDatas implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
	@TableId(value = "OBJECT_ID", type = IdType.UUID)
    private String objectId;

    @ApiModelProperty(value = "是否被删除(0:否;1:是)")
	@TableLogic(value = "0", delval = "1")
    @TableField("IS_DELETED")
    private int isDeleted;

    @ApiModelProperty(value = "创建时间")
	@TableField("CREATE_TIME")
	private Date createTime = new Date(System.currentTimeMillis());

    @ApiModelProperty(value = "Datas的id")
    @TableField("DATAS_ID")
    private String datasId;

    @ApiModelProperty(value = "历史节点表数据")
    @TableField("ACT_HI_ACTINST")
    private String actHiActinst;

    @ApiModelProperty(value = "历史附件表数据")
    @TableField("ACT_HI_ATTACHMENT")
    private String actHiAttachment;

    @ApiModelProperty(value = "历史意见表数据")
    @TableField("ACT_HI_COMMENT")
    private String actHiComment;

    @ApiModelProperty(value = "历史详情表数据")
    @TableField("ACT_HI_DETAIL")
    private String actHiDetail;

    @ApiModelProperty(value = "历史流程人员表数据")
    @TableField("ACT_HI_IDENTITYLINK")
    private String actHiIdentitylink;

    @ApiModelProperty(value = "历史流程实例表数据")
    @TableField("ACT_HI_PROCINST")
    private String actHiProcinst;

    @ApiModelProperty(value = "历史任务实例表数据")
    @TableField("ACT_HI_TASKINST")
    private String actHiTaskinst;

    @ApiModelProperty(value = "历史变量表数据")
    @TableField("ACT_HI_VARINST")
    private String actHiVarinst;

    @ApiModelProperty(value = "throwEvent、catchEvent时间监听信息表数据")
    @TableField("ACT_RU_EVENT_SUBSCR")
    private String actRuEventSubscr;

    @ApiModelProperty(value = "运行时流程执行实例表数据")
    @TableField("ACT_RU_EXECUTION")
    private String actRuExecution;

    @ApiModelProperty(value = "运行时流程人员表数据")
    @TableField("ACT_RU_IDENTITYLINK")
    private String actRuIdentitylink;

    @ApiModelProperty(value = "运行时定时任务数据表数据")
    @TableField("ACT_RU_JOB")
    private String actRuJob;

    @ApiModelProperty(value = "运行时任务节点表数据")
    @TableField("ACT_RU_TASK")
    private String actRuTask;

    @ApiModelProperty(value = "运行时流程变量数据表数据")
    @TableField("ACT_RU_VARIABLE")
    private String actRuVariable;


}
