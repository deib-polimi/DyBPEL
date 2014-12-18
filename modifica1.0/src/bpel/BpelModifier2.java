package bpel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Scanner;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.oasisOpen.docs.wsbpel.x20.process.executable.ProcessDocument;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TProcess;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TVariable;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TVariables;

import support.CreaScript;

import com.activeEndpoints.schemas.pdd.x2006.x08.pdd.MyRoleType;
import com.activeEndpoints.schemas.pdd.x2006.x08.pdd.PartnerLinkType;
import com.activeEndpoints.schemas.pdd.x2006.x08.pdd.ProcessDocument.Process.PartnerLinks;

import com.activeEndpoints.schemas.pdd.x2006.x08.pdd.ProcessDocument.Process;
public abstract class BpelModifier2 {
	
	/*
	 * 1- seleziona la path dove sta la versione di partenza (se la versione di partenza e vuota o null allora prendo quella piu nuova)
	 * 2- fa una copia dell'intera cartella cambiando il nome
	 * 3- modifica il file bpel
	 * 4- fa le modifiche necessarie per poter deployare una nuova versione
	 * 5- deploya la nuova versione:
	 * 		- creo il file bpr
	 * 		- lo copio nella cartella giusta (tomcat/bpr)
	 * 6- inserisce un nuovo record nulla tabella version
	 * 7- restituisce la nuova version
	 */
	/**
	 * Metodi disponibili:
	 * ADDACTIVITY,REMOVEACTIVITY,ADDVARIABLE,REMOVEVARIABLE
	 * 
	 * @param nomeProcesso
	 * @param versionePartenza  se ==null or =="" allora prendere l'ultima versione deployata
	 * @param metodi
	 * @param attributi
	 * @return versioneCreata
	 */
	public static String modifica(String nomeProcesso,String versionePartenza,Metodi[] metodi,String[] attributi)
	{
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		System.out.println("BpelModifier2 inizio: "+new java.sql.Timestamp(now.getTime()));
		Integer versioneNuova=null;
		String driver = "com.mysql.jdbc.Driver";
		try {
			Class.forName(driver);
			       
			//mi connetto al server di mysql al db versionmanager
			String url = "jdbc:mysql://localhost/versionmanager";
			
			Connection con = DriverManager.getConnection (url, "root", "20002000");
			   
			Statement cmd = con.createStatement ();

			//in questa query prende tutti i processi e le sue relative versione gia deployate
			String qry = "SELECT versiontable.cod_proc,versiontable.dest_path,versiontable.filename,versiontable.service,versiontable.source_path,versiontable.dest_path,versiontable.service FROM processtable,versiontable WHERE processtable.cod_proc=versiontable.cod_proc AND processtable.name='"+nomeProcesso+"'";
			if((versionePartenza=="")||(versionePartenza==null))
			{
				qry = qry+ "AND versiontable.data >=ALL (SELECT versiontable.data FROM processtable,versiontable WHERE processtable.cod_proc=versiontable.cod_proc AND processtable.name='"+nomeProcesso+"')";
			}
			else
			{
				qry= qry + "AND versiontable.version='"+versionePartenza+"'";
				
			}
			//System.out.println(qry);
			String sDir =null;
			String vecchioServizio =null;
			String filename= null;
			String dest_path = null;
			int cod_proc=0;
			ResultSet res = cmd.executeQuery(qry);  
		
			while(res.next())
			{
				sDir = res.getString("versiontable.source_path");
				vecchioServizio = res.getString("versiontable.service");
				filename=res.getString("versiontable.filename");
				dest_path=res.getString("versiontable.dest_path");
				cod_proc=res.getInt("versiontable.cod_proc");
				//System.out.println(sDir);
			}
			//mi calcolo la destination path
			qry = "SELECT versiontable.source_path FROM processtable,versiontable WHERE processtable.cod_proc=versiontable.cod_proc AND processtable.name='"+nomeProcesso+"'AND versiontable.data <=ALL (SELECT versiontable.data FROM processtable,versiontable WHERE processtable.cod_proc=versiontable.cod_proc AND processtable.name='"+nomeProcesso+"')";
			res = cmd.executeQuery(qry);
			String dDir = null;
			while(res.next())
			{
					dDir = res.getString("versiontable.source_path");
					//System.out.println(dDir);
			}	
			//mi calcolo a quale versione siamo arrivati
			qry = "SELECT versiontable.version FROM processtable,versiontable WHERE processtable.cod_proc=versiontable.cod_proc AND processtable.name='"+nomeProcesso+"'AND versiontable.data >=ALL (SELECT versiontable.data FROM processtable,versiontable WHERE processtable.cod_proc=versiontable.cod_proc AND processtable.name='"+nomeProcesso+"')";
			res = cmd.executeQuery(qry);
			
			while(res.next())
			{
					versioneNuova = Integer.parseInt(res.getString("versiontable.version"))+1;
					dDir = dDir+versioneNuova.toString();
					//System.out.println(dDir);
			}	
			
			new File(dDir).mkdirs();
			
			try {
				copyDirectory(sDir,dDir);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//ora mi devo costruire la stringa che indica la path del file bpel da modificare
			
			File dir = new File (dDir+"/bpel"); /////////////////////////////////////////////////////////////////////////////////////////modifica fatta qui

		      
		    FileExtFilter fef = new FileExtFilter ("bpel");

		    String[] list = dir.list (fef);
		    String pathFileBpel=null;
		       
		    for (int i = 0; i < list.length; i++)
		    {
		    	pathFileBpel= dDir+"/bpel/"+list[i];
		    }

		   
		    //passo 3
			int contaAttributi=0;
			for(int i=0;i<metodi.length;i++)
			{
				
				switch (metodi[i]) 
				{
			    	case ADDACTIVITY:  addActivity(pathFileBpel,attributi[contaAttributi],attributi[contaAttributi+1]);contaAttributi+=2; break;
			        case REMOVEACTIVITY:  removeActivity(pathFileBpel,attributi[contaAttributi]);contaAttributi++; break;
			        case ADDVARIABLE:  addVariable(pathFileBpel,attributi[contaAttributi],attributi[contaAttributi+1],attributi[contaAttributi+2]);contaAttributi+=3; break;
			        case REMOVEVARIABLE:  removeVariable(pathFileBpel,attributi[contaAttributi]);contaAttributi++; break;
			        default: break;
			    }
	
			}
			
			//passo 4: modifiche necessarie per poter deployare una nuova versione
			
			modificheNecessarie(dDir,versioneNuova.toString(),vecchioServizio);
			
			//passso 5: creo il file bpr
			String nuovoFilename= filename.replaceFirst("[0-9]*.bpr$", versioneNuova.toString()+".bpr");
			String pathScript =CreaScript.creaScript(nuovoFilename, dDir);
			try {
				CreaScript.eseguiScript(pathScript);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
						
			//passo 6: inserisco il nuovo record nella tabella version
			String inserisci = "INSERT INTO `versionmanager`.`versiontable` (`cod_proc`,`version`,`filename`,`source_path`,`dest_path`,`service`,`data`) VALUES (?,?,?,?,?,?,?)";
			PreparedStatement SQLPreparedStatement = con.prepareStatement(inserisci);
			
			SQLPreparedStatement.setInt(1,cod_proc);
			SQLPreparedStatement.setString(2, versioneNuova.toString());
			SQLPreparedStatement.setString(3, nuovoFilename);
			SQLPreparedStatement.setString(4, dDir);
			SQLPreparedStatement.setString(5, dest_path);
			SQLPreparedStatement.setString(6, vecchioServizio.replaceFirst("[0-9]*$", versioneNuova.toString()));
			java.util.Date javaDate = new java.util.Date();
		    long javaTime = javaDate.getTime();
			java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(javaTime);
			SQLPreparedStatement.setTimestamp(7, sqlTimestamp);
			//System.out.println(inserisci);
			SQLPreparedStatement.executeUpdate(); 
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar calendar1 = Calendar.getInstance();
		java.util.Date now1 = calendar1.getTime();
		System.out.println("BpelModifier2 fine: "+new java.sql.Timestamp(now1.getTime()));
		return versioneNuova.toString();
	}
	/*
	 * 0- vedere che versione siamo arrivati e calcolarsi la nuova versione
	 * 1- modificare il file pdd e il file bpel per farlo funzionare (uso del metodo privato modificheNecessarie)
	 * 2- creare e copiare il file bpr
	 * 3- inserire il nuovo record nella tabella versiontable
	 * 4- restituisce la nuova versione
	 */
	/**
	 * @param nomeProcesso
	 * @param path contiene la path della cartella dove risiede il necessario per deployare il file bpel
	 * @return il numero della versione deployata
	 */
	/*public static String nuovo(String nomeProcesso,String path)
	{
		
		Integer versioneNuova = 0;
		String driver = "com.mysql.jdbc.Driver";
		
		try {
			Class.forName(driver);
			
			       
			//mi connetto al server di mysql al db versionmanager
			String url = "jdbc:mysql://localhost/versionmanager";
			
			Connection con = DriverManager.getConnection (url, "root", "20002000");
			   
			Statement cmd = con.createStatement ();

			//in questa query prende tutti i processi e le sue relative versione gia deployate
			String qry = "SELECT versiontable.dest_path,versiontable.version,versiontable.service,versiontable.filename,versiontable.cod_proc FROM processtable,versiontable" +
					" WHERE processtable.cod_proc=versiontable.cod_proc AND processtable.name='"+nomeProcesso+"' " +
					"AND versiontable.data >=ALL (SELECT versiontable.data " +
						"FROM processtable,versiontable " +
						"WHERE processtable.cod_proc=versiontable.cod_proc AND processtable.name='"+nomeProcesso+"')";
			
			ResultSet res = cmd.executeQuery(qry);  
			String vecchioServizio = null;
			String filename = null;
			int cod_proc =0;
			String dest_path = null;
			while(res.next())
			{
				//passo 0
				versioneNuova = Integer.parseInt(res.getString("versiontable.version"))+1;
				vecchioServizio = res.getString("versiontable.service");
				filename =res.getString("versiontable.filename");
				cod_proc =res.getInt("versiontable.cod_proc");
				dest_path =res.getString("versiontable.dest_path");
			}
			
			//passo 1
			modificheNecessarie(path,versioneNuova.toString(),vecchioServizio);
			
			//passo 2
			String nuovoFilename= filename.replaceFirst("[0-9]*.bpr$", versioneNuova.toString()+".bpr");
			String pathScript = CreaScript.creaScript(nuovoFilename, path);
			try {
				CreaScript.eseguiScript(pathScript);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//passo 3
			String inserisci = "INSERT INTO `versionmanager`.`versiontable` (`cod_proc`,`version`,`filename`,`source_path`,`dest_path`,`service`,`data`) VALUES (?,?,?,?,?,?,?)";
			PreparedStatement SQLPreparedStatement = con.prepareStatement(inserisci);
			
			SQLPreparedStatement.setInt(1,cod_proc);
			SQLPreparedStatement.setString(2, versioneNuova.toString());
			SQLPreparedStatement.setString(3, nuovoFilename);
			SQLPreparedStatement.setString(4, path);
			SQLPreparedStatement.setString(5, dest_path);
			SQLPreparedStatement.setString(6, vecchioServizio.replaceFirst("[0-9]*$", versioneNuova.toString()));
			java.util.Date javaDate = new java.util.Date();
		    long javaTime = javaDate.getTime();
			java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(javaTime);
			SQLPreparedStatement.setTimestamp(7, sqlTimestamp);
			//System.out.println(inserisci);
			SQLPreparedStatement.executeUpdate(); 
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//passo 4
		return versioneNuova.toString();
		
	}
	*/
	private static void modificheNecessarie(String dDir,String versione,String vecchioServizio)
	{
		File dir = new File (dDir+"/bpel");
		FileExtFilter fef = new FileExtFilter ("bpel");

	    String[] list = dir.list (fef);
	    String pathFileBpel=null;
	    for (int i = 0; i < list.length; i++)
	    {
	    	pathFileBpel= dDir+"/bpel/"+list[i];
	    }
		
	    File xmlFile = new File(pathFileBpel); 
		
		ProcessDocument bpel = null;
	
		try {
			bpel = ProcessDocument.Factory.parse(xmlFile);
		} catch (XmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TProcess processo = bpel.getProcess();
		String nomeProcesso = processo.getName();
		processo.setName(nomeProcesso.replaceFirst("[0-9]*$", versione));
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrint();
		
		try {
			bpel.save(xmlFile,xmlOptions);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dir = new File (dDir);
		fef = new FileExtFilter ("pdd");

	    list = dir.list (fef);
	    String pathFilePdd=null;
	    for (int i = 0; i < list.length; i++)
	    {
	    	pathFilePdd= dDir+"/"+list[i];
	    }
		
	    xmlFile = new File(pathFilePdd); 
	    com.activeEndpoints.schemas.pdd.x2006.x08.pdd.ProcessDocument pdd = null;
		
		try {
			pdd = com.activeEndpoints.schemas.pdd.x2006.x08.pdd.ProcessDocument.Factory.parse(xmlFile);
		} catch (XmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Process processoPdd = pdd.getProcess();
		processoPdd.setName(new QName((processoPdd.getName()).getNamespaceURI(), nomeProcesso.replaceFirst("[0-9]*$", versione)));
		PartnerLinks partnerLinks = processoPdd.getPartnerLinks();
		for(int i=0;i<partnerLinks.sizeOfPartnerLinkArray();i++)
		{
			PartnerLinkType partnerLink = partnerLinks.getPartnerLinkArray(i);
			MyRoleType myRole = partnerLink.getMyRole();
			//System.out.println(partnerLink.getName());
			
			if((myRole!=null)&&(myRole.getService()).equals(vecchioServizio))
			{
				myRole.setService(vecchioServizio.replaceFirst("[0-9]*$", versione));
			}
		}
		
		try {
			pdd.save(xmlFile,xmlOptions);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	    
	}
	 private static void copyDirectory(String sDir, String dDir) throws IOException{
	  	    File srcDir = new File(sDir);
	        File dstDir = new File(dDir);
	        if (srcDir.isDirectory()) {
	            if (!dstDir.exists()) {
	                dstDir.mkdir();
	            }
	            String[] children = srcDir.list();
	            for (int i=0; i<children.length; i++) {
	                //System.out.println(children[i]);
	                copyDirectory(sDir+"/"+children[i], dDir+"/"+children[i]);
	            }
	        } else {
	            copyFile(srcDir, dstDir);
	        }
	    }
	 private static void copyFile(File src, File dst) throws IOException {
		 
		   while(!src.exists());
		   InputStream in = new FileInputStream(src);
		   OutputStream out = new FileOutputStream(dst);
		    
		   byte[] buf = new byte[1024];
		   int len;
		   while ((len = in.read(buf)) > 0) {
		      out.write(buf, 0, len);
		   }
		   in.close();
		   out.close();
		   
		}
	 
	private static void addActivity(String pathFileBpel,String xpath,String attivita)
	{
		File xmlFile = new File(pathFileBpel); 
		
		ProcessDocument bpel = null;
		
		try {
			bpel = ProcessDocument.Factory.parse(xmlFile);
		} catch (XmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TProcess processo = bpel.getProcess();
		XmlCursor cursor = processo.newCursor();
		cursor.selectPath(xpath);
		cursor.toNextSelection();
		cursor.insertChars(attivita);
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrint();
		
		try {
			bpel.save(xmlFile,xmlOptions);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Scanner sc = null;
		try {
			sc = new Scanner(new File(pathFileBpel));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sc.useDelimiter("\n");
		String s="";
		while(sc.hasNext())
		{
			String appoggio = sc.next();
			appoggio = appoggio.replaceAll("&lt;", "<");
			appoggio = appoggio.replaceAll("[^a-z][^a-z][^a-z]CDATA[^a-z]", "");
			appoggio = appoggio.replaceAll("]]>", "");
			
			s=s+appoggio;
		}
		sc.close();
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(
				new BufferedWriter(
					new FileWriter(pathFileBpel,
						false)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		writer.print(s);
		
		writer.close();
	}
	private static void removeActivity(String pathFileBpel,String xpath)
	{
		File xmlFile = new File(pathFileBpel); 
		
		ProcessDocument bpel = null;
		try {
			bpel = ProcessDocument.Factory.parse(xmlFile);
		} catch (XmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TProcess processo = bpel.getProcess();

		XmlCursor cursor = processo.newCursor();
		cursor.selectPath(xpath);
		
		cursor.toNextSelection();
		cursor.removeXml();
		
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrint();
		
		try {
			bpel.save(xmlFile,xmlOptions);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void addVariable(String pathFileBpel,String nomeVariabile,String nameSpace,String messageType)
	{
		File xmlFile = new File(pathFileBpel); 
		ProcessDocument bpel = null;
		
		try {
			bpel = ProcessDocument.Factory.parse(xmlFile);
		} catch (XmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TProcess processo = bpel.getProcess();
		TVariables variables =processo.getVariables();
		TVariable variabile =variables.addNewVariable();
		variabile.setMessageType(new QName(nameSpace,messageType));	
		variabile.setName(nomeVariabile);
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrint();
		
		try {
			bpel.save(xmlFile,xmlOptions);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void removeVariable(String pathFileBpel,String nomeVariabile)
	{
		File xmlFile = new File(pathFileBpel); 
		ProcessDocument bpel = null;
		
		try {
			bpel = ProcessDocument.Factory.parse(xmlFile);
		} catch (XmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TProcess processo = bpel.getProcess();
		TVariables variables =processo.getVariables();
		int i=0;
		boolean trovato = false;
		while((i<variables.sizeOfVariableArray())&&(!(trovato)))
		{
			if(variables.getVariableArray(i).getName().equals(nomeVariabile))
			{
				variables.removeVariable(i);
				trovato=true;
			}
			i++;
		}
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrint();
		
		try {
			bpel.save(xmlFile,xmlOptions);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
}
