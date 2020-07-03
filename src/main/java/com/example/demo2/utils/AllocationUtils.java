package com.example.demo2.utils;
import java.io.File;
import java.util.Random;

public class AllocationUtils {

    public static int[][] allocate1(int[][] stuData, int[][] projectData) {//第一种分配方法
        int maxProject = 0; //最大项目号
        int maxPrjStuNum = 0; //最大项目人数
        for (int i = 0; i < stuData.length; i++) {//先把所有学生分配到第一志愿
            stuData[i][5] = stuData[i][0];
            projectData[stuData[i][5]][1]++;//第一志愿人数加一
            if (projectData[stuData[i][5]][1] > maxPrjStuNum) {
                maxPrjStuNum = projectData[stuData[i][5]][1];
                maxProject = stuData[i][5];
            }
        }

        while (maxPrjStuNum > projectData[maxProject][0]) {
            int[][] maxStus = new int[6][maxPrjStuNum];
            int[] counts = new int[6];
            for (int i = 0; i < 6; i++) counts[i] = 0;

            for (int i = 0; i < stuData.length; i++) {  //分第几志愿获取人数最多项目的学生信息
                if (stuData[i][5] == maxProject) {
                    int count = 0;
                    for (int j = 0; j < 5; j++) {
                        if (stuData[i][j] == maxProject) { //在志愿内
                            maxStus[j][counts[j]] = i;
                            counts[j]++;
                            count++;
                            break;
                        }
                    }
                    if (count == 0) {//不在志愿内
                        maxStus[5][counts[5]] = i;
                        counts[5]++;
                    }
                }
            }
            int[] stuNum = new int[6];
            System.arraycopy(counts, 0, stuNum, 0, 6);
            Random r = new Random();
            int random;
            for (int i = 0; i < maxPrjStuNum - projectData[maxProject][0]; i++) { //将多出来的同学分到下调一个志愿
                if (i < counts[5]) {
                    random = r.nextInt(stuNum[5]);
                    stuData[maxStus[5][random]][5] = -1;
                    maxStus[5] = listOut(maxStus[5], random);
                    stuNum[5]--;
                } else if (i < counts[5] + counts[4]) {
                    random = r.nextInt(stuNum[4]);
                    stuData[maxStus[4][random]][5] = -1;//如果本来就是第五志愿且项目也满了，则将分配项目改为-1
                    maxStus[4] = listOut(maxStus[4], random);
                    stuNum[4]--;
                } else if (i < counts[5] + counts[4] + counts[3]) {
                    random = r.nextInt(stuNum[3]);
                    stuData[maxStus[3][random]][5] = stuData[maxStus[3][random]][4];
                    if (stuData[maxStus[3][random]][4] != -1) projectData[stuData[maxStus[3][random]][4]][1]++;
                    maxStus[3] = listOut(maxStus[3], random);
                    stuNum[3]--;
                } else if (i < counts[5] + counts[4] + counts[3] + counts[2]) {
                    random = r.nextInt(stuNum[2]);
                    stuData[maxStus[2][random]][5] = stuData[maxStus[2][random]][3];
                    if (stuData[maxStus[2][random]][3] != -1) projectData[stuData[maxStus[2][random]][3]][1]++;
                    maxStus[2] = listOut(maxStus[2], random);
                    stuNum[2]--;
                } else if (i < counts[5] + counts[4] + counts[3] + counts[2] + counts[1]) {
                    random = r.nextInt(stuNum[1]);
                    stuData[maxStus[1][random]][5] = stuData[maxStus[1][random]][2];
                    if (stuData[maxStus[1][random]][2] != -1) projectData[stuData[maxStus[1][random]][2]][1]++;
                    maxStus[1] = listOut(maxStus[1], random);
                    stuNum[1]--;
                } else {
                    random = r.nextInt(stuNum[0]);
                    stuData[maxStus[0][random]][5] = stuData[maxStus[0][random]][1];//随机从第一志愿的学生中选一名下调一个档次
                    if (stuData[maxStus[0][random]][1] != -1) projectData[stuData[maxStus[0][random]][1]][1]++;
                    maxStus[0] = listOut(maxStus[0], random);
                    stuNum[0]--;
                }
                projectData[maxProject][1]--;
            }

            maxProject = 0;
            maxPrjStuNum = 0;
            for (int i = 0; i < projectData.length; i++) {  //得到下一个爆满项目
                if (projectData[i][1] > projectData[i][0] && projectData[i][1] > maxPrjStuNum) {
                    maxPrjStuNum = projectData[i][1];
                    maxProject = i;
                }
            }
        }

        stuData=allocateTheRest(stuData,projectData);
        return stuData;
    }

    public static int[][] allocate2(int[][] stuData, int[][] projectData){  //第二种分配方法

        for (int i = 0; i < stuData.length; i++) {
            stuData[i][5]=-1;
        }

        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < projectData.length; i++) {
                for (int k = 0; k < stuData.length&&projectData[i][0]>projectData[i][1]; k++) {
                    if(stuData[k][j]==i&&getRank(stuData[k],stuData[k][5])>j){
                        if(stuData[k][5]!=-1){
                            projectData[stuData[k][5]][1]--;
                        }
                        stuData[k][5]=i;
                        projectData[i][1]++;
                    }
                }
            }
        }

        stuData=allocateTheRest(stuData,projectData);

        return stuData;
    }

    public static int[][] allocateTheRest(int[][] stuData,int[][] projectData){  //分配剩余学生
        boolean allocated;
        for (int i = 0; i < stuData.length; i++) {
            allocated=false;
            if (stuData[i][5] == -1) {
                for (int j = 0; j < 5&&stuData[i][j]!=-1; j++) {
                    if(projectData[stuData[i][j]][0]>projectData[stuData[i][j]][1]){
                        stuData[i][5]=stuData[i][j];
                        projectData[stuData[i][5]][1]++;
                        allocated=true;
                        break;
                    }
                }
                if(allocated==false) {
                    for (int j = 0; j < projectData.length; j++) {
                        if (projectData[j][1] < projectData[j][0]) {
                            stuData[i][5] = j;
                            projectData[j][1]++;
                            break;
                        }
                    }
                }
            }
        }
        return stuData;
    }

    public static int getRank(int[] wish,int projectNum){
        for (int i = 0; i < 5; i++) {
            if(wish[i]==projectNum){
                return i;
            }
        }
        return 5;
    }

    public static int[] listOut(int[] list, int n) {
        for (int i = n; i < list.length - 1; i++) {
            list[i] = list[i + 1];
        }
        return list;
    }
}
