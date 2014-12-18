// $Header: /gestionale/org.activebpel.rt.bpel.server/src/org/activebpel/rt/bpel/server/catalog/resource/IAeResourceStats.java,v 1.1 2009/09/23 13:57:21 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//               PROPRIETARY RIGHTS STATEMENT
// The contents of this file represent confidential information that is the
// proprietary property of Active Endpoints, Inc.  Viewing or use of
// this information is prohibited without the express written consent of
// Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
// is strictly forbidden. Copyright (c) 2002-2004 All rights reserved.
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.rt.bpel.server.catalog.resource;


/**
 * Interface for tracking cache performance.
 */
public interface IAeResourceStats
{
   
   /**
    * Log a read from disk.
    */
   public void logDiskRead( );
   
   /**
    * Log cache access.
    */
   public void logTotalRead( );
   
   /**
    * Accessor for total reads.
    */
   public int getTotalReads();
   
   /**
    * Accessor for disk reads.
    */
   public int getDiskReads();

}
