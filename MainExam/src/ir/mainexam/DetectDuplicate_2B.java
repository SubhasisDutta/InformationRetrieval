package ir.mainexam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DetectDuplicate_2B {
	
	
	public static int getRandom(int[] array) {
	    int rnd = new Random().nextInt(array.length);
	    return array[rnd];
	}
	
	public static void findJCWithSketchVectors(int[] W1,int[] W2){
		int[] skerch1 = new int[25];
		int[] sketch2 = new int[25];
		for(int i=0;i<25;i++){
			skerch1[i]=getRandom(W1);
			sketch2[i]=getRandom(W2);
			System.out.println(skerch1[i]+" "+sketch2[i]);
		}
		System.out.println();
		int equalCount=0;
		for(int i=0;i<25;i++){
			if(skerch1[i]==sketch2[i]){
				equalCount++;
			}
		}
		for(int i=0;i<25;i++){
			System.out.print(skerch1[i]+", ");
		}
		System.out.println();
		for(int i=0;i<25;i++){
			System.out.print(sketch2[i]+", ");
		}
		System.out.println();
		double prob = (double)equalCount/25;
		System.out.println(prob);
		/*
		 *  
		    2 9
			1 10
			3 5
			1 7
			5 5
			3 10
			6 11
			3 1
			1 9
			5 5
			6 10
			6 10
			5 6
			4 11
			2 7
			3 8
			3 1
			3 9
			4 9
			2 7
			2 6
			3 11
			6 6
			4 5
			1 7
			Sumilarity = 0.12
		 * 
		 */
	}
	
	
	public static void main(String[] args){
		String W1= "the class will cover link analysis";
		String W2= "link analysis techniques shall be presented in the class";
		/*String[] shingles1 = W1.split(" ");
		String[] shingles2 = W2.split(" ");
		
		Set<String> union = new HashSet<String>();
		union.addAll(Arrays.asList(shingles1));
		union.addAll(Arrays.asList(shingles2));
		String[] universe = (String[])union.toArray();*/
		int[] h_1 = {1,2,3,4,5,6};
		int[] h_2 = {5,6,7,8,9,10,11,1,2};
		
		findJCWithSketchVectors(h_1,h_2);
		
	}
}
