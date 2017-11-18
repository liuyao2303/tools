package com.ccq.framework.dao;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.ccq.framework.lang.ErrorCode;
import com.ccq.framework.exception.AppException;
import com.ccq.framework.lang.Page;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.ccq.framework.lyorm.InterfaceMaker;

public class MybatisDao implements InitializingBean{

	//使用sqlSessionaTemplete的线程安全会话
	@Autowired
	private SqlSession sqlSession;

	/**
	 * key-value    dmoclass   mapperInterfaceClass
	 */
	public static ConcurrentMap<Class<?>, Class<?>>  classMap = new ConcurrentHashMap<Class<?>, Class<?>>();

	public void afterPropertiesSet() throws Exception {

		//校验工作
		if(getSqlSession() == null) {
			
			throw new AppException("mybatis orm initlize failed!");
		}
	}

	public SqlSession getSqlSession() {
		return sqlSession;
	}

	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	//获取Sqlsession的方式
	public SqlSession getSqlSession(Class<?> dmoClass) {
		if(!classMap.containsKey(dmoClass)) {
			synchronized (classMap) {
				//如果当前的mapper中没有这个类型的mapper，则创建新的 double check!!!
				if(!classMap.containsKey(dmoClass)) {
					Class<?> targetClass = InterfaceMaker.make(dmoClass);
					//添加映射注解
					classMap.put(dmoClass, targetClass);
					getSqlSession().getConfiguration().addMapper(targetClass);
				}
			}
		}
		return sqlSession;
	}

	/* insert  */
	public int insert(String statement) {
		
		return getSqlSession().insert(statement);
	}
	
	public int insert(String statement,Object params) {
		
		return getSqlSession().insert(statement, params);
	}
	
	/* update */
	public int update(String statement) {
		
		return getSqlSession().update(statement);
	}
	
	public int update(String statement,Object params) {
		
		return getSqlSession().update(statement, params);
	}
	
	/* select */
	public <T> T selectOne(String statement,Object params) {
		
		return getSqlSession().selectOne(statement, params);
	}
	
	public <T> T selectOne(String statement) {
		
		return getSqlSession().selectOne(statement);
	}
	
	/* selectList */
	public <T> List<T> selectList(String statement) {
		
		return getSqlSession().selectList(statement);
	}
	
	public <T> List<T> selectList(String statement,Object params) {
		
		return getSqlSession().selectList(statement, params);
	}
	
	public <T> int insert(T dmo) {
		
		return getSqlSession(dmo.getClass()).insert(dmo.getClass().getSimpleName()+".insert",dmo);
	}
	
	public <T> int update(T dmo)  {
		
		return getSqlSession(dmo.getClass()).update(dmo.getClass().getSimpleName()+".update",dmo);
	}

	public <T> T selectOne(T dmo) {
		return getSqlSession(dmo.getClass()).selectOne(dmo.getClass().getSimpleName()+".selectOne",dmo);
	}

	public <T> List<T> selectList(T dmo) {
		return getSqlSession(dmo.getClass()).selectList(dmo.getClass().getSimpleName()+".selectList",dmo);
	}
	public <T> Long selectCount(T dmo) {
		return getSqlSession(dmo.getClass()).selectOne(dmo.getClass().getSimpleName()+".selectCount",dmo);
	}
	
	public <T> List<T> selectListByPage(T dmo,Page page) {
		if(page.getPageNum() <= 0) {
			throw new AppException("wrong page num format");
		}
		int offset = (page.getPageNum()-1)*page.getPageSize();
		int limit = page.getPageSize();
		Long count = getSqlSession(dmo.getClass()).selectOne(dmo.getClass().getSimpleName()+".selectCount", dmo);
		if(count == null) {
			throw new AppException(false,"check you code or mapper file to sure you not pass a wrong param!", ErrorCode.INTERNAL_ERROR);
		}
		page.setPages(((int) (count/page.getPageSize())) + ((count%page.getPageSize()>0) ? 1:0));
		List<T> result = getSqlSession(dmo.getClass()).selectList(dmo.getClass().getSimpleName()+".selectList", dmo,
				new RowBounds(offset, limit));
		return result;
	}
	
	public <T> List<T> selectListByPage(String CountStatement, String mapperStatement,Object arg1,Page page) {
		if(page.getPageNum() <= 0) {
			throw new AppException("wrong page num format");
		}
		int offset = (page.getPageNum()-1)*page.getPageSize();
		int limit = page.getPageSize();
		Long count = getSqlSession().selectOne(CountStatement, arg1);
		if(count == null) {
			throw new AppException(false,"check you code or mapper file to sure you not pass a wrong param!", ErrorCode.INTERNAL_ERROR);
		}
		page.setPages(((int) (count/page.getPageSize())) + ((count%page.getPageSize()>0) ? 1:0));
		List<T> result = getSqlSession().selectList(mapperStatement, arg1,
				new RowBounds(offset, limit));
		return result;
	}
}
