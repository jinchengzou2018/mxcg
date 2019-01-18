package redis.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

//@Configuration
//@EnableCaching
@Component
public class RedisCacheConfig //extends CachingConfigurerSupport
{



    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisTemplate<String,Object> redisTemplate() {
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }





//    @Value("${spring.redis.host}")
//    private String host;
//    
//    @Value("${spring.redis.port}")
//    private int port;
//
//    @Value("${spring.redis.password}")
//    private String password;
//
//    @Value("${spring.application.name}")
//    private String application;
//    
//    @Value("${spring.redis.prefix:default}")
//    private String prefix;
//        
//    @Value("${spring.redis.timeout:3600}")
//    private int timeout;
//    
//    @Value("${spring.redis.cachedatabase:0}")
//    private int database;
//    
//    @Value("${spring.redis.pool.max-idle:8}")
//    private int maxIdle;
//    
//    @Value("${spring.redis.pool.min-idle:0}")
//    private int minIdle;
//    
//    @Bean
//    public KeyGenerator keyGenerator() {
//        return new KeyGenerator() {
//          @Override
//          public Object generate(Object target, Method method, Object... params) {
//               StringBuilder sb = new StringBuilder();
//               sb.append(target.getClass().getName());
//               sb.append(method.getName());
//               for (Object obj : params) {
//                   sb.append(obj.toString());
//               }
//               return sb.toString();
//          }
//        };
//    }
//    
//    @Bean
//    public CacheManager cacheManager(RedisTemplate<?, ?> redisTemplate)
//    {
//        RedisCacheManager cacheManager = new RedisCacheManager
//        cacheManager.setUsePrefix(true);
//        RedisCachePrefix cachePrefix = new RedisPrefix(prefix, application);
//        cacheManager.setCachePrefix(cachePrefix);
//        
//        cacheManager.setDefaultExpiration(3600);
//        return cacheManager;
//    }
//    
//    @Bean
//    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory connectionFactory)
//    {
//        RedisTemplate<?, ?> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory);
//        
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setHashKeySerializer(new StringRedisSerializer());
//        
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//        
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//        template.setHashValueSerializer(jackson2JsonRedisSerializer);
//        
//        template.afterPropertiesSet();
//        return template;
//    }
//    
//    @Bean
//    public JedisConnectionFactory redisConnectionFactory()
//    {
//        JedisConnectionFactory factory = new JedisConnectionFactory();
//        factory.setHostName(host);
//        factory.setPort(port);
//        factory.setPassword(password);
//        //存储的库
//        factory.setDatabase(database);
//        //设置连接超时时间
//        factory.setTimeout(timeout);
//        factory.setUsePool(true);
//        factory.setPoolConfig(jedisPoolConfig());
//        return factory;
//    }
//    
//    @Bean
//    public JedisPoolConfig jedisPoolConfig()
//    {
//        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//        jedisPoolConfig.setMaxIdle(maxIdle);
//        jedisPoolConfig.setMinIdle(minIdle);
//        //      jedisPoolConfig.set ...
//        return jedisPoolConfig;
//    }
//    
//
//    /**
//     * redis数据操作异常处理
//     * 这里的处理：在日志中打印出错误信息，但是放行
//     * 保证redis服务器出现连接等问题的时候不影响程序的正常运行，使得能够出问题时不用缓存
//     * @return
//     */
//    @Bean
//    @Override
//    public CacheErrorHandler errorHandler() {
//        CacheErrorHandler cacheErrorHandler = new CacheErrorHandler() {
//
//            @Override
//            public void handleCacheClearError(RuntimeException e, Cache cache)
//            {
//                System.out.println("redis异常：");
//                e.printStackTrace();
//            }
//
//            @Override
//            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key)
//            {
//
//                System.out.println("redis异常：");
//                e.printStackTrace();
//            }
//
//            @Override
//            public void handleCacheGetError(RuntimeException e, Cache cache, Object key)
//            {
//
//                System.out.println("redis异常：");
//                e.printStackTrace();
//            }
//
//            @Override
//            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value)
//            {
//
//                System.out.println("redis异常：");
//                e.printStackTrace();
//            }
//        };
//        return cacheErrorHandler;
//    }
    
}
