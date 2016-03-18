import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;


public class Tokenization {
	
	private String inputDirectory;	
	private List<String> documentIDs;
	private Map<String,Token> tokenDictionary;	
	private List<Term> tokenList;
	
	public List<Term> getTokenList() {
		return tokenList;
	}
	public Tokenization(String inputDirectory){
		this.inputDirectory=inputDirectory;		
		this.tokenDictionary=new HashMap<String, Token>();
		this.tokenList=new ArrayList<Term>();
	}
	public List<String> getDocumentIDs() {
		return documentIDs;
	}
	public void setDocumentIDs(List<String> documentIDs) {
		this.documentIDs = documentIDs;
	}
	public static String readFile(String filePath)throws IOException{
		return new String(Files.readAllBytes(Paths.get(filePath)));
	}
	public Map<String, Token> getTokenDictionary() {
		return tokenDictionary;
	}
	/**
	 *  This function calls all the steps involved in tokenization process.
	 *  1. Iterate over all available files one after another.
	 *  	1. Load the file.
	 *  	2. Convert the file to lower case.
	 *      3. Remove the <tags> and split the words based on space.
	 *      4. Remove special characters and handle special case.
	 *      	1. If 's found at end of word remove it.
	 *          2. If two or more characters are separated by .(dot) keep the dot but remove the dot from end.
	 *          3. If a word contains a -(hyphen) in the middle keep it.
	 *          4. for rest of the symbols remove them from start and end.
	 *      5. Append the term along with the document ID to the term list.
	 *      
	 *  2. Iterate over the termList and build the tokenDictionary
	 *  	1. check if term/word present in token dictionary.
	 *  	2. If not present add new token - create token initialize for one and put it in the dictionary
	 *      3. If present update the token for the term in the dictionary.
	 *     
	 *    
	 */
	public void process(){	
		
		try{
			documentIDs=getDocumentID();			
			// Build the Token List -> (word,document ID)
			for(String documentID:documentIDs){
				StringTokenizer terms=getDocumentTerms(documentID);					
				while(terms.hasMoreElements()){
					String term=terms.nextToken();
					//Converts Possessives - university's to university
					if(term.endsWith("'s")){
						term=term.replace("'s", "");
					}
					//Remove all characters apart from characters and digits
					term=term.replaceAll("[^a-zA-Z0-9]", "");
					if(term.length()>0){
						Term t=new Term();
						t.setDocumentID(documentID);
						t.setWord(term);
						tokenList.add(t);						
					}
				}						
			}
			
			for(Term term: tokenList){
				if(tokenDictionary.containsKey(term.getWord())){
					Token token=tokenDictionary.get(term.getWord());
					token.setTotalFrequency(token.getTotalFrequency()+1);
					if(token.getPosting().containsKey(term.getDocumentID())){
						Map<String,Integer> p=token.getPosting();						
						Integer frequency=p.get(term.getDocumentID());
						p.put(term.getDocumentID(), frequency+1);						
					}else{
						token.getPosting().put(term.getDocumentID(), 1);
						token.setNoOfDocuments(token.getNoOfDocuments()+1);
					}
				}else{
					Token token=new Token();
					token.setWord(term.getWord());
					token.setNoOfDocuments(1);
					token.setTotalFrequency(1);
					Map<String,Integer> posting=new HashMap<String, Integer>();
					posting.put(term.getDocumentID(), 1);
					token.setPosting(posting);
					tokenDictionary.put(term.getWord(), token);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
	}	
	/**
	 * Get all the Tokens in a Document File
	 * @param documentID
	 * @return
	 */
	private StringTokenizer getDocumentTerms(String documentID){		
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
		}catch(Exception ex){
			ex.printStackTrace();
		}		
		return new StringTokenizer(builder.toString());		
	}
	/**
	 * REturns a list of files available in a folder.
	 * @return
	 */
	private List<String> getDocumentID(){
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
	/**
	 *  Used to Print the Tokenization Results.	
	 */
	public void displayTokenizationResults(){
		System.out.println("1. No.of Tokens in collection: "+tokenList.size());
		System.out.println("2. No.of Unique Tokens incollection: "+ tokenDictionary.size());
		int onlyOnceCount=0;
		List<String> onlyOneWords=new ArrayList<String>();
		TreeMap<Integer,List<String>> wordFrequncy=new TreeMap<Integer, List<String>>();
		for(Map.Entry<String, Token> word : tokenDictionary.entrySet()){
			Token token=word.getValue();
			if(token.getTotalFrequency()==1){
				onlyOnceCount++;
				onlyOneWords.add(token.getWord());
			}
			if(wordFrequncy.containsKey(token.getTotalFrequency())){
				List<String> s= wordFrequncy.get(token.getTotalFrequency());
				s.add(token.getWord());
				wordFrequncy.put(token.getTotalFrequency(), s);
			}else{
				List<String> s=new ArrayList<String>();
				s.add(token.getWord());
				wordFrequncy.put(token.getTotalFrequency(), s);
			}
		}
		System.out.println("3. No.of Tokens that occur once in collection: "+ onlyOnceCount);
		NavigableMap<Integer,List<String>> nmap=wordFrequncy.descendingMap();
		System.out.println("4. 30 most frequent Tokens:");
		
		int count=0;
		Iterator entries = nmap.entrySet().iterator();
		while (entries.hasNext()) {
			  Entry thisEntry = (Entry) entries.next();
			  Object key = thisEntry.getKey();
			  Object value = thisEntry.getValue();				  
			  if (count >= 30) break;
			  System.out.println((count+1)+ " --> " + key+ " "+value);				  
			  count++;
		}		
		System.out.println("5. The average number of word tokens per document: "+(tokenList.size()/documentIDs.size()));
	}
}

