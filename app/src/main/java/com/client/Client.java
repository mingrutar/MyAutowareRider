package com.client;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by linna on 11/13/2016.
 */
public abstract class Client {
    private static final String LOG_TAG = Client.class.getSimpleName();
    private static final int TIMEOUT = 5 * 1000;       // 5 sec

    private Socket mSocket;
    private String mAddress;
    private int mPort;

    public boolean isClosed() {
        return (mSocket == null) || ((mSocket!=null) && mSocket.isClosed());
    }
    public void connect(String address, int port) {
        mAddress = address;
        mPort = port;
        if ((mSocket != null) && !mSocket.isClosed() ) {
            this.close();
            mSocket = null;
        }
    }

    private synchronized boolean connect() {
        try {
            mSocket = new Socket(mAddress, mPort);
            mSocket.setSoTimeout(TIMEOUT);
            Log.v(LOG_TAG, "++++ connected to "+mAddress+":"+mPort);
            return true;
        } catch (IOException ex) {
            Log.e(LOG_TAG, "connect to "+mAddress+":"+mPort+" caught exception:"+ex);
            return false;
        }
    }

    public synchronized void close() {
        if (mSocket != null ) {
            try {
                mSocket.close();
            } catch (IOException ex) {
                Log.e(LOG_TAG, "close caught exception:"+ex);
            }
        }
    }

    public void sendInt(int arg0) {
        if (mSocket == null)
            connect();
        if ((mSocket != null) && mSocket.isConnected()) {
            try {
                OutputStream os = mSocket.getOutputStream();
                os.write(arg0);
                os.flush();
                Log.v(LOG_TAG, "++++ sendInt = "+arg0);
            } catch (IOException ex) {
                Log.e(LOG_TAG, "sendInt caught exception:"+ex);
            }
        } else {
            Log.e(LOG_TAG, "sendInt not connected to remote");
        }
    }

    public void sendIntTuple(int arg0, int arg1) {
        if (mSocket == null)
            connect();
        if ((mSocket != null) && mSocket.isConnected()) {
            try {
                OutputStream os = mSocket.getOutputStream();
                os.write(arg0);
                os.write(arg1);
                os.flush();
            } catch (IOException ex) {
                Log.e(LOG_TAG, "sendIntTuple caught exception:"+ex);
            }
        } else {
            Log.e(LOG_TAG, "sendIntTuple not connected to remote");
        }
    }

    public void sendDoubleArray(double arg0[]) {
        if (mSocket == null)
            connect();
        if ((mSocket != null) && mSocket.isConnected()) {
            try {
                DataOutputStream dos = new DataOutputStream(mSocket.getOutputStream());
                for (double d : arg0) {
                    dos.writeDouble(d);
                }
                dos.flush();
                Log.v(LOG_TAG, "++++ sendDoubleArray # double= "+arg0.length);
            } catch (IOException ex) {
                Log.e(LOG_TAG, "sendIntTuple caught exception:"+ex);
            }
        } else {
            Log.e(LOG_TAG, "sendIntTuple not connected to remote");
        }
    }

    public int recvInt() {
        if (mSocket == null)
            connect();
        int ret = -1;
        if ((mSocket != null) && mSocket.isConnected()) {
            try {
                DataInputStream dis = new DataInputStream(mSocket.getInputStream());
                ret = dis.readInt();
                Log.v(LOG_TAG, "++++ recvInt = "+ret);
            } catch (IOException ex) {
                Log.e(LOG_TAG, "sendIntTuple caught exception:" + ex);
            }
        } else {
                Log.e(LOG_TAG, "sendIntTuple not connected to remote");
        }
        return ret;
    }

    public int recvNDT() {
        if (mSocket == null)
            connect();
        int ret = -1;
        if ((mSocket != null) && mSocket.isConnected()) {
            try {
                DataInputStream dis = new DataInputStream(mSocket.getInputStream());
                int[] buf = new int[6];
                for (int i = 0; i < 6; i++) {
                    buf[i] = dis.readInt();
                    Log.v(LOG_TAG, "++++ recvNDT i="+i+", val="+buf[i]);
                }
                return (buf[1] > 5) ? 0 : 1;
            } catch (IOException ex) {
                Log.e(LOG_TAG, "sendIntTuple caught exception:" + ex);
            }
        } else {
            Log.e(LOG_TAG, "sendIntTuple not connected to remote");
        }
        return ret;
    }
}
