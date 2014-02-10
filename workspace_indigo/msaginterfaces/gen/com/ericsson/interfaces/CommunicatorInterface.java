/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /root/workspace_indigo/msaginterfaces/src/com/ericsson/interfaces/CommunicatorInterface.aidl
 */
package com.ericsson.interfaces;
//import ericsson.msag.ObservedData;

public interface CommunicatorInterface extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.ericsson.interfaces.CommunicatorInterface
{
private static final java.lang.String DESCRIPTOR = "com.ericsson.interfaces.CommunicatorInterface";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.ericsson.interfaces.CommunicatorInterface interface,
 * generating a proxy if needed.
 */
public static com.ericsson.interfaces.CommunicatorInterface asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.ericsson.interfaces.CommunicatorInterface))) {
return ((com.ericsson.interfaces.CommunicatorInterface)iin);
}
return new com.ericsson.interfaces.CommunicatorInterface.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
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
case TRANSACTION_getDesc:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String[] _result = this.getDesc();
reply.writeNoException();
reply.writeStringArray(_result);
return true;
}
case TRANSACTION_getData:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String[] _result = this.getData();
reply.writeNoException();
reply.writeStringArray(_result);
return true;
}
case TRANSACTION_getCurrentCache:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String[] _result = this.getCurrentCache();
reply.writeNoException();
reply.writeStringArray(_result);
return true;
}
case TRANSACTION_getCacheItem:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.getCacheItem(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_store:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
boolean _result = this.store(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getLatestCacheItem:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _result = this.getLatestCacheItem(_arg0, _arg1);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getCacheItemsInPeriod:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
java.lang.String _arg3;
_arg3 = data.readString();
java.lang.String[] _result = this.getCacheItemsInPeriod(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
reply.writeStringArray(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.ericsson.interfaces.CommunicatorInterface
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public java.lang.String[] getDesc() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getDesc, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public java.lang.String[] getData() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getData, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public java.lang.String[] getCurrentCache() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getCurrentCache, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public java.lang.String getCacheItem(java.lang.String name) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(name);
mRemote.transact(Stub.TRANSACTION_getCacheItem, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean store(java.lang.String filename, java.lang.String data) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(filename);
_data.writeString(data);
mRemote.transact(Stub.TRANSACTION_store, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public java.lang.String getLatestCacheItem(java.lang.String sensorType, java.lang.String deviceId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(sensorType);
_data.writeString(deviceId);
mRemote.transact(Stub.TRANSACTION_getLatestCacheItem, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public java.lang.String[] getCacheItemsInPeriod(java.lang.String sensorType, java.lang.String deviceId, java.lang.String t1, java.lang.String t2) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(sensorType);
_data.writeString(deviceId);
_data.writeString(t1);
_data.writeString(t2);
mRemote.transact(Stub.TRANSACTION_getCacheItemsInPeriod, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_getDesc = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getData = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getCurrentCache = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getCacheItem = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_store = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_getLatestCacheItem = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_getCacheItemsInPeriod = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
}
public java.lang.String[] getDesc() throws android.os.RemoteException;
public java.lang.String[] getData() throws android.os.RemoteException;
public java.lang.String[] getCurrentCache() throws android.os.RemoteException;
public java.lang.String getCacheItem(java.lang.String name) throws android.os.RemoteException;
public boolean store(java.lang.String filename, java.lang.String data) throws android.os.RemoteException;
public java.lang.String getLatestCacheItem(java.lang.String sensorType, java.lang.String deviceId) throws android.os.RemoteException;
public java.lang.String[] getCacheItemsInPeriod(java.lang.String sensorType, java.lang.String deviceId, java.lang.String t1, java.lang.String t2) throws android.os.RemoteException;
}
