// $Header: /gestionale/org.activebpel.rt.bpel/src/org/activebpel/rt/bpel/impl/activity/support/AeEventHandlers.java,v 1.1 2009/09/23 13:09:34 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//               PROPRIETARY RIGHTS STATEMENT
// The contents of this file represent confidential information that is the
// proprietary property of Active Endpoints, Inc.  Viewing or use of
// this information is prohibited without the express written consent of
// Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
// is strictly forbidden. Copyright (c) 2002-2004 All rights reserved.
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.rt.bpel.impl.activity.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.activebpel.rt.bpel.def.activity.support.AeOnMessageDef;

/**
 * Models the events that may occur within a <code>scope</code> or <code>pick</code>
 */
public class AeEventHandlers
{
   /**
    * list of messages
    */
   private List mMessages;
   
   /**
    * list of alarms
    */
   private List mAlarms;

   /**
    * Adds the alarm to the list.
    * @see org.activebpel.rt.bpel.impl.activity.IAeEventParent#addAlarm(org.activebpel.rt.bpel.impl.activity.support.AeOnAlarm)
    */
   public void addAlarm(AeOnAlarm aAlarm)
   {
      getAlarmsList().add(aAlarm);
   }

   /**
    * Adds the message to the list.
    * @see org.activebpel.rt.bpel.impl.activity.IAeEventParent#addMessage(org.activebpel.rt.bpel.impl.activity.support.AeOnMessage)
    */
   public void addMessage(AeOnMessage aMessage)
   {
      getMessagesList().add(aMessage);
   }

   public void removeMessage(AeOnMessage aMessage)
   {
	   
	   
	   
	   
	   
   }
   public void removeOnMessage(AeOnMessage aMessage)
   {
	   for(int i=0;i<=mMessages.size();i++)
	   {
		   if(mMessages.get(i).equals(aMessage))
		   {
			   mMessages.remove(i);
		   		System.out.println("rimuovo l'onMessage " + i);
		   		break;
		   }
   
	   }
   }
   /**
    * Getter for the alarms, lazy load here. 
    */
   protected List getAlarmsList()
   {
      if (mAlarms == null)
      {
         mAlarms = new ArrayList();
      }
      return mAlarms;
   }

   /**
    * Getter for the messages, lazy load here.
    */
   protected List getMessagesList()
   {
      if (mMessages == null)
      {
         mMessages = new ArrayList();
      }
      return mMessages;
   }
   
   /**
    * Gets the alarms or empty iterator if there are none.
    */
   public Iterator getAlarms()
   {
      if (mAlarms == null)
      {
         return Collections.EMPTY_LIST.iterator();
      }
      return getAlarmsList().iterator();
   }
   
   /**
    * Gets the messages or empty iterator if there are none.
    */
   public Iterator getMessages()
   {
      if (mMessages == null)
      {
         return Collections.EMPTY_LIST.iterator();
      }
      return getMessagesList().iterator();
   }
}
