package com.ccq.framework.plugin;

import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Intercepts({@Signature(type = StatementHandler.class,method = "prepare",args = {java.sql.Connection.class})})
public class MyPagePlugin implements Interceptor{

	private Dialect dialect;
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		
		Logger log = LoggerFactory.getLogger(MyPagePlugin.class);
		StatementHandler hander = (StatementHandler) invocation.getTarget();
		MetaObject meta = MetaObject.forObject(hander, new DefaultObjectFactory(), 
				new DefaultObjectWrapperFactory(), new DefaultReflectorFactory());
		RowBounds rowBound = (RowBounds) meta.getValue("delegate.rowBounds");
		if(rowBound.equals(RowBounds.DEFAULT)) {
			
			//未指定分页命令
			return invocation.proceed();
		}
		BoundSql boundSql = (BoundSql) meta.getValue("delegate.boundSql");
		if(dialect == null) {
			
			log.debug("not assigned dialect,use mysql default");
			dialect = new MysqlDialect();
		}
		String buildSql = dialect.builderSql(boundSql.getSql(), rowBound.getOffset(), rowBound.getLimit());
		MetaObject boundSqlMeta = MetaObject.forObject(boundSql, new DefaultObjectFactory(), 
				new DefaultObjectWrapperFactory(), new DefaultReflectorFactory());
		boundSqlMeta.setValue("sql", buildSql);
		meta.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
		meta.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
		return invocation.proceed();
	}

	//生成目标对象的代理
	@Override
	public Object plugin(Object target) {
		if(target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		}else {
			return target;
		}
	}

	/**
	 *  设置属性功能
	 */
	@Override
	public void setProperties(Properties properties) {
		if(properties.getProperty("dialect").equals("mysql")) {
			dialect = new MysqlDialect();
		}else if(properties.getProperty("dialect").equals("oracle")) {
			//TODO  暂时未加入oracle的功能
		}
	}

}
