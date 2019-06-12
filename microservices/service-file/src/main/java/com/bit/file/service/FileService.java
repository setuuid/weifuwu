package com.bit.file.service;

import com.bit.base.vo.BaseVo;
import com.bit.file.model.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author lyj
 */
public interface FileService {


    /**
     * @param filedata
     * @return
     */
    BaseVo uploadFile(MultipartFile filedata);

    /**
     * @param fileId
     * @return
     */
    byte[]  downloadFile(Long fileId);

    /**
     * @param fileId
     * @return
     */
    List<FileInfo> getFileInfo(Long fileId);


    /**
     * @param fileId
     * @return
     */
    void  delFile(Long fileId) throws Exception;
}
