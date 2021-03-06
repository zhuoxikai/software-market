package com.sicau.entity.dto;

public class Project {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.project_id
     *
     * @mbg.generated Fri Feb 01 16:05:07 CST 2019
     */
    private String projectId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.project_time
     *
     * @mbg.generated Fri Feb 01 16:05:07 CST 2019
     */
    private String projectTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.project_name
     *
     * @mbg.generated Fri Feb 01 16:05:07 CST 2019
     */
    private String projectName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.project_requirement
     *
     * @mbg.generated Fri Feb 01 16:05:07 CST 2019
     */
    private String projectRequirement;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.project_describe
     *
     * @mbg.generated Fri Feb 01 16:05:07 CST 2019
     */
    private String projectDescribe;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column project.project_price
     *
     * @mbg.generated Fri Feb 01 16:05:07 CST 2019
     */
    private String projectPrice;

    private String releaseTime;

    private String userId;

    private String state;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.project_id
     *
     * @return the value of project.project_id
     *
     * @mbg.generated Fri Feb 01 16:05:07 CST 2019
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.project_id
     *
     * @param projectId the value for project.project_id
     *
     * @mbg.generated Fri Feb 01 16:05:07 CST 2019
     */
    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.project_time
     *
     * @return the value of project.project_time
     *
     * @mbg.generated Fri Feb 01 16:05:07 CST 2019
     */
    public String getProjectTime() {
        return projectTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.project_time
     *
     * @param projectTime the value for project.project_time
     *
     * @mbg.generated Fri Feb 01 16:05:07 CST 2019
     */
    public void setProjectTime(String projectTime) {
        this.projectTime = projectTime == null ? null : projectTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.project_name
     *
     * @return the value of project.project_name
     *
     * @mbg.generated Fri Feb 01 16:05:07 CST 2019
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.project_name
     *
     * @param projectName the value for project.project_name
     *
     * @mbg.generated Fri Feb 01 16:05:07 CST 2019
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.project_requirement
     *
     * @return the value of project.project_requirement
     *
     * @mbg.generated Fri Feb 01 16:05:07 CST 2019
     */
    public String getProjectRequirement() {
        return projectRequirement;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.project_requirement
     *
     * @param projectRequirement the value for project.project_requirement
     *
     * @mbg.generated Fri Feb 01 16:05:07 CST 2019
     */
    public void setProjectRequirement(String projectRequirement) {
        this.projectRequirement = projectRequirement == null ? null : projectRequirement.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.project_describe
     *
     * @return the value of project.project_describe
     *
     * @mbg.generated Fri Feb 01 16:05:07 CST 2019
     */
    public String getProjectDescribe() {
        return projectDescribe;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.project_describe
     *
     * @param projectDescribe the value for project.project_describe
     *
     * @mbg.generated Fri Feb 01 16:05:07 CST 2019
     */
    public void setProjectDescribe(String projectDescribe) {
        this.projectDescribe = projectDescribe == null ? null : projectDescribe.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column project.project_price
     *
     * @return the value of project.project_price
     *
     * @mbg.generated Fri Feb 01 16:05:07 CST 2019
     */
    public String getProjectPrice() {
        return projectPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column project.project_price
     *
     * @param projectPrice the value for project.project_price
     *
     * @mbg.generated Fri Feb 01 16:05:07 CST 2019
     */
    public void setProjectPrice(String projectPrice) {
        this.projectPrice = projectPrice == null ? null : projectPrice.trim();
    }

    public Project(String projectId, String projectTime, String projectName, String projectRequirement, String projectDescribe, String projectPrice, String releaseTime, String userId, String state) {
        this.projectId = projectId;
        this.projectTime = projectTime;
        this.projectName = projectName;
        this.projectRequirement = projectRequirement;
        this.projectDescribe = projectDescribe;
        this.projectPrice = projectPrice;
        this.releaseTime = releaseTime;
        this.userId = userId;
        this.state = state;
    }

    public Project() {
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}