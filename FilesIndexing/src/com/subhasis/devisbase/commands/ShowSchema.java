package com.subhasis.devisbase.commands;

import java.util.ArrayList;
import java.util.List;

import com.subhasis.devisbase.enums.DataType;
import com.subhasis.devisbase.utils.CommonUtils;
import com.subhasis.devisbase.utils.DisplayUtility;
import com.subhasis.devisbase.utils.InformationSchemaUtility;


public class ShowSchema implements iCommands {
	
	public void process(String command){
		long nano = System.nanoTime();
		if(validateCommand(command) && evaluate(command))
			System.out.println("Executed in "+((System.nanoTime() - nano)/1E6)+"ms");
	}
	
	public boolean validateCommand(String command){
		String s = command.toLowerCase().replace("show schemas", "");
		if(s.equals("")){
			return true;
		}
		System.out.println("Syntax of SHOW SCHEMA is not valid");
		return false;
	}
	
	public boolean evaluate(String command){		
		List<String> records = getSchemas();
		if(records.size() < 1){
			System.out.println("No Schema Prsent.");
			return false;
		}			
		List<String> headers = new ArrayList<String>();
		headers.add("Database");			
		DisplayUtility.displayTable(headers, records);
		return true;
	}
	public List<String> getSchemas(){		
		try{
			String schemaFileStr = CommonUtils.DB_LOCATION+InformationSchemaUtility.INFORMATION_SCHEMA_SCHEMATA;
			List<DataType> columnDataTypes = InformationSchemaUtility.getDataTypes(InformationSchemaUtility.INFORMATION_SCHEMA, InformationSchemaUtility.IS_SCHEMATA);
			//System.out.println(columnDataTypes);
			//columnDataTypes.add(DataType.VARCHAR_N);
			return CommonUtils.getAllFileRecords(schemaFileStr, columnDataTypes);
		}catch(Exception e) {			
			System.out.println(e);
			//e.printStackTrace();
		}
		return new ArrayList<String>();
	}
}
