package com.mxcg.core.exception;

import com.mxcg.core.exception.util.ErrorConfig;
import com.mxcg.core.exception.util.ExceptionBean;
import com.mxcg.core.json.JsonUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;


/**
 * 
 * Tofocus 异常
 * <p>说明：
 * </p>
 * <p>1. 自定义异常通过实现TofocusException(String excCode, Throwable cause)，TofocusException(String excCode)或TofocusException(String excCode, Object[] args)等构造方法，
 *     前提是resultcodedesc.config.properties中有配置excCode，带参数的参考3.
 *     </p>
 * <p>示例：
 *      try
 *      {
 *           int h = 2 /(5-5); // by zero
 *            System.out.println(h);
 *      }
 *      catch (Exception ex2)
 *      {
 *            throw new WikiException("1111111", ex2);
 *            SimpleLog.outException(w);
 *      }
 *      </p>
 * <p>2. 自定义异常通过实现TofocusException(String excCode, String customizedMessage, Throwable cause)等构造方法，可传递自定义的错误描述。</p>
 * <p>示例：
 *        try
 *       {
 *            int h = 2 /(5-5); // by zero
 *            System.out.println(h);
 *       }
 *       catch (Exception ex2)
 *       {
 *            throw new WikiException("1111111", "计算错误", ex2);
 *            SimpleLog.outException(w);
 *       }
 *       </p>
 * <p>3. 自定义异常通过实现TofocusException(String excCode, Object[] args, Throwable cause)等构造方法，可传递参数列表，
 *     前提是pip.resultcodedesc.config.properties中有配置excCode，如 -- [88888888888=祝福你{0},我是{1}。]。
 *     </p>
 * <p>示例：
 *    try
 *        {
 *             int h = 2 /(5-5); // by zero
 *             System.out.println(h);
 *        }
 *        catch (Exception ex2)
 *        {
 *            throw new WikiException("88888888888", new Object[]{"中国", "红领巾"}, ex2);
 *            SimpleLog.outException(w);
 *        }
 *        </p>
 * <p>通过getExceptionCode()方法取得异常码，getExceptionMessage()方法取得异常描述。
 * </p>
 * 
 * @author  huhao
 * @version  [版本号, 2013-9-29]
 */
public class TofocusException extends RuntimeException
{
    private static final long serialVersionUID = 477816283856688396L;
    
    protected ExceptionBean exceptionBean;
    
    private Throwable nestedThrowable;
    
    /**
     * 构造函数
     */
    public TofocusException()
    {
        exceptionBean = new ExceptionBean();
        exceptionBean.setCode(UNKNOW_INTER_FAIL);
        exceptionBean.setMessage(ErrorConfig.getMsg(UNKNOW_INTER_FAIL));
        nestedThrowable = null;
    }
    
    /**
     * 构造函数
     * @param cause 异常堆栈信息
     */
    public TofocusException(Throwable cause)
    {
        exceptionBean = new ExceptionBean();
        exceptionBean.setCode(UNKNOW_INTER_FAIL);
        exceptionBean.setMessage(ErrorConfig.getMsg(UNKNOW_INTER_FAIL));
        nestedThrowable = cause;
    }
    
    /**
     * 构造函数
     * @param exceptionCode 异常码
     */
    public TofocusException(String exceptionCode)
    {
        exceptionBean = new ExceptionBean();
        nestedThrowable = null;
        exceptionBean.setCode(exceptionCode);
        exceptionBean.setMessage(ErrorConfig.getMsg(exceptionCode));
    }
    
    /**
     * 构造函数
     * @param exceptionCode 异常码
     * @param cause 异常堆栈信息
     */
    public TofocusException(String exceptionCode, Throwable cause)
    {
        exceptionBean = new ExceptionBean();
        nestedThrowable = null;
        exceptionBean.setCode(exceptionCode);
        exceptionBean.setMessage(ErrorConfig.getMsg(exceptionCode));
        nestedThrowable = cause;
    }
    
    /**
     * 构造函数
     * @param exceptionCode 异常码
     * @param cause 异常堆栈信息
     * @param exceptionArgs 异常参数
     */
    public TofocusException(String exceptionCode, Throwable cause, Object... exceptionArgs)
    {
        exceptionBean = new ExceptionBean();
        nestedThrowable = null;
        nestedThrowable = cause;
        exceptionBean.setCode(exceptionCode);
        exceptionBean.setMessage(ErrorConfig.getMsg(exceptionCode));
        exceptionBean.setParameters(exceptionArgs);
    }
    
    /**
     * 构造函数
     * @param exceptionCode 异常码
     * @param customizedMessage 自定义描述
     */
    public TofocusException(String exceptionCode, String customizedMessage)
    {
        exceptionBean = new ExceptionBean();
        nestedThrowable = null;
        exceptionBean.setCode(exceptionCode);
        exceptionBean.setMessage(ErrorConfig.getMsg(exceptionCode));
        exceptionBean.setCustomizedMessage(customizedMessage);
    }
    
    /**
     * 构造函数
     * @param exceptionCode 异常码
     * @param customizedMessage 自定义描述
     * @param cause 异常堆栈信息
     */
    public TofocusException(String exceptionCode, String customizedMessage, Throwable cause)
    {
        exceptionBean = new ExceptionBean();
        nestedThrowable = null;
        nestedThrowable = cause;
        exceptionBean.setCode(exceptionCode);
        exceptionBean.setMessage(ErrorConfig.getMsg(exceptionCode));
        exceptionBean.setCustomizedMessage(customizedMessage);
    }
    
    /**
     * 构造函数
     * @param exceptionCode 异常码
     * @param customizedMessage 自定义描述
     * @param cause 异常堆栈信息
     * @param exceptionArgs 异常参数
     */
    public TofocusException(String exceptionCode, String customizedMessage, Throwable cause, Object... exceptionArgs)
    {
        exceptionBean = new ExceptionBean();
        nestedThrowable = null;
        nestedThrowable = cause;
        exceptionBean.setCode(exceptionCode);
        exceptionBean.setMessage(ErrorConfig.getMsg(exceptionCode));
        exceptionBean.setCustomizedMessage(customizedMessage);
        exceptionBean.setParameters(exceptionArgs);
    }
    
    /**
     * 获取异常错误码
     * @return 错误码
     */
    public String getExceptionCode()
    {
        return exceptionBean.getCode();
    }
    
    /**
     * 获取异常描述, 不包含堆栈信息
     * @return 异常描述
     */
    public String getExceptionMessage()
    {
        //默认优先取自定义的错误描述
        StringBuilder sb = new StringBuilder();
        if (exceptionBean.getMessage() != null)
        {
            sb.append(exceptionBean.getMessage());
        }
        if (exceptionBean.getCustomizedMessage() != null)
        {
            sb.append("[");
            sb.append(exceptionBean.getCustomizedMessage());
            sb.append("]");
        }
        if (exceptionBean.getParameters() != null)
        {
            sb.append("，参数：");
            sb.append(JsonUtil.toString(exceptionBean.getParameters()));
        }
        if (sb.length() == 0)
            return "UNKNOW_ERROR";
        else
            return sb.toString();
    }
    
    /**
     * 重写异常类的getMsaage方法, 不包含上级异常信息
     */
    @Override
    public String getMessage()
    {
        StringBuilder sb = new StringBuilder();
        if (exceptionBean.getCode() != null)
        {
            sb.append(exceptionBean.getCode());
            sb.append(":");
        }
        if (exceptionBean.getMessage() != null)
        {
            sb.append(exceptionBean.getMessage());
        }
        if (exceptionBean.getCustomizedMessage() != null)
        {
            sb.append("[");
            sb.append(exceptionBean.getCustomizedMessage());
            sb.append("]");
        }
        if (getCause() != null)
        {
            sb.append(" <-- ");
            sb.append(getCause().getMessage());
        }
        if (exceptionBean.getParameters() != null)
        {
            sb.append("，参数：");
            sb.append(JsonUtil.toString(exceptionBean.getParameters()));
        }
        return sb.toString();
    }
    
    /**
     * 包含堆栈信息
     */
    public String getStackTraceMessage()
    {
        StringBuilder sb = new StringBuilder();
        if (exceptionBean.getCode() != null)
        {
            sb.append(exceptionBean.getCode());
            sb.append(":");
        }
        if (exceptionBean.getMessage() != null)
        {
            sb.append(exceptionBean.getMessage());
        }
        if (exceptionBean.getCustomizedMessage() != null)
        {
            sb.append("[");
            sb.append(exceptionBean.getCustomizedMessage());
            sb.append("]");
        }
        if (getCause() != null)
        {
            sb.append(" <-- ");
            StringWriter stringWriter = new StringWriter();
            PrintWriter writer = new PrintWriter(stringWriter);
            getCause().printStackTrace(writer);
            StringBuffer buffer = stringWriter.getBuffer();
            
            sb.append(buffer.toString());
        }
        if (exceptionBean.getParameters() != null)
        {
            sb.append("，参数：");
            sb.append(JsonUtil.toString(exceptionBean.getParameters()));
        }
        return sb.toString();
    }
    
    public Map<String, Object> getAttributes()
    {
        return exceptionBean.getAttributes();
    }
    
    public void setAttributes(Map<String, Object> attrs)
    {
        exceptionBean.setAttributes(attrs);
    }
    
    public void setMessage(String msg)
    {
        exceptionBean.setMessage(msg);
    }
    
    public Throwable getCause()
    {
        return nestedThrowable;
    }
    
    public Object[] getParamers()
    {
        return exceptionBean.getParameters();
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName());
        builder.append(" [ExceptionCode=");
        builder.append(this.getExceptionCode());
        builder.append(", ExceptionDescription=");
        builder.append(this.getMessage());
        builder.append("]");
        return builder.toString();
    }
    
    /**
     * 成功响应返回码
     */
    public static final String SUCCESS_CODE = "0";
    
    /**
     * 服务器异常
     */
    public static final String UNKNOW_INTER_FAIL = "99999999";
    
    /**
     * 未实现的方法
     */
    public static final String UNIMPLENT_FUNCTION = "90000000";
    
    /**
     * 未知站点
     */
    public static final String UNKOWN_STATION = "90000001";
    
    /**
     * 必要参数为空
     */
    public static final String REQUIRED_PRARAM_NULL = "90000002";
    
    /**
     * 必要参数格式错误
     */
    public static final String REQUIRED_PRARAM_ERROR = "90000003";
    
    /**
     * 数据库错误
     */
    public static final String DB_ERROR = "90000004";
    
    /**
     * 手机号码不合法
     */
    public static final String PHONE_ERROR = "90000005";
    
    /**
     * 服务访问失败
     */
    public static final String SERVER_CANTACCESS = "90000006";
    
    /**
     * 远程调用异常
     */
    public static final String PRC_EXCEPTION = "90000007";

    /**
     * 远程调用可重试异常
     */
    public static final String PRC_RETRY_EXCEPTION = "90000008";
    /**
     * 未激活
     */
    public static final String UNACTIVE = "90000009";

    public static class CacheResultCode
    {
        
        /**
         * 缓存读取初始化数据失败
         */
        public static final String CACHE_INIERR = "00100001";
        
        /**
         * 删除节点失败
         */
        public static final String DELETE_NODE_FAIL = "00100002";
        
        /**
         * 主键不可为空
         */
        public static final String KEY_IS_NULL = "00100003";
        
        /**
         * 主键已存在
         */
        public static final String KEY_IS_EXIST = "00100004";
        
        /**
         * 主键不存在
         */
        public static final String KEY_IS_NOT_EXIST = "00100005";
        
        /**
         * 索引不可为空
         */
        public static final String INDEX_IS_NULL = "00100006";
        
        /**
         * 索引违反唯一性
         */
        public static final String INDEX_IS_UNUNIQ = "00100007";
        
        /**
         * 要修改的值不存在
         */
        public static final String OLD_VALUE_IS_NOT_EXIST = "00100008";
        
        /**
         * 父节点不存在
         */
        public static final String PARENT_NODE_IS_NOT_EXIST = "00100009";
        
        /**
         * 存在子节点，不能删除
         */
        public static final String HAS_CHILD_NODE = "00100010";
        
        /**
         * 父节点存在死循环
         */
        public static final String PARENTS_ID_IS_CYCLE = "00100011";
    }
}
