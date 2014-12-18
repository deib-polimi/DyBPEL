// $Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/def/activity/support/AeFromPartDef.java,v 1.1 2009/09/23 13:09:05 zampognaro Exp $
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
 * Models the invoke|receive|onMessage|onEvent 'fromPart' bpel construct introduced in WS-BPEL 2.0.
 */
public class AeFromPartDef extends AeBaseDef
{
   /** The 'part' attribute. */
   private String mPart;
   /** The 'toVariable' attribute. */
   private String mToVariable;

   /**
    * Default c'tor.
    */
   public AeFromPartDef()
   {
      super();
   }

   /**
    * @return Returns the part.
    */
   public String getPart()
   {
      return mPart;
   }

   /**
    * @param aPart The part to set.
    */
   public void setPart(String aPart)
   {
      mPart = aPart;
   }

   /**
    * @return Returns the toVariable.
    */
   public String getToVariable()
   {
      return mToVariable;
   }

   /**
    * @param aToVariable The toVariable to set.
    */
   public void setToVariable(String aToVariable)
   {
      mToVariable = aToVariable;
   }
   
   /**
    * @see org.activebpel.rt.bpel.def.AeBaseDef#accept(org.activebpel.rt.bpel.def.visitors.IAeDefVisitor)
    */
   public void accept(IAeDefVisitor aVisitor)
   {
      aVisitor.visit(this);
   }
}
