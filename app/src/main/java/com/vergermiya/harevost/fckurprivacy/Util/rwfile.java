package com.vergermiya.harevost.fckurprivacy.Util;

import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
 
public class rwfile {
	/**
     * ��ȡ�ļ�����
     * 
     * @param filePathAndName
     * String �� c:\\1.txt ����·��
     * @return boolean
     */
   public static String readFile(String filePath) {
       String fileContent = "";
       String encoding = "gbk"; 
       //String encoding = "UTF-8";  
       File file = new File(filePath);
       try {

           if (file.isFile() && file.exists()) {
               /*InputStreamReader read = new InputStreamReader(new FileInputStream(f), "UTF-8");
               BufferedReader reader = new BufferedReader(read);
               String line;
               while ((line = reader.readLine()) != null) {
                   fileContent += line;
               }
               read.close();*/
        	   Long filelength = file.length();  
               byte[] filecontent = new byte[filelength.intValue()];  
               try {  
                   FileInputStream in = new FileInputStream(file);  
                   in.read(filecontent);  
                   in.close();  
               } catch (FileNotFoundException e) {  
                   e.printStackTrace();  
               } catch (IOException e) {  
                   e.printStackTrace();  
               }  
               try {  
                   return new String(filecontent, encoding);  
               } catch (UnsupportedEncodingException e) {  
                   System.err.println("The OS does not support " + encoding);  
                   e.printStackTrace();  
                   return null;  
               }  
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
       return fileContent;
   }
   
//   public String readToString(String fileName) {  
//       String encoding = "UTF-8";  
//       File file = new File(fileName);  
//       Long filelength = file.length();  
//       byte[] filecontent = new byte[filelength.intValue()];  
//       try {  
//           FileInputStream in = new FileInputStream(file);  
//           in.read(filecontent);  
//           in.close();  
//       } catch (FileNotFoundException e) {  
//           e.printStackTrace();  
//       } catch (IOException e) {  
//           e.printStackTrace();  
//       }  
//       try {  
//           return new String(filecontent, encoding);  
//       } catch (UnsupportedEncodingException e) {  
//           System.err.println("The OS does not support " + encoding);  
//           e.printStackTrace();  
//           return null;  
//       }  
//   }
//	
   
   /**
    * 
    * @Title: writeFile
    * @Description: д�ļ�
    * @param @param filePath �ļ�·��
    * @param @param fileContent    �ļ�����
    * @return void    ��������
    * @throws
    */
   public static void writeFile(String filePath, String fileContent) {
       try {
           File file = new File(filePath);
           if (!file.exists()) {
        	   file.createNewFile();
           }
           OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file), "gbk");
           BufferedWriter writer = new BufferedWriter(write);
           writer.write(fileContent);
           writer.close();
       } catch (Exception e) {
           System.out.println("д�ļ����ݲ�������");
           e.printStackTrace();
       }
   }
   
   
   
   
//	public void writefile(String fileName, String content) {  
//		try {
//			File writename = new File(fileName); // ���·�������û����Ҫ����һ���µ�output��txt�ļ�
//			writename.createNewFile(); // �������ļ�
//			BufferedWriter out = new BufferedWriter(new FileWriter(writename));
//			out.write(content); // \r\n��Ϊ����
//			out.flush(); // �ѻ���������ѹ���ļ�
//			out.close(); // ���ǵùر��ļ�
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//    }
	
	
	
	/*
	public static void main(String args[]) {
		
			// ����TXT�ļ� 
			String pathname = "D:\\xxq\\input.txt"; // ����·�������·�������ԣ������Ǿ���·����д���ļ�ʱ��ʾ���·��
			cin_txt b = new cin_txt();
			String a = b.readFile(pathname);
			System.out.println(a);
 
			// д��Txt�ļ� 	 	
			cin_txt c = new cin_txt();
			c.writeFile("D:\\xxq\\output.txt", "һֻСhhh");

		} 
		*/
	}


