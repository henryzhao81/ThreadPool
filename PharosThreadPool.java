/**
********************************
Copyright 2013 Proteus Digital Health, Inc.
********************************

CONFIDENTIAL INFORMATION OF PROTEUS DIGITAL HEALTH, INC.

Author : hzhao@proteusdh.com
May 28, 2013
*/

package com.proteus.orientdb.pool;

public class PharosThreadPool {

    private ConsumerPool pool = null;
    private static PharosThreadPool threadPool = new PharosThreadPool();
    private PharosThreadPool() {
        pool = new ConsumerPool(20);
    }
    
    public static PharosThreadPool getInstance() {
        return threadPool;
    }
    
    public void addTask(Job job) {
        pool.addJob(job);
    }

}
