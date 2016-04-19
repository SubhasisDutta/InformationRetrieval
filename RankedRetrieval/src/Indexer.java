import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Indexer {
	
	private String file_location;	
	
	private Set<String> stop_words;
	private Map<String,IndexEntity> lemmaIndex; //[term: all index related data]
	private Map<Integer,VectorEntity> document_meta_map_lema;  //[DocID:all document related data]
	
	private long collectionSize;
	private double avgdoclen;
	
	
	/**
	 * This builds the Stop word Set and initialize the index.
	 * @param file_location
	 * @param stop_word_location
	 */
	Indexer(String file_location,String stop_word_location){
		this.file_location=file_location;
		this.stop_words=new HashSet<String>();
		this.lemmaIndex = new TreeMap<String, IndexEntity>();		
		this.document_meta_map_lema = new TreeMap<Integer, VectorEntity>();		
		
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
		List<String> documentstr_IDs = ProcessUtility.getInputDocuments(file_location);
		//DocumentEntity.documentFrquency = 1; // for doing with df 1  
		collectionSize = documentstr_IDs.size();
		long sumDocLength = 0;
		for(String documentstr_ID:documentstr_IDs){
			//Build the Document Entity for each document
			//Get all the tokens for the document
			Tokens documentTokens = ProcessUtility.getDocumentTokens(file_location, documentstr_ID);			
			//Get all the lemmas fo the document after removing all stopwords
			Map<String,Integer> lema_map = ProcessUtility.getLemaMap(documentTokens.getTokens(),stop_words);
			
			VectorEntity docVectorObj = new VectorEntity();
			//the length of the document, in words,discounting stop-words			
			docVectorObj.findDocumentLength(lema_map);	
			//used to find average doc length at the end
			sumDocLength +=docVectorObj.getDocument_length();
			//the frequency of the most frequent indexed term in the document
			docVectorObj.setMaxFrequency(lema_map);
			//get the title of the doc for book keeping
			docVectorObj.setDocumentTitle(ProcessUtility.getTitle(file_location, documentstr_ID));
			//get the name of the doc for book keeping
			docVectorObj.setDocumentName(documentstr_ID);
			//save the (tem,tf) in the document
			docVectorObj.setDocumentTermFrequency(lema_map);			
			//lema_DE.findDocumentWeights();					
								
			//update the lema index
			int document_id =documentTokens.getDocument_id();
			updateIndex(document_id,lema_map,this.lemmaIndex);
			
			document_meta_map_lema.put(document_id, docVectorObj);			
		}
		
		avgdoclen = (double)sumDocLength / collectionSize;
		for(Integer docID : document_meta_map_lema.keySet()){
			VectorEntity d = document_meta_map_lema.get(docID);
			d.calculateWeight1(lemmaIndex,collectionSize); // insert the lemmaIndex use his to get the document fequency
			d.calculateWeight2(lemmaIndex,collectionSize,avgdoclen);
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
	
	
	
	public Map<String, IndexEntity> getLemmaIndex() {
		return lemmaIndex;
	}

	public void setLemmaIndex(Map<String, IndexEntity> lemmaIndex) {
		this.lemmaIndex = lemmaIndex;
	}
		
	public Map<Integer, VectorEntity> getDocument_meta_map_lema() {
		return document_meta_map_lema;
	}

	public void setDocument_meta_map_lema(
			Map<Integer, VectorEntity> document_meta_map_lema) {
		this.document_meta_map_lema = document_meta_map_lema;
	}	
	
	public Set<String> getStop_words() {
		return stop_words;
	}

	public void setStop_words(Set<String> stop_words) {
		this.stop_words = stop_words;
	}
	
	public String getFile_location() {
		return file_location;
	}

	public void setFile_location(String file_location) {
		this.file_location = file_location;
	}

	public long getCollectionSize() {
		return collectionSize;
	}

	public void setCollectionSize(long collectionSize) {
		this.collectionSize = collectionSize;
	}

	public double getAvgdoclen() {
		return avgdoclen;
	}

	public void setAvgdoclen(double avgdoclen) {
		this.avgdoclen = avgdoclen;
	}
}
