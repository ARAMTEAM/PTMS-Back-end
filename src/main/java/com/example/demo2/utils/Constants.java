package com.example.demo2.utils;

public interface Constants {
     interface Page{
         int DEFAUT_PAGE_SIZE = 10;
     }

    interface TimeValue{
        int MIN_1 = 60;
        int HOUR_1 =60 * MIN_1;
        int HOUR_2 =2 * HOUR_1 ; //2小时的毫秒数
        int DAY = 24 * HOUR_1;
        int MONTH = 30 * DAY;
        int YEAR = 365 * DAY;

    }

    interface User{
        String KEY_CONTENT = "KEY_CONTNET_";
        String KEY_TOKEN = "key_token";
        String COOKIE_TOKE_KEY = "ptms_aram_token";
        String UserType_admin = "管理员";
        String UserType_jiaowu = "教务";
        String UserType_teacher = "教师";
        String UserType_student = "学生";
    }

    interface operation{
        String OP_LOGIN = "登录";
        String OP_LOGOUT = "登出";
        String OP_ADDJIAOWU = "创建教务";
        String OP_DELETEJIAOWU = "删除教务";
        String OP_UPDATEJIAOWU = "更新教务";
    }


    interface Status{
         String STATUS1 = "待审核";
         String STATUS2 = "通过";
         String STATUS3 = "需答辩";
         String STATUS4 = "不通过";
         String STATUS5 = "等待教师审核";
         String STATUS6 = "教师未同意";
    }

    interface Setting {
        String MANAGER_ACCOUNT_INIT_STATE = "manager_account_init_state";
    }


}
