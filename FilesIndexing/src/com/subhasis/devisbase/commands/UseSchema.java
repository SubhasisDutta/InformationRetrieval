package com.subhasis.devisbase.commands;

import com.subhasis.devisbase.utils.CommonUtils;

public class UseSchema implements iCommands {
	public void process(String command){
		long nano = System.nanoTime();
		if(validateCommand(command) && evaluate(command))
			System.out.println("Executed in "+((System.nanoTime() - nano)/1E6)+"ms");
	}
	
	public boolean validateCommand(String command){
		return true;
	}
	public boolean evaluate(String command){
		String schemaName= extractSchemaName(command);
		//System.out.println(schemaName);
		if(vaildSchema(schemaName)){
			CommonUtils.DFAULT_SCHEMA = schemaName;
			System.out.println("Database changed to "+schemaName+" sucessfully.");
			return true;
		}
		return false;
	}
	
	private String extractSchemaName(String command){
		String t = command.toLowerCase().replaceFirst("use", "");
		t=t.trim().replace(" ", "_");
		return t;
	}
	private boolean vaildSchema(String schemaName){
		ShowSchema s = new ShowSchema();	
		//System.out.println(s.getSchemas());
		if(!s.getSchemas().contains(schemaName)){
			System.out.println("Schema with name "+schemaName+" does not exist. Enter a valid schema.");
			return false;
		}
		return true;
	}
}
