package com.subhasis.devisbase.commands;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import com.subhasis.devisbase.enums.DataType;
import com.subhasis.devisbase.utils.CommonUtils;


public class ImportInsertCSV implements iCommands{
	public void process(String command){
		System.out.println("Functionality not yet present. Will be added in next Version.");
	}
	
	public boolean validateCommand(String command){
		return true;
	}
	
	public boolean evaluate(String command){
		return true;
	}
	
	//DUMP
	public long findOffsetPoint(RandomAccessFile file,DataType type,Object newData)throws IOException {
		long offset=findIndexPositionLinear(file,type,newData);	
		
		return offset;		
	}
	
	public long findIndexPositionLinear(RandomAccessFile file,DataType type,Object search)throws IOException{
		//int indexFileLocation = 0;
		//long indexOfRecord = 0;
		boolean recordFound = false;
		/*
		 *  Use exhaustive brute force seach over the binary index file to locate
		 *  the requested ID values. Then use its assoicated address to seek the 
		 *  record in the widget table binary data file.
		 *
		 *  You may instead want to load the binary index file into a HashMap
		 *  or similar key:value data structure for efficient index-address lookup,
		 *  but this is not required.
		 */
		while(!recordFound) {
			//tableIdIndex.seek(indexFileLocation);
			//if(newDataGreater) {
			//	
			//}
		}
		return (long)1;
		
	}
	
	public void insertIndexData(String filename, long offset, DataType type,Object data,long newRecordPointer)throws IOException {
		  RandomAccessFile r = new RandomAccessFile(new File(filename), "rw");
		  RandomAccessFile rtemp = new RandomAccessFile(new File(filename+"~"), "rw");
		  long fileSize = r.length();
		  FileChannel sourceChannel = r.getChannel();
		  FileChannel targetChannel = rtemp.getChannel();
		  sourceChannel.transferTo(offset, (fileSize - offset), targetChannel);
		  sourceChannel.truncate(offset);
		  r.seek(offset);
		  //r.write(content);		  
		  //insert data and long pointer
		  CommonUtils.insertData(r, type, data);
		  CommonUtils.insertData(r, DataType.LONG, newRecordPointer);
		  
		  long newOffset = r.getFilePointer();
		  targetChannel.position(0L);
		  sourceChannel.transferFrom(targetChannel, newOffset, (fileSize - offset));
		  sourceChannel.close();
		  targetChannel.close();
		  r.close();
		  rtemp.close();
		  File f= new File(filename+"~");
		  f.delete();
		}
}
