package com.example.demo2.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * MySQL备份还原工具类
 * @author Louis
 * @date Sep 21, 2018
 */
@Slf4j
public class MySqlBackupRestoreUtils {

    /**
     * 备份数据库
     * @param host host地址，可以是本机也可以是远程
     * @param userName 数据库的用户名
     * @param password 数据库的密码
     * @param backupFolderPath 备份的路径
     * @param fileName 备份的文件名
     * @param database 需要备份的数据库的名称
     * @return
     * @throws IOException
     */
    public static boolean backup(String host, String userName, String password, String backupFolderPath, String fileName, String database) throws Exception {
        File backupFolderFile = new File(backupFolderPath);
        if (!backupFolderFile.exists()) {
            // 如果目录不存在则创建
            backupFolderFile.mkdirs();
        }
        if (!backupFolderPath.endsWith(File.separator) && !backupFolderPath.endsWith("/")) {
            backupFolderPath = backupFolderPath + File.separator;
        }
        String backupFilePath = backupFolderPath + fileName;
        // 调用外部执行 exe 文件的 Java API
        try {
            Runtime rt = Runtime.getRuntime();

            // 调用 调用mysql的安装目录的命令
            Process child = rt.exec("mysqldump -h "+host+" -u"+userName+" -p"+password+" "+database);
            // 设置导出编码为utf-8。这里必须是utf-8
            // 把进程执行中的控制台输出信息写入.sql文件，即生成了备份文件。注：如果不对控制台信息进行读出，则会导致进程堵塞无法运行
            InputStream in = child.getInputStream();// 控制台的输出信息作为输入流
            InputStreamReader xx = new InputStreamReader(in, "utf-8");
            // 设置输出流编码为utf-8。这里必须是utf-8，否则从流中读入的是乱码

            String inStr;
            StringBuffer sb = new StringBuffer("");
            String outStr;
            // 组合控制台输出信息字符串
            BufferedReader br = new BufferedReader(xx);
            while ((inStr = br.readLine()) != null) {
                sb.append(inStr + "\r\n");
            }
            outStr = sb.toString();

            // 要用来做导入用的sql目标文件：
            FileOutputStream fout = new FileOutputStream(backupFilePath+".sql");
            OutputStreamWriter writer = new OutputStreamWriter(fout, "utf-8");
            writer.write(outStr);
            writer.flush();
            in.close();
            xx.close();
            br.close();
            writer.close();
            fout.close();
            System.out.println("");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;


    }

    /**
     * 还原数据库
     * @param restoreFilePath 数据库备份的脚本路径
     * @param host IP地址
     * @param database 数据库名称
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    public static boolean restore(String restoreFilePath, String host, String userName, String password, String database) throws Exception {
        File restoreFile = new File(restoreFilePath);
        if (restoreFile.isDirectory()) {
            for (File file : restoreFile.listFiles()) {
                if (file.exists() && file.getPath().endsWith(".sql")) {
                    restoreFilePath = file.getAbsolutePath();
                    break;
                }
            }
        }

        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime
                    .exec("mysql.exe -h"+host+" -u"+userName+" -p"+password+" --default-character-set=utf8 " + database);
            OutputStream outputStream = process.getOutputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(restoreFilePath), "utf-8"));
            String str = null;
            StringBuffer sb = new StringBuffer();
            while ((str = br.readLine()) != null) {
//                log.info(str);
                sb.append(str + "\r\n");
            }
            str = sb.toString();
//            log.info(str);
            // System.out.println(str);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream, "utf-8");
            writer.write(str);
            writer.flush();
            outputStream.close();
            br.close();
            writer.close();
            log.info("数据已从 " + restoreFilePath + " 导入到数据库中");
            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.info("数据从 " + restoreFilePath + " 导入到数据库失败");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.info("数据从 " + restoreFilePath + " 导入到数据库失败");
        } catch (IOException e) {
            e.printStackTrace();
            log.info("数据从 " + restoreFilePath + " 导入到数据库失败");
        }
        return false;
    }




}
