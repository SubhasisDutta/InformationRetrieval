package ir.mainexam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GlobalAnalysis {
	public static void main(String[] args) throws IOException {
		HashMap<Integer, Integer> wordCount = new HashMap<Integer, Integer>();
		HashMap<String, WordParameter> wordParams = new HashMap<String, WordParameter>();
		HashMap<Integer, Double> itf = new HashMap<Integer, Double>();
		
		int i = 0;
		String[] input= {"Adobe released emergency update Flash Player after security researchers discovered bug allows attackers take over crash users machines",
				"first time Adobe software targeted worst attack came hackers managed access personal data nearly million customers"};
		for (int p=0;p<2;p++) {	
			String[] d = input[p].split("\\W+");			
			WordParameter wp = null;
			ArrayList<String> words = new ArrayList<String>();
			int wc = 0;
			for (String u : d) {
				if (!words.contains(u)) {
					wc += 1;
					words.add(u);
				}
				if (wordParams.containsKey(u)) {
					wp = wordParams.get(u);
					wp.termFrequency[i]++;
					wordParams.put(u, wp);
				} else {
					wp = new WordParameter();
					wp.termFrequency = new int[2];
					wp.termFrequency[i] = 1;
					wordParams.put(u, wp);
				}
			}
			wordCount.put(i, wc);
			i++;
		}
		int totalWordCount = wordParams.size();
		for (int docId : wordCount.keySet()) {
			double result = ((double) totalWordCount) / wordCount.get(docId);			
			itf.put(docId, Math.log(result));
		}
		for (String term : wordParams.keySet()) {
			WordParameter wp = wordParams.get(term);
			int[] tf = wp.termFrequency;
			for (i = 0; i < tf.length; i++) {
				if (tf[i] > wp.mostFrequentDoc)
					wp.mostFrequentDoc = tf[i];
			}
			for (i = 0; i < 2; i++) {
				double vector = 0, temp = 0;
				for (int j = 0; j < 2; j++) {
					temp += Math.pow((0.5 + 0.5 * ((double) tf[j] / wp.mostFrequentDoc)), 2) * Math.pow(itf.get(j), 2);
				}
				vector = (0.5 + 0.5 * ((double) tf[i] / wp.mostFrequentDoc)) * itf.get(i) / (Math.sqrt(temp));
				wp.termVector.add(vector);
			}
		}
		DecimalFormat df = new DecimalFormat("#.####");
		for (String s : wordParams.keySet()) {
			System.out.print(s + ", ");
			for (int j = 0; j < wordParams.get(s).termVector.size(); j++) {
				System.out.print(df.format(wordParams.get(s).termVector.get(j)) + ", ");
			}
			System.out.println();
		}
		// For query weights
		HashMap<String, WordParameter> queryVector = new HashMap<String, WordParameter>();
		String queryWords = "Adobe security";
		String[] d = queryWords.replaceAll("[\\W&&[^\\s]]", "").split("\\W+");
		WordParameter wp = null;
		i = 0;
		for (String u : d) {
			if (queryVector.containsKey(u)) {
				wp = queryVector.get(u);
				wp.termFrequency[i]++;
				queryVector.put(u, wp);
			} else {
				wp = new WordParameter();
				wp.termFrequency = new int[1];
				wp.termFrequency[i] = 1;
				queryVector.put(u, wp);
			}
		}
		ArrayList<Double> qTermVec = new ArrayList<Double>();
		System.out.println("Query vectors:");
		for (String term : queryVector.keySet()) {
			System.out.print(term+" ");
			wp = queryVector.get(term);
			int[] tf = wp.termFrequency;
			double weight = 0, temp = 0;
			int[] tfd = wordParams.get(term).termFrequency;
			int freqDoc = wordParams.get(term).mostFrequentDoc;
			ArrayList<Double> docTermVector = wordParams.get(term).termVector;
			for (int j = 0; j < 2; j++) {
				temp += Math.pow((0.5 + 0.5 * ((double) tfd[j] / freqDoc)), 2) * Math.pow(itf.get(j), 2);
			}
			weight = (0.5 + 0.5 * ((double) tf[0] / freqDoc)) * ((double) totalWordCount / queryVector.size())/ (Math.sqrt(temp));
			for (int j = 0; j < docTermVector.size(); j++) {
				System.out.print(df.format(docTermVector.get(j) * weight) + ",");
				if (qTermVec.size() == docTermVector.size()) {
					qTermVec.set(j, qTermVec.get(j) + docTermVector.get(j) * weight);
				} else {
					qTermVec.add(docTermVector.get(j) * weight);
				}
			}
			System.out.println();
		}
		HashMap<String, Double> similarity = new HashMap<String, Double>();
		for (String term : wordParams.keySet()) {
			ArrayList<Double> termVec = wordParams.get(term).termVector;
			double simVal = 0;
			for (int j = 0; j < termVec.size(); j++) {
				simVal += termVec.get(j) * qTermVec.get(j);
			}
			similarity.put(term, simVal);
		}
		for (String term : similarity.keySet()){
			System.out.println(term + "=" + df.format(similarity.get(term)) + ", ");
		}
	}
}

class WordParameter {
	ArrayList<Double> termVector = new ArrayList<Double>();
	int[] termFrequency;
	int mostFrequentDoc;
}