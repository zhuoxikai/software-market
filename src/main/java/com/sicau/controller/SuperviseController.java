package com.sicau.controller;

import com.sicau.constants.CommonConstants;
import com.sicau.entity.dto.Achievements;
import com.sicau.entity.dto.Delay;
import com.sicau.entity.dto.RunAndAchievements;
import com.sicau.entity.pojo.vo.ResultVO;
import com.sicau.service.AchievementsService;
import com.sicau.service.DelayService;
import com.sicau.service.SuperviseService;
import com.sicau.util.IDUtil;
import com.sicau.util.VeDate;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @program: software-market
 * @description: 监督和评价系统
 * @author: Lee
 * @create: 2019-02-07 21:40
 **/
@RestController
@Slf4j
@RequestMapping(CommonConstants.NONPUBLIC_PREFIX+"/supervise")
public class SuperviseController {
    @Autowired
    SuperviseService superviseService;
    @Autowired
    private AchievementsService achievementsService;
    @Autowired
    private DelayService delayService;
    /**
     * @param runID:进行的项目ID
     * @param score：分数
     * @return com.sicau.entity.pojo.vo.ResultVO
     * @author Lee
     * @description 给项目打分
     * @date 2019/2/7
     */
    @RequestMapping(value ="/scoring",method = RequestMethod.POST)
    public ResultVO scoringProject(@RequestParam("runId") String runID, @RequestParam("score") String score){
        return superviseService.scoring(runID,score);
    }
    /**
     * @param delayId:延迟的项目ID
     * @return com.sicau.entity.pojo.vo.ResultVO
     * @author Lee
     * @description:获取项目的延迟审核状态
     * @date 2019/2/7
     */
    @RequestMapping(value ="/delayState",method = RequestMethod.GET)
    public ResultVO getDealyState(@RequestParam("delayId") String delayId){
        System.out.println(superviseService.getDelayState(delayId));
        return  superviseService.getDelayState(delayId);
    }

    /**
     * Description:查看审核状态
     * @author tzw
     * CreateTime 1:38 2019/2/15
     **/
   @RequestMapping("/state")
    public ResultVO getUndertakeState(@RequestParam("undertakeId") String undertakeId)
   {
       return superviseService.getUndertakeState(undertakeId);
   }

    /**
     *
     * @param runId 正在运行的项目id
     * @param timeNode 时间节点
     * @param achievementType 成果类型
     * @param achievement 成果文件
     * @return
     * @author hyc
     */
    @RequestMapping("/achievements")
    private ResultVO commitAchievement(@RequestParam("runId") String runId,
                                       @RequestParam("timeNode") String timeNode,
                                       @RequestParam("achievementType") String achievementType,
                                       @RequestParam("achievement") MultipartFile achievement)
    {
        return achievementsService.commit(runId, timeNode, achievement);

    }

    /**
     * 延期申请
     * @param runId
     * @param delayTime
     * @return
     */
    @RequestMapping(value = "/delay",method = RequestMethod.POST)
    public ResultVO addDelay(@RequestParam("runId") String runId,
                             @RequestParam("delayTime") String delayTime)
    {
        try{
            Delay delay = new Delay();
            delay.setState("1");
            delay.setDelayId(IDUtil.getUUID());
            delay.setDelayTime(delayTime);
            return delayService.addDelay(delay,runId);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("插入数据库发生错误");
            return null;
        }
    }

    /**
     * 生成运行时项目并创建时刻表
     * @param timeNode
     * @return
     */
    @RequestMapping(value = "/timetable",method = RequestMethod.POST)
    public ResultVO makeRunProject(@RequestParam(required = true)String timeNode){
//        Gson gson = new Gson();
//        List<TimeNodePO> timeNodePOList = new ArrayList<>();
//        try{
//            timeNodePOList =gson.fromJson(timeNode,
//                    new TypeToken<List<TimeNodePO>>(){
//                    }.getType());
//        }catch (Exception e){
//            log.error("【json转换】错误，string={}",timeNode);
//            return new ResultVO(-1,"【json转换】错误");
//        }
        if(timeNode==null){
            return new ResultVO(10,"资源不存在");
        }
        String result = new String();
        try {
            JSONArray jsonArray = JSONArray.fromObject(timeNode);
            for (int i=0;i<jsonArray.size();i++){
                JSONObject jsonObject= (JSONObject)jsonArray.get(i);
                //将对应的字段整合成一个字符串
                result = new StringBuilder().append(result).append(jsonObject.getString("number"))
                        .append(" ").append(jsonObject.getString("time")).append(" ")
                        .append(jsonObject.getString("things"))
                        .append(";").toString();
            }
        } catch (Exception e){
//            log.error("【json转换】错误，string={}",timeNode);
            return new ResultVO(-1,"【json转换】错误");
        }
        return superviseService.createTimeTable(result);
    }

    /**
     * 项目进展查看
     * @param runId
     * @return
     */
    @RequestMapping(value = "/progress",method = RequestMethod.GET)
    public ResultVO progress(@RequestParam(required = true)String runId){
        return superviseService.getProgress(runId);
    }

    /**
     * 超时判断
     * @param runId
     * @return
     */
    @RequestMapping(value = "/timeoutJudgement",method = RequestMethod.POST)
    public ResultVO timeoutJudgement(@RequestParam(required = true)String runId){
        return superviseService.timeoutJudgement(runId);
    }
    /**
     * 超时判断
     * @param runId
     * @return
     */
    @RequestMapping(value = "/changeProcess",method = RequestMethod.POST)
    public ResultVO changeProcess(@RequestParam(required = true)String runId){
        return superviseService.changeProcess(runId);
    }

    /**
     * Description:根据消息id获取消息
     * @author tzw
     * CreateTime 12:31 2019/2/26
     **/
    @RequestMapping("/getMessageByMessageId")
    public ResultVO getMessageByMessageId(@RequestParam("messageId") String messageId)
    {
        System.out.println("====================================="+messageId);
        return superviseService.getMessageByMessageId(messageId);
    }

    /**
     * Description:根据接收方获取信息
     * @author tzw
     * CreateTime 18:45 2019/2/26
     **/
    @RequestMapping("/getMessageByUserGet")
    public ResultVO getMessageByUserGet(@RequestParam("userGet") String userGet)
    {
        return superviseService.getMessageByUserGet(userGet);
    }

    /**
     * @Description:列入黑名单
     * @param teamId
     * @return com.sicau.entity.pojo.vo.ResultVO
     * @author cxh 测试成功
     * @date 2019/3/4
     **/
    @RequestMapping(value = "/addBlacklist",method = RequestMethod.POST)
    public ResultVO addBlackList(@RequestParam("teamId") String teamId){
        return superviseService.addBlackList(teamId);
    }

    /**
     * @Describe 发送信息
     * @author yj
     * @return ResultVO 以修改部分并测试成功
     */
    @RequestMapping("/sendMessage")
    public ResultVO sendMessage(@RequestParam("content") String content,
                                @RequestParam("messageType") String messageType,
                                @RequestParam("userGet") String userGet,
                                @RequestParam("userSend") String userSend,
                                @RequestParam("messageTopic") String messageTopic){
        return superviseService.sendMessage(content,messageType,userGet,userSend,messageTopic);
    }

    /**
     * Description:根据接收方获取信息
     * @author tzw
     * CreateTime 00:26 2019/3/6
     **/
    @RequestMapping("/blacklistJudgement")
    public ResultVO blacklistJudgement(@RequestParam("runId") String runId)
    {
        return superviseService.judge(runId);
    }


    /**
     * @param achievementFilename 项目成果文件名
     * @param runId               运行项目ID
     * @return ResultVO
     * @author Lee
     * @description 下载项目成果
     * @date 2019/3/6
     */
    @RequestMapping("/downloadAchievement")
    public ResultVO downloadAchievement(@RequestParam("achievementFilename") String achievementFilename, @RequestParam("runId") String runId, HttpServletResponse httpServletResponse) {
        return superviseService.downloadAchievement(achievementFilename, runId, httpServletResponse);
    }

    ;

    /**
     * @param projectFilename 项目计划文件名
     * @param projectId       项目ID
     * @return ResultVO
     * @author Lee
     * @description 下载项目计划
     * @date 2019/3/6
     */
    @RequestMapping("/downloadProjectDocument")
    public ResultVO downloadProjectDocument(@RequestParam("projectFilename") String projectFilename, @RequestParam("projectId") String projectId, HttpServletResponse httpServletResponse) {
        return superviseService.downloadProjectDocument(projectFilename, projectId, httpServletResponse);
    }

    /**
     * @param teamFilename 团队文件名
     * @param teamId       团队ID
     * @return ResultVO
     * @author Lee
     * @description 下载团队信息
     * @date 2019/3/6
     */
    @RequestMapping("/downloadTeamMsg")
    public ResultVO downloadTeamMsg(@RequestParam("teamFilename") String teamFilename, @RequestParam("teamId") String teamId, HttpServletResponse httpServletResponse) {
        return superviseService.downloadTeamMsg(teamFilename, teamId, httpServletResponse);
    }
    @RequestMapping("/allTimeNodes")
    public ResultVO allTimeNodes(String runId){
        return superviseService.allTimeNodes(runId);
    }
}
