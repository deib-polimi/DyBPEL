// $Header: /gestionale/org.activebpel.rt.bpel.server/src/org/activebpel/rt/bpel/server/admin/rdebug/client/IAeEventHandlerService.java,v 1.1 2009/09/23 13:57:24 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//               PROPRIETARY RIGHTS STATEMENT
// The contents of this file represent confidential information that is the
// proprietary property of Active Endpoints, Inc.  Viewing or use of
// this information is prohibited without the express written consent of
// Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
// is strictly forbidden. Copyright (c) 2002-2004 All rights reserved.
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.rt.bpel.server.admin.rdebug.client;

import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

/**
 * The interface which defines the remote debug service.
 */
public interface IAeEventHandlerService extends Service
{
   /**
    * Returns the address of the remote debug service.
    */
   public String getRemoteDebugAddress();

   /**
    * Returns the interface for the web service methods of the remote debugger. 
    * @throws ServiceException
    */
   public IAeEventHandler getRemoteDebugService() throws ServiceException;
}
