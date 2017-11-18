package test;

import java.io.IOException;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ccq.framework.lang.Page;
import com.liuyao.test.Daotest;
import com.liuyao.test.ForeumDmo;

import javassist.CannotCompileException;
import javassist.NotFoundException;

public class TestSelectList {
	public static void main(String[] args) throws CannotCompileException, IOException, NotFoundException {
		@SuppressWarnings("resource")
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/application-db.xml");

		Daotest dao = (Daotest) ctx.getBean("dao");

		ForeumDmo dmo = new ForeumDmo();
		dmo.setUsername("liuyao");
		Page page = new Page();
		page.setPageNum(-1);
		page.setPageSize(8);
		List<ForeumDmo> list = dao.selectListByPage(dmo,page );
		for(ForeumDmo dmoA : list) {
			System.out.println(dmoA);
		}
		System.out.println(page);
	}
}
