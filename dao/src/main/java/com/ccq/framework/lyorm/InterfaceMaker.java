package com.ccq.framework.lyorm;

import java.io.IOException;

import javax.persistence.GenerationType;

import com.ccq.framework.entity.ColumnMap;
import com.ccq.framework.exception.AppException;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.ClassMemberValue;
import javassist.bytecode.annotation.StringMemberValue;

public class InterfaceMaker {
	//涓嶆兂鐢ㄥ叏灞�鍙橀噺锛岀嚎绋嬩笉瀹夊叏锛屼細鍑洪棶棰�
	//	private CtClass ctClass;
	//	private ConstPool constPool;
	//	private String dmoClassName;
	//	private String dmoSimpleName;

	/**
	 * 创建一个mapper接口，通过注解的方式动态执行sql
	 * 
	 * @param dmoClass
	 * @return
	 * @throws CannotCompileException
	 * @throws IOException 
	 * @throws NotFoundException 
	 */
	public static Class<?> make(Class<?> dmoClass) {

		String dmoSimpleName = dmoClass.getSimpleName();
		ClassPool pool = ClassPool.getDefault();
		pool.insertClassPath(new ClassClassPath(dmoClass));
		CtClass target = pool.makeInterface(dmoSimpleName);

		ColumnMap columnMap = new ColumnMap(dmoClass);
		columnMap.compiler();
		AnnotationProvider provider = new AnnotationProvider(dmoClass);
		provider.setColumnMap(columnMap);
		provider.compiler();

		try {
			target.addMethod(
					addMethod(target,"public void insert(%s dmo);","org.apache.ibatis.annotations.InsertProvider","insert",dmoClass,columnMap));
			target.addMethod(
					addMethod(target,"public void update(%s dmo);","org.apache.ibatis.annotations.UpdateProvider","update",dmoClass,columnMap));
			target.addMethod(
					addMethod(target,"public %s selectOne(%s dmo);","org.apache.ibatis.annotations.SelectProvider","selectOne",dmoClass,columnMap));
			target.addMethod(
					addMethod(target,"public java.util.List selectList(%s dmo);","org.apache.ibatis.annotations.SelectProvider","selectList",dmoClass,columnMap));
			target.addMethod(
					addMethod(target,"public Long selectCount(%s dmo);","org.apache.ibatis.annotations.SelectProvider","selectCount",dmoClass,columnMap));

		} catch (CannotCompileException e) {
			e.printStackTrace();
		}
		Class<?> cls = null;

		try {
			cls = target.toClass();

		} catch (CannotCompileException e) {

			throw new AppException(e.getMessage());
		}
		return cls;
	}

	/**
	 *  鏂规硶鍚嶇О,娉ㄨВ绫诲瀷,sql鏂规硶
	 * @throws CannotCompileException 
	 * 
	 */
	public static CtMethod addMethod(CtClass ctType,String methodName,String annotation,String sqlMethod,Class<?> dmoClass,ColumnMap columnMap)
			throws CannotCompileException {

		ClassFile cf = ctType.getClassFile();
		ConstPool constpool = cf.getConstPool();
		AnnotationsAttribute aa = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);

		//娉ㄨВ鍚嶇О
		Annotation ant = new Annotation(String.format(annotation, dmoClass.getName()), constpool);

		//澧炲姭type娉ㄨВ
		ClassMemberValue typeMember = new ClassMemberValue(String.format("%sProvider", dmoClass.getSimpleName()),constpool);
		ant.addMemberValue("type", typeMember);

		//澧炲姞method娉ㄨВ
		StringMemberValue methodMember = new StringMemberValue(sqlMethod, constpool);
		ant.addMemberValue("method", methodMember);

		aa.addAnnotation(ant);

		if(methodName.contains("insert")) { 

			Annotation selectKey = 
					new Annotation("org.apache.ibatis.annotations.SelectKey", constpool);
			if(columnMap.getKeyGenratoyType() == GenerationType.IDENTITY) {

				//添加statement
				StringMemberValue[] statement = new StringMemberValue[]{
						new StringMemberValue(columnMap.getGeneratorStatement(),constpool)};
				ArrayMemberValue statements = new ArrayMemberValue(constpool);
				statements.setValue(statement);
				selectKey.addMemberValue("statement", statements);

				//before
				BooleanMemberValue before = new BooleanMemberValue(false, constpool);
				selectKey.addMemberValue("before", before);

				for(String prop:columnMap.getKeyProperty().keySet()){

					StringMemberValue keyProperty = new StringMemberValue(prop, constpool);
					selectKey.addMemberValue("keyProperty", keyProperty);
				}

				//澧炲姭type娉ㄨВ
				ClassMemberValue resultType = new ClassMemberValue("java.lang.Long",constpool);
				selectKey.addMemberValue("resultType", resultType);

				aa.addAnnotation(selectKey);
			}else if(columnMap.getKeyGenratoyType() == GenerationType.SEQUENCE) {
				//添加statement
				StringMemberValue[] statement = new StringMemberValue[]{
						new StringMemberValue(columnMap.getGeneratorStatement(),constpool)};
				ArrayMemberValue statements = new ArrayMemberValue(constpool);
				statements.setValue(statement);
				selectKey.addMemberValue("statement", statements);
				
				
				//before
				BooleanMemberValue before = new BooleanMemberValue(false, constpool);
				selectKey.addMemberValue("before", before);

				for(String prop:columnMap.getKeyProperty().keySet()){

					StringMemberValue keyProperty = new StringMemberValue(prop, constpool);
					selectKey.addMemberValue("keyProperty", keyProperty);
				}

				//澧炲姭type娉ㄨВ
				ClassMemberValue resultType = new ClassMemberValue("java.lang.Long",constpool);
				selectKey.addMemberValue("resultType", resultType);

				aa.addAnnotation(selectKey);
			}
		}

		CtMethod method = CtMethod.make(String.format(methodName, new Object[]{dmoClass.getName(),
				dmoClass.getName()}), ctType);
		if(methodName.contains("List")) {

			String genericSignature = String.format("(L%s;)Ljava/util/List<L%s;>;", new Object[] { dmoClass.getName(), dmoClass.getName() }).replace('.', '/');
			method.setGenericSignature(genericSignature);
		}
		//澧炲姞娉ㄨВ灞炴��
		method.getMethodInfo().addAttribute(aa);
		return method;
	}

}
