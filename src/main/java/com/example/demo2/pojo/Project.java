package com.example.demo2.pojo;


import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "project")
public class Project {

  @Id
  @Column(name = "project_id")
  private int projectId;
  @Column(name = "project_name")
  private String projectName;
  @Column(name = "project_group_num")
  private int projectGroupNum;
  @Column(name = "project_status")
  private String projectStatus;
  @Column(name = "project_max_num")
  private int projectMaxNum;
  @Column(name = "project_introduction")
  private String projectIntroduction;
  @Column(name = "project_helper")
  private String projectHelper;
  @Column(name = "project_helper_tel")
  private String projectHelperTel;
  @Column(name = "project_interval_day")
  private int projectIntervalDay;
  @Column(name = "project_applicant_type")
  private String projectApplicantType;
  @Column(name = "training_id")
  private int trainingId;
  @Column(name = "teacher_id")
  private String teacherId;

  public Project (int projectId,
                  int projectMaxNum){
      this.projectId = projectId;
      this.projectMaxNum = projectMaxNum;
  }


  public Project() {
  }

  public Project(String projectName, int projectGroupNum, String projectStatus, String projectIntroduction, String projectHelper, String projectHelperTel, int projectIntervalDay, String projectApplicantType, int trainingId, String teacherId) {
    this.projectName = projectName;
    this.projectGroupNum = projectGroupNum;
    this.projectStatus = projectStatus;
    this.projectIntroduction = projectIntroduction;
    this.projectHelper = projectHelper;
    this.projectHelperTel = projectHelperTel;
    this.projectIntervalDay = projectIntervalDay;
    this.projectApplicantType = projectApplicantType;
    this.trainingId = trainingId;
    this.teacherId = teacherId;
  }

  public int getProjectId() {
    return projectId;
  }

  public void setProjectId(int projectId) {
    this.projectId = projectId;
  }

  public int getTrainingId() {
    return trainingId;
  }

  public void setTrainingId(int trainingId) {
    this.trainingId = trainingId;
  }

  public String getTeacherId() {
    return teacherId;
  }

  public void setTeacherId(String teacherId) {
    this.teacherId = teacherId;
  }

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }



  public String getProjectStatus() {
    return projectStatus;
  }

  public void setProjectStatus(String projectStatus) {
    this.projectStatus = projectStatus;
  }




  public String getProjectIntroduction() {
    return projectIntroduction;
  }

  public void setProjectIntroduction(String projectIntroduction) {
    this.projectIntroduction = projectIntroduction;
  }


  public String getProjectHelper() {
    return projectHelper;
  }

  public void setProjectHelper(String projectHelper) {
    this.projectHelper = projectHelper;
  }


  public String getProjectHelperTel() {
    return projectHelperTel;
  }

  public void setProjectHelperTel(String projectHelperTel) {
    this.projectHelperTel = projectHelperTel;
  }


  public int getProjectGroupNum() {
    return projectGroupNum;
  }

  public void setProjectGroupNum(int projectGroupNum) {
    this.projectGroupNum = projectGroupNum;
  }

  public int getProjectMaxNum() {
    return projectMaxNum;
  }

  public void setProjectMaxNum(int projectMaxNum) {
    this.projectMaxNum = projectMaxNum;
  }

  public int getProjectIntervalDay() {
    return projectIntervalDay;
  }

  public void setProjectIntervalDay(int projectIntervalDay) {
    this.projectIntervalDay = projectIntervalDay;
  }

  public String getProjectApplicantType() {
    return projectApplicantType;
  }

  public void setProjectApplicantType(String projectApplicantType) {
    this.projectApplicantType = projectApplicantType;
  }

}
