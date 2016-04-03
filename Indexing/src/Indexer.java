import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Indexer {
	private String file_location;	
	private Set<String> stop_words;
	
	public static StanfordLemmatizer lemmatizer = new StanfordLemmatizer();
	
	private Map<String,IndexEntity> lemmaIndex;	
	

	private Map<Integer,DocumentEntity> document_meta_map_lema;
	private Map<String,IndexEntity> stemIndex;
	private Map<Integer,DocumentEntity> document_meta_map_stem;
	
	/**
	 * This builds the Stop word Set and initialize the index.
	 * @param file_location
	 * @param stop_word_location
	 */
	Indexer(String file_location,String stop_word_location){
		this.file_location=file_location;
		this.stop_words=new HashSet<String>();
		this.lemmaIndex = new TreeMap<String, IndexEntity>();
		this.stemIndex = new TreeMap<String, IndexEntity>();
		this.document_meta_map_lema = new TreeMap<Integer, DocumentEntity>();
		this.document_meta_map_stem = new TreeMap<Integer, DocumentEntity>();
		
		try{
			BufferedReader bufferedReader=new BufferedReader(new FileReader(stop_word_location));
			for(String line;(line = bufferedReader.readLine())!=null;){
				this.stop_words.add(line.trim());
			}
			bufferedReader.close();
		}catch(FileNotFoundException e1){
			e1.printStackTrace();
		}catch (IOException e2) {
			e2.printStackTrace();
		}
	}
	
	public void createIndex(){
		//get all valid Tokens for a document (documentID,[tokens]) and populate document_meta_map
		List<String> documentstr_IDs = ProcessInputUtility.getInputDocuments(file_location);
		for(String documentstr_ID:documentstr_IDs){
			//Build the Document Entity for each document
			DocumentTokens documentTokens = ProcessInputUtility.getDocumentTokens(file_location, documentstr_ID);
			DocumentEntity lema_DE = new DocumentEntity();
			DocumentEntity stem_DE = new DocumentEntity();
			lema_DE.setDocument_length(documentTokens.getTokens().size());
			stem_DE.setDocument_length(documentTokens.getTokens().size());
			//Create the lema and stem map for each document
			Map<String,Integer> lema_map = getLemaMap(documentTokens.getTokens());
			Map<String,Integer> stem_map = getStemMap(documentTokens.getTokens());
			
			lema_DE.setMaxFrequency(lema_map);
			stem_DE.setMaxFrequency(stem_map);
								
			//update the lema and stem index
			int document_id =documentTokens.getDocument_id();
			updateIndex(document_id,lema_map,this.lemmaIndex);
			updateIndex(document_id,stem_map,this.stemIndex);

			document_meta_map_lema.put(document_id, lema_DE);
			document_meta_map_stem.put(document_id, stem_DE);
		}
	}	
	
	private void updateIndex(int doc_id,Map<String,Integer> doc_map,Map<String,IndexEntity> index ){
		for (final Entry<String, Integer> entry : doc_map.entrySet()) {
			IndexEntity temp;
			if (index.containsKey(entry.getKey())) {				
				temp = index.get(entry.getKey());				
			} else {				
				temp = new IndexEntity();
				index.put(entry.getKey(), temp);
			}
			temp.setDocument_frequency(temp.getDocument_frequency() + 1);
			temp.setTotal_frequency(temp.getTotal_frequency()+entry.getValue());
			temp.getDocument_term_frequency().put(doc_id, entry.getValue());	
			
		}
		//System.out.println(index);
	}
	
	private Map<String,Integer> getLemaMap(List<String> tokens){
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
	
	private Map<String,Integer> getStemMap(List<String> tokens){
		Map<String,Integer> stemsMap= new HashMap<String, Integer>();
		for(String token:tokens){
			if(!stop_words.contains(token)){				
				Stemmer stemmer=new Stemmer();
				stemmer.add(token.toCharArray(), token.length());
				stemmer.stem();
				String stem=stemmer.toString();
				int count = stemsMap.containsKey(stem) ? stemsMap.get(stem) : 0;				
				stemsMap.put(stem,count+1);
			}
		}
		return stemsMap;
	}
	

	public Map<String, IndexEntity> getLemmaIndex() {
		return lemmaIndex;
	}

	public void setLemmaIndex(Map<String, IndexEntity> lemmaIndex) {
		this.lemmaIndex = lemmaIndex;
	}

	public Map<String, IndexEntity> getStemIndex() {
		return stemIndex;
	}

	public void setStemIndex(Map<String, IndexEntity> stemIndex) {
		this.stemIndex = stemIndex;
	}
	
	public Map<Integer, DocumentEntity> getDocument_meta_map_lema() {
		return document_meta_map_lema;
	}

	public void setDocument_meta_map_lema(
			Map<Integer, DocumentEntity> document_meta_map_lema) {
		this.document_meta_map_lema = document_meta_map_lema;
	}

	public Map<Integer, DocumentEntity> getDocument_meta_map_stem() {
		return document_meta_map_stem;
	}

	public void setDocument_meta_map_stem(
			Map<Integer, DocumentEntity> document_meta_map_stem) {
		this.document_meta_map_stem = document_meta_map_stem;
	}
	
}
