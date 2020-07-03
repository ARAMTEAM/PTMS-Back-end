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
@Table(name = "jiaowu")
public class Jiaowu {

  @Id
  @Column(name = "jiaowu_id")
  private String jiaowuId;

  @Column(name = "jiaowu_password")
  private String jiaowuPassword;

  @Column(name = "jiaowu_dept")
  private String jiaowuDept;

  @Column(name = "jiaowu_name")
  private String jiaowuName;

  @Column(name = "jiaowu_username")
  private String jiaowuUsername;

  @Column(name = "jiaowu_nianji")
  private String jiaowuNianji;

  //除了密码都有的构造方法
  public Jiaowu(String jiaowuId,
                String jiaowuDept,
                String jiaowuName,
                String jiaowuUsername,
                String jiaowuNianji){
    this.jiaowuId = jiaowuId;
    this.jiaowuDept = jiaowuDept;
    this.jiaowuName = jiaowuName;
    this.jiaowuUsername = jiaowuUsername;
    this.jiaowuNianji = jiaowuNianji;
  }

  public Jiaowu(){

  }

  public String getJiaowuId() {
    return jiaowuId;
  }

  public void setJiaowuId(String jiaowuId) {
    this.jiaowuId = jiaowuId;
  }

  public String getJiaowuPassword() {
    return jiaowuPassword;
  }

  public void setJiaowuPassword(String jiaowuPassword) {
    this.jiaowuPassword = jiaowuPassword;
  }


  public String getJiaowuDept() {
    return jiaowuDept;
  }

  public void setJiaowuDept(String jiaowuDept) {
    this.jiaowuDept = jiaowuDept;
  }


  public String getJiaowuName() {
    return jiaowuName;
  }

  public void setJiaowuName(String jiaowuName) {
    this.jiaowuName = jiaowuName;
  }


  public String getJiaowuUsername() {
    return jiaowuUsername;
  }

  public void setJiaowuUsername(String jiaowuUsername) {
    this.jiaowuUsername = jiaowuUsername;
  }


  public String getJiaowuNianji() {
    return jiaowuNianji;
  }

  public void setJiaowuNianji(String jiaowuNianji) {
    this.jiaowuNianji = jiaowuNianji;
  }

}
