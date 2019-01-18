package com.mxcg.core.queue;


import com.mxcg.core.exception.TofocusException;

public interface ExceptionCallBack<MSG>
{
    /**
     * 
     * 发送到远端发生异常
     * 
     * @param msg
     * @param ex
     */
    void exception(MSG msg, TofocusException ex);
}
