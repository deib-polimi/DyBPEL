// $Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/def/AeCompensationHandlerDef.java,v 1.1 2009/09/23 13:09:28 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//               PROPRIETARY RIGHTS STATEMENT
// The contents of this file represent confidential information that is the
// proprietary property of Active Endpoints, Inc.  Viewing or use of
// this information is prohibited without the express written consent of
// Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
// is strictly forbidden. Copyright (c) 2002-2004 All rights reserved.
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.rt.bpel.def;

import org.activebpel.rt.bpel.def.visitors.IAeDefVisitor;


/** 
 * Models the 'compensationHandler' bpel construct.
 */
public class AeCompensationHandlerDef extends AeSingleActivityParentBaseDef 
   implements IAeSingleActivityContainerDef, IAeCompensateParentDef, 
              IAeFCTHandlerDef, IAeUncrossableLinkBoundary
{
   /**
    * Default c'tor.
    */
   public AeCompensationHandlerDef()
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

   /**
    * @see org.activebpel.rt.bpel.def.IAeUncrossableLinkBoundary#canCrossInbound()
    */
   public boolean canCrossInbound()
   {
      return false;
   }

   /**
    * @see org.activebpel.rt.bpel.def.IAeUncrossableLinkBoundary#canCrossOutbound()
    */
   public boolean canCrossOutbound()
   {
      return false;
   }
}
