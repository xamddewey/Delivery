package com.sky.config;

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
    public COSUtil cosUtil() {
        log.info("开始创建腾讯云文件上传工具类对象: {}", COSUtil.class);
        return new COSUtil();
    }
}
