// $Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/def/activity/support/AeTargetDef.java,v 1.1 2009/09/23 13:09:03 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//               PROPRIETARY RIGHTS STATEMENT
// The contents of this file represent confidential information that is the
// proprietary property of Active Endpoints, Inc.  Viewing or use of
// this information is prohibited without the express written consent of
// Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
// is strictly forbidden. Copyright (c) 2002-2004 All rights reserved.
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.rt.bpel.def.activity.support;

import org.activebpel.rt.bpel.def.AeBaseDef;
import org.activebpel.rt.bpel.def.visitors.IAeDefVisitor;

/**
 * Definition base for all named definition elements
 */
public class AeTargetDef extends AeBaseDef
{
   // persistent attributes of the definition
   private String mLinkName;

   /**
    * Default constructor
    */
   public AeTargetDef()
   {
      super();
   }

   /**
    * Accessor method to obtain link name of this object.
    * 
    * @return name of link
    */
   public String getLinkName()
   {
      return mLinkName;
   }

   /**
    * Mutator method to set the link name of this object.
    * 
    * @param aLinkName name of the link
    */
   public void setLinkName(String aLinkName)
   {
      mLinkName = aLinkName;
   }
   
   /**
    * @see org.activebpel.rt.bpel.def.AeBaseDef#accept(org.activebpel.rt.bpel.def.visitors.IAeDefVisitor)
    */
   public void accept(IAeDefVisitor aVisitor)
   {
      aVisitor.visit(this);
   }
}
