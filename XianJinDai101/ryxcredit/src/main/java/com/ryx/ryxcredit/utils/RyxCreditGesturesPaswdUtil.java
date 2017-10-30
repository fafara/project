package com.ryx.ryxcredit.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 手势密码保存数据工具类
 */
public class RyxCreditGesturesPaswdUtil {
	private final String MAK = "ryx";
    private final int MODE = Context.MODE_PRIVATE;
    private final SharedPreferences sharedpreferences;

	public RyxCreditGesturesPaswdUtil(Context context, String fileName) {
		sharedpreferences = context.getSharedPreferences(fileName, MODE);
	}

//	public boolean saveSharedPreferencesEncrypt(String key, String value) {
//		Editor editor = sharedpreferences.edit();
//		try {
//			editor.putString(key, AESEncryptorUtil.encrypt(MAK, value));
//		} catch (Exception e) {
//			editor.putString(key, value);
//			e.printStackTrace();
//		}
//		return editor.commit();
//	}
//
//	public String loadStringSharedPreferenceDecrypt(String key) {
//		String str = null;
//		try {
//			str = sharedpreferences.getString(key, null);
//			if (str != null && !"".equals(str)) {
//				str = AESEncryptorUtil.decrypt(MAK, str);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return str;
//	}

	public boolean saveSharedPreferences(String key, String value) {
		Editor editor = sharedpreferences.edit();
		try {
			editor.putString(key, value);
		} catch (Exception e) {
			editor.putString(key, value);
			e.printStackTrace();
		}
		return editor.commit();
	}

	// 存储用户信息
	public void saveSharedPreferences(Map<String, String> map) {
		Iterator it = map.entrySet().iterator();
		Editor editor = sharedpreferences.edit();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			editor.putString(key, value);
			editor.commit();
		}

	}

	public String loadStringSharedPreference(String key) {
		String str = null;
		try {
			str = sharedpreferences.getString(key, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	public boolean saveSharedPreferences(String key, int value) {
		Editor editor = sharedpreferences.edit();
		editor.putInt(key, value);
		return editor.commit();
	}

	public int loadIntSharedPreference(String key) {
		return sharedpreferences.getInt(key, 0);
	}

	public boolean saveSharedPreferences(String key, float value) {
		Editor editor = sharedpreferences.edit();
		editor.putFloat(key, value);
		return editor.commit();
	}

	public float loadFloatSharedPreference(String key) {
		return sharedpreferences.getFloat(key, 0f);
	}

	public boolean saveSharedPreferences(String key, Long value) {
		Editor editor = sharedpreferences.edit();
		editor.putLong(key, value);
		return editor.commit();
	}

	public long loadLongSharedPreference(String key) {
		return sharedpreferences.getLong(key, 0l);
	}

	public boolean saveSharedPreferences(String key, Boolean value) {
		Editor editor = sharedpreferences.edit();
		editor.putBoolean(key, value);
		return editor.commit();
	}

	public boolean loadBooleanSharedPreference(String key) {
		return sharedpreferences.getBoolean(key, false);
	}

	public boolean saveAllSharePreference(String keyName, List<?> list) {
		int size = list.size();
		if (size < 1) {
			return false;
		}
		Editor editor = sharedpreferences.edit();
		if (list.get(0) instanceof String) {
			for (int i = 0; i < size; i++) {
				editor.putString(keyName + i, (String) list.get(i));
			}
		} else if (list.get(0) instanceof Long) {
			for (int i = 0; i < size; i++) {
				editor.putLong(keyName + i, (Long) list.get(i));
			}
		} else if (list.get(0) instanceof Float) {
			for (int i = 0; i < size; i++) {
				editor.putFloat(keyName + i, (Float) list.get(i));
			}
		} else if (list.get(0) instanceof Integer) {
			for (int i = 0; i < size; i++) {
				editor.putLong(keyName + i, (Integer) list.get(i));
			}
		} else if (list.get(0) instanceof Boolean) {
			for (int i = 0; i < size; i++) {
				editor.putBoolean(keyName + i, (Boolean) list.get(i));
			}
		}
		return editor.commit();
	}

	public Map<String, ?> loadAllSharePreference(String key) {
		return sharedpreferences.getAll();
	}

	public boolean removeKey(String key) {
		Editor editor = sharedpreferences.edit();
		editor.remove(key);
		return editor.commit();
	}

	public boolean removeAllKey() {
		Editor editor = sharedpreferences.edit();
		editor.clear();
		return editor.commit();
	}
}