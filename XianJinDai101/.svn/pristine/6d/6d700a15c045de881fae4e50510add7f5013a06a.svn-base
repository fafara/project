package com.ryx.ryxpay.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.ProgressView;
import com.ryx.payment.ryxhttp.callback.FileCallBack;
import com.ryx.ryxpay.R;
import com.ryx.ryxpay.RyxAppconfig;
import com.ryx.ryxpay.bean.RyxPayResult;
import com.ryx.ryxpay.net.XmlCallback;
import com.ryx.ryxpay.utils.HttpUtil;
import com.ryx.ryxpay.utils.LogUtil;
import com.ryx.ryxpay.utils.PreferenceUtil;
import com.ryx.ryxpay.widget.RyxLoadDialogBuilder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/5/9.
 */
@EActivity(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {
	String updateContent = "";
	String version;
	String updateUrl;
	String must;
	String updateInfo;
	boolean isstart=false;
	RyxLoadDialogBuilder ryxLoadDialogBuilder;

	@AfterViews
	public void initView(){

//		new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//			@Override
//			public void run() {
//
//				Intent intent = new Intent(SplashActivity.this,MainDrawerActivity_.class);
//				startActivity(intent);
//				finish();
//			}
//		},2000);

		initQtPatParams();
		doUpdate();
}

	/**
	 * 版本更新检测
	 */
	public void doUpdate() {
		qtpayApplication.setValue("ClientUpdate2.Req");
		qtpayAttributeList.add(qtpayApplication);
		httpsPost("ClientUpdateTag", new XmlCallback(){

			@Override
			public void onTradeSuccess(RyxPayResult payResult) {
				doAfterSuccess(payResult);
			}

			@Override
			public void onLoginAnomaly() {

			}

			@Override
			public void onOtherState() {

			}

			@Override
			public void onTradeFailed() {

			}
		});
	}

	/**
	 * 请求成功后做的事情
	 * @param ryxPayResult
     */
	private void doAfterSuccess(RyxPayResult ryxPayResult){
		if (qtpayApplication.getValue().equals("ClientUpdate2.Req")) {
			getUpdateInfo(ryxPayResult.getData());
		}
	}

	private void getUpdateInfo(String jsonstring) {
		try {
			JSONObject jsonObj = new JSONObject(jsonstring);
			if (RyxAppconfig.QTNET_SUCCESS.equals(jsonObj.getJSONObject(
					"result").getString("resultCode"))) {
				updateInfo = "发现新版本";
				// 解析更新的信息
				JSONArray resultBeans = jsonObj.getJSONArray("resultBean");
				if (resultBeans != null) {
					for (int i = 0; i < resultBeans.length(); i++) {
						updateContent = updateContent
								+ resultBeans.getJSONObject(i).getString(
								"updateContent") + "\n";
					}
					// updateContent = "强制更新\n强制更新\n强制更新";
					version = jsonObj.getJSONObject("summary").getString(
							"version");
					updateUrl = jsonObj.getJSONObject("summary").getString(
							"updateUrl");
					must = jsonObj.getJSONObject("summary").getString("must");
					PreferenceUtil.getInstance(SplashActivity.this)
							.saveString("version", version);
					PreferenceUtil.getInstance(SplashActivity.this)
							.saveString("updateContent", updateContent);
					PreferenceUtil.getInstance(SplashActivity.this)
							.saveString("updateUrl", updateUrl);
					PreferenceUtil.getInstance(SplashActivity.this)
							.saveString("must", must);

					showUpdataDialog(); // 需要更新就显示升级对话框
				}
			} else if ("0001".equals(jsonObj.getJSONObject("result").getString(
					"resultCode"))) {
				updateInfo = "查询失败";
				getUserInstruction();
			} else if ("0002".equals(jsonObj.getJSONObject("result").getString(
					"resultCode"))) {
				updateInfo = "不需要更新";
				getUserInstruction();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			PreferenceUtil.getInstance(SplashActivity.this).saveString(
					"updateInfo", updateInfo);
		}
	}

	/**
	 * 展示下载框
	 */
	private void showUpdataDialog() {
	final 	SimpleDialog.Builder builder=new SimpleDialog.Builder(R.style.SimpleDialogLight){
			@Override
			public void onNegativeActionClicked(DialogFragment fragment) {
				//取消
				fragment.dismiss();
				Intent intent = new Intent(SplashActivity.this,MainDrawerActivity_.class);
				startActivity(intent);
				finish();
			}

			@Override
			public void onPositiveActionClicked(DialogFragment fragment) {
				//确定
				downApk(updateUrl);
				fragment.dismiss();
			}
		};
		 builder.message("n".equals(must)?"发现新版本请升级！":"发现新版本是否需要升级?").title("版本升级"). positiveAction("确定");
		if("n".equals(must)) {
			builder.negativeAction("取消");
		}
		DialogFragment fragment = DialogFragment.newInstance(builder);
		fragment.show(getSupportFragmentManager(), null);
	}
	public void downApk(String url){
		final String downUrl=Environment.getExternalStorageDirectory().getAbsolutePath()+"/ryxpay/";
		int index = url.lastIndexOf("/");
		final String fname = url.substring(index + 1); // 获取文件名
		HttpUtil.getInstance().httpsFilePost(url,"downApkTag","", new FileCallBack(downUrl,fname) {
			@Override
			public void inProgress(float progress, long total) {
				LogUtil.showLog("progress="+progress);
				showProgressView(progress,downUrl+fname);
			}

			@Override
			public void onError(Call call, Exception e) {
				LogUtil.showLog("onError="+e.getMessage());
			}

			@Override
			public void onResponse(File response) {
				LogUtil.showLog("onResponse====");
			}
		});
}
	/**
	 * 获取用户须知
	 */
	private void getUserInstruction() {
		LogUtil.showToast(SplashActivity.this,"获取用户须知");
	}
	private void showProgressView(float progress,String fileName){
		DecimalFormat df = new DecimalFormat("#.00");
//		showLoading("当前进度为："+(df.format(progress*100))+"%");
		if(ryxLoadDialogBuilder==null){
			ryxLoadDialogBuilder=RyxLoadDialogBuilder.getInstance(SplashActivity.this);
		}
		ryxLoadDialogBuilder.setMessage("当前进度为："+(df.format(progress*100))+"%");
		ryxLoadDialogBuilder.show();
		if(progress==1){
			ryxLoadDialogBuilder.dismiss();
			installApk(fileName);
		}
	}
	private void installApk(String filename)
	{
		File file = new File(filename);
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		String type = "application/vnd.android.package-archive";
		intent.setDataAndType(Uri.fromFile(file), type);
		startActivity(intent);
	}
}
