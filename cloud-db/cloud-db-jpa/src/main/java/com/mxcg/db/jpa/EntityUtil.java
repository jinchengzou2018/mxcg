package com.mxcg.db.jpa;

import java.util.ArrayList;
import java.util.List;

public class EntityUtil
{
    public static <T> List<T> getEntitis(List<? extends HaveEntity> list, Class<T> clazz)
    {
        List<T> result = new ArrayList<>();
        for(HaveEntity m : list)
        {
            result.add((T)m.getEntity());
        }
        return result;
    }
}
