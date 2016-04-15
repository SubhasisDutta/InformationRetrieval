package com.subhasis.devisbase.utils;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;








import com.subhasis.devisbase.enums.DataType;
import com.subhasis.devisbase.models.DataTypeObj;

public class InformationSchemaUtility {
	
	public static final String INFORMATION_SCHEMA_SCHEMATA = "information_schema.schemata.tbl";
	public static final String INFORMATION_SCHEMA_TABLE = "information_schema.tables.tbl";
	public static final String INFORMATION_SCHEMA_COLUMNS = "information_schema.columns.tbl";
	
	public static String INFORMATION_SCHEMA = "information_schema";
	public static String IS_SCHEMATA = "SCHEMATA";
	public static String IS_TABLES = "TABLES";
	public static String IS_COLUMNS = "COLUMNS";
	
	
	public static DataTypeObj getColumnType(String schema,String table,String columnName)throws IOException{		
		List<DataTypeObj> columnDataObj = getDataTypeObjects(schema, table);
		for(DataTypeObj o : columnDataObj){
			if(o.getColumnName().equals(columnName))
				return o;
		}
		return null;
	}
	
	
	public static List<DataType> getDataTypes(String schema,String table)throws IOException{		
		List<DataType> columnDataTypes = new ArrayList<DataType>();
		List<DataTypeObj> o = getDataTypeObjects(schema,table);
		int size = o.size();
		for(int i=1;i<=size;i++){
			for(DataTypeObj d : o){
				if(d.getOrdinalValue() == i){
					columnDataTypes.add(d.getType());
				}
			}
		}
		return columnDataTypes;		
	}
	
	public static void incrementTableRowCount(String schema,String table)throws IOException{
		//get the table record increment row count and insert back again
		List<DataType> columnDataTypes = InformationSchemaUtility.getDataTypes(INFORMATION_SCHEMA, IS_TABLES);
		
		List<String> records = CommonUtils.getAllFileRecords(CommonUtils.DB_LOCATION+INFORMATION_SCHEMA_TABLE, columnDataTypes);
		//System.out.println(records);
		for(String record :records){
			String[] arr = record.split(",");
			if(arr[0].equals(schema) && arr[1].equals(table)){
				int r=Integer.parseInt(arr[2]);
				r++;
				updateISTableRow(schema,table,r,columnDataTypes);
				return;
			}
		}
	}
	
	private static void updateISTableRow(String schema,String table,int rowCount,List<DataType> is_tableTypes)throws IOException{
		RandomAccessFile tableFile = new RandomAccessFile(CommonUtils.DB_LOCATION+INFORMATION_SCHEMA_TABLE, "rw");
		while(true){
			try{
				String record = CommonUtils.getCurrentRecord(tableFile,is_tableTypes);	
				String[] d = record.split(",");
				if(d[0].equals(schema) && d[1].equals(table)){
					//System.out.println(record);
					long currentPosition = tableFile.getFilePointer();
					tableFile.seek(currentPosition-4); //goes back to the row column
					tableFile.writeInt(rowCount);
					return;
				}
			}catch(EOFException e){
				return;
				//should throw error if not found assumes able is found
			}
		}
		
	}
	
	public static List<DataTypeObj> getDataTypeObjects(String schema,String table)throws IOException{		
		RandomAccessFile columnsTableFile = new RandomAccessFile(CommonUtils.DB_LOCATION+INFORMATION_SCHEMA_COLUMNS, "r");
		List<DataTypeObj> o = new ArrayList<DataTypeObj>();
		//need column types converted ordered by ordinal value
		List<DataType> is_columnTypes = new ArrayList<DataType>();		
		is_columnTypes.add(DataType.VARCHAR_N); //schema_name
		is_columnTypes.add(DataType.VARCHAR_N);// TABLE_NAME
		is_columnTypes.add(DataType.VARCHAR_N);// COLUMN_NAME
		is_columnTypes.add(DataType.INT);// ORDINAL_POSITION
		is_columnTypes.add(DataType.VARCHAR_N);// COLUMN_TYPE
		is_columnTypes.add(DataType.VARCHAR_N);// IS_NULLABLE
		is_columnTypes.add(DataType.VARCHAR_N);// COLUMN_KEY
		while(true){
			try{
				String record = CommonUtils.getCurrentRecord(columnsTableFile,is_columnTypes);				
				String[] d = record.split(",");
				//System.out.println("Recod = "+record);
				//System.out.println(d.length);
				//for(String a:d){
				//	System.out.println(a);
				//}
				if(d[0].equalsIgnoreCase(schema) && d[1].equalsIgnoreCase(table)){
					if(d[6].equals(CommonUtils.NULL_STRING)){
						d[6]="";
					}
					DataTypeObj t = getDataTypeObj(d[2],Integer.parseInt(d[3]),d[4],d[5],d[6]);
					//System.out.println(t);
					o.add(t);
				}
			}catch(EOFException e){
				break;
			}
		}
		//TODO: the list should be returned by arranging according to ordinal value 
		
		return o;
	}
	private static DataTypeObj getDataTypeObj(String colName,int position,String colType,String isNullable,String ifPK){
		DataTypeObj d = new DataTypeObj();
		d.setColumnName(colName);
		d.setType(getType(colType));		
		d.setTypeString(colType);
		d.setLength(getDataMaxLength(colType));
		d.setOrdinalValue(position);
		if(isNullable.equalsIgnoreCase("YES")){
			d.setNullable(true);
		}
		if(ifPK.equalsIgnoreCase("PRI")){
			d.setPK(true);
		}
		return d;
	}
	
	private static int getDataMaxLength(String colType){
		if(colType.startsWith("byte"))
			return 1;
		if(colType.startsWith("short"))
			return 2;
		if(colType.startsWith("int"))
			return 4;
		if(colType.startsWith("long int"))
			return 8;
		if(colType.startsWith("char")){
			String s = colType.replace("char(", "");
			s = s.substring(0,s.length()-1);
			return Integer.parseInt(s);
		}			
		if(colType.startsWith("varchar")){
			String s = colType.replace("varchar(", "");
			s = s.substring(0,s.length()-1);
			return Integer.parseInt(s);
		}			
		if(colType.startsWith("float"))
			return 4;
		if(colType.startsWith("double"))
			return 8;
		if(colType.startsWith("datetime"))
			return 19;
		if(colType.startsWith("date"))
			return 8;		
		return -1; //For Null
	}
	
	public static DataType getType(DataTypeObj d){
		return getType(d.getTypeString());
	}
	
	public static DataType getType(String colType){
		if(colType.startsWith("byte"))
			return DataType.BYTE;
		if(colType.startsWith("short"))
			return DataType.SHORT;
		if(colType.startsWith("int"))
			return DataType.INT;
		if(colType.startsWith("long"))
			return DataType.LONG;
		if(colType.startsWith("char"))
			return DataType.CHAR_N;
		if(colType.startsWith("varchar"))
			return DataType.VARCHAR_N;
		if(colType.startsWith("float"))
			return DataType.FLOAT;
		if(colType.startsWith("double"))
			return DataType.DOUBLE;
		if(colType.startsWith("datetime"))
			return DataType.DATETIME;
		if(colType.startsWith("date"))
			return DataType.DATE;
		return DataType.NULL;
	}
	
	public static boolean removeTableRecord(String schema,String table)throws IOException{
		//RandomAccessFile tableFile = new RandomAccessFile(CommonUtils.DB_LOCATION+INFORMATION_SCHEMA_TABLE, "rw");
		List<DataType> columnDataTypes = InformationSchemaUtility.getDataTypes(INFORMATION_SCHEMA, IS_TABLES);	
		String tableFile = CommonUtils.DB_LOCATION+INFORMATION_SCHEMA_TABLE;
		searchAndRemove(tableFile,columnDataTypes,schema,table);		
		return true;
	}
	
	private static void searchAndRemove(String fileName,List<DataType> columnDataTypes,String schema,String table)throws IOException{		
		RandomAccessFile file = new RandomAccessFile(fileName, "rw");
		while(true){
			try{	
				//get the record
				long beforeSeek = file.getFilePointer();
				String record = CommonUtils.getCurrentRecord(file, columnDataTypes);
				long afterSeek = file.getFilePointer();
				//check if schema and table matches
				String[] d = record.split(",");
				if(d[0].equals(schema) && d[1].equals(table)){
					//removeRecord(file, beforeSeek, afterSeek);
					RandomAccessFile temp = new RandomAccessFile(CommonUtils.DB_LOCATION+INFORMATION_SCHEMA+"_temp", "rw");
					long fileSize = file.length();
					FileChannel sourceChannel = file.getChannel();
					FileChannel targetChannel = temp.getChannel();
					sourceChannel.transferTo(afterSeek, (fileSize - afterSeek), targetChannel);
					file.seek(beforeSeek);
					sourceChannel.truncate(beforeSeek);		
					targetChannel.position(0L);		
					sourceChannel.transferFrom(targetChannel, file.getFilePointer(), (fileSize - afterSeek));
					//sourceChannel.close();
					//targetChannel.close();
					//temp.close();
					file.seek(beforeSeek);
					temp.close();					
					//remove temporary file
					CommonUtils.deleteFile(new File(CommonUtils.DB_LOCATION+INFORMATION_SCHEMA+"_temp"));
				}				
			}catch(EOFException e){
				break;
			}						
		}		
	}
	
	@Deprecated
	public static boolean removeRecord(RandomAccessFile file,long before,long after)throws IOException{
		//RandomAccessFile file = new RandomAccessFile(CommonUtils.DB_LOCATION+INFORMATION_SCHEMA_TABLE, "rw");
		RandomAccessFile temp = new RandomAccessFile(CommonUtils.DB_LOCATION+INFORMATION_SCHEMA+"_temp", "rw");
		long fileSize = file.length();
		FileChannel sourceChannel = file.getChannel();
		FileChannel targetChannel = temp.getChannel();
		sourceChannel.transferTo(after, (fileSize - after), targetChannel);
		file.seek(before);
		sourceChannel.truncate(before);		
		targetChannel.position(0L);		
		sourceChannel.transferFrom(targetChannel, file.getFilePointer(), (fileSize - after));
		//sourceChannel.close();
		//targetChannel.close();
		//temp.close();
		file.seek(before);
		temp.close();
		file.close();
		//remove temporary file
		CommonUtils.deleteFile(new File(CommonUtils.DB_LOCATION+INFORMATION_SCHEMA+"_temp"));		
		return true;
	}
	
	public static boolean removeColumnRecords(String schema,String table)throws IOException{
		//RandomAccessFile columnsTableFile = new RandomAccessFile(CommonUtils.DB_LOCATION+INFORMATION_SCHEMA_COLUMNS, "r");		
		List<DataType> columnDataTypes = InformationSchemaUtility.getDataTypes(INFORMATION_SCHEMA, IS_COLUMNS);	
		String columnsTableFile = CommonUtils.DB_LOCATION+INFORMATION_SCHEMA_COLUMNS;
		searchAndRemove(columnsTableFile,columnDataTypes,schema,table);		
		return true;
	}
	
}
