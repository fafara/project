package com.ryx.ryxcredit.xjd;

import com.ryx.ryxcredit.xjd.bean.borrowrecord.MultiBorrowRecordsResponse;

import java.util.List;

/**
 * 借款记录回调
 * @author muxin
 * @time 2016-10-12 17:01
 */
public interface IMultiBorrowRecordCallBack {
    void recordCallBackSuccess(List<MultiBorrowRecordsResponse.ResultBean> list);

    void recordCallBackFailed(String tips);
}
