package com.mxcg.core.exception;

import java.io.UnsupportedEncodingException;


import com.mxcg.core.Result;
import com.mxcg.core.exception.util.ErrorConfig;
import com.mxcg.core.json.JsonObject;
import feign.FeignException;
import feign.hystrix.FallbackFactory;

public abstract class AbstractFallbackFactory<T> implements FallbackFactory<T>
{
    protected Result createExceptionResult(Throwable cause)
    {
        Result result = new Result<>();
        if (cause instanceof FeignException)
        {
            if (((FeignException)cause).status() == 500)
            {
                String[] array = cause.getMessage().split("\n");
                if (array != null && array.length > 1)
                {
                    try
                    {
                        JsonObject json = new JsonObject(array[1]);
                        String[] ss = json.get("message").toString().split(":");
                        if (ss != null && ss.length > 1)
                        {
                            result.setCode(ss[0]);
                            result.setDescript(ss[1]);
                            return result;
                        }
                    }
                    catch (UnsupportedEncodingException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        result.setCode(TofocusException.UNKNOW_INTER_FAIL);
        String descript = "[" + ErrorConfig.getMsg(TofocusException.UNKNOW_INTER_FAIL) + "]" + cause.toString();
        result.setDescript(descript);
        return result;
    }
}
