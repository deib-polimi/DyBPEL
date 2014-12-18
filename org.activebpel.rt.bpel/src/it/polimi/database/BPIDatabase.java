package it.polimi.database;


import it.polimi.data.DataWorkflow;


import java.sql.SQLException;
import java.util.ArrayList;

public interface BPIDatabase {

	/**
	 * Metodo che permette di inserire un nodo di tipo worflow nel sistema
	 * @param node identifica un nodo di tipo workflow
	 * @throws SQLException eccezione lanciata quando ci sono problemi nella query
	 */
	public abstract void insertWorkflowNode(DataWorkflow node)
			throws SQLException;

	
	/**
	 * Metodo che permette di estrarre una lista degli oggetti di tipo workflow dal 
	 * database
	 * @param processName il nome del processo a cui siamo interessati
	 * @param operation l'operazione di I/O del processo (receive, reply, invoke)
	 * @return la lista degli oggetti di tipo workflow
	 * @throws SQLException eccezione lanciata nel caso di problemi nell'esecuzione 
	 * della query SQL
	 */
	public abstract ArrayList<DataWorkflow> extractWorkflowNode(
			String processName, String operation) throws SQLException;
	
	/**
	 * Metodo che permette di estrarre una lista degli oggetti di tipo workflow dal 
	 * database
	 * @param processName il nome del processo a cui siamo interessati
	 * @return la lista degli oggetti di tipo workflow
	 * @throws SQLException eccezione lanciata nel caso di problemi nell'esecuzione 
	 * della query SQL
	 */
	public abstract ArrayList<DataWorkflow> extractWorkflowNodes(
			String processName) throws SQLException;

	/**
	 * Metodo che permette di eliminare tutte le tuple che dipendono da un file
	 * @param bpiFile file da cui dipendono le tuple
	 * @throws SQLException errore di SQL 
	 */
	public abstract void removeFile(String bpiFile) throws SQLException;
	
	
	/**
	 * Metodo che permette di eliminare una tupla di workflow
	 * @param bpiFile file da cui dipendono le tuple
	 * @throws SQLException errore di SQL 
	 */
	public abstract void removeWorkflow(String processID, String operation, String status, String correlationKey, String correlationValue) throws SQLException;
	
	public abstract void removeEPRs(String processID, String operation, String status, String dataExtraction, String correlationKey, String correlationValue) throws SQLException;

	public abstract void closeDB();
	
	public abstract void closeAndDeleteDB();

	public abstract ArrayList<String> extractProcessIDs() throws SQLException;

}