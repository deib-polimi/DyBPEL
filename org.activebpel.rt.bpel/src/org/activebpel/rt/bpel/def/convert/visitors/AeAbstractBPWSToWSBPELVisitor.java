// $Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/def/convert/visitors/AeAbstractBPWSToWSBPELVisitor.java,v 1.1 2009/09/23 13:10:04 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//               PROPRIETARY RIGHTS STATEMENT
// The contents of this file represent confidential information that is the
// proprietary property of Active Endpoints, Inc.  Viewing or use of
// this information is prohibited without the express written consent of
// Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
// is strictly forbidden. Copyright (c) 2002-2006 All rights reserved.
/////////////////////////////////////////////////////////////////////////////

package org.activebpel.rt.bpel.def.convert.visitors;

import org.activebpel.rt.bpel.def.visitors.AeAbstractDefVisitor;
import org.activebpel.rt.bpel.def.visitors.AeDefTraverser;
import org.activebpel.rt.bpel.def.visitors.AeTraversalVisitor;

/**
 * A base class for all BPWS --> WS-BPEL 2.0 visitors.
 */
public abstract class AeAbstractBPWSToWSBPELVisitor extends AeAbstractDefVisitor
{
   /**
    * Constructor.
    */
   public AeAbstractBPWSToWSBPELVisitor()
   {
      setTraversalVisitor( new AeTraversalVisitor( new AeDefTraverser(), this ) );
   }
}
