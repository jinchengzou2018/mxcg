package com.mxcg.core.queue;

public interface RemoteCallBack<MSG, BACK>
{
    /**
     * 远端回调
     * @param callbackMsg
     */
    void callBack(BACK callbackMsg);

    /**
     * 远端回调超时
     * @param callbackMsg
     */
    void timeout(MSG msg);
}
