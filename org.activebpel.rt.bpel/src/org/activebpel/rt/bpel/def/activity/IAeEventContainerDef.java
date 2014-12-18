//$Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/def/activity/IAeEventContainerDef.java,v 1.1 2009/09/23 13:09:47 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//PROPRIETARY RIGHTS STATEMENT
//The contents of this file represent confidential information that is the 
//proprietary property of Active Endpoints, Inc.  Viewing or use of 
//this information is prohibited without the express written consent of 
//Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT 
//is strictly forbidden. Copyright (c) 2002-2006 All rights reserved. 
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.rt.bpel.def.activity; 

import java.util.Iterator;

import org.activebpel.rt.bpel.def.activity.support.AeOnEventDef;

/**
 * Interface for &lt;eventHandlers&gt;.
 */
public interface IAeEventContainerDef extends IAeAlarmParentDef
{
   /**
    * Gets an iterator over the onEvent defs.
    */
   public Iterator getOnEventDefs();

   /**
    * Adds the event.
    * 
    * @param aEvent
    */
   public void addOnEventDef(AeOnEventDef aEvent);
}
