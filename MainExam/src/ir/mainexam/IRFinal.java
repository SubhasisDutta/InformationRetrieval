package ir.mainexam;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;


public class IRFinal
{
	public static String COLLECTION_LOCATION ="C:\\Workspace\\Github\\Information Retrieval\\MainExam\\docs";
   public static void main(String[]args) throws IOException
    {
        
        ArrayList<String>[] documents = new ArrayList[10];
        
        ArrayList<String>[] documentTerms = new ArrayList[10];
        HashMap<String, Integer> stopwords = new HashMap<String, Integer>();
        HashMap<String, Integer> globalVocab = new HashMap<String,Integer>();
        ArrayList<String> filePaths = new ArrayList<String>();
        
        Scanner scanner = new Scanner(new File("stopwords"));
        while(scanner.hasNextLine()){            
            stopwords.put(scanner.nextLine().toLowerCase(), 1);
        }
    	
        
        File collection = new File(COLLECTION_LOCATION);
        if(collection.isDirectory()){
            try{
			    Files.walk(Paths.get(COLLECTION_LOCATION)).forEach(filePath -> {		    
				    if(Files.isRegularFile(filePath)){
				    	filePaths.add(filePath+"");
				    }
				});
			}catch(IOException e){
				System.out.println(e);
			}
        }
	   
        
        for(int i = 0; i <filePaths.size(); i++){
            
            documents[i]= new ArrayList<String>();
            documentTerms[i] = new ArrayList<String>();
            
            scanner = new Scanner(new File(filePaths.get(i)));
            
            while(scanner.hasNext()){
                StringTokenizer tokens = new StringTokenizer(scanner.nextLine());
                while (tokens.hasMoreTokens()) {
                            String nextToken = tokens.nextToken().toLowerCase().replaceAll("[\\W&&[^\\s]]","");
                            
                            documents[i].add(nextToken); //keep this for doc length later
                            if(stopwords.containsKey(nextToken)){
                               
                            }else{
                                if(globalVocab.containsKey(nextToken)){
                                    globalVocab.put(nextToken, globalVocab.get(nextToken)+1);
                                    documentTerms[i].add(nextToken);
                                }else{
                                    globalVocab.put(nextToken, 1);
                                    documentTerms[i].add(nextToken);                                    
                                }                                
                            }
                }
            
            
            }
        }
        
        ArrayList<double[]> documentVectors= new ArrayList<double[]>();
        
        Map<String, Integer> sortedMap = new TreeMap<String, Integer>(globalVocab);
        
        
        for(int i =0; i < 10;i++){
            ArrayList<String> s = documentTerms[i];
            double[] d = new double[globalVocab.size()];
            int j=0;
            for(String key: sortedMap.keySet()){
                d[j] = tf(s,key) * idf(s,key);
            
                if(d[j]!=d[j])//check if NaN
                    d[j]=0;
                       
                
                 j++;
            
            }
        documentVectors.add(d);
        }
        
        //KMeansClusters(documentVectors, filePaths, globalVocab.size());
        
        //singleLinkClustering(documentVectors,filePaths,globalVocab.size());
        completeLinkClustering(documentVectors,filePaths,globalVocab.size());
        //printGroupAverageClusters(documentVectors, filePaths, globalVocab.size());
    }
   
   
   
   public static void groupAverageClusters(ArrayList<double[]> documentVectors,ArrayList<String> filePaths, int globalVocabSize){
	   int D = documentVectors.size(); 
       ArrayList<double[]> clusterCentroids = new ArrayList<double[]>();
       int[][] documentClusterAssignment = new int[D][D];       
       for(int i=0; i < documentVectors.size(); i++){ 
           clusterCentroids.add(documentVectors.get(i));
           documentClusterAssignment[i][i] = 1; 
       }       
       boolean done = false;
       ArrayList<Integer> deletedClusters = new ArrayList<Integer>();
       while(!done){          
           for(int i=0; i<D; i++){              
               boolean deleted = false;
               for(int a=0; a<deletedClusters.size(); a++){
	               if(deletedClusters.get(a)==i){
	                   deleted = true;
	               }
               }               
               if(!deleted){
	               double minSim = Double.MAX_VALUE;
	               int clusterToMerge = 0;
	               for(int j = 0; j<D; j++){
	                   
	                   if(!(i==j)){ 
	                       double sim = cosSim(clusterCentroids.get(i), clusterCentroids.get(j));
	                       
	                       if(sim<minSim){
	                           minSim = sim;
	                           clusterToMerge =j;
	                       }
	                       
	                   }
	               }
	               deletedClusters.add(clusterToMerge);	               
	               for(int j=0; j < D; j++){
	                   if(documentClusterAssignment[clusterToMerge][j]==1){ 
	                       documentClusterAssignment[clusterToMerge][j] =0; 	                       
	                       documentClusterAssignment[i][j] = 1; 
	                   }	                   
	               }
	               for(int l=0; l<globalVocabSize;l++){
	                   clusterCentroids.get(i)[l]=0.0;
	               }	               
	               ArrayList<Integer> kDocuments = new ArrayList<Integer>();	               
	               for(int l=0; l<D; l++){ 
	                   if(documentClusterAssignment[i][l]==1){
	                       kDocuments.add(l);
	                   }
	               }	               
	               for(int l =0; l<kDocuments.size(); l++){	                   
	                   double[] documentVector = documentVectors.get(kDocuments.get(l));	                  
	                   for(int p= 0 ; p<documentVector.length; p++){
	                       clusterCentroids.get(i)[p]+= documentVector[p];	                       
	                   }	                   
	                   for(int p= 0 ; p<documentVector.length; p++){
	                       clusterCentroids.get(i)[p] = clusterCentroids.get(i)[p]/kDocuments.size();	                       
	                   }
	               }
           }           
           if(deletedClusters.size()>10)
               done=true;           
           }           
       }     
       for(int i=0; i < D; i++){
           System.out.print("Cluster "+(i+1)+": { ");
           for(int j=0; j<D; j++){
               if(documentClusterAssignment[i][j]==1){                   
                   String docName = filePaths.get(j).replace(COLLECTION_LOCATION+"\\","").replace(".txt", "");                 
                   System.out.print(docName+", ");
               } 
           }           
           System.out.print("}\nCentroid Vector: [ ");
           String centroidVector ="";
            for(int j=0; j<globalVocabSize; j++){                
                centroidVector+= (Math.round (clusterCentroids.get(i)[j] * 10000.0) / 10000.0)+", ";                   
            }           
            centroidVector = centroidVector.substring(0, centroidVector.lastIndexOf(","));           
           System.out.println(centroidVector+" ]\n");
       }       
   }
   
   public static void completeLinkClustering(ArrayList<double[]> documentVectors,ArrayList<String> filePaths, int globalVocabSize){       
       int D = documentVectors.size(); 
       ArrayList<double[]> clusterCentroids = new ArrayList<double[]>();
       int[][] documentClusterAssignment = new int[D][D];       
       for(int i=0; i < documentVectors.size(); i++){ 
           clusterCentroids.add(documentVectors.get(i));
           documentClusterAssignment[i][i] = 1; 
       }       
       boolean done = false;
       ArrayList<Integer> deletedClusters = new ArrayList<Integer>();
       while(!done){          
           for(int i=0; i<D; i++){              
               boolean deleted = false;
               for(int a=0; a<deletedClusters.size(); a++){
	               if(deletedClusters.get(a)==i){
	                   deleted = true;
	               }
               }               
               if(!deleted){
	               double minSim = Double.MAX_VALUE;
	               int clusterToMerge = 0;
	               for(int j = 0; j<D; j++){
	                   
	                   if(!(i==j)){ 
	                       double sim = cosSim(clusterCentroids.get(i), clusterCentroids.get(j));
	                       
	                       if(sim<minSim){
	                           minSim = sim;
	                           clusterToMerge =j;
	                       }
	                       
	                   }
	               }
	               deletedClusters.add(clusterToMerge);	               
	               for(int j=0; j < D; j++){
	                   if(documentClusterAssignment[clusterToMerge][j]==1){ 
	                       documentClusterAssignment[clusterToMerge][j] =0; 	                       
	                       documentClusterAssignment[i][j] = 1; 
	                   }	                   
	               }
	               for(int l=0; l<globalVocabSize;l++){
	                   clusterCentroids.get(i)[l]=0.0;
	               }	               
	               ArrayList<Integer> kDocuments = new ArrayList<Integer>();	               
	               for(int l=0; l<D; l++){ 
	                   if(documentClusterAssignment[i][l]==1){
	                       kDocuments.add(l);
	                   }
	               }	               
	               for(int l =0; l<kDocuments.size(); l++){	                   
	                   double[] documentVector = documentVectors.get(kDocuments.get(l));	                  
	                   for(int p= 0 ; p<documentVector.length; p++){
	                       clusterCentroids.get(i)[p]+= documentVector[p];	                       
	                   }	                   
	                   for(int p= 0 ; p<documentVector.length; p++){
	                       clusterCentroids.get(i)[p] = clusterCentroids.get(i)[p]/kDocuments.size();	                       
	                   }
	               }
           }           
           if(deletedClusters.size()>10)
               done=true;           
           }           
       }     
       for(int i=0; i < D; i++){
           System.out.print("Cluster "+(i+1)+": { ");
           for(int j=0; j<D; j++){
               if(documentClusterAssignment[i][j]==1){                   
                   String docName = filePaths.get(j).replace(COLLECTION_LOCATION+"\\","").replace(".txt", "");                 
                   System.out.print(docName+", ");
               } 
           }           
           System.out.print("}\nCentroid Vector: [ ");
           String centroidVector ="";
            for(int j=0; j<globalVocabSize; j++){                
                centroidVector+= (Math.round (clusterCentroids.get(i)[j] * 10000.0) / 10000.0)+", ";                   
            }           
            centroidVector = centroidVector.substring(0, centroidVector.lastIndexOf(","));           
           System.out.println(centroidVector+" ]\n");
       }
   }
   
   
   public static void singleLinkClustering(ArrayList<double[]> dv,ArrayList<String> fp, int size){       
       int D = dv.size(); 
       List<double[]> cc = new ArrayList<double[]>();
       int[][] da = new int[D][D];       
       for(int i=0; i < dv.size(); i++){ 
    	   cc.add(dv.get(i));
    	   da[i][i] = 1; 
       }       
       boolean done = false;
       List<Integer> dc = new ArrayList<Integer>();
       while(!done){            
           for(int i=0; i<D; i++){             
               if(dc.contains(i))
                   continue;               
               double maxSim = Double.MIN_VALUE;
               int cm = 0;
               for(int j = 0; j<D; j++){                   
                   if(!(i==j)){ 
                       double sim = cosSim(cc.get(i), cc.get(j));                       
                       if(sim>maxSim){
                           maxSim = sim;
                           cm =j;
                       }                       
                   }
               }
               dc.add(cm);               
               for(int j=0; j < D; j++){
                   if(da[cm][j]==1){ 
                	   da[cm][j] =0;                    
                	   da[i][j] = 1; 
                   }                   
               }
               for(int l=0; l<size;l++){
            	   cc.get(i)[l]=0.0;
               }               
               ArrayList<Integer> kd = new ArrayList<Integer>();               
               for(int l=0; l<D; l++){ 
                   if(da[i][l]==1){
                	   kd.add(l);
                   }
               }               
               for(int l =0; l<kd.size(); l++){
                   double[] dV = dv.get(kd.get(l));                   
                   for(int p= 0 ; p<dV.length; p++){
                	   cc.get(i)[p]+= dV[p];                       
                   }                   
                   for(int p= 0 ; p<dV.length; p++){
                	   cc.get(i)[p] = cc.get(i)[p]/kd.size();                       
                   }
               } 
           }           
           if(dc.size()>6)
               done=true;
       } 
       for(int i=0; i < D; i++){
           System.out.print("Cluster "+(i+1)+": { ");
           for(int j=0; j<D; j++){
               if(da[i][j]==1){                   
                   String docName = fp.get(j).replace(COLLECTION_LOCATION+"\\","").replace(".txt", "");                                    
                   System.out.print(docName+", ");
               } 
           }           
           System.out.print("}\nCentroid Vector: [ ");
           String cvs ="";
            for(int j=0; j<size; j++){                
            	cvs+= (Math.round (cc.get(i)[j] * 10000.0) / 10000.0)+", ";                   
            }           
            cvs = cvs.substring(0, cvs.lastIndexOf(","));           
           System.out.println(cvs+" ]\n");
       }
   }
   
   
   public static void KMeansClusters(ArrayList<double[]> docV,ArrayList<String> files, int vsize){
       int K = 3; 
       int D = 10; 
       double[][] centroid = new double[K][vsize];
       int[][] clusters = new int[K][D];
       Random ran = new Random();
       for(int i=0; i< K; i++){
           int docNumber= ran.nextInt(10);           
           for(int j =0; j<docV.get(docNumber).length; j++){               
        	   centroid[i][j] = docV.get(docNumber)[j];
           }
       }
       for(int m =0; m<800; m++){           
           for(int i=0; i< docV.size(); i++){
               double cosineSim = Double.MIN_VALUE;
               int clusterAssignment = 0;
               for(int j=0; j<K; j++){                    
                   double [] tempCentroidVector = new double[vsize];                   
                   for(int y= 0 ; y<vsize; y++)
                        tempCentroidVector[y] = centroid[j][y];                   
                   double temp = cosSim(tempCentroidVector,docV.get(i) );                    
                   if(temp > cosineSim){
                       cosineSim = temp;
                       clusterAssignment = j;
                   }                   
               }               
               for(int j=0; j<K; j++){                   
                   if(j==clusterAssignment)
                       clusters[j][i]=1;
                   else
                       clusters[j][i]=0;                   
               }
           }
           for(int j=0; j<K;j++){
               for(int l=0; l<D;l++){
            	   centroid[j][l]=0.0;
               }
           }
           for(int j=0; j<K; j++){               
               ArrayList<Integer> kDocuments = new ArrayList<Integer>();               
               for(int l=0; l<D; l++){ 
                   if(clusters[j][l]==1){
                       kDocuments.add(l);
                   }
               }               
               for(int l =0; l<kDocuments.size(); l++){                   
                   double[] documentVector = docV.get(kDocuments.get(l));                   
                   for(int p= 0 ; p<documentVector.length; p++){
                	   centroid[j][p]+= documentVector[p];                       
                   }                   
                   for(int p= 0 ; p<documentVector.length; p++){
                	   centroid[j][p] = centroid[j][p]/kDocuments.size();                       
                   }
               }
           }
           for(int i=0; i < K; i++){
        	   System.out.print("Cluster "+(i+1)+": { ");
               for(int j=0; j<D; j++){
                   if(clusters[i][j]==1){                   
                       String docName = files.get(j).replace(COLLECTION_LOCATION+"\\","").replace(".txt", "");                    
                       System.out.print(docName+", ");
                   }
               }
               System.out.print("}\n");
           }      
           System.out.println();
       }       
       for(int i=0; i < K; i++){
           System.out.print("Cluster "+(i+1)+": { ");
           for(int j=0; j<D; j++){
               if(clusters[i][j]==1){                   
                   String docName = files.get(j).replace(COLLECTION_LOCATION+"\\","").replace(".txt", "");                    
                   System.out.print(docName+", ");
               }
           }           
           System.out.print("}\nCentroid Vector: { ");
           String centroidStr ="";
            for(int j=0; j<vsize; j++){
            	centroidStr+= (Math.round (centroid[i][j] * 10000.0) / 10000.0) +", ";
            }           
            centroidStr = centroidStr.substring(0, centroidStr.lastIndexOf(","));           
           System.out.println(centroidStr+" }\n");
       } 
   }
   
   static double tf(ArrayList<String> doc, String term){
    	double n = 0;
    	for(int i=0; i<doc.size(); i++){
            String s = doc.get(i);
    		if(s.compareTo(term)==0)
    			n++;
        }
    	return n/doc.size();
    }
   
   
   static  double idf(ArrayList<String> doc, String term){
        
        
    	double n = 0;
    	for(int i=0; i<doc.size(); i++){
            String s = doc.get(i);
    			if(s.compareTo(term)==0){
    				n++;
    				break;
    			}
    	}
    	return Math.log(doc.size()/n);
    }
   
   static double cosSim(double[] a, double[] b){
    	double dotp=0, maga=0, magb=0;
    	for(int i=0;i<a.length;i++){
    		dotp+=a[i]*b[i];
    		maga+=Math.pow(a[i],2);
    		magb+=Math.pow(b[i],2);
    	}
    	maga = Math.sqrt(maga);
    	magb = Math.sqrt(magb);
    	double d = dotp / (maga * magb);
        
    	return d==Double.NaN?0:d;
    }
   
   
   
}
/*
           
   
   //read in documents from all .txt files in same folder as kmeans.java
    	//save parallel lists of documents (String[]) and their filenames, and create global set list of words
        ArrayList<String[]> docs = new ArrayList<String[]>();
        ArrayList<String> filenames = new ArrayList<String>();
        ArrayList<String> global = new ArrayList<String>();
        ArrayList<String> stopWords = new ArrayList<String>();
        
        Scanner scanner = new Scanner(new File("stopwords"));
        
        while(scanner.hasNextLine()){
            
            stopWords.add(scanner.nextLine());
        }
        
        
        File folder = new File("/Users/Matthew_Bachelder/Documents/Development/IRFinal/documents");
	    List<File> files = Arrays.asList(folder.listFiles(new FileFilter() {
	        public boolean accept(File f) {
                    
	            return f.isFile() && f.getName().endsWith(".txt");
	        }
	    }));
	    BufferedReader in = null;
	    for(File f:files){
	    	in = new BufferedReader(new FileReader(f));
	    	StringBuffer sb = new StringBuffer();
	    	String s = null;
	    	while((s = in.readLine()) != null){
                        
                        String temp = "";
                        
                        StringTokenizer tokens = new StringTokenizer(s);

                        while (tokens.hasMoreTokens()) {
                            String nextToken = tokens.nextToken();
                            if(!stopWords.contains(nextToken.toLowerCase()))
                                temp+= nextToken.toLowerCase()+" ";
                        }
                        
                        s= temp;
	    		sb.append(s);
                        
	    	}
	    	//input cleaning regex
	    	String[] d = sb.toString().replaceAll("[\\W&&[^\\s]]","").split("\\W+");
	    	for(String u:d)
	    		if(!global.contains(u)){ //&& !stopWords.contains(u.toLowerCase())){
                                
                                global.add(u.toLowerCase());
                                
                        }
	    	docs.add(d);
	    	filenames.add(f.getName());
	    }
	    //

		//compute tf-idf and create document vectors (double[])
	    ArrayList<double[]> vecspace = new ArrayList<double[]>();
	    for(String[] s:docs){
	    	double[] d = new double[global.size()];
	    	for(int i=0;i<global.size();i++)
	    		d[i] = tf(s,global.get(i)) * idf(docs,global.get(i));
	    	vecspace.add(d);
	    }

            /*for(int i = 0; i<vecspace.size(); i++){
                
                System.out.println("Document: "+ filenames.get(i));
                for(int j=0; j<vecspace.get(i).length; j++){
                    
                    System.out.println(vecspace.get(i)[j]);
                    
                    
                }
                
                
                
            }*/
            
            
            /*
	    //iterate k-means
	    HashMap<double[],TreeSet<Integer>> clusters = new HashMap<double[],TreeSet<Integer>>();
	    HashMap<double[],TreeSet<Integer>> step = new HashMap<double[],TreeSet<Integer>>();
	    HashSet<Integer> rand = new HashSet<Integer>();
	    TreeMap<Double,HashMap<double[],TreeSet<Integer>>> errorsums = new TreeMap<Double,HashMap<double[],TreeSet<Integer>>>();
	    int k = 3;
	    int maxiter = 500;
	    for(int init=0;init<100;init++){
	    	clusters.clear();
	    	step.clear();
	    	rand.clear();
	    	//randomly initialize cluster centers
		    while(rand.size()< k)
		    	rand.add((int)(Math.random()*vecspace.size()));
		    for(int r:rand){
		    	double[] temp = new double[vecspace.get(r).length];
		    	System.arraycopy(vecspace.get(r),0,temp,0,temp.length);
		    	step.put(temp,new TreeSet<Integer>());
		    }
		    boolean go = true;
		    int iter = 0;
		    while(go){
		    	clusters = new HashMap<double[],TreeSet<Integer>>(step);
		    	//cluster assignment step
		    	for(int i=0;i<vecspace.size();i++){
			    	double[] cent = null;
			    	double sim = 0;
		    		for(double[] c:clusters.keySet()){
		    			double csim = cosSim(vecspace.get(i),c);
                                        
		    			if(csim > sim){
		    				sim = csim;
		    				cent = c;
		    			}
		    		}
                                if(clusters.get(cent)!=null)
		    		clusters.get(cent).add(i);
		    	}
		    	//centroid update step
		    	step.clear();
		    	for(double[] cent:clusters.keySet()){
		    		double[] updatec = new double[cent.length];
		    		for(int d:clusters.get(cent)){
		    			double[] doc = vecspace.get(d);
		    			for(int i=0;i<updatec.length;i++)
		    				updatec[i]+=doc[i];
		    		}
		    		for(int i=0;i<updatec.length;i++)
		    			updatec[i]/=clusters.get(cent).size();
		    		step.put(updatec,new TreeSet<Integer>());
		    	}
		    	//check break conditions
		    	String oldcent="", newcent="";
		    	for(double[] x:clusters.keySet())
		    		oldcent+=Arrays.toString(x);
		    	for(double[] x:step.keySet())
		    		newcent+=Arrays.toString(x);
		    	if(oldcent.equals(newcent)) go = false;
		    	if(++iter >= maxiter) go = false;
		    }
		    System.out.println(clusters.toString().replaceAll("\\[[\\w@]+=",""));
		    if(iter<maxiter)
		    	System.out.println("Converged in "+iter+" steps.");
		    else System.out.println("Stopped after "+maxiter+" iterations.");
		    System.out.println("");

			//calculate similarity sum and map it to the clustering
		    double sumsim = 0;
		    for(double[] c:clusters.keySet()){
		    		TreeSet<Integer> cl = clusters.get(c);
		    		for(int vi:cl){
		    			sumsim+=cosSim(c,vecspace.get(vi));
		    		}
		    	}
		    errorsums.put(sumsim,new HashMap<double[],TreeSet<Integer>>(clusters));

	    }
	    //pick the clustering with the maximum similarity sum and print the filenames and indices
	    System.out.println("Best Convergence:");
	    System.out.println(errorsums.get(errorsums.lastKey()).toString().replaceAll("\\[[\\w@]+=",""));
	    System.out.print("{");
	    for(double[] cent:errorsums.get(errorsums.lastKey()).keySet()){
		   	System.out.print("[");
		   	for(int pts:errorsums.get(errorsums.lastKey()).get(cent)){
		   		System.out.print(filenames.get(pts).substring(0,filenames.get(pts).lastIndexOf(".txt"))+", ");
		   	}
		   	System.out.print("\b\b], ");
	    }
	    System.out.println("\b\b}");
    }

    static double cosSim(double[] a, double[] b){
    	double dotp=0, maga=0, magb=0;
    	for(int i=0;i<a.length;i++){
    		dotp+=a[i]*b[i];
    		maga+=Math.pow(a[i],2);
    		magb+=Math.pow(b[i],2);
    	}
    	maga = Math.sqrt(maga);
    	magb = Math.sqrt(magb);
    	double d = dotp / (maga * magb);
        
    	return d==Double.NaN?0:d;
    }

    static double tf(String[] doc, String term){
    	double n = 0;
    	for(String s:doc)
    		if(s.equalsIgnoreCase(term))
    			n++;
    	return n/doc.length;
    }

    static double idf(ArrayList<String[]> docs, String term){
    	double n = 0;
    	for(String[] x:docs)
    		for(String s:x){
                    
    			if(s.equalsIgnoreCase(term)){
    				n++;
    				break;
	    		}
                }
    	return Math.log(docs.size()/n);
    }
   
   
   
   */