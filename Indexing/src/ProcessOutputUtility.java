import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ProcessOutputUtility {
	public static StanfordLemmatizer lemmatizer = new StanfordLemmatizer();
	
	public static void displayIndexResults(long time_taken,long time_write,String file_name,
			long file_size,int index_size){
		//System.out.println("Size of "+file_name+"       : "+file_size+" bytes.");
		System.out.println("Time Taken to build Index                 : "+time_taken+" milliSeconds");
		//System.out.println("Time Taken to write Index                 : "+time_write+" milliSeconds");
		System.out.println("Number of inverted list in Index          : "+index_size);
		System.out.println();
		System.out.println();
	}
	
	public static void displayTERM_DF_TF_List_Length(Set<String> terms,Map<String,IndexEntity> index){
		System.out.println("FOR :Reynolds, NASA, Prandtl, flow, pressure, boundary, shock");
		System.out.println("TERM                TF             DF              Size of inverted list");
		for(final String term: terms){
			long size =0;
			if(index.containsKey(term)){
				IndexEntity entity = index.get(term);
				size += Character.SIZE * term.length();
				size += Integer.SIZE * 2; // for document frequency and total term frequency
				
				for(final Entry<Integer,Integer> entry : entity.getDocument_term_frequency().entrySet()){
					size += Integer.SIZE * 2; // document ID and document term frequency
				}
				
				System.out.println(term + "            "+ entity.getTotal_frequency()+
						"          "+entity.getDocument_frequency()+
						"          "+size+" bytes");
			}else{
				System.out.println(term+ "Not Found in Index.");
			}
		}
	}
	
	public static Set<String> getLemmas(Set<String> terms){
		Set<String> lemmas = new HashSet<String>();
		for(String s: terms){
			List<String> lemas = lemmatizer.lemmatize(s);
			for(String l: lemas){
				lemmas.add(l);
			}
		}
		return lemmas;
	}
	public static Set<String> getStems(Set<String> terms){
		Set<String> stems = new HashSet<String>();
		for(String s: terms){
			Stemmer stemmer=new Stemmer();
			stemmer.add(s.toCharArray(), s.length());
			stemmer.stem();
			stems.add(stemmer.toString());
		}
		return stems;
	}
	
	public static void processSingleTerm(String term,Map<String,IndexEntity> index,Map<Integer,DocumentEntity> docData){
		System.out.println("Information for term : "+ term);
		System.out.println("Total Term Frequency : "+ index.get(term).getTotal_frequency());
		System.out.println("DOCUMENT FREQUENCY   : "+ index.get(term).getDocument_frequency());
		System.out.println("First Three Documents");
		System.out.println("Document ID      Term Frequency      Max-Term-Freq   Document Length");
		int count =0;
		for(final Entry<Integer,Integer> entry : index.get(term).getDocument_term_frequency().entrySet()){
			System.out.print(entry.getKey()+"                   "+entry.getValue()+"                    ");
			System.out.println(docData.get(entry.getKey()).getMax_term_frequency()+"                    "
							+docData.get(entry.getKey()).getDocument_length());
			count++;
			if(count>2)break;
		}
	}
	public static void displayLargestAndSmallstDFTerms(Map<String,IndexEntity> index){
		int max=0;
		int min=9999;
		List<String> maxTerms = new ArrayList<String>();
		List<String> minTerms = new ArrayList<String>();
		for(final Entry<String,IndexEntity> entry : index.entrySet()){
			if(entry.getValue().getDocument_frequency()>max){
				max = entry.getValue().getDocument_frequency();
				maxTerms = new ArrayList<String>();
				maxTerms.add(entry.getKey());				
			}else if(entry.getValue().getDocument_frequency() == max){
				maxTerms.add(entry.getKey());
			}
			if(entry.getValue().getDocument_frequency()<min){
				min = entry.getValue().getDocument_frequency();
				minTerms = new ArrayList<String>();
				minTerms.add(entry.getKey());				
			}else if(entry.getValue().getDocument_frequency() == min){
				minTerms.add(entry.getKey());
			}
		}
		System.out.println("The maximum Document Frequency is : "+max);
		System.out.println("The tems with the maximum DF      : "+maxTerms);
		System.out.println("The minimum Document Frequency is : "+min);
		System.out.println("The tems with the minimum DF      : "+minTerms);		
	}
	public static void displayDocumentLagestMax_TFandDOCLEN(Map<Integer,DocumentEntity> docData){
		int max_tf=0;
		int max_doclen =0;
		String max_tf_term="";
		List<Integer> max_tfDocID=new ArrayList<Integer>();
		List<Integer> max_docLenDocID = new ArrayList<Integer>();
		
		for(final Entry<Integer,DocumentEntity> entry : docData.entrySet()){
			if(entry.getValue().getMax_term_frequency()>max_tf){
				max_tf = entry.getValue().getMax_term_frequency();
				max_tfDocID = new ArrayList<Integer>();
				max_tfDocID.add(entry.getKey());
				max_tf_term = entry.getValue().getMax_term();
			}else if(entry.getValue().getMax_term_frequency() == max_tf){
				max_tfDocID.add(entry.getKey());
			}
			
			if(entry.getValue().getDocument_length()>max_doclen){
				max_doclen = entry.getValue().getDocument_length();
				max_docLenDocID = new ArrayList<Integer>();
				max_docLenDocID.add(entry.getKey());				
			}else if(entry.getValue().getDocument_length() == max_doclen){
				max_docLenDocID.add(entry.getKey());
			}
		}
		System.out.println("Document with largest max_tf : "+max_tfDocID+" it has "+max_tf_term+
				" which is repeated "+max_tf+" times.");
		System.out.println("Document with largest doclen : "+max_docLenDocID+" it has doclen "+max_doclen+" .");
	}
}
