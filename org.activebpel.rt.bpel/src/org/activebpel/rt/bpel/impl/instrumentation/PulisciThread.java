package org.activebpel.rt.bpel.impl.instrumentation;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Questa classe che estende la classe thread serve per cancellare le vecchie versioni dei processi BPLE
 * non più utilizzate da nessuno
 * per fare ciò si connette al database versionmanager e controlla che non ci siano istanze attive
 * su ogni versione deployata per ogni processo (con eccezzione dell'ultima versione), se il numero di istanze attive è = 0 allora 
 * undeploya quella versione del processo. 
 * @author patrik
 *
 */
public class PulisciThread extends Thread 
{
	private static String driver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost/versionmanager";
	public PulisciThread() 
	{
		super();
	}
		
	public void run() 
	{
		while(true) 
		{
			try {
				Class.forName(driver);
				System.out.println("sveglio thread");
				Connection con = DriverManager.getConnection (url, "root", "20002000");
				ArrayList<Record> daNonCancellare = new ArrayList<Record>();
				ArrayList<Record> daCancellare = new ArrayList<Record>();
				Statement cmd = con.createStatement ();
				String qry = "SELECT cod_proc,version,istanze_attive,data,dest_path,fileName FROM versiontable ORDER BY data desc";
				
				ResultSet res = cmd.executeQuery(qry);  
				
				int cod_proc=0;
				String version = null;
				int istanze_attive = 0;
				java.sql.Timestamp data = null;
				String dest_path = null;
				String fileName =null;
				while(res.next())
				{
					//passo 0
					cod_proc = res.getInt("cod_proc");
					version = res.getString("version");
					istanze_attive = res.getInt("istanze_attive");
					data = res.getTimestamp("data");
					dest_path = res.getString("dest_path");
					fileName = res.getString("fileName");
					boolean cancellare = false;
					for(int i=0;i<daNonCancellare.size();i++)
					{
						if(daNonCancellare.get(i).getCod_proc()==cod_proc)
						{
							cancellare = true;
							break;
						}
					}
					Record record = new Record(cod_proc,version,istanze_attive,dest_path,fileName);
					if(cancellare)
					{
						if(record.getIstanze_attive()==0)
							daCancellare.add(record);
					}else
					{
						daNonCancellare.add(record);
					}
				}
			
			
				for(int j=0; j<daCancellare.size();j++)
				{
					String cancella = "DELETE FROM versiontable WHERE cod_proc=? AND version=?";
					PreparedStatement SQLPreparedStatement = con.prepareStatement(cancella);
					File file = new File(daCancellare.get(j).getDest_path()+"/"+daCancellare.get(j).getFileName());
					System.out.println(file.delete());					
					SQLPreparedStatement.setInt(1,daCancellare.get(j).getCod_proc());
					SQLPreparedStatement.setString(2,daCancellare.get(j).getVersion());
				
					SQLPreparedStatement.executeUpdate(); 
					
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				sleep(60000);
			} catch (InterruptedException e){}
			
			
		}
	
	}
}
