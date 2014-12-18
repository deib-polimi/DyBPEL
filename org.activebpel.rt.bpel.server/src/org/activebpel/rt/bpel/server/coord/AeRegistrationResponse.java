//$Header: /gestionale/org.activebpel.rt.bpel.server/src/org/activebpel/rt/bpel/server/coord/AeRegistrationResponse.java,v 1.1 2009/09/23 13:57:15 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//PROPRIETARY RIGHTS STATEMENT
//The contents of this file represent confidential information that is the
//proprietary property of Active Endpoints, Inc.  Viewing or use of
//this information is prohibited without the express written consent of
//Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
//is strictly forbidden. Copyright (c) 2002-2005 All rights reserved.
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.rt.bpel.server.coord;

import org.activebpel.rt.bpel.coord.IAeCoordinating;
import org.activebpel.rt.bpel.coord.IAeRegistrationResponse;

/**
 * Basic implementation of a registration response.
 */
public class AeRegistrationResponse extends AeContextBase implements IAeRegistrationResponse
{

   /**
    * Default constructor.
    */
   public AeRegistrationResponse()
   {
      super();
   }

   /**
    * Overrides method to 
    * @see org.activebpel.rt.bpel.coord.IAeRegistrationRequest#getProtocolIdentifier()
    */
   public String getProtocolIdentifier()
   {
      return getProperty(IAeCoordinating.WSCOORD_PROTOCOL);
   }

   /**
    * Overrides method to 
    * @see org.activebpel.rt.bpel.coord.IAeRegistrationRequest#setProtocolIdentifier(java.lang.String)
    */
   public void setProtocolIdentifier(String aProtocolId)
   {
      setProperty(IAeCoordinating.WSCOORD_PROTOCOL, aProtocolId);
   }   
}
