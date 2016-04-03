import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

enum CompressType{
	GAMMA,DELTA
}
enum CodingType{
	BLOCK_CODING,FRONT_CODING
}

public class CompressedIndex {
	private byte[] termString;
	
	private List<CompressedEntity> compressedIndex;
	
	private List<DocumentDataEntity> compressed_doc_list;

	public byte[] getTermString() {
		return termString;
	}

	public void setTermString(byte[] termString) {
		this.termString = termString;
	}

	public List<CompressedEntity> getCompressedIndex() {
		return compressedIndex;
	}

	public void setCompressedIndex(List<CompressedEntity> compressedIndex) {
		this.compressedIndex = compressedIndex;
	}

	public List<DocumentDataEntity> getCompressed_doc_list() {
		return compressed_doc_list;
	}

	public void setCompressed_doc_list(List<DocumentDataEntity> compressed_doc_list) {
		this.compressed_doc_list = compressed_doc_list;
	}
	
	public void createCompressedIndex(Map<String,IndexEntity> index,CompressType type,CodingType coding,int k){
		StringBuilder termBuilder = new StringBuilder();
		this.compressedIndex = new ArrayList<CompressedEntity>();
		List<String> blockBuilder = new ArrayList<String>();
		int counter =0;
		for (final Entry<String, IndexEntity> entry : index.entrySet()) {
			CompressedEntity e = new CompressedEntity();
			if(counter % k == 0){
				int stringIndex=termBuilder.length();
				if(type == CompressType.GAMMA){						
					e.setIndex_in_term_string(Converter.gammaCode(stringIndex));
				}
				if(type == CompressType.DELTA){					
					e.setIndex_in_term_string(Converter.deltaCode(stringIndex));
				}
			}
			if(type == CompressType.GAMMA){						
				e.setDocument_frequency(Converter.gammaCode(entry.getValue().getDocument_frequency()));
			}
			if(type == CompressType.DELTA){					
				e.setDocument_frequency(Converter.deltaCode(entry.getValue().getDocument_frequency()));
			}
			String term = entry.getKey();
			if(counter%k ==0){				
				if(coding == CodingType.BLOCK_CODING){					
					termBuilder.append(createBlockCode(blockBuilder));					
				}
				if(coding == CodingType.FRONT_CODING){					
					//System.out.println(createFrontCode(blockBuilder));	
					termBuilder.append(createFrontCode(blockBuilder));
				}
				blockBuilder = new ArrayList<String>();
			}
			blockBuilder.add(term);	
			
			
			List<DocumentTermEntity> de = new ArrayList<DocumentTermEntity>();
			int previous_doc_id=0;
			for (final Entry<Integer, Integer> tfEntity : entry.getValue().getDocument_term_frequency().entrySet()) {
				DocumentTermEntity tf = new DocumentTermEntity();
				int doc_id=tfEntity.getKey();
				int term_fq = tfEntity.getValue();					
				int docID_diff = doc_id - previous_doc_id;
				previous_doc_id = doc_id;
				if(type == CompressType.GAMMA){					
					tf.setDocument_id_gap(Converter.gammaCode(docID_diff));
					tf.setDoc_term_frequency(Converter.gammaCode(term_fq));					
				}
				if(type == CompressType.DELTA){
					tf.setDocument_id_gap(Converter.deltaCode(docID_diff));
					tf.setDoc_term_frequency(Converter.deltaCode(term_fq));						
				}
				de.add(tf);
			}			
			e.setDoc_term_list(de);
			counter++;
		}
		// to flush the remaining string
		if(coding == CodingType.BLOCK_CODING){
			termBuilder.append(createBlockCode(blockBuilder));					
		}
		if(coding == CodingType.FRONT_CODING){
			termBuilder.append(createFrontCode(blockBuilder));			
		}
		this.termString = termBuilder.toString().getBytes(Charset.forName("UTF-8"));
	}
	private String createBlockCode(List<String> terms){		
		StringBuilder b= new StringBuilder();
		for(final String term : terms){
			b.append(term.length()+term);
		}
		if(b.length() < 3){
			return "";
		}
		return b.toString();
	}
	
	private String createFrontCode(List<String> terms){		
		StringBuilder b= new StringBuilder();
		String commonString = findCommon(terms);
		b.append(commonString.length()+commonString+"*");
		for(final String term : terms){
			String remaining =term.replaceFirst(commonString, "");
			b.append(remaining+remaining.length()+"%");
		}
		if(b.length() < 3){
			return "";
		}
		return b.toString();
	}
	public String findCommon(List<String> terms){		
		StringBuilder c = new StringBuilder();
		if(terms.size()<1) {			
			return "";
		}			
		outerloop:
		for(int i=0;i<terms.get(0).length();i++){
			char ch=terms.get(0).charAt(i);
			for(int j=1;j<terms.size();j++){
				if(i<terms.get(j).length()){
					if(terms.get(j).charAt(i) != ch){
						break outerloop;
					}
				}else{
					break outerloop;
				}
			}
			c.append(ch);
		}
		return c.toString();
	}
	
	public void createCompressedDocumentList(Map<Integer,DocumentEntity> docData,CompressType type){
		List<DocumentDataEntity> docs = new ArrayList<DocumentDataEntity>();
		int previous_doc_id=0;
		for(final Entry<Integer, DocumentEntity> entry : docData.entrySet()){
			int doc_id=entry.getKey();
			int max_fq = entry.getValue().getMax_term_frequency();
			int doc_len = entry.getValue().getDocument_length();
			int docID_diff = doc_id - previous_doc_id;
			previous_doc_id = doc_id;
			
			DocumentDataEntity dataEntity = new DocumentDataEntity();
			if(type == CompressType.GAMMA){
				dataEntity.setDocument_id_gap(Converter.gammaCode(docID_diff));
				dataEntity.setMax_tf(Converter.gammaCode(max_fq));
				dataEntity.setDoc_len(Converter.gammaCode(doc_len));
			}
			if(type == CompressType.DELTA){
				dataEntity.setDocument_id_gap(Converter.deltaCode(docID_diff));
				dataEntity.setMax_tf(Converter.deltaCode(max_fq));
				dataEntity.setDoc_len(Converter.deltaCode(doc_len));
			}			
			docs.add(dataEntity);
		}
		this.compressed_doc_list = docs;				
	}	
}

class CompressedEntity{
	private byte[] index_in_term_string;
	private byte[] document_frequency;
	private List<DocumentTermEntity> doc_term_list;
	
	
	
	public byte[] getDocument_frequency() {
		return document_frequency;
	}
	public void setDocument_frequency(byte[] document_frequency) {
		this.document_frequency = document_frequency;
	}
	public byte[] getIndex_in_term_string() {
		return index_in_term_string;
	}
	public void setIndex_in_term_string(byte[] index_in_term_string) {
		this.index_in_term_string = index_in_term_string;
	}
	public List<DocumentTermEntity> getDoc_term_list() {
		return doc_term_list;
	}
	public void setDoc_term_list(List<DocumentTermEntity> doc_term_list) {
		this.doc_term_list = doc_term_list;
	}
}
class DocumentDataEntity{
	private byte[] document_id_gap;
	private byte[] max_tf;
	private byte[] doc_len;
	public byte[] getDocument_id_gap() {
		return document_id_gap;
	}
	public void setDocument_id_gap(byte[] document_id_gap) {
		this.document_id_gap = document_id_gap;
	}
	public byte[] getMax_tf() {
		return max_tf;
	}
	public void setMax_tf(byte[] max_tf) {
		this.max_tf = max_tf;
	}
	public byte[] getDoc_len() {
		return doc_len;
	}
	public void setDoc_len(byte[] doc_len) {
		this.doc_len = doc_len;
	}	
}

class DocumentTermEntity{
	private byte[] document_id_gap;
	private byte[] doc_term_frequency;
	
	
	public byte[] getDocument_id_gap() {
		return document_id_gap;
	}
	public void setDocument_id_gap(byte[] document_id_gap) {
		this.document_id_gap = document_id_gap;
	}
	public byte[] getDoc_term_frequency() {
		return doc_term_frequency;
	}
	public void setDoc_term_frequency(byte[] doc_term_frequency) {
		this.doc_term_frequency = doc_term_frequency;
	}
	
}