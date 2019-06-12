package com.bit.file.controller;


import com.bit.base.controller.BaseController;
import com.bit.base.exception.BusinessException;
import com.bit.base.vo.BaseVo;
import com.bit.common.ResultCode;
import com.bit.file.component.FastDFSClient;
import com.bit.file.model.FileInfo;
import com.bit.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/")
public class FileController extends BaseController {


    @Autowired
    private FastDFSClient fastDFSClient;

    @Autowired
    private FileService fileService;

    /**
     * 上传文件
     * @param filedata :
     * @return : com.bit.base.vo.BaseVo
     * @description:
     * @author liyujun
     * @date 2018-10-13
     */
    @RequestMapping(value = "uploadFile", method = RequestMethod.POST)
    public BaseVo uploadFileNew(@RequestParam MultipartFile filedata) {
        if (filedata != null && !filedata.isEmpty()) {
            return fileService.uploadFile(filedata);
        }else {
            throw new BusinessException("文件不允许为空");
        }
    }


    /**
     * @param fileId : 文件id
     * @return : com.bit.base.vo.BaseVo
     * @description:下载服务
     * @author liyujun
     * @date 2018-10-13
     */
    @RequestMapping(value = "getDownLoadFile/{fileId}", method = RequestMethod.GET)
    public void getDownLoadFile(HttpServletResponse response, @PathVariable("fileId") Long fileId) throws IOException {
        OutputStream toClient = null;
        try {

            List<FileInfo> infoList = new ArrayList<FileInfo>();
            infoList = fileService.getFileInfo(fileId);
            // 判断文件是否存在
            if (infoList.size() > 0) {
                FileInfo info = infoList.get(0);
                byte[] buffer = fastDFSClient.downloadFile(info.getPath());
                // 清空response
                response.reset();
                // 设置response的Header
                response.addHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode(info.getFileName(), "UTF-8").replaceAll("\\+","%20") + "."+info.getSuffix());
                response.addHeader("Content-Length", "" + buffer.length);
                // 通过文件流的形式写到客户端
                toClient = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/octet-stream");
                toClient.write(buffer);

            }
        } catch (IOException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 写完以后关闭文件流
            toClient.flush();
            toClient.close();
        }
    }


    /**
     * @param fileId :
     * @return : com.bit.base.vo.BaseVo
     * @description: 删除某个文件
     * @author liyujun
     * @date 2018-10-15
     */
    @RequestMapping(value = "fileDel/{fileId}", method = RequestMethod.DELETE)
    public BaseVo fileDel(@PathVariable("fileId") Long fileId) {
        BaseVo rs = new BaseVo();
        try {
            fileService.delFile(fileId);
            rs.setCode(ResultCode.SUCCESS.getCode());
            rs.setMsg(ResultCode.SUCCESS.getInfo());
        } catch (Exception e) {
            e.printStackTrace();
            rs.setCode(ResultCode.WRONG.getCode());
            rs.setMsg(e.getMessage());
        }
        return rs;
    }
}