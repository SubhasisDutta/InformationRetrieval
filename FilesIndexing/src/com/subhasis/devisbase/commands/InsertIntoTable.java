package com.subhasis.devisbase.commands;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.subhasis.devisbase.models.DataTypeObj;
import com.subhasis.devisbase.utils.CommonUtils;
import com.subhasis.devisbase.utils.IndexUtility;
import com.subhasis.devisbase.utils.InformationSchemaUtility;

public class InsertIntoTable implements iCommands{
	public void process(String command){
		if(CommonUtils.DFAULT_SCHEMA.equals(InformationSchemaUtility.INFORMATION_SCHEMA)){
			System.out.println("Cannot insert record in "+InformationSchemaUtility.INFORMATION_SCHEMA+" use a different schema.");
			return;
		}
		long nano = System.nanoTime();		
		if(validateCommand(command)&& evaluate(command))
			System.out.println("1 row inserted in "+((System.nanoTime() - nano)/1E6)+"ms");
	}
	
	public boolean validateCommand(String command){
		try{
			//check if command containss into and values
			String cm = command.toLowerCase();
			if(!cm.contains(" into ")||!cm.contains(" values ")){
				System.out.println("Invalid insert syntax");
				System.out.println("Please enter in format: INSERT INTO <table_name> VALUES (<comma seperated values>);");
				return false;
			}
			//check if table present in selected schema			
			String tableName = cm.substring(cm.indexOf(" into table")+11, cm.indexOf(" values ")).trim();
			ShowTables s = new ShowTables();
			if(!s.checkTablePresent(CommonUtils.DFAULT_SCHEMA,tableName)){
				System.out.println("Table with name "+tableName+" not found in schema "+CommonUtils.DFAULT_SCHEMA+". Unable to complete insert operation.");
				return false;
			}
			//need to validate the data columns - currently assuming they are valid data		
			String values = command.substring(command.indexOf("(")+1, command.indexOf(")"));			
			String[] recordValues = values.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
			for (int i = 0; i < recordValues.length; i++) {
				recordValues[i] = recordValues[i].trim();
				if(recordValues[i].startsWith("\"")&& recordValues[i].endsWith("\""))
					recordValues[i] = recordValues[i].substring(1, recordValues[i].length()-1);
				if(recordValues[i].startsWith("\'")&& recordValues[i].endsWith("\'"))
					recordValues[i] = recordValues[i].substring(1, recordValues[i].length()-1);
				//System.out.println(recordValues[i]);
			}
			List<DataTypeObj> tableCols = InformationSchemaUtility.getDataTypeObjects(CommonUtils.DFAULT_SCHEMA, tableName);
			if(recordValues.length != tableCols.size()){
				System.out.println("Invalid Syntax.Number of data poins for "+tableName+" dont match up.");
				return false;
			}			
					
			for(int i=0;i<tableCols.size();i++){
				//need to match the data types
				Object obj;
				try{
					//System.out.println(recordValues[i]);
					obj = CommonUtils.convertDataValue(tableCols.get(i).getType(), recordValues[i]);
				}catch(ParseException e){
					obj=null;
					System.out.println("Date Time format is not correct");
					//e.printStackTrace();
					return false;
				}
				catch(Exception e){
					obj=null;
					System.out.println("Data Type conversion ERROR.Please check the data format in the insert statement.");
					//e.printStackTrace();
					return false;
				}
				/*if(obj==null){
					System.out.println("Insert has data that is not yet supported.");
					return false;
				}*/			
				//System.out.println(tableCols.get(i).getColumnName());
				//System.out.println(tableCols.get(i).isPK());
				//need to check no duplicates in primary key inserts
				if(tableCols.get(i).isPK()){
					IndexUtility iO = new IndexUtility();
					if(!iO.checkUnique(CommonUtils.DFAULT_SCHEMA,tableName,tableCols.get(i).getColumnName(),obj)){
						System.out.println("Cannot INSERT. Duplicate value or NULL for column "+tableCols.get(i).getColumnName());
						return false;
					}
				}
				//need to check not null are not empty
				if(!tableCols.get(i).isNullable()){
					int len = recordValues[i].length();
					if(len == 0){
						System.out.println("Cannot INSERT. Value for column "+tableCols.get(i).getColumnName()+" cannot be NULL.");
						return false;
					}
				}
			}
			return true;
		}catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean evaluate(String command){
		try{
			String cm = command.toLowerCase();
			String tableName = cm.substring(cm.indexOf(" into table")+11, cm.indexOf(" values ")).trim();
			String values = command.substring(command.indexOf("(")+1, command.indexOf(")"));			
			String[] recordValues = values.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
			//System.out.println(tableName);
			for (int i = 0; i < recordValues.length; i++) {
				recordValues[i] = recordValues[i].trim();
				//System.out.println(recordValues[i]);
				if(recordValues[i].startsWith("\"")&& recordValues[i].endsWith("\""))
					recordValues[i] = recordValues[i].substring(1, recordValues[i].length()-1);
				if(recordValues[i].startsWith("\'")&& recordValues[i].endsWith("\'"))
					recordValues[i] = recordValues[i].substring(1, recordValues[i].length()-1);
			}
			//append record to corresponding data table & modify the index for all the columns of the table 
			List<DataTypeObj> tableCols = InformationSchemaUtility.getDataTypeObjects(CommonUtils.DFAULT_SCHEMA, tableName);
			//assumes the value and datatype match
			String dataTableFileStr = CommonUtils.DB_LOCATION+CommonUtils.DFAULT_SCHEMA+"/"+tableName+"/"+CommonUtils.DFAULT_SCHEMA+"."+tableName+".dat";
			RandomAccessFile dataTableFile = new RandomAccessFile(dataTableFileStr, "rw");
			long end = dataTableFile.length();
			dataTableFile.seek(end);
			long newRecordPointer = dataTableFile.getFilePointer();
			
			int index =0;			
			for(DataTypeObj dObj: tableCols){
				String val=recordValues[index];
				insertDataValue(CommonUtils.DFAULT_SCHEMA,dataTableFile,tableName,dObj,val,newRecordPointer);
				index++;				
			}			
			//increment the row count in IS_Table
			InformationSchemaUtility.incrementTableRowCount(CommonUtils.DFAULT_SCHEMA,tableName);		
			dataTableFile.close();
			return true;
		}catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean insertDataValue(String schema,RandomAccessFile dataTableFile,String tableName,
			DataTypeObj columnMetaData,String value,long newRecordPointer)throws IOException,ClassNotFoundException,Exception{
		
		Object valueObj=CommonUtils.convertDataValue(columnMetaData.getType(), value);
		if(valueObj == null){
			//insert null to data file
			CommonUtils.insertData(dataTableFile, columnMetaData.getType(), valueObj);
			return true;
		}
		//insert the value in its index file
		String indexTableFileStr = CommonUtils.DB_LOCATION+schema+"/"+tableName+"/"+schema+"."+tableName+"."+columnMetaData.getColumnName()+".nxd";
		
		IndexUtility indexUObj = new IndexUtility();
		Map<Object,List<Long>> indexMap= indexUObj.getIndexMap(indexTableFileStr);
		List<Long> poinerList = indexMap.get(valueObj);
		if(poinerList == null){
			poinerList = new ArrayList<Long>();
		}
		poinerList.add(newRecordPointer);
		indexMap.put(valueObj, poinerList);
		indexUObj.setIndexFile(indexTableFileStr, columnMetaData.getType(), indexMap);
					
		//insert it in the data file
		CommonUtils.insertData(dataTableFile, columnMetaData.getType(), valueObj);
		return true;
	}
	
	
	
	

}
