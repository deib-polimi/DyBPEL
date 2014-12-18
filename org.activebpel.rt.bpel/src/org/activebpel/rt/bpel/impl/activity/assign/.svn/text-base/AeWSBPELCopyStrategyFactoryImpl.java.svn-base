// $Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/impl/activity/assign/AeWSBPELCopyStrategyFactoryImpl.java,v 1.1 2009/09/23 13:08:38 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//               PROPRIETARY RIGHTS STATEMENT
// The contents of this file represent confidential information that is the
// proprietary property of Active Endpoints, Inc.  Viewing or use of
// this information is prohibited without the express written consent of
// Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
// is strictly forbidden. Copyright (c) 2002-2006 All rights reserved.
/////////////////////////////////////////////////////////////////////////////

package org.activebpel.rt.bpel.impl.activity.assign;

import org.activebpel.rt.bpel.def.IAeBPELConstants;
import org.w3c.dom.Element;

/**
 * A WSBPEL version of a copy strategy factory.
 */
public class AeWSBPELCopyStrategyFactoryImpl extends AeAbstractCopyStrategyFactoryImpl
{
   /**
    * Overrides the no-op method in super to add check for xsi:nil attribute on
    * EII to AII and EII to TII copy operations.
    * 
    * @see org.activebpel.rt.bpel.impl.activity.assign.AeAbstractCopyStrategyFactoryImpl#adjustIndex(org.activebpel.rt.bpel.impl.activity.assign.AeAbstractCopyStrategyFactoryImpl.AeStrategyIndex)
    */
   protected void adjustIndex(AeStrategyIndex aIndex)
   {
      if (aIndex.getFromType() == ELEMENT_TYPE && (aIndex.getToType() == TEXT_TYPE || aIndex.getToType() == ATTR_TYPE))
      {
         Element elem = (Element) aIndex.getFromData();
         if (elem.getAttributeNS(IAeBPELConstants.W3C_XML_SCHEMA_INSTANCE, "nil").equals("true")) //$NON-NLS-1$ //$NON-NLS-2$
         {
            aIndex.setFromType(NULL);
         }
      }
   }
}
