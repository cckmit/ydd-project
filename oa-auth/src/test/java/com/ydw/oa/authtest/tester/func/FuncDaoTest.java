package com.ydw.oa.authtest.tester.func;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ydw.oa.auth.business.func.entity.AuFunc;
import com.ydw.oa.auth.business.func.service.IAuFuncService;

/**
 * 
 * @author 冯晓东 398479251@qq.com
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Configuration
public class FuncDaoTest {

	@Autowired
	IAuFuncService funcService;

	@Test
	public void list() {
		List<String> funcIds = new ArrayList<>();
		funcIds.add("b18883ef57073d5d7443565c4415e00f");
		funcIds.add("3ee3f23742d4e7ec52898286de9b5193");

		QueryWrapper<AuFunc> funcQuery = new QueryWrapper<>();
		funcQuery.in("OBJECT_ID", funcIds);
		funcQuery.eq("FUNC_TYPE", 0);
		List<AuFunc> funcList = funcService.list(funcQuery);
		System.err.println(funcList);
	}

}
