package com.mxcg.common.util;

public class ComparableUtil
{
    
    public static <T> int compare(Comparable<T> org, T tar)
    {
        if (org == null)
        {
            if (tar == null)
            {
                return 0;
            }
            else
            {
                return -1;
            }
        }
        else
        {
            if (tar == null)
            {
                return 1;
            }
            else
            {
                return org.compareTo(tar);
            }
        }
    }
    
}
