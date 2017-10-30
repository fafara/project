/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\RuiYinXin\\XianJinDai101\\ruishua\\src\\main\\aidl\\com\\ryx\\payment\\payplug\\service\\IRyxPayServiceInterface.aidl
 */
package com.ryx.payment.payplug.service;
public interface IRyxPayServiceInterface extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.ryx.payment.payplug.service.IRyxPayServiceInterface
{
private static final java.lang.String DESCRIPTOR = "com.ryx.payment.payplug.service.IRyxPayServiceInterface";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.ryx.payment.payplug.service.IRyxPayServiceInterface interface,
 * generating a proxy if needed.
 */
public static com.ryx.payment.payplug.service.IRyxPayServiceInterface asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.ryx.payment.payplug.service.IRyxPayServiceInterface))) {
return ((com.ryx.payment.payplug.service.IRyxPayServiceInterface)iin);
}
return new com.ryx.payment.payplug.service.IRyxPayServiceInterface.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_add:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
int _result = this.add(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_addStudent:
{
data.enforceInterface(DESCRIPTOR);
com.ryx.payment.payplug.bean.Students _arg0;
if ((0!=data.readInt())) {
_arg0 = com.ryx.payment.payplug.bean.Students.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
java.util.List<com.ryx.payment.payplug.bean.Students> _result = this.addStudent(_arg0);
reply.writeNoException();
reply.writeTypedList(_result);
return true;
}
case TRANSACTION_addStudentCallBack:
{
data.enforceInterface(DESCRIPTOR);
com.ryx.payment.payplug.bean.Students _arg0;
if ((0!=data.readInt())) {
_arg0 = com.ryx.payment.payplug.bean.Students.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
com.ryx.payment.payplug.service.ICallBack _arg1;
_arg1 = com.ryx.payment.payplug.service.ICallBack.Stub.asInterface(data.readStrongBinder());
this.addStudentCallBack(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_getCardBalance:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
com.ryx.payment.payplug.service.ICallBack _arg1;
_arg1 = com.ryx.payment.payplug.service.ICallBack.Stub.asInterface(data.readStrongBinder());
this.getCardBalance(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_toOrderPay:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.util.Map _arg1;
java.lang.ClassLoader cl = (java.lang.ClassLoader)this.getClass().getClassLoader();
_arg1 = data.readHashMap(cl);
com.ryx.payment.payplug.service.ICallBack _arg2;
_arg2 = com.ryx.payment.payplug.service.ICallBack.Stub.asInterface(data.readStrongBinder());
this.toOrderPay(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.ryx.payment.payplug.service.IRyxPayServiceInterface
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public int add(int num1, int num2) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(num1);
_data.writeInt(num2);
mRemote.transact(Stub.TRANSACTION_add, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.util.List<com.ryx.payment.payplug.bean.Students> addStudent(com.ryx.payment.payplug.bean.Students student) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<com.ryx.payment.payplug.bean.Students> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((student!=null)) {
_data.writeInt(1);
student.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_addStudent, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArrayList(com.ryx.payment.payplug.bean.Students.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
//这里的in 一定需要的  或者out

@Override public void addStudentCallBack(com.ryx.payment.payplug.bean.Students student, com.ryx.payment.payplug.service.ICallBack callBack) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((student!=null)) {
_data.writeInt(1);
student.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeStrongBinder((((callBack!=null))?(callBack.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_addStudentCallBack, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//这里的in 一定需要的  或者out
//---------------------------以上为测试例子接口,以下接口为正式接口-------------------------------------------

@Override public void getCardBalance(java.lang.String reqCode, com.ryx.payment.payplug.service.ICallBack callBack) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(reqCode);
_data.writeStrongBinder((((callBack!=null))?(callBack.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_getCardBalance, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//调用RyxPay收款台

@Override public void toOrderPay(java.lang.String reqCode, java.util.Map reqMap, com.ryx.payment.payplug.service.ICallBack callBack) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(reqCode);
_data.writeMap(reqMap);
_data.writeStrongBinder((((callBack!=null))?(callBack.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_toOrderPay, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_add = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_addStudent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_addStudentCallBack = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getCardBalance = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_toOrderPay = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
}
public int add(int num1, int num2) throws android.os.RemoteException;
public java.util.List<com.ryx.payment.payplug.bean.Students> addStudent(com.ryx.payment.payplug.bean.Students student) throws android.os.RemoteException;
//这里的in 一定需要的  或者out

public void addStudentCallBack(com.ryx.payment.payplug.bean.Students student, com.ryx.payment.payplug.service.ICallBack callBack) throws android.os.RemoteException;
//这里的in 一定需要的  或者out
//---------------------------以上为测试例子接口,以下接口为正式接口-------------------------------------------

public void getCardBalance(java.lang.String reqCode, com.ryx.payment.payplug.service.ICallBack callBack) throws android.os.RemoteException;
//调用RyxPay收款台

public void toOrderPay(java.lang.String reqCode, java.util.Map reqMap, com.ryx.payment.payplug.service.ICallBack callBack) throws android.os.RemoteException;
}
