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
@Table(name = "slogs")
public class Slogs {

  @Id
  @Column(name = "ID")
  private int id;
  @Column(name = "time")
  private java.sql.Timestamp time;
  @Column(name = "user_type")
  private String userType;
  @Column(name = "user_id")
  private String userId;
  @Column(name = "operation")
  private String operation;
  @Column(name = "ip")
  private String ip;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public java.sql.Timestamp getTime() {
    return time;
  }

  public void setTime(java.sql.Timestamp time) {
    this.time = time;
  }


  public String getUserType() {
    return userType;
  }

  public void setUserType(String userType) {
    this.userType = userType;
  }


  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }


  public String getOperation() {
    return operation;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }


  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

}
