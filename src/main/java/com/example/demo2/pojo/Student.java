package com.example.demo2.pojo;


import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "student")
public class Student {

  @Id
  @Column(name = "student_id")
  private String studentId;
  @Column(name = "student_name")
  private String studentName;
  @Column(name = "student_password")
  private String studentPassword;
  @Column(name = "student_class")
  private String studentClass;
  @Column(name = "student_telephone")
  private String studentTelephone;
  @Column(name = "training_id")
  private Integer trainingId;

  public Integer getTrainingId() {
    return trainingId;
  }

  public void setTrainingId(Integer trainingId) {
    this.trainingId = trainingId;
  }

  public String getStudentId() {
    return studentId;
  }

  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }


  public String getStudentName() {
    return studentName;
  }

  public void setStudentName(String studentName) {
    this.studentName = studentName;
  }


  public String getStudentPassword() {
    return studentPassword;
  }

  public void setStudentPassword(String studentPassword) {
    this.studentPassword = studentPassword;
  }


  public String getStudentClass() {
    return studentClass;
  }

  public void setStudentClass(String studentClass) {
    this.studentClass = studentClass;
  }

  public String getStudentTelephone() {
    return studentTelephone;
  }

  public void setStudentTelephone(String studentTelephone) {
    this.studentTelephone = studentTelephone;
  }


}
