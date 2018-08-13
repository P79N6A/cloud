package com.springframework.dfs.configure;

/**
 * @author summer
 * 2018/8/13
 */
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
@Component
public class FastDFSClientWrapper {
    private final Logger log = LoggerFactory.getLogger(FastDFSClientWrapper.class);
    @Autowired
    private FastFileStorageClient storageClient;
    /**
     * 上传文件
     *
     * @param file 文件对象
     * @return 服务器存储地址
     * @throws IOException
     */
    public String uploadFile(MultipartFile file) throws IOException {
        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(),
                FilenameUtils.getExtension(file.getOriginalFilename()), null);
        return storePath != null ? storePath.getFullPath() : null;
    }
    /**
     * 下载文件
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public byte[] downloadFile(String filePath) throws IOException {
        StorePath storePath = StorePath.praseFromUrl(filePath);
        return storageClient.downloadFile(storePath.getGroup(), storePath.getPath(), ins -> inputStreamToByte(ins));
    }

    /**
     * 获取byte流
     *
     * @param inputStream
     * @return
     */
    private byte[] inputStreamToByte(InputStream inputStream) throws IOException {
        return IOUtils.toByteArray(inputStream);
    }
}