package com.mxcg.core.exception;

public class ResultCode {
    
    public static class AccountResultCode {
        /**
         * 用户不存在
         */
        public static final String UNKNOW_USER = "00200001";
        
        /**
         * 未激活用户登录失败
         */
        public static final String USER_LOGIN_FAIL_BY_ACTIVE = "00200002";
        
        /**
         * 已过期用户登录失败
         */
        public static final String USER_LOGIN_FAIL_BY_EXPIRED = "00200003";
        
        /**
         * 用户登陆密码验证失败
         */
        public static final String USER_LOGIN_FAIL_BY_WRONG_PASSWORD = "00200004";
        
        /**
         * 用户密码验证失败
         */
        public static final String CHECK_PASSWORD_FAIL = "00200005";
        
        /**
         * 验证码错误
         */
        public static final String CHECK_CAPTCHA_FAIL = "00200006";
        
        /**
         * 会话id不存在
         */
        public static final String CHECK_SESSION_FAIL = "00200007";
        
        /**
         * userid已被使用
         */
        public static final String USERID_USED = "00200008";
        
        /**
         * 手机号码已被使用
         */
        public static final String PHONE_USED = "00200009";
        
        /**
         * cliten id 不匹配
         */
        public static final String CLIENT_ID_NOT_MATCH = "00200010";
        
        /**
         * 权限验证失败
         */
        public static final String UNAUTHENTICATION = "00200011";
        
        /**
         * 用户未登录
         */
        public static final String UNLOGIN = "00200012";
        
        /**
         * 密码规则验证失败
         */
        public static final String CODERULES = "00200013";
        
        /**
         * 邮箱规则验证失败
         */
        public static final String EMAILRULES = "00200014";
    }
    
    public static class FileResultCode {
        /**
         * md5值不正确
         */
        public static final String MD5_ERROR = "00400001";
        
        /**
         * 该md5没有对应的文件
         */
        public static final String MD5_FILE_NOTFOUND = "00400002";
        
    }
    
    public static class PosManagerResultCode {
        /**
         * 数据查询失败或数据为空
         */
        public static final String QUERY_ERROR = "10010001";
        
        /**
         * 市场已存在商户
         */
        public static final String MARKETT_ERROR = "10010002";
    }
    
    public static class PayResultCode {
        //商户不存在
        public static final String MER_NON_EXIST = "02400001";
        
        //支付方式异常
        public static final String TRAN_TYPE_ERR = "02400003";
        
        //订单日期异常
        public static final String DATEFORMT_ERR = "02400005";
        
        //AppId异常
        public static final String APPID_ERR = "02400007";
        
        //参数校验失败
        public static final String PARAM_MAC_ERR = "02400009";
        
        //商户订单号已存在
        public static final String MER_ORDERNUM_REPEAT = "02400011";
        
      //订单金额少于0
        public static final String ORDER_AMT_ERR = "02400013";
        
        
        
        
        
    }
}
