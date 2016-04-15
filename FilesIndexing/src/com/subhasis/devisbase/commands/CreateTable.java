package com.subhasis.devisbase.commands;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.subhasis.devisbase.enums.DataType;
import com.subhasis.devisbase.utils.CommonUtils;
import com.subhasis.devisbase.utils.InformationSchemaUtility;

public class CreateTable implements iCommands{
	public void process(String command){
		if(CommonUtils.DFAULT_SCHEMA.equals(InformationSchemaUtility.INFORMATION_SCHEMA)){
			System.out.println("Cannot create table in "+InformationSchemaUtility.INFORMATION_SCHEMA+" use a different schema.");
			return;
		}
		long nano = System.nanoTime();
		if(validateCommand(command) && evaluate(command)){
			System.out.println("Table added to schema "+CommonUtils.DFAULT_SCHEMA);
			System.out.println("Executed in "+((System.nanoTime() - nano)/1E6)+"ms");
		}			
	}
	
	public boolean validateCommand(String command){
		//System.out.println(command);
		//make sure all parameters are ok and options are only pk or null
		String tableName= extractTableName(command);
		ShowTables s = new ShowTables();
		String colList =command.substring(command.indexOf("(")+1,command.length()-1).replaceAll("\\r|\\n", "");	
		String[] params = colList.split(",");
		//validate if table with same name exist
		if(s.checkTablePresent(CommonUtils.DFAULT_SCHEMA,tableName)){		
			System.out.println("Table with name "+tableName+" already exiests in schema "+CommonUtils.DFAULT_SCHEMA);			
			return false;
		}
		//validate if parameters are correct
		for(String p: params){
			p=p.toLowerCase().replace("short int", "SHORT").replace("long int", "LONG")
					.replace("primary key", "PRIMARYKEY").replace("not null", "NOTNULL");
			//System.out.println(p.replace(String.valueOf((char) 160), " ").trim());
			String[] d = p.trim().split(" ");			
			if(d.length < 2 || d.length > 4){
				System.out.println("The Syntax of one of the parameter "+p+" is not corect.Should be column_name data_type(size) [primary key|not null].");
				return false;
			}			
		}		
		//validate all distinct column names
		Set<String> dCol = new HashSet<String>();
		for(String p: params){
			String[] d = p.trim().split(" ");
			dCol.add(d[0].toLowerCase());
		}
		if(params.length != dCol.size()){
			System.out.println("All column names in create table should be distinct.");
			return false;
		}
		//validate all data types used
		for(String p: params){
			p=p.toLowerCase().replace("short int", "short").replace("long int", "long")
					.replace("primary key", "PRIMARYKEY").replace("not null", "NOTNULL");
			String[] d = p.trim().split(" ");
			if(InformationSchemaUtility.getType(d[1])== DataType.NULL){
				System.out.println("The data type used in "+p+" is not supported.");
				return false;
			}
		}
		//validate if optional parameters are correct
		for(String p: params){
			p=p.toLowerCase().replace("short int", "short").replace("long int", "long")
					.replace("primary key", "PRIMARYKEY").replace("not null", "NOTNULL");			
			String[] d = p.trim().split(" ");
			if(d.length == 3 ||d.length == 4){
				if(d[2].equals("PRIMARYKEY")||d[2].equals("NOTNULL")){
					continue;
				}else{
					System.out.println("The optional parameters used in "+p+" is not supported.");
					return false;
				}
			}
			if(d.length == 4){
				if(d[3].equals("PRIMARYKEY")||d[3].equals("NOTNULL")){
					continue;
				}else{
					System.out.println("The optional parameters used in "+p+" is not supported.");					
					return false;
				}
			}
		}
		//System.out.println("Valid Create Table");
		return true;
	}
	
	public boolean evaluate(String command){
		//System.out.println("Evaliate");
		try{
			String tableName= extractTableName(command);		
			String colList =command.substring(command.indexOf("(")+1,command.length()-1).replaceAll("\\r|\\n", "");			
			String[] params = colList.split(",");		
			return createTable(tableName,CommonUtils.DFAULT_SCHEMA,params);
		}catch(Exception e){
			System.out.println(e);
			return false;
		}		
	}
	
	private String extractTableName(String command){
		String t = command.toLowerCase().replace("create table", "");
		t=t.substring(0,t.indexOf("("));
		t=t.trim().replace(" ", "_");
		return t;
	}
	
	private boolean createTable(String tableName,String schema,String[] colStr)throws IOException{
		// create entry in IS tables and column
		//System.out.println(tableName);
		String tableFileStr = CommonUtils.DB_LOCATION+InformationSchemaUtility.INFORMATION_SCHEMA_TABLE;
		String columnFileStr = CommonUtils.DB_LOCATION+InformationSchemaUtility.INFORMATION_SCHEMA_COLUMNS;
		List<DataType> columnDataTypes = InformationSchemaUtility.getDataTypes(InformationSchemaUtility.INFORMATION_SCHEMA, InformationSchemaUtility.IS_TABLES);
		//columnDataTypes.add(DataType.VARCHAR_N); // schema_name
		//columnDataTypes.add(DataType.VARCHAR_N); // table_name
		//columnDataTypes.add(DataType.LONG); //table_rows
		
		List<Object> data = new ArrayList<Object>();
		data.add(schema);
		data.add(tableName);
		data.add((long)0);
		
		
		CommonUtils.appendRecord(tableFileStr, columnDataTypes, data);
		createTableFile(schema,tableName);
		
		//Add rows to columns	
		columnDataTypes = InformationSchemaUtility.getDataTypes(InformationSchemaUtility.INFORMATION_SCHEMA, InformationSchemaUtility.IS_COLUMNS);
		//columnDataTypes.add(DataType.VARCHAR_N); //schema_name
		//columnDataTypes.add(DataType.VARCHAR_N);// TABLE_NAME
		//columnDataTypes.add(DataType.VARCHAR_N);// COLUMN_NAME
		//columnDataTypes.add(DataType.INT);// ORDINAL_POSITION
		//columnDataTypes.add(DataType.VARCHAR_N);// COLUMN_TYPE
		//columnDataTypes.add(DataType.VARCHAR_N);// IS_NULLABLE
		//columnDataTypes.add(DataType.VARCHAR_N);// COLUMN_KEY	
		int ordinalValue=1;
		for(String c:colStr){
			data = new ArrayList<Object>();
			//column_name1 data_type(size) [primary key|not null]
			c=c.toLowerCase().replace("short int", "short").replace("long int", "long")
					.replace("primary key", "PRIMARYKEY").replace("not null", "NOTNULL");
			String columnData[] = c.trim().split(" ");
			
			data.add(schema);
			data.add(tableName);
			data.add(columnData[0]);
			data.add(ordinalValue);
			data.add(columnData[1]);
			if(columnData.length == 2){
				data.add("YES");
				data.add("");
			}else{				
				if(c.contains("PRIMARYKEY")){
					data.add("NO");
					data.add("PRI");
				}else{
					if(c.contains("NOTNULL")){
						data.add("NO");
						data.add("");
					}else{
						data.add("YES");
						data.add("");
					}					
				}
			}
			
			CommonUtils.appendRecord(columnFileStr, columnDataTypes, data);
			createTableIndexFile(schema,tableName,columnData[0]);
			//System.out.print(c.length());
			//System.out.println(c);
			ordinalValue++;
		}
		
		return true;
	}
	
	public boolean createTableFile(String schema,String tableName)throws IOException{
		createTableDirectory(schema,tableName);
		FileWriter w = new FileWriter(CommonUtils.DB_LOCATION+schema+"/"+tableName+"/"+schema+"."+tableName+".dat");
		w.close();
		return true;
	}
	
	private void createTableDirectory(String schema,String tableName)throws IOException{
		File schemaDictory = new File(CommonUtils.DB_LOCATION+schema);
		File tableDictory = new File(CommonUtils.DB_LOCATION+schema+"/"+tableName);
		if (!schemaDictory.exists()) {
			schemaDictory.mkdir();
		}
		if (!tableDictory.exists()) {
			tableDictory.mkdir();
		}		
	}
	
	public boolean createTableIndexFile(String schema,String tableName,String columnName)throws IOException{
		createTableDirectory(schema,tableName);
		FileWriter w = new FileWriter(CommonUtils.DB_LOCATION+schema+"/"+tableName+"/"+schema+"."+tableName+"."+columnName+".nxd");
		w.close();
		return true;
	}
	
	
}
