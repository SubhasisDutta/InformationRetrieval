import java.util.Map;
import java.util.Map.Entry;


public class DocumentEntity {
	private int document_length;
	private int max_term_frequency;
	private String max_term;
	
	
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
}
