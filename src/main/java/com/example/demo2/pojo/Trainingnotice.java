package com.example.demo2.pojo;


import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "trainingnotice")
public class Trainingnotice {


  @Id
  @Column(name = "trainingnotice_id")
  private int trainingnoticeId;
  @Column(name = "trainingnotice_create_time")
  private Date trainingnoticeCreateTime;
  @Column(name = "trainingnotice_title")
  private String trainingnoticeTitle;
  @Column(name = "trainingnotice_content")
  private String trainingnoticeContent;
  @Column(name = "trainingnotice_update_time")
  private Date trainingnoticeUpdateTime;
  @Column(name = "training_id")
  private int trainingId;


  public int getTrainingnoticeId() {
    return trainingnoticeId;
  }

  public void setTrainingnoticeId(int trainingnoticeId) {
    this.trainingnoticeId = trainingnoticeId;
  }

  public Date getTrainingnoticeCreateTime() {
    return trainingnoticeCreateTime;
  }

  public void setTrainingnoticeCreateTime(Date trainingnoticeCreateTime) {
    this.trainingnoticeCreateTime = trainingnoticeCreateTime;
  }

  public String getTrainingnoticeTitle() {
    return trainingnoticeTitle;
  }

  public void setTrainingnoticeTitle(String trainingnoticeTitle) {
    this.trainingnoticeTitle = trainingnoticeTitle;
  }

  public String getTrainingnoticeContent() {
    return trainingnoticeContent;
  }

  public void setTrainingnoticeContent(String trainingnoticeContent) {
    this.trainingnoticeContent = trainingnoticeContent;
  }

  public Date getTrainingnoticeUpdateTime() {
    return trainingnoticeUpdateTime;
  }

  public void setTrainingnoticeUpdateTime(Date trainingnoticeUpdateTime) {
    this.trainingnoticeUpdateTime = trainingnoticeUpdateTime;
  }

  public int getTrainingId() {
    return trainingId;
  }

  public void setTrainingId(int trainingId) {
    this.trainingId = trainingId;
  }
}
