package com.ryx.ryxcredit.newactivity.newinter;

/**
 * Author：lijing on 16/6/25 09:36
 * Mail：lijing1-jn@ruiyinxin.com
 * Description：
 */
public interface NewIBaseInfoFragmentCallback {
    //
    void setPersonInfo(Object personInfo);

    void setWorkInfo(Object workInfo);

    void setCallRecordInfo(Object callRecordInfo);

  //  void setFaceCollectInfo(Object faceCollectInfo);

    void setFaceCollectInfo();

    void setApplyInfo(Object applyInfo);

    void setOtherInfo(Object otherInfo);

    void setCallRecordInfo();
}
