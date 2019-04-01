package com.sicau.controller;


import java.util.Map;

import com.sicau.entity.dto.*;
import com.sicau.entity.pojo.vo.ResultVO;
import com.sicau.enums.ExamineTypeEnum;
import com.sicau.service.DelayService;
import com.sicau.util.IDUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sicau.constants.CommonConstants;
import com.sicau.service.ExamineService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Description:審核controller
 * @author yj
 * CreateTime 22:01 2019/2/13
 **/
@RestController
@RequestMapping(CommonConstants.NONPUBLIC_PREFIX+"/examine")
public class ExamineController {
	
	@Autowired
	private ExamineService examineService;
	@Autowired
	private DelayService delayService;

	@ResponseBody
	@RequestMapping(value ="/delay", method = {RequestMethod.POST})
	public Map<String, Object> delay(@RequestParam(value = "delayId", defaultValue ="-1")String delayId, @RequestParam(value = "state", defaultValue = "-1")String state) {
		Map<String, Object>map = examineService.delayByDelayId(delayId, state);
		return map;
	}

	@ResponseBody
	@RequestMapping(value = "/modificationProjectX", method = {RequestMethod.POST})
	public Map<String, Object> modificationProjectX(@RequestParam(required = true)String projectId, @RequestParam(required = true)String projectName, @RequestParam(required = true)String projectRequirement, @RequestParam(required = true)String projectTime, @RequestParam(required = true)String projectDescribe, @RequestParam(required = true)String projectPrice) {
		Map<String, Object> map = examineService.modificateProjectByX(projectId, projectName, projectRequirement, projectTime, projectDescribe, projectPrice);
		return map;
	}

	@ResponseBody
	@RequestMapping(value = "/modificationProjectJudge", method = RequestMethod.POST)
	public Map<String, Object> modificationProjectJudge(@RequestParam(required = true)String modificationId, @RequestParam(required = true)String state) {
		Map<String, Object> map = examineService.updateModificationProjectState(modificationId, state);
		return map;
	}

	@ResponseBody
	@RequestMapping(value = "/get/team", method = RequestMethod.GET)
	public Map<String, Object> getTeams(){
		Map<String, Object>map = examineService.getUnauditedTeams();
		System.out.println("========================map:" + map);
		return map;
	}
	/**
	 * @return com.sicau.entity.pojo.vo.ResultVO
	 * @author Lee
	 * @description :获取全部的项目修改请求以及内容
	 * @date 2019/2/7
	 */
	@ResponseBody
	@RequestMapping(value="/get/modificationProject",method = RequestMethod.GET)
	public ResultVO getModificationProject(){
		return examineService.getModificationProject();
	};

    /**
     * @Describe 审核项目
	 * @author yj
     * @return ResultVO 测试成功
     */
    @RequestMapping(value = "/project",method = {RequestMethod.POST})
    public ResultVO projectAudit(@RequestParam("projectId") String projectId,
                                 @RequestParam("state") String state){
        return examineService.projectAudit(projectId,state);
    }

    /**
     * @Describe 审核团队
	 * @author yj
     * @return ResultVO  测试成功
     */
    @RequestMapping(value = "/team",method = {RequestMethod.POST})
    public ResultVO teamAudit(@RequestParam("teamId") String teamId,
                              @RequestParam("state") String state){
        return examineService.teamAudit(teamId,state);
    }
    /**
     * @Describe 审核项目分配
	 * @author yj
     * @return ResultVO  测试成功
     */
    @RequestMapping(value = "/allocation",method = {RequestMethod.POST})
    public ResultVO projectAllocation(@RequestParam("undertakeId") String undertakeId,
                                      @RequestParam("state") String state){
        return examineService.projectAllocation(undertakeId,state);
    }

    /**
     * @Describe 获取待审核项目分配（哪些团队接哪些项目）
	 * @author yj
     * @return ResultVO 已修改并测试成功
     */
    @RequestMapping(value = "/get/allocation",method = {RequestMethod.GET})
    public ResultVO projectGetAllocation(){
        return examineService.projectGetAllocation();
    }


	/**
	 * Description:获取待审核项目
	 * @author tzw
	 * CreateTime 21:11 2019/2/7
	 **/
    @RequestMapping("/get/project")
	public ResultVO getProject()
	{
          return examineService.getAllProject();
	}

	/**
	 * 获取延期申请
	 * @return
	 */
	@RequestMapping(value = "/get/delay",method = RequestMethod.GET)
	public ResultVO getDelay() {
		return delayService.getAllDelay();
	}

	/**
	 * 团队修改审核
	 * @param modificationId
	 * @param state
	 * @return
	 */
	@RequestMapping(value = "/modificationTeamJudge", method = RequestMethod.POST)
	public ResultVO modificationTeamJudge(@RequestParam(required = true)String modificationId, @RequestParam(required = true)String state) {
		ResultVO resultVO = examineService.updateModificationTeamState(modificationId, state);
		return resultVO;
	}

	/**
	 *发起团队修改
	 * @param teamId
	 * @param modificationType
	 * @param modificationContent
	 * @param originalData
	 * @param modificationPlace
	 * @return
	 */
	@RequestMapping(value = "/modificationTeam", method = RequestMethod.POST)
	public ResultVO modificationTeam(@RequestParam(required = true)String teamId, @RequestParam(required = true)String modificationType,
									 @RequestParam(required = true)String modificationContent,@RequestParam(required = true)String originalData,
									 @RequestParam(required = true)String modificationPlace) {
		ModificationTeam modificationTeam = new ModificationTeam();
		modificationTeam.setTeamId(teamId);
		modificationTeam.setState(ExamineTypeEnum.WAIT.getMsg());
		modificationTeam.setModificationContent(modificationContent);
		modificationTeam.setModificationType(modificationType);
		modificationTeam.setOriginalData(originalData);
		modificationTeam.setModificationPlace(modificationPlace);
		modificationTeam.setModificationId(IDUtil.getUUID());
		ResultVO resultVO = examineService.createTeamApplication(modificationTeam);
		return resultVO;
	}

	/**
	 * 发起项目修改
	 * @param projectId
	 * @param modificationType
	 * @param modificationContent
	 * @param originalData
	 * @param modificationPlace
	 * @return
	 */
	@RequestMapping(value = "/modificationProject", method = RequestMethod.POST)
	public ResultVO modificationProject(@RequestParam(required = true)String projectId, @RequestParam(required = true)String modificationType,
									 @RequestParam(required = true)String modificationContent,@RequestParam(required = true)String originalData,
									 @RequestParam(required = true)String modificationPlace) {
		ModificationProject modificationProject = new ModificationProject();
		modificationProject.setProjectId(projectId);
		modificationProject.setState(ExamineTypeEnum.WAIT.getMsg());
		modificationProject.setModificationContent(modificationContent);
		modificationProject.setModificationType(modificationType);
		modificationProject.setOriginalData(originalData);
		modificationProject.setModificationPlace(modificationPlace);
		modificationProject.setModificationId(IDUtil.getUUID());
		ResultVO resultVO = examineService.createProjectApplication(modificationProject);
		return resultVO;
	}

	/**
	 * 团队修改（信推办）
	 * @param teamId
	 * @param teamScore
	 * @param teamState
	 * @return
	 */
	@RequestMapping(value = "/modificationTeamX", method = RequestMethod.POST)
	public ResultVO modificationTeamX(@RequestParam(required = true)String teamId, @RequestParam(required = true)String teamScore,
										@RequestParam(required = true)String teamState) {
		Team team = new Team();
		team.setTeamId(teamId);
		team.setTeamState(teamState);
		team.setTeamScore(teamScore);
		ResultVO resultVO = examineService.updateTeamMessage(team);
		return resultVO;
	}

	/**
	 * 获取团队信息修改请求
	 * @return
	 */
	@RequestMapping(value = "/get/modificationTeam", method = RequestMethod.GET)
	public ResultVO getModificationTeam() {
		ResultVO resultVO = examineService.getTeamModificationMessage();
		return resultVO;
	}

	/**
	 * @Description:项目根据状态分类
	 * @param state 支持可变长参数state查询
	 * @return com.sicau.entity.pojo.vo.ResultVO 列表
	 * @author cxh
	 * @date 2019/3/9
	 **/
	@RequestMapping(value = "/projectsClassifyByState",method = RequestMethod.POST)
	public ResultVO projectsClassifyByState(@RequestParam("state") String... state){
		return examineService.projectsClassifyByState(state);
	}
	/**
	 * @return com.sicau.entity.pojo.vo.ResultVO
	 * @author Lee
	 * @description 添加成员
	 * @date 2019/3/12
	 */
	@RequestMapping(value =" /addTeammate",method = RequestMethod.POST)
	public ResultVO addTeammate(@RequestParam("teamId") String teamId, @RequestParam("captainId")String captainId,
								@RequestParam("name")String name, @RequestParam("sex")String sex,
								@RequestParam("department")String department, @RequestParam("grade")String grade,
								@RequestParam("major")String major, @RequestParam("direction")String direction,
								@RequestParam("studentId")String studentId, @RequestParam("description")String description,
                                @RequestParam("telNumber")String telNumber){
		return examineService.addTeammate(teamId,captainId,name,sex,department,grade,major,direction,studentId,description,telNumber);
	}
	/**
	 * @return com.sicau.entity.pojo.vo.ResultVO
	 * @author Lee
	 * @description 删除成员
	 * @date 2019/3/12m
	 */
	@RequestMapping(value = "/deleteTeammate",method = RequestMethod.POST)
	public ResultVO deleteTeammate(@RequestParam("userId") String userId,@RequestParam("teamId") String teamId){
		return examineService.deleteTeammate(userId,teamId);
	}
}
