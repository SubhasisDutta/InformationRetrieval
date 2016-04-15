package com.subhasis.devisbase.utils;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.subhasis.devisbase.enums.DataType;

public class IndexUtility {
	@SuppressWarnings("unchecked")
	public TreeMap<Object,List<Long>> getIndexMap(String indexFileName)throws IOException,ClassNotFoundException{		
		//RandomAccessFile indexFile = new RandomAccessFile(indexFileName, "r");
		TreeMap<Object,List<Long>> index;
		//System.out.println(indexFileName);
		ObjectInputStream indexFile = null;
		try{
			indexFile = new ObjectInputStream(new FileInputStream(indexFileName));
			index = (TreeMap<Object,List<Long>>)indexFile.readObject();
			//System.out.println(index);
			indexFile.close();
		}catch(EOFException e){
			//System.out.println("Creating New Index Object");
			index = new TreeMap<Object, List<Long>>();
		}		
		return index;
	}
	
	public boolean setIndexFile(String indexFileName,DataType keyDataType,Map<Object,List<Long>> indexMap)throws IOException{		
		ObjectOutputStream indexFile = new ObjectOutputStream(new FileOutputStream(indexFileName));
		indexFile.writeObject(indexMap);
		indexFile.close();
		return true;
	}
	
	public boolean checkUnique(String schema,String tableName,String columnName,Object find)throws ClassNotFoundException,IOException{
		if(find == null){
			System.out.println(columnName+ " is a PRIMARY KEY. It cannot be null.");
			return false;
		}
		String indexTableFileStr = CommonUtils.DB_LOCATION+schema+"/"+tableName+"/"+schema+"."+tableName+"."+columnName+".nxd";
		IndexUtility indexUObj = new IndexUtility();
		TreeMap<Object,List<Long>> indexMap= indexUObj.getIndexMap(indexTableFileStr);
		List<Long> v = indexMap.get(find);
		if(v == null || v.size() == 0){
			return true;
		}else{
			return false;
		}		
	}
}
