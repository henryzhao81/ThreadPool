/**
********************************
Copyright 2013 Proteus Digital Health, Inc.
********************************

CONFIDENTIAL INFORMATION OF PROTEUS DIGITAL HEALTH, INC.

Author : hzhao@proteusdh.com
May 28, 2013
*/

package com.proteus.orientdb.pool;

import java.util.Vector;

public class ConsumerPool {
    //Store jobs
    public final Vector jobQueue = new Vector();
    //Store handler
    public final Vector waitPool = new Vector();

    private Object jobMutex = new Object();

    private Object poolMutex = new Object();

    public ConsumerPool(int poolSize) {
        for (int i = 0; i < poolSize; i++) {
            ThreadHandler handler = new ThreadHandler(this, i);
            Thread t = new Thread(handler, "poolThread" + i);
            t.start();
        }
    }

    public int addJob(Job job) {
        int poolSize;
        synchronized (jobMutex) {
            this.jobQueue.addElement(job);
            poolSize = this.jobQueue.size();
        }
        ThreadHandler handler = null;
        synchronized (poolMutex) {
            if (this.waitPool.size() > 0) {
                handler = (ThreadHandler) this.waitPool.elementAt(0);
            }
        }
        if (handler != null) {
            synchronized (handler.handlerMutex) {
                handler.handlerMutex.notify();
                removeFromWaitQueue(handler);
            }
        }
        return poolSize;
    }

    protected void addToWaitQueue(ThreadHandler handler) {
        synchronized (this.poolMutex) {
            if (!this.waitPool.contains(handler)) {
                this.waitPool.addElement(handler);
            }
        }
    }

    protected void removeFromWaitQueue(ThreadHandler handler) {
        synchronized (this.poolMutex) {
            this.waitPool.removeElement(handler);
        }
    }

    public Job getNextJob() {
        Job job = null;
        synchronized (jobMutex) {
            if (this.jobQueue.size() > 0) {
                job = (Job) this.jobQueue.elementAt(0);
                this.jobQueue.removeElementAt(0);
            }
        }
        return job;

    }
}

