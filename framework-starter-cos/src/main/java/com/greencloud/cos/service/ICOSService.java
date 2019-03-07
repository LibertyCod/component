package com.greencloud.cos.service;

import java.io.File;

/**
 * @author Binbin Wang
 * @date 2017/11/2
 */
public interface ICOSService {

    /**
     * 以File上传文件至COS
     * @param file
     * @param cosFilePath
     * @return
     */
    String uploadFile(File file, String cosFilePath);

    /**
     * 以字节数组上传文件至COS
     * @param contentBuffer
     * @param cosFilePath
     * @return
     */
    String uploadFile(byte[] contentBuffer, String cosFilePath);

}
