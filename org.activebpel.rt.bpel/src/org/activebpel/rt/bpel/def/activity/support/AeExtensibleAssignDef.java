// $Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/def/activity/support/AeExtensibleAssignDef.java,v 1.1 2009/09/23 13:09:05 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//               PROPRIETARY RIGHTS STATEMENT
// The contents of this file represent confidential information that is the
// proprietary property of Active Endpoints, Inc.  Viewing or use of
// this information is prohibited without the express written consent of
// Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
// is strictly forbidden. Copyright (c) 2002-2006 All rights reserved.
/////////////////////////////////////////////////////////////////////////////

package org.activebpel.rt.bpel.def.activity.support;

import org.activebpel.rt.bpel.def.AeBaseDef;
import org.activebpel.rt.bpel.def.visitors.IAeDefVisitor;

/**
 * Models the extensibleAssign construct introduced in WS-BPEL 2.0.
 */
public class AeExtensibleAssignDef extends AeBaseDef
{
   /**
    * Default c'tor.
    */
   public AeExtensibleAssignDef()
   {
      super();
   }

   /**
    * @see org.activebpel.rt.bpel.def.AeBaseDef#accept(org.activebpel.rt.bpel.def.visitors.IAeDefVisitor)
    */
   public void accept(IAeDefVisitor aVisitor)
   {
      aVisitor.visit(this);
   }
}
