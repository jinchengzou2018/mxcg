package com.mxcg.core.queue;

public interface SendCallBack<RESULT>
{
    /**
     * 发送到远端完成
     * @param result
     */
    void done(RESULT result);
}
