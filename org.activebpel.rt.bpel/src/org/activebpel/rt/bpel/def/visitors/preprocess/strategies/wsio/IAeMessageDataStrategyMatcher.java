//$Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/def/visitors/preprocess/strategies/wsio/IAeMessageDataStrategyMatcher.java,v 1.1 2009/09/23 13:10:05 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//PROPRIETARY RIGHTS STATEMENT
//The contents of this file represent confidential information that is the 
//proprietary property of Active Endpoints, Inc.  Viewing or use of 
//this information is prohibited without the express written consent of 
//Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT 
//is strictly forbidden. Copyright (c) 2002-2006 All rights reserved. 
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.rt.bpel.def.visitors.preprocess.strategies.wsio; 

import org.activebpel.rt.bpel.def.activity.IAeMessageDataConsumerDef;
import org.activebpel.rt.bpel.def.activity.IAeMessageDataProducerDef;

/**
 * Interface for matching a def to the strategy used to produce message data
 */
public interface IAeMessageDataStrategyMatcher
{
   /**
    * Matches the attributes and child elements for the def to one of the 
    * prescribed message producer strategies. If there is no match found
    * then this is not a legal construct. 
    * @param aDef
    * @return strategy name or null if not a valid def
    */
   public String getProducerStrategy(IAeMessageDataProducerDef aDef);

   /**
    * Matches the attributes and child elements for the def to one of the 
    * prescribed message producer strategies. If there is no match found
    * then this is not a legal construct. 
    * @param aDef
    * @return strategy name or null if not a valid def
    */
   public String getConsumerStrategy(IAeMessageDataConsumerDef aDef);
}
 