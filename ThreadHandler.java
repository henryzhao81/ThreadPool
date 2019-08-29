/**
********************************
Copyright 2013 Proteus Digital Health, Inc.
********************************

CONFIDENTIAL INFORMATION OF PROTEUS DIGITAL HEALTH, INC.

Author : hzhao@proteusdh.com
May 28, 2013
*/

package com.proteus.orientdb.pool;

public class ThreadHandler implements Runnable {
    // static public boolean isRunning = true;
    static public long WAIT_TIMEOUT = 600000; // 10 minutes
    //static private int threadCounter = 0;
    // object's monitor
    protected Object handlerMutex = new Object();
    private ConsumerPool pool;
    //protected int id;
    public int handlerID = 0;

    protected ThreadHandler(ConsumerPool pool, int handlerID) {
        this.pool = pool;
        this.handlerID = handlerID;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Job job = this.pool.getNextJob();
                if (job == null) {
                    synchronized (handlerMutex) {
                        this.pool.addToWaitQueue(this);
                        long sTime = System.currentTimeMillis();
                        try {
                            handlerMutex.wait(WAIT_TIMEOUT);
                        } catch (InterruptedException ie) {
                            // remove from queue if wake up by exception
                            this.pool.removeFromWaitQueue(this);
                        }
                        long dt = System.currentTimeMillis() - sTime;
                        if (dt >= WAIT_TIMEOUT){
                            // remove from queue if wake up by timeout
                            this.pool.removeFromWaitQueue(this);
                        }
                    }
                } else {
                    job.doIt(this.handlerID);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
