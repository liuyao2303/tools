package com.ccq.framework.inceptor;
/**
 *  @author xiaoliu
 *  
 *  异常拦截器，用于对异常的包装并返回
 *  
 */

import com.ccq.framework.exception.AppException;
import com.ccq.framework.lang.Result;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ServiceTraceInceptor implements MethodInterceptor,Ordered{

	public static Logger logger = LoggerFactory.getLogger(ServiceTraceInceptor.class);
	
	@Around("@within(com.ccq.framework.annotation.ServiceTrace)")
	public Object methodInceptor(ProceedingJoinPoint pjp) {
		Object result = null;
		try {
			result = pjp.proceed();
		} catch (Throwable ex) {

		    logger.warn("ServiceTrace Exection Message : " + ex.getCause() + ex.toString());

			if(ex instanceof AppException) {
				Result r = new Result(false,((AppException)ex).getMessage(),((AppException)ex).getCode());
				if(result instanceof Result) {
					return r;
				}
				return r;
			}else {
				Result r = new Result(false,"DATA_ACCESS_EXCEPTION","-1");
				return r;
			}
		}
		
		return result;
	}

	@Override
	public Object invoke(MethodInvocation arg0) throws Throwable {
		
		
		return null;
	}

	/**
	 * Get the order value of this object.
	 * <p>Higher values are interpreted as lower priority. As a consequence,
	 * the object with the lowest value has the highest priority (somewhat
	 * analogous to Servlet {@code load-on-startup} values).
	 * <p>Same order values will result in arbitrary sort positions for the
	 * affected objects.
	 *
	 * 实现最好要比spring的事物小，不会拦截事物要处理的异常
	 * @return the order value
	 * @see #HIGHEST_PRECEDENCE
	 * @see #LOWEST_PRECEDENCE
	 */
	@Override
	public int getOrder() {
		return 5;
	}
}
