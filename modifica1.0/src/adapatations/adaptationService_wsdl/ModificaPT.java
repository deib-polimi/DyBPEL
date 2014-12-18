/**
 * ModificaPT.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package adapatations.adaptationService_wsdl;

import java.sql.SQLException;

public interface ModificaPT extends java.rmi.Remote {
	/**
	 * 
	 * @param nomeProcesso
	 * @param operazioniArray = aggiungiAttivita,rimuoviAttivita,aggiungiVariabile,rimuoviVariabile
	 *  
	 * @param attributiArray
	 * L'operazione aggiungiAttivita richiede 3 attributi: at_intercettare,at_target,at_nuova
	 * L'operazione rimuoviAttivita richiede 2 attributi: at_intercettare,at_target
	 * L'operazione aggiungiVariabile richiede 3 attribyti: nomeVariabile, nameSpace, messageType
	 * L'operazione rimuoviVariabile richiede 1 attributo: nomeVariabile
	 * @return
	 * @throws java.rmi.RemoteException
	 */
    public java.lang.String modificaOP(java.lang.String nomeProcesso, java.lang.String[] operazioniArray, java.lang.String[] attributiArray) throws java.rmi.RemoteException;
}
