//$Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/def/validation/process/AePartnersValidator.java,v 1.1 2009/09/23 13:09:52 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//PROPRIETARY RIGHTS STATEMENT
//The contents of this file represent confidential information that is the 
//proprietary property of Active Endpoints, Inc.  Viewing or use of 
//this information is prohibited without the express written consent of 
//Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT 
//is strictly forbidden. Copyright (c) 2002-2006 All rights reserved. 
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.rt.bpel.def.validation.process; 

import org.activebpel.rt.bpel.def.AePartnersDef;
import org.activebpel.rt.bpel.def.validation.AeBaseValidator;

/**
 * validates the partners def
 */
public class AePartnersValidator extends AeBaseValidator
{

   /**
    * ctor
    * @param aDef
    */
   public AePartnersValidator(AePartnersDef aDef)
   {
      super(aDef);
   }

}
 