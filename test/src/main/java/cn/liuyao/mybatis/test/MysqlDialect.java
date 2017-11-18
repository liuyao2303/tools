package cn.liuyao.mybatis.test;

import com.ccq.framework.exception.AppException;

public class MysqlDialect extends Dialect{

	@Override
	public String builderSql(String rawSql,int OFFSET,int LIMIT) {
		
		if(rawSql.endsWith(";")) {
			
			throw new AppException("Bad sql: grammer error on ; with limit");
		}
		
		StringBuffer sb = new StringBuffer(rawSql);
		//SELECT * FROM table LIMIT [offset,] rows
		sb.append(String.format(" limit %s,%s",new Object[]{OFFSET,LIMIT}));
		return sb.toString();
	}
}
