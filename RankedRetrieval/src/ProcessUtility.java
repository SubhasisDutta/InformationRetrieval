import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;


public class ProcessUtility {
	private static Pattern pattern = Pattern.compile("<.?title>",Pattern.CASE_INSENSITIVE);
	private static StanfordLemmatizer lemmatizer = new StanfordLemmatizer();
	
	public static List<String> getInputDocuments(String inputDirectory){
		List<String> documentIDs=new ArrayList<String>();
		try{
			File folder = new File(inputDirectory);
			for (File fileEntry : folder.listFiles()) {
		        documentIDs.add(fileEntry.getName());		        
		    }
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return documentIDs;
	}
	
	public static Tokens getDocumentTokens(String inputDirectory,String documentName){
		StringTokenizer terms=_getDocumentTokens(inputDirectory,documentName);
		Tokens documentTokens = new Tokens();		
		String doc_id = documentName.replaceAll("[^\\d]", "");
		documentTokens.setDocument_id(Integer.parseInt(doc_id));		
		documentTokens.setTokens(getTokens(terms));
		return documentTokens;
	}
	
	public static Tokens getQueryTokens(String query){
		StringTokenizer terms=new StringTokenizer(query);
		Tokens documentTokens = new Tokens();		
		documentTokens.setTokens(getTokens(terms));
		return documentTokens;
	}
	
	private static List<String> getTokens(StringTokenizer terms){
		List<String> tokens = new ArrayList<String>();
		while(terms.hasMoreElements()){
			String term=terms.nextToken();
			//Converts Possessives - university's to university
			if(term.endsWith("'s")){
				term=term.replace("'s", "");
			}
			//Remove all characters apart from characters and digits
			term=term.replaceAll("[^a-zA-Z0-9]", "");
			if(term.length()>0){
				tokens.add(term);						
			}
		}
		return tokens;
	}
	
	/**
	 * Get all the Tokens in a Document File
	 * @param documentID
	 * @return
	 */
	private static StringTokenizer _getDocumentTokens(String inputDirectory,String documentID){		
		StringBuilder builder=new StringBuilder();
		try{
			BufferedReader br = new BufferedReader(new FileReader(inputDirectory+documentID));
			String currentLine;
			while ((currentLine = br.readLine()) != null) {
				if(!(currentLine.contains("<") && currentLine.contains(">"))){
					currentLine = currentLine.replaceAll("[-]", " ").toLowerCase();					
					builder.append(currentLine+" ");
				}
			}	
			br.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}		
		return new StringTokenizer(builder.toString());		
	}
	
	public static String getTitle(String inputDirectory,String documentName) {
		try {
			File file = new File(inputDirectory+documentName);
			String data = new String(Files.readAllBytes(file.toPath()));
			String[] parts= pattern.split(data);
			if(parts.length>1){
				return parts[1].replace("\n", " ");
			}else 
				System.out.println("...."+file.getPath());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static double findW1(int termFreq, int maxTermFreq, int docFreq, long collectionSize){
		double temp = 0.0;
		if(docFreq == 0){
			return temp;
		}
		try {
			temp = ( 0.4 + 0.6 * Math.log (termFreq + 0.5) / Math.log (maxTermFreq + 1.0) ) *  (Math.log(collectionSize / (double)docFreq) / Math.log(collectionSize)) ;
		} catch (Exception e) {
			temp = 0.0;
		}
		return temp;
	}

	public static double  findW2(int termFreq, int doclength, double avgDoclength, int docFreq, long collectionSize){
		double temp = 0.0;
		if(docFreq == 0){
			return temp;
		}
		try {
			temp = (0.4 + 0.6 * (termFreq / (termFreq + 0.5 + 1.5 * (doclength / avgDoclength))) * Math.log (collectionSize / (double)docFreq) / Math.log(collectionSize) );
			
		} catch (Exception e) {
			//e.printStackTrace();
			temp = 0.0;
		}		
		return temp;
	}
	
	public static Map<String,Integer> getLemaMap(List<String> tokens,Set<String> stop_words){
		Map<String,Integer> lemmaMap= new HashMap<String, Integer>();
		for(String token:tokens){
			if(!stop_words.contains(token)){				
				List<String> lemas = lemmatizer.lemmatize(token);  //Lematizer.lematize(token);
				int count =0;
				for(String lema: lemas){
					count = lemmaMap.containsKey(lema) ? lemmaMap.get(lema) : 0;
					lemmaMap.put(lema,count+1);
				}
			}
		}
		return lemmaMap;
	}
}
