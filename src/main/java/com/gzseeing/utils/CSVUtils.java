package com.gzseeing.utils;

import java.io.Closeable;
import java.nio.charset.Charset;

import com.csvreader.CsvWriter;

public class CSVUtils implements Closeable{

	

	public final static CSVUtils getInstance(String filePath){
		 return new CSVUtils(filePath,null);
	}
	public final static CSVUtils getInstance(String filePath,String charset){
		 return new CSVUtils(filePath,charset);
	}
	//******************constructor***************************/

	private String filePath;
	private String charset;
	private CsvWriter csvWriter;
	private long count=0;
	private CSVUtils(){
		
	}
	private CSVUtils(String filePath,String charset){
		this.filePath=filePath;
		this.charset=charset;
		if (this.charset==null){
			this.charset="utf-8";
		}
		Init();
 	}
 
	private void Init() {
		csvWriter = new CsvWriter(filePath,',', Charset.forName(charset));		
	}
	//*********************************************/
	public void writeHeader(String... headers){
		try {
			csvWriter.writeRecord(headers);
		} catch (Exception e) {
			e.printStackTrace();
 		}
	}
	public void writeRecord(String... row){
		try {
			csvWriter.writeRecord(row);
			if (count%200L==0L){
				flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
 		}
	}
	public void flush(){
		csvWriter.flush();
	}
	public void close(){
		csvWriter.close();
	}
}
