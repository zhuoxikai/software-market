package com.sicau.service.impl;

import com.aliyun.oss.OSSClient;
import com.sicau.dao.AchievementsAndTypeMapper;
import com.sicau.dao.AchievementsMapper;
import com.sicau.entity.dto.Achievements;
import com.sicau.entity.dto.AchievementsAndType;
import com.sicau.entity.dto.AchievementsType;
import com.sicau.entity.pojo.vo.ResultVO;
import com.sicau.service.AchievementsService;
import com.sicau.util.IDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
@Service
public class AchievementsServiceImpl implements AchievementsService {

    @Autowired
    private AchievementsMapper achievementsDao;

    @Autowired
    private AchievementsAndTypeMapper achievementsAndTypeDao;

    /**
     * 成果提交
     * @param runId
     * @param timeNode
     * @param achievement
     * @return
     * @author hyc
     */
    @Override
    public ResultVO commit(String runId, String timeNode, MultipartFile achievement) {
        ResultVO result = new ResultVO();
        if(achievement.getSize() > 1024*1024*100){
            result.setStatus(1);
            result.setMsg("文件过大,上传失败");
            return result;
        }
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        String accessKeyId = "LTAIbfETF6Wukn9l";
        String accessKeySecret = "mbu2xEul5IS284yJMOmdZRkvlA2jwC";
        String bucketName = "lucism";
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        String directoryName = runId;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        String time = sdf.format(date);
        String fileName = timeNode + "_" + time + "_" + achievement.getOriginalFilename().split("\\.")[1];
        String objectName = "achievement/" + runId + "/" + fileName;
        try {
            ossClient.deleteObject(bucketName, objectName);
            ossClient.putObject(bucketName, objectName, (ByteArrayInputStream)achievement.getInputStream());
            result.setStatus(0);
            result.setMsg("成功");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            ossClient.shutdown();
        }
        return result;
    }
}
