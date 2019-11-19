package com.sevenpay.agentmanager.controller;

import com.alibaba.fastjson.JSONObject;
import com.sevenpay.agentmanager.pojo.ResultBean;
import com.sevenpay.agentmanager.utils.DateUtils;
import com.sevenpay.agentmanager.utils.YouTuUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传和优图解析
 */
@Controller
@RequestMapping("common")
public class UploadFileController {
    private static final Logger logger = LoggerFactory.getLogger(UploadFileController.class);

    @Value("${images.uri}")
    private String uri;
    @Value("${images.relativePath}")
    private String relativePath;
    //存储路径
    @Value("${images.absolutePaths}")
    private String absolutePaths;


    @PostMapping("upload")
    @ResponseBody
    public ResultBean fileUpload(@RequestParam("file")MultipartFile file){

        // 获取文件名后缀名
        String suffix = file.getOriginalFilename();
        String prefix = suffix.substring(suffix.lastIndexOf("."));
        String Filename = DateUtils.getDateStr8()+"_"+UUID.randomUUID().toString().replaceAll("-","");
        if (!file.isEmpty()) {//文件不为空
            try {
                //上传路径
                StringBuilder filePath = new StringBuilder(absolutePaths).append(Filename).append(prefix);
                File saveDir = new File(String.valueOf(filePath));
                if (!saveDir.getParentFile().exists()){
                    saveDir.getParentFile().mkdirs();
                }
                // 转存文件
                file.transferTo(saveDir);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("imagePath",String.valueOf(filePath));
                jsonObject.put("uri",uri);
                jsonObject.put("url",new StringBuilder(relativePath).append(Filename).append(prefix));
                return new ResultBean("200",jsonObject.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResultBean("404","网络延迟，请重新提交");

    }

    /**
     * 优图解析，并上传到本地
     * @return
     */
    @RequestMapping("youTu")
    @ResponseBody
    public String youTu(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("********************获取图片内容********************");
        JSONObject object = new JSONObject();
        try {
            //优图工具
            YouTuUtils youto = new YouTuUtils();
            //文件base64字符串
            String base64String = request.getParameter("str");
            String str = base64String.substring(base64String.lastIndexOf(",")+1);
            //图片标识
            String flag = request.getParameter("flag");

            //图片上传，返回路径
            ResultBean<String[]> resultBean = youto.BASE64CodeToBeImage(str);
            String[] resultMsg = resultBean.getResultMsg();
            //解析图片，返回图片信息
            object = youto.youTu(str, flag);
            object.put("imagePath",resultMsg[0]);
            object.put("uri",uri);
            object.put("url",new StringBuilder(relativePath).append(resultMsg[1]));
        } catch (Exception e) {
            logger.error("解析图片出现问题" + e);
            object.put("result", "FAIL");
            object.put("message", e.getMessage());
        }
        return object.toJSONString();
    }


}
