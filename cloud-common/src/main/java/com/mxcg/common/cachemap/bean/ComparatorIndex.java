package com.mxcg.common.cachemap.bean;

import java.util.Comparator;
import java.util.List;

/**
 * 
 * 索引比较器

 */
public class ComparatorIndex<K, Idx extends Comparable<Idx>>
    implements Comparator<HasIndex<K, Idx>>
{
    @Override
    public int compare(HasIndex<K, Idx> arg0, HasIndex<K, Idx> arg1)
    {
        List<Idx> list0 = arg0.getIndex();
        List<Idx> list1 = arg1.getIndex();
        if (list0.size() < list1.size())
        {
            return -1;
        }
        else if (list0.size() > list1.size())
            return 1;
        else
        {
            for (int i = 0; i < list0.size(); i++)
            {
                int result = list0.get(i).compareTo(list1.get(i));
                if (result != 0)
                {
                    return result;
                }
            }
            return 0;
        }
    }
}
