package com.subhasis.devisbase.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.subhasis.devisbase.enums.DataType;
import com.subhasis.devisbase.utils.CommonUtils;
import com.subhasis.devisbase.utils.DisplayUtility;
import com.subhasis.devisbase.utils.InformationSchemaUtility;

public class ShowTables implements iCommands {
	
	public static String tableFileStr = CommonUtils.DB_LOCATION+InformationSchemaUtility.INFORMATION_SCHEMA_TABLE;
	public void process(String command){
		long nano = System.nanoTime();
		if(validateCommand(command) && evaluate(command))
			System.out.println("Executed in "+((System.nanoTime() - nano)/1E6)+"ms");
	}	
	
	public boolean validateCommand(String command){
		return true;
	}
	
	public boolean evaluate(String command){
		try{
			List<String> filterRecords = getSchemaTables(CommonUtils.DFAULT_SCHEMA);
			if(filterRecords.size()<1){
				System.out.println("Empty Set");
			}else{
				List<String> headers = new ArrayList<String>();
				headers.add("Tables_in_"+CommonUtils.DFAULT_SCHEMA);			
				DisplayUtility.displayTable(headers, filterRecords);
			}			
		}catch(Exception e) {			
			System.out.println(e);
			return false;
		}
		return true;
	}
	
	public List<String> getSchemaTables(String schema)throws IOException{
		List<DataType> columnDataTypes = InformationSchemaUtility.getDataTypes(InformationSchemaUtility.INFORMATION_SCHEMA, InformationSchemaUtility.IS_TABLES);
		//columnDataTypes.add(DataType.VARCHAR_N);
		//columnDataTypes.add(DataType.VARCHAR_N);
		//columnDataTypes.add(DataType.LONG);
		List<String> records = CommonUtils.getAllFileRecords(tableFileStr, columnDataTypes);
		//System.out.println(schema);
		//System.out.println(records);
		return filterTableName(schema,records);
	}
	
	private List<String> filterTableName(String schema,List<String> records){
		//System.out.println(records);
		List<String> tables = new ArrayList<String>();
		for(String r: records){
			String[] s = r.split(",");
			if(s[0].toLowerCase().equals(schema)){
				tables.add(s[1]);
			}
		}
		return tables;
	}
	
	public boolean checkTablePresent(String schema,String table){
		String tableFileStr = CommonUtils.DB_LOCATION+InformationSchemaUtility.INFORMATION_SCHEMA_TABLE;
		
		try{		
			List<DataType> columnDataTypes = InformationSchemaUtility.getDataTypes(InformationSchemaUtility.INFORMATION_SCHEMA, InformationSchemaUtility.IS_TABLES);
			//columnDataTypes.add(DataType.VARCHAR_N);
			//columnDataTypes.add(DataType.VARCHAR_N);
			//columnDataTypes.add(DataType.LONG);
			List<String> records = CommonUtils.getAllFileRecords(tableFileStr, columnDataTypes);
			//System.out.println(table);
			//System.out.println(records);
			List<String> filterRecords = filterTableName(CommonUtils.DFAULT_SCHEMA,records);
			
			//System.out.println(filterRecords);
			if(filterRecords.size()<1){
				return false;
			}else{
				if(filterRecords.contains(table)||filterRecords.contains(table.toUpperCase())){
					return true;
				}
			}
		}catch(Exception e) {			
			System.out.println(e);
			return false;
		}
		return false;
	}
	
}
