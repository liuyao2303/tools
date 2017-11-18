package cn.liuyao.mybatis.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.liuyao.test.ForeumDmo;

public class AnnotationTest {
	
	public static void main(String[] args) throws IOException {
		
		InputStream in = Resources.getResourceAsStream("mbt.cfg.xml");
		SqlSessionFactory builder = new SqlSessionFactoryBuilder().build(in);
		SqlSession session = builder.openSession();
		session.getConfiguration().addMapper(MapperInterface.class);
		
		ForeumDmo dmo = new ForeumDmo();
		dmo.setId(153L);
		dmo.setCity("Tianjing");
		dmo.setContent("I love Tianjing");
		dmo.setCreateTime(new Date());
		dmo.setUsername("liuyao");
		dmo.setTopicTitle("Love Tianjing");
		//session.insert("cn.liuyao.mybatis.test.MapperInterface.insert",dmo);
		List<ForeumDmo> lkist = session.selectList("cn.liuyao.mybatis.test.MapperInterface.select",dmo, new RowBounds(2, 2));
		System.out.println(lkist);
	}
}
