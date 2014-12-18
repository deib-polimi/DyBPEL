// $Header: /gestionale/org.activebpel.rt.bpel.server/src/org/activebpel/rt/bpel/server/engine/storage/IAeCounterStore.java,v 1.1 2009/09/23 13:57:04 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
// PROPRIETARY RIGHTS STATEMENT
// The contents of this file represent confidential information that is the
// proprietary property of Active Endpoints, Inc.  Viewing or use of
// this information is prohibited without the express written consent of
// Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
// is strictly forbidden. Copyright (c) 2002-2005 All rights reserved.
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.rt.bpel.server.engine.storage;

import java.rmi.RemoteException;

/**
 * Defines interface for counter store.
 */
public interface IAeCounterStore extends IAeStorage
{
   /**
    * Returns next block of values for a counter.
    *
    * @return long first new value in block
    * @throws AeStorageException
    * @throws RemoteException
    */
   public long getNextValues(String aCounterName, int aBlockSize) throws AeStorageException, RemoteException;
}
