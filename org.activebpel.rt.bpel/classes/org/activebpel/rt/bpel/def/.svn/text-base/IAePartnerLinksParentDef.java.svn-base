// $Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/def/IAePartnerLinksParentDef.java,v 1.1 2009/09/23 13:09:32 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//               PROPRIETARY RIGHTS STATEMENT
// The contents of this file represent confidential information that is the
// proprietary property of Active Endpoints, Inc.  Viewing or use of
// this information is prohibited without the express written consent of
// Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
// is strictly forbidden. Copyright (c) 2002-2006 All rights reserved.
/////////////////////////////////////////////////////////////////////////////

package org.activebpel.rt.bpel.def;

import java.util.Iterator;

/**
 * Constructs that can be the parent of a 'partnerLinks' container def should implement this
 * interface.
 */
public interface IAePartnerLinksParentDef
{
   /**
    * Gets the partner links container def.
    */
   public AePartnerLinksDef getPartnerLinksDef();

   /**
    * Sets the partner links container def.
    *
    * @param aDef
    */
   public void setPartnerLinksDef(AePartnerLinksDef aDef);

   /**
    * Gets the partner link with the given name.
    *
    * @param aPartnerLinkName
    */
   public AePartnerLinkDef getPartnerLinkDef(String aPartnerLinkName);

   /**
    * Gets the partner link defs.
    */
   public Iterator getPartnerLinkDefs();
}
