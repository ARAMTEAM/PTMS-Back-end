package com.example.demo2.pojo;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "jiaowunotice")
public class Systemnotice {

  @Id
  @Column(name = "jiaowunotice_id")
  private long jiaowunoticeId;
  @Column(name = "jiaowunotice_create_time")
  private java.sql.Timestamp jiaowunoticeCreateTime;
  @Column(name = "jiaowunotice_title")
  private String jiaowunoticeTitle;
  @Column(name = "jiaowunotice_content")
  private String jiaowunoticeContent;
  @Column(name = "jiaowunotice_update_time")
  private java.sql.Timestamp jiaowunoticeUpdateTime;
  @Column(name = "training_id")
  private int trainingId;
  @Column(name = "jiaowu_id")
  private int jiaowuId;

  public int getTrainingId() {
    return trainingId;
  }

  public void setTrainingId(int trainingId) {
    this.trainingId = trainingId;
  }

  public int getJiaowuId() {
    return jiaowuId;
  }

  public void setJiaowuId(int jiaowuId) {
    this.jiaowuId = jiaowuId;
  }

  public long getJiaowunoticeId() {
    return jiaowunoticeId;
  }

  public void setJiaowunoticeId(long jiaowunoticeId) {
    this.jiaowunoticeId = jiaowunoticeId;
  }


  public java.sql.Timestamp getJiaowunoticeCreateTime() {
    return jiaowunoticeCreateTime;
  }

  public void setJiaowunoticeCreateTime(java.sql.Timestamp jiaowunoticeCreateTime) {
    this.jiaowunoticeCreateTime = jiaowunoticeCreateTime;
  }


  public String getJiaowunoticeTitle() {
    return jiaowunoticeTitle;
  }

  public void setJiaowunoticeTitle(String jiaowunoticeTitle) {
    this.jiaowunoticeTitle = jiaowunoticeTitle;
  }


  public String getJiaowunoticeContent() {
    return jiaowunoticeContent;
  }

  public void setJiaowunoticeContent(String jiaowunoticeContent) {
    this.jiaowunoticeContent = jiaowunoticeContent;
  }


  public java.sql.Timestamp getJiaowunoticeUpdateTime() {
    return jiaowunoticeUpdateTime;
  }

  public void setJiaowunoticeUpdateTime(java.sql.Timestamp jiaowunoticeUpdateTime) {
    this.jiaowunoticeUpdateTime = jiaowunoticeUpdateTime;
  }

}
