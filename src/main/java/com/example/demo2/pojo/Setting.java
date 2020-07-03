package com.example.demo2.pojo;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "setting")
public class Setting {

  @Id
  @Column(name = "id")
  private String id;
  @Column(name = "akey")
  private String akey;//通过key查询value是否存在
  @Column(name = "avalue")
  private String avalue;//1表示存在，0表示删除
  @Column(name = "create_time")
  private Date createTime;
  @Column(name = "update_time")
  private Date updateTime;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAkey() {
    return akey;
  }

  public void setAkey(String akey) {
    this.akey = akey;
  }

  public String getAvalue() {
    return avalue;
  }

  public void setAvalue(String avalue) {
    this.avalue = avalue;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }
}
