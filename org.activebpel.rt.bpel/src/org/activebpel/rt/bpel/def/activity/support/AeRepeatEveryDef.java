// $Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/def/activity/support/AeRepeatEveryDef.java,v 1.1 2009/09/23 13:09:04 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//               PROPRIETARY RIGHTS STATEMENT
// The contents of this file represent confidential information that is the
// proprietary property of Active Endpoints, Inc.  Viewing or use of
// this information is prohibited without the express written consent of
// Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
// is strictly forbidden. Copyright (c) 2002-2006 All rights reserved.
/////////////////////////////////////////////////////////////////////////////

package org.activebpel.rt.bpel.def.activity.support;

import org.activebpel.rt.bpel.def.visitors.IAeDefVisitor;

/**
 * Models the 'repeatEvery' bpel construct introduced in WS-BPEL 2.0.
 */
public class AeRepeatEveryDef extends AeForDef
{
   /**
    * Default c'tor.
    */
   public AeRepeatEveryDef()
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
