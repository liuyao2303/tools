package com.ccq.framework.entity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.ccq.framework.exception.AppException;

public class ColumnMap {

	private Map<String,String> keyProperty = new HashMap<String, String>();
	//story for key - property name mapping
	private Map<String,String> columnMap = new HashMap<String, String>();
	//主键生成类型
	private GenerationType keyGenratoyType;
	//对于oracle来说，可以使用sequence，需要制定sequence的名字    ,暂时不考虑
	private String generatorStatement;
	//表名称
	private String tableName;
	//目标类
	private Class<?> targetClass;

	public String getGeneratorStatement() {
		return generatorStatement;
	}

	public void setGeneratorStatement(String generatorStatement) {
		this.generatorStatement = generatorStatement;
	}

	public Map<String,String> getKeyProperty() {
		return keyProperty;
	}

	public void setKeyProperty(Map<String,String> keyProperty) {
		this.keyProperty = keyProperty;
	}

	public Map<String, String> getColumnMap() {
		return columnMap;
	}

	public void setColumnMap(Map<String, String> columnMap) {
		this.columnMap = columnMap;
	}

	public GenerationType getKeyGenratoyType() {
		return keyGenratoyType;
	}

	public void setKeyGenratoyType(GenerationType keyGenratoyType) {
		this.keyGenratoyType = keyGenratoyType;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	/**
	 * 构造
	 * @param targetClass
	 */
	public ColumnMap(Class<?> targetClass) {
		super();
		this.targetClass = targetClass;
	}

	/**
	 * 编译 ,, 根据注解信息，生成映射表
	 */
	public void compiler() {

		if(!this.getTargetClass().isAnnotationPresent(Entity.class)) {

			throw new AppException("Error : target class not support Entity | NOT SUPPORT！");
		}

		//extract table name
		Entity entity = (Entity)this.getTargetClass().getAnnotation(Entity.class);
		this.setTableName(entity.name());

		//extract all property
		Field[] fields =  this.getTargetClass().getDeclaredFields();
		for(Field field : fields) {
			if(field.isAnnotationPresent(Column.class) && !field.isAnnotationPresent(Id.class)) {

				this.getColumnMap().put(field.getName(), ((Column)field.getAnnotation(Column.class)).name());
			}else if(field.isAnnotationPresent(Id.class) && field.isAnnotationPresent(Id.class)){
				//id ,key column
				keyProperty.put(field.getName(), ((Column)field.getAnnotation(Column.class)).name());
				GeneratedValue gen = field.getAnnotation(GeneratedValue.class);
				GenerationType genType = gen.strategy();
				setKeyGenratoyType(genType);
				if (genType == GenerationType.IDENTITY) {
					//mysql的主键生成策略
					setGeneratorStatement("SELECT LAST_INSERT_ID()");
				}else if(genType == GenerationType.SEQUENCE) {
					//ORACLE的主键生成策略
					setGeneratorStatement(String.format("SELECT %s.NEXTVAL FROM DUAL",gen.generator()));
				}
			}
		}
	}
}