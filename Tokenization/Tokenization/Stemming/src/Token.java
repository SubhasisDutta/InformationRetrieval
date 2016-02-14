import java.util.Map;


public class Token {
	private String word;
	
	private int noOfDocuments;
	private int totalFrequency;
	private Map<String,Integer> posting;
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public int getNoOfDocuments() {
		return noOfDocuments;
	}
	public void setNoOfDocuments(int noOfDocuments) {
		this.noOfDocuments = noOfDocuments;
	}
	public int getTotalFrequency() {
		return totalFrequency;
	}
	public void setTotalFrequency(int totalFrequency) {
		this.totalFrequency = totalFrequency;
	}
	public Map<String, Integer> getPosting() {
		return posting;
	}
	public void setPosting(Map<String, Integer> posting) {
		this.posting = posting;
	}
	
	
}
