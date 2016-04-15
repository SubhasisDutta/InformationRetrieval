package com.subhasis.devisbase.utils;

import com.subhasis.devisbase.enums.CommandType;

public class CommandUtility {
	public static CommandType getCommandType(String command){
		command=command.toLowerCase();
		if(command.startsWith("quit")||command.startsWith("exit")){
			return CommandType.EXIT;
		}else if(command.startsWith("select")){
			return CommandType.SELECT_FROM_WHERE;
		}else if(command.startsWith("insert")){
			return CommandType.INSERT_INTO_TABLE;
		}else if(command.startsWith("delete")){
			return CommandType.DELETE_FROM;
		}else if(command.startsWith("help")){
			return CommandType.HELP;
		}else if(command.startsWith("drop table")){
			return CommandType.DROP_TABLE;
		}else if(command.startsWith("use")){
			return CommandType.USE_SCHEMA;
		}else if(command.startsWith("show schemas")){
			return CommandType.SHOW_SCHEMA;
		}else if(command.startsWith("show tables")){
			return CommandType.SHOW_TABLES;
		}else if(command.startsWith("create schema")){
			return CommandType.CREATE_SCHEMA;
		}else if(command.startsWith("create table")){
			return CommandType.CREATE_TABLE;
		}else if(command.startsWith("version")){
			return CommandType.VERSION;
		}		
		return CommandType.UNKNOWN;
	}	
	
}
