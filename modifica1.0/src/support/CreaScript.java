package support;
import java.io.*;
/**
 * Classe per la gestione di script in windows
 * @author Patrik
 *
 */
public abstract class CreaScript{

	/**
	 * Crea uno script che serve per porter creare il processo bpel che avrà un'estensione .bpr.
	 * Come parametri in ingresso verranno dati la versione, che servirà soltanto 
	 * per il nome del file bpel creato, e la path che è la cartella contenente tutto il necessario
	 * per compilare un file bpel. 
	 * Per esempio se verrà chiamato il metodo in questo modo:
	 * String script= CreaScript.creaScript("v1", "c:\\loan_approval\\");
	 * la gerarchia della cartella loan_approval sarà:
	 * loan_approval
	 *  - bpel
	 *  	- loan_approval.bpel
	 *  - META-INF
	 *  	- MANIFEST.MF
	 *  	- catalog.xml
	 *  - wsdl
	 *  	- loan_approval.wsdl
	 *  - loan_approval.pdd
	 *  
	 * lo script verra quindi
	 * 
	 * da porter eseguire 
	 * @param versione
	 * @param path
	 * @return una stringa contenente la path completa dello script creato. Nel caso c'è un errore il 
	 *  metodo ritorna una stringa contenente "errore"
	 */
	public static String creaScript(String nomeFile,String path)
	{
	
		String pathScript = path + "/script.bat";	
		System.out.println(pathScript);
		FileOutputStream file;
		try {
			file = new FileOutputStream(pathScript);
		} catch (IOException e) {
			  
		      return "errore";
		 }
		PrintStream output = new PrintStream(file);
		output.println("cd "+ path);
		output.println("jar cf "+nomeFile+" *.pdd META-INF bpel wsdl");
		output.println("copy /Y "+nomeFile+" C:\\ORCServices\\bpr");
		output.close();
		return pathScript;
		 
	}
	/**
	 * Viene eseguito uno script con la path indicata nel parametro di ingresso
	 * @param path
	 * @throws IOException
	 */
	public static void eseguiScript(String path) throws IOException
	{
		
		String[] cmd = new String[3];
        cmd[0] = "cmd.exe";
        cmd[1] = "/C";
        cmd[2] = path;
        
    	if(path.equals("errore"))
		{
			System.out.println("errore nella creazione dello script");
		}else 
		{
			Runtime.getRuntime().exec(cmd);
			System.out.println("nuova file bpel creato");
		    
		}
	}

}
