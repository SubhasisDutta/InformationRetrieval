package com.subhasis.devisbase.utils;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map.Entry;

public class IndexFileHandler {
	public static void indexSample() {
		// TODO Auto-generated method stub
		System.out.println("Index Example as TreeMap");
		System.out.println("TreeMap will keep keys in sorted order.");

		// WARNING: IndexFileHandler.java uses unchecked or unsafe operations.
		/* TreeMap columnIndex will represent an entire index as a TreeMap 
		 * This example uses Object type as the key. This allows indexing of
		 * Integer, String, Date, etc. using the same method.
		 * Alternatively, a safer implementation would create separate handler
		 * for each key data type.
		 */
		TreeMap<Object, ArrayList<Long>> columnIndex = new TreeMap<Object, ArrayList<Long>>();
		/* The ArrayList tableAddresses will potentially hold more than one
		 * table address if the index is on a non-unique attribute of the table
		 */
		ArrayList<Long> tableAddresses = null;

		/* This section populates the TreeMap 
		 * This example shows a manual population. Your TreeMap index will
		 * read your .ndx file to populate the TreeMap.
		 */
		tableAddresses = new ArrayList<Long>();
		tableAddresses.add((long)12);
		columnIndex.put(2, tableAddresses);

		tableAddresses = new ArrayList<Long>();
		tableAddresses.add((long)0);
		columnIndex.put(1, tableAddresses);

		tableAddresses = new ArrayList<Long>();
		tableAddresses.add((long)51);
		columnIndex.put(5, tableAddresses);

		tableAddresses = new ArrayList<Long>();
		tableAddresses.add((long)25);
		tableAddresses.add((long)65);
		tableAddresses.add((long)78);
		columnIndex.put(3, tableAddresses);

		tableAddresses = new ArrayList<Long>();
		tableAddresses.add((long)39);
		columnIndex.put(4, tableAddresses);

		
		/* Index entries can be modified, inserted, or deleted */
		
		
		/* This section displays the contents of the index to STDOUT.
		 * Notice that the keys are displayed in sorted order.
		 * If you use this TreeMap strategy, your implementation
		 * can use a similar iteration to overwrite an updated .ndx file
		 * */
		for(Entry<Object,ArrayList<Long>> entry : columnIndex.entrySet()) {
			Object key = entry.getKey();         // Get the index key
			System.out.print(key + " => ");       // Display the index key
			ArrayList<Long> value = entry.getValue();   // Get the list of record addresses
			System.out.print("[" + value.get(0)); // Display the first address
			for(int i=1; i < value.size();i++) {  // Check for and display additional addresses for non-unique indexes
				System.out.print("," + value.get(i));
			}
			System.out.println("]");
		}
	}
}
