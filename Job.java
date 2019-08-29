/**
********************************
Copyright 2013 Proteus Digital Health, Inc.
********************************

CONFIDENTIAL INFORMATION OF PROTEUS DIGITAL HEALTH, INC.

Author : hzhao@proteusdh.com
May 28, 2013
*/

package com.proteus.orientdb.pool;

public interface Job<T> {
    public T doIt(int handlerID) throws Exception;
}