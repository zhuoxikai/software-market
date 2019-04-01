package com.sicau.service.impl;


import com.google.gson.JsonObject;
import com.sicau.dao.PublishMapper;
import com.sicau.entity.dto.Project;
import com.sicau.entity.dto.TeamAndUser;
import com.sicau.entity.dto.User;
import com.sicau.entity.pojo.po.TeamPO;
import com.sicau.entity.pojo.vo.ResultVO;
import com.sicau.service.PublishService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @author ttxxi
 * write on 2019-1-31
 * 发布系统服务接口的实现类
 */
@Service
public class PublishServiceImpl implements PublishService {

    @Autowired
    PublishMapper publishMapper;

    @Override
    public ResultVO publishTeamBy(String condition ,String value){
        try {
            List<TeamPO> list = new ArrayList();
//            if (condition.equals("direction") || condition.equals("-1")){
//
//                System.out.println("进入了根据方向查团队"+value);
//                list = publishMapper.selectTeamByDirection(value);//先通过dir在team表中查询到所有团队，对应前端 /teambydirection  以及/team 中teamId=-1的情况
//            }else if (condition.equals("state")){
//                System.out.println("进入了根据状态查团队"+value);
//                list = publishMapper.selectTeamByState(value);//先通过state在team表中查询到所有团队， 对应前端 /teambystate
//            }else if(condition.equals("0")){
//                System.out.println("进入了查所有团队"+condition);
//                list = publishMapper.selectAllTeam();//先team表中查询到所有团队， 对应前端 /team 中teamId=0的情况
//            }else{
//                System.out.println("进入了根据id查团队"+condition);
//                list = publishMapper.selectTeamByTeamId(condition);//先team表中查询到所有团队， 对应前端 /team 中teamId!=0 & teamId!=-1的情况
//            }


            switch (condition){
                case "direction":
                    System.out.println("进入了根据方向查团队"+value);
                    String[] directionList = value.split(",");
//                    List list = new ArrayList();
                    for (String item: directionList) {
                        list.addAll(publishMapper.selectTeamByDirection(item));//先通过dir在team表中查询到所有团队  对应前端 /teambydirection
                    }

                    break;
                case "state":
                    System.out.println("进入了根据状态查团队"+value);
                    list = publishMapper.selectTeamByState(value);//先通过state在team表中查询到所有团队， 对应前端 /teambystate
                    break;
                case "-1":
                    System.out.println("进入了根据方向查团队"+value);
                    list = publishMapper.selectTeamByDirection(value);//先通过dir在team表中查询到所有团队，对应前端/team 中teamId=-1的情况
                    break;
                case "0":
                    System.out.println("进入了查所有团队"+condition);
                    list = publishMapper.selectAllTeam();//先team表中查询到所有团队， 对应前端 /team 中teamId=0的情况
                    break;
                default:
                    System.out.println("进入了根据id查团队"+condition);
                    list = publishMapper.selectTeamByTeamId(condition);//先team表中查询到所有团队， 对应前端 /team 中teamId!=0 & teamId!=-1的情况
                    break;
            }

            //判断是否查找到数据，如果未查找的直接返回
            if(list.isEmpty()){
                return new ResultVO(-1,"未查找到数据");
            }
            JSONArray jsonArray = JSONArray.fromObject(list);//将查询到的结果转化为JSONArray
            System.out.println(jsonArray.toString());

            return new ResultVO(0,"成功",getUserToTeamList(jsonArray));
        } catch(IndexOutOfBoundsException e){
            e.printStackTrace();
            System.out.println("处理失败，返回失败结果");
            return new ResultVO(-1,"查询团队和队员失败，请检查数据库前后是否一致...");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("处理失败，返回失败结果");
            return new ResultVO(-1,e.toString());
        }

    }

    @Override
    public ResultVO publishProject(String projectId){
        try {
            if(projectId.equals("0")) {
                List<Project> list = publishMapper.selectAllProject();
                if(list.isEmpty()){
                    return new ResultVO(0,"成功，未查找到数据");
                }
                return new ResultVO(0,"成功",list);
            }else {
                Project project = publishMapper.selectProjectById(projectId);
                if (project == null){
                    return new ResultVO(-1,"未查找到数据");
                }
                JSONObject jsonObject = JSONObject.fromObject(project);
                jsonObject.put("undertake", publishMapper.selectUndertakeByProjectId(projectId));//查询对应的正在申请的团队信息
                JSONArray jsonArray = new JSONArray();
                jsonArray.add(jsonObject);
                return new ResultVO(0,"成功",jsonArray);
            }


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("处理失败，返回失败结果");
            return new ResultVO(-1,e.toString());
        }
    }


    /**
     * 为teamList获取队长队员信息，返回的是获取完信息的teamList
     * @param jsonArray team的JsonArray
     * @return 获取完信息的teamList
     */
    public JSONArray getUserToTeamList(JSONArray jsonArray){
        for(int i = 0 ; i<jsonArray.size();i++){//依次取出每一个团队id，再去查抄团队对应的队长、队员

            String teamId = jsonArray.getJSONObject(i).getString("teamId");

            System.out.println("第"+i+"队teamID:"+teamId);
            List<TeamAndUser> teamAndUserList = publishMapper.selectTeamAndUserById(teamId);//查询团队id对应的队长队员

            JSONArray jsonArray1 = JSONArray.fromObject(teamAndUserList);
            User captain = publishMapper.selectUserById(jsonArray1.getJSONObject(0).getString("captain"));//查询队长的信息
            System.out.println("当前第"+i+"队队长id为："+captain.getUserId());
            jsonArray.getJSONObject(i).element("captainInformation", new JSONArray().fromObject(captain));//设置captainInformation

            JSONArray memberArray = new JSONArray();//用来储存队员信息的数组
            for(int j = 0 ; j<jsonArray1.size() ; j++){
                User member = publishMapper.selectUserById(jsonArray1.getJSONObject(j).getString("member"));//查询队员的信息
                memberArray.add(member);
            }
            jsonArray.getJSONObject(i).element("memberInformation", memberArray);

            //再去查询对应团队已经完成的作品
            List<String> projectIdList = publishMapper.selectWorksByTeamId(teamId);
            System.out.println("对应团队id"+teamId+"查询到的works集合有:"+projectIdList.toString());

            JSONArray projectArray = new JSONArray();
            for (int t = 0; t<projectIdList.size() ; t++){
                projectArray.add(publishMapper.selectProjectById(projectIdList.get(t)));
            }
            jsonArray.getJSONObject(i).element("works", projectArray);

        }

        return jsonArray;
    }




}
