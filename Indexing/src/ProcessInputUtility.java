import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class ProcessInputUtility {
	
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
	
	public static DocumentTokens getDocumentTokens(String inputDirectory,String documentName){
		StringTokenizer terms=_getDocumentTokens(inputDirectory,documentName);
		DocumentTokens documentTokens = new DocumentTokens();
		String doc_id = documentName.replaceAll("[^\\d]", "");
		documentTokens.setDocument_id(Integer.parseInt(doc_id));
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
		documentTokens.setTokens(tokens);
		return documentTokens;
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
}
