// $Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/impl/activity/IAeCopyFromParent.java,v 1.1 2009/09/23 13:09:02 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//               PROPRIETARY RIGHTS STATEMENT
// The contents of this file represent confidential information that is the
// proprietary property of Active Endpoints, Inc.  Viewing or use of
// this information is prohibited without the express written consent of
// Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
// is strictly forbidden. Copyright (c) 2002-2006 All rights reserved.
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.rt.bpel.impl.activity;

import org.activebpel.rt.bpel.impl.activity.assign.IAeFrom;

/**
 * Interface used to set from copy operation, needed in handling virtual copy operations.
 */
public interface IAeCopyFromParent
{
   /**
    * Sets the from child impl.
    * 
    * @param aFrom
    */
   public void setFrom(IAeFrom aFrom);
}
