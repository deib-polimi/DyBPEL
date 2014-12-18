// $Header: /gestionale/org.activebpel.rt.bpel.server/src/org/activebpel/rt/bpel/server/engine/storage/AePersistentAttachmentManager.java,v 1.1 2009/09/23 13:57:04 zampognaro Exp $
/////////////////////////////////////////////////////////////////////////////
//               PROPRIETARY RIGHTS STATEMENT
// The contents of this file represent confidential information that is the
// proprietary property of Active Endpoints, Inc.  Viewing or use of
// this information is prohibited without the express written consent of
// Active Endpoints, Inc. Removal of this PROPRIETARY RIGHTS STATEMENT
// is strictly forbidden. Copyright (c) 2002-2007 All rights reserved.
/////////////////////////////////////////////////////////////////////////////
package org.activebpel.rt.bpel.server.engine.storage;

import java.util.Map;

import org.activebpel.rt.bpel.impl.AeFileAttachmentManager;
import org.activebpel.rt.bpel.impl.attachment.IAeAttachmentStorage;
import org.activebpel.rt.bpel.server.AeMessages;
import org.activebpel.rt.bpel.server.engine.storage.attachment.AeCompositeAttachmentStorage;

/**
 * Implements a persistent attachment manager.
 */
public class AePersistentAttachmentManager extends AeFileAttachmentManager
{
   /** The composite storage object. */
   private IAeAttachmentStorage mCompositeStorage;

   /** The default persistent storage object. */
   private IAeAttachmentStorage mPersistentStorage;

   /**
    * Constructs the attachment manager with the given engine configuration.
    * @param aConfig The engine configuration for this manager.
    */
   public AePersistentAttachmentManager(Map aConfig)
   {
      super(aConfig);
   }

   /**
    * Returns the persistent (database) storage implementation.
    */
   public IAeAttachmentStorage getPersistentStorage() throws AeStorageException
   {
      if (mPersistentStorage == null)
      {
         mPersistentStorage = AePersistentStoreFactory.getInstance().getAttachmentStorage();

         if (mPersistentStorage == null)
         {
            throw new AeStorageException(AeMessages.getString("AePersistentAttachmentManager.ERROR_GettingStorage")); //$NON-NLS-1$ 
         }
      }

      return mPersistentStorage;
   }

   /**
    * Overrides method to return the composite storage implementation.
    * 
    * @see org.activebpel.rt.bpel.impl.AeAbstractAttachmentManager#getStorage()
    */
   public IAeAttachmentStorage getStorage() throws AeStorageException
   {
      if (mCompositeStorage == null)
      {
         mCompositeStorage = new AeCompositeAttachmentStorage(getPersistentStorage(), getFileStorage());
      }
      
      return mCompositeStorage;
   }
}
