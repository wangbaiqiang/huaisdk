package com.huai.gamesdk.bean;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class RoleInfo {

	public String userId;
	/** 角色ID */
	public String roleId;
	/** 角色名 */
	public String roleName;
	/** 角色等级 */
	public String roleLevel;
	/** 区ID */
	public String zoneId;
	/** 区名称 */
	public String zoneName;
	/** 数据类型：1，进入游戏；2，创建角色；3，角色升级 */
	public String dataType;
	/** 额外字段 */
	public String ext;
	

	public RoleInfo() {
		super();
	}


	@Override
	public String toString() {
		return "RoleInfo [userId=" + userId + ", roleId=" + roleId
				+ ", roleName=" + roleName + ", roleLevel=" + roleLevel
				+ ", zoneId=" + zoneId + ", zoneName=" + zoneName
				+ ", dataType=" + dataType + ", ext=" + ext + ", extMap="
				+ extMap + "]";
	}


	public final Map<String, Object> extMap = new HashMap<String, Object>();
	
	public String toJson() {
		JSONObject object = new JSONObject();
		try {
			
			object.put("userId", userId);
			object.put("roleId", roleId);
			object.put("roleName", roleName);
			object.put("roleLevel", roleLevel);
			object.put("zoneId", zoneId);
			object.put("zoneName", zoneName);
			object.put("dataType", dataType);
			object.put("extend", extMap);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}
}
