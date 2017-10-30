package com.ryx.ryxcredit.beans.bussiness.borrowrecords;

import com.ryx.ryxcredit.beans.bussiness.CbaseRequest;
import com.ryx.ryxcredit.utils.CJSONUtils;

/**
 * Created by DIY on 2016/9/14.
 */
public class BorrowRecordsRequest extends CbaseRequest{
    //	贷款状态	String	Y
    private String   loan_status	;

    public String getLoan_status() {
        return loan_status;
    }

    public void setLoan_status(String loan_status) {
        this.loan_status = loan_status;
    }

    @Override
    public String toString() {
        return  CJSONUtils.getInstance().toJSONString(this);
//        return "BorrowRecordsResponse{" +
//                "loan_status='" + loan_status + '\'' +
//                '}';
    }
}
