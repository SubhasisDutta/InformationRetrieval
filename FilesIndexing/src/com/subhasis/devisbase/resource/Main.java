package com.subhasis.devisbase.resource;
import java.io.File;
import java.util.Scanner;

import com.subhasis.devisbase.commands.CreateSchema;
import com.subhasis.devisbase.commands.CreateTable;
import com.subhasis.devisbase.commands.DeleateFromTable;
import com.subhasis.devisbase.commands.DropTable;
import com.subhasis.devisbase.commands.ImportInsertCSV;
import com.subhasis.devisbase.commands.InsertIntoTable;
import com.subhasis.devisbase.commands.SelectFromTable;
import com.subhasis.devisbase.commands.ShowSchema;
import com.subhasis.devisbase.commands.ShowTables;
import com.subhasis.devisbase.commands.UseSchema;
import com.subhasis.devisbase.commands.iCommands;
import com.subhasis.devisbase.enums.CommandType;
import com.subhasis.devisbase.utils.CommandUtility;
import com.subhasis.devisbase.utils.CommonUtils;
import com.subhasis.devisbase.utils.DisplayUtility;
import com.subhasis.devisbase.utils.MakeInformationSchema;


public class Main {
		
	public static void main(String[] args){
		if(args.length < 1 ){
			CommonUtils.DB_LOCATION = "db/";			
		} else {
			CommonUtils.DB_LOCATION = args[0];			
		}
		//Give welcome Message
		DisplayUtility.splashScreen();
		DisplayUtility.help();
		// check if initial information schema present, if not present create it
		if(!checkISPresent()){
			MakeInformationSchema.initilize();			
		}
		
		Scanner scanner = new Scanner(System.in).useDelimiter(";");
		String userCommand; // Variable to collect user input from the prompt
		do {  // do-while !exit
			System.out.print(DisplayUtility.prompt);
			userCommand = scanner.next().trim();
			CommandType commandType = CommandUtility.getCommandType(userCommand);
			iCommands e;
			switch (commandType) {
				case VERSION:
					DisplayUtility.version();
					break;
				case HELP:
					DisplayUtility.help();
					break;
				case SHOW_SCHEMA:
					e = new ShowSchema();
					e.process(userCommand);
					break;
				case USE_SCHEMA:
					//use schema
					e = new UseSchema();
					e.process(userCommand);
					break;
				case SHOW_TABLES:
					e = new ShowTables();
					e.process(userCommand);
					break;	
				case CREATE_SCHEMA:
					//create schema
					e = new CreateSchema();
					e.process(userCommand);
					break;
				case CREATE_TABLE:
					// create table
					e = new CreateTable();
					e.process(userCommand);
					break;
				case INSERT_INTO_TABLE:
					//insert into table
					e = new InsertIntoTable();
					e.process(userCommand);
					break;
				case DELETE_FROM:
					//TODO:delete from
					e = new DeleateFromTable();
					e.process(userCommand);
					break;
				case DROP_TABLE:
					//drop table
					e= new DropTable();
					e.process(userCommand);
					break;
				case SELECT_FROM_WHERE:
					//select
					e=new SelectFromTable();
					e.process(userCommand);
					break;
				case IMPORT:
					//TODO:import csv file and insert
					e= new ImportInsertCSV();
					e.process(userCommand);
					break;
				case EXIT:
					//exit
					userCommand="exit";
					break;
				default:
					System.out.println("I didn't understand the command: \"" + userCommand + "\"");
			}
			
		}while(!userCommand.equals("exit"));
		scanner.close();
		System.out.println("Exiting...");
	}
	
	public static boolean checkISPresent(){
		String[] initialFiles = new String[3];
		initialFiles[0] = "information_schema.schemata.tbl";
		initialFiles[1] = "information_schema.table.tbl";
		initialFiles[2] = "information_schema.columns.tbl";
		for (String file : initialFiles){
			File f = new File(CommonUtils.DB_LOCATION+file);
			if(!f.exists() || f.isDirectory()) { 
			    return false;
			}
		}
		return true;		
	}
}
