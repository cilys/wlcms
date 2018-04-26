package com.cily.wlcms.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.cily.utils.base.StrUtils;
import com.cily.wlcms.web.conf.SQLParam;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


public class BaseImagesController extends BaseController {
	
	protected Map<String, String> getImages(String filePath) throws FileUploadException, IOException{
		HttpServletRequest request = this.getRequest();
		request.setCharacterEncoding("utf-8");  //设置编码
		
		//获得磁盘文件条目工厂  
        DiskFileItemFactory factory = new DiskFileItemFactory();
        
        //如果没以下两行设置的话，上传大的 文件 会占用 很多内存
        //设置暂时存放的 存储室 , 这个存储室，可以和 最终存储文件 的目录不同
        /** 
         * 原理 它是先存到 暂时存储室，然后在真正写到 对应目录的硬盘上
         * 按理来说 当上传一个文件时，其实是上传了两份，第一个是以 .tem 格式的
         * 然后再将其真正写到 对应目录的硬盘上
         */  
        factory.setRepository(new File(filePath));
        //设置 缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室
        factory.setSizeThreshold(1024*1024);
          
        //高水平的API文件上传处理
        ServletFileUpload upload = new ServletFileUpload(factory);
        
        Map<String, String>map_result = new HashMap<String, String>();
        
        //可以上传多个文件  
        List<FileItem> list = (List<FileItem>)upload.parseRequest(request);
        for(FileItem item : list){
        	//获取表单的属性名字
            String name = item.getFieldName();
            //如果获取的 表单信息是普通的 文本 信息  
            if(item.isFormField()){
            	//获取用户具体输入的字符串 ，名字起得挺好，因为表单提交过来的是 字符串类型的  
                String value = item.getString("UTF-8") ;
                  
                request.setAttribute(name, value);
                
                map_result.put(name, value);
            }else{
            	//对传入的非 简单的字符串进行处理 ，比如说二进制的 图片，电影这些 
            	/**
                 * 以下三步，主要获取 上传文件的名字
                 */
                //获取路径名
                String value = item.getName();
                //索引到最后一个反斜杠
//                int start = value.lastIndexOf("\\");
//                //截取 上传文件的 字符串名字，加1是 去掉反斜杠
//                String filename = value.substring(start+1);
                String fileEnd = value.substring(value.lastIndexOf("."));
                UUID uuid = UUID.randomUUID();
                String fileName = uuid.toString().replaceAll("-", "") + fileEnd;
                request.setAttribute(name, fileName);
                String lastImgUrl = map_result.get(name);
                if (StrUtils.isEmpty(lastImgUrl)){
                    lastImgUrl = fileName;
                }else {
                    if (lastImgUrl.endsWith(",")){
                        lastImgUrl = lastImgUrl + "," + fileName;
                    }else {
                        lastImgUrl = "," + lastImgUrl + "," + fileName;
                    }
                }
                map_result.put(SQLParam.RECORD_IMG_URL, lastImgUrl);

//                map_result.put(name, fileName);
                //真正写到磁盘
                //手动写的
                OutputStream out = new FileOutputStream(new File(filePath, fileName));
                InputStream in = item.getInputStream();
                  
                int length = 0;
                byte [] buf = new byte[1024];

                // in.read(buf) 每次读到的数据存放在   buf 数组中
                while( (length = in.read(buf) ) != -1){
                    //在   buf 数组中 取出数据 写到 （输出流）磁盘上
                    out.write(buf, 0, length);
                }
                in.close();
                out.close();
            }
        }
        return map_result;
	}
}