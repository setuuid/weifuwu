package com.bit.file.service.impl;

import com.bit.base.vo.BaseVo;
import com.bit.file.component.FastDFSClient;
import com.bit.file.dao.FileInfoDao;
import com.bit.file.model.FileInfo;
import com.bit.file.service.FileService;
import com.bit.file.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
@Transactional
public class FileServiceImpl implements FileService {


    @Autowired
    private FastDFSClient fastDFSClient;

    @Autowired
    private FileInfoDao fileInfoDao;

    @Value("${fastdfs.address}")
    private String fastAddress;

    @Override
    public BaseVo uploadFile(MultipartFile filedata) {
        BaseVo baseVo = new BaseVo();
        FileInfo info = new FileInfo();
        try {
            info.setFileName(FileUtil.getFileName(filedata.getOriginalFilename()));
            info.setSuffix(FileUtil.getExtensionName(filedata.getOriginalFilename()));
            info.setFileSize(filedata.getSize());
            String path = fastDFSClient.uploadFile(filedata.getBytes(), filedata.getOriginalFilename());
            info.setPath(path);
            fileInfoDao.insert(info);
            FileInfo fileInfo = fileInfoDao.findById(info.getId());
            if (fileInfo != null){
                fileInfo.setPath(fastAddress + "/" + fileInfo.getPath());
            }
            baseVo.setData(fileInfo);
            return baseVo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baseVo;
    }


    @Override
    public byte[] downloadFile(Long fileId) {
        byte[] buffer = null;
        List<FileInfo> list = fileInfoDao.query(fileId);
        if (list.size() < 0) {
            //throw new Exception("无此文件");
        } else {
            FileInfo info = list.get(0);
            try {
                buffer = fastDFSClient.downloadFile(info.getPath());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return buffer;
    }

    @Override
    public List<FileInfo> getFileInfo(Long fileId) {
        return fileInfoDao.query(fileId);
    }


    @Override
    public void delFile(Long fileId) throws Exception {
        List<FileInfo> list = fileInfoDao.query(fileId);
        if (!list.isEmpty()) {
            fastDFSClient.deleteFile(list.get(0).getPath());
            fileInfoDao.delete(fileId);
        } else {
            throw new Exception("无此文件");
        }

    }
}
