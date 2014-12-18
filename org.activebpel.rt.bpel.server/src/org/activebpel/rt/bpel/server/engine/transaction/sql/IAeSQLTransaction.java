//$Header: /gestionale/org.activebpel.rt.bpel.server/src/org/activebpel/rt/bpel/server/engine/transaction/sql/IAeSQLTransaction.java,v 1.1 2009/09/23 13:57:19 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//PROPRIETARY RIGHTS STATEMENT
//The contents of this file represent confidential information that is the
//proprietary property of Active Endpoints, Inc.  Viewing or use of
//this information is prohibited without the express written consent of
//Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
//is strictly forbidden. Copyright (c) 2002-2006 All rights reserved.
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.rt.bpel.server.engine.transaction.sql;

import java.sql.Connection;
import java.sql.SQLException;

import org.activebpel.rt.bpel.server.engine.transaction.IAeTransaction;

/**
 * Defines interface for transactions that can return database connections.
 */
public interface IAeSQLTransaction extends IAeTransaction
{
   /**
    * Return database connection for current transaction.
    */
   public Connection getConnection() throws SQLException;
}
