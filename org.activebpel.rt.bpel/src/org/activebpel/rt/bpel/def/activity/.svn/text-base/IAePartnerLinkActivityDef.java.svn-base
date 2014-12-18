//$Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/def/activity/IAePartnerLinkActivityDef.java,v 1.1 2009/09/23 13:09:47 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//PROPRIETARY RIGHTS STATEMENT
//The contents of this file represent confidential information that is the
//proprietary property of Active Endpoints, Inc.  Viewing or use of
//this information is prohibited without the express written consent of
//Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
//is strictly forbidden. Copyright (c) 2002-2004 All rights reserved.
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.rt.bpel.def.activity;

import javax.xml.namespace.QName;

import org.activebpel.rt.bpel.def.AePartnerLinkOpKey;

/**
 * Provides interface for defs that model a partner link operation like a <code>receive</code>,
 * <code>onMessage</code>, <code>invoke</code>, and <code>reply</code>.
 */
public interface IAePartnerLinkActivityDef
{
   /**
    * Gets the name of the partner link.
    */
   public String getPartnerLink();

   /**
    * Gets the port type.
    */
   public QName getPortType();

   /**
    * Gets the operation
    */
   public String getOperation();

   /**
    * Returns the partnerlink:operation key for this activity.
    */
   public AePartnerLinkOpKey getPartnerLinkOperationKey();
}
