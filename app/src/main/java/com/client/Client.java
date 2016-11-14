package com.client;

import com.ghostagent.SoundManagementNative;

/**
 * Created by linna on 11/13/2016.
 */
public abstract class Client {
    private static final int TIMEOUT = 5;

    private int sockfd = -1;

    public boolean isClosed() {
        if (sockfd < 0)
            return true;
        else
            return false;
    }

    public synchronized boolean connect(String address, int port) {
        sockfd = SoundManagementNative.socket();
        if (sockfd < 0)
            return false;

        if (SoundManagementNative.connect(sockfd, TIMEOUT, address, port) < 0) {
            close();
            return false;
        }

        return true;
    }

    public synchronized void close() {
        SoundManagementNative.close(sockfd);
        sockfd = -1;
    }

    public void sendInt(int arg0) {
        SoundManagementNative.sendInt(sockfd, TIMEOUT, arg0);
    }

    public void sendIntTuple(int arg0, int arg1) {
        SoundManagementNative.sendIntTuple(sockfd, TIMEOUT, arg0, arg1);
    }

    public void sendDoubleArray(double arg0[]) {
        SoundManagementNative.sendDoubleArray(sockfd, TIMEOUT, arg0);
    }

    public int recvInt() {
        return SoundManagementNative.recvInt(sockfd, TIMEOUT);
    }

    public int recvNDT() {
        return SoundManagementNative.recvNDT(sockfd, TIMEOUT);
    }
}
