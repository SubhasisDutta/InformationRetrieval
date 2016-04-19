import java.util.Map;


public class QueryEntity { 
	private VectorEntity queryVectorObj;
	private Map<Integer, Double> scoreTable1; //(docID,document Score)
	private Map<Integer, Double> scoreTable2;
	
	public QueryEntity(VectorEntity queryVectorObj,Map<Integer, Double> scoreTable1,Map<Integer, Double> scoreTable2) {
		this.queryVectorObj = queryVectorObj;
		this.scoreTable1 = scoreTable1;
		this.scoreTable2 = scoreTable2;
	}
	
	public VectorEntity getQueryVectorObj() {
		return queryVectorObj;
	}
	public void setQueryVectorObj(VectorEntity queryVectorObj) {
		this.queryVectorObj = queryVectorObj;
	}
	public Map<Integer, Double> getScoreTable1() {
		return scoreTable1;
	}
	public void setScoreTable1(Map<Integer, Double> scoreTable1) {
		this.scoreTable1 = scoreTable1;
	}
	public Map<Integer, Double> getScoreTable2() {
		return scoreTable2;
	}
	public void setScoreTable2(Map<Integer, Double> scoreTable2) {
		this.scoreTable2 = scoreTable2;
	}
	
	
	
	
	
	
	
}
