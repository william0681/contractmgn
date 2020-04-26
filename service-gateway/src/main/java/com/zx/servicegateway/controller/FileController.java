package com.zx.servicegateway.controller;

import com.alibaba.fastjson.JSONObject;
import com.zx.servicegateway.pojo.Message;
import com.zx.servicegateway.util.FileUtil;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class FileController {

    @Value("${pdf.path}")
    private String FILE_DIR;


    @GetMapping("download")
    @RequiresRoles(value = {"manager","accountant","overallchief","areachief"},logical = Logical.OR)
    public void download(HttpServletResponse response, @RequestParam String fileName) {

        //通过文件的保存文件夹路径加上文件的名字来获得文件
        File file = new File(FILE_DIR, fileName);
        //当文件存在
        if (file.exists()) {
            //首先设置响应的内容格式是force-download，那么你一旦点击下载按钮就会自动下载文件了
            response.setContentType("application/force-download");
            //通过设置头信息给文件命名，也即是，在前端，文件流被接受完还原成原文件的时候会以你传递的文件名来命名
            response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
            //进行读写操作
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                //从源文件中读
                int i = bis.read(buffer);
                while (i != -1) {
                    //写到response的输出流中
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //善后工作，关闭各种流
                try {
                    if (bis != null) {
                        bis.close();
                    }
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @PostMapping("upload")
    @ResponseBody
    @RequiresRoles(value = {"manager","accountant","overallchief","areachief"},logical = Logical.OR)
    public Message upload(@RequestParam("file") MultipartFile file){

        JSONObject object = new JSONObject();
        String originalFilename = file.getOriginalFilename();
        try {
            String filename = System.currentTimeMillis()+"_"+originalFilename;
            file.transferTo(FileUtil.getFile(filename, FILE_DIR));
            object.put("filename",filename);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Message.createSuc(object);
    }
}
