// $Header: /gestionale/org.activebpel.rt.bpel.server/src/org/activebpel/rt/bpel/server/engine/process/IAeProcessStateReader.java,v 1.1 2009/09/23 13:57:22 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
// PROPRIETARY RIGHTS STATEMENT
// The contents of this file represent confidential information that is the
// proprietary property of Active Endpoints, Inc.  Viewing or use of
// this information is prohibited without the express written consent of
// Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
// is strictly forbidden. Copyright (c) 2002-2005 All rights reserved.
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.rt.bpel.server.engine.process;

import org.activebpel.rt.bpel.AeBusinessProcessException;
import org.activebpel.rt.bpel.IAeBusinessProcess;

/**
 * Defines interface for reading process state from storage.
 */
public interface IAeProcessStateReader
{
   /**
    * Reads the state of the given process from storage.
    *
    * @param aProcess
    */
   public void readProcess(IAeBusinessProcess aProcess) throws AeBusinessProcessException;
}
