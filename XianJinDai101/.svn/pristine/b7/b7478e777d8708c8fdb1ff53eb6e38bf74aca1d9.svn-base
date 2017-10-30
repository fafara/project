package com.ryx.payment.ruishua.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;

import static com.ryx.payment.ruishua.R.id.bt_next;

/**
 * 此类用于创建订单
 *
 * @author xucc
 */

public class CreateOrderView extends LinearLayout implements OnClickListener {
	private Button bt;

	// private TextView tv_type;
	private TextView tv_money;
	private TextView tv11, tv12, tv21, tv22, tv31, tv32, tv41, tv42, tv51,
			tv52;
	private RelativeLayout layout4, layout5;
	public int payType = -1;

	Class<?> link;
	Context context;
	Intent intent;
	private OnClickListener nextListener;
	/*
	 * 使用说明 调用 initMneuIvICON setMenuText SelectMenu(int index) initMneuLinks
	 */
	public CreateOrderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);// 通过SystemService获得layout扩展服务
		inflater.inflate(R.layout.creatorder, this);// 实例化xml布局文件

		// tv_type=(TextView)findViewById(R.id.type);
		tv_money = (TextView) findViewById(R.id.order_money);
		tv11 = (TextView) findViewById(R.id.tv11);
		tv12 = (TextView) findViewById(R.id.tv12);
		tv21 = (TextView) findViewById(R.id.tv21);
		tv22 = (TextView) findViewById(R.id.tv22);
		tv31 = (TextView) findViewById(R.id.tv31);
		tv32 = (TextView) findViewById(R.id.tv32);
		tv41 = (TextView) findViewById(R.id.tv41);
		tv42 = (TextView) findViewById(R.id.tv42);
		tv51 = (TextView) findViewById(R.id.tv51);
		tv52 = (TextView) findViewById(R.id.tv52);
		bt = (Button) findViewById(bt_next);
		layout4 = (RelativeLayout) findViewById(R.id.layout4);
		layout5 = (RelativeLayout) findViewById(R.id.layout5);

	}

	public void setExplanation(String text1, String text2, String text3,
			String text4) {
		tv11.setText(text1);
		tv21.setText(text2);
		tv31.setText(text3);
		tv41.setText(text4);
		bt.setOnClickListener(this);
	}

	public void setExplanation(String text1, String text2, String text3,
			String text4, String text5, String bttext) {
		tv11.setText(text1);
		tv21.setText(text2);
		tv31.setText(text3);
		tv41.setText(text4);
		tv51.setText(text5);
		// bt.setText(bttext);
		bt.setOnClickListener(this);
	}

	/*
	 * 2014-06-12 为转账汇款而增加的
	 *
	 * @author tianyingzhong
	 *
	 * @param String money 金额；
	 *
	 * @param String orderNo "订单号"
	 *
	 * @param String explanation "支付说明"
	 *
	 * @param String chamberlain "收款人"
	 *
	 * @param String collectionAccount "收款账户"
	 *
	 * @param String paymentAccount "收款账户"
	 *
	 * @param String bttext 按钮上显示的字
	 */
	public void setTransferExplanation() {
		tv11.setText("订单号");
		tv21.setText("支付说明");
		tv31.setText("收款人");
		tv41.setText("收款账户");
		tv51.setText("支付账户");
		bt.setText("确认支付");
		bt.setOnClickListener(this);
	}

	public void setValue(String money, String dingdanhao, String shuoming,
			String merchantId, String productId) {
		tv_money.setText(money + "");
		tv12.setText(dingdanhao);
		tv22.setText(shuoming);
		tv32.setText(merchantId);
		tv42.setText(productId);
		layout5.setVisibility(View.GONE);
		bt.setOnClickListener(this);
	}
	public void setBindDisPalyValue(String money, String dingdanhao, String shuoming,
						 String merchantId, String productId,String couponBindDisPaly) {
		tv_money.setText(money + "");
		tv12.setText(dingdanhao);
		tv22.setText(shuoming);
		tv32.setText(merchantId);
		tv42.setText(productId);
		layout5.setVisibility(View.VISIBLE);
		tv52.setText(couponBindDisPaly);
		bt.setOnClickListener(this);
	}

	public void setValue(String money, String dingdanhao, String shuoming,
			String merchantId) {
		tv_money.setText(money + "");
		tv12.setText(dingdanhao);
		tv22.setText(shuoming);
		tv32.setText(merchantId);
		layout4.setVisibility(View.GONE);
		layout5.setVisibility(View.GONE);
		bt.setOnClickListener(this);
	}

	/*
	 * 为提款而增加的
	 *
	 * @param String money 金额；
	 *
	 * @param String orderNo "订单号"
	 *
	 * @param String explanation "收款说明"
	 *
	 * @param String chamberlain "收款人"
	 *
	 * @param String collectionAccount "收款账户"
	 *
	 * @param String paymentAccount "收款账户"
	 *
	 * @param String bttext 按钮上显示的字
	 */
	public void setTransferValue(String money, String orderNo,
			String explanation, String chamberlain, String collectionAccount,
			String paymentAccount) {
		tv_money.setText(money + "");
		tv12.setText(orderNo);
		tv22.setText(explanation);
		tv32.setText(chamberlain);
		tv42.setText(collectionAccount);
		tv52.setText(paymentAccount);
		bt.setOnClickListener(this);
	}

	public void setValue(String money, String orderNo, String explanation,
			String chamberlain, String collectionAccount,
			String paymentAccount, String bttext) {
		tv_money.setText(money + "");
		tv12.setText(orderNo);
		tv22.setText(explanation);
		tv32.setText(chamberlain);
		tv42.setText(collectionAccount);
		tv52.setText(paymentAccount);
		// bt.setText(bttext);
		bt.setOnClickListener(this);
	}

	/*
	 *
	 * @param String money 金额；
	 *
	 * @param String dingdanhao "订单号"
	 *
	 * @param String shuoming "收款说明"
	 *
	 * @param String merchantId "商户名称"
	 *
	 * @param String productId "收款账户"
	 *
	 * @param String bttext 按钮上显示的字
	 */
	public void setValue(String money, String dingdanhao, String shuoming,
			String merchantId, String productId, String bttext) {
		tv_money.setText(money + "");
		tv12.setText(dingdanhao);
		tv22.setText(shuoming);
		tv32.setText(merchantId);
		tv42.setText(productId);
		bt.setText(bttext);
		bt.setOnClickListener(this);
	}

	public void setIntent(Intent intent) {
		this.intent = intent;
	};

	/**
	 * 设置下一步按钮的监听
	 * @param onClickListener
     */
    public void setNextListener(OnClickListener onClickListener){
		this.nextListener=onClickListener;
	}

	/**
	 * 设置按钮的名称
	 * @param content
     */
	public void setNextBtnText(String content){
		bt.setText(content);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case bt_next:
			disabledTimerView(v);
			if (intent != null) {

				// ((Activity)
				// context).startActivityForResult(intent,QtpayAppConfig.WILL_BE_CLOSED);

//				if (payType == 0) {
//					Intent intent2 = new Intent((Activity) context,
//							RedEnvelope_Scene_Pay.class);
//					intent2.putExtra("orderinfo", (OrderInfo) this.intent
//							.getExtras().get("orderinfo"));
//
//					if (null != this.intent.getStringExtra("PayForSenior")) {
//						intent2.putExtra("PayForSenior",
//								this.intent.getStringExtra("PayForSenior"));
//					}
//
//					((Activity) context).startActivityForResult(intent2,
//							QtpayAppConfig.WILL_BE_CLOSED);
//				} else
                if (payType == 1) {
					((Activity) context).startActivityForResult(intent,
							RyxAppconfig.WILL_BE_CLOSED);

					// context.startActivity(intent);
					// ((Activity) context).finish();
				} else {
					((Activity) context).startActivityForResult(intent,
                            RyxAppconfig.WILL_BE_CLOSED);
				}

			}else if(nextListener!=null){
				nextListener.onClick(v);
			}

			break;
		}
	}

	public String hideInfo(String countnum) {
		if (countnum != null && countnum.length() > 8) {

		}
		return countnum;
	}
	public  void disabledTimerView(final View v) {
		v.setClickable(false);
		v.setEnabled(false);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				v.setClickable(true);
				v.setEnabled(true);
			}
		}, 2000);
	}
}
