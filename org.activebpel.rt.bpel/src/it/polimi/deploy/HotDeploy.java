package it.polimi.deploy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


import it.polimi.data.DataWorkflow;

import it.polimi.database.BPIDatabase;

/**
 * This class permits to do a hot deployment of a bpi files.
 * 
 * @author Paoli Matteo
 *
 */
public class HotDeploy extends Thread {

	/**
	 * Indica la classe per la gestione del database
	 */
	private BPIDatabase bpidb;
	
	/**
	 * Indica se bisogna terminare il thread
	 */
	private boolean killProcess;
	
	/**
	 * Tempo di sleep del processo
	 */
	private final int SLEEPTIME = 2000;
	
	/**
	 * HashMap che rappresenta la relazione tra file e tempo di creazione
	 */
	private HashMap <String, Long> map;

	/**
	 * Rappresenta il percorso dove trovare la cartella bpi 
	 */
	private String homeBpi;
	
	/**
	 * Costruttore della classe e considera la variabile d'ambiene CATALINA_HOME per 
	 * trovare la cartella bpi
	 * @param dbName nome del database
	 * @param dbUsername nome dell'utente
	 * @param dbPassword password dell'utente
	 * @throws Exception eccezioni del database
	 */
	public HotDeploy(BPIDatabase bpidb) throws Exception
	{
		/* istanziamo la hash map */
		map = new HashMap<String, Long> ();
		
		/* settiamo il percorso della classe come la directory di tomcat */
		
		homeBpi = System.getenv("CATALINA_HOME");
		if (homeBpi.endsWith(System.getProperties().getProperty("file.separator"))) {
			homeBpi = homeBpi + "bpi";
		}
		else {
			homeBpi = homeBpi + System.getProperties().getProperty("file.separator") + "bpi";
		}
		
		//homeBpi = homeBpi.replaceAll(System.getProperties().getProperty("file.separator")+System.getProperties().getProperty("file.separator"), System.getProperties().getProperty("file.separator"));
		
		this.killProcess = false;
		
		/* istanziamo la classe per la gestione del db */
		this.bpidb = bpidb;
		
	}
	
	/**
	 * Costruttore della classe con la possibilità di impostare la directory bpi
	 * @param bpiDirectory percorso della cartella bpi
	 * @param dbName nome del database
	 * @param dbUsername nome dell'utente
	 * @param dbPassword password dell'utente
	 * @throws Exception eccezione per problemi nel db
	 */
	public HotDeploy (String bpiDirectory, BPIDatabase bpidb) throws Exception
	{
		/* istanziamo la hash map */
		map = new HashMap<String, Long> ();
		
		/* settiamo il percorso della classe come la directory di tomcat */
		homeBpi = bpiDirectory;
		
		this.killProcess = false;
		
		/* istanziamo la classe per la gestione del db */
		this.bpidb = bpidb;
	}
	
	/**
	 * Metodo che permette di leggere il file XML ed estrarre i dati
	 * @param bpiFile percorso assoluto del file bpi da analizzare
	 */
	private void readXML (String bpiFile)
	{
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;

		try {
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();

			Document xml = builder.parse(bpiFile);

			/* carichiamo i nodi di tipo worflow */
			this.findWorkflowNode(xml, bpiFile);

		} catch( IOException ex ) {
			System.err.println("[HOTDEPLOY] IOError reading " + bpiFile);
		} catch( SAXException ex ) {
			System.err.println("[HOTDEPLOY] SAX Error");
		} catch( FactoryConfigurationError ex ) {
			System.err.println("[HOTDEPLOY] FactoryConfiguration error");
		} catch( ParserConfigurationException ex ) {
			System.err.println("[HOTDEPLOY] Parser Configuration error");
		}
	}
	
	
	
	
	/**
	 * Metodo che permette di caricare tutti i nodi di tipo Workflow
	 * @param xml documento xml
	 */
	private void findWorkflowNode (Document xml, String bpiFile)
	{
		/* prendiamo la lsita dei nodi di tipo workflow */
		NodeList workflowNodes = xml.getElementsByTagName("workflow");
		
		/* nodo di tipi service */
		DataWorkflow nodeWork;
		
		/* scorriamo tutti i nodi di tipo workflow */
		for (int i=0; i < workflowNodes.getLength() ; i++)
		{
			nodeWork = new DataWorkflow (bpiFile.replace(this.homeBpi + System.getProperties().getProperty("file.separator"), ""));
			
			/* controlliamo se ha figli */
			if (workflowNodes.item(i).hasChildNodes())
			{
				/* carichiamo i nodi figli */
				NodeList figli = workflowNodes.item(i).getChildNodes();
				
				/* scorriamo i nodi figli */
				for (int j = 0; j < figli.getLength(); j ++)
				{
					/* controlliamo che il nodo prelevato sia di tipo nodo */
					if (figli.item(j).getNodeType() == Node.ELEMENT_NODE)
					{
						/* preleviamo la mappa dei valori degli attributi del nodo */
						NamedNodeMap attributes = figli.item(j).getAttributes();

						/* if che servono a capire quale nodo figlio è */
						if (figli.item(j).getNodeName().trim().equals("targetEPR"))
						{
							try {
								nodeWork.addTargetEPR(attributes.getNamedItem("value").getNodeValue());
						
							} catch (Exception e) {
								System.err.println("[HOTDEPLOY] " + e.getMessage());
							}
						}
						else if (figli.item(j).getNodeName().trim().equals("processID")) {
							nodeWork.setProcessID(attributes.getNamedItem("value").getNodeValue());
						 
						}
						else if (figli.item(j).getNodeName().trim().equals("operation")) {
							nodeWork.setOperation(attributes.getNamedItem("value").getNodeValue());
						
						}
						else if (figli.item(j).getNodeName().trim().equals("status")) {
							nodeWork.setStatus(attributes.getNamedItem("value").getNodeValue());
						
						}
						else if (figli.item(j).getNodeName().trim().equals("correlationKey")) {
							nodeWork.setCorrelationKey(attributes.getNamedItem("value").getNodeValue());
						
						}
						else if (figli.item(j).getNodeName().trim().equals("correlationValue")) {
							nodeWork.setCorrelationValue(attributes.getNamedItem("value").getNodeValue());
						
						}
					
					}
				}
			}
			
			/* salviamo sul db il nodo */
			try {
				
				this.bpidb.insertWorkflowNode(nodeWork);
				
				System.out.println("[HOTDEPLOY] Insert workflow node in the DB.");
				
			} catch (Exception e) {
				System.err.println("[HOTDEPLOY] " + e.getMessage());
				
			}
		}
	}
	
		
	/**
	 * Metodo che permette di validare un file bpi passato
	 * @param bpiFile percorso assoluto del file bpi da analizzare
	 * @throws SAXException eccezione SAX
	 * @throws IOException eccezione IO
	 * @throws ParserConfigurationException eccezione del Parser
	 */
	private void validateBPI (String bpiFile) throws SAXException, IOException, ParserConfigurationException 
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setErrorHandler(new org.xml.sax.ErrorHandler() {
			//Ignore the fatal errors
			public void fatalError(SAXParseException arg0)throws SAXException { 
				throw new SAXException (arg0.getMessage());
			}


			public void error(SAXParseException arg0) throws SAXException {
				throw new SAXException (arg0.getMessage());
				
			}


			public void warning(SAXParseException arg0) throws SAXException {
				throw new SAXException (arg0.getMessage());
				
			}
			
		});
		builder.parse(new FileInputStream(bpiFile));
	}
	
	/**
	 * Metodo che permette di fermare il thread 
	 */
	@SuppressWarnings("unused")
	public void killHotDeploy ()
	{
		this.killProcess = true;
	}
	
	
	/**
	 * Metodo che permette di fare l'hot deployment dei file *.bpi
	 * @throws Exception quando non si trova la cartella bpi.
	 */
	private void hotDeployment () throws Exception 
	{		
		File f = new File(this.homeBpi);
		
		/* controlliamo che esista la directory */
		if (!f.exists()) 
			throw new Exception ("[HOTDEPLOY] No bpi directory find in " + this.homeBpi);
			
		/* lista di file nella directory */
		File[] list=f.listFiles();
		
		/* array che contiene solo i file di tipo bpi */
		ArrayList <File> fileList = new ArrayList<File>();
		
		/* cicliamo ed estraimo solo i file con estensione bpi */
		for (int i = 0; i < list.length; i++)
		{
			if (list[i].isFile() && list[i].getName().contains(".bpi"))
			{
				fileList.add(list[i]);
			}
		}
	
		/* controlliamo se sono aggiunti elementi */
		if ( fileList.size() >= map.size())
		{
			/* cicliamo su tutti gli elementi trovati */
			for (int i = 0; i < fileList.size();  i++)
			{
				/* analizziamo solo i file presenti nella directory */
				if (list[i].isFile())
				{
					/* controlliamo se il file è già presente nel sistema */
					if (map.get(fileList.get(i).getName()) != null)
					{
						/* controlliamo se il file è stato modificato */
						if (map.get(fileList.get(i).getName()) != fileList.get(i).lastModified())
						{
							System.out.println("[HOTDEPLOY] Modified " + fileList.get(i).getName() + " file");
							
							/* eliminiamo i vecchi dati */
							this.bpidb.removeFile(fileList.get(i).getName());
							
							try {
								/* validiamo il file */
								this.validateBPI(fileList.get(i).getAbsolutePath());
								
								/* aggiungiamo nella mappa il nuovo file */
								map.put(fileList.get(i).getName(), fileList.get(i).lastModified());
								
								
								
								System.out.println("[HOTDEPLOY] " + fileList.get(i).getName() + " is valid." );

								/* validiamo il file */
								System.out.println("[HOTDEPLOY] Extract data from " + fileList.get(i).getName());
								this.readXML(fileList.get(i).getAbsolutePath());
																
							} catch (SAXException e) {
								System.err.println("[HOTDEPLOY] " + e.getMessage());
							} catch (IOException e) {
								System.err.println("[HOTDEPLOY] " + e.getMessage());
							}
						}
					}
					else{
						
						/* il file non è presente nella mappa e quindi lo inseriamo */
						System.out.println("[HOTDEPLOY] Add " + fileList.get(i).getName() + " file in bpi directory");
						
						/* aggiungiamo davvero il file */
						map.put(fileList.get(i).getName(), fileList.get(i).lastModified());
						
						try {
							/* validiamo il file bpi */
							this.validateBPI(fileList.get(i).getAbsolutePath());
							
							System.out.println("[HOTDEPLOY] File " + fileList.get(i).getName() + " is valid." );
							
							/* validiamo il file */
							System.out.println("[HOTDEPLOY] Extract data from " + fileList.get(i).getName());
							this.readXML(fileList.get(i).getAbsolutePath());
							
						} catch (FileNotFoundException e) {
							System.err.println("[HOTDEPLOY] " + e.getMessage());
						} catch (SAXException e) {
							System.err.println("[HOTDEPLOY] " + e.getMessage());
						} catch (IOException e) {
							System.err.println("[HOTDEPLOY] " + e.getMessage());
						} 
						
					}
				}
			}
		}
		/* sono stati eliminati dei file */
		else 
		{		
			/* preleviamo la lista delle chiavi */			
			Collection <String> x = map.keySet();

			/* costruiamo l'iteratore sul set delle chiavi */
			java.util.Iterator<String> it = x.iterator();

			/* booleano che indica se il file è presente */
			boolean findElement;
			
			/* andiamo a cercare quali sono i file che sono stati cancellati */
			while (it.hasNext())
			{
				String keyElement = it.next();
				findElement = false;
				
				/* scorriamo la lista dei file presenti */
				for (int i = 0; i < fileList.size(); i++)
				{				
					/* vediamo se l'elemento cercato è presente */
					if (fileList.get(i).getName().equals(keyElement))
					{
						findElement = true;
						
						break;
					}
				}
				
				/* controlliamo se non abbiamo trovato l'elemento */
				if (!findElement)
				{
					/* rimuoviamo dalla mappa l'elemento */
					it.remove();
					
					/* rimuoviamo tutti i nodi che dipendono dal file */
					this.bpidb.removeFile(keyElement);
					
					System.out.println("[HOTDEPLOY] Delete " + keyElement + " file in bpi directory");
				}
			}
		}
	}
	
	@SuppressWarnings("static-access")
	public void run ()
	{
		/* stampiamo in console che l'hot deployment è partito */
		System.out.println("[HOTDEPLOY] Start Hot Deployment for BPI files (" + this.homeBpi + ")");
		
		try {
			while (!this.killProcess)
			{
				/* chiamiamo il metodo per l'hot deployment */
				this.hotDeployment();
				try {
					/* mettiamo il thread in pausa per il tempo fissato */
					this.sleep(SLEEPTIME);
				} catch (InterruptedException e) {
					System.err.println("[HOTDEPLOY] " + e.getMessage());
				}
			}
		} catch (Exception e1) {
			System.err.println("[HOTDEPLOY] " + e1.getMessage());
		}	
	}
}