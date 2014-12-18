package test;


import java.io.IOException;
import org.apache.xmlbeans.XmlException;

import bpel.BpelModifier2;
import bpel.Metodi;
 

public class Tester {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws XmlException 
	 */
	public static void main(String[] args) throws IOException, XmlException {
		// TODO Auto-generated method stub
	
	
		String posizione1 = "declare namespace ns='http://docs.oasis-open.org/wsbpel/2.0/process/executable';$this//ns:*[@name='reply1']";
		String posizione2 = "declare namespace ns='http://docs.oasis-open.org/wsbpel/2.0/process/executable';$this//ns:*[@name='reply1']";
		
		String messageType = "richiestaNotizia";
		String nameSpaceVariabile = "http://docs.active-endpoints.com/activebpel/sample/wsdl/bpelServer/2006/09/bpelServer.wsdl";
		String variabileToRemove = "requestNotizia";
		String attivita = "<reply name=\"reply2\" operation=\"request\" partnerLink=\"customer\" portType=\"lns:bpelServerPT\" variable=\"response\">" +
				"<correlations>" +
				"<correlation set=\"tieniTraccia\" initiate=\"no\"/>" +
				"</correlations>" +
				"</reply>";
		
		
		Metodi[] metodi = new Metodi[2];
		metodi[0]=Metodi.ADDACTIVITY;
		metodi[1]=Metodi.REMOVEACTIVITY;
	
		
		String[] attributi= new String[3];
		attributi[0]=posizione1;
		attributi[1]=attivita;
		attributi[2]=posizione2;
		
		
		String nomeProcesso = "bpelServerProcess";
		
		System.out.println(BpelModifier2.modifica(nomeProcesso,"",metodi, attributi));
	}
	

	
	
}