package com.sky.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class COSUtil {
    // 1 初始化用户身份信息（secretId, secretKey）
    private String secretId;
    private String secretKey;
    private String apRegion;
    private String bucketName;

    private COSCredentials cred;
    private Region region;
    private ClientConfig clientConfig;

    public COSUtil(String secretId, String secretKey, String apRegion, String bucketName) {
        this.secretId = secretId;
        this.secretKey = secretKey;
        this.apRegion = apRegion;
        this.bucketName = bucketName;

        this.cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的地域
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法
        this.region = new Region(apRegion);
        this.clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端
        this.clientConfig.setHttpProtocol(HttpProtocol.https);
    }

    public String upload(MultipartFile multipartFile) throws IOException {

        COSClient cosClient = new COSClient(cred, clientConfig);

        // 避免文件覆盖
        String originalFilename = multipartFile.getOriginalFilename();
        assert originalFilename != null;
        String fileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));

        // 将 MultipartFile 转换成 File
        File tmp = new File( System.getProperty("user.dir")+ "/sky-server/src/main/resources/tmpfiles" + fileName);
        multipartFile.transferTo(tmp);

        // 上传文件到COS
        String key = "multipartfile/sky/" + fileName;
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, tmp);
        cosClient.putObject(putObjectRequest);

        // 删除产生的临时文件
        if (tmp.delete()) {
            log.info("Delete tmp file successfully: {}", tmp.getAbsolutePath());
        } else {
            log.warn("Delete tmp file FAILED: {}", tmp.getAbsolutePath());
        }
        cosClient.shutdown();
        return "https://" + bucketName + ".cos.ap-guangzhou" + ".myqcloud.com/" + key;
    }

}
