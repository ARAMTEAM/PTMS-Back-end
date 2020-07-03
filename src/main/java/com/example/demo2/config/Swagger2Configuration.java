package com.example.demo2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Configuration {

    //版本
    public static final String VERSION = "1.0.0";

    /**
     * 管理员api
     *
     * @return
     */
    @Bean
    public Docket Admin_Api() {
        return new Docket(DocumentationType.SWAGGER_12)
                .apiInfo(adminApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo2.controller.admin"))
                .paths(PathSelectors.any()) // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build()
                .groupName("管理员Api");
    }
    private ApiInfo adminApiInfo() {
        return new ApiInfoBuilder()
                .title("实训管理系统管理员接口文档") //设置文档的标题
                .description("管理员接口") // 设置文档的描述
                .version(VERSION) // 设置文档的版本信息-> 1.0.0 Version information
                .build();
    }

    /**
     * 教务api
     *
     * @return
     */
    @Bean
    public Docket Jiaowu_Api() {
        return new Docket(DocumentationType.SWAGGER_12)
                .apiInfo(jiaowuApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo2.controller.jiaowu"))
                .paths(PathSelectors.any()) // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build()
                .groupName("教务Api");
    }
    private ApiInfo jiaowuApiInfo() {
        return new ApiInfoBuilder()
                .title("实训管理系统教务接口文档") //设置文档的标题
                .description("教务接口文档") // 设置文档的描述
                .version(VERSION) // 设置文档的版本信息-> 1.0.0 Version information
                .build();
    }

    /**
     * 管理员公告api
     *
     * @return
     */
    @Bean
    public Docket AdminNotice_Api() {
        return new Docket(DocumentationType.SWAGGER_12)
                .apiInfo(adminnoticeApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo2.controller.adminnotice"))
                .paths(PathSelectors.any()) // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build()
                .groupName("管理员公告Api");
    }
    private ApiInfo adminnoticeApiInfo() {
        return new ApiInfoBuilder()
                .title("实训管理系统管理员公告接口文档") //设置文档的标题
                .description("管理员公告接口文档") // 设置文档的描述
                .version(VERSION) // 设置文档的版本信息-> 1.0.0 Version information
                .build();
    }

    /**
     * 教务api
     *
     * @return
     */
    @Bean
    public Docket Training_Api() {
        return new Docket(DocumentationType.SWAGGER_12)
                .apiInfo(trainingApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo2.controller.training"))
                .paths(PathSelectors.any()) // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build()
                .groupName("实训Api");
    }
    private ApiInfo trainingApiInfo() {
        return new ApiInfoBuilder()
                .title("实训管理系统实训接口文档") //设置文档的标题
                .description("实训接口文档") // 设置文档的描述
                .version(VERSION) // 设置文档的版本信息-> 1.0.0 Version information
                .build();
    }

    /**
     * 学生api
     *
     * @return
     */
    @Bean
    public Docket Student_Api() {
        return new Docket(DocumentationType.SWAGGER_12)
                .apiInfo(studentApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo2.controller.student"))
                .paths(PathSelectors.any()) // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build()
                .groupName("学生Api");
    }
    private ApiInfo studentApiInfo() {
        return new ApiInfoBuilder()
                .title("实训管理系统学生接口文档") //设置文档的标题
                .description("学生接口文档") // 设置文档的描述
                .version(VERSION) // 设置文档的版本信息-> 1.0.0 Version information
                .build();
    }

    /**
     * 教师api
     *
     * @return
     */
    @Bean
    public Docket Teacher_Api() {
        return new Docket(DocumentationType.SWAGGER_12)
                .apiInfo(teacherApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo2.controller.teacher"))
                .paths(PathSelectors.any()) // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build()
                .groupName("教师Api");
    }
    private ApiInfo teacherApiInfo() {
        return new ApiInfoBuilder()
                .title("实训管理系统教师接口文档") //设置文档的标题
                .description("教师接口文档") // 设置文档的描述
                .version(VERSION) // 设置文档的版本信息-> 1.0.0 Version information
                .build();
    }

    /**
     * 教师api
     *
     * @return
     */
    @Bean
    public Docket Expectation_Api() {
        return new Docket(DocumentationType.SWAGGER_12)
                .apiInfo(ExpectationApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo2.controller.expectation"))
                .paths(PathSelectors.any()) // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build()
                .groupName("志愿Api");
    }
    private ApiInfo ExpectationApiInfo() {
        return new ApiInfoBuilder()
                .title("实训管理系统志愿接口文档") //设置文档的标题
                .description("志愿接口文档") // 设置文档的描述
                .version(VERSION) // 设置文档的版本信息-> 1.0.0 Version information
                .build();
    }


    /**
     * 项目api
     *
     * @return
     */
    @Bean
    public Docket Project_Api() {
        return new Docket(DocumentationType.SWAGGER_12)
                .apiInfo(projectInFo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo2.controller.project"))
                .paths(PathSelectors.any()) // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build()
                .groupName("项目Api");
    }
    private ApiInfo projectInFo() {
        return new ApiInfoBuilder()
                .title("实训管理系统项目接口文档") //设置文档的标题
                .description("项目接口文档") // 设置文档的描述
                .version(VERSION) // 设置文档的版本信息-> 1.0.0 Version information
                .build();
    }
    /**
     * 学生_项目api
     *
     * @return
     */
    @Bean
    public Docket stuAndpro_Api() {
        return new Docket(DocumentationType.SWAGGER_12)
                .apiInfo(projectInFo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo2.controller.studentprojectgrade"))
                .paths(PathSelectors.any()) // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build()
                .groupName("学生_项目Api");
    }
    private ApiInfo stuAndproInFo() {
        return new ApiInfoBuilder()
                .title("实训管理系统学生_项目接口文档") //设置文档的标题
                .description("学生_项目接口文档") // 设置文档的描述
                .version(VERSION) // 设置文档的版本信息-> 1.0.0 Version information
                .build();
    }

    /**
     * 教务公告api
     *
     * @return
     */
    @Bean
    public Docket JiaowuNotice_Api() {
        return new Docket(DocumentationType.SWAGGER_12)
                .apiInfo(JiaowuNoticeInFo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo2.controller.trainingnotice"))
                .paths(PathSelectors.any()) // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build()
                .groupName("实训公告Api");
    }
    private ApiInfo JiaowuNoticeInFo() {
        return new ApiInfoBuilder()
                .title("实训管理系统实训公告接口文档") //设置文档的标题
                .description("实训公告接口文档") // 设置文档的描述
                .version(VERSION) // 设置文档的版本信息-> 1.0.0 Version information
                .build();
    }

    /**
     * 学生报告api
     *
     * @return
     */
    @Bean
    public Docket Studentreport_Api() {
        return new Docket(DocumentationType.SWAGGER_12)
                .apiInfo(StudentreportInFo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo2.controller.studentreport"))
                .paths(PathSelectors.any()) // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build()
                .groupName("学生报告Api");
    }
    private ApiInfo StudentreportInFo() {
        return new ApiInfoBuilder()
                .title("实训管理系统学生报告接口文档") //设置文档的标题
                .description("实训学生报告接口文档") // 设置文档的描述
                .version(VERSION) // 设置文档的版本信息-> 1.0.0 Version information
                .build();
    }

    /**
     * 项目公告api
     *
     * @return
     */
    @Bean
    public Docket Projectnotice_Api() {
        return new Docket(DocumentationType.SWAGGER_12)
                .apiInfo(ProjectnoticeInFo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo2.controller.projectnotice"))
                .paths(PathSelectors.any()) // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build()
                .groupName("项目公告Api");
    }
    private ApiInfo ProjectnoticeInFo() {
        return new ApiInfoBuilder()
                .title("实训管理系统项目公告接口文档") //设置文档的标题
                .description("实训项目公告接口文档") // 设置文档的描述
                .version(VERSION) // 设置文档的版本信息-> 1.0.0 Version information
                .build();
    }


    /**
     * 文件上传api
     *
     * @return
     */
    @Bean
    public Docket FileUpload_Api() {
        return new Docket(DocumentationType.SWAGGER_12)
                .apiInfo(FileUploadApiinfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo2.controller.fileResource"))
                .paths(PathSelectors.any()) // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build()
                .groupName("文件上传Api");
    }
    private ApiInfo FileUploadApiinfo() {
        return new ApiInfoBuilder()
                .title("实训管理系统文件上传接口文档") //设置文档的标题
                .description("实训文件上传接口文档") // 设置文档的描述
                .version(VERSION) // 设置文档的版本信息-> 1.0.0 Version information
                .build();
    }

    /**
     * 数据库备份
     *
     * @return
     */
    @Bean
    public Docket DbBackup_Api() {
        return new Docket(DocumentationType.SWAGGER_12)
                .apiInfo(DbBackupApiinfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo2.controller.backup"))
                .paths(PathSelectors.any()) // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build()
                .groupName("数据库备份Api");
    }
    private ApiInfo DbBackupApiinfo() {
        return new ApiInfoBuilder()
                .title("实训管理系统数据库备份接口文档") //设置文档的标题
                .description("实训数据库备份接口文档") // 设置文档的描述
                .version(VERSION) // 设置文档的版本信息-> 1.0.0 Version information
                .build();
    }

    /**
     * 管理员日志
     *
     * @return
     */
    @Bean
    public Docket Slogs_Api() {
        return new Docket(DocumentationType.SWAGGER_12)
                .apiInfo(Slogsinfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo2.controller.slogs"))
                .paths(PathSelectors.any()) // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build()
                .groupName("管理员日志Api");
    }
    private ApiInfo Slogsinfo() {
        return new ApiInfoBuilder()
                .title("实训管理系统管理员日志接口文档") //设置文档的标题
                .description("实训管理员日志接口文档") // 设置文档的描述
                .version(VERSION) // 设置文档的版本信息-> 1.0.0 Version information
                .build();
    }
}