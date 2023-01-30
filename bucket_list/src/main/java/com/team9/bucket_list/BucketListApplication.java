package com.team9.bucket_list;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BucketListApplication {

    public static void main(String[] args) {
        SpringApplication.run(BucketListApplication.class, args);
    }

}
