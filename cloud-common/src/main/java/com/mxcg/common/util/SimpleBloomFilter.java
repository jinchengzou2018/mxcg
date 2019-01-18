package com.mxcg.common.util;


import java.util.BitSet;


public class SimpleBloomFilter
{
    private static final int DEFAULT_SIZE = 2 << 29;
    
    private static final int[] seeds = new int[] {7, 11, 13, 31, 37, 61,};
    
    private BitSet bits = new BitSet(DEFAULT_SIZE);
    
    private SimpleHash[] func = new SimpleHash[seeds.length];
    
    public SimpleBloomFilter()
    {
        for (int i = 0; i < seeds.length; i++)
        {
            func[i] = new SimpleHash(DEFAULT_SIZE, seeds[i]);
        }
    }
    
    public void add(String value)
    {
        for (SimpleHash f : func)
        {
            bits.set(f.hash(value), true);
        }
    }
    
    public boolean contains(String value)
    {
        if (value == null)
        {
            return false;
        }
        boolean ret = true;
        for (SimpleHash f : func)
        {
            ret = ret && bits.get(f.hash(value));
        }
        return ret;
    }
    
    public static class SimpleHash
    {
        
        private int cap;
        
        private int seed;
        
        public SimpleHash(int cap, int seed)
        {
            this.cap = cap;
            this.seed = seed;
        }
        public int hash(String value)
        {
            int result = 0;
            int len = value.length();
            for (int i = 0; i < len; i++)
            {
                result = seed * result + value.charAt(i);
            }
            return (cap - 1) & result;
        }
        public int hash2(String key)
        {
            int hash = 0, x = 0, i = 0;
            char[] keys = key.toCharArray();
            for (i = 0; i < key.length(); i++)
            {
                hash = (hash << 4) + keys[i];
                if ((x = (int)(hash & 0xF0000000L)) != 0)
                    hash ^= (x >> 24);
                hash &= ~x;
            }
            return hash;
        }
        
        public int hash3(String key)
        {
            //
            final int p = 16777619;
            int hash = (int)2166136261L;
            for (int i = 0; i < key.length(); i++)
                hash = (hash ^ key.charAt(i)) * p;
            hash += hash << 13;
            hash ^= hash >> 7;
            hash += hash << 3;
            hash ^= hash >> 17;
            hash += hash << 5;
            hash = hash ^ (hash >> 10) ^ (hash >> 20);
            
            return hash & 0x7FFFFFFF;
        }
    }
    
    public static void main(String[] args)
    {
        String value = " stone2083@yahoo.cn ";
        SimpleBloomFilter filter = new SimpleBloomFilter();
        System.out.println(filter.contains(value));
        filter.add(value);
        System.out.println(filter.contains(value));
    }
    
}
