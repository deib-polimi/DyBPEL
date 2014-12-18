//$Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/def/adapter/IAeValidationPreprocessingAdapter.java,v 1.1 2009/09/23 13:10:02 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//PROPRIETARY RIGHTS STATEMENT
//The contents of this file represent confidential information that is the
//proprietary property of Active Endpoints, Inc.  Viewing or use of
//this information is prohibited without the express written consent of
//Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
//is strictly forbidden. Copyright (c) 2002-2007 All rights reserved.
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.rt.bpel.def.adapter;

import org.activebpel.rt.bpel.IAeExpressionLanguageFactory;
import org.activebpel.rt.wsdl.IAeContextWSDLProvider;
import org.activebpel.rt.xml.def.IAeAdapter;

/**
 * All extension implementations that participate in validation preprocessing need to implement this interface
 */
public interface IAeValidationPreprocessingAdapter extends IAeAdapter
{
   /**
    * Perform validation preprocessing tasks
    * @param aContextProvider
    * @param aExpressionLanguageFactory
    */
   public void preprocessForValidation(IAeContextWSDLProvider aContextProvider, IAeExpressionLanguageFactory aExpressionLanguageFactory);
}
