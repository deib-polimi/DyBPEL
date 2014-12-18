// $Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/IAeEvent.java,v 1.1 2009/09/23 13:09:39 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//               PROPRIETARY RIGHTS STATEMENT
// The contents of this file represent confidential information that is the
// proprietary property of Active Endpoints, Inc.  Viewing or use of
// this information is prohibited without the express written consent of
// Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
// is strictly forbidden. Copyright (c) 2002-2006 All rights reserved.
/////////////////////////////////////////////////////////////////////////////

package org.activebpel.rt.bpel;

import java.util.Date;

/**
 * An interface for a generic event.
 */
public interface IAeEvent
{
   /**
    * Returns the event's timestamp.
    */
   public Date getTimestamp();
}
