package com.learnbind.ai.util.wx.template;

public class SendAllFilter {
	
	public Boolean is_to_all;
	
	public Integer group_id;
	
	public SendAllFilter(Integer group_id) {
		super();
		this.is_to_all = false;
		this.group_id = group_id;
	}

	public SendAllFilter() {
		super();
		this.is_to_all = true;
	}

	public Boolean getIs_to_all() {
		return is_to_all;
	}

	public void setIs_to_all(Boolean is_to_all) {
		this.is_to_all = is_to_all;
	}

	public Integer getGroup_id() {
		return group_id;
	}

	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}

	@Override
	public String toString() {
		return "SendAllFilter [is_to_all=" + is_to_all + ", group_id="
				+ group_id + "]";
	}
	

}
