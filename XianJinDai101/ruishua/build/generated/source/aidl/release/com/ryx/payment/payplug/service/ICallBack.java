/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\RuiYinXin\\XianJinDai101\\ruishua\\src\\main\\aidl\\com\\ryx\\payment\\payplug\\service\\ICallBack.aidl
 */
package com.ryx.payment.payplug.service;
public interface ICallBack extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.ryx.payment.payplug.service.ICallBack
{
private static final java.lang.String DESCRIPTOR = "com.ryx.payment.payplug.service.ICallBack";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.ryx.payment.payplug.service.ICallBack interface,
 * generating a proxy if needed.
 */
public static com.ryx.payment.payplug.service.ICallBack asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.ryx.payment.payplug.service.ICallBack))) {
return ((com.ryx.payment.payplug.service.ICallBack)iin);
}
return new com.ryx.payment.payplug.service.ICallBack.Stub.Proxy(obj);
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
case TRANSACTION_handleByServer:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.handleByServer(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_parcelableBeanCallBack:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<com.ryx.payment.payplug.bean.Students> _arg0;
_arg0 = data.createTypedArrayList(com.ryx.payment.payplug.bean.Students.CREATOR);
this.parcelableBeanCallBack(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_resultCallBack:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.util.Map _arg1;
java.lang.ClassLoader cl = (java.lang.ClassLoader)this.getClass().getClassLoader();
_arg1 = data.readHashMap(cl);
this.resultCallBack(_arg0, _arg1);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.ryx.payment.payplug.service.ICallBack
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
@Override public void handleByServer(java.lang.String param) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(param);
mRemote.transact(Stub.TRANSACTION_handleByServer, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void parcelableBeanCallBack(java.util.List<com.ryx.payment.payplug.bean.Students> students) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeTypedList(students);
mRemote.transact(Stub.TRANSACTION_parcelableBeanCallBack, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//---------------------------以上为测试例子接口,以下接口为正式接口-------------------------------------------

@Override public void resultCallBack(java.lang.String reqCode, java.util.Map resultmap) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(reqCode);
_data.writeMap(resultmap);
mRemote.transact(Stub.TRANSACTION_resultCallBack, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_handleByServer = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_parcelableBeanCallBack = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_resultCallBack = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
public void handleByServer(java.lang.String param) throws android.os.RemoteException;
public void parcelableBeanCallBack(java.util.List<com.ryx.payment.payplug.bean.Students> students) throws android.os.RemoteException;
//---------------------------以上为测试例子接口,以下接口为正式接口-------------------------------------------

public void resultCallBack(java.lang.String reqCode, java.util.Map resultmap) throws android.os.RemoteException;
}
