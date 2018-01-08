package com.huai.gamesdk.mvp.model;

public class MVPLogingBean {
	
	private String userName;
	private String passWord;

	public MVPLogingBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MVPLogingBean(String userName, String passWord) {
		super();
		this.userName = userName;
		this.passWord = passWord;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	@Override
	public String toString() {
		return "MVPLoginBean [userName=" + userName + ", passWord=" + passWord
				+ "]";
	}
}
