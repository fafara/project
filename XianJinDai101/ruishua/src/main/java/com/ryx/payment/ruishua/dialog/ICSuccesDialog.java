package com.ryx.payment.ruishua.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.ryx.payment.ruishua.R;


public class ICSuccesDialog extends Dialog {

	Context context;
	String address;
	
	LinearLayout menulayout;

	public ICSuccesDialog(Context context) {
		super(context);
	}

	public ICSuccesDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_ic_succed);
		
		menulayout=(LinearLayout)findViewById(R.id.menulayout);
		
		menulayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ICSuccesDialog.this.dismiss();				
			}
		});
		
	}
}
