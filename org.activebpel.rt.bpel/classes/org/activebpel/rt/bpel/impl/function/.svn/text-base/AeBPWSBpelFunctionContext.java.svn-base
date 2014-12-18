//$Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/impl/function/AeBPWSBpelFunctionContext.java,v 1.1 2009/09/23 13:08:28 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//PROPRIETARY RIGHTS STATEMENT
//The contents of this file represent confidential information that is the
//proprietary property of Active Endpoints, Inc.  Viewing or use of
//this information is prohibited without the express written consent of
//Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
//is strictly forbidden. Copyright (c) 2002-2004 All rights reserved.
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.rt.bpel.impl.function;

import org.activebpel.rt.bpel.function.AeUnresolvableException;
import org.activebpel.rt.bpel.function.IAeFunction;

/**
 * A <code>IAeFunctionContext</code> implementation that handles returning
 * standard BPEL 1.1 functions.
 * 
 * Currently supported function are:
 * 
 * <pre>getVariableData</pre> 
 * <pre>getVariableProperty</pre> 
 * <pre>getLinkStatus</pre> 
 */
public class AeBPWSBpelFunctionContext extends AeAbstractBpelFunctionContext
{
   // Constant for getVariableData bpel function. */
   public static final String GET_VARIABLE_DATA     = "getVariableData"; //$NON-NLS-1$
   // Constant for getLinkStatus bpel function. */
   public static final String GET_LINK_STATUS       = "getLinkStatus"; //$NON-NLS-1$

   /**
    * @see org.activebpel.rt.bpel.function.IAeFunctionContext#getFunction(java.lang.String)
    */
   public IAeFunction getFunction(String aLocalName) throws AeUnresolvableException
   {
      if (GET_VARIABLE_DATA.equals(aLocalName))
      {
         return new AeGetVariableDataFunction();
      }
      else if (GET_LINK_STATUS.equals(aLocalName))
      {
         return new AeGetLinkStatusFunction();
      }
      else
      {
         return super.getFunction(aLocalName);
      }
   }
}
