// $Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/def/IAeEventHandlersParentDef.java,v 1.1 2009/09/23 13:09:28 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//               PROPRIETARY RIGHTS STATEMENT
// The contents of this file represent confidential information that is the
// proprietary property of Active Endpoints, Inc.  Viewing or use of
// this information is prohibited without the express written consent of
// Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
// is strictly forbidden. Copyright (c) 2002-2006 All rights reserved.
/////////////////////////////////////////////////////////////////////////////

package org.activebpel.rt.bpel.def;

/**
 * Defs that can be a parent of the 'eventHandlers' construct should implement this
 * interface.
 */
public interface IAeEventHandlersParentDef
{
   /**
    * Returns the event handlers child def.
    */
   public AeEventHandlersDef getEventHandlersDef();

   /**
    * Sets the event handlers child def.
    * 
    * @param aEventHandlers
    */
   public void setEventHandlers(AeEventHandlersDef aEventHandlers);
}
