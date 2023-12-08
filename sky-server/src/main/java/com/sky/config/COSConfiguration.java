package com.sky.config;

import com.sky.properties.COSProperties;
import com.sky.utils.COSUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class COSConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public COSUtil cosUtil(COSProperties cosProperties) {
        log.info("开始创建腾讯云文件上传工具类对象: {}", cosProperties);
        return new COSUtil(
                cosProperties.getSecretId(),
                cosProperties.getSecretKey(),
                cosProperties.getApRegion(),
                cosProperties.getBucketName()
        );
    }
}
