package com.greencloud.cos;

import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.sign.Credentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Binbin Wang
 * @date 2017/12/22
 */
@Configuration
public class COSConfig {

    @Value("${bucket.appId}")
    private Long bucketAppId;
    @Value("${bucket.secretID}")
    private String bucketSecretId;
    @Value("${bucket.secretKey}")
    private String bucketSecretKey;
    @Value("${bucket.region}")
    private String bucketRegion;

    @Bean
    public ClientConfig clientConfig() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setRegion(bucketRegion);
        return clientConfig;
    }

    @Bean
    public Credentials credentials() {
        return new Credentials(bucketAppId, bucketSecretId, bucketSecretKey);
    }

}