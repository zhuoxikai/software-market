package com.sicau.service;

import com.sicau.entity.dto.Achievements;
import com.sicau.entity.pojo.vo.ResultVO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Description:
 *
 * @author tzw
 * CreateTime 23:09 2019/2/9
 **/

@Service
public interface AchievementsService {


    ResultVO commit(String runId, String timeNode, MultipartFile achievement);
}
