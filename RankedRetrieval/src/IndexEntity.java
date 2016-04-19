import java.util.Map;
import java.util.TreeMap;


public class IndexEntity {
	private int document_frequency;
	private int total_frequency;	
	private Map<Integer,Integer> document_term_frequency;  //(docID,termFrequncyOfTerm)
		
	public IndexEntity() {	
		this.document_frequency = 0;
		this.total_frequency = 0;
		this.document_term_frequency = new TreeMap<Integer, Integer>();
	}

	public int getDocument_frequency() {
		return document_frequency;
	}

	public void setDocument_frequency(int document_frequency) {
		this.document_frequency = document_frequency;
	}

	public int getTotal_frequency() {
		return total_frequency;
	}

	public void setTotal_frequency(int total_frequency) {
		this.total_frequency = total_frequency;
	}
	
	public Map<Integer, Integer> getDocument_term_frequency() {
		return document_term_frequency;
	}

	public void setDocument_term_frequency(
			Map<Integer, Integer> document_term_frequency) {
		this.document_term_frequency = document_term_frequency;
	}
	
}
