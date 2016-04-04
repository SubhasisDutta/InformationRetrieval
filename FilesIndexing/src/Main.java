import java.io.File;


public class Main {
	public static String LOCATION = "db/";
	public static void main(String[] args){
		if(args.length < 1 ){
			LOCATION = "db/";			
		} else {
			LOCATION = args[0];			
		}
		//Give welcome Message
		
		// check if initial information schema present, if not present create it
		if(checkISPresent()){
			MakeInformationSchema.initilize(LOCATION);
			System.out.println("The database has been initilized and will be stored in location "+ LOCATION);
		}
		
		
		System.out.println("jfkdjfkd");
		
		
	}
	
	public static boolean checkISPresent(){
		String[] initialFiles = new String[3];
		initialFiles[0] = "information_schema.schemata.tbl";
		initialFiles[0] = "information_schema.table.tbl";
		initialFiles[0] = "information_schema.columns.tbl";
		for (String file : initialFiles){
			File f = new File(LOCATION+file);
			if(!f.exists() && f.isDirectory()) { 
			    return true;
			}
		}
		return false;		
	}
}
