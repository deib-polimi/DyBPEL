//$Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/impl/activity/assign/IAeTo.java,v 1.1 2009/09/23 13:08:38 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//PROPRIETARY RIGHTS STATEMENT
//The contents of this file represent confidential information that is the 
//proprietary property of Active Endpoints, Inc.  Viewing or use of 
//this information is prohibited without the express written consent of 
//Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT 
//is strictly forbidden. Copyright (c) 2002-2006 All rights reserved. 
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.rt.bpel.impl.activity.assign; 

import org.activebpel.rt.attachment.IAeAttachmentContainer;
import org.activebpel.rt.bpel.AeBusinessProcessException;


/**
 * Interface for an impl object that knows how to get the target for a copy operation 
 */
public interface IAeTo
{
   /**
    * Gets the target for the copy operation.
    * @throws AeBusinessProcessException 
    */
   public Object getTarget() throws AeBusinessProcessException;
   
   /**
    * Setter for the copy operation that is the parent of this &lt;to&gt;
    * @param aCopyOperation
    */
   public void setCopyOperation(IAeCopyOperation aCopyOperation);
   
   /**
    * Returns the attachment container associated with the to data
    */
   public IAeAttachmentContainer getAttachmentsTarget();
}
 