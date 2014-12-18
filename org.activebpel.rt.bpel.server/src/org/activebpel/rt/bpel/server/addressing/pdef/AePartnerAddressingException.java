// $Header: /gestionale/org.activebpel.rt.bpel.server/src/org/activebpel/rt/bpel/server/addressing/pdef/AePartnerAddressingException.java,v 1.1 2009/09/23 13:57:13 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//               PROPRIETARY RIGHTS STATEMENT
// The contents of this file represent confidential information that is the
// proprietary property of Active Endpoints, Inc.  Viewing or use of
// this information is prohibited without the express written consent of
// Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
// is strictly forbidden. Copyright (c) 2002-2004 All rights reserved.
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.rt.bpel.server.addressing.pdef;

import org.activebpel.rt.bpel.server.deploy.AeDeploymentException;

/**
 * General exception for partner definition addressing layer.
 */
public class AePartnerAddressingException extends AeDeploymentException
{
   
   /**
    * Constructor.
    * 
    * @param aMessage
    */
   public AePartnerAddressingException(String aMessage)
   {
      super(aMessage);
   }


   /**
    * Constructor.
    * 
    * @param aMessage
    * @param aCause
    */
   public AePartnerAddressingException(String aMessage, Throwable aCause)
   {
      super(aMessage, aCause);
   }

}
