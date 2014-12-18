//$Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/impl/IAeCompensatableActivity.java,v 1.1 2009/09/23 13:08:28 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//PROPRIETARY RIGHTS STATEMENT
//The contents of this file represent confidential information that is the
//proprietary property of Active Endpoints, Inc.  Viewing or use of
//this information is prohibited without the express written consent of
//Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
//is strictly forbidden. Copyright (c) 2002-2005 All rights reserved.
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.rt.bpel.impl;

import org.activebpel.rt.bpel.AeBusinessProcessException;
import org.activebpel.rt.bpel.impl.activity.support.AeCompInfo;
import org.activebpel.rt.bpel.impl.activity.support.AeCompensationHandler;

/**
 * Interface mark an activity as compensatable.
 */
public interface IAeCompensatableActivity
{

   /**
    * Getter for the AeCompInfo object.
    */
   public AeCompInfo getCompInfo();
   
   /** 
    * Returns the compensation handler. If a handler has not been installed, then
    * an implicit handler will be installed and returned.
    * @return the compensation handler.
    */
   public AeCompensationHandler getCompensationHandler();
   
   /**
    * Terminates currently active compensation handler if the compensation
    * handler is executing.
    */
   public void terminateCompensationHandler() throws AeBusinessProcessException;
}
