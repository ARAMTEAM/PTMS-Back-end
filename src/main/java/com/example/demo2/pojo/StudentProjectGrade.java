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
@Table(name = "student_project_grade")
public class StudentProjectGrade {

  @Id
  @Column(name = "student_id")
  private String studentId;
  @Column(name = "project_id")
  private int projectId;
  @Column(name = "grades_100points")
  private int grades100Points;
  @Column(name = "grades_5points")
  private double grades5Points;
  @Column(name = "isleader")
  private int isleader;
  @Column(name = "isjointed")
  private int isjointed;
  @Column(name ="training_id")
  private int trainingId;

  public int getTrainingId() {
    return trainingId;
  }

  public void setTrainingId(int trainingId) {
    this.trainingId = trainingId;
  }

  public String getStudentId() {
    return studentId;
  }

  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }

  public int getProjectId() {
    return projectId;
  }

  public void setProjectId(int projectId) {
    this.projectId = projectId;
  }

  public int getGrades100Points() {
    return grades100Points;
  }

  public void setGrades100Points(int grades100Points) {
    this.grades100Points = grades100Points;
  }

  public double getGrades5Points() {
    return grades5Points;
  }

  public void setGrades5Points(double grades5Points) {
    this.grades5Points = grades5Points;
  }

  public int getIsleader() {
    return isleader;
  }

  public void setIsleader(int isleader) {
    this.isleader = isleader;
  }

  public int getIsjointed() {
    return isjointed;
  }

  public void setIsjointed(int isjointed) {
    this.isjointed = isjointed;
  }
}
