package com.example.demo2.pojo;


import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "projectnotice")
public class Projectnotice {

  @Id
  @Column(name = "projectnotice_id")
  private int projectnoticeId;
  @Column(name = "projectnotice_title")
  private String projectnoticeTitle;
  @Column(name = "projectnotice_create_time")
  private Date projectnoticeCreateTime;
  @Column(name = "projectnotice_content")
  private String projectnoticeContent;
  @Column(name = "projectnotice_update_time")
  private Date projectnoticeUpdateTime;
  @Column(name = "project_id")
  private int projectId;

  public int getProjectnoticeId() {
    return projectnoticeId;
  }

  public void setProjectnoticeId(int projectnoticeId) {
    this.projectnoticeId = projectnoticeId;
  }

  public String getProjectnoticeTitle() {
    return projectnoticeTitle;
  }

  public void setProjectnoticeTitle(String projectnoticeTitle) {
    this.projectnoticeTitle = projectnoticeTitle;
  }

  public Date getProjectnoticeCreateTime() {
    return projectnoticeCreateTime;
  }

  public void setProjectnoticeCreateTime(Date projectnoticeCreateTime) {
    this.projectnoticeCreateTime = projectnoticeCreateTime;
  }

  public String getProjectnoticeContent() {
    return projectnoticeContent;
  }

  public void setProjectnoticeContent(String projectnoticeContent) {
    this.projectnoticeContent = projectnoticeContent;
  }

  public Date getProjectnoticeUpdateTime() {
    return projectnoticeUpdateTime;
  }

  public void setProjectnoticeUpdateTime(Date projectnoticeUpdateTime) {
    this.projectnoticeUpdateTime = projectnoticeUpdateTime;
  }

  public int getProjectId() {
    return projectId;
  }

  public void setProjectId(int projectId) {
    this.projectId = projectId;
  }
}
