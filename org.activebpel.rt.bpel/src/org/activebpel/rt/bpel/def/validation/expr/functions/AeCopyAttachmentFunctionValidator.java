//$Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/def/validation/expr/functions/AeCopyAttachmentFunctionValidator.java,v 1.1 2009/09/23 13:09:20 zampognaro Exp $
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

/**
 * Validates the ActiveBPEL extension function copyAttachment(fromVariableName, fromItemNumber, toVariableName) 
 */
public class AeCopyAttachmentFunctionValidator extends AeAbstractActiveBpelExtensionFunctionValidator
{
   /**
    * @see org.activebpel.rt.expr.validation.functions.IAeFunctionValidator#validate(org.activebpel.rt.expr.def.AeScriptFuncDef, org.activebpel.rt.expr.validation.AeExpressionValidationResult, org.activebpel.rt.expr.validation.IAeExpressionValidationContext)
    */
   public void validate(AeScriptFuncDef aScriptFunction,
         AeExpressionValidationResult aResult,
         IAeExpressionValidationContext aContext)
   {
      super.validate(aScriptFunction, aResult, aContext);
      
      int numArgs = aScriptFunction.getArgs().size();
      if (numArgs != 3)
      {
         addError(aResult,
               AeMessages.getString("AeAbstractActiveBpelExtensionFunctionValidator.ERROR_INCORRECT_ARGS_NUMBER"),  //$NON-NLS-1$
               new Object [] {aScriptFunction.getName(), new Integer(3),  new Integer(numArgs), aResult.getParseResult().getExpression() });
      }
      else 
      {
         if ( !(aScriptFunction.getArgument(0) instanceof String) )
            addError(aResult,
               AeMessages.getString("AeAbstractActiveBpelExtensionFunctionValidator.ERROR_INCORRECT_ARG_TYPE"), //$NON-NLS-1$
               new Object [] { aScriptFunction.getName(), "1", "String", aResult.getParseResult().getExpression() });//$NON-NLS-1$//$NON-NLS-2$
                           
         if ( !(aScriptFunction.getArgument(2) instanceof String) )
            addError(aResult,
                  AeMessages.getString("AeAbstractActiveBpelExtensionFunctionValidator.ERROR_INCORRECT_ARG_TYPE"), //$NON-NLS-1$
                  new Object [] { aScriptFunction.getName(), "3", "String", aResult.getParseResult().getExpression() });//$NON-NLS-1$//$NON-NLS-2$
      }
   }
   
   
}
 
