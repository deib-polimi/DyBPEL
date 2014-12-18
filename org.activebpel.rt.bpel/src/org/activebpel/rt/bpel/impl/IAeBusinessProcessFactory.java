// $Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/impl/IAeBusinessProcessFactory.java,v 1.1 2009/09/23 13:08:33 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//               PROPRIETARY RIGHTS STATEMENT
// The contents of this file represent confidential information that is the
// proprietary property of Active Endpoints, Inc.  Viewing or use of
// this information is prohibited without the express written consent of
// Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
// is strictly forbidden. Copyright (c) 2002-2004 All rights reserved.
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.rt.bpel.impl;

import org.activebpel.rt.bpel.IAeBusinessProcess;

/**
 * Factory for creating business process instances
 */
public interface IAeBusinessProcessFactory
{
   /**
    * Creates a new business process instance
    */
   public IAeBusinessProcess createProcess(long aPid, IAeProcessPlan aPlan);
}
