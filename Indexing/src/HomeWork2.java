import java.util.Calendar;


public class HomeWork2 {

	public static void main(String[] args) {
		String fileDirectoryPath = "C:\\Workspace\\Github\\Information Retrieval\\Indexing\\src\\C2\\";		
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
