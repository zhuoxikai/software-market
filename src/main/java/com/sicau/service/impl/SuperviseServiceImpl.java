package com.sicau.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.sicau.dao.*;
import com.sicau.entity.dto.*;
import com.sicau.entity.pojo.po.MessagePo;
import com.sicau.entity.pojo.vo.ResultVO;
import com.sicau.enums.ResultEnum;
import com.sicau.service.SuperviseService;
import com.sicau.util.DownloadUtil;
import com.sicau.util.IDUtil;
import com.sicau.util.VeDate;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: software-market
 * @description:
 * @author: Lee
 * @create: 2019-02-07 21:42
 **/
@Service
public class SuperviseServiceImpl implements SuperviseService {
    @Autowired
    RunProjectMapper runProjectDao;
    @Autowired
    TeamMapper teamDao;
    @Autowired
    UndertakeMapper undertakeDao;
    @Autowired
    DelayMapper delayDao;
    @Autowired
    MessageMapper messageMapper;
    @Autowired
    MessageTypeMapper messageTypeMapper;
    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public ResultVO scoring(String runID, String score) {
        ResultVO resultVO = new ResultVO();
        int Rresult = runProjectDao.updateScore(runID, score);
        if (Rresult == 1) {
            String undertakeId=runProjectDao.selectUndertakeIdByRunId(runID);
            Undertake undertake = undertakeDao.getUndertake(undertakeId);
            Team team = teamDao.getTeam(undertake.getTeamId());
            //将Team中原来的分数和新的分数进行相加，再和团队项目数相除，获取新的Team分数
            Float finaScore = Float.parseFloat(team.getTeamScore()) + (Float.parseFloat(score) / Float.parseFloat(team.getNumber()));
            int result = teamDao.updateScore(undertake.getTeamId(), finaScore.toString());
            if (result == 1) {
                resultVO.setMsg(ResultEnum.SUCCESS.getMessage());
                resultVO.setStatus(ResultEnum.SUCCESS.getCode());
            } else {
                resultVO.setMsg(ResultEnum.PARAM_ERROR.getMessage());
                resultVO.setStatus(ResultEnum.PARAM_ERROR.getCode());
            }
        }else {
            resultVO.setMsg(ResultEnum.PARAM_ERROR.getMessage());
            resultVO.setStatus(ResultEnum.PARAM_ERROR.getCode());
        }
        return resultVO;
    }
    @Override
    public ResultVO getDelayState(String delayID) {
        ResultVO resultVO=new ResultVO();
        String state=delayDao.getState(delayID);
        if(state!=null){
            resultVO.setData(delayDao.getState(delayID));
            resultVO.setStatus(ResultEnum.SUCCESS.getCode());
            resultVO.setMsg(ResultEnum.SUCCESS.getMessage());
        }else {
            resultVO.setStatus(ResultEnum.PARAM_ERROR.getCode());
            resultVO.setMsg(ResultEnum.PARAM_ERROR.getMessage());
        }
        return resultVO;
    }

    @Override
    public ResultVO getUndertakeState(String undertake_id) {
        try{
            Undertake undertake=undertakeDao.selectUndertakeById(undertake_id);
            return new ResultVO(0,"查看审核状态成功",undertake.getState());
        }catch (Exception e)
        {
            e.printStackTrace();
            return new ResultVO(-1,"查看审核状态出错");
        }

    }

    @Override
    public ResultVO createTimeTable(String timeNode) {
        timeNode = timeNode.substring(0,timeNode.length()-1);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        RunProject runProject = new RunProject();
        runProject.setTimeNode(timeNode);
        runProject.setDelayTime("0");
        runProject.setStartTime(String.valueOf(df.format(new Date())));
        runProject.setOvertime("0");
        runProject.setScore("0");
        runProject.setRunId(IDUtil.getUUID());
        String[] progress = timeNode.split(";");
        runProject.setProgress(progress[0]);
        runProjectDao.addRunProject(runProject);
        return new ResultVO(0,"成功");
    }

    @Override
    public ResultVO getProgress(String runId) {
        String result = runProjectDao.selectProgress(runId);
        if(result==null){
            return new ResultVO(10,"资源不存在");
        }
        return new ResultVO(0,"成功",result);
    }

    @Override
    public ResultVO timeoutJudgement(String runId) {
        String result = runProjectDao.selectProgress(runId);
        String[] time = result.split(" ");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = new Date();
            Date dt2 = df.parse(time[1]);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return new ResultVO(0,"成功",1);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResultVO(-1,"异常",-1);
        }
        return new ResultVO(0,"成功",0);
    }

    @Override
    public ResultVO changeProcess(String runId) {
        RunProject runProject = runProjectDao.selectByRunId(runId);
        String[] progress = runProject.getTimeNode().split(";");
        String nowProgress = runProject.getProgress();
        String[] node = nowProgress.split(" ");
        try {
            runProject.setProgress(progress[Integer.parseInt(node[0])]);
        }catch (Exception exception){
            return new ResultVO(0,"已完成项目");
        }
        runProjectDao.updateProgress(runProject);
        return new ResultVO(0,"成功");
    }

    @Override
    public ResultVO getMessageByMessageId(String messageId) {
        try {
            System.out.println("------------------------------------"+messageId);
            if(messageId.equals("0")) {
                List<MessagePo> messages=messageMapper.selectMessagePO();
                return new ResultVO(0,"获取消息",messages);
            }else {
                MessagePo message=messageMapper.selectMessagePoById(messageId);
                List<MessagePo> messages=new ArrayList<>();
                messages.add(message);
                return new ResultVO(0,"获取消息",messages);
            }

        }catch (Exception e)
        {
            e.printStackTrace();
            return new ResultVO(-1,"获取消息失败");
        }
    }

    @Override
    public ResultVO getMessageByUserGet(String userGet) {
        try {
            List<MessagePo> messagePos=messageMapper.selectMessagePOByUserGet(userGet);
            return new ResultVO(0,"获取消息成功",messagePos);

        }catch (Exception e)
        {
            e.printStackTrace();
            return new ResultVO(-1,"获取消息失败");
        }
    }

    @Override
    public ResultVO addBlackList(String teamId){
        try{
            String state="4";
            teamDao.updateStateTeam(teamId,state);
            return new ResultVO(0,"列入黑名单成功！");
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVO(1,"参数不正确");
        }
    }

    @Override
    public ResultVO judge(String runId) {
        try {
            String overTime=runProjectDao.selectOverTime(runId);
            if(overTime.equals("3")) {
                return new ResultVO(0,"要加入黑名单",1);
            }else
                return new ResultVO(0,"未超过限制超时次数，不加入黑名单",0);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVO(-1,"未知错误");
        }




    }

    @Override
    public ResultVO sendMessage(String content, String messageTypeId, String userGet, String userSend, String messageTopic) {
        //消息初始查看状态为未查看
        String messageState = "未查看";
        //记录message的id
        String messageId = IDUtil.getUUID();
        //关联内容
        //ArrayList<String> relation = new ArrayList<>();
        String relation = null;
        try {
//            if(Integer.parseInt(messageTypeId)==1){
//
//            }else if (Integer.parseInt(messageTypeId)==2){
//
//            }else if (Integer.parseInt(messageTypeId)==3){
//
//            }else if (Integer.parseInt(messageTypeId)==4){
//                //4->审核团队,关联团队id
//                relation = userGet;
//            }else if (Integer.parseInt(messageTypeId)==5){
//                //5->审核项目,关联项目id
//                relation = projectMapper.selectProjectByUserId(userGet);
//            }else if (Integer.parseInt(messageTypeId)==6){
//                //6->审核团队项目申请,关联undertakeId
//                int temp = undertakeDao.selectUndertakeByTeamId(userGet);
//                relation = String.valueOf(temp);
//            }
            //消息创建时间
            String createTime = VeDate.getStringDate();
            //对message进行封装
            Message message = new Message(messageId,messageState,createTime,content,messageTopic,relation);
            //判断messageType消息类型的id是否存在数据库中
            System.out.printf("============="+messageTypeId+"-------------------");
            MessageType messageType = messageTypeMapper.selectMessageTypeById(messageTypeId);
            //若messageType不为空，则存在表中，对messageAndType和MessageAndType表、message表添加对应信息
            if (messageType!=null)
            {
                //对message进行插入
                boolean hasInsert = messageTypeMapper.insertMessage(message);
                if (hasInsert){
                    //对messageAndType表添加对应信息
                    boolean hasMessageAndType = messageTypeMapper.insertMessageAndType(messageId,messageTypeId);
                    //对MessageAndUser表添加对应信息
                    boolean hasMessageUser = messageTypeMapper.insertMessageAndUser(messageId,userSend,userGet);
                    //判断是否插入成功
                    if (hasMessageAndType&&hasMessageUser)
                    {
                        return new ResultVO(0,"成功");
                    }else {
                        return new ResultVO(-1,"未知错误");
                    }
                }else {
                    return new ResultVO(1,"消息类型参数错误");
                }
            }else
            {
                return new ResultVO(-1,"未知错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVO(-1,"异常");
        }
    }

    @Override
    public ResultVO downloadAchievement(String achievementFilename, String runId, HttpServletResponse httpServletResponse) {
        DownloadUtil downloadUtil = new DownloadUtil();
        String url = "achievement/" + runId + "/" + achievementFilename;
        return downloadUtil.downLoad(url,achievementFilename,httpServletResponse);
    }

    @Override
    public ResultVO downloadProjectDocument(String projectFilename, String projectId, HttpServletResponse httpServletResponse) {
        DownloadUtil downloadUtil = new DownloadUtil();
        String url = "projectApplicationDocument/" + projectId + "/" + projectFilename;
        return downloadUtil.downLoad(url,projectId,httpServletResponse);
    }

    @Override
    public ResultVO downloadTeamMsg(String teamFilename, String teamId, HttpServletResponse httpServletResponse) {
        DownloadUtil downloadUtil = new DownloadUtil();
        String url = "teamDocument/" + teamId + "/" + teamFilename;
        return downloadUtil.downLoad(url,teamFilename,httpServletResponse);
}

    @Override
    public ResultVO allTimeNodes(String runId) {
        return null;
    }
}
