package com.history;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.history.db.PageInsert;

public class Main {

   // private static PageInsert pageInsert = new PageInsert();
    private static int extraterror = 0;
    private static int inserterror = 0;
    private static int success = 0;

    private static OutputStream ops = null;
    private static long begintime = System.currentTimeMillis();
    private static long lasttime = 0;

    public static void main(String[] args) {
        String dir = "/Users/wangbei/his/history/";

        //debug
        String filePath = "/Users/wangbei/his/history/1_2.html";


        File dirFile =  new File(dir);
        //日志文件
        try {
            ops = new FileOutputStream(dirFile.getParent()+"/log-"+System.currentTimeMillis()+".txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //debug
        File testFile = new File(dir);
        resloveFile(testFile);

        //resloveFile(new File(dir));

        try {
            writeLog("time: "+(System.currentTimeMillis()-begintime)/1000+"  success: "+success+"  ExtratError: "+extraterror+"  inserterror: "+inserterror);
            ops.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void resloveFile(File file){
        long currenttime = System.currentTimeMillis();
        if(currenttime > (lasttime+5000)){
            System.out.println("time: "+(currenttime-begintime)+"  success: "+success+"  ExtratError: "+extraterror+"  inserterror: "+inserterror);
        }
        lasttime = currenttime;
        if(file.isFile() ){
            String path[] = file.getPath().split("/");
            if("history".equals(path[path.length-2]) && path[path.length-1].matches("[0-9]*_?[0-9]*.html$")){
            	//System.out.println("!!!");
                Page page = null;
                boolean isSkip = false;
                try{
                    page = Extractor.extractHtml(file);
                    System.out.println(page);
                }catch (Exception e){
                    isSkip = true;
                    extraterror++;
                    writeLog("extract error   "+file.getPath());
                    System.out.println("解析失败  "+ file.getPath());
                }
                /*if(!isSkip){
                    try{
                        int result = sightInsert.insert(sight);
                        if(result > 0){
                            success++;
                        }else {
                            inserterror++;
                            writeLog("Duplicate insert error "+file.getPath());
                            System.out.print("已存在该景点   ");
                        }
                        System.out.print(result+"  名字:("+sight.getName()+")   属地:(");
                        for (int i = 0; i < sight.getPlace().length; i++) {
                            System.out.print(sight.getPlace()[i]+"/");
                        }
                        System.out.println(")");
                    }catch (Exception e){
                        inserterror++;
                        writeLog("insert error   "+file.getPath());
                        System.out.println("插入失败  "+ file.getPath());
                    }
                }*/
            }
        }else {
            //System.out.println("分析文件夹  "+file.getPath());
            for (File subFile :
                    file.listFiles() ) {
                resloveFile(subFile);
            }
        }
    }

    private static void writeLog(String log){
        try {
            ops.write((log+"\n").getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
