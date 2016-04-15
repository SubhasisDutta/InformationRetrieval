package com.subhasis.devisbase.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.subhasis.devisbase.enums.DataType;
import com.subhasis.devisbase.utils.CommonUtils;
import com.subhasis.devisbase.utils.InformationSchemaUtility;


public class CreateSchema implements iCommands{
	
	public void process(String command){
		long nano = System.nanoTime();
		if(evaluate(command))
			System.out.println("Executed in "+((System.nanoTime() - nano)/1E6)+"ms");
	}
	
	public boolean validateCommand(String  command){
		String schemaName= extractSchemaName(command);		
		return vaildSchema(schemaName);
	}
	
	public boolean evaluate(String command){			
		try{
			if(validateCommand(command)){
				String schemaName= extractSchemaName(command);
				enterSchemaName(schemaName);
				System.out.println("Created Schema with name "+schemaName+" sucessfully.");
				return true;
			}
		}catch(Exception e) {		
			System.out.println(e);			
		}
		return false;
		
	}
	
	private String extractSchemaName(String command){
		String t = command.toLowerCase().replace("create schema", "");
		t=t.trim().replace(" ", "_");
		return t;
	}
	
	private boolean vaildSchema(String schemaName){
		ShowSchema s = new ShowSchema();
		if(s.getSchemas().contains(schemaName)){
			System.out.println("Schema with name "+schemaName+" already present.Create Schema with different name.");
			return false;
		}
		return true;
	}
	
	private void enterSchemaName(String schemaName)throws IOException{
		String schemaFileStr = CommonUtils.DB_LOCATION+InformationSchemaUtility.INFORMATION_SCHEMA_SCHEMATA;
		List<DataType> columnDataTypes = InformationSchemaUtility.getDataTypes(InformationSchemaUtility.INFORMATION_SCHEMA, InformationSchemaUtility.IS_SCHEMATA);
		//columnDataTypes.add(DataType.VARCHAR_N);
		List<Object> data = new ArrayList<Object>();
		data.add(schemaName);
		CommonUtils.appendRecord(schemaFileStr, columnDataTypes, data);
	}
}
