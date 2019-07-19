package com.gzseeing;

 
 
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootMain {
    public static void main(String[] args) {
    	//SpringApplication.run(SpringBootMain.class, args);
    	SpringApplication app=new SpringApplication(SpringBootMain.class);
    	app.setBannerMode(Banner.Mode.OFF );
    	app.run(args);
    }
}