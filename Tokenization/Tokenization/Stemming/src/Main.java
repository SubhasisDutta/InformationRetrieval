import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Map.Entry;
import java.util.TreeMap;


public class Main {

	public static void main(String[] args) {
		Tokenization tokenization=new Tokenization();
		tokenization.process();
		Map<String, Token> tokenDictionary=tokenization.getTokenDictionary();
		List<String> tokensDistinct=new ArrayList<String>();
		
		Map<String,Integer> stemsDictionary=new HashMap<String, Integer>();
		
		Iterator entries = tokenDictionary.entrySet().iterator();
		while (entries.hasNext()) {
			  Entry thisEntry = (Entry) entries.next();
			  tokensDistinct.add((String)thisEntry.getKey());
		}
		
		for(String word:tokensDistinct){
			Stemmer stemmer=new Stemmer();
			stemmer.add(word.toCharArray(), word.length());
			stemmer.stem();
			String s=stemmer.toString();
			if(stemsDictionary.containsKey(s)){
				Integer c=stemsDictionary.get(s);
				stemsDictionary.put(s, c+1);
			}else{
				stemsDictionary.put(s, 1);
			}
		}
		System.out.println("1. The number of distinct stems in the Cranfield text collection : "+stemsDictionary.size());
		
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
		
		System.out.println("2. The number of stems that occur only once in the Cranfield text collection : "+onlyOnceCount);
		
		System.out.println("3. The 30 most frequent stems in the Cranfield text collection – list them and their respective frequency information :");
		NavigableMap<Integer,List<String>> nmap=stemFrequncy.descendingMap();
		//System.out.println(nmap);
		int count=0;
		entries = nmap.entrySet().iterator();
		while (entries.hasNext()) {
			  Entry thisEntry = (Entry) entries.next();
			  Integer key = (Integer)thisEntry.getKey();
			  Object value = thisEntry.getValue();				  
			  if (count >= 10) break;
			  System.out.println((count+1)+ " --> " + key+ " "+value);				  
			  count++;
		}
		int documentCount=tokenization.getDocumentIDs().size();
		System.out.println("4. The average number of word stems per document : "+stemsDictionary.size()/documentCount);
		
		

	}

}
