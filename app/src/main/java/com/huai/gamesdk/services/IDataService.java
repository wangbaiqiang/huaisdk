package com.huai.gamesdk.services;

import java.util.List;

import org.json.JSONObject;

/**
 * 客户端保存数据类
 */
public interface IDataService {
	/**
	 * 登录类型
	 */
	public static enum UidType {
		account, phone;
	}

	public boolean isFirstLoad(); 
	
	/**
	 * 最后一次登录的类型
	 * @return
	 */
	public String currentLoginType();
	
	/**
	 * 读取最近登录的用户信息
	 * @param type
	 * @return
	 */
	public JSONObject readCurntUid(UidType type);

	/**
	 * 按类型读取所有用户登录信息
	 * @param type
	 * @return
	 */
	public List<JSONObject> readUids(UidType type);

	/**
	 * 对登录成功的用户信息进行写入操作
	 * @param type
	 * @param uid
	 * @param pwd
	 */
	public void writeUid(UidType type, String uid, String pwd);

	/**
	 * 删除指定类型的用户信息
	 * @param uid
	 */
	public void delteUid(String uid);
	/**
	 * 生成手机标识串
	 * @param 
	 * @param 
	 */
	public void  writeImie(String imie);
	/**
	 * 生成手机标识串
	 * @param 
	 * @param 
	 */
	public String getImie();
	/**
	 * 获取自己产生的手机标识串
	 * @param 
	 * @param 
	 */
}
