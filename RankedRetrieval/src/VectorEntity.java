import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class VectorEntity {
		
	//set by setMaxFrequency
	private int max_term_frequency;
	private String max_term;
	
	
	private String documentTitle;
	private String documentName;
	
	private int document_length;
		
	
	private Map<String,Integer> documentTermFrequency;
	
	private Map<String,Double> documentTermFrequencyWeighed;
	
	//private Map<String,Double> weightINC;
	//private Map<String,Double> weightINC_normalized;
	
	private Map<String,Double> weight1; //'max tf' term weighting
	private Map<String,Double> weight1_normalized;
	
	private Map<String,Double> weight2; //Okapi term weighting
	private Map<String,Double> weight2_normalized;
	
	public void findDocumentLength(Map<String,Integer> map){
		int docLen =0;
		for(String s : map.keySet()){
			docLen += map.get(s);
		}
		document_length=docLen;
	}
	
	/*public void findDocumentWeights(){
		documentTermFrequencyWeighed = new HashMap<String, Double>();
		weightINC = new HashMap<String, Double>();
		weightINC_normalized = new HashMap<String, Double>();
		for(String term : documentTermFrequency.keySet()){			
			double value = 1 + Math.log(documentTermFrequency.get(term));
			documentTermFrequencyWeighed.put(term, value);
		}
		double sq_sum = 0.0;
		for(String term : documentTermFrequencyWeighed.keySet()){
			weightINC.put(term, documentTermFrequencyWeighed.get(term));
			double t = weightINC.get(term);
			t = t*t;
			sq_sum +=t;
		}
		sq_sum = Math.sqrt(sq_sum);
		for(String term : weightINC.keySet()){
			double value = weightINC.get(term)/sq_sum;
			weightINC_normalized.put(term, value);
		}
		
		//This function to be changed if Weight Calculation is Wrong
	}*/
	
	public void calculateWeight1(Map<String,IndexEntity> index,long collectionSize){
		weight1 = new HashMap<String, Double>();
		weight1_normalized = new HashMap<String, Double>();
		double sq_sum = 0.0;
		for(String term : documentTermFrequency.keySet()){	
			int tf = documentTermFrequency.get(term);
			int df =0;
			if(index.get(term) != null){
				df = index.get(term).getDocument_frequency();
			}			
			weight1.put(term, ProcessUtility.findW1(tf,max_term_frequency, df, collectionSize));
			double t = weight1.get(term);
			t = t*t;
			sq_sum +=t;
		}
		sq_sum = Math.sqrt(sq_sum);
		for(String term : weight1.keySet()){
			double value = weight1.get(term)/sq_sum;
			weight1_normalized.put(term, value);
		}
	}
	
	public void calculateWeight2(Map<String,IndexEntity> index,long collectionSize,double avgdoclen){
		weight2 = new HashMap<String, Double>();
		weight2_normalized = new HashMap<String, Double>();
		double sq_sum = 0.0;
		for(String term : documentTermFrequency.keySet()){	
			int tf = documentTermFrequency.get(term);
			int df =0;
			if(index.get(term) != null){
				df = index.get(term).getDocument_frequency();
			}			
			//findW2(int termFreq, int doclength, double avgDoclength, int docFreq, long collectionSize)
			//System.out.println(tf+"  "+document_length+"  "+avgdoclen+"  "+df+"  "+collectionSize);
			weight2.put(term, ProcessUtility.findW2(tf,document_length, avgdoclen,df, collectionSize)); //DocumentEntity.documentFrquency
			double t = weight2.get(term);
			t = t*t;
			sq_sum +=t;
		}
		sq_sum = Math.sqrt(sq_sum);
		for(String term : weight2.keySet()){
			double value = weight2.get(term)/sq_sum;
			weight2_normalized.put(term, value);
		}
	}
	
	public void setMaxFrequency(Map<String,Integer> map){
		int max_fq =0;	
		String max_word ="";
		for(final Entry<String, Integer> entry : map.entrySet()){
			if(max_fq < entry.getValue()){
				max_fq = entry.getValue();
				max_word = entry.getKey();
			}
		}
		this.setMax_term(max_word);
		this.setMax_term_frequency(max_fq);
	}
	
	public int getDocument_length() {
		return document_length;
	}
	public void setDocument_length(int document_length) {
		this.document_length = document_length;
	}
	public int getMax_term_frequency() {
		return max_term_frequency;
	}
	public void setMax_term_frequency(int max_term_frequency) {
		this.max_term_frequency = max_term_frequency;
	}
	public String getMax_term() {
		return max_term;
	}
	public void setMax_term(String max_term) {
		this.max_term = max_term;
	}

	public String getDocumentTitle() {
		return documentTitle;
	}

	public void setDocumentTitle(String documentTitle) {
		this.documentTitle = documentTitle;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	/*public Integer getDocumentFrquency() {
		return documentFrquency;
	}

	public void setDocumentFrquency(Integer documentFrquency) {
		this.documentFrquency = documentFrquency;
	}*/

	public Map<String, Integer> getDocumentTermFrequency() {
		return documentTermFrequency;
	}

	public void setDocumentTermFrequency(Map<String, Integer> documentTermFrequency) {
		this.documentTermFrequency = documentTermFrequency;
	}

	public Map<String, Double> getDocumentTermFrequencyWeighed() {
		return documentTermFrequencyWeighed;
	}

	public void setDocumentTermFrequencyWeighed(
			Map<String, Double> documentTermFrequencyWeighed) {
		this.documentTermFrequencyWeighed = documentTermFrequencyWeighed;
	}

	/*public Map<String, Double> getWeightINC() {
		return weightINC;
	}

	public void setWeightINC(Map<String, Double> weightINC) {
		this.weightINC = weightINC;
	}

	public Map<String, Double> getWeightINC_normalized() {
		return weightINC_normalized;
	}

	public void setWeightINC_normalized(Map<String, Double> weightINC_normalized) {
		this.weightINC_normalized = weightINC_normalized;
	}*/

	public Map<String, Double> getWeight1() {
		return weight1;
	}

	public void setWeight1(Map<String, Double> weight1) {
		this.weight1 = weight1;
	}

	public Map<String, Double> getWeight1_normalized() {
		return weight1_normalized;
	}

	public void setWeight1_normalized(Map<String, Double> weight1_normalized) {
		this.weight1_normalized = weight1_normalized;
	}

	public Map<String, Double> getWeight2() {
		return weight2;
	}

	public void setWeight2(Map<String, Double> weight2) {
		this.weight2 = weight2;
	}

	public Map<String, Double> getWeight2_normalized() {
		return weight2_normalized;
	}

	public void setWeight2_normalized(Map<String, Double> weight2_normalized) {
		this.weight2_normalized = weight2_normalized;
	}
	
	
}
