import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;


public class Tokenization {
	public static final String INPUT_DIRECTORY="C:\\Workspace\\Github\\Information Retrieval\\Tokenization\\Tokenization\\Tokenization\\src\\Cranfield\\";
	public static final String[] SYMBOLS={".","/",",","="};
	
	private List<String> documentIDs;
	public List<String> getDocumentIDs() {
		return documentIDs;
	}
	public void setDocumentIDs(List<String> documentIDs) {
		this.documentIDs = documentIDs;
	}

	private Map<String,Token> tokenDictionary;
	
	private List<Term> tokenList;
	
	public Tokenization() {
		this.tokenDictionary=new HashMap<String, Token>();
		this.tokenList=new ArrayList<Term>();
	}
	
	
	
	static String readFile(String filePath)throws IOException{
		return new String(Files.readAllBytes(Paths.get(filePath)));
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
	 *  3. Display - The number of tokens in the Cranfield text collection (find from tokenList)
	 *  4. Display - The number of unique words in the Cranfield text collection (find from token dictionary)
	 *  5. Display - The number of words that occur only once in the Cranfield text collection (find from token dictionary)
	 *  6. Display - The 30 most frequent words in the Cranfield text collection – list them and their respective frequency information (find from token dictionay)
	 *  7. Display - The average number of word tokens per document (Find from tokenList)   
	 *  
	 *  1. How long the program took to acquire the text characteristics (Find time diff)
	 *  2. 
	 */
	public void process(){	
		
		try{
			documentIDs=getDocumentID();
			//Map<String,Integer> tokenPerDocument=new HashMap<String, Integer>();
			// Build the Token List
			for(String documentID:documentIDs){
				String[] terms=getDocumentTerms(documentID);
				//int count=0;
				for(String term:terms){	
					//System.out.println(term+" "+term.length());
					term=cleanTerm(SYMBOLS,term);
					if(term.length()>0){
						//System.out.println(term+" "+term.length());
						if(term.endsWith("'s")){
							term=term.replace("'s", "");
						}						
						//System.out.println(term);
						Term t=new Term();
						t.setDocumentID(documentID);
						t.setWord(term);
						tokenList.add(t);
						//count++;
					}					
				}
				//tokenPerDocument.put(documentID, count);				
			}
			
			System.out.println("1. The number of tokens in the Cranfield text collection : "+tokenList.size());
			
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
			
			System.out.println("2.The number of unique words in the Cranfield text collection : "+ tokenDictionary.size());
			
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
			System.out.println("3. The number of words that occur only once in the Cranfield text collection : "+ onlyOnceCount);
			
			NavigableMap<Integer,List<String>> nmap=wordFrequncy.descendingMap();
			System.out.println("4. The 30 most frequent words in the Cranfield text collection – list them and their respective frequency information.");
			
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
			
			System.out.println("5. The average number of word tokens per document : "+(tokenList.size()/documentIDs.size()));
			/*entries = tokenPerDocument.entrySet().iterator();
			int sumCount=0;
			while (entries.hasNext()) {
				  Entry thisEntry = (Entry) entries.next();
				  Object key = thisEntry.getKey();
				  Object value = thisEntry.getValue();				  
				  if (count >= 30) break;
				  System.out.println((count+1)+ " --> " + key+ " "+value);				  
				  count++;
			}*/
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
	}
	
	private String cleanTerm(String[] symbols,String term){		
		for(String symbol:symbols){
			if(term.length()<2){
				if(term.equals(symbol)){
					return "";
				}
			}
			if(term.startsWith(symbol)){
				term=term.substring(1,term.length());
			}
			if(term.endsWith(symbol)){
				term=term.substring(0,term.length()-1);
			}			
		}
		return term;
	}
	
	private String[] getDocumentTerms(String documentID){
		String[] terms=null;
		try{
			String input = readFile(INPUT_DIRECTORY+documentID);
			StringBuilder builder=new StringBuilder();
			boolean passText=true;			
			for(int i=0;i<input.length();i++){
				if(input.charAt(i) == '<'){
					passText=false;
				}else if(input.charAt(i)=='>'){
					passText=true;
					builder.append(" ");
				}else if (input.charAt(i)== '\n'){
					builder.append(" ");
				}else if(passText){
					builder.append(input.charAt(i));
				}
			}
			terms=builder.toString().trim().toLowerCase().split(" ");						
		}catch(IOException ex){
			ex.printStackTrace();
		}
		return terms;
		
	}
	
	private List<String> getDocumentID(){
		List<String> documentIDs=new ArrayList<String>();
		try{
			File folder = new File(INPUT_DIRECTORY);
			for (File fileEntry : folder.listFiles()) {
		        documentIDs.add(fileEntry.getName());		        
		    }
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return documentIDs;
	}
	
	public static void main(String[] args){
		Tokenization main=new Tokenization();
		main.process();
	}
}
