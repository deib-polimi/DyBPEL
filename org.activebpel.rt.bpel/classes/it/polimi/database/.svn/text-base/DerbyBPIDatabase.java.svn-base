package it.polimi.database;


import it.polimi.data.DataWorkflow;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.activebpel.rt.bpel.impl.instrumentation.WwdUtils;

public class DerbyBPIDatabase implements BPIDatabase {
	
	// define the driver to use 
    //String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    String driver = "org.apache.derby.jdbc.ClientDriver";
 // the database name  
    String dbName="DerbyBPI";
 // define the Derby connection URL to use 
    
    
    Connection conn = null;
	
	public DerbyBPIDatabase() {
		super();
		
	}
	
	public void connectDB() {
		
	      //String connectionURL = "jdbc:derby:" + dbName + ";create=true";
	      //String connectionURL = "jdbc:derby:Databases/" + dbName + ";create=true";
	      //String connectionURL = "jdbc:derby:Databases/" + dbName + ";";
			String connectionURL = "jdbc:derby://localhost:1527/DerbyBPI;create=true";
		  //String connectionURL = "jdbc:derby:DerbyBPI;create=true";
		  //String connectionURL = "jdbc:derby://localhost:1527/"+dbName+";create=true";
		//  String connectionURL = "jdbc:derby://ORCDatabase:1527/"+dbName+";create=true";

	      try {	   
			Class.forName(driver).newInstance(); 
	        System.out.println(driver + " loaded. ");
	          
	      } catch(java.lang.ClassNotFoundException e) {
	          System.err.print("ClassNotFoundException: ");
	          System.err.println(e.getMessage());
	          System.out.println("\n    >>> Please check your CLASSPATH variable   <<<\n");
	      } catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			
			conn = DriverManager.getConnection(connectionURL);
			System.out.println("******* Connected to database " + dbName);	 
 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}


	      
	      
		}
	
	public void connectAndCreateDB() {
		
      //String connectionURL = "jdbc:derby:" + dbName + ";create=true";
      //String connectionURL = "jdbc:derby:Databases/" + dbName + ";create=true";
      //String connectionURL = "jdbc:derby:Databases/" + dbName + ";";
		String connectionURL = "jdbc:derby://localhost:1527/DerbyBPI;create=true";
	  //String connectionURL = "jdbc:derby:DerbyBPI;create=true";
	  //String connectionURL = "jdbc:derby://localhost:1527/"+dbName+";create=true";
	//  String connectionURL = "jdbc:derby://ORCDatabase:1527/"+dbName+";create=true";

      try {	   
		Class.forName(driver).newInstance(); 
        System.out.println(driver + " loaded. ");
          
      } catch(java.lang.ClassNotFoundException e) {
          System.err.print("ClassNotFoundException: ");
          System.err.println(e.getMessage());
          System.out.println("\n    >>> Please check your CLASSPATH variable   <<<\n");
      } catch (InstantiationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}


	// Create (if needed) and connect to the database
    
		try {
			
			conn = DriverManager.getConnection(connectionURL);
			System.out.println("******* Connected to database " + dbName);	 
			
			if (this.tablesExist() == true) {
				this.deleteAllTable();
				this.createTables();
			}
			else {
				this.createTables();
			}

		    
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


      
      
	}
	
	private void deleteAllTable () throws SQLException
	{
		String query = "DROP TABLE workflow";
		this.insertQuery(query);
		System.out.println(" . . . . deleted workflow");

		query = "DROP TABLE targeteprs";
		this.insertQuery(query);
		System.out.println(" . . . . deleted targeteprs");

	}
	
	/**
	 * Metodo che permette di inserire una query
	 * @param query query di insert 
	 * @throws SQLException eccezione lanciata nel caso di problemi di SQL
	 */
	void insertQuery (String query) throws SQLException
	{
		/* Creiamo lo Statement per l'esecuzione della query */
		Statement s = conn.createStatement();     
		
		/* eseguiamo la query */
        s.executeUpdate(query);
	}
	
	/**
	 * Metodo per eseguire una query per estrarre i dati dal database
	 * @param query la query SQL da eseguire
	 * @return il risultato della query
	 * @throws SQLException eccezione lanciata nel caso di problemi nell'esecuzione 
	 * della query SQL
	 */
	ResultSet extractQuery(String query) throws SQLException {
		
		/* Creiamo lo Statement per l'esecuzione della query */
		Statement s = conn.createStatement();
		
		/* Eseguiamo la query e restituiamo il risultato */
		return s.executeQuery(query);
	}

	private void createTables() {
		// TODO Auto-generated method stub
		Statement s = null;
		try {
			s = conn.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String createString = "CREATE TABLE targeteprs (" +
					  "targetepr varchar(255) NOT NULL, " +
					  "processID varchar(255) NOT NULL, " +
					  "operation varchar(255) NOT NULL, " +
					  "status varchar(255) NOT NULL, " +
					  "uniqueID varchar(255) NOT NULL, " +
					  "correlationKey varchar(255) NOT NULL, "+
					  "correlationValue varchar(255) NOT NULL)";
		//System.out.println("CREATE: " + createString);
		System.out.println("--> Created targetepr table.");
		try {
			this.insertQuery(createString);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		createString = "CREATE TABLE workflow (" +
					  "uniqueID varchar(100) NOT NULL, " +
					  "processID varchar(255) NOT NULL, " +
					  "operation varchar(255) NOT NULL, " +
					  "status varchar(255) NOT NULL, " +
					  "correlationKey varchar(255) NOT NULL, "+
					  "correlationValue varchar(255) NOT NULL)";
		//System.out.println("CREATE: " + createString);
		System.out.println("--> Created workflow table.");
		try {
			this.insertQuery(createString);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ArrayList<DataWorkflow> extractWorkflowNode(String processName,
			String operation) throws SQLException {
		// TODO Auto-generated method stub
		/* Gli oggetti necessari a contenere i risultati delle due query */
		ResultSet dataSet;
		ResultSet dataEPRSet;
		/* L'oggetto equivalente a una tupla ottenuta dalla query*/
		DataWorkflow data;
		/* La lista contenente tutti gli oggetti da restituire */
		ArrayList<DataWorkflow> dataList = new ArrayList<DataWorkflow>();
		
		/* Estraiamo dal database le tuple di nostro interesse */
		String query = "SELECT * FROM workflow w WHERE w.processID = '" + processName + "' and w.operation = '" + operation + "'";
		dataSet = this.extractQuery(query);
		
		/* Riempiamo una lista partendo dalle tuple ottenute con la query */
		while (dataSet.next()) {
			/* Istanziamo un nuovo oggetto e settiamo i suoi campi */
			data = new DataWorkflow();

			data.setProcessID(dataSet.getString("processID"));
			data.setOperation(dataSet.getString("operation"));
			data.setStatus(dataSet.getString("status"));
			data.setCorrelationKey(dataSet.getString("correlationKey"));
			data.setCorrelationValue(dataSet.getString("correlationValue"));
			
			/* Estraiamo dal database gli EPR dell'oggetto che stiamo creando */
			String EPRquery = "SELECT targetepr FROM targeteprs e WHERE e.processID = '" + data.getProcessID() + "' and e.operation = '" + data.getOperation() + "' and e.status = '" + data.getStatus() + "' and e.correlationKey = '" + data.getCorrelationKey() + "' and e.correlationValue = '" + data.getCorrelationValue() +"'";
			dataEPRSet = this.extractQuery(EPRquery);

			/* Riempiamo una lista partendo dalle tuple ottenute con la query */
			while(dataEPRSet.next()) {
					try {
						data.addTargetEPR(dataEPRSet.getString("targetepr"));
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
			/* Aggiungiamo l'oggetto alla lista da restituire */
			dataList.add(data);
		}
		/* Restituiamo la lista */
		return dataList;
	}

	
	public void insertWorkflowNode(DataWorkflow node) throws SQLException {
		
		boolean error = false;
		
		conn.setAutoCommit(false);
		
		String query;
		
		
		query = "INSERT INTO workflow VALUES ('" + node.getUniqueID() + "', '" + node.getProcessID() + "' ,  '" + node.getOperation() + "' , '" + node.getStatus() + "' , '" + node.getCorrelationKey() + "' , '" + node.getCorrelationValue() + "')";
		
		try {
//			System.out.println("--> Preforming node insertion");
//			System.out.println("Query: " + query);
			this.insertQuery(query); 
			System.out.println("--> Completed node inseriton");
		}
		catch (Exception e) {
			error=true;
			System.out.println(e);
		}
		
		ArrayList <String> targetEPRs = node.getTargetEPRs();
		
		/* inseriamo tutti i valori dei targetEPR */
		for (int i=0; i < targetEPRs.size(); i++)
		{
			query = "INSERT INTO targeteprs VALUES ('" + targetEPRs.get(i) + "' , '" + node.getProcessID() + "' , '" + node.getOperation() + "' , '" + node.getStatus() + "' , '" + node.getUniqueID() + "' , '" + node.getCorrelationKey() + "' , '" + node.getCorrelationValue() + "')";
			try {
//				System.out.println("--> Performing targetEpr insertion");
//				System.out.println("Query: " + query);
				this.insertQuery(query); 
				System.out.println("--> Completed targetEpr insertion");
			}
			catch (Exception e) {
				error=true;
				System.out.println(e);
			}
			
		}

		if (error == false)	conn.commit();
		
		conn.setAutoCommit(true);

	}

	

	public void removeFile(String bpiFile) throws SQLException {

		String query = "DELETE FROM workflow WHERE fileName = '" + bpiFile + "'";
		this.insertQuery(query);
		System.out.println(" . . . . deleting from workflow");
				
		//must delete targeteprs as well
		


	}
	
	public boolean tablesExist () throws SQLException {
	      boolean chk = true;
	      boolean doCreate = false;
	      try {
	         Statement s = conn.createStatement();
	         s.execute("select * from workflow");
	      }  catch (SQLException sqle) {
	         return false;
	      }
	      //  System.out.println("Just got the warning - table exists OK ");
	      return true;
	   }

	public void closeAndDeleteDB() {
		// TODO Auto-generated method stub
		boolean gotSQLExc = false;
		try {
			this.deleteAllTable();
			conn.close();
            //DriverManager.getConnection("jdbc:derby:;shutdown=true");   
            
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			if (e.getSQLState().equals("XJ015") ) {		
	             gotSQLExc = true;
	        } else e.printStackTrace();
		}
//		
//		if (!gotSQLExc) {
//	       	  System.out.println("Database did not shut down normally");
//	       }  else  {
//	          System.out.println("Database shut down normally");	
//	       }
		
	}
	public void closeDB() {
		// TODO Auto-generated method stub
		boolean gotSQLExc = false;
		try {
			conn.close();
            //DriverManager.getConnection("jdbc:derby:;shutdown=true");   
            
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			if (e.getSQLState().equals("XJ015") ) {		
	             gotSQLExc = true;
	        } else e.printStackTrace();
		}
		
	}

	public ArrayList<DataWorkflow> extractWorkflowNodes(String processName)
			throws SQLException {
		// TODO Auto-generated method stub
		/* Gli oggetti necessari a contenere i risultati delle due query */
		ResultSet dataSet;
		ResultSet dataEPRSet;
		/* L'oggetto equivalente a una tupla ottenuta dalla query*/
		DataWorkflow data;
		/* La lista contenente tutti gli oggetti da restituire */
		ArrayList<DataWorkflow> dataList = new ArrayList<DataWorkflow>();
		
		/* Estraiamo dal database le tuple di nostro interesse */
		String query = "SELECT * FROM workflow w WHERE w.processID = '" + processName + "'";
		System.out.println("[TEST] Extracting with query " + query);
		dataSet = this.extractQuery(query);
		
		/* Riempiamo una lista partendo dalle tuple ottenute con la query */
		while (dataSet.next()) {
			/* Istanziamo un nuovo oggetto e settiamo i suoi campi */
			data = new DataWorkflow();
			data.setUniqueID(dataSet.getString("uniqueID"));
			data.setProcessID(dataSet.getString("processID"));
			System.out.println("[TEST] Extracting... operation " + dataSet.getString("operation") );
			data.setOperation(dataSet.getString("operation"));
			System.out.println("[TEST] Set in data operation " + data.getOperation());
			//data.setDataExtraction(dataSet.getString("dataExtraction"));
			data.setStatus(dataSet.getString("status"));
			data.setCorrelationKey(dataSet.getString("correlationKey"));
			data.setCorrelationValue(dataSet.getString("correlationValue"));
			
			/* Estraiamo dal database gli EPR dell'oggetto che stiamo creando */
			String EPRquery = "SELECT targetepr FROM targeteprs e WHERE e.uniqueID = '" + data.getUniqueID() + "' and e.processID = '" + data.getProcessID() + "' and e.operation = '" + data.getOperation() + "' and e.status = '" + data.getStatus() + "' and e.correlationKey = '" + data.getCorrelationKey() +"' and e.correlationValue = '" + data.getCorrelationValue() +"'" ;
			//String EPRquery = "SELECT targetepr FROM targeteprs e WHERE e.uniqueID = '" + data.getUniqueID() + "' and e.fileName = '" + data.getFileName() + "'";
			dataEPRSet = this.extractQuery(EPRquery);

			/* Riempiamo una lista partendo dalle tuple ottenute con la query */
			while(dataEPRSet.next()) {
					try {
						data.addTargetEPR(dataEPRSet.getString("targetepr"));
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
			/* Aggiungiamo l'oggetto alla lista da restituire */
			dataList.add(data);
		}
		/* Restituiamo la lista */
		return dataList;
	}

	public void removeWorkflow(String processID, String operation,
			String status, String correlationKey, String correlationValue) throws SQLException {
		// TODO Auto-generated method stub
		
		conn.setAutoCommit(false);
		
		String query = "DELETE FROM workflow WHERE processID = '" + processID + "' and operation = '" + operation + "' and status = '" + status + "' and correlationKey = '" + correlationKey + "' and correlationValue = '" + correlationValue + "'" ;
		this.insertQuery(query);
		
		query = "DELETE FROM targeteprs WHERE processID = '" + processID + "' and operation = '" + operation + "' and status = '" + status + "' and correlationKey = '" + correlationKey + "' and correlationValue = '" + correlationValue + "'" ;
		this.insertQuery(query);
		
		conn.commit();
		
		conn.setAutoCommit(true);
		
		
	}

	public void removeEPRs(String processID, String operation, String status,
			String dataExtraction, String correlationKey, String correlationValue) throws SQLException {
		// TODO Auto-generated method stub
		
		String query = "DELETE FROM targeteprs WHERE processID = '" + processID + "' and operation = '" + operation + "' and status = '" + status + "' and dataExtraction = '" + dataExtraction +"' and correlationKey = '" + correlationKey + "' and correaltionValue = '" + correlationValue +"'" ;
		this.insertQuery(query);
		
	}

	public ArrayList<String> extractProcessIDs() throws SQLException {
		// TODO Auto-generated method stub
		
		ResultSet dataSet;
		ArrayList<String> result = new ArrayList<String>();
		
		/* Estraiamo dal database le processID di nostro interesse */
		String query = "SELECT distinct processID FROM workflow";
		dataSet = this.extractQuery(query);
		
		/* Riempiamo una lista partendo dalle tuple ottenute con la query */

		while (dataSet.next()) {
			/* Istanziamo un nuovo oggetto e settiamo i suoi campi */
			
			result.add(dataSet.getString("processID"));

		}
		/* Restituiamo la lista */
		return result;
	}







}
