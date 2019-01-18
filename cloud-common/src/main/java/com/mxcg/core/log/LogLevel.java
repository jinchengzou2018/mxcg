package com.mxcg.core.log;

public enum LogLevel
{
    Mini, //String, Err, Exception(屏幕显示 异常的message,不显示堆栈)
    Info, //String, Err, Info, Exception(屏幕显示 异常的message,不显示堆栈)
    Detail, //String, Err, Info, Detail, Exception(屏幕显示 异常的message,不显示堆栈)
    Debug //String, Err, Info, Detail, Debug, Exception(屏幕显示 异常显示堆栈)
}
