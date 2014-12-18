//$Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/IAeBaseProcessEvent.java,v 1.1 2009/09/23 13:09:39 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//PROPRIETARY RIGHTS STATEMENT
//The contents of this file represent confidential information that is the 
//proprietary property of Active Endpoints, Inc.  Viewing or use of 
//this information is prohibited without the express written consent of 
//Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT 
//is strictly forbidden. Copyright (c) 2002-2004 All rights reserved. 
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.rt.bpel; 

/**
 * A base interface for process events. 
 */
public interface IAeBaseProcessEvent extends IAeEvent
{
   /**
    * Returns the node path related to this event.
    */
   public String getNodePath();
   
   /**
    * Returns the ID of this event.
    */
   public int getEventID();
   
   /**
    * Returns the name of the Fault associated with this event, or empty string.
    */
   public String getFaultName();
   
   /**
    * Returns ancilliary information which may have been specified for the process event.
    */
   public String getAncillaryInfo();
   
   /**
    * Returns the process ID of the engine instance that broadcast this event.
    */
   public long getPID();
}
 