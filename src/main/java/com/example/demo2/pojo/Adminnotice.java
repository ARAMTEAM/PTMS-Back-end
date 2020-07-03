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
@Table(name = "adminnotice")
public class Adminnotice {

  @Id
  @Column(name = "adminnotice_id")
  private int adminnoticeId;
  @Column(name = "adminnotice_title")
  private String adminnoticeTitle;
  @Column(name = "adminnotice_content")
  private String adminnoticeContent;
  @Column(name = "adminnotice_create_time")
  private java.sql.Timestamp adminnoticeCreateTime;
  @Column(name = "adminnotice_update_time")
  private java.sql.Timestamp adminnoticeUpdateTime;
  @Column(name = "admin_id")
  private String adminId;

  public String getAdminId() {
    return adminId;
  }

  public void setAdminId(String adminId) {
    this.adminId = adminId;
  }

  public int getAdminnoticeId() {
    return adminnoticeId;
  }

  public void setAdminnoticeId(int adminnoticeId) {
    this.adminnoticeId = adminnoticeId;
  }


  public String getAdminnoticeTitle() {
    return adminnoticeTitle;
  }

  public void setAdminnoticeTitle(String adminnoticeTitle) {
    this.adminnoticeTitle = adminnoticeTitle;
  }


  public String getAdminnoticeContent() {
    return adminnoticeContent;
  }

  public void setAdminnoticeContent(String adminnoticeContent) {
    this.adminnoticeContent = adminnoticeContent;
  }


  public java.sql.Timestamp getAdminnoticeCreateTime() {
    return adminnoticeCreateTime;
  }

  public void setAdminnoticeCreateTime(java.sql.Timestamp adminnoticeCreateTime) {
    this.adminnoticeCreateTime = adminnoticeCreateTime;
  }


  public java.sql.Timestamp getAdminnoticeUpdateTime() {
    return adminnoticeUpdateTime;
  }

  public void setAdminnoticeUpdateTime(java.sql.Timestamp adminnoticeUpdateTime) {
    this.adminnoticeUpdateTime = adminnoticeUpdateTime;
  }



}
