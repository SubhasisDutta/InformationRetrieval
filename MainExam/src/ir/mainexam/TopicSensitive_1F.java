package ir.mainexam;

import java.util.*;

public class TopicSensitive_1F{
  
    public int path[][] = new int[11][11];
    public double pagerank[] = new double[11];
    public double pv[][]=new double[11][11];
  
    public void calc(double n){    
    	double init;
    	double c=0; 
    	double temp[] = new double[11];
    	int i,j,u=1,k=1;
    	init = 1/n; 
    	System.out.printf(" n value:"+n+"\t init value :"+init+"\n");
    	for(i=1;i<=n;i++)
    		this.pagerank[i]=init;
    	System.out.printf("\n Initial PageRank Values , 0th Step \n");
    	for(i=1;i<=n;i++)
    		System.out.printf(" Page Rank of D"+i+" is :\t"+this.pagerank[i]+"\n");
   
    	while(u<=9){
    		for(i=1;i<=n;i++){    			
    			temp[i]=this.pagerank[i];
    			this.pagerank[i]=0;
    		}
     
    		for(j=1;j<=n;j++){
    			for(i=1;i<=n;i++){
    				if(this.path[i][j] == 1){
    					k=1;c=0; 
    					while(k<=n){
    						if(this.path[i][k] == 1 )
    							c=c+1;
    						k=k+1;
    					} 
    					this.pagerank[j]=this.pagerank[j]+temp[i]*(1/c);    
    				}
    			}    				
    		}
    		for(i=1;i<=n;i++){   			    			
    			this.pagerank[i]=0.15*temp[i]+0.85*this.pagerank[i];
    		}	 
   
    		System.out.printf("\n After "+u+"th Step \n"); 
    		for(i=1;i<=n;i++) 
    			System.out.printf(" Page Rank of D"+i+" is :\t"+this.pagerank[i]+"\n"); 
   
    		u=u+1;
    	}
    	
    	for(i=1;i<=n;i++){
    		double rank= this.pagerank[i];
    		for(j=1;j<=n;j++){
    			this.pv[i][j]+=rank;
    		}
    	}
    	for(i=1;i<=n;i++){    		
    		for(j=1;j<=n;j++){
    			System.out.print(this.pv[i][j]+" ");
    		}
    		System.out.println();
    	}
    }
    public static void main(String args[])
    {
        int nodes;
        nodes = 10;
        TopicSensitive_1F p = new TopicSensitive_1F();
        int[][] a={{0,0,0,0,0,0,0,0,0,0,0},
        		   {0,0,1,1,0,1,0,1,0,0,0},
        		   {0,0,0,0,1,0,0,1,0,0,0},
        		   {0,1,0,0,0,0,1,0,0,0,1},
        		   {0,0,0,0,0,1,1,0,0,1,0},
        		   {0,0,0,0,0,0,1,1,0,1,1},
        		   {0,0,1,1,0,0,0,0,1,0,0},
        		   {0,1,0,1,0,0,0,0,0,1,0},
        		   {0,1,0,0,0,1,0,0,0,0,1},
        		   {0,0,1,0,0,1,0,0,0,0,1},
        		   {0,1,0,1,0,0,1,0,0,0,0}};
        double in_topic = 0.4;
        double not_in_topic = 0.2;
        double[][] pv={{0,0,0,0,0,0,0,0,0,0,0},
     		   {0,0,not_in_topic,not_in_topic,0,in_topic,0,not_in_topic,0,0,0},
     		   {0,0,0,0,in_topic,0,0,not_in_topic,0,0,0},//difftetencec in 4
     		   {0,not_in_topic,0,0,0,0,in_topic,0,0,0,in_topic},
     		   {0,0,0,0,0,not_in_topic,not_in_topic,0,0,in_topic,0},
     		   {0,0,0,0,0,0,not_in_topic,not_in_topic,0,not_in_topic,not_in_topic},
     		   {0,0,not_in_topic,in_topic,0,0,0,0,not_in_topic,0,0},
     		   {0,not_in_topic,0,in_topic,0,0,0,0,0,not_in_topic,0},
     		   {0,in_topic,0,0,0,in_topic,0,0,0,0,not_in_topic},
     		   {0,0,in_topic,0,0,not_in_topic,0,0,0,0,not_in_topic},
     		   {0,not_in_topic,0,in_topic,0,0,in_topic,0,0,0,0}};
        p.path =a;
        p.pv = pv;
        p.calc(nodes); 
    } 
}
