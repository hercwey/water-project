package com.learnbind.ai.util.wx.template;


public class Data {
	
	private String value;
	
	private String color;
	
	public Data(String value,String color)
	{
		this.value = value;
		this.color = color;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "Date [value=" + value + ", color=" + color + "]";
	}

}
