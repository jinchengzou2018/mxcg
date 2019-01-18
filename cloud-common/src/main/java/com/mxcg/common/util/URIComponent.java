package com.mxcg.common.util;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by wgj on 2016/6/23.
 */
public class URIComponent
{

    public static String encode(String s, String enc) throws Exception
    {
        return URLEncoder.encode(s, enc).replace("+", "%20");
    }

    public static String encode(String s) throws Exception
    {
        return encode(s, "utf-8");
    }

    public static String decode(String s, String enc) throws Exception
    {
        return URLDecoder.decode(s, enc);
    }

    public static String decode(String s) throws Exception
    {
        return decode(s, "utf-8");
    }

}
