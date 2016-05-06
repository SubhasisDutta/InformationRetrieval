package ir.mainexam;

public class HITS_1E {
	public int path[][] = new int[11][11];
    public double hubScore[] = new double[11];
    public double authorityScore[] = new double[11];	
    public void calc(int n){       	
    	for(int i=1;i<=n;i++){
    		hubScore[i]=1;
    		authorityScore[i]=1;
    	}
    	//Iterate 5 times
    	for(int step=0;step<5;step++){
    		System.out.println("For ITERATION "+ (step+1));
    		double norm=0;   		
    		
    		//update all authority values
    		for(int i=1;i<=n;i++){ //for each node
    			authorityScore[i]=0;
    			for(int j=1;j<=n;j++){
    				if(this.path[j][i]==1){
    					authorityScore[i]+=hubScore[j];
    				}
    			}    			
    			norm+=Math.pow(authorityScore[i], 2);
    		}
    		norm=Math.sqrt(norm);
    		for(int i=1;i<=n;i++){
    			authorityScore[i]=authorityScore[i]/norm;
    		}
    		for(int i=1;i<=n;i++)
        		System.out.printf(" Authority Score of D"+i+" is :\t"+this.authorityScore[i]+"\n");
    		norm=0;
    		for(int i=0;i<=n;i++){
    			hubScore[i]=0;
    			for(int j=1;j<=n;j++){
    				if(this.path[i][j]==1){
    					hubScore[i]+=authorityScore[j];
    				}
    			}
    			norm+=Math.pow(hubScore[i], 2);
    		}
    		norm=Math.sqrt(norm);
    		for(int i=1;i<=n;i++){
    			hubScore[i]=hubScore[i]/norm;
    		}
    		for(int i=1;i<=n;i++)
        		System.out.printf(" Hub Score of D"+i+" is :\t"+this.hubScore[i]+"\n");
    	}    	
    }   
    
	public static void main(String args[])
    {
        int nodes;
        nodes = 10;
        HITS_1E p = new HITS_1E();
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
        p.path =a;
        p.calc(nodes); 
    }
}
