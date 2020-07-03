package com.example.demo2.pojo;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "expectation")
public class Expectation {

  @Id
  @Column(name = "student_id")
  private String studentId;
  @Column(name = "expectation_1")
  private int expectation1;
  @Column(name = "expectation_2")
  private int expectation2;
  @Column(name = "expectation_3")
  private int expectation3;
  @Column(name = "expectation_4")
  private int expectation4;
  @Column(name = "expectation_5")
  private int expectation5;

  public String getStudentId() {
    return studentId;
  }

  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }

  public int getExpectation1() {
    return expectation1;
  }

  public void setExpectation1(int expectation1) {
    this.expectation1 = expectation1;
  }

  public int getExpectation2() {
    return expectation2;
  }

  public void setExpectation2(int expectation2) {
    this.expectation2 = expectation2;
  }

  public int getExpectation3() {
    return expectation3;
  }

  public void setExpectation3(int expectation3) {
    this.expectation3 = expectation3;
  }

  public int getExpectation4() {
    return expectation4;
  }

  public void setExpectation4(int expectation4) {
    this.expectation4 = expectation4;
  }

  public int getExpectation5() {
    return expectation5;
  }

  public void setExpectation5(int expectation5) {
    this.expectation5 = expectation5;
  }
}
