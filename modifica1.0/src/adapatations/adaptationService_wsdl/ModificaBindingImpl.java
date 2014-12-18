/**
 * ModificaBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package adapatations.adaptationService_wsdl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;
import java.util.Calendar;

import bpel.BpelModifier2;
import bpel.Metodi;

public class ModificaBindingImpl implements adapatations.adaptationService_wsdl.ModificaPT{
    //si suppone che il namespace utilizzato dal processo bpel sia sempre questo
	private static String ns = "declare namespace ns = 'http://docs.oasis-open.org/wsbpel/2.0/process/executable';";
	
	public java.lang.String modificaOP(java.lang.String nomeProcesso, java.lang.String[] operazioniArray, java.lang.String[] attributiArray) throws java.rmi.RemoteException {
		
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		System.out.println("Modifica inizio: "+new java.sql.Timestamp(now.getTime()));
		String driver = "com.mysql.jdbc.Driver";
		String risposta =null;
    	int cod_proc = 0; 	
    	String versionNuova = null; 	
    	String tipo_modifica = null;
    	String at_intercettare = null; 	
    	String at_target = null;
    	String at_nuova = null;
    	int count = 0;
    	int k = 0;
    	int dimensioneAttributi = 0;
    	Metodi[] metodi = new Metodi[operazioniArray.length];
    	boolean statica =true;
    	for(int h=0;h<operazioniArray.length;h++)
    	{
    		if(operazioniArray[h].equals("aggiungiAttivita"))
    		{
    			dimensioneAttributi += 2;
    		}else if(operazioniArray[h].equals("rimuoviAttivita"))
    		{
    			dimensioneAttributi += 1;
    		}else if(operazioniArray[h].equals("aggiungiVariabile"))
    		{
    			dimensioneAttributi += 3;
    		}else if(operazioniArray[h].equals("rimuoviVariabile"))
    		{
    			dimensioneAttributi += 1;
    		}
    	}
    	String[] attributi = new String[dimensioneAttributi];
    	System.out.println("attributi.length: "+attributi.length);
    	System.out.println("operazioniArray.length: "+operazioniArray.length);
    	System.out.println("attributiArray.length: " + attributiArray.length);
    	try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String url = "jdbc:mysql://localhost/versionmanager";
    	Connection con = null;
		try {
			con = DriverManager.getConnection (url, "root", "20002000");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		Statement cmd = null;
		try {
			cmd = con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
    	String qry = "SELECT versiontable.cod_proc,versiontable.version " +
    			"FROM processtable,versiontable " +
    			"WHERE processtable.cod_proc=versiontable.cod_proc " +
    				"AND processtable.name='"+nomeProcesso+"'" +
    				"AND versiontable.data >=" +
    					"ALL " +
    						"(SELECT versiontable.data " +
    						"FROM processtable,versiontable " +
    						"WHERE processtable.cod_proc=versiontable.cod_proc " +
    							"AND processtable.name='"+nomeProcesso+"')";
    	ResultSet res = null;
		try {
			res = cmd.executeQuery(qry);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	try {
    		res.next();
			cod_proc=res.getInt("versiontable.cod_proc");
			Integer versionInt = (Integer.parseInt(res.getString("versiontable.version"))+1);
			versionNuova = versionInt.toString();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	for(int i=0;i<operazioniArray.length;i++)
    	{
    		if(operazioniArray[i].equals("aggiungiAttivita"))
    		{
    			//modifiche dinamiche
    			tipo_modifica = "aggiungi";
    			String inserisci = "INSERT INTO `versionmanager`.`adaptationtable` (`cod_proc`,`version`,`tipo_modifica`,`at_intercettare`,`at_target`,`at_nuova`) VALUES (?,?,?,?,?,?)";
    			PreparedStatement SQLPreparedStatement;
    			at_intercettare = attributiArray[count];
    			count++;
    			at_target= attributiArray[count];
    			count++;
    			at_nuova = attributiArray[count];
    			count++;
    			try {
					SQLPreparedStatement = con.prepareStatement(inserisci);
					SQLPreparedStatement.setInt(1,cod_proc);
	    			SQLPreparedStatement.setString(2,versionNuova);
	    			SQLPreparedStatement.setString(3,tipo_modifica);
	    			SQLPreparedStatement.setString(4,at_intercettare);
	    			SQLPreparedStatement.setString(5,at_target);
	    			SQLPreparedStatement.setString(6,at_nuova);
	    			//System.out.println(inserisci);
	    			SQLPreparedStatement.executeUpdate(); 
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//modifiche statiche
    			metodi[i] = Metodi.ADDACTIVITY;
    			attributi[k] = ns +" $this//ns:*[@"+at_target.split("@")[at_target.split("@").length-1];
    			k++;
    			attributi[k] = at_nuova;
    			k++;
    		}else if(operazioniArray[i].equals("rimuoviAttivita"))
    		{
    			
    			//modifiche dinamiche
    			tipo_modifica = "rimuovi";
    			String inserisci = "INSERT INTO `versionmanager`.`adaptationtable` (`cod_proc`,`version`,`tipo_modifica`,`at_intercettare`,`at_target`,`at_nuova`) VALUES (?,?,?,?,?,?)";
    			
    			PreparedStatement SQLPreparedStatement;
    			at_intercettare = attributiArray[count];
    			count++;
    			at_target = attributiArray[count];
    			count++;
				try {
					SQLPreparedStatement = con.prepareStatement(inserisci);
					SQLPreparedStatement.setInt(1,cod_proc);
	    			SQLPreparedStatement.setString(2, versionNuova);
	    			SQLPreparedStatement.setString(3, tipo_modifica);
	    			SQLPreparedStatement.setString(4, at_intercettare);
	    			SQLPreparedStatement.setString(5, at_target);
	    			SQLPreparedStatement.setString(6,"");
	    			//System.out.println(inserisci);
	    			SQLPreparedStatement.executeUpdate(); 
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			//modifiche statiche
    			metodi[i] = Metodi.REMOVEACTIVITY;
    			attributi[k] = ns +" $this//ns:*[@"+at_target.split("@")[at_target.split("@").length-1];
    			k++;
    			
    		}else if(operazioniArray[i].equals("aggiungiVaribiale"))
    		{
    			metodi[i]=Metodi.ADDVARIABLE;
    			attributi[k] = attributiArray[count];
    			k++;
    			attributi[k] = attributiArray[count];
    			k++;
    			attributi[k] = attributiArray[count];
    			k++;
    		}else if(operazioniArray[i].equals("rimuoviVariabile"))
    		{
    			metodi[i]=Metodi.REMOVEVARIABLE;
    			attributi[k] = attributiArray[count];
    			k++;
    		}else
    		{
    			statica =false;
    			risposta="operazione non esistente";
    			break;
    		}
    	}
    	if(statica)	
    	{
    		//modificatore.pippo(nomeProcesso,"",metodi,attributi);
    		BpelModifier2.modifica(nomeProcesso,"",metodi,attributi);
    		risposta="modifica statica effettuata correttamente";
    	}
		Calendar calendar1 = Calendar.getInstance();
		java.util.Date now1 = calendar1.getTime();
		System.out.println("Modifica Fine: "+new java.sql.Timestamp(now1.getTime()));
    	return risposta;
    }

}
