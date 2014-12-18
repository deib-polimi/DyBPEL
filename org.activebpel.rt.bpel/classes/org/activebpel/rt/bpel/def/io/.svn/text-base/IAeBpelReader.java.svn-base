// $Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/def/io/IAeBpelReader.java,v 1.1 2009/09/23 13:09:17 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//               PROPRIETARY RIGHTS STATEMENT
// The contents of this file represent confidential information that is the
// proprietary property of Active Endpoints, Inc.  Viewing or use of
// this information is prohibited without the express written consent of
// Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
// is strictly forbidden. Copyright (c) 2002-2004 All rights reserved.
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.rt.bpel.def.io;

import org.activebpel.rt.AeException;
import org.activebpel.rt.bpel.def.AeProcessDef;
import org.w3c.dom.Document;

/**
 * Interface for working with BPEL deserializers. 
 */
public interface IAeBpelReader
{
   /**
    * Deserializes the BPEL xml into its corresponding
    * AeDef object model.
    * @param aBpelDoc validated BPEL is expected
    * @return <code>AeProcessDef</code>
    * @throws AeException 
    */
   public AeProcessDef readBPEL( Document aBpelDoc ) throws AeException;
}
