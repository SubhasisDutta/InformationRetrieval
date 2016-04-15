package com.subhasis.devisbase.models;

import com.subhasis.devisbase.enums.DataType;

public class DataTypeObj {
	private String columnName;
	private DataType type;
	private String typeString;
	private int length;
	private int ordinalValue;
	private boolean isNullable;
	private boolean isPK;
	
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public DataType getType() {
		return type;
	}
	public void setType(DataType type) {
		this.type = type;
	}
	public String getTypeString() {
		return typeString;
	}
	public void setTypeString(String typeString) {
		this.typeString = typeString;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getOrdinalValue() {
		return ordinalValue;
	}
	public void setOrdinalValue(int ordinalValue) {
		this.ordinalValue = ordinalValue;
	}
	public boolean isNullable() {
		return isNullable;
	}
	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}
	public boolean isPK() {
		return isPK;
	}
	public void setPK(boolean isPK) {
		this.isPK = isPK;
	}
	
	
}
