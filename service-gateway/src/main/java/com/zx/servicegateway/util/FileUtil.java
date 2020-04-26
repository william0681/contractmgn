package com.zx.servicegateway.util;

import java.io.File;

public class FileUtil {
    public static File getFile(String fileName ,String SFileDir){
        File fileDir = new File(SFileDir);
        //目录不存在,则创建目录
        if(!fileDir.exists()){
            fileDir.mkdir();
        }
        return new File(SFileDir+fileName);
    }

}
