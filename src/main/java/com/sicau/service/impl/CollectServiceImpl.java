package com.sicau.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sicau.dao.CollectMapper;
import com.sicau.dao.UndertakeMapper;
import com.sicau.dao.UserMapper;
import com.sicau.entity.dto.*;
import com.sicau.entity.pojo.po.TeamPO;
import com.sicau.entity.pojo.vo.ResultVO;
import com.sicau.service.CollectService;
import com.sicau.util.VeDate;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollectServiceImpl implements CollectService {

    @Autowired
    private CollectMapper collectMapper;

    @Autowired
    private UndertakeMapper undertakeMapper;

    @Autowired
    private UserMapper userMapper;

   @Override
    public ResultVO collectUndertake(Undertake undertake){
        try {
            undertake.setState("未审核");
            collectMapper.updateUndertake(undertake);
            return new ResultVO(0,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("处理失败，返回失败结果");
            return new ResultVO(-1,"未知错误");
        }
    }

    @Override
    public ResultVO collectNeed(Project project){
        try {
            System.out.println("开始处理");
            project.setProjectId(UUID.randomUUID().toString().replaceAll("-",""));//设置id为uuid
            collectMapper.updateProject(project);
            System.out.println("处理完成");
            return new ResultVO(0,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("处理失败，返回失败结果");
            return new ResultVO(-1,"未知错误");
        }
    }

    @Override
    public ResultVO collectTeam(TeamPO teamPO){
        try {
            System.out.println("开始处理");

            teamPO.setTeamId(UUID.randomUUID().toString().replaceAll("-",""));//设置id为uuid

            //查询队长信息
            // 将字符串转换为json数组
            JSONObject captainObject = JSONObject.fromObject(teamPO.getCaptainInformation());
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            User captain = mapper.readValue(captainObject.toString(), User.class);//将json字符串转化为UserDTO对象
            String captainId = collectMapper.selectUserId(captain);//查询
            System.out.println("队长id为"+captainId);
            if (captainId==null || captainId==""){
                return new ResultVO(-1,"查无学号"+captain.getStudentId()+"的队长");
            }

            //查询队员信息
            JSONArray memberArray = JSONArray.fromObject(teamPO.getMemberInformation());
            for(int i = 0;i<memberArray.size();i++){//每一次循环都找到一个队员，并且将相关信息加入到teamAndUser表中
                User member = mapper.readValue(memberArray.getJSONObject(i).toString(), User.class);
                String memberId = collectMapper.selectUserId(member);//查询
                if (memberId==null || memberId==""){
                    return new ResultVO(-1,"查无学号"+member.getStudentId()+"的队员");
                }
                TeamAndUser teamAndUser = new TeamAndUser(teamPO.getTeamId(),captainId,memberId);
                collectMapper.updateTeamAndUser(teamAndUser);
            }

            //将团队信息加入team表中
            teamPO.setTeamState("1");
            teamPO.setCreateTime(new SimpleDateFormat("yyyy.MM.dd").format(new Date()));
//            teamPO.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            teamPO.setNumber(""+memberArray.size()+1);
            collectMapper.updateTeam(teamPO);
            System.out.println("团队信息添加成功");



            return new ResultVO(0,"成功");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("处理失败，返回失败结果");
            return new ResultVO(-1,"未知错误");
        }
    }

    /**
     * @Describe 开发团队参与项目征集
     * @author yj
     * @return 状态值
     */
    @Override
    public ResultVO projectTeamSolicitation(Undertake undertake) {
        try {
            //团队申请承接项目时间
            String date = VeDate.getStringDate();
            //存入对象
            undertake.setTime(date);
            boolean hasInsert = undertakeMapper.insertUndertake(undertake);
            if (hasInsert){
                return new ResultVO(0,"成功");
            }else {
                return new ResultVO(-1,"插入记录失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVO(-1,"未知错误");
        }
    }

    @Override
    public ResultVO selectAllTeamDirection() {
        try{
            List<DirectionName> directionNameList = collectMapper.selectAllTeamDirection();
            return new ResultVO(0,"返回团队方向成功",directionNameList);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVO(1,"未知参数错误");
            
        }
    }

    @Override
    public ResultVO selectAllUserDirection() {
        try {
            List<UserDirection> userDirectionList = userMapper.selectAllUsersDirection();
            if (userDirectionList!=null){
                return new ResultVO(0,"成功",userDirectionList);
            }else {
                return new ResultVO(-1,"失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVO(-1,"未知错误");
        }

    }
}
