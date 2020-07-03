package com.example.demo2.pojo;


import javax.persistence.*;

@Entity
@Table(name= "file_resource")
public class FileResource {
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }
    public String getPath() {
        return Path;
    }
    public void setPath(String Path){
        this.Path=Path;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public String getFilename() {
        return filename;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "file_path")
    private String Path;

    @Column(name = "filename")
    private String filename;


}