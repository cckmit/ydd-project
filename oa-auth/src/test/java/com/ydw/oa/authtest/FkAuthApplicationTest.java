package com.ydw.oa.authtest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.ChkUtil;
import com.ydw.oa.auth.FkAuthApplication;
import com.ydw.oa.auth.business.dept.entity.AuDept;
import com.ydw.oa.auth.business.dept.service.IAuDeptService;
import com.ydw.oa.auth.business.role.entity.AuRole;
import com.ydw.oa.auth.business.role.service.IAuRoleService;
import com.ydw.oa.auth.business.usr.entity.AuUsr;
import com.ydw.oa.auth.business.usr.entity.AuUsrDept;
import com.ydw.oa.auth.business.usr.entity.AuUsrRole;
import com.ydw.oa.auth.business.usr.service.IAuUsrDeptService;
import com.ydw.oa.auth.business.usr.service.IAuUsrRoleService;
import com.ydw.oa.auth.business.usr.service.IAuUsrService;
import com.ydw.oa.auth.util.excel.ExcelReadTools;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * .测试模板
 * 
 * @author 冯晓东 398479251@qq.com
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FkAuthApplication.class)
public class FkAuthApplicationTest {

	@Autowired
	private IAuUsrService usrService;
	@Autowired
	private IAuDeptService deptService;
	@Autowired
	private IAuRoleService roleService;
	@Autowired
	private IAuUsrRoleService usrRoleService;
	@Autowired
	private IAuUsrDeptService usrDeptService;

	@Test
	public void contextLoads() {
		String path = "C:\\Users\\86150\\Desktop\\流程管控新增需求\\人员信息表.xlsx";
		InputStream is = null;
		try {
			is = new FileInputStream(path);
			ExcelReadTools excelReader = new ExcelReadTools();
			List<Map<String, Object>> list = excelReader.readExcel2ListMap(is,
					new String[] { "index", "realName", "mobile", "deptName", "roleName",
							"wxUserId", "note"},
					1);
			for (Map<String, Object> map : list) {
				QueryWrapper<AuDept> dw = new QueryWrapper<AuDept>();
				dw.eq("NAME", map.get("deptName"));
				AuDept dept = deptService.getOne(dw);
				if(ChkUtil.isNull(dept)) {
					break;
				}
				QueryWrapper<AuRole> rw = new QueryWrapper<AuRole>();
				rw.eq("NAME", map.get("roleName"));
				AuRole role = roleService.getOne(rw);
				if(ChkUtil.isNull(role)) {
					break;
				}
				String name = map.get("wxUserId").toString().replace(" ", "");
				AuUsr usr = new AuUsr();
				usr.setWxUserId(map.get("wxUserId").toString());
				usr.setRealName(name);
				usr.setUsrName(name);
				usr.setNote(map.get("note").toString());
				usr.setMobile(map.get("mobile").toString());
				usr.setSignUrl(this.getCamelPinYin(name) + ".png");
				usrService.save(usr);
				AuUsrRole usrRole = new AuUsrRole();
				usrRole.setRoleId(role.getObjectId());
				usrRole.setUsrId(usr.getObjectId());
				usrRoleService.save(usrRole);
				AuUsrDept usrDept = new AuUsrDept();
				usrDept.setDeptId(dept.getObjectId());
				usrDept.setUsrId(usr.getObjectId());
				usrDeptService.save(usrDept);
			}
		} catch (FileNotFoundException e) {
			System.out.println("未找到指定路径的文件!");
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 将汉字转换为全拼
	 * 
	 * @param src
	 * @return String
	 */
	private String getCamelPinYin(String src) {
		char[] t1 = null;
		t1 = src.toCharArray();
		String[] t2 = new String[t1.length];
		// 设置汉字拼音输出的格式
		HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
		t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		t3.setVCharType(HanyuPinyinVCharType.WITH_V);
		String t4 = "", t = "";
		int t0 = t1.length;
		try {
			for (int i = 0; i < t0; i++) {
				// 判断是否为汉字字符
				if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
					t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);// 将汉字的几种全拼都存到t2数组中
					t = t2[0];// 取出该汉字全拼的第一种读音并连接到字符串t4后
				} else {
					// 如果不是汉字字符，直接取出字符并连接到字符串t4后
					t = Character.toString(t1[i]);
				}
//				t = t.substring(0, 1).toUpperCase() + t.substring(1);
				t4 += t;
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
		}
		return t4;
	}

}
