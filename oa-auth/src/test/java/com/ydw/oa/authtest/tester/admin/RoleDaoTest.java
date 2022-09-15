package com.ydw.oa.authtest.tester.admin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import com.ydw.oa.auth.business.usr.service.IAuUsrService;

/**
 * AdminDaoTest
 * 
 * @author 冯晓东 398479251@qq.com
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Configuration
public class RoleDaoTest {

	@Autowired
	private IAuUsrService isrService;

	@Test
	public void list() {
		isrService.list();
	}

}
