import java.util.Calendar;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class HomeWork2 {
	
	public static String DATA_LOCATION; 
	public static String STOPWORDS_FILE;
	
	public static void main(String[] args) {		
		if(args.length < 1 ){
			DATA_LOCATION = "C:\\Workspace\\Github\\Information Retrieval\\Indexing\\src\\Cranfield\\";
			STOPWORDS_FILE="C:\\Workspace\\Github\\Information Retrieval\\Indexing\\src\\stopwords";
		} else {
			DATA_LOCATION = args[0];
			STOPWORDS_FILE= args[1];
		}
		
		final Set<String> terms = new HashSet<String>();
		terms.add("reynolds");
		terms.add("nasa");
		terms.add("prandtl");
		terms.add("flow");
		terms.add("pressure");
		terms.add("boundary");
		terms.add("shock");
		
		long startTime = Calendar.getInstance().getTimeInMillis();		
		Indexer indexer=new Indexer(DATA_LOCATION,STOPWORDS_FILE);
		indexer.createIndex();
		
		
		
		long endTime = Calendar.getInstance().getTimeInMillis();
		long indexBuild = endTime-startTime;
		System.out.println("*******************************************VERSION 1*******************************************");
		//Display and create file for Version 1 - with lemma of words
		String file_name="Index_Version1.uncompressed";
		//long startWrite = Calendar.getInstance().getTimeInMillis();
		long file_size = ProcessFileOutputUtility.createUncompressedOutputFile(file_name,
				indexer.getLemmaIndex(), indexer.getDocument_meta_map_lema());
		//long endWrite = Calendar.getInstance().getTimeInMillis();
		ProcessOutputUtility.displayIndexResults(indexBuild,0,file_name,
				file_size, indexer.getLemmaIndex().size());
		
		file_name="Index_Version1.compressed";
		startTime = Calendar.getInstance().getTimeInMillis();
		file_size =ProcessFileOutputUtility.buildCompressedIndex(file_name,
				indexer.getLemmaIndex(),indexer.getDocument_meta_map_lema(),CompressType.GAMMA,CodingType.BLOCK_CODING,8);
		endTime = Calendar.getInstance().getTimeInMillis();
		ProcessOutputUtility.displayIndexResults(endTime-startTime,0,file_name,
				file_size, indexer.getLemmaIndex().size());
		
		System.out.println("------------------------------LEMMATIZATION TOKENS------------------------------");	
		Set<String> lemma_terms = ProcessOutputUtility.getLemmas(terms);
		ProcessOutputUtility.displayTERM_DF_TF_List_Length(lemma_terms,indexer.getLemmaIndex());
		System.out.println();
		ProcessOutputUtility.processSingleTerm("nasa",indexer.getLemmaIndex(),indexer.getDocument_meta_map_lema());
		ProcessOutputUtility.displayLargestAndSmallstDFTerms(indexer.getLemmaIndex());
		ProcessOutputUtility.displayDocumentLagestMax_TFandDOCLEN(indexer.getDocument_meta_map_lema());
		System.out.println();
		System.out.println();
		//Display and create file for Version 2 - with stemming
		System.out.println("*******************************************VERSION 2*******************************************");
		file_name="Index_Version2.uncompressed";
		//startWrite = Calendar.getInstance().getTimeInMillis();
		file_size = ProcessFileOutputUtility.createUncompressedOutputFile(file_name,
				indexer.getStemIndex(), indexer.getDocument_meta_map_stem());
		//endWrite = Calendar.getInstance().getTimeInMillis();
		ProcessOutputUtility.displayIndexResults(indexBuild,0,file_name,
				file_size, indexer.getStemIndex().size());
		
		file_name="Index_Version2.compressed";
		startTime = Calendar.getInstance().getTimeInMillis();
		file_size =ProcessFileOutputUtility.buildCompressedIndex(file_name,
				indexer.getStemIndex(),indexer.getDocument_meta_map_stem(),CompressType.DELTA,CodingType.FRONT_CODING,8);
		endTime = Calendar.getInstance().getTimeInMillis();
		ProcessOutputUtility.displayIndexResults(endTime-startTime,0,file_name,
				file_size, indexer.getStemIndex().size());
		
		
		
		System.out.println("------------------------------STEMMING TOKENS------------------------------");
		Set<String> stem_terms = ProcessOutputUtility.getStems(terms);
		ProcessOutputUtility.displayTERM_DF_TF_List_Length(stem_terms,indexer.getStemIndex());
		System.out.println();
		ProcessOutputUtility.processSingleTerm("nasa",indexer.getStemIndex(),indexer.getDocument_meta_map_stem());
		ProcessOutputUtility.displayLargestAndSmallstDFTerms(indexer.getStemIndex());
		ProcessOutputUtility.displayDocumentLagestMax_TFandDOCLEN(indexer.getDocument_meta_map_stem());

	}

}
