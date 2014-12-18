//$Header: /gestionale/org.activebpel.rt.bpel.server/src/org/activebpel/timer/AeAbstractTimerWork.java,v 1.1 2009/09/23 13:57:10 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//PROPRIETARY RIGHTS STATEMENT
//The contents of this file represent confidential information that is the
//proprietary property of Active Endpoints, Inc.  Viewing or use of
//this information is prohibited without the express written consent of
//Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
//is strictly forbidden. Copyright (c) 2002-2005 All rights reserved.
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.timer;

import commonj.timers.Timer;
import commonj.timers.TimerListener;
import commonj.work.WorkException;

import org.activebpel.rt.AeException;
import org.activebpel.rt.bpel.server.AeMessages;
import org.activebpel.rt.bpel.server.engine.AeEngineFactory;
import org.activebpel.work.AeAbstractWork;

/**
 * This abstract class is an adapter used to facilitate scheduling work using the timer facility.
 */
public abstract class AeAbstractTimerWork extends AeAbstractWork implements TimerListener 
{
   /**
    * Called when the timer we scheduled has expired so that we may perform the appropriate work. 
    * @see commonj.timers.TimerListener#timerExpired(commonj.timers.Timer)
    */
   public void timerExpired(Timer aTimer)
   {
      try
      {
         AeEngineFactory.getWorkManager().schedule(this);
      }
      catch (WorkException we)
      {
         AeException.logError(we, AeMessages.getString("AeTimerListenerAdapter.ERROR_ErrorFiringTimer")); //$NON-NLS-1$
      }
   }
   
   /** 
    * The actual timer work to be performed.  
    * Note this work is executed via the engine's work manager.
    */
   abstract public void run();
}