package com.ms.test;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
public class Main {
	public static void main(String[] args) throws IOException{
		test();
		 /*Properties props = new Properties();
	        InputStream is = Main.class.getResourceAsStream("privateKey");
	        
	        props.load(is);
	        
	        System.out.println(props.getProperty("url"));
	        System.out.println(props.getProperty("name"));
	        System.out.println(props.getProperty("password"));*/
	}
	
	public static void test(){
		 InputStream is = Main.class.getResourceAsStream("privateKey");
		 System.out.println(is);
	}
}
