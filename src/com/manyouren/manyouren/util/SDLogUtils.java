package com.manyouren.manyouren.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;

public class SDLogUtils {
	
	public static String lastFileName = "";
	
	public static void saveToSDCard(String filePath,String fileName, String content) throws Exception{
        String path = Environment.getExternalStorageDirectory() + filePath;
        File dir = new File(path);
        if (!dir.exists()) {
			dir.mkdirs();
		}
        
        File file = new File(path + fileName + ".txt");
        if (!file.exists()) {
        	file.createNewFile();
		}
        
        try {
        	FileWriter filerWriter = null;
        	if(lastFileName.equals(fileName)){
        		 filerWriter = new FileWriter(file, true);//后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖  
        	}else{
        		filerWriter = new FileWriter(file, false);//后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖  
        	}
            
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);  
            bufWriter.write(content);  
            bufWriter.newLine();  
            bufWriter.close();  
            filerWriter.close();   
        } catch (IOException e) {  
            e.printStackTrace();  
        }finally{
        	lastFileName = fileName;
        }
    }  
	
	private String SDPATH;

	private int FILESIZE = 4 * 1024;

	public String getSDPATH() {
		return SDPATH;
	}

	public SDLogUtils() {
		// 得到当前外部存储设备的目录( /SDCARD )
		SDPATH = Environment.getExternalStorageDirectory() + "/CityLog/";
	}

	/**
	 * 在SD卡上创建文件
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public File createSDFile(String fileName) throws IOException {
		File file = new File(SDPATH + fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 * @return
	 */
	public File createSDDir(String dirName) {
		File dir = new File(SDPATH + dirName);
		dir.mkdir();
		return dir;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		return file.exists();
	}

	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 * 
	 * @param path
	 * @param fileName
	 * @param input
	 * @return
	 */
	public File write2SDFromInput(String path, String fileName,
			InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			createSDDir(path);
			file = createSDFile(path + fileName);
			output = new FileOutputStream(file);
			byte[] buffer = new byte[FILESIZE];

			/*
			 * 真机测试，这段可能有问题，请采用下面网友提供的 while((input.read(buffer)) != -1){
			 * output.write(buffer); }
			 */

			/* 网友提供 begin */
			int length;
			while ((length = (input.read(buffer))) > 0) {
				output.write(buffer, 0, length);
			}
			/* 网友提供 end */

			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

}
