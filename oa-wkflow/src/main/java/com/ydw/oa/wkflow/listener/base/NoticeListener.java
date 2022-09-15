package com.ydw.oa.wkflow.listener.base;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.business_wkflow.form.service.FormValsService;
import com.ydw.oa.wkflow.util.SpringContextUtil;
import com.ydw.oa.wkflow.util.activiti.ReviewTool;
import org.activiti.engine.delegate.DelegateTask;

import java.util.List;

/**
 * @Title NoticeListener
 * @Description 通知父类
 * @Author ZhangKai
 * @Date 2021/03/07 23:43
 * @Version 1.0
 * @Email 410618538@qq.com
 */

public class NoticeListener {

    protected JSONObject getWkFlowDatas(DelegateTask delegateTask){
        FormValsService formValsService = SpringContextUtil.getBean(FormValsService.class);
        IDatasService datasService = SpringContextUtil.getBean(IDatasService.class);
        QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ACTI_TASK_ID", delegateTask.getId());
        Datas datas = datasService.getOne(queryWrapper);
        if (ChkUtil.isNull(datas)) {
            datas = formValsService.getFirstForm(delegateTask.getProcessInstanceId());
        }
        JSONObject object = JsonUtil.jsonStrToJsonObject(datas.getFormValsJson());
        return object;
    }

    protected void notice(DelegateTask delegateTask, List<String> usrs,String type){
        JSONObject object = getWkFlowDatas(delegateTask);
        if (ChkUtil.isNotNull(object)) {
            String name = object.getString("oa-text-creator-0");
            String dept = object.getString("oa-text-dept-0");
            if ("todo".equals(type)) {
                ReviewTool.todo(String.join(",", usrs), delegateTask,name,dept);
            } else if ("notice".equals(type)) {
                ReviewTool.notice(String.join(",", usrs), delegateTask, name, dept);
            }
        }
    }

    protected void notice(DelegateTask delegateTask, String usrs,String type){
        JSONObject object = getWkFlowDatas(delegateTask);
        if (ChkUtil.isNotNull(object)) {
            String name = object.getString("oa-text-creator-0");
            String dept = object.getString("oa-text-dept-0");
            if ("todo".equals(type)) {
                ReviewTool.todo(usrs, delegateTask,name,dept);
            } else if ("notice".equals(type)) {
                ReviewTool.notice(usrs, delegateTask, name, dept);
            }
        }
    }

}
