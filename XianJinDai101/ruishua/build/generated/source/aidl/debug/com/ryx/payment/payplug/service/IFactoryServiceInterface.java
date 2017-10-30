/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\RuiYinXin\\XianJinDai101\\ruishua\\src\\main\\aidl\\com\\ryx\\payment\\payplug\\service\\IFactoryServiceInterface.aidl
 */
package com.ryx.payment.payplug.service;
public interface IFactoryServiceInterface extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.ryx.payment.payplug.service.IFactoryServiceInterface
{
private static final java.lang.String DESCRIPTOR = "com.ryx.payment.payplug.service.IFactoryServiceInterface";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.ryx.payment.payplug.service.IFactoryServiceInterface interface,
 * generating a proxy if needed.
 */
public static com.ryx.payment.payplug.service.IFactoryServiceInterface asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.ryx.payment.payplug.service.IFactoryServiceInterface))) {
return ((com.ryx.payment.payplug.service.IFactoryServiceInterface)iin);
}
return new com.ryx.payment.payplug.service.IFactoryServiceInterface.Stub.Proxy(obj);
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
case TRANSACTION_getMyAidlService:
{
data.enforceInterface(DESCRIPTOR);
com.ryx.payment.payplug.service.IRyxPayServiceInterface _result = this.getMyAidlService();
reply.writeNoException();
reply.writeStrongBinder((((_result!=null))?(_result.asBinder()):(null)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.ryx.payment.payplug.service.IFactoryServiceInterface
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
@Override public com.ryx.payment.payplug.service.IRyxPayServiceInterface getMyAidlService() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
com.ryx.payment.payplug.service.IRyxPayServiceInterface _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getMyAidlService, _data, _reply, 0);
_reply.readException();
_result = com.ryx.payment.payplug.service.IRyxPayServiceInterface.Stub.asInterface(_reply.readStrongBinder());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_getMyAidlService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public com.ryx.payment.payplug.service.IRyxPayServiceInterface getMyAidlService() throws android.os.RemoteException;
}
