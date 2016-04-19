import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.Map.Entry;


public class DisplayUtility {

	
	public static void showTopFive(Map<Integer, Double> scoreTable,Map<Integer,VectorEntity> documents , String type) {
		
		class ValueComparator implements Comparator<Entry<Integer, Double>> {
			@Override
			public int compare(Entry<Integer, Double> o1, Entry<Integer, Double> o2) {
				if(o1.getValue() < o2.getValue()){
					return 1;
				}	
			   return -1;
			}
		}
		
		TreeSet<Entry<Integer, Double>> sortedSet = new TreeSet<Entry<Integer, Double>>(new ValueComparator());
		sortedSet.addAll(scoreTable.entrySet());		
		Iterator<Entry<Integer, Double>> iterator = sortedSet.iterator();
		
		System.out.println("Rank" + "\t Score          " + "\t Doc. Identifier" + " \t " + "Document Title");
		for(int i = 0 ; i < 5 && iterator.hasNext(); i++){
			Entry<Integer, Double> entry = iterator.next();		
			int docID = entry.getKey();				
			System.out.println((i+1) + "\t" + entry.getValue() + "\t" + 
					documents.get(docID).getDocumentName() + "\t" + documents.get(docID).getDocumentTitle());
			/*System.out.println();
			System.out.println("Vector Representation of Document "+ documents.get(docID).getDocumentName());
			if(type.equals("W1")){
				System.out.println(documents.get(docID).getWeight1().toString());
			}else{
				System.out.println(documents.get(docID).getWeight2().toString());
			}
			System.out.println("Normalized Vector Representation of Document:  "+ documents.get(docID).getDocumentName());
			if(type.equals("W1")){
				System.out.println(documents.get(docID).getWeight1_normalized().toString());
			}else{
				System.out.println(documents.get(docID).getWeight2_normalized().toString());
			}	*/
			//System.out.println();	
		}	
		System.out.println();		
		
	}
	
	
}
