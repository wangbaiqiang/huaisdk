package com.huai.gamesdk.exception;

public class GameSDKException extends Exception {
	private static final long serialVersionUID = 6718244309216359724L;


	public GameSDKException(){
		super();
	}
	
	
	public GameSDKException(String message) {
		super(message);
	}
	
	public GameSDKException(String message, Throwable cause){
		super(message, cause);
	}
	
}
