//$Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/def/validation/expr/functions/AeAbstractFunctionValidator.java,v 1.1 2009/09/23 13:09:20 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//PROPRIETARY RIGHTS STATEMENT
//The contents of this file represent confidential information that is the 
//proprietary property of Active Endpoints, Inc.  Viewing or use of 
//this information is prohibited without the express written consent of 
//Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT 
//is strictly forbidden. Copyright (c) 2002-2007 All rights reserved. 
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.rt.bpel.def.validation.expr.functions; 

import java.text.MessageFormat;

import org.activebpel.rt.expr.validation.AeExpressionValidationResult;
import org.activebpel.rt.expr.validation.functions.IAeFunctionValidator;

/**
 * Base class for function validators
 */
public abstract class AeAbstractFunctionValidator implements IAeFunctionValidator
{
   /**
    * Adds an error to the validation result.
    *
    * @param aMessage
    * @param aArgs
    */
   protected void addError(AeExpressionValidationResult aResult, String aMessage, Object [] aArgs)
   {
      String msg = MessageFormat.format(aMessage, aArgs);
      aResult.addError(msg);
   }

   /**
    * Adds a warning to the validation result.
    *
    * @param aMessage
    * @param aArgs
    */
   protected void addWarning(AeExpressionValidationResult aResult, String aMessage, Object [] aArgs)
   {
      String msg = MessageFormat.format(aMessage, aArgs);
      aResult.addWarning(msg);
   }

   /**
    * Adds an info to the validation result.
    *
    * @param aMessage
    * @param aArgs
    */
   protected void addInfo(AeExpressionValidationResult aResult, String aMessage, Object [] aArgs)
   {
      String msg = MessageFormat.format(aMessage, aArgs);
      aResult.addInfo(msg);
   }
}
 
