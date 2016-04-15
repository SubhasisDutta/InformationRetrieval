package com.subhasis.devisbase.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import com.subhasis.devisbase.models.DataTypeObj;
import com.subhasis.devisbase.utils.CommonUtils;
import com.subhasis.devisbase.utils.IndexUtility;
import com.subhasis.devisbase.utils.InformationSchemaUtility;

public class WhereClause {
	public String clauseVariable;
	
	public boolean equalTo;
	public boolean not;
	public boolean greaterThan;
	public boolean lesserThan;
	
	public String str_val;
	public float flt_val;
	
	
	//public String valType;
	private String clause;
	private String tableName;
	
	private DataTypeObj variableType;
	private Object valueObj;
	
	public String getClause() {
		return clause;
	}

	public void setClause(String clause) {
		this.clause = clause;
	}

	public WhereClause(String tableName,String clause) {
		this.clause = clause;
		this.tableName = tableName;
	}
	
	public boolean validateClause()throws Exception{
		//check where clause format
		String[] c = this.clause.split(" ");
		if(c.length != 3){			
			System.out.println("WHERE clause not in correct format. Currently only one WHERE <column_name> <operator> <value> is supported;");
			return false;
		}		
		this.clauseVariable = getClauseVariable(this.clause);
		if(clauseVariable == null) {
			System.out.println("Clause Variable name cannot be null");
			return false;
		}
		variableType = InformationSchemaUtility.getColumnType(CommonUtils.DFAULT_SCHEMA,tableName,clauseVariable);
		//need to verify if the column name exist in table
		if(variableType==null){
			System.out.println("Where clause variable is not present in table "+tableName+".");
			return false;
		}
		//check the value type of the variable		
		valueObj=CommonUtils.convertDataValue(variableType.getType(),  getClauseValue(clause));
		//validate the where clause .. else assume it is correct.
		if(valueObj==null){
			System.out.println("The value in WHERE clause is not of correct type.");
			return false;
		}
		return true;
	}
	
	public List<Long> getRecordPointers(String schema,String table)throws Exception{
		List<Long> pointers = new ArrayList<Long>();
		evaluateClause();
		String indexTableFileStr = CommonUtils.DB_LOCATION+schema+"/"+tableName+"/"+schema+"."+tableName+"."+variableType.getColumnName()+".nxd";
		
		IndexUtility indexUObj = new IndexUtility();
		TreeMap<Object,List<Long>> indexMap= indexUObj.getIndexMap(indexTableFileStr);
		if(equalTo){
			//= present
			if(greaterThan){
				NavigableMap<Object, List<Long>> result = indexMap.tailMap(valueObj, true);
				addToPointers(pointers,result);				
			}else if(lesserThan){
				NavigableMap<Object, List<Long>> result = indexMap.headMap(valueObj, true);
				addToPointers(pointers,result);
			}else if(not){
				NavigableMap<Object, List<Long>> greater = indexMap.tailMap(valueObj, false);
				NavigableMap<Object, List<Long>> less = indexMap.headMap(valueObj, false);
				addToPointers(pointers,greater);
				addToPointers(pointers,less);
			}else{
				//only equals present
				return indexMap.get(valueObj);
			}
		}else{
			//==not present
			if(greaterThan){
				NavigableMap<Object, List<Long>> greater = indexMap.tailMap(valueObj, false);
				addToPointers(pointers,greater);
			}else if(lesserThan){
				NavigableMap<Object, List<Long>> less = indexMap.headMap(valueObj, false);
				addToPointers(pointers,less);
			}else if(not){
				NavigableMap<Object, List<Long>> greater = indexMap.tailMap(valueObj, false);
				NavigableMap<Object, List<Long>> less = indexMap.headMap(valueObj, false);
				addToPointers(pointers,greater);
				addToPointers(pointers,less);
			}else{
				//only equals present
				System.out.println("The WHERE clause does not have a valid combination od operators.");				
			}
		}		
		return pointers;
	}
	
	private void addToPointers(List<Long> pointers,NavigableMap<Object, List<Long>> map){
		for(Object key: map.keySet()){
			List<Long> ps=map.get(key);
			for(Long p:ps){
				pointers.add(p);
			}
		}
	}

	private void evaluateClause()throws Exception {
		this.clauseVariable = getClauseVariable(this.clause);
		if(this.clause.contains("="))
			this.equalTo=true;
		if(this.clause.contains("!")||this.clause.contains("not"))
			this.not = true;
		if(this.clause.contains(">"))
			this.greaterThan = true;
		if(this.clause.contains("<"))
			this.lesserThan = true;
		//String clauseVal = getClauseValue(this.clause);
		//clauseVal = clauseVal.trim();
		//if(clauseVal.startsWith("\"")&& clauseVal.endsWith("\""))
		//	clauseVal = clauseVal.substring(1, clauseVal.length()-1);
		variableType = InformationSchemaUtility.getColumnType(CommonUtils.DFAULT_SCHEMA,tableName,clauseVariable);
		valueObj=CommonUtils.convertDataValue(variableType.getType(),getClauseValue(clause));		
	}
	
	
	private String getClauseValue(String clause) {
		char[] c = clause.trim().toCharArray();
		for (int i = c.length-1; i >=0; i--) {
			if(c[i] == '>'||c[i] == '<'||c[i] == '='||c[i] == '!'){
				String result = clause.trim().substring(i+1, c.length).trim();
				if(result.startsWith("\"")&& result.endsWith("\"")||result.startsWith("\'")&& result.endsWith("\'"))
					result = result.substring(1, result.length()-1);
				//System.out.println(result);
				return result;
			}				
		}
		return null;
	}

	private String getClauseVariable(String clause) {
		//System.out.println(clause);
		char[] c = clause.trim().toCharArray();
		for (int i = 0; i < c.length; i++) {
			if(c[i] == ' '||c[i] == '>'||c[i] == '<'||c[i] == '='||c[i] == '!'){
				String result = clause.substring(0, i);
				//System.out.println(result);
				return result;
			}
				
		}
		return null;
	}
}
