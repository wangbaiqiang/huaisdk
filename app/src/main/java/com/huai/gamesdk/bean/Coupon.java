package com.huai.gamesdk.bean;

import java.util.Date;




public class Coupon {
	public int  id;
	public int money;// 金额
	public Date deadLine;// 过期日期
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	

	public long getMoney() {
		return money;
	}

	public void setMoney(int l) {
		this.money = l;
	}

	public Date getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(Date deadLine) {
		this.deadLine = deadLine;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getCheckState() {
		return checkState;
	}

	public void setCheckState(int checkState) {
		this.checkState = checkState;
	}

	public int state;// 当前状态（0：可用，1：锁定,2：不用代金卷）
	public int checkState;// UI中是否被选中
}
