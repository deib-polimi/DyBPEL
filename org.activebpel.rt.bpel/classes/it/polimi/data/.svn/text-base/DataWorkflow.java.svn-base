package it.polimi.data;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Classe che rappresenta un nodo di tipo workflow
 * 
 * @author Paoli Matteo
 *
 */
public class DataWorkflow {
	
	private String correlationKey;
	
	private String correlationValue;
	
	private String dataExtraction;
	
	private String uniqueID;
	
	
	
	public String getUniqueID() {
		return uniqueID;
	}


	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}


	public String getDataExtraction() {
		return dataExtraction;
	}


	public void setDataExtraction(String dataExtraction) {
		this.dataExtraction = dataExtraction;
	}


	public String getCorrelationKey() {
		return correlationKey;
	}


	public void setCorrelationKey(String correlationKey) {
		this.correlationKey = correlationKey;
	}


	public String getCorrelationValue() {
		return correlationValue;
	}


	public void setCorrelationValue(String correlationValue) {
		this.correlationValue = correlationValue;
	}


	/**
	 * Stringa che rappresenta il nome del file
	 */
	private String fileName;
	
	
	/** 
	 * Lista di targetERP 
	 */
	private ArrayList <String> targetEPRs;
	
	/** 
	 * Stringa che rappresenta il processID 
	 */
	private String processID;
	
	/** 
	 * Stringa che rappresenta una operation 
	 */
	private String operation;
	
	/** 
	 * Stringa che rappresenta lo status 
	 */
	private String status;

	
	private String busChannel;
	
	/**
	 *  Costruttore della classe 
	 */
	public DataWorkflow (String fileName)
	{
		/* inizializziamo la lista dei target */
		this.targetEPRs = new ArrayList <String>();
		
		/* inizializziamo il nome del file */
		this.fileName = fileName;
	}
	

	public DataWorkflow() {
		// TODO Auto-generated constructor stub
		/* inizializziamo la lista dei target */
		this.targetEPRs = new ArrayList <String>();
	}


	/**
	 * Metodo che serve ad avere il nome del file
	 * @return
	 */
	public String getFileName ()
	{
		return this.fileName;
	}
	
	
	/**
	 * Metodo che restituisce la lista dei targetEPR
	 * @return arrayList dei targetEPR
	 */
	public ArrayList <String> getTargetEPRs ()
	{
		return this.targetEPRs;
	}
	
	

	/**
	 *  Getter di processID 
	 */
	public String getProcessID() {
		return processID;
	}

	/**
	 * Setter di processID
	 * @param processID stringa che rappresenta un processID
	 */
	public void setProcessID(String processID) {
		this.processID = processID;
	}

	/**
	 *  Getter di operation 
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * Setter di operation
	 * @param operation stringa che rappresenta un operation
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 *  Getter di status 
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Setter di status
	 * @param status stringa che rappresenta uno status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	
	
	
	
	public String getBusChannel() {
		return busChannel;
	}


	public void setBusChannel(String busChannel) {
		this.busChannel = busChannel;
	}


	/**
	 * Metodo che permette di inserire un nuovo targetEPR
	 * @param targetEPR stringa che rappresenta un targetEPR
	 * @throws Exception eccezione lanciata quando non si inserisce una stringa
	 */
	public void addTargetEPR (String targetEPR) throws Exception
	{
		/* cotrolliamo che il target esista */
		if (targetEPR != null)
		{
			this.targetEPRs.add(targetEPR);
		}

	}

}
