package com.example.demo2.pojo;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "studentreport")
public class Studentreport {

  @Id
  @Column(name = "studentreport_id")
  private int studentreportId;

  @Column(name = "studentreport_type")
  private String studentreportType;

  @Column(name = "studentreport_title")
  private String studentreportTitle;

  @Column(name = "studentreport_date")
  private Date studentreportDate;

  @Column(name = "studentreport_filepath")
  private String studentreportFilepath;

  public String getStudentreportFilename() {
    return studentreportFilename;
  }

  public void setStudentreportFilename(String studentreportFilename) {
    this.studentreportFilename = studentreportFilename;
  }

  @Column(name = "studentreport_filename")
  private String studentreportFilename;

  @Column(name = "studentreport_status")
  private String studentreportStatus;

  @Column(name = "student_id")
  private String studentId;

  public String getStudentId() {
    return studentId;
  }

  public int getStudentreportId() {
    return studentreportId;
  }

  public void setStudentreportId(int studentreportId) {
    this.studentreportId = studentreportId;
  }

  public String getStudentreportType() {
    return studentreportType;
  }

  public void setStudentreportType(String studentreportType) {
    this.studentreportType = studentreportType;
  }

  public String getStudentreportTitle() {
    return studentreportTitle;
  }

  public void setStudentreportTitle(String studentreportTitle) {
    this.studentreportTitle = studentreportTitle;
  }

  public Date getStudentreportDate() {
    return studentreportDate;
  }

  public void setStudentreportDate(Date studentreportDate) {
    this.studentreportDate = studentreportDate;
  }

  public String getStudentreportFilepath() {
    return studentreportFilepath;
  }

  public void setStudentreportFilepath(String studentreportFilepath) {
    this.studentreportFilepath = studentreportFilepath;
  }

  public String getStudentreportStatus() {
    return studentreportStatus;
  }

  public void setStudentreportStatus(String studentreportStatus) {
    this.studentreportStatus = studentreportStatus;
  }

  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }
}
