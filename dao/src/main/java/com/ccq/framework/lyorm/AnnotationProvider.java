package com.ccq.framework.lyorm;

import javax.persistence.GenerationType;

import com.ccq.framework.entity.ColumnMap;
import com.ccq.framework.exception.AppException;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.bytecode.ConstPool;

//用于创建 select provider
public class AnnotationProvider {

	//数据模型类
	private Class<?> dmoClass;
	//生成目标类
	private Class<?> targetClass;
	//常量池
	private ConstPool constPool;

	private CtClass proxClass;

	private ColumnMap columnMap;

	public ColumnMap getColumnMap() {
		return columnMap;
	}

	public void setColumnMap(ColumnMap columnMap) {
		this.columnMap = columnMap;
	}

	public CtClass getProxClass() {
		return proxClass;
	}

	public void setProxClass(CtClass proxClass) {
		this.proxClass = proxClass;
	}

	public Class<?> getDmoClass() {
		return dmoClass;
	}

	public void setDmoClass(Class<?> dmoClass) {
		this.dmoClass = dmoClass;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	public ConstPool getConstPool() {
		return constPool;
	}

	public void setConstPool(ConstPool constPool) {
		this.constPool = constPool;
	}

	/**
	 * gouzao
	 * @param dmoClass
	 */
	public AnnotationProvider() {
		super();
	}

	/**
	 * gouzao
	 * @param dmoClass
	 * @throws CannotCompileException 
	 */
	public AnnotationProvider(Class<?> dmoClass) {
		super();
		this.dmoClass = dmoClass;
		//编译columnmap信息
	}

	/**
	 *  编译生成的类
	 * @throws CannotCompileException 
	 */
	public void compiler() {

		ClassPool pool = ClassPool.getDefault();
		pool.insertClassPath(new ClassClassPath(dmoClass));
		setProxClass(pool.makeClass(String.format("%sProvider", dmoClass.getSimpleName())));
		try{
			getProxClass().addMethod(
					makeInsertMethod(getProxClass(),dmoClass));

			getProxClass().addMethod(
					makeUpdateMethod(getProxClass(),dmoClass));

			getProxClass().addMethod(
					makeSelectOneMethod(getProxClass(),dmoClass));

			getProxClass().addMethod(
					makeSelectListMethod(getProxClass(),dmoClass));

			getProxClass().addMethod(
					makeSelectCount(getProxClass(),dmoClass));
		}catch(CannotCompileException ex) {
			//抛出应用程序错误信息
			throw new AppException(ex.getMessage());
		}
		
		//compiler maked class
		try {
			Class<?> cls = getProxClass().toClass();
			cls.newInstance();
		} catch (CannotCompileException e) {
			//can not compile
			throw new AppException(e.getMessage());
		}catch (Exception e) {
			throw new AppException(e.getMessage());
		}
	}

	//创造insert方法
	public CtMethod makeInsertMethod(CtClass type, Class<?> dmoClass) {

		CtMethod method = null;
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("public  String insert(%s insertDmo){",dmoClass.getName()));
			sb.append("org.apache.ibatis.jdbc.SQL sql = new org.apache.ibatis.jdbc.SQL();");
			sb.append(String.format("sql.INSERT_INTO(\"%s\");",getColumnMap().getTableName()));
			
			if(getColumnMap().getKeyGenratoyType() == GenerationType.SEQUENCE) {
				for(String keyProp : getColumnMap().getKeyProperty().keySet()) {
					String getter = keyProp.substring(0, 1).toUpperCase()+keyProp.substring(1);
					sb.append(String.format("if(insertDmo.get%s() != null) {sql.VALUES(\"%s\",\"#{%s}\");}", 
							new Object[]{getter,getColumnMap().getKeyProperty().get(keyProp),keyProp}));
				}
			}
			
			for(String prop:getColumnMap().getColumnMap().keySet()) {
				String getter = prop.substring(0, 1).toUpperCase()+prop.substring(1);
				sb.append(String.format("if(insertDmo.get%s() != null) {sql.VALUES(\"%s\",\"#{%s}\");}",
						new Object[]{getter,getColumnMap().getColumnMap().get(prop),prop}));
			}
			sb.append("return sql.toString();}");
			method = CtNewMethod.make(sb.toString(), type);
		} catch (CannotCompileException e) {
			throw new AppException(e.getMessage());
		}
		return method;
	}

	//创造update方法
	public CtMethod makeUpdateMethod(CtClass type, Class<?> dmoClass) {

		CtMethod method = null;
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("public  String update(%s updateDmo){",dmoClass.getName()));
			sb.append("org.apache.ibatis.jdbc.SQL sql = new org.apache.ibatis.jdbc.SQL();");
			sb.append(String.format("sql.UPDATE(\"%s\");",getColumnMap().getTableName()));
			for(String prop:getColumnMap().getKeyProperty().keySet()) {

				String getter = prop.substring(0, 1).toUpperCase()+prop.substring(1);
				sb.append(String.format("if(updateDmo.get%s() != null) {sql.SET(\"%s = #{%s}\");}",
						new Object[]{getter,getColumnMap().getKeyProperty().get(prop),prop}));
				sb.append(String.format("if(updateDmo.get%s() != null) {sql.WHERE(\"%s = #{%s}\");}",
						new Object[]{getter,getColumnMap().getKeyProperty().get(prop),prop}));
			}
			
			//根据主键更新，如果没有主键则失败
			for(String prop:getColumnMap().getColumnMap().keySet()) {
				
				String getter = prop.substring(0, 1).toUpperCase()+prop.substring(1);
				//根据主键更新，如果没有主键则失败
//				sb.append(String.format("if(updateDmo.get%s() != null) {sql.SET(\"%s = #{%s}\");}",
//						new Object[]{getter,getColumnMap().columnMap.get(prop),prop}));
				sb.append(String.format("if(updateDmo.get%s() != null) {sql.SET(\"%s = #{%s}\");}",
						new Object[]{getter,getColumnMap().getColumnMap().get(prop),prop}));
			}
			sb.append("return sql.toString();}");
			method = CtNewMethod.make(sb.toString(), type);
		} catch (CannotCompileException e) {
			throw new AppException(e.getMessage());
		}
		return method;
	}

	//创造selectOne方法
	public CtMethod makeSelectOneMethod(CtClass type, Class<?> dmoClass) {

		CtMethod method = null;
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("public  String selectOne(%s insertDmo){",dmoClass.getName()));
			sb.append("org.apache.ibatis.jdbc.SQL sql = new org.apache.ibatis.jdbc.SQL();");
			for(String prop:getColumnMap().getKeyProperty().keySet()) {

				sb.append(String.format("sql.SELECT(\"%s as %s\");",new Object[]{getColumnMap().getKeyProperty().get(prop),prop}));
			}
			for(String prop:getColumnMap().getColumnMap().keySet()) {

				sb.append(String.format("sql.SELECT(\"%s as %s\");",new Object[]{getColumnMap().getColumnMap().get(prop),prop}));
			}
			sb.append(String.format("sql.FROM(\"%s\");",getColumnMap().getTableName()));
			for(String prop:getColumnMap().getKeyProperty().keySet()) {

				String getter = prop.substring(0, 1).toUpperCase()+prop.substring(1);
				sb.append(String.format("if(insertDmo.get%s() != null) {sql.WHERE(\"%s = #{%s}\");}",new Object[]{getter,getColumnMap().getKeyProperty().get(prop),prop}));
			}
			for(String prop:getColumnMap().getColumnMap().keySet()) {
				
				String getter = prop.substring(0, 1).toUpperCase()+prop.substring(1);
				sb.append(String.format("if(insertDmo.get%s() != null) {sql.WHERE(\"%s = #{%s}\");}",new Object[]{getter,getColumnMap().getColumnMap().get(prop),prop}));
				}

			sb.append("return sql.toString();}");
			method = CtNewMethod.make(sb.toString(), type);
		} catch (CannotCompileException e) {
			throw new AppException(e.getMessage());
		}
		return method;
	}

	//创造selectList方法
	public CtMethod makeSelectListMethod(CtClass type, Class<?> dmoClass) {

		CtMethod method = null;
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("public  String selectList(%s insertDmo){",dmoClass.getName()));
			sb.append("org.apache.ibatis.jdbc.SQL sql = new org.apache.ibatis.jdbc.SQL();");
			for(String prop:getColumnMap().getKeyProperty().keySet()) {

				sb.append(String.format("sql.SELECT(\"%s as %s\");",new Object[]{getColumnMap().getKeyProperty().get(prop),prop}));
			}
			for(String prop:getColumnMap().getColumnMap().keySet()) {

				sb.append(String.format("sql.SELECT(\"%s as %s\");",new Object[]{getColumnMap().getColumnMap().get(prop),prop}));
			}
			sb.append(String.format("sql.FROM(\"%s\");",getColumnMap().getTableName()));
			for(String prop:getColumnMap().getKeyProperty().keySet()) {

				String getter = prop.substring(0, 1).toUpperCase()+prop.substring(1);
				sb.append(String.format("if(insertDmo.get%s() != null) {sql.WHERE(\"%s = #{%s}\");}",new Object[]{getter,getColumnMap().getKeyProperty().get(prop),prop}));
			}
			for(String prop:getColumnMap().getColumnMap().keySet()) {
				
				String getter = prop.substring(0, 1).toUpperCase()+prop.substring(1);
				sb.append(String.format("if(insertDmo.get%s() != null) {sql.WHERE(\"%s = #{%s}\");}",new Object[]{getter,getColumnMap().getColumnMap().get(prop),prop}));
				}

			sb.append("return sql.toString();}");
			method = CtNewMethod.make(sb.toString(), type);
		} catch (CannotCompileException e) {
			throw new AppException(e.getMessage());
		}
		return method;
	}

	//创造selectCount方法
	public CtMethod makeSelectCount(CtClass type, Class<?> dmoClass) {

		CtMethod method = null;
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("public  String selectCount(%s insertDmo){",dmoClass.getName()));
			sb.append("org.apache.ibatis.jdbc.SQL sql = new org.apache.ibatis.jdbc.SQL();");
			sb.append("sql.SELECT(\"count(*)\");");
			sb.append(String.format("sql.FROM(\"%s\");",getColumnMap().getTableName()));
			
			for(String prop:getColumnMap().getKeyProperty().keySet()) {

				String getter = prop.substring(0, 1).toUpperCase()+prop.substring(1);
				sb.append(String.format("if(insertDmo.get%s() != null) {sql.WHERE(\"%s = #{%s}\");}",new Object[]{getter,getColumnMap().getKeyProperty().get(prop),prop}));
			}
			for(String prop:getColumnMap().getColumnMap().keySet()) {
				
				String getter = prop.substring(0, 1).toUpperCase()+prop.substring(1);
				sb.append(String.format("if(insertDmo.get%s() != null) {sql.WHERE(\"%s = #{%s}\");}",new Object[]{getter,getColumnMap().getColumnMap().get(prop),prop}));
			}
			sb.append("return sql.toString();}");
			method = CtNewMethod.make(sb.toString(), type);
		} catch (CannotCompileException e) {
			throw new AppException(e.getMessage());
		}
		return method;
	}
}
