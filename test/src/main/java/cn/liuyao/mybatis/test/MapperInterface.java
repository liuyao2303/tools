package cn.liuyao.mybatis.test;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import com.liuyao.test.ForeumDmo;

public interface MapperInterface {

	@Insert(
			"insert into forum (username,city,topic_title,content,create_time) "
			+ "values(#{username},#{city},#{topicTitle},#{content},#{createTime})"
			)
	@SelectKey(statement = "select last_insert_id()",before = false,
		keyProperty = "id",resultType = Long.class)
	public int insert(ForeumDmo dmo);
	
	@Select("select * from forum where id = #{id}")
	public ForeumDmo select(ForeumDmo dmo);
}
