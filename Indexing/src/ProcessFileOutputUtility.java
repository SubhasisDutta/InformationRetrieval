import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;


public class ProcessFileOutputUtility {

	public static long createUncompressedOutputFile(String file_name,Map<String,IndexEntity> index,
			Map<Integer,DocumentEntity> docData){
		final File file = new File(file_name);
		int index_size =0;
		try (final PrintWriter writer = new PrintWriter(file, "UTF-8")) {			
			for (final Entry<String, IndexEntity> entry : index.entrySet()) {
				StringBuilder stringBuilder = new StringBuilder();
				index_size += Character.SIZE * entry.getKey().length();
				writer.print(entry.getKey() + ":" + entry.getValue().getDocument_frequency() +
						","+entry.getValue().getTotal_frequency()+
						" [");
				index_size += Integer.SIZE; // for document_frequency
				Map<Integer,Integer> document_term_frequency = entry.getValue().getDocument_term_frequency();
				for (final Entry<Integer, Integer> list : document_term_frequency.entrySet()) {
					stringBuilder.append(list.getKey()+":"+list.getValue()+",");
					index_size += Integer.SIZE * 2; //docID and term frequency
				}
				stringBuilder.deleteCharAt(stringBuilder.length()-1);
				stringBuilder.append("]");
				writer.println(stringBuilder.toString());
			}
			writer.println("XX--SEPERATOR--XX");			
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("[");
			for(final Entry<Integer, DocumentEntity> entry : docData.entrySet()){				
				stringBuilder.append(entry.getKey()+":"+entry.getValue().getMax_term_frequency()+
						","+entry.getValue().getMax_term()+
						","+entry.getValue().getDocument_length()+"|");
				index_size += Integer.SIZE * 3; //docID and max_tf and doclen
			}
			stringBuilder.deleteCharAt(stringBuilder.length()-1);
			stringBuilder.append("]");
			writer.println(stringBuilder.toString());
			
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("Uncompressed Index Size                   :"+index_size+" bytes");
		return file.length();
	}
	
	public static long buildCompressedIndex(String file_name,Map<String,IndexEntity> index,
			Map<Integer,DocumentEntity> docData,CompressType type,CodingType coding,int k){		
		CompressedIndex comp_index = new CompressedIndex();
		comp_index.createCompressedIndex(index,type,coding,k);
		comp_index.createCompressedDocumentList(docData,type);
		int index_size =0;
		try{
			final FileOutputStream fos = new FileOutputStream(file_name);			
			fos.write(comp_index.getTermString());
			index_size += comp_index.getTermString().length;
			//fos.write("\n".getBytes());
			for(final CompressedEntity e : comp_index.getCompressedIndex() ){
				fos.write(e.getIndex_in_term_string());
				index_size += e.getIndex_in_term_string().length;
				//fos.write(":".getBytes());
				fos.write(e.getDocument_frequency());
				index_size += e.getDocument_frequency().length;
				//fos.write("[".getBytes());
				for(final DocumentTermEntity d : e.getDoc_term_list()){
					fos.write(d.getDocument_id_gap());	
					index_size += d.getDocument_id_gap().length;
					//fos.write(",".getBytes());
					fos.write(d.getDoc_term_frequency());
					index_size += d.getDoc_term_frequency().length;
				}
				//fos.write("]".getBytes());
				
			}			
			//fos.write("[".getBytes());			
			for(final DocumentDataEntity d: comp_index.getCompressed_doc_list()){
				fos.write(d.getDocument_id_gap());
				index_size += d.getDocument_id_gap().length;
				//fos.write(":".getBytes());
				fos.write(d.getMax_tf());
				index_size += d.getMax_tf().length;
				//fos.write(",".getBytes());
				fos.write(d.getDoc_len());
				index_size += d.getDoc_len().length;
				//fos.write("|".getBytes());
			}
			//fos.write("]".getBytes());
			fos.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		final File file = new File(file_name);
		System.out.println("Compressed Index Size                     :"+index_size+" bytes");
		return file.length();
	}	
}
