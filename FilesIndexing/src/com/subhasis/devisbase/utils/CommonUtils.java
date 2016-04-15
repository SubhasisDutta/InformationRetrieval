package com.subhasis.devisbase.utils;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.subhasis.devisbase.enums.DataType;

public class CommonUtils {
	
	public static String DFAULT_SCHEMA = InformationSchemaUtility.INFORMATION_SCHEMA;//"amusement_park";//InformationSchemaUtility.INFORMATION_SCHEMA;
	public static String DB_LOCATION = "db/";
	public static final String BLANK ="X~BLANK~X";
	public static final String NULL_STRING = "X~NULL~X";
	public static final int NULL_INT = -2147483648;
	public static final short NULL_SHORT = -32768;
	public static final long NULL_LONG = -2147483648;
	public static final byte NULL_BYTE = -128;
	public static final float NULL_FLOAT = -2147483648000.0f;
	public static final double NULL_DOUBLE = -2147483648000000.0;
	
		
	
	public static String getCurrentRecord(RandomAccessFile file,List<DataType> columnDataTypes)throws IOException,EOFException{
		StringBuilder b= new StringBuilder();
		for(DataType ct : columnDataTypes){
			String s = getData(file, ct);
			if(s.equals("")){
				s = NULL_STRING;
			}
			if(s.equals(" ")){
				s = BLANK;
			}
			b.append(s);
			b.append(",");
		}
		//System.out.println(b.toString());
		return b.substring(0, b.length()-1).toString();//.substring(0, b.length()-1)
	}
	
	public static List<String> getAllFileRecords(String fileName,List<DataType> columnDataTypes)throws IOException{		
		List<String> results = new ArrayList<String>();
		RandomAccessFile file = new RandomAccessFile(fileName, "r");
		while(true){
			try{				
				StringBuilder b= new StringBuilder();
				for(DataType ct : columnDataTypes){
					b.append(getData(file, ct));
					b.append(",");
					//System.out.println(b.toString());
				}
				if(b.length()>0){					
					results.add(b.substring(0, b.length()-1).toString());//
				}
			}catch(EOFException e){
				break;
			}						
		}
		return results;
	}
	
	public static boolean appendRecord(String fileName,List<DataType> columnDataTypes,List<Object> data){
		try{
			RandomAccessFile file = new RandomAccessFile(fileName, "rw");
			long end = file.length();
			file.seek(end);
			int index =0;
			for(DataType d : columnDataTypes){
				insertData(file,d,data.get(index));
				index++;
			}
			return true;
		}catch(Exception e){
			System.out.println(e);
			return false;
		}		
	}
	
	public static Object convertDataValue(DataType type,String data)throws Exception{
		Object obj = null;
		if(data.equals(""))
			return null;
		if(type == DataType.BYTE){
			obj=Byte.valueOf(data);
		}else if(type == DataType.SHORT){
			obj=Short.valueOf(data);
		}else if(type == DataType.INT){
			obj=Integer.valueOf(data);
		}else if(type == DataType.LONG){
			obj=Long.valueOf(data);
		}else if(type == DataType.CHAR_N){
			obj=data;
		}else if(type == DataType.VARCHAR_N){
			obj=data;
		}else if(type == DataType.FLOAT){
			obj=Float.valueOf(data);
		}else if(type == DataType.DOUBLE){
			obj=Double.valueOf(data);
		}else if(type == DataType.DATETIME){ 
			String string_date = data;
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
			Date d = f.parse(string_date);
			long milliseconds = d.getTime();
			obj=Long.valueOf(milliseconds);						
		}else if(type == DataType.DATE){
			String string_date = data;
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			Date d = f.parse(string_date);
			long milliseconds = d.getTime();
			obj=Long.valueOf(milliseconds);						
		}else {
			obj=null;
		}				
		return obj;
	}
	
	public static void insertData(RandomAccessFile file,DataType type,Object data)throws IOException{
		if(type == DataType.BYTE){
			if(data == null){
				data=(byte)NULL_BYTE;
			}
			file.write((byte)data);
		}else if(type == DataType.SHORT){
			if(data == null){
				data=(short)NULL_SHORT;
			}
			file.writeShort((short)data);
		}else if(type == DataType.INT){
			if(data == null){
				data=(int)NULL_INT;
			}
			file.writeInt((int)data);
		}else if(type == DataType.LONG){
			if(data == null){
				data=(long)-2147483648;
			}
			file.writeLong((long)data);
		}else if(type == DataType.CHAR_N){
			if(data == null){
				data="";
			}
			file.writeByte(data.toString().length());
			file.writeBytes(data.toString());
		}else if(type == DataType.VARCHAR_N){//TODO: Check how to implement varchar
			if(data == null){
				data="";
			}
			file.writeByte(data.toString().length());
			file.writeBytes(data.toString());
			//file.writeUTF(str);
		}else if(type == DataType.FLOAT){
			if(data == null){
				data=(float)NULL_FLOAT;
			}
			file.writeFloat((float)data);
		}else if(type == DataType.DOUBLE){
			if(data == null){
				data=(double)NULL_DOUBLE;
			}
			file.writeDouble((double)data);
		}else if(type == DataType.DATETIME){ //TODO: Check how to implement timestamp
			if(data == null){
				data=(long)0;
			}
			file.writeLong((long)data);
			//file.writeByte(data.toString().length());
			//file.writeBytes(data.toString());
		}else if(type == DataType.DATE){//TODO: Check how to implement date
			if(data == null){
				data=(long)0;
			}
			file.writeLong((long)data);
			//file.writeByte(data.toString().length());
			//file.writeBytes(data.toString());
		}else {
			file.write(null);
		}
	}
	
	private static String getData(RandomAccessFile file,DataType type)throws IOException,EOFException{
		if(type == DataType.BYTE){
			byte a = file.readByte();
			if (a == NULL_BYTE){
				return CommonUtils.NULL_STRING;
			}
			return String.valueOf(a);
		}else if(type == DataType.SHORT){
			short a = file.readShort();
			if (a == NULL_SHORT){
				return CommonUtils.NULL_STRING;
			}
			return String.valueOf(a);
		}else if(type == DataType.INT){
			int a = file.readInt();
			if (a == NULL_INT){
				return CommonUtils.NULL_STRING;
			}
			return String.valueOf(a);
		}else if(type == DataType.LONG){
			long a = file.readLong();
			if (a == NULL_LONG){
				return CommonUtils.NULL_STRING;
			}
			return String.valueOf(a);
		}else if(type == DataType.CHAR_N){//TODO: Check how to implement char
			byte len = file.readByte();
			StringBuilder b = new StringBuilder();
			for(int i=0;i< len;i++){
				b.append((char)file.readByte());
			}
			return b.toString();
		}else if(type == DataType.VARCHAR_N){
			byte len = file.readByte();			
			StringBuilder b = new StringBuilder();
			for(int i=0;i< len;i++){				
				b.append((char)file.readByte());
			}
			return b.toString();
		}else if(type == DataType.FLOAT){
			float a = file.readFloat();
			if (a == NULL_FLOAT){
				return CommonUtils.NULL_STRING;
			}
			return String.valueOf(a);
		}else if(type == DataType.DOUBLE){
			double a = file.readDouble();
			if (a == NULL_DOUBLE){
				return CommonUtils.NULL_STRING;
			}
			return String.valueOf(a);
		}else if(type == DataType.DATETIME){ //TODO: Check how to implement timestamp
			long a = file.readLong();
			if (a == 0){
				return CommonUtils.NULL_STRING;
			}
			String dateTime = convertTime(a);
			return dateTime;
			//byte len = file.readByte();
			//StringBuilder b = new StringBuilder();
			//for(int i=0;i< len;i++){
			//	b.append((char)file.readByte());
			//}
			//return b.toString();
		}else if(type == DataType.DATE){//TODO: Check how to implement date
			long a = file.readLong();
			if (a == 0){
				return CommonUtils.NULL_STRING;
			}
			String[] dateTime = convertTime(a).split("_");
			return dateTime[0];
			//byte len = file.readByte();
			//StringBuilder b = new StringBuilder();
			//for(int i=0;i< len;i++){
			//	b.append((char)file.readByte());
			//}
			//return b.toString();
		}else {
			return CommonUtils.NULL_STRING;
		}
	}
	private static String convertTime(long time){
	    Date date = new Date(time);
	    Format format = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
	    return format.format(date);
	}
	
	
	public static void deleteFile(File element) {		
	    if (element.isDirectory()) {
	        for (File sub : element.listFiles()) {
	            deleteFile(sub);
	        }
	    }	    
	    if(element.delete()){
	    	element.deleteOnExit();
	    }
	}
	
	
	
}
