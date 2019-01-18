package com.mxcg.common;


import org.apache.http.HttpStatus;



public class Constant {

    public static class Code implements HttpStatus {

        // feign客户端调用失败
        public final static int SC_FEIGN_FALLBACK = 6001;

    }

    public static class Message {
        public final static String SC_OK = "操作成功.";
        public final static String SC_INTERNAL_SERVER_ERROR = "操作失败(服务器内部错误).";
        public final static String SC_FEIGN_FALLBACK = "服务器未连接~请稍后重试!";
        public final static String SC_FORBIDDEN = "没有权限访问该资源~";
        public final static String SC_UNAUTHORIZED = "授权失败~";
    }






//    /**
//     * 微服务名统一管理
//     */
    public static class Service {
        public final static String COULD_REGISTRATION = "cloud-registration";
        public final static String CLOUD_CONFIG = "cloudconfig";
        public final static String CLOUD_GATEWAY = "cloud-gateway";
        public final static String CLOUD_AUTHORITY = "cloud-author";
        public final static String CLOUD_ACCOUNT = "cloud-account";
    }



    public static class Auth {
        public final static String TOKEN_HEADER = "Authorization";
        public final static String TOKEN_VALUE_PREV = "Bearer ";
        // TODO: JWT签名,暂时写死
        public final static String JWT_SIGNING_KEY = "zoujincheng";
    }



}
