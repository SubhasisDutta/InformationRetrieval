package com.subhasis.devisbase.commands;

import java.io.File;

import com.subhasis.devisbase.utils.CommonUtils;
import com.subhasis.devisbase.utils.InformationSchemaUtility;

public class DropTable implements iCommands{
	public void process(String command){
		if(CommonUtils.DFAULT_SCHEMA.equals(InformationSchemaUtility.INFORMATION_SCHEMA)){
			System.out.println("Cannot drop table in "+InformationSchemaUtility.INFORMATION_SCHEMA+" use a different schema.");
			return;
		}
		long nano = System.nanoTime();
		if(validateCommand(command) && evaluate(command)){
			System.out.println("Table dropped from schema "+CommonUtils.DFAULT_SCHEMA);
			System.out.println("Executed in "+((System.nanoTime() - nano)/1E6)+"ms");
		}
	}
	
	public boolean validateCommand(String command){
		//check if table exist in schema
		String tableName = extractTableName(command);
		ShowTables s = new ShowTables();
		if(!s.checkTablePresent(CommonUtils.DFAULT_SCHEMA,tableName)){		
			System.out.println("Table with name "+tableName+" does not exiests in schema "+CommonUtils.DFAULT_SCHEMA);			
			return false;
		}
		return true;
	}
	
	private String extractTableName(String command){		
		return command.toLowerCase().replace("drop table", "").trim();
	}
	
	public boolean evaluate(String command){
		try{
			String tableName = extractTableName(command);
			String location = CommonUtils.DB_LOCATION+CommonUtils.DFAULT_SCHEMA+"/"+tableName;
			//delete the files and directory for the table
			CommonUtils.deleteFile(new File(location));
			//remove the table record from IStable and IS column
			InformationSchemaUtility.removeTableRecord(CommonUtils.DFAULT_SCHEMA,tableName);
			InformationSchemaUtility.removeColumnRecords(CommonUtils.DFAULT_SCHEMA,tableName);
			return true;
		}catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
			return false;
		}
		
	}
}
