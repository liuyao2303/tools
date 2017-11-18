/**
 *
 * 定义常见的错误代码，可以根据错误代码进行问题跟踪
 *
 */

package com.ccq.framework.lang;

/**
 * Created by xiaoliu on 2017/9/12.
 */
public class ErrorCode {

    /* 用来返回标识前台传输的参数不合法 */
    public static final String PARAM_INVILD = "1096";
    public static final java.lang.String DATA_EMPTY = "1067";
    public static final String INTERNAL_ERROR = "0317";

    //1404代表add或delete或update操作失败
    public static final String OPERATEFAIL ="1404";

    //输入数据为空
    public static final String INPUT_EMPTY="1000";
    //输入数据不合法
    public static final String INPUT_INVALID="1001";
    //查询
    public static final String QUERY_EXCEPTION="1002";
    //插入异常
    public static final String INSERT_EXCEPTION="1003";
    //更新异常
    public static final String UPDATE_EXCEPTION="1004";
    //删除异常
    public static final String DELETE_EXCEPTION="1005";
    //暂无可借图书
    public static final String BORROW_NOBOOKS="1006";

}
