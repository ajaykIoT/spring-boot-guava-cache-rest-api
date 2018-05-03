package com.demoapp.application;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.google.common.cache.CacheBuilder;

@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
@EnableAutoConfiguration
@ComponentScan("com.demoapp")
@EnableJpaRepositories("com.demoapp.repository")
@EntityScan("com.demoapp.model")
public class Application implements CachingConfigurer {

	public final static String CACHE_GREETING = "greetings";
	public final static String CACHE_GREETING1 = "greetings1";
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.out.println("Application Started");
	}	
	
 	@Bean
    @Override
    public CacheManager cacheManager() {
    
 		 SimpleCacheManager cacheManager = new SimpleCacheManager();

         GuavaCache cache1 = new GuavaCache(CACHE_GREETING, CacheBuilder.newBuilder()
                 .expireAfterWrite(60, TimeUnit.MINUTES)
                 .build());
         
         GuavaCache cache2 = new GuavaCache(CACHE_GREETING1, CacheBuilder.newBuilder()
                 .expireAfterWrite(60, TimeUnit.MINUTES)
                 .build());

         cacheManager.setCaches(Arrays.asList(cache1, cache2));

         return cacheManager;
    }

    @Override
    public CacheResolver cacheResolver() {
        return null;
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return null;
    }
	
	

}
