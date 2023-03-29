package com.tasc.blogging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
@EnableAspectJAutoProxy
@EnableCaching
public class BloggingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BloggingApplication.class, args);
    }

    @Bean(name = "bCryptPasswordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(name = "cacheBlog")
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("blogs");
    }
}
