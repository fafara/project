package com.ryx.payment.ruishua.setting;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.LogUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_feed_back)
public class FeedBack extends BaseActivity {

    @ViewById(R.id.tileleftImg)
    ImageView mBackImg;
    @ViewById(R.id.tilerightImg)
    ImageView mMsgImg;
    @ViewById(R.id.feedback_content_edit)
    EditText mFeedBack;
    @ViewById(R.id.btn_submit)
    Button mSubmitBtn;
    private String suggestionStr;

    @AfterViews
    public void initViews() {
        setTitleLayout("意见反馈",true,false);
        initQtPatParams();
    }

    @Click(R.id.tileleftImg)
    public void backBtnClick() {
        finish();
    }

    @Click(R.id.btn_submit)
    public void submitBtnClick() {
        if (checkInput()) {
            qtpayAttributeList.add(qtpayApplication);
            qtpayParameterList.add(new Param("noticeCode", "0000"));
            qtpayParameterList.add(new Param("phoneModel", android.os.Build.MODEL.trim()));
            qtpayParameterList.add(new Param("content", suggestionStr));
            mSubmitBtn.setClickable(false);
            httpsPost("doReportSuggestion", new XmlCallback() {
                @Override
                public void onTradeSuccess(RyxPayResult payResult) {
                    mSubmitBtn.setClickable(true);
                    LogUtil.showToast(FeedBack.this, "您的反馈提交成功,我们会及时处理,谢谢");
                    finish();
                }

                @Override
                public void onLoginAnomaly() {

                }

                @Override
                public void onOtherState() {

                }

                @Override
                public void onTradeFailed() {
                    mSubmitBtn.setClickable(true);
                }
            });
        }
    }

    public boolean checkInput() {
        suggestionStr = mFeedBack.getText().toString().trim();
        if (TextUtils.isEmpty(suggestionStr)) {
            LogUtil.showToast(this, "内容不能为空");
            return false;
        }
        if (suggestionStr.length() > 128) {
            LogUtil.showToast(this, "内容过长");
            return false;
        }
        return true;
    }

    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        qtpayApplication = new Param("application", "ReportProblem.Req");
    }
}
