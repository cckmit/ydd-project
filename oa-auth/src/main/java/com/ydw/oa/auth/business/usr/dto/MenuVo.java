package com.ydw.oa.auth.business.usr.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * UsrDto 系统用户表
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "UsrDto 返回页面的菜单对象", description = "")
public class MenuVo {

	private String icon;
	private String label;
	private String path;
	private String component;
	private String objectId;
	private String parentId;
	private int status;
	private String roles;
	private List<MenuVo> children;
	private int sort;
	private Meta meta;
//	private boolean keepAlive;
}
