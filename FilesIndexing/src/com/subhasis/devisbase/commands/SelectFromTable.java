package com.subhasis.devisbase.commands;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import com.subhasis.devisbase.enums.DataType;
import com.subhasis.devisbase.models.DataTypeObj;
import com.subhasis.devisbase.utils.CommonUtils;
import com.subhasis.devisbase.utils.DisplayUtility;
import com.subhasis.devisbase.utils.InformationSchemaUtility;


public class SelectFromTable implements iCommands{
	public void process(String command){		
		long nano = System.nanoTime();
		if(validateCommand(command) && evaluate(command)){			
			System.out.println("Executed in "+((System.nanoTime() - nano)/1E6)+"ms");
		}
	}
	
	public boolean validateCommand(String command){
		try{
			if(!command.toLowerCase().startsWith("select * from")){
				System.out.println("Currently ony SELECT * FROM <tableName> [WHERE <condition>] is supported.");
				return false;
			}
			//check syntax 
			if(!command.toLowerCase().contains("* from ")){
				System.out.println("Incorrect select syntax.");
				System.out.println("Please enter in format: SELECT * FROM <tableName> [WHERE <condition>];");
				return false;
			}
			String tableName = extractTableName(command);
			//validate the table exist
			ShowTables s = new ShowTables();
			if(!s.checkTablePresent(CommonUtils.DFAULT_SCHEMA,tableName)){		
				System.out.println("Table with name "+tableName+" does not exiests in schema "+CommonUtils.DFAULT_SCHEMA);			
				return false;
			}
			//validate the where clause
			String clause = extractWhereClause(command);
			if(clause!=null){
				WhereClause c = new WhereClause(tableName,clause);			
				if(!c.validateClause())return false;
			}
			return true;			
		}catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
			return false;
		}		
	}
	
	private String extractTableName(String command){	
		//System.out.println(command);
		String afterFrom = command.toLowerCase().substring(command.toLowerCase().indexOf(" from ")+6, command.length()).trim();
		//System.out.println(afterFrom);
		String tableName;
		if(afterFrom.contains(" where ")){			
			tableName = afterFrom.substring(0, afterFrom.indexOf(" where ")).trim();
		}else{
			tableName = afterFrom;
		}
		//System.out.println(tableName);
		return tableName;		
	}
	private String extractWhereClause(String command){
		String afterFrom = command.substring(command.toLowerCase().indexOf(" from ")+6, command.length()).trim();
		String clause = null;
		if(afterFrom.toLowerCase().contains(" where ")){			
			clause = afterFrom.substring(afterFrom.toLowerCase().indexOf(" where ")+7);
		}
		return clause;
	}
	
	
	public boolean evaluate(String command){
		try{
			String tableName = extractTableName(command);
			String clause = extractWhereClause(command);
			
			List<DataType> columnDataTypes = InformationSchemaUtility.getDataTypes(CommonUtils.DFAULT_SCHEMA, tableName);
			String dataFileName = CommonUtils.DB_LOCATION+CommonUtils.DFAULT_SCHEMA+"/"+tableName+"/"+CommonUtils.DFAULT_SCHEMA+"."+tableName+".dat";
			if(CommonUtils.DFAULT_SCHEMA.equals(InformationSchemaUtility.INFORMATION_SCHEMA))
				dataFileName=CommonUtils.DB_LOCATION+CommonUtils.DFAULT_SCHEMA+"."+tableName+".tbl";
			//System.out.println(dataFileName);
			RandomAccessFile file = new RandomAccessFile(dataFileName, "r");
			List<String> records;
			if(clause!=null){
				if(CommonUtils.DFAULT_SCHEMA.equals(InformationSchemaUtility.INFORMATION_SCHEMA)){
					System.out.println("Currently WHERE clause is not supported in INFORMATION_SCHEMA");
					file.close();
					return false;
				}
				// need to do selective extraction and display
				records = new ArrayList<String>();
				WhereClause c = new WhereClause(tableName,clause);
				List<Long> pointers = c.getRecordPointers(CommonUtils.DFAULT_SCHEMA,tableName);
				if(pointers == null || pointers.size() == 0){
					System.out.println("No Recod matches the WHERE clause or the TABLE is empty.");
					file.close();
					return true;
				}
				for(Long pointer: pointers){
					file.seek(pointer);
					records.add(CommonUtils.getCurrentRecord(file, columnDataTypes));
				}				
			}else{
				//need to get the entire table and display		
				//System.out.println(columnDataTypes);
				records = CommonUtils.getAllFileRecords(dataFileName, columnDataTypes);				
			}
			System.out.println("Tables_in "+CommonUtils.DFAULT_SCHEMA+" "+tableName);			
			List<String> headers = new ArrayList<String>();
			List<DataTypeObj> columnsM = InformationSchemaUtility.getDataTypeObjects(CommonUtils.DFAULT_SCHEMA, tableName);
			for(DataTypeObj o: columnsM){
				headers.add(o.getColumnName());
			}
			DisplayUtility.displayTable(headers, records);
			return true;
		}catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
			return false;
		}
	}
	
	
}
