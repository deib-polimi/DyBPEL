//$Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/def/validation/activity/links/AeSourcesValidator.java,v 1.1 2009/09/23 13:09:34 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//PROPRIETARY RIGHTS STATEMENT
//The contents of this file represent confidential information that is the 
//proprietary property of Active Endpoints, Inc.  Viewing or use of 
//this information is prohibited without the express written consent of 
//Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT 
//is strictly forbidden. Copyright (c) 2002-2006 All rights reserved. 
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.rt.bpel.def.validation.activity.links; 

import org.activebpel.rt.bpel.def.activity.support.AeSourcesDef;
import org.activebpel.rt.bpel.def.validation.AeBaseValidator;

/**
 * Model for the sources def
 */
public class AeSourcesValidator extends AeBaseValidator
{
   /**
    * ctor
    * @param aDef
    */
   public AeSourcesValidator(AeSourcesDef aDef)
   {
      super(aDef);
   }
}
 