//$Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/def/validation/expr/functions/AeBPWSFunctionUsedInWSBPELValidator.java,v 1.1 2009/09/23 13:09:20 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//PROPRIETARY RIGHTS STATEMENT
//The contents of this file represent confidential information that is the 
//proprietary property of Active Endpoints, Inc.  Viewing or use of 
//this information is prohibited without the express written consent of 
//Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT 
//is strictly forbidden. Copyright (c) 2002-2007 All rights reserved. 
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.rt.bpel.def.validation.expr.functions; 

import org.activebpel.rt.bpel.AeMessages;
import org.activebpel.rt.expr.def.AeScriptFuncDef;
import org.activebpel.rt.expr.validation.AeExpressionValidationResult;
import org.activebpel.rt.expr.validation.IAeExpressionValidationContext;
import org.activebpel.rt.expr.validation.functions.IAeFunctionValidator;

/**
 * Pass through validator that also reports an info message that the function
 * being used isn't appropriate for this version of BPEL.
 */
public class AeBPWSFunctionUsedInWSBPELValidator extends AeDelegatingFunctionValidator
{
   /**
    * C'tor
    * @param aValidator
    */
   public AeBPWSFunctionUsedInWSBPELValidator(IAeFunctionValidator aValidator)
   {
      super(aValidator);
   }
   
   /**
    * @see org.activebpel.rt.bpel.def.validation.expr.functions.AeDelegatingFunctionValidator#validate(org.activebpel.rt.expr.def.AeScriptFuncDef, org.activebpel.rt.expr.validation.AeExpressionValidationResult, org.activebpel.rt.expr.validation.IAeExpressionValidationContext)
    */
   public void validate(AeScriptFuncDef aScriptFunction, AeExpressionValidationResult aResult, IAeExpressionValidationContext aContext)
   {
      super.validate(aScriptFunction, aResult, aContext);
      addInfo(aResult, AeMessages.getString("AeBPWSFunctionUsedInWSBPELValidator.BPEL11_FUNCTION_USED_WARNING"), new Object[] { aScriptFunction.getName() }); //$NON-NLS-1$
   }

}
 
