import java.util.Calendar;


public class HomeWork1 {
	public static String fileDirectoryPath;		
	public static void main(String[] args) {
		if(args.length < 1 ){
			fileDirectoryPath = "C:\\Workspace\\Github\\Information Retrieval\\Indexing\\src\\Cranfield\\";			
		} else {
			fileDirectoryPath = args[0].toString();			
		}
				
		long startTime = Calendar.getInstance().getTimeInMillis();
		Tokenization tokenization=new Tokenization(fileDirectoryPath);
		System.out.println("*******************TOKENIZATION****************************");
		tokenization.process();	
		tokenization.displayTokenizationResults();
		System.out.println("Time taken to acquire characteristics: " + (Calendar.getInstance().getTimeInMillis()-startTime) + "ms");
		System.out.println("*******************STEMMING****************************");
		startTime = Calendar.getInstance().getTimeInMillis();
		Stemming stemming=new Stemming(tokenization);
		stemming.findStems();
		stemming.displayStemmingResults();
		System.out.println("Time taken to acquire characteristics: " + (Calendar.getInstance().getTimeInMillis()-startTime) + "ms");
	}
	
	

}
