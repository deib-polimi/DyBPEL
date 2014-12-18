// $Header: /gestionale/org.activebpel.rt.bpel.server/src/org/activebpel/rt/bpel/server/deploy/bpr/AeUnpackedBprContext.java,v 1.1 2009/09/23 13:57:24 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//               PROPRIETARY RIGHTS STATEMENT
// The contents of this file represent confidential information that is the
// proprietary property of Active Endpoints, Inc.  Viewing or use of
// this information is prohibited without the express written consent of
// Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
// is strictly forbidden. Copyright (c) 2002-2004 All rights reserved.
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.rt.bpel.server.deploy.bpr;

import java.net.URL;


/**
 * Allows short name to be specified in x-tor.
 */
public class AeUnpackedBprContext extends AeBprContext
{
   /** Short name. */
   private String mShortName;
   
   /**
    * Constructor.
    * @param aURL
    * @param aLoader
    * @param aName
    */
   public AeUnpackedBprContext( URL aURL, ClassLoader aLoader, String aName )
   {
      super( aURL, aURL, aLoader );
      mShortName = aName;
   }
   
   /**
    * @see org.activebpel.rt.bpel.server.deploy.IAeDeploymentContext#getShortName()
    */
   public String getShortName()
   {
      return mShortName;
   }
}
