package com.subhasis.devisbase.utils;
import java.util.List;



public class DisplayUtility {
	public static final String prompt = "davisql> ";
	
	public static void version() {
		System.out.println("DavisBaseLite v1.0\n");
	}
	
	/**
	 *  Display the welcome "splash screen"
	 */
	public static void splashScreen() {
		System.out.println(line("*",80));
        System.out.println("Welcome to DavisBaseLite"); // Display the string.
		version();
		System.out.println("Type \"help;\" to display supported commands.");
		System.out.println(line("*",80));
	}
	
	/**
	 * @param s The String to be repeated
	 * @param num The number of time to repeat String s.
	 * @return String A String object, which is the String s appended to itself num times.
	 */
	public static String line(String s,int num) {
		String a = "";
		for(int i=0;i<num;i++) {
			a += s;
		}
		return a;
	}
	
	/**
	 * @param num The number of newlines to be displayed to <b>stdout</b>
	 */
	public static void newline(int num) {
		for(int i=0;i<num;i++) {
			System.out.println();
		}
	}
	
	/**
	 *  Help: Display supported commands
	 */
	public static void help() {
		System.out.println(line("*",80));
		System.out.println();
		System.out.println(" version;                                 Show the program version.");
		System.out.println(" help;                                    Show this help information");
		System.out.println(" show schemas;                            Displays all schemas defined in your database.");
		System.out.println(" use <schema_name>;                       Chooses a schema as default.");
		System.out.println(" show tables;                             Displays all tables in the currectly chosen schema.");
		System.out.println(" create schema <schema_name>;             Creates a new schema to hold tables.");
		System.out.println(" create table <table_name>(...);          Creates a new table schema, i.e. a new empty table.");
		System.out.println(" insert into table <table_name> ...;      Inserts a row/record into a table.");
		System.out.println("INSERT INTO TABLE table_name VALUES (values_list);");
		System.out.println(" delete from <table_name> ... ;           Deletes one or more rows/records from a table.(Not Supported.)");
		System.out.println(" drop table <table_name>;                 Remove a table schema, and all of its contained data.");
		System.out.println(" select ... from <table_name> where ...;  Query a table.");	
		System.out.println(" SELECT * FROM <table_name> WHERE field_name [!=|=|>|>=|<|<=] value;");
		System.out.println(" exit/quit;                               Exit the program");
		System.out.println();
		System.out.println();
		System.out.println(line("*",80));
	}
	/**
	 * This displays the table to CLI.
	 * @param headers
	 * @param commaRecords
	 */
	public static void displayTable(List<String> headers,List<String> commaRecords){
		System.out.println(line("+",80));
		System.out.print("|  ");
		for(String header: headers){
			System.out.print("\t");
			System.out.print(header);
		}	
		System.out.print("\t|");
		System.out.println();
		System.out.println(line("+",80));
		for(String record: commaRecords){
			int comma_count = record.length() - record.replace(",", "").length();
			//System.out.println(record);
			String[] r = new String[comma_count+1];
			String[] ro = record.split(",");
			for(int i=0;i<ro.length;i++){
				r[i]=ro[i];
			}
			//System.out.println(r.length);
			System.out.print("|  ");
			for(String s: r){
				//System.out.print(s+" "+s.length());	
				if(s==null){
					System.out.print("");
				}else if(s.equals(CommonUtils.NULL_STRING)){
					System.out.print("NULL");
				}
				else if(s.length() == 0){
					System.out.print("NULL");
				}
				else if(s.equals(CommonUtils.BLANK)){
					System.out.print("   ");
				}
				else{
					System.out.print(s);	
					//System.out.print(s+" "+s.length());	
				}				
				System.out.print("  |  ");
			}			
			System.out.println();			
		}
		System.out.println(line("+",80));
	}
}
