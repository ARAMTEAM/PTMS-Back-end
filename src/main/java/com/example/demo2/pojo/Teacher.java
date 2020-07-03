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
@Table(name = "teacher")
public class Teacher {

  @Id
  @Column(name = "teacher_id")
  private String teacherId;
  @Column(name = "teacher_password")
  private String teacherPassword;
  @Column(name = "teacher_name")
  private String teacherName;
  @Column(name = "teacher_dept")
  private String teacherDept;
  @Column(name = "teacher_rank")
  private String teacherRank;
  @Column(name = "teacher_telephone")
  private String teacherTelephone;
  @Column(name = "teacher_email")
  private String teacherEmail;
  @Column(name = "training_id")
  private int trainingId;


  public String getTeacherId() {
    return teacherId;
  }

  public void setTeacherId(String teacherId) {
    this.teacherId = teacherId;
  }


  public String getTeacherPassword() {
    return teacherPassword;
  }

  public void setTeacherPassword(String teacherPassword) {
    this.teacherPassword = teacherPassword;
  }


  public String getTeacherName() {
    return teacherName;
  }

  public void setTeacherName(String teacherName) {
    this.teacherName = teacherName;
  }


  public String getTeacherDept() {
    return teacherDept;
  }

  public void setTeacherDept(String teacherDept) {
    this.teacherDept = teacherDept;
  }


  public String getTeacherRank() {
    return teacherRank;
  }

  public void setTeacherRank(String teacherRank) {
    this.teacherRank = teacherRank;
  }


  public String getTeacherTelephone() {
    return teacherTelephone;
  }

  public void setTeacherTelephone(String teacherTelephone) {
    this.teacherTelephone = teacherTelephone;
  }


  public String getTeacherEmail() {
    return teacherEmail;
  }

  public void setTeacherEmail(String teacherEmail) {
    this.teacherEmail = teacherEmail;
  }


  public int getTrainingId() {
    return trainingId;
  }

  public void setTrainingId(int trainingId) {
    this.trainingId = trainingId;
  }

}
