package redis.cache;


public class RedisPrefix //implements CacheKeyPrefix
{



//    private final String top = "cache";
//    
//    private final String prefix;
//    
//    private final String application;
//    
//    private final RedisSerializer serializer;
//    
//    private final String fullpath;
//
//    public RedisPrefix(String prefix, String application)
//    {
//        this.prefix = prefix;
//        this.application = application;
//        this.serializer = new StringRedisSerializer();
//        
//        StringBuilder sb = new StringBuilder();
//        sb.append(top);
//        sb.append(":");
//        sb.append(prefix);
//        sb.append(":");
//        sb.append(application);
//        sb.append(":");
//        fullpath = sb.toString();
//    }
//
//    @Override
//    public byte[] prefix(String cacheName)
//    {
//        return this.serializer.serialize(fullpath + cacheName + ":");
//    }
    
}
