package com.ydw.oa.wechat.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @title: RedisConfig
 * @description: Redis配置类
 * @author: zhangkai
 * @create: 2019-09-29 15:30
 * @version:1.0.0
 **/
@Configuration
public class RedisConfig {

    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
//        log.info("-----------------------加载redis缓存配置开始-----------------------");


        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(factory);

        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
//        Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);

//        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jacksonSeial.setObjectMapper(om);
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        //使用StringRedisSerializer来序列化和反序列化redis的key值
//        template.setKeySerializer(stringRedisSerializer);
        // 值采用json序列化
//        template.setValueSerializer(jacksonSeial);
        // 设置hash key 和value序列化模式
//        template.setHashKeySerializer(stringRedisSerializer);
//        template.setHashValueSerializer(jacksonSeial);
        template.afterPropertiesSet();

        log.info("-----------------------> 加载redis缓存配置 <-----------------------");
        return template;
    }

}
