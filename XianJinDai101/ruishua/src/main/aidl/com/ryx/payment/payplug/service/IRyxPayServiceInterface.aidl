// IRyxPayServiceInterface.aidl
package com.ryx.payment.payplug.service;

// Declare any non-default types here with import statements
import com.ryx.payment.payplug.bean.Students;
import com.ryx.payment.payplug.service.ICallBack;
interface IRyxPayServiceInterface {
 int add(int num1,int num2);
        List<Students> addStudent(in Students student);//这里的in 一定需要的  或者out
       void addStudentCallBack(in Students student,ICallBack callBack);//这里的in 一定需要的  或者out
        //---------------------------以上为测试例子接口,以下接口为正式接口-------------------------------------------
            void getCardBalance(in String reqCode, ICallBack callBack);
            //调用RyxPay收款台
             void toOrderPay(in String reqCode,in Map reqMap,ICallBack callBack);
}
