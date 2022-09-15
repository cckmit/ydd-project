package com.ydw.oa.wkflow.business_main.datas.service.impl;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.entity.HiDatas;
import com.ydw.oa.wkflow.business_main.datas.mapper.HiDatasMapper;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.business_main.datas.service.IHiDatasService;
import com.ydw.oa.wkflow.util.date.DateTools;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-23
 */
@Service
public class HiDatasServiceImpl extends ServiceImpl<HiDatasMapper, HiDatas> implements IHiDatasService {

	@Autowired
	private JdbcTemplate jt;
	@Autowired
	private IDatasService datasService;

	@Override
	public HiDatas createHistoryActivitTablesDatas(String objectId, String pId) {
		// TODO 生成工作流当前相关数据记录
		QueryWrapper<HiDatas> hiQueryWrapper = new QueryWrapper<>();
		hiQueryWrapper.eq("DATAS_ID", objectId);
		HiDatas hiDatas = this.getOne(hiQueryWrapper);
		if(ChkUtil.isNull(hiDatas)) {
			hiDatas = new HiDatas();
			hiDatas.setDatasId(objectId);
			// 保存工作流表结构的数据
			Field[] fields = HiDatas.class.getDeclaredFields();
			for (Field field : fields) {
				String name = field.getName();
				if ("objectId".equals(name) || "isDeleted".equals(name) || "createTime".equals(name)
						|| "datasId".equals(name) || "serialVersionUID".equals(name) || "actRuJob".equals(name)) {
					continue;
				}
				field.setAccessible(true);
				TableField tableField = field.getAnnotation(TableField.class);
				String actTable = tableField.value();
				List<Map<String, Object>> list = this.selectFieldDatas(actTable, pId);
				String value = "";
				if (!list.isEmpty()) {
					value = JsonUtil.toJson(list);
				}
				try {
					field.set(hiDatas, value);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			this.save(hiDatas);
		}
		return hiDatas;
	}

	private List<Map<String, Object>> selectFieldDatas(String actTable, String pId) {
		// TODO 获取工作流数据
		String sql = "select * from @table where PROC_INST_ID_=? ";
		sql = sql.replace("@table", actTable);
		List<Map<String, Object>> list = jt.queryForList(sql, pId);
		return list;
	}

	@Transactional
	@Override
	public void rollBack(HiDatas hiDatas, String pId, String currentTaskId) {
		// TODO 回退
		if (ChkUtil.isNull(hiDatas)) {
			return;
		}
		// 删除工作流原有数据
		this.deleteFieldDatas("ACT_HI_ACTINST", pId);
		this.deleteFieldDatas("ACT_HI_ATTACHMENT", pId);
		this.deleteFieldDatas("ACT_HI_COMMENT", pId);
		this.deleteFieldDatas("ACT_HI_DETAIL", pId);
		this.deleteFieldDatas("ACT_HI_IDENTITYLINK", pId);
		this.deleteFieldDatas("ACT_HI_PROCINST", pId);
		this.deleteFieldDatas("ACT_HI_TASKINST", pId);
		this.deleteFieldDatas("ACT_HI_VARINST", pId);
		this.deleteFieldDatas("ACT_RU_EVENT_SUBSCR", pId);
		this.deleteFieldDatas("ACT_RU_IDENTITYLINK", pId);
		this.deleteIndentityLink(currentTaskId, pId);
		this.deleteFieldDatas("ACT_RU_TASK", pId);
		this.deleteFieldDatas("ACT_RU_VARIABLE", pId);
		this.deleteFieldDatas("ACT_RU_EXECUTION", pId);
		// 创建目标任务的相关数据
		this.createFieldDatas("ACT_RU_EXECUTION", hiDatas.getActRuExecution());
		this.createFieldDatas("ACT_RU_VARIABLE", hiDatas.getActRuVariable());
		this.createFieldDatas("ACT_RU_TASK", hiDatas.getActRuTask());
		this.createFieldDatas("ACT_RU_IDENTITYLINK", hiDatas.getActRuIdentitylink());
		this.createFieldDatas("ACT_RU_EVENT_SUBSCR", hiDatas.getActRuEventSubscr());
		this.createFieldDatas("ACT_HI_VARINST", hiDatas.getActHiVarinst());
		this.createFieldDatas("ACT_HI_TASKINST", hiDatas.getActHiTaskinst());
		this.createFieldDatas("ACT_HI_PROCINST", hiDatas.getActHiProcinst());
		this.createFieldDatas("ACT_HI_IDENTITYLINK", hiDatas.getActHiIdentitylink());
		this.createFieldDatas("ACT_HI_DETAIL", hiDatas.getActHiDetail());
		this.createFieldDatas("ACT_HI_COMMENT", hiDatas.getActHiComment());
		this.createFieldDatas("ACT_HI_ATTACHMENT", hiDatas.getActHiAttachment());
		this.createFieldDatas("ACT_HI_ACTINST", hiDatas.getActHiActinst());
	}

	@Transactional
	private void deleteIndentityLink(String currentTaskId, String pId) {
		// TODO 删除ACT_RU_IDENTITYLINK
		QueryWrapper<Datas> qw = new QueryWrapper<Datas>();
		qw.eq("ACTI_PROC_INST_ID", pId);
		qw.select("ACTI_TASK_ID");
		List<Datas> list = datasService.list(qw);
		for (Datas datas : list) {
			String sql = "delete from ACT_RU_IDENTITYLINK where TASK_ID_=? ";
			jt.update(sql, datas.getActiTaskId());
		}
		String sql = "delete from ACT_RU_IDENTITYLINK where TASK_ID_=? ";
		jt.update(sql, currentTaskId);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	private void createFieldDatas(String table, String data) {
		// TODO 还原工作流相关数据
		if (ChkUtil.isNull(data)) {
			return;
		}
		JSONArray array = JSONArray.parseArray(data);
		for (Object object : array) {
			String sql = "insert into @table ";
			String fields = "(";
			String values = "(";
			Map<String, Object> map = (Map<String, Object>) object;
			for (String key : map.keySet()) {
				fields += "," + key;
				Object val = map.get(key);
				if (key.indexOf("TIME_") > 0) {
					val = DateTools.formatDateTime(new Date(Long.parseLong(val + "")));
				}
				if(ChkUtil.isNull(val)) {
					val = "";
				}
				values += ",'" + val + "'";
			}
			fields += ")";
			values += ")";
			fields = fields.replaceFirst(",", "");
			values = values.replaceFirst(",", "");
			sql = sql + fields + " values " + values;
			sql = sql.replace("@table", table);
			jt.execute(sql);
		}
	}

	@Transactional
	private void deleteFieldDatas(String actTable, String pId) {
		// TODO 删除工作流相关数据
		if("ACT_RU_EXECUTION".equals(actTable)) {
			String sql = "delete from @table where PROC_INST_ID_=? and IS_ACTIVE_=1 ";
			sql = sql.replace("@table", actTable);
			jt.update(sql, pId);
			
			sql = sql.replace("IS_ACTIVE_=1", "IS_ACTIVE_=0");
			jt.update(sql, pId);
			
			return;
		}
		String sql = "delete from @table where PROC_INST_ID_=? ";
		sql = sql.replace("@table", actTable);
		jt.update(sql, pId);
	}

}
