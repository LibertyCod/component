package com.greencloud.cos.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.greencloud.cos.service.ICOSService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.meta.InsertOnly;
import com.qcloud.cos.request.CreateFolderRequest;
import com.qcloud.cos.request.UploadFileRequest;
import com.qcloud.cos.sign.Credentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * @author Binbin Wang
 * @date 2017/11/2
 */
@Service("cosService")
public class COSServiceImpl implements ICOSService {

    private static final Logger LOG = LoggerFactory.getLogger(COSServiceImpl.class);

    @Autowired
    private Credentials credentials;
    @Autowired
    private ClientConfig clientConfig;
    @Value("${bucket.name}")
    private String bucketName;

    /**
     * 返回云对象路径
     * @param localFile
     * @param cosFilePath
     * @return
     */
    @Override
    public String uploadFile(File localFile, String cosFilePath) {
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        String fileAccessPath = null;
        try {
            fis = new FileInputStream(localFile);
            bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int len;
            while((len = fis.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            byte[] contentBtye = bos.toByteArray();
            fileAccessPath = uploadFile(contentBtye, cosFilePath);
        } catch (FileNotFoundException e) {
            LOG.error(e.getMessage());
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }finally {
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                LOG.error(e.getMessage());
            }
            try {
                fis.close();
            } catch (IOException e) {
                LOG.error(e.getMessage());
            }
        }
        return fileAccessPath;
    }

    /**
     * 返回腾讯云对象路径
     * @param contentBuffer
     * @param cosFilePath
     * @return
     */
    @Override
    public String uploadFile(byte[] contentBuffer, String cosFilePath) {
        //创建COSClient
        COSClient cosClient = new COSClient(clientConfig, credentials);
        //先创建目录
        createFold(cosClient, cosFilePath);
        UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, cosFilePath, contentBuffer);
        //设置覆盖式上传
        uploadFileRequest.setInsertOnly(InsertOnly.OVER_WRITE);
        String uploadFileRet = cosClient.uploadFile(uploadFileRequest);
        cosClient.shutdown();
        JSONObject uploadJsonObject = (JSONObject) JSONObject.parse(uploadFileRet);
        Integer code = uploadJsonObject.getInteger("code");
        if (code != 0) {
            return null;
        } else {
            String fileAccessPath = uploadJsonObject.getJSONObject("data").getString("source_url").replace("http://", "https://");
            return fileAccessPath;
        }

    }

    private String createFold(COSClient cosClient, String cosFilePath) {
        String foldPath = cosFilePath.substring(0, cosFilePath.lastIndexOf("/") + 1);
        CreateFolderRequest createFolderRequest = new CreateFolderRequest(bucketName, foldPath);
        String createFolderReturn = cosClient.createFolder(createFolderRequest);
        return createFolderReturn;
    }
}
