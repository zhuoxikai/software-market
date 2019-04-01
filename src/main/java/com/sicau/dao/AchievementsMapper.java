package com.sicau.dao;

import com.sicau.entity.dto.Achievements;
import com.sicau.entity.dto.RunAndAchievements;
import org.apache.ibatis.annotations.Param;

/**
 * Description:
 *
 * @author tzw
 * CreateTime 23:28 2019/2/9
 **/

public interface AchievementsMapper {

    String selectRunAndAchieveByRunId(@Param("runId") String runId);

    int insert(Achievements achievements);
}
