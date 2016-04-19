import java.util.HashMap;
import java.util.Map;
import java.util.Set;



public class QueryProcessor {
	private Set<String> stopWords;
	private long collectionSize;
	private double avgdoclen;
	private Map<String,IndexEntity> index;
	private Map<Integer,VectorEntity> documentVectors; 
	
	QueryProcessor(Indexer indexer){
		this.collectionSize = indexer.getCollectionSize();
		this.avgdoclen = indexer.getAvgdoclen();		
		this.stopWords = indexer.getStop_words();
		this.index = indexer.getLemmaIndex();
		this.documentVectors = indexer.getDocument_meta_map_lema();
	}
	
	public QueryEntity process(String query){
		//Get all tokens in query
		Tokens documentTokens = ProcessUtility.getQueryTokens(query);
		
		Map<String,Integer> lema_map = ProcessUtility.getLemaMap(documentTokens.getTokens(),stopWords);
		
		VectorEntity queryVectorObj = new VectorEntity();
		//find the length of the terms in query into doclen
		queryVectorObj.findDocumentLength(lema_map);
		//find frequency of most frequent term in query
		queryVectorObj.setMaxFrequency(lema_map);
		queryVectorObj.setDocumentTitle(query);
		//set the (term,tf)
		queryVectorObj.setDocumentTermFrequency(lema_map);
		
		queryVectorObj.calculateWeight1(index, collectionSize);
		queryVectorObj.calculateWeight2(index, collectionSize, avgdoclen);
		
		Map<Integer, Double> scoreTable1 = new HashMap<Integer, Double>(); //[docID: score sum from normalized weights after summation
		Map<Integer, Double> scoreTable2 = new HashMap<Integer, Double>();
		for(String term : lema_map.keySet()){			
			IndexEntity termIndex = index.get(term);			
			if(termIndex != null){
				for(int docID : termIndex.getDocument_term_frequency().keySet()){
					VectorEntity document = documentVectors.get(docID);
					double docW1Normal = document.getWeight1_normalized().get(term);
					double queryW1Normal = queryVectorObj.getWeight1_normalized().get(term);
					if(scoreTable1.get(docID) == null){
						scoreTable1.put(docID, docW1Normal*queryW1Normal);
					}else{
						double t = scoreTable1.get(docID);
						scoreTable1.put(docID, (docW1Normal*queryW1Normal) + t);
					}
					
					double docW2Normal = document.getWeight2_normalized().get(term);
					double queryW2Normal = queryVectorObj.getWeight2_normalized().get(term);
					if(scoreTable2.get(docID) == null){
						scoreTable2.put(docID, docW2Normal*queryW2Normal);
					}else{
						double t = scoreTable2.get(docID);
						scoreTable2.put(docID, (docW2Normal*queryW2Normal) + t);
					}				
				}
			}						
		}		
		return new QueryEntity(queryVectorObj,scoreTable1,scoreTable2);
	}
	
	

	public Set<String> getStopWords() {
		return stopWords;
	}

	public void setStopWords(Set<String> stopWords) {
		this.stopWords = stopWords;
	}

	public long getCollectionSize() {
		return collectionSize;
	}

	public void setCollectionSize(long collectionSize) {
		this.collectionSize = collectionSize;
	}

	public Map<String, IndexEntity> getIndex() {
		return index;
	}

	public void setIndex(Map<String, IndexEntity> index) {
		this.index = index;
	}

	public Map<Integer, VectorEntity> getDocumentVectors() {
		return documentVectors;
	}

	public void setDocumentVectors(Map<Integer, VectorEntity> documentVectors) {
		this.documentVectors = documentVectors;
	}
	
	
}
