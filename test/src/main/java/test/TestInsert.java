package test;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.liuyao.test.Daotest;
import com.liuyao.test.ForeumDmo;

public class TestInsert {

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		final ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/application-db.xml");
		
		ExecutorService exex = Executors.newFixedThreadPool(50);
		for(int i = 0 ;i<20 ;i++){
			exex.execute(new Runnable() {

				@Override
				public void run() {
					Daotest dao = (Daotest) ctx.getBean("dao");

					ForeumDmo dmo = new ForeumDmo();
					dmo.setCity("Beijing");
					dmo.setContent("I love Beijing");
					dmo.setCreateTime(new Date());
					dmo.setTopicTitle("I love Beijing");
					dmo.setUsername("liuyao");
					dao.insert(dmo);

					System.out.println(dmo);
				}
			});
		}
		exex.shutdown();
	}
}
