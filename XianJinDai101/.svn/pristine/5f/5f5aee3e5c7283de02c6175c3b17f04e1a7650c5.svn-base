package com.ryx.payment.ruishua.utils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Contacts;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.ryx.payment.ruishua.RyxAppconfig;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


@SuppressWarnings("deprecation")
public class ContactHelper {
	

	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	
	public static String setStarForPhoneNumber(String phone) {
		phone = trimMobilePhone(phone);
		if (phone.length() > 0) {
			return phone.substring(0, 3) + "****" + phone.substring(7);
		} else {
			return "";
		}
	}

	
	public static String setStarForEmail(String email) {
		int index = email.indexOf("@");
		if (index < 0 ) {
			return "";
		}
		String headEmail = email.substring(0, index);
		String endEmail = email.substring(index);

		
		if (headEmail != null && headEmail.length() > 3) {
			String headThree = headEmail.substring(0, 3);
			headEmail = headThree+"***";
		} else if (headEmail != null && headEmail.length() <= 3&&headEmail.length() >= 1) {
			headEmail = headEmail.substring(0,1)+"***";
		}
		return headEmail + endEmail;
	}



	public static DisplayMetrics getDisplayMetrics(Context context) {
		DisplayMetrics tDisplayMetrics = new DisplayMetrics();
		Display tDisplay = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		tDisplay.getMetrics(tDisplayMetrics);

		return tDisplayMetrics;
	}

	



	
	
	public static void doPhoneContact(Activity context) {
		try {
			
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_PICK);
				if (Integer.parseInt(Build.VERSION.SDK) > 4)// 2.x sdk 
				{
					// intent.setData(Uri.parse("content://com.android.contacts/contacts"));
					intent.setData(ContactsContract.Contacts.CONTENT_URI);
				} else {
					intent.setData(People.CONTENT_URI);
				}

				context.startActivityForResult(intent, RyxAppconfig.REQUEST_CONTACT);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getContactName(Uri data, Activity context) {
		Uri person = null;// ContentUris.withAppendedId(People.CONTENT_URI,
							// ContentUris.parseId(data));
		String version = Build.VERSION.SDK;
		try {
			if (Integer.parseInt(version) > 4)// 2.x�?sdk??????
			{
				person = ContentUris
						.withAppendedId(
								ContactsContract.Contacts.CONTENT_URI
								/*
								 * Uri.parse(
								 * "content://com.android.contacts/contacts")
								 */, ContentUris.parseId(data));
			} else// 1.6以�??SDK
			{
				person = ContentUris.withAppendedId(People.CONTENT_URI,
						ContentUris.parseId(data));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		String hintName = "";

		Cursor cur = context.managedQuery(person, null, null, null, null);
		try {
			if (cur.moveToFirst()) {
				int nameIndex = 0;

				nameIndex = cur.getColumnIndex(Contacts.Phones.DISPLAY_NAME);
				if (nameIndex != -1) {
					hintName = cur.getString(nameIndex);
				}
			}
		} catch (Exception e) {
			//
		} finally {
			cur.close();
			context.stopManagingCursor(cur);
		}

		return hintName;
	}

	public static ArrayList<String> getContactPhoneNo(Uri data, Activity context) {
		Uri person = null;// ContentUris.withAppendedId(People.CONTENT_URI,
							// ContentUris.parseId(data));
		String version = Build.VERSION.SDK;
		ArrayList<String> tArray = new ArrayList<String>();
		try {
			if (Integer.parseInt(version) > 4)// 2.x�?sdk??????
			{
				person = ContentUris.withAppendedId(
						ContactsContract.Contacts.CONTENT_URI,
						ContentUris.parseId(data));
			} else// 1.6以�??SDK
			{
				person = ContentUris.withAppendedId(People.CONTENT_URI,
						ContentUris.parseId(data));
			}
		} catch (Exception e) {
			return tArray;
		}

		// String number = context.getString(R.string.NoContactPhoneNo);
		try {
			Cursor cur = context.managedQuery(person, null, null, null, null);
			try {
				if (cur.moveToFirst()) {
					int i = 0;
					if (Integer.parseInt(version) > 4) {
						String contactId = cur.getString(cur
								.getColumnIndex("_id"));
						if (contactId != null && !contactId.equals("")) {
							// Cursor phones =
							// context.getContentResolver().query(Uri.parse("content://com.android.contacts/data/phones"),null,"contact_id"
							// +" = "+ contactId + " and " + "data2" + "=" + "2"
							// ,null, null);
							Cursor phones = context
									.getContentResolver()
									.query(Uri
											.parse("content://com.android.contacts/data/phones"),
											null,
											"contact_id" + " = " + contactId,
											null, null);
							while (phones.moveToNext()) {
								tArray.add(trimMobilePhone(phones
										.getString(phones
												.getColumnIndex("data1"))));
							}
							phones.close();
						}

					} else {
						i = cur.getColumnIndex(Contacts.Phones.NUMBER);
						if (i != -1) {
							tArray.add(trimMobilePhone(cur.getString(i)));
						}
					}

				}
			} catch (Exception e) {
				if (Integer.parseInt(version) > 4) {
					int i = cur.getColumnIndex(Contacts.Phones.NUMBER);
					if (i != -1) {
						tArray.add(trimMobilePhone(cur.getString(i)));
					}
				}
			} finally {
				cur.close();
				context.stopManagingCursor(cur);
			}
		} catch (Exception e) {

		}
		return tArray;
	}

	public static String trimMobilePhone(String number) {

		number = number.replace("-", "");
		number = number.replace(" ", "");
		int n = number.length();
		if (n > 11) {
			number = number.substring(n - 11);
		}

		return number;
	}

	

	public static JSONObject string2JSON(String str, String split) {
		JSONObject json = new JSONObject();
		try {
			String[] arrStr = str.split(split);
			for (int i = 0; i < arrStr.length; i++) {
				String[] arrKeyValue = arrStr[i].split("=");
				json.put(arrKeyValue[0],
						arrStr[i].substring(arrKeyValue[0].length() + 1));
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	


	public static boolean is3GOrWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo info = cm.getActiveNetworkInfo();
			if (info != null && info.isAvailable()) {
				int netType = info.getType();
				if (netType == ConnectivityManager.TYPE_WIFI) {
					return true;
				} else if (netType == ConnectivityManager.TYPE_MOBILE) {
					String typeName = info.getTypeName().toLowerCase();
					typeName = info.getExtraInfo().toLowerCase();
					if (typeName.contains("3g")) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean isWapApn(Context context) {
		boolean iswap = false;

		try {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm != null) {
				NetworkInfo info = cm.getActiveNetworkInfo();
				if (info != null && info.isAvailable()) {
					int netType = info.getType();
					if (netType == ConnectivityManager.TYPE_WIFI) {
						iswap = false;
					} else if (netType == ConnectivityManager.TYPE_MOBILE) {
						String typeName = info.getTypeName().toLowerCase(); // cmwap/cmnet/wifi/uniwap/uninet
						typeName = info.getExtraInfo().toLowerCase();
						// 3gnet/3gwap/uninet/uniwap/cmnet/cmwap/ctnet/ctwap
						if (typeName.contains("wap")) {
							iswap = true;
						}

						String host = android.net.Proxy.getDefaultHost();
						int port = android.net.Proxy.getDefaultPort();
						if (host != null && !host.equals("")) {
							iswap = true;
						}

						
				}
			}
		} }catch (Exception e) {
			e.printStackTrace();
			iswap = false;
		}

		return iswap;
	}


	public static String getTimeStamp() {
		SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmssSSS");
		Date date = new Date();

		String timeStamp = format.format(date);
		return timeStamp;
	}

	
	/**
	 * ???�?repeat
	 * 
	 * @param view
	 */
	public static void fixBackgroundRepeat(View view) {
		Drawable bg = view.getBackground();
		if (bg != null) {
			if (bg instanceof BitmapDrawable) {
				BitmapDrawable bmp = (BitmapDrawable) bg;
				bmp.mutate(); // make sure that we aren't sharing state anymore
				bmp.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
			}
		}
	}
}
