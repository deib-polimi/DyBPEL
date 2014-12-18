//$Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/impl/activity/assign/to/AeToPropertyType.java,v 1.1 2009/09/23 13:08:50 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//PROPRIETARY RIGHTS STATEMENT
//The contents of this file represent confidential information that is the 
//proprietary property of Active Endpoints, Inc.  Viewing or use of 
//this information is prohibited without the express written consent of 
//Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT 
//is strictly forbidden. Copyright (c) 2002-2006 All rights reserved. 
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.rt.bpel.impl.activity.assign.to; 

import javax.xml.namespace.QName;

import org.activebpel.rt.bpel.AeBusinessProcessException;
import org.activebpel.rt.bpel.def.activity.support.AeToDef;
import org.activebpel.rt.bpel.impl.activity.assign.IAeVariableDataWrapper;
import org.activebpel.rt.wsdl.def.IAePropertyAlias;

/**
 * Gets the target for the copy using a propertyAlias 
 */
public class AeToPropertyType extends AeToPropertyBase
{
   /**
    * Ctor accepts def and context
    *  
    * @param aToDef
    */
   public AeToPropertyType(AeToDef aToDef)
   {
      super(aToDef);
   }
   
   /**
    * Ctor accepts variable and property
    * @param aVariable
    * @param aProperty
    */
   public AeToPropertyType(String aVariable, QName aProperty)
   {
      super(aVariable, aProperty);
   }

   /**
    * @see org.activebpel.rt.bpel.impl.activity.assign.IAePropertyAliasCopyOperation#getPropertyAlias()
    */
   public IAePropertyAlias getPropertyAlias() throws AeBusinessProcessException
   {
      return getCopyOperation().getContext().getPropertyAlias(IAePropertyAlias.TYPE, getVariable().getType(), getProperty());
   }

   /**
    * @see org.activebpel.rt.bpel.impl.activity.assign.IAePropertyAliasCopyOperation#getDataForQueryContext(org.activebpel.rt.wsdl.def.IAePropertyAlias)
    */
   public Object getDataForQueryContext(IAePropertyAlias aPropAlias) throws AeBusinessProcessException
   {
      return getVariable().getTypeData();
   }

   /**
    * @see org.activebpel.rt.bpel.impl.activity.assign.to.AeToPropertyBase#getVariableDataWrapper()
    */
   protected IAeVariableDataWrapper getVariableDataWrapper() throws AeBusinessProcessException
   {
      return new AeVariableComplexTypeDataWrapper(getVariable());
   }
}
 