package com.example.demo2.pojo;


import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "training")
public class Training {

  @Id
  @Column(name = "training_id")
  private int trainingId;
  @Column(name = "training_name")
  private String trainingName;
  @Column(name = "training_notice")
  private String trainingNotice;
  @Column(name = "training_time_1")
  private Date trainingTime1;
  @Column(name = "training_time_2")
  private Date trainingTime2;
  @Column(name = "training_time_3")
  private Date trainingTime3;
  @Column(name = "training_time_4")
  private Date trainingTime4;
  @Column(name = "training_time_5")
  private Date trainingTime5;
  @Column(name = "training_time_6")
  private Date trainingTime6;
  @Column(name = "kecheng_id")
  private String kechengId;
  @Column(name = "kexu_id")
  private String kexuID;
  @Column(name = "jiaowu_id")
  private String jiaowuId;


  public Training() {
  }

  public Training(int trainingId,
                  String trainingName,
                  String trainingNotice,
                  Date trainingTime1,
                  Date trainingTime2,
                  Date trainingTime3,
                  Date trainingTime4,
                  Date trainingTime5,
                  Date trainingTime6,
                  String kechengId,
                  String kexuID) {
    this.trainingId = trainingId;
    this.trainingName = trainingName;
    this.trainingNotice = trainingNotice;
    this.trainingTime1 = trainingTime1;
    this.trainingTime2 = trainingTime2;
    this.trainingTime3 = trainingTime3;
    this.trainingTime4 = trainingTime4;
    this.trainingTime5 = trainingTime5;
    this.trainingTime6 = trainingTime6;
    this.kechengId = kechengId;
    this.kexuID = kexuID;
  }


  public Date getTrainingTime1() {
    return trainingTime1;
  }

  public void setTrainingTime1(Date trainingTime1) {
    this.trainingTime1 = trainingTime1;
  }

  public Date getTrainingTime2() {
    return trainingTime2;
  }

  public void setTrainingTime2(Date trainingTime2) {
    this.trainingTime2 = trainingTime2;
  }

  public Date getTrainingTime3() {
    return trainingTime3;
  }

  public void setTrainingTime3(Date trainingTime3) {
    this.trainingTime3 = trainingTime3;
  }

  public Date getTrainingTime4() {
    return trainingTime4;
  }

  public void setTrainingTime4(Date trainingTime4) {
    this.trainingTime4 = trainingTime4;
  }

  public Date getTrainingTime5() {
    return trainingTime5;
  }

  public void setTrainingTime5(Date trainingTime5) {
    this.trainingTime5 = trainingTime5;
  }

  public Date getTrainingTime6() {
    return trainingTime6;
  }

  public void setTrainingTime6(Date trainingTime6) {
    this.trainingTime6 = trainingTime6;
  }


  public String getJiaowuId() {
    return jiaowuId;
  }

  public void setJiaowuId(String jiaowuId) {
    this.jiaowuId = jiaowuId;
  }


  public String getKechengId() {
    return kechengId;
  }

  public void setKechengId(String kechengId) {
    this.kechengId = kechengId;
  }

  public String getKexuID() {
    return kexuID;
  }

  public void setKexuID(String kexuID) {
    this.kexuID = kexuID;
  }

  public int getTrainingId() {
    return trainingId;
  }

  public void setTrainingId(int trainingId) {
    this.trainingId = trainingId;
  }



  public String getTrainingName() {
    return trainingName;
  }

  public void setTrainingName(String trainingName) {
    this.trainingName = trainingName;
  }


  public String getTrainingNotice() {
    return trainingNotice;
  }

  public void setTrainingNotice(String trainingNotice) {
    this.trainingNotice = trainingNotice;
  }


}
