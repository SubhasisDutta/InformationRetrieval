import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;



public class HomeWork3 {
	
	public static String DATA_LOCATION; 
	public static String STOPWORDS_FILE;
	public static String QUERY_FILE;
	
	
	public static void main(String[] args) {		
		if(args.length < 1 ){
			DATA_LOCATION = "C:\\Workspace\\Github\\Information Retrieval\\RankedRetrieval\\src\\Cranfield\\"; ///people/cs/s/sanda/cs6322/Cranfield/
			STOPWORDS_FILE="C:\\Workspace\\Github\\Information Retrieval\\RankedRetrieval\\src\\stopwords"; ///people/cs/s/sanda/cs6322/resourcesIR/stopwords
			QUERY_FILE ="C:\\Workspace\\Github\\Information Retrieval\\RankedRetrieval\\src\\hw3.queries"; ///people/cs/s/sanda/cs6322/hw3.queries
		} else {
			DATA_LOCATION = args[0];
			STOPWORDS_FILE= args[1];
			QUERY_FILE = args[2];
		}
		
		
		
		long startTime = Calendar.getInstance().getTimeInMillis();		
		Indexer indexer=new Indexer(DATA_LOCATION,STOPWORDS_FILE);
		indexer.createIndex();  //
				
		List<String> querySet = readQueries(QUERY_FILE);
		
		QueryProcessor queryProcessor = new QueryProcessor(indexer);
		
		for(int i = 0; i < querySet.size(); i++){
			System.out.println("\nQuery"+(i+1)+ ": " + querySet.get(i) );
			QueryEntity q = queryProcessor.process(querySet.get(i));
			//System.out.println("Query Lemmas: "+ q.getQueryVectorObj().getDocumentTermFrequency().toString() );
			//System.out.println("Weight 1 Vector: \n"+q.getQueryVectorObj().getWeight1().toString());
			//System.out.println("Normalized: \n"+q.getQueryVectorObj().getWeight1_normalized().toString());
			System.out.println();
			System.out.println("TOP 5 document by WEIGHT-1");
			DisplayUtility.showTopFive(q.getScoreTable1(), indexer.getDocument_meta_map_lema(), "W1");			
			//System.out.println("Weight 2 Vector: \n"+q.getQueryVectorObj().getWeight2().toString());
			//System.out.println("Normalized: \n"+q.getQueryVectorObj().getWeight1_normalized().toString());
			System.out.println();
			System.out.println("TOP 5 document by WEIGHT-2");
			DisplayUtility.showTopFive(q.getScoreTable2(), indexer.getDocument_meta_map_lema(), "W2");
		}
		
		long endTime = Calendar.getInstance().getTimeInMillis();
		long indexBuild = endTime-startTime;
		System.out.println("Time Taken to Execute : "+indexBuild+" milliseconds");

	}
	
	private static List<String> readQueries(String filename) {
		try{
			String data = new String(Files.readAllBytes(new File(filename).toPath() ));
			String[] parts= Pattern.compile("[Q0-9:]+").split(data);
			List<String> queries = new ArrayList<>();
			for(String part : parts ){
				String query = part.trim().replaceAll("\\r\\n", " ");
				if(query.length() > 0){
					queries.add(query);
				}
			}
			return queries;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
}
