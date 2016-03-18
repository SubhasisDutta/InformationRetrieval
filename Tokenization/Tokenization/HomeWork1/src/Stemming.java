import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Map.Entry;


public class Stemming {
	private Map<String,Integer> stemsDictionary;
	private int stemCount;
	private Tokenization tokenization;
	public Stemming(Tokenization tokenization) {
		this.tokenization=tokenization;
		this.stemsDictionary=new HashMap<String, Integer>();		
	}
	
	public void findStems(){
		
		Map<String, Token> tokenDictionary=tokenization.getTokenDictionary();
		Iterator entries = tokenDictionary.entrySet().iterator();
		while (entries.hasNext()) {
			  Entry thisEntry = (Entry) entries.next();			  
			  Token t= (Token)thisEntry.getValue();
			  Stemmer stemmer=new Stemmer();			  
			  stemmer.add(t.getWord().toCharArray(), t.getWord().length());
			  stemmer.stem();
			  String s=stemmer.toString();			  
			  if(stemsDictionary.containsKey(s)){				  
				  Integer c=stemsDictionary.get(s)+t.getTotalFrequency();
				  stemsDictionary.put(s,c);
			  }else{
				  Integer c=t.getTotalFrequency();				  
				  stemsDictionary.put(s, c);
			  }
			  stemCount+=t.getTotalFrequency();
		}	
				
	}
	
	public void displayStemmingResults(){
		System.out.println("1. No.of distinct Stems: "+stemsDictionary.size());
		
		int onlyOnceCount=0;
		List<String> onlyOneWords=new ArrayList<String>();
		TreeMap<Integer,List<String>> stemFrequncy=new TreeMap<Integer, List<String>>();
		for(Map.Entry<String, Integer> stem : stemsDictionary.entrySet()){
			if(stem.getValue()==1){
				onlyOnceCount++;
				onlyOneWords.add(stem.getKey());
			}
			if(stemFrequncy.containsKey(stem.getValue())){
				List<String> s= stemFrequncy.get(stem.getValue());
				s.add(stem.getKey());
				stemFrequncy.put(stem.getValue(), s);
			}else{
				List<String> s=new ArrayList<String>();
				s.add(stem.getKey());
				stemFrequncy.put(stem.getValue(), s);
			}
		}
		
		System.out.println("2. No.of stems occur only once: "+onlyOnceCount);
		
		System.out.println("3. The 30 most frequent stems:");
		NavigableMap<Integer,List<String>> nmap=stemFrequncy.descendingMap();		
		int count=0;
		Iterator entries = nmap.entrySet().iterator();
		while (entries.hasNext()) {
			  Entry thisEntry = (Entry) entries.next();
			  Integer key = (Integer)thisEntry.getKey();
			  Object value = thisEntry.getValue();				  
			  if (count >= 30) break;
			  System.out.println((count+1)+ " --> " + key+ " "+value);				  
			  count++;
		}
		int documentCount=tokenization.getDocumentIDs().size();
		System.out.println("4. The average number of word stems per document : "+stemCount/documentCount);
	}
}
