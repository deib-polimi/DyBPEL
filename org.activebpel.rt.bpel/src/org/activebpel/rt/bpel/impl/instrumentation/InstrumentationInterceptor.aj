package org.activebpel.rt.bpel.impl.instrumentation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.activebpel.rt.bpel.def.AeCorrelationsDef;
import org.activebpel.rt.bpel.def.AeProcessDef;
import org.activebpel.rt.bpel.def.AeVariableDef;
import org.activebpel.rt.bpel.def.activity.AeActivityAssignDef;
import org.activebpel.rt.bpel.def.activity.AeActivityInvokeDef;
import org.activebpel.rt.bpel.def.activity.AeActivityPickDef;
import org.activebpel.rt.bpel.def.activity.AeActivityReceiveDef;
import org.activebpel.rt.bpel.def.activity.AeActivityReplyDef;
import org.activebpel.rt.bpel.def.activity.AeActivitySequenceDef;
import org.activebpel.rt.bpel.def.activity.IAeMessageDataConsumerDef;
import org.activebpel.rt.bpel.def.activity.IAeMessageDataProducerDef;
import org.activebpel.rt.bpel.def.activity.support.AeAssignCopyDef;
import org.activebpel.rt.bpel.def.activity.support.AeCorrelationDef;
import org.activebpel.rt.bpel.def.activity.support.AeCorrelationDef.AeCorrelationPatternType;
import org.activebpel.rt.bpel.def.activity.support.AeFromDef;
import org.activebpel.rt.bpel.def.activity.support.AeFromPartDef;
import org.activebpel.rt.bpel.def.activity.support.AeFromPartsDef;
import org.activebpel.rt.bpel.def.activity.support.AeLiteralDef;
import org.activebpel.rt.bpel.def.activity.support.AeOnMessageDef;
import org.activebpel.rt.bpel.def.activity.support.AeQueryDef;
import org.activebpel.rt.bpel.def.activity.support.AeToDef;
import org.activebpel.rt.bpel.def.activity.support.AeToPartDef;
import org.activebpel.rt.bpel.def.activity.support.AeToPartsDef;
import org.activebpel.rt.bpel.def.io.readers.def.AeSpecStrategyKey;
import org.activebpel.rt.bpel.def.visitors.preprocess.strategies.wsio.IAeMessageDataStrategyNames;
import org.activebpel.rt.bpel.impl.AeBPELMessageDataValidator;
import org.activebpel.rt.bpel.impl.AeBusinessProcess;
import org.activebpel.rt.bpel.impl.IAeActivityParent;
import org.activebpel.rt.bpel.impl.IAeMessageValidator;
import org.activebpel.rt.bpel.impl.activity.AeActivityAssignImpl;
import org.activebpel.rt.bpel.impl.activity.AeActivityImpl;
import org.activebpel.rt.bpel.impl.activity.AeActivityInvokeImpl;
import org.activebpel.rt.bpel.impl.activity.AeActivityPickImpl;
import org.activebpel.rt.bpel.impl.activity.AeActivityReceiveImpl;
import org.activebpel.rt.bpel.IAeFault;
import org.activebpel.rt.bpel.impl.activity.AeActivityReplyImpl;
import org.activebpel.rt.bpel.impl.activity.AeActivitySequenceImpl;
import org.activebpel.rt.bpel.impl.activity.assign.AeCopyOperation;
import org.activebpel.rt.bpel.impl.activity.assign.IAeAssignOperation;
import org.activebpel.rt.bpel.impl.activity.assign.IAeFrom;
import org.activebpel.rt.bpel.impl.activity.assign.IAeTo;
import org.activebpel.rt.bpel.impl.activity.assign.from.AeFromBase;
import org.activebpel.rt.bpel.impl.activity.assign.from.AeFromExpression;
import org.activebpel.rt.bpel.impl.activity.assign.from.AeFromLiteral;
import org.activebpel.rt.bpel.impl.activity.assign.from.AeFromPartnerLink;
import org.activebpel.rt.bpel.impl.activity.assign.from.AeFromPropertyElement;
import org.activebpel.rt.bpel.impl.activity.assign.from.AeFromPropertyMessage;
import org.activebpel.rt.bpel.impl.activity.assign.from.AeFromPropertyType;
import org.activebpel.rt.bpel.impl.activity.assign.from.AeFromVariableElement;
import org.activebpel.rt.bpel.impl.activity.assign.from.AeFromVariableElementWithQuery;
import org.activebpel.rt.bpel.impl.activity.assign.from.AeFromVariableMessage;
import org.activebpel.rt.bpel.impl.activity.assign.from.AeFromVariableMessagePart;
import org.activebpel.rt.bpel.impl.activity.assign.from.AeFromVariableMessagePartWithQuery;
import org.activebpel.rt.bpel.impl.activity.assign.from.AeFromVariableType;
import org.activebpel.rt.bpel.impl.activity.assign.from.AeFromVariableTypeWithQuery;
import org.activebpel.rt.bpel.impl.activity.assign.to.AeToBase;
import org.activebpel.rt.bpel.impl.activity.assign.to.AeToPartnerLink;
import org.activebpel.rt.bpel.impl.activity.assign.to.AeToPropertyElement;
import org.activebpel.rt.bpel.impl.activity.assign.to.AeToPropertyMessage;
import org.activebpel.rt.bpel.impl.activity.assign.to.AeToPropertyType;
import org.activebpel.rt.bpel.impl.activity.assign.to.AeToVariableElement;
import org.activebpel.rt.bpel.impl.activity.assign.to.AeToVariableElementWithQuery;
import org.activebpel.rt.bpel.impl.activity.assign.to.AeToVariableMessage;
import org.activebpel.rt.bpel.impl.activity.assign.to.AeToVariableMessagePart;
import org.activebpel.rt.bpel.impl.activity.assign.to.AeToVariableMessagePartWithQuery;
import org.activebpel.rt.bpel.impl.activity.assign.to.AeToVariableType;
import org.activebpel.rt.bpel.impl.activity.assign.to.AeToVariableTypeWithQuery;
import org.activebpel.rt.bpel.impl.activity.support.AeCorrelationsImpl;
import org.activebpel.rt.bpel.impl.activity.support.AeCorrelationsPatternImpl;
import org.activebpel.rt.bpel.impl.activity.support.AeIMACorrelations;
import org.activebpel.rt.bpel.impl.activity.support.AeOnMessage;
import org.activebpel.rt.bpel.impl.activity.wsio.consume.AeFromPartsMessageDataConsumer;
import org.activebpel.rt.bpel.impl.activity.wsio.consume.AeNoopMessageDataConsumer;
import org.activebpel.rt.bpel.impl.activity.wsio.consume.AeVariableMessageDataConsumer;
import org.activebpel.rt.bpel.impl.activity.wsio.consume.IAeMessageDataConsumer;
import org.activebpel.rt.bpel.impl.activity.wsio.produce.AeEmptyMessageDataProducer;
import org.activebpel.rt.bpel.impl.activity.wsio.produce.AeToPartsMessageDataProducer;
import org.activebpel.rt.bpel.impl.activity.wsio.produce.AeVariableMessageDataProducer;
import org.activebpel.rt.bpel.impl.activity.wsio.produce.IAeMessageDataProducer;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;



//ricordarsi di togliere il commento alla riga 169
public aspect InstrumentationInterceptor{
	private long monitor = 0;
	private long app = 0;
	private long app2 = 0;
	private long modifica = 0;
	private int id = 500;
	private boolean threadLanciato = false;
	private HashMap<String,Integer> modEff = new HashMap<String,Integer>();
	
	
	pointcut startProcess(AeBusinessProcess process):
		execution(public void AeBusinessProcess.execute()) && target(process);
	before (AeBusinessProcess process): startProcess(process) {
		
		process.setMaxLocationId(1000);
		String nomeProcesso = process.getName().getLocalPart();
		
		String versione = nomeProcesso.replaceAll("^[a-zA-Z]*","");
		String nome = nomeProcesso.replace(versione, "");
		if(versione.equals(""))
			versione = "1";
		String driver = "com.mysql.jdbc.Driver";
	    try {
			Class.forName(driver);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	       
		//mi connetto al server di mysql al db versionmanager
	  	String url = "jdbc:mysql://localhost/versionmanager";
	  	Connection con = null;
	  	ResultSet res = null;
		
	  	try {
			con = DriverManager.getConnection (url, "root", "20002000");
		
			Statement cmd = null;
		
			cmd = con.createStatement();
			
			String qry = "SELECT versiontable.cod_proc,versiontable.version,versiontable.istanze_attive " +
					"FROM versiontable,processtable " +
					"WHERE versiontable.cod_proc=processtable.cod_proc AND processtable.name='"+nome+"'" +
							"AND versiontable.version = '"+versione+"'";
			res = cmd.executeQuery(qry);
			if(res.next())
			{
				int istanze_attive = res.getInt("versiontable.istanze_attive");
				int cod_proc = res.getInt("versiontable.cod_proc");
				istanze_attive++;
				Statement stmt = con.createStatement(); 
				String sql = "UPDATE versiontable SET istanze_attive="+istanze_attive+" " +
						"WHERE cod_proc = "+cod_proc+" AND version = '"+versione+"'"; 
				//System.out.println(sql);
				stmt.executeUpdate(sql); 
			}
		  	} catch (SQLException e) {
				
				e.printStackTrace();
			}
		  	if(!threadLanciato)
			{
				threadLanciato = true;
				//new PulisciThread().start();
			}
	  	
	}
	
	pointcut termProcess(AeBusinessProcess process):
		execution(public void AeBusinessProcess.processEnded(IAeFault)) && target(process);

	after (AeBusinessProcess process): termProcess(process) {
		
		
		String nomeProcesso = process.getName().getLocalPart();
		String versione = nomeProcesso.replaceAll("^[a-zA-Z]*","");
		String nome = nomeProcesso.replace(versione, "");
		if(versione.equals(""))
			versione = "1";
		String driver = "com.mysql.jdbc.Driver";
	    try {
			Class.forName(driver);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	       
		//mi connetto al server di mysql al db versionmanager
	  	String url = "jdbc:mysql://localhost/versionmanager";
	  	Connection con = null;
	  	ResultSet res = null;
		
	  	try {
			con = DriverManager.getConnection (url, "root", "20002000");
		
			Statement cmd = null;
		
			cmd = con.createStatement();
			
			
			String qry = "SELECT versiontable.cod_proc,versiontable.version,versiontable.istanze_attive " +
			"FROM versiontable,processtable " +
			"WHERE versiontable.cod_proc=processtable.cod_proc AND processtable.name='"+nome+"'" +
				"AND versiontable.version = '"+versione+"'";
			res = cmd.executeQuery(qry);
			
			if(res.next())
			{
				int istanze_attive = res.getInt("versiontable.istanze_attive");
				int cod_proc = res.getInt("versiontable.cod_proc");
				istanze_attive--;
				Statement stmt = con.createStatement(); 
				String sql = "UPDATE versiontable SET istanze_attive="+istanze_attive+" " +
						"WHERE cod_proc = "+cod_proc+" AND version = '"+versione+"'"; 
				//System.out.println(sql);
				stmt.executeUpdate(sql); 
			}
		  	
	  	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Interaction pointcuts
	pointcut activityExecuted(AeActivityImpl aeAct):
		call(void execute()) && target(aeAct);
	before(AeActivityImpl aeAct): activityExecuted(aeAct) {
		if(aeAct.getLocationPath().equals("/process/sequence[@name='sequence1']/if[@name='If2']/if-condition/sequence[@name='G13seq']/while[@name='G13While']/pick[@name='G13Pick']/onMessage/sequence[@name='ReqNewsSeq']"))
		{
			monitor = 0;
			
		}
	
		app = System.currentTimeMillis();
		String driver = "com.mysql.jdbc.Driver";
	    try {
			Class.forName(driver);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	       
		//mi connetto al server di mysql al db versionmanager
	  	String url = "jdbc:mysql://localhost/versionmanager";
	  	String nomeProcesso = aeAct.getProcess().getName().getLocalPart();
	  	String v = nomeProcesso.replaceAll("^[a-zA-Z]*","");
	  	if(v.equals("")) v="1";
		Integer versioneProcesso = Integer.parseInt(v);
	  	String at_target = null;
		String at_nuova = null;
		String tipo_modifica =null;
	  	Connection con = null;
	  	ResultSet res = null;
		Integer version = null;
		Integer cod_adp = null;
		/*if(aeAct.getLocationPath().equals("/process/sequence/invoke[@name='invoke1']"))
		{
			AeVariable variabile = (AeVariable) aeAct.getProcess().getVariable(aeAct.getLocationPath(), "request");
			
			
			System.out.println("variabile.getName : "+variabile.getName());
			
			//questo non serve	System.out.println("variabile.getTypeData() : "+variabile.getTypeData());
			
			//no... System.out.println("variabile.getElement : "+variabile.getElement());
			
			// non serve System.out.println("variabile.getElementData() : "+variabile.getElementData());
			
			
			//no... System.out.println("variabile.getMessageData() : "+variabile.getMessageData());
			
			System.out.println("variabile.getMessageType() : "+variabile.getMessageType());
			System.out.println("variabile.getParent() : "+variabile.getParent());
			AeVariableDef varDef = variabile.getDefinition();
			System.out.println("varDef.getLocationId(): "+varDef.getLocationId());
			System.out.println("getPartCount(): "+varDef.getPartCount());
			System.out.println("getMessageParts(): "+varDef.getMessageParts());
			System.out.println("getMessageType(): "+varDef.getMessageType());
			System.out.println("getNamespaceMap(): "+varDef.getNamespaceMap());
			try {
				System.out.println("getPartInfo(): "+varDef.getPartInfo("nomeAutore"));
			} catch (AeBpelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		try
		{	
			modifica = 0;
			app2 = System.currentTimeMillis();
			con = DriverManager.getConnection (url, "root", "20002000");
			Statement cmd = null;
		
			cmd = con.createStatement();
			String at_int = aeAct.getLocationPath().replace("'","\\'");
			//System.out.println("\n----------- "+aeAct.getLocationPath());
			//System.out.println("at_int: " +at_int);
			String qry = "SELECT tipo_modifica,at_target,at_nuova,version,cod_adp FROM adaptationtable,processtable WHERE processtable.cod_proc=adaptationtable.cod_proc AND at_intercettare='"+at_int+"' AND processtable.name='"+aeAct.getProcess().getName().getLocalPart()+"' ORDER BY tipo_modifica ASC" ;
	  		//System.out.println(qry);
	  		
			res = cmd.executeQuery(qry);
			
			
			
			while(res.next())
			{
				
				
				
				cod_adp = res.getInt("cod_adp");
				tipo_modifica = res.getString("tipo_modifica");
				at_target = res.getString("at_target");
				at_nuova = res.getString("at_nuova");
				version = Integer.parseInt(res.getString("version"));
				//System.out.println("aeAct.getLocationPath() " + aeAct.getLocationPath());
				
				//System.out.println("aeAct.getParent().getLocationPath() " + aeAct.getParent().getLocationPath());
				AeActivityImpl parent = (AeActivityImpl) aeAct.getParent();
				Long id = aeAct.getProcess().getProcessId();
				String chiave = id.toString()+"-"+cod_adp.toString(); 
				
				//System.out.println("chiave "+chiave);
				if(!(modEff.containsKey(chiave)))
				{
					
					modEff.put(chiave,1);
				
					if(versioneProcesso<version)
					{	
						if(tipo_modifica.equals("aggiungi"))
						{
							System.out.println("aggiungo");
							XmlObject controllaAttivita = null;
						      
							try {
								controllaAttivita = XmlObject.Factory.parse(at_nuova);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} 
							XmlCursor cursorAttivita = controllaAttivita.newCursor();
							cursorAttivita.toNextToken();
						
							if(cursorAttivita.getName().getLocalPart().equals("assign"))
							{
								aggiungiAssign(at_target,at_nuova,parent);
							}else if (cursorAttivita.getName().getLocalPart().equals("reply"))
							{
								aggiungiReply(at_target,at_nuova,parent);
							}else if (cursorAttivita.getName().getLocalPart().equals("receive"))
							{
								aggiungiReceive(at_target,at_nuova,parent);
							}else if (cursorAttivita.getName().getLocalPart().equals("invoke"))
							{
								aggiungiInvoke(at_target,at_nuova,parent);
							}else if(cursorAttivita.getName().getLocalPart().equals("sequence"))
							{
								aggiungiSequence(at_target,at_nuova,parent);
							}else if((cursorAttivita.getName().getLocalPart().equals("onMessage")))
							{
								 //questo non va bene da modificare e inserire in ogni possibile attività
								aggiungiOnMessage(at_target, at_nuova, parent);
							}else if((cursorAttivita.getName().getLocalPart().equals("pick")))
							{
								aggiungiPick(at_target, at_nuova, parent);
							}
						}else if(tipo_modifica.equals("rimuovi"))
						{
							
							System.out.println("rimuovo");
							rimuoviAttivita(at_target,parent);
							
						}
						
					}
				}
			}
			res.close();
			cmd.close();
			con.close();
			
			modifica = modifica + System.currentTimeMillis() - app2;
			System.out.println("modifica: " + modifica);
			
			//System.out.println("\n\nfine metodo before");
			} catch (SQLException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			monitor = monitor + System.currentTimeMillis() - app;
			
			if(aeAct.getLocationPath().equals("/process/sequence[@name='sequence1']/if[@name='If2']/if-condition/sequence[@name='G13seq']/while[@name='G13While']/pick[@name='G13Pick']"))
			{
				System.out.println("tempo esecuzione del monitoring: " + monitor);
			}
	}
	
	private void rimuoviAttivita(String at_target,AeActivityImpl aeAct)
	{
		AeActivityImpl parent = (AeActivityImpl) aeAct;
		
		
		//questo solo in caso in cui il padre sia una sequence
		List itParent = ((AeActivitySequenceImpl)parent).getMActivities();
		for(int i=0;i<itParent.size();i++)
		{
			
			if((((AeActivityImpl)itParent.get(i)).getLocationPath().equals(at_target)))
			{ 
				AeActivitySequenceDef parentDef = (AeActivitySequenceDef)parent.getDefinition();
				parentDef.getActivityDefsList().remove(i);
		        itParent.remove(i);
				//System.out.println("\n\nattività rimossa");
			}//fine if su attivita target
		}//fine for sulle attivita del padre

	}
	
	private void aggiungiAssign(String at_target,String at_nuova,AeActivityImpl aeAct)
	{
		AeActivityImpl parent = (AeActivityImpl) aeAct;
		
		
		//questo solo in caso in cui il padre sia una sequence
		List itParent = ((AeActivitySequenceImpl)parent).getMActivities();
		if((at_target.equals(""))||(at_target==null))
		{
			aggiungiAssignPosizione(itParent,parent,at_nuova,-1);
		}
		else
		for(int i=0;i<itParent.size();i++)
		{
			
			if((((AeActivityImpl)itParent.get(i)).getLocationPath().equals(at_target)))
			{ 
				aggiungiAssignPosizione(itParent,parent,at_nuova,i);
				
			}//fine if su attivita target
		}//fine for sulle attivita del padre

        
	}
	private void aggiungiAssignPosizione(List itParent,AeActivityImpl parent,String at_nuova,int i)
	{
		
        XmlObject xmlObject = null;
       // File file = new File("assign.bpel");
		try {
			xmlObject = XmlObject.Factory.parse(at_nuova);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		XmlCursor cursor = xmlObject.newCursor();
		cursor.toNextToken();
		aggiungiAssignPosizioneCursor(itParent,parent,i,cursor);
		
		
	}
	
	
	private void aggiungiAssignPosizioneCursor(List itParent,AeActivityImpl parent,int i,XmlCursor cursor)
	{
		AeActivityAssignDef assignDef = new AeActivityAssignDef();
        AeActivitySequenceDef parentDef = (AeActivitySequenceDef)parent.getDefinition();
        assignDef.setLocationId(id);
        id++;
		
		String nameAssign = cursor.getAttributeText(new QName("name"));
		String nameSpaceURI = cursor.getName().getNamespaceURI();
		String assignPath = parentDef.getLocationPath()+"/assign[@name='"+nameAssign+"']";
		
        assignDef.setLocationPath(assignPath);
        assignDef.setName(nameAssign);
        assignDef.setParentXmlDef(parentDef);
        assignDef.setDefaultNamespace(nameSpaceURI);
        AeActivityAssignImpl assign = new AeActivityAssignImpl(assignDef, (IAeActivityParent) parent);
        
        //mi sposto sulla prima copy... una ci deve essere per forza per come è definita l'assign
        cursor.toFirstChild();
        //faccio una copia del cursore. cursorCopy verrà utilizzato per scorrere le copy
        XmlCursor cursorCopy = cursor.newCursor();
        boolean notFirstCopy = false;
        int countCopy=2;
        do
        {
        	cursor = cursorCopy.newCursor();
        	AeAssignCopyDef copyDef = new AeAssignCopyDef();
	        copyDef.setLocationId(id);
	        id++;
	        copyDef.setParentXmlDef(assignDef);
	        String copyPath = assignPath + "/copy";
	        if(notFirstCopy==false)
	        	notFirstCopy=true;
	        else
	        {
	        	copyPath = copyPath + "["+countCopy+"]";
	        	countCopy++;
	        }
	        copyDef.setLocationPath(copyPath);
	        cursor = cursorCopy.newCursor();
	        
	        AeFromDef fromDef = new AeFromDef();
	        fromDef.setLocationId(id);
	        id++;
	        String fromPath = copyPath + "/from";
	        fromDef.setLocationPath(fromPath);
	        fromDef.setParentXmlDef(copyDef);
	        
        	//ora mi sposto sulla from
        	cursor.toFirstChild();
        	AeSpecStrategyKey aStrategyKeyFrom = null;
        	if(cursor.getAttributeText(new QName("partnerLink")) != null)
        	{
        		//qui ci aspettiamo che sia una strategy from-partnerLink
        		fromDef.setPartnerLink(cursor.getAttributeText(new QName("partnerLink")));
        		fromDef.setEndpointReference(cursor.getAttributeText(new QName("endpointReference")));
        		aStrategyKeyFrom = new AeSpecStrategyKey("from-partnerLink");
        	}else if(cursor.getAttributeText(new QName("property")) != null)
        	{
        		//qui ci aspettiamo che sia una strategy from-property-.....
        		String varName = cursor.getAttributeText(new QName("variable"));
        		AeVariableDef variabile = parent.getProcess().getVariable(parent.getProcess().getLocationPath(), varName).getDefinition();
        		fromDef.setVariable(varName);
        		fromDef.setProperty(new QName(cursor.getAttributeText(new QName("property"))));
        		if(variabile.getMessageType()!=null)
        		{
        			//qui ci aspettiamo che sia una strategy from-property-message
        			aStrategyKeyFrom = new AeSpecStrategyKey("from-property-message");
        		}else if(variabile.getElement()!=null)
        		{
        			//qui ci aspettiamo che sia una strategy from-property-element
        			aStrategyKeyFrom = new AeSpecStrategyKey("from-property-element");
        		}else if(variabile.getType()!=null)
        		{
        			//qui ci aspettiamo che sia una strategy from-property-type
        			aStrategyKeyFrom = new AeSpecStrategyKey("from-property-type");
        		}
        		
        	}else if(cursor.getAttributeText(new QName("variable")) != null)
        	{
        		//qui ci aspettiamo che sia una strategy from-variable-.....
        		String varName = cursor.getAttributeText(new QName("variable"));
        		AeVariableDef variabile = parent.getProcess().getVariable(parent.getProcess().getLocationPath(), varName).getDefinition();
        		fromDef.setVariable(varName);
        		if(variabile.getMessageType()!=null)
        		{
        			//qui ci aspettiamo che sia una strategy from-variable-message-...
        			if(cursor.getAttributeText(new QName("part")) != null)
        			{
        				//qui ci aspettiamo che sia una strategy from-variable-message-part-..
        				fromDef.setPart(cursor.getAttributeText(new QName("part")));
        				if(cursor.getAttributeText(new QName("query")) != null)
        				{
        					//qui ci aspettiamo che sia una strategy from-variable-message-part-query
        					AeQueryDef queryDef = new AeQueryDef();
        					queryDef.setQuery(cursor.getAttributeText(new QName("query")));
        					queryDef.setQueryLanguage(null);
        					queryDef.setLocationId(id);
        					id++;
        					queryDef.setParentXmlDef(fromDef);
        					fromDef.setQueryDef(queryDef);
        					aStrategyKeyFrom = new AeSpecStrategyKey("from-variable-message-part-query");
        				}else if(cursor.toFirstChild())
        				{
        					//qui ci aspettiamo che sia una strategy from-variable-message-part-query
        					AeQueryDef queryDef = new AeQueryDef();
        					queryDef.setQuery(cursor.getTextValue());
        					queryDef.setQueryLanguage(null);
        					queryDef.setLocationId(id);
        					id++;
        					queryDef.setParentXmlDef(fromDef);
        					fromDef.setQueryDef(queryDef);
        					aStrategyKeyFrom = new AeSpecStrategyKey("from-variable-message-part-query");
        					cursor.toParent();
        				}else
        				{
        					//qui ci aspettiamo che sia una strategy from-variable-message-part
        					fromDef.setPart(cursor.getAttributeText(new QName("part")));
        					aStrategyKeyFrom = new AeSpecStrategyKey("from-variable-message-part");
        				}
        				
        			}else
        			{
        				//qui ci aspettiamo che sia una strategy from-variable-message
        				aStrategyKeyFrom = new AeSpecStrategyKey("from-variable-message");
        			}
        			
        		}else if(variabile.getElement()!=null)
        		{
        			//qui ci aspettiamo che sia una strategy from-variable-element
        			
        			if(cursor.getAttributeText(new QName("query"))!=null)
    				{
    					//qui ci aspettiamo che sia una strategy from-variable-element-query
    					AeQueryDef queryDef = new AeQueryDef();
    					queryDef.setQuery(cursor.getAttributeText(new QName("query")));
    					queryDef.setQueryLanguage(null);
    					queryDef.setLocationId(id);
    					id++;
    					queryDef.setParentXmlDef(fromDef);
    					fromDef.setQueryDef(queryDef);
    					aStrategyKeyFrom = new AeSpecStrategyKey("from-variable-element-query");
    				}else if(cursor.toFirstChild())
    				{
    					//qui ci aspettiamo che sia una strategy from-variable-element-query
    					AeQueryDef queryDef = new AeQueryDef();
    					queryDef.setQuery(cursor.getTextValue());
    					queryDef.setQueryLanguage(null);
    					queryDef.setLocationId(id);
    					id++;
    					queryDef.setParentXmlDef(fromDef);
    					fromDef.setQueryDef(queryDef);
    					aStrategyKeyFrom = new AeSpecStrategyKey("from-variable-element-query");
    					cursor.toParent();
    				}else
    				{
    					//qui ci aspettiamo che sia una strategy from-variable-element
    					aStrategyKeyFrom = new AeSpecStrategyKey("from-variable-element");
    				}
        		}else if(variabile.getType()!=null)
        		{
        			//qui ci aspettiamo che sia una strategy from-variable-type-..
        			if(cursor.getAttributeText(new QName("query"))!=null)
    				{
    					//qui ci aspettiamo che sia una strategy from-variable-type-query
    					AeQueryDef queryDef = new AeQueryDef();
    					queryDef.setQuery(cursor.getAttributeText(new QName("query")));
    					queryDef.setQueryLanguage(null);
    					queryDef.setLocationId(id);
    					id++;
    					queryDef.setParentXmlDef(fromDef);
    					fromDef.setQueryDef(queryDef);
    					aStrategyKeyFrom = new AeSpecStrategyKey("from-variable-type-query");
    				}else if(cursor.toFirstChild())
    				{
    					//qui ci aspettiamo che sia una strategy from-variable-message-part-query
    					AeQueryDef queryDef = new AeQueryDef();
    					queryDef.setQuery(cursor.getTextValue());
    					queryDef.setQueryLanguage(null);
    					queryDef.setLocationId(id);
    					id++;
    					queryDef.setParentXmlDef(fromDef);
    					fromDef.setQueryDef(queryDef);
    					aStrategyKeyFrom = new AeSpecStrategyKey("from-variable-type-query");
    					cursor.toParent();
    				}else
    				{
    					aStrategyKeyFrom = new AeSpecStrategyKey("from-variable-type");
    				}
        		}
        		
        	}else if(!(cursor.getTextValue().equals("")))
        	{
        		//qui ci aspettiamo che sia una strategy from-literal
        		aStrategyKeyFrom = new AeSpecStrategyKey("from-literal");

        		String toLiteral = "<literal>" +
        				cursor.getTextValue() +
        				"</literal>";
        		PrintWriter writer = null;
        		try {
        			writer = new PrintWriter(
        				new BufferedWriter(
        					new FileWriter("literal.bpel",
        						false)));
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		} 

        		writer.print(toLiteral);
        		
        		writer.close();
        		AeLiteralDef literalDef = new AeLiteralDef();
		        literalDef.setLocationId(id);
		        id++;
		        String literalPath = fromPath + "/literal";
		        literalDef.setLocationPath(literalPath);
		        literalDef.setParentXmlDef(fromDef);
		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        factory.setNamespaceAware(true);
		        factory.setValidating(false);
 	            // Create the document builder and parse the filename
		        DocumentBuilder builder;
		        
		        Element  element=null;
		        try {
		        	builder = factory.newDocumentBuilder();
					Document dom = builder.parse("literal.bpel");
					element = dom.getDocumentElement();
		    	} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		    	} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		    	} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	literalDef.addChildNode(element.getFirstChild());
			    fromDef.setLiteralDef(literalDef);
        	}else if(cursor.toFirstChild())
        	{
        		//qui ci aspettiamo che sia una strategy from-literal o from-expression
        		if(cursor.getName().getLocalPart().equals("literal"))
        		{
        			//qui ci aspettiamo che sia una strategy from-literal
        			aStrategyKeyFrom = new AeSpecStrategyKey("from-literal");
	        		String toLiteral = "<literal>"+cursor.getTextValue()+"</literal>";
	        		PrintWriter writer = null;
	        		try {
	        			writer = new PrintWriter(
	        				new BufferedWriter(
	        					new FileWriter("literal.bpel",
	        						false)));
	        		} catch (IOException e) {
	        			// TODO Auto-generated catch block
	        			e.printStackTrace();
	        		} 
	        		writer.print(toLiteral);
	        		writer.close();
	        		AeLiteralDef literalDef = new AeLiteralDef();
			        literalDef.setLocationId(id);
			        id++;
			        String literalPath = fromPath + "/literal";
			        literalDef.setLocationPath(literalPath);
			        literalDef.setParentXmlDef(fromDef);
			        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			        factory.setNamespaceAware(true);
			        factory.setValidating(false);
     	            // Create the document builder and parse the filename
			        DocumentBuilder builder;
			        
			        Element  element=null;
			        try {
			        	builder = factory.newDocumentBuilder();
						Document dom = builder.parse("literal.bpel");
						element = dom.getDocumentElement();
			    	} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			    	} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			    	} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    	literalDef.addChildNode(element.getFirstChild());
				    fromDef.setLiteralDef(literalDef);
	        	}else
        		{
	        		//qui ci aspettiamo che sia una strategy from-expression
	        		aStrategyKeyFrom = new AeSpecStrategyKey("from-expression");
	        		fromDef.setExpressionLanguage(cursor.getAttributeText(new QName("expressionLanguage")));
	        		fromDef.setExpression(cursor.getTextValue());
	        	}
        		cursor.toParent();
        	}else System.out.println("non ha trovato nessuna specifica della from");
        	fromDef.setStrategyKey(aStrategyKeyFrom);
        	copyDef.setFromDef(fromDef);
        	 
        	//ora mi sposto sulla to
        	cursor.toNextSibling();
        	AeToDef toDef = new AeToDef();
	        toDef.setLocationId(id);
	        id++;
	        String toPath = copyPath + "/to";
	        toDef.setLocationPath(toPath);
	        AeSpecStrategyKey aStrategyKeyTo = null;
	        if(cursor.getAttributeText(new QName("partnerLink")) != null)
	        {
	        	//qui ci aspettiamo che sia una strategy to-partnerLink
	        	aStrategyKeyTo = new AeSpecStrategyKey("to-partnerLink");
	        	toDef.setPartnerLink(cursor.getAttributeText(new QName("partnerLink")));
	        }else if(cursor.getAttributeText(new QName("property")) != null)
	        {
	        	//qui ci aspettiamo che sia una strategy to-property-.....
        		String varName = cursor.getAttributeText(new QName("variable"));
        		AeVariableDef variabile = parent.getProcess().getVariable(parent.getProcess().getLocationPath(), varName).getDefinition();
        		toDef.setVariable(varName);
        		toDef.setProperty(new QName(cursor.getAttributeText(new QName("property"))));
        		if(variabile.getMessageType()!=null)
        		{
        			//qui ci aspettiamo che sia una strategy to-property-message
        			aStrategyKeyTo = new AeSpecStrategyKey("to-property-message");
        		}else if(variabile.getElement()!=null)
        		{
        			//qui ci aspettiamo che sia una strategy to-property-element
        			aStrategyKeyTo = new AeSpecStrategyKey("to-property-element");
        		}else if(variabile.getType()!=null)
        		{
        			//qui ci aspettiamo che sia una strategy to-property-type
        			aStrategyKeyTo = new AeSpecStrategyKey("to-property-type");
        		}
        	}else if(cursor.getAttributeText(new QName("variable")) != null)
        	{
        		//qui ci aspettiamo che sia una strategy to-variable-.....
        		String varName = cursor.getAttributeText(new QName("variable"));
        		
        		AeVariableDef variabile = parent.getProcess().getVariable(parent.getProcess().getLocationPath(), varName).getDefinition();
        		toDef.setVariable(varName);
        		if(variabile.getMessageType()!=null)
        		{
        			//qui ci aspettiamo che sia una strategy to-variable-message-...
        			if(cursor.getAttributeText(new QName("part")) != null)
        			{
        				//qui ci aspettiamo che sia una strategy to-variable-message-part-..
        				toDef.setPart(cursor.getAttributeText(new QName("part")));
        				if(cursor.getAttributeText(new QName("query")) != null)
        				{
        					//qui ci aspettiamo che sia una strategy to-variable-message-part-query
        					AeQueryDef queryDef = new AeQueryDef();
        					queryDef.setQuery(cursor.getAttributeText(new QName("query")));
        					queryDef.setQueryLanguage(null);
        					queryDef.setLocationId(id);
        					id++;
        					queryDef.setParentXmlDef(toDef);
        					toDef.setQueryDef(queryDef);
        					aStrategyKeyTo = new AeSpecStrategyKey("to-variable-message-part-query");
        				}else if(cursor.toFirstChild())
        				{
        					//qui ci aspettiamo che sia una strategy to-variable-message-part-query
        					AeQueryDef queryDef = new AeQueryDef();
        					queryDef.setQuery(cursor.getTextValue());
        					queryDef.setQueryLanguage(null);
        					queryDef.setLocationId(id);
        					id++;
        					queryDef.setParentXmlDef(toDef);
        					toDef.setQueryDef(queryDef);
        					aStrategyKeyTo = new AeSpecStrategyKey("to-variable-message-part-query");
        					cursor.toParent();
        				}else
        				{
        					//qui ci aspettiamo che sia una strategy to-variable-message-part
        					toDef.setPart(cursor.getAttributeText(new QName("part")));
        					aStrategyKeyTo = new AeSpecStrategyKey("to-variable-message-part");
        				}
        				
        			}else
        			{
        				//qui ci aspettiamo che sia una strategy to-variable-message
        				aStrategyKeyTo = new AeSpecStrategyKey("to-variable-message");
        			}
        			
        		}else if(variabile.getElement()!=null)
        		{
        			//qui ci aspettiamo che sia una strategy to-variable-element
        			
        			if(cursor.getAttributeText(new QName("query"))!=null)
    				{
    					//qui ci aspettiamo che sia una strategy to-variable-element-query
    					AeQueryDef queryDef = new AeQueryDef();
    					queryDef.setQuery(cursor.getAttributeText(new QName("query")));
    					queryDef.setQueryLanguage(null);
    					queryDef.setLocationId(id);
    					id++;
    					queryDef.setParentXmlDef(toDef);
    					toDef.setQueryDef(queryDef);
    					aStrategyKeyTo = new AeSpecStrategyKey("to-variable-element-query");
    				}else if(cursor.toFirstChild())
    				{
    					//qui ci aspettiamo che sia una strategy to-variable-element-query
    					AeQueryDef queryDef = new AeQueryDef();
    					queryDef.setQuery(cursor.getTextValue());
    					queryDef.setQueryLanguage(null);
    					queryDef.setLocationId(id);
    					id++;
    					queryDef.setParentXmlDef(toDef);
    					toDef.setQueryDef(queryDef);
    					aStrategyKeyTo = new AeSpecStrategyKey("to-variable-element-query");
    					cursor.toParent();
    				}else
    				{
    					//qui ci aspettiamo che sia una strategy to-variable-element
    					aStrategyKeyTo = new AeSpecStrategyKey("to-variable-element");
    				}
        		}else if(variabile.getType()!=null)
        		{
        			//qui ci aspettiamo che sia una strategy to-variable-type-..
        			if(cursor.getAttributeText(new QName("query"))!=null)
    				{
    					//qui ci aspettiamo che sia una strategy to-variable-type-query
    					AeQueryDef queryDef = new AeQueryDef();
    					queryDef.setQuery(cursor.getAttributeText(new QName("query")));
    					queryDef.setQueryLanguage(null);
    					queryDef.setLocationId(id);
    					id++;
    					queryDef.setParentXmlDef(toDef);
    					toDef.setQueryDef(queryDef);
    					aStrategyKeyTo = new AeSpecStrategyKey("to-variable-type-query");
    				}else if(cursor.toFirstChild())
    				{
    					//qui ci aspettiamo che sia una strategy to-variable-message-part-query
    					AeQueryDef queryDef = new AeQueryDef();
    					queryDef.setQuery(cursor.getTextValue());
    					queryDef.setQueryLanguage(null);
    					queryDef.setLocationId(id);
    					id++;
    					queryDef.setParentXmlDef(toDef);
    					toDef.setQueryDef(queryDef);
    					aStrategyKeyTo = new AeSpecStrategyKey("to-variable-type-query");
    					cursor.toParent();
    				}else
    				{
    					aStrategyKeyTo = new AeSpecStrategyKey("to-variable-type");
    				}
        		
        		}
        	}
	        toDef.setStrategyKey(aStrategyKeyTo);
	        toDef.setParentXmlDef(copyDef);
	        copyDef.setToDef(toDef);
	        assignDef.addCopyDef(copyDef);
	        //qui dalla fromDef devo creare la implementazione della from in base alla strategy scelta
	        AeFromBase fromImpl = null;
	        if(fromDef.getStrategyKey().getStrategyName().equals("from-variable-type"))
	        {
	        	fromImpl= new AeFromVariableType(fromDef);
	        }else if(fromDef.getStrategyKey().getStrategyName().equals("from-variable-type-query"))
	        {
	        	fromImpl= new AeFromVariableTypeWithQuery(fromDef);
	        }else if(fromDef.getStrategyKey().getStrategyName().equals("from-variable-message-part-query"))
	        {
	        	fromImpl= new AeFromVariableMessagePartWithQuery(fromDef);
	        }else if(fromDef.getStrategyKey().getStrategyName().equals("from-variable-message-part"))
	        {
	        	fromImpl= new AeFromVariableMessagePart(fromDef);
	        }else if(fromDef.getStrategyKey().getStrategyName().equals("from-variable-message"))
	        {
	        	fromImpl= new AeFromVariableMessage(fromDef);
	        }else if(fromDef.getStrategyKey().getStrategyName().equals("from-variable-element-query"))
	        {
	        	fromImpl= new AeFromVariableElementWithQuery(fromDef);
	        }else if(fromDef.getStrategyKey().getStrategyName().equals("from-variable-element"))
	        {
	        	fromImpl= new AeFromVariableElement(fromDef);
	        }else if(fromDef.getStrategyKey().getStrategyName().equals("from-property-type"))
	        {
	        	fromImpl= new AeFromPropertyType(fromDef);
	        }else if(fromDef.getStrategyKey().getStrategyName().equals("from-property-message"))
	        {
	        	fromImpl= new AeFromPropertyMessage(fromDef);
	        }else if(fromDef.getStrategyKey().getStrategyName().equals("from-property-element"))
	        {
	        	fromImpl= new AeFromPropertyElement(fromDef);
	        }else if(fromDef.getStrategyKey().getStrategyName().equals("from-partnerLink"))
	        {
	        	fromImpl= new AeFromPartnerLink(fromDef);
	        }else if(fromDef.getStrategyKey().getStrategyName().equals("from-literal"))
	        {
	        	fromImpl= new AeFromLiteral(fromDef);
	        }else if(fromDef.getStrategyKey().getStrategyName().equals("from-expression"))
	        {
	        	fromImpl= new AeFromExpression(fromDef);
	        }else System.out.println("errore nella creazione della fromImpl");
	        
	        //qui dalla toDef devo creare la implementazione della to in base alla strategy scelta
	        AeToBase toImpl = null;
	        if(toDef.getStrategyKey().getStrategyName().equals("to-variable-type"))
	        {
	        	toImpl = new  AeToVariableType(toDef);
	        }else if(toDef.getStrategyKey().getStrategyName().equals("to-variable-type-query"))
	        {
	        	toImpl = new  AeToVariableTypeWithQuery(toDef);
	        }else if(toDef.getStrategyKey().getStrategyName().equals("to-variable-message-part-query"))
	        {
	        	toImpl = new  AeToVariableMessagePartWithQuery(toDef);
	        }else if(toDef.getStrategyKey().getStrategyName().equals("to-variable-message-part"))
	        {
	        	toImpl = new  AeToVariableMessagePart(toDef);
	        }else if(toDef.getStrategyKey().getStrategyName().equals("to-variable-message"))
	        {
	        	toImpl = new  AeToVariableMessage(toDef);
	        }else if(toDef.getStrategyKey().getStrategyName().equals("to-variable-element-query"))
	        {
	        	toImpl = new  AeToVariableElementWithQuery(toDef);
	        }else if(toDef.getStrategyKey().getStrategyName().equals("to-variable-element"))
	        {
	        	toImpl = new  AeToVariableElement(toDef);
	        }else if(toDef.getStrategyKey().getStrategyName().equals("to-property-type"))
	        {
	        	toImpl = new  AeToPropertyType(toDef);
	        }else if(toDef.getStrategyKey().getStrategyName().equals("to-property-message"))
	        {
	        	toImpl = new  AeToPropertyMessage(toDef);
	        }else if(toDef.getStrategyKey().getStrategyName().equals("to-property-element"))
	        {
	        	toImpl = new  AeToPropertyElement(toDef);
	        }else if(toDef.getStrategyKey().getStrategyName().equals("to-partnerLink"))
	        {
	        	toImpl = new  AeToPartnerLink(toDef);
	        }else System.out.println("errore nella creazione della toImpl");
	        
	        
        	AeCopyOperation copy = new AeCopyOperation(copyDef, assign.getCopyOperationContext());
        	copy.setFrom((IAeFrom) fromImpl);
	        copy.setTo((IAeTo) toImpl);
        	assign.addCopyOperation((IAeAssignOperation) copy);
        	
        }while(cursorCopy.toNextSibling());
        
        parentDef.getActivityDefsList().add(i+1, assignDef);
        itParent.add(i+1,assign);
    }
	
	private void aggiungiAssignOnMessage(AeOnMessage parent,XmlCursor cursor)
	{
		AeActivityAssignDef assignDef = new AeActivityAssignDef();
		AeOnMessageDef parentDef = (AeOnMessageDef)parent.getDefinition();
        assignDef.setLocationId(id);
        id++;
		
		String nameAssign = cursor.getAttributeText(new QName("name"));
		String nameSpaceURI = cursor.getName().getNamespaceURI();
		String assignPath = parentDef.getLocationPath()+"/assign[@name='"+nameAssign+"']";
		
        assignDef.setLocationPath(assignPath);
        assignDef.setName(nameAssign);
        assignDef.setParentXmlDef(parentDef);
        assignDef.setDefaultNamespace(nameSpaceURI);
        AeActivityAssignImpl assign = new AeActivityAssignImpl(assignDef, (IAeActivityParent) parent);
        
        //mi sposto sulla prima copy... una ci deve essere per forza per come è definita l'assign
        cursor.toFirstChild();
        //faccio una copia del cursore. cursorCopy verrà utilizzato per scorrere le copy
        XmlCursor cursorCopy = cursor.newCursor();
        boolean notFirstCopy = false;
        int countCopy=2;
        do
        {
        	cursor = cursorCopy.newCursor();
        	AeAssignCopyDef copyDef = new AeAssignCopyDef();
	        copyDef.setLocationId(id);
	        id++;
	        copyDef.setParentXmlDef(assignDef);
	        String copyPath = assignPath + "/copy";
	        if(notFirstCopy==false)
	        	notFirstCopy=true;
	        else
	        {
	        	copyPath = copyPath + "["+countCopy+"]";
	        	countCopy++;
	        }
	        copyDef.setLocationPath(copyPath);
	        cursor = cursorCopy.newCursor();
	        
	        AeFromDef fromDef = new AeFromDef();
	        fromDef.setLocationId(id);
	        id++;
	        String fromPath = copyPath + "/from";
	        fromDef.setLocationPath(fromPath);
	        fromDef.setParentXmlDef(copyDef);
	        
        	//ora mi sposto sulla from
        	cursor.toFirstChild();
        	AeSpecStrategyKey aStrategyKeyFrom = null;
        	if(cursor.getAttributeText(new QName("partnerLink")) != null)
        	{
        		//qui ci aspettiamo che sia una strategy from-partnerLink
        		fromDef.setPartnerLink(cursor.getAttributeText(new QName("partnerLink")));
        		fromDef.setEndpointReference(cursor.getAttributeText(new QName("endpointReference")));
        		aStrategyKeyFrom = new AeSpecStrategyKey("from-partnerLink");
        	}else if(cursor.getAttributeText(new QName("property")) != null)
        	{
        		//qui ci aspettiamo che sia una strategy from-property-.....
        		String varName = cursor.getAttributeText(new QName("variable"));
        		AeVariableDef variabile = parent.getProcess().getVariable(parent.getProcess().getLocationPath(), varName).getDefinition();
        		fromDef.setVariable(varName);
        		fromDef.setProperty(new QName(cursor.getAttributeText(new QName("property"))));
        		if(variabile.getMessageType()!=null)
        		{
        			//qui ci aspettiamo che sia una strategy from-property-message
        			aStrategyKeyFrom = new AeSpecStrategyKey("from-property-message");
        		}else if(variabile.getElement()!=null)
        		{
        			//qui ci aspettiamo che sia una strategy from-property-element
        			aStrategyKeyFrom = new AeSpecStrategyKey("from-property-element");
        		}else if(variabile.getType()!=null)
        		{
        			//qui ci aspettiamo che sia una strategy from-property-type
        			aStrategyKeyFrom = new AeSpecStrategyKey("from-property-type");
        		}
        		
        	}else if(cursor.getAttributeText(new QName("variable")) != null)
        	{
        		//qui ci aspettiamo che sia una strategy from-variable-.....
        		String varName = cursor.getAttributeText(new QName("variable"));
        		AeVariableDef variabile = parent.getProcess().getVariable(parent.getProcess().getLocationPath(), varName).getDefinition();
        		fromDef.setVariable(varName);
        		if(variabile.getMessageType()!=null)
        		{
        			//qui ci aspettiamo che sia una strategy from-variable-message-...
        			if(cursor.getAttributeText(new QName("part")) != null)
        			{
        				//qui ci aspettiamo che sia una strategy from-variable-message-part-..
        				fromDef.setPart(cursor.getAttributeText(new QName("part")));
        				if(cursor.getAttributeText(new QName("query")) != null)
        				{
        					//qui ci aspettiamo che sia una strategy from-variable-message-part-query
        					AeQueryDef queryDef = new AeQueryDef();
        					queryDef.setQuery(cursor.getAttributeText(new QName("query")));
        					queryDef.setQueryLanguage(null);
        					queryDef.setLocationId(id);
        					id++;
        					queryDef.setParentXmlDef(fromDef);
        					fromDef.setQueryDef(queryDef);
        					aStrategyKeyFrom = new AeSpecStrategyKey("from-variable-message-part-query");
        				}else if(cursor.toFirstChild())
        				{
        					//qui ci aspettiamo che sia una strategy from-variable-message-part-query
        					AeQueryDef queryDef = new AeQueryDef();
        					queryDef.setQuery(cursor.getTextValue());
        					queryDef.setQueryLanguage(null);
        					queryDef.setLocationId(id);
        					id++;
        					queryDef.setParentXmlDef(fromDef);
        					fromDef.setQueryDef(queryDef);
        					aStrategyKeyFrom = new AeSpecStrategyKey("from-variable-message-part-query");
        					cursor.toParent();
        				}else
        				{
        					//qui ci aspettiamo che sia una strategy from-variable-message-part
        					fromDef.setPart(cursor.getAttributeText(new QName("part")));
        					aStrategyKeyFrom = new AeSpecStrategyKey("from-variable-message-part");
        				}
        				
        			}else
        			{
        				//qui ci aspettiamo che sia una strategy from-variable-message
        				aStrategyKeyFrom = new AeSpecStrategyKey("from-variable-message");
        			}
        			
        		}else if(variabile.getElement()!=null)
        		{
        			//qui ci aspettiamo che sia una strategy from-variable-element
        			
        			if(cursor.getAttributeText(new QName("query"))!=null)
    				{
    					//qui ci aspettiamo che sia una strategy from-variable-element-query
    					AeQueryDef queryDef = new AeQueryDef();
    					queryDef.setQuery(cursor.getAttributeText(new QName("query")));
    					queryDef.setQueryLanguage(null);
    					queryDef.setLocationId(id);
    					id++;
    					queryDef.setParentXmlDef(fromDef);
    					fromDef.setQueryDef(queryDef);
    					aStrategyKeyFrom = new AeSpecStrategyKey("from-variable-element-query");
    				}else if(cursor.toFirstChild())
    				{
    					//qui ci aspettiamo che sia una strategy from-variable-element-query
    					AeQueryDef queryDef = new AeQueryDef();
    					queryDef.setQuery(cursor.getTextValue());
    					queryDef.setQueryLanguage(null);
    					queryDef.setLocationId(id);
    					id++;
    					queryDef.setParentXmlDef(fromDef);
    					fromDef.setQueryDef(queryDef);
    					aStrategyKeyFrom = new AeSpecStrategyKey("from-variable-element-query");
    					cursor.toParent();
    				}else
    				{
    					//qui ci aspettiamo che sia una strategy from-variable-element
    					aStrategyKeyFrom = new AeSpecStrategyKey("from-variable-element");
    				}
        		}else if(variabile.getType()!=null)
        		{
        			//qui ci aspettiamo che sia una strategy from-variable-type-..
        			if(cursor.getAttributeText(new QName("query"))!=null)
    				{
    					//qui ci aspettiamo che sia una strategy from-variable-type-query
    					AeQueryDef queryDef = new AeQueryDef();
    					queryDef.setQuery(cursor.getAttributeText(new QName("query")));
    					queryDef.setQueryLanguage(null);
    					queryDef.setLocationId(id);
    					id++;
    					queryDef.setParentXmlDef(fromDef);
    					fromDef.setQueryDef(queryDef);
    					aStrategyKeyFrom = new AeSpecStrategyKey("from-variable-type-query");
    				}else if(cursor.toFirstChild())
    				{
    					//qui ci aspettiamo che sia una strategy from-variable-message-part-query
    					AeQueryDef queryDef = new AeQueryDef();
    					queryDef.setQuery(cursor.getTextValue());
    					queryDef.setQueryLanguage(null);
    					queryDef.setLocationId(id);
    					id++;
    					queryDef.setParentXmlDef(fromDef);
    					fromDef.setQueryDef(queryDef);
    					aStrategyKeyFrom = new AeSpecStrategyKey("from-variable-type-query");
    					cursor.toParent();
    				}else
    				{
    					aStrategyKeyFrom = new AeSpecStrategyKey("from-variable-type");
    				}
        		}
        		
        	}else if(!(cursor.getTextValue().equals("")))
        	{
        		//qui ci aspettiamo che sia una strategy from-literal
        		aStrategyKeyFrom = new AeSpecStrategyKey("from-literal");

        		String toLiteral = "<literal>" +
        				cursor.getTextValue() +
        				"</literal>";
        		PrintWriter writer = null;
        		try {
        			writer = new PrintWriter(
        				new BufferedWriter(
        					new FileWriter("literal.bpel",
        						false)));
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		} 

        		writer.print(toLiteral);
        		
        		writer.close();
        		AeLiteralDef literalDef = new AeLiteralDef();
		        literalDef.setLocationId(id);
		        id++;
		        String literalPath = fromPath + "/literal";
		        literalDef.setLocationPath(literalPath);
		        literalDef.setParentXmlDef(fromDef);
		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        factory.setNamespaceAware(true);
		        factory.setValidating(false);
 	            // Create the document builder and parse the filename
		        DocumentBuilder builder;
		        
		        Element  element=null;
		        try {
		        	builder = factory.newDocumentBuilder();
					Document dom = builder.parse("literal.bpel");
					element = dom.getDocumentElement();
		    	} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		    	} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		    	} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	literalDef.addChildNode(element.getFirstChild());
			    fromDef.setLiteralDef(literalDef);
        	}else if(cursor.toFirstChild())
        	{
        		//qui ci aspettiamo che sia una strategy from-literal o from-expression
        		if(cursor.getName().getLocalPart().equals("literal"))
        		{
        			//qui ci aspettiamo che sia una strategy from-literal
        			aStrategyKeyFrom = new AeSpecStrategyKey("from-literal");
	        		String toLiteral = "<literal>"+cursor.getTextValue()+"</literal>";
	        		PrintWriter writer = null;
	        		try {
	        			writer = new PrintWriter(
	        				new BufferedWriter(
	        					new FileWriter("literal.bpel",
	        						false)));
	        		} catch (IOException e) {
	        			// TODO Auto-generated catch block
	        			e.printStackTrace();
	        		} 
	        		writer.print(toLiteral);
	        		writer.close();
	        		AeLiteralDef literalDef = new AeLiteralDef();
			        literalDef.setLocationId(id);
			        id++;
			        String literalPath = fromPath + "/literal";
			        literalDef.setLocationPath(literalPath);
			        literalDef.setParentXmlDef(fromDef);
			        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			        factory.setNamespaceAware(true);
			        factory.setValidating(false);
     	            // Create the document builder and parse the filename
			        DocumentBuilder builder;
			        
			        Element  element=null;
			        try {
			        	builder = factory.newDocumentBuilder();
						Document dom = builder.parse("literal.bpel");
						element = dom.getDocumentElement();
			    	} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			    	} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			    	} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    	literalDef.addChildNode(element.getFirstChild());
				    fromDef.setLiteralDef(literalDef);
	        	}else
        		{
	        		//qui ci aspettiamo che sia una strategy from-expression
	        		aStrategyKeyFrom = new AeSpecStrategyKey("from-expression");
	        		fromDef.setExpressionLanguage(cursor.getAttributeText(new QName("expressionLanguage")));
	        		fromDef.setExpression(cursor.getTextValue());
	        	}
        		cursor.toParent();
        	}else System.out.println("non ha trovato nessuna specifica della from");
        	fromDef.setStrategyKey(aStrategyKeyFrom);
        	copyDef.setFromDef(fromDef);
        	 
        	//ora mi sposto sulla to
        	cursor.toNextSibling();
        	AeToDef toDef = new AeToDef();
	        toDef.setLocationId(id);
	        id++;
	        String toPath = copyPath + "/to";
	        toDef.setLocationPath(toPath);
	        AeSpecStrategyKey aStrategyKeyTo = null;
	        if(cursor.getAttributeText(new QName("partnerLink")) != null)
	        {
	        	//qui ci aspettiamo che sia una strategy to-partnerLink
	        	aStrategyKeyTo = new AeSpecStrategyKey("to-partnerLink");
	        	toDef.setPartnerLink(cursor.getAttributeText(new QName("partnerLink")));
	        }else if(cursor.getAttributeText(new QName("property")) != null)
	        {
	        	//qui ci aspettiamo che sia una strategy to-property-.....
        		String varName = cursor.getAttributeText(new QName("variable"));
        		AeVariableDef variabile = parent.getProcess().getVariable(parent.getProcess().getLocationPath(), varName).getDefinition();
        		toDef.setVariable(varName);
        		toDef.setProperty(new QName(cursor.getAttributeText(new QName("property"))));
        		if(variabile.getMessageType()!=null)
        		{
        			//qui ci aspettiamo che sia una strategy to-property-message
        			aStrategyKeyTo = new AeSpecStrategyKey("to-property-message");
        		}else if(variabile.getElement()!=null)
        		{
        			//qui ci aspettiamo che sia una strategy to-property-element
        			aStrategyKeyTo = new AeSpecStrategyKey("to-property-element");
        		}else if(variabile.getType()!=null)
        		{
        			//qui ci aspettiamo che sia una strategy to-property-type
        			aStrategyKeyTo = new AeSpecStrategyKey("to-property-type");
        		}
        	}else if(cursor.getAttributeText(new QName("variable")) != null)
        	{
        		//qui ci aspettiamo che sia una strategy to-variable-.....
        		String varName = cursor.getAttributeText(new QName("variable"));
        		
        		AeVariableDef variabile = parent.getProcess().getVariable(parent.getProcess().getLocationPath(), varName).getDefinition();
        		toDef.setVariable(varName);
        		if(variabile.getMessageType()!=null)
        		{
        			//qui ci aspettiamo che sia una strategy to-variable-message-...
        			if(cursor.getAttributeText(new QName("part")) != null)
        			{
        				//qui ci aspettiamo che sia una strategy to-variable-message-part-..
        				toDef.setPart(cursor.getAttributeText(new QName("part")));
        				if(cursor.getAttributeText(new QName("query")) != null)
        				{
        					//qui ci aspettiamo che sia una strategy to-variable-message-part-query
        					AeQueryDef queryDef = new AeQueryDef();
        					queryDef.setQuery(cursor.getAttributeText(new QName("query")));
        					queryDef.setQueryLanguage(null);
        					queryDef.setLocationId(id);
        					id++;
        					queryDef.setParentXmlDef(toDef);
        					toDef.setQueryDef(queryDef);
        					aStrategyKeyTo = new AeSpecStrategyKey("to-variable-message-part-query");
        				}else if(cursor.toFirstChild())
        				{
        					//qui ci aspettiamo che sia una strategy to-variable-message-part-query
        					AeQueryDef queryDef = new AeQueryDef();
        					queryDef.setQuery(cursor.getTextValue());
        					queryDef.setQueryLanguage(null);
        					queryDef.setLocationId(id);
        					id++;
        					queryDef.setParentXmlDef(toDef);
        					toDef.setQueryDef(queryDef);
        					aStrategyKeyTo = new AeSpecStrategyKey("to-variable-message-part-query");
        					cursor.toParent();
        				}else
        				{
        					//qui ci aspettiamo che sia una strategy to-variable-message-part
        					toDef.setPart(cursor.getAttributeText(new QName("part")));
        					aStrategyKeyTo = new AeSpecStrategyKey("to-variable-message-part");
        				}
        				
        			}else
        			{
        				//qui ci aspettiamo che sia una strategy to-variable-message
        				aStrategyKeyTo = new AeSpecStrategyKey("to-variable-message");
        			}
        			
        		}else if(variabile.getElement()!=null)
        		{
        			//qui ci aspettiamo che sia una strategy to-variable-element
        			
        			if(cursor.getAttributeText(new QName("query"))!=null)
    				{
    					//qui ci aspettiamo che sia una strategy to-variable-element-query
    					AeQueryDef queryDef = new AeQueryDef();
    					queryDef.setQuery(cursor.getAttributeText(new QName("query")));
    					queryDef.setQueryLanguage(null);
    					queryDef.setLocationId(id);
    					id++;
    					queryDef.setParentXmlDef(toDef);
    					toDef.setQueryDef(queryDef);
    					aStrategyKeyTo = new AeSpecStrategyKey("to-variable-element-query");
    				}else if(cursor.toFirstChild())
    				{
    					//qui ci aspettiamo che sia una strategy to-variable-element-query
    					AeQueryDef queryDef = new AeQueryDef();
    					queryDef.setQuery(cursor.getTextValue());
    					queryDef.setQueryLanguage(null);
    					queryDef.setLocationId(id);
    					id++;
    					queryDef.setParentXmlDef(toDef);
    					toDef.setQueryDef(queryDef);
    					aStrategyKeyTo = new AeSpecStrategyKey("to-variable-element-query");
    					cursor.toParent();
    				}else
    				{
    					//qui ci aspettiamo che sia una strategy to-variable-element
    					aStrategyKeyTo = new AeSpecStrategyKey("to-variable-element");
    				}
        		}else if(variabile.getType()!=null)
        		{
        			//qui ci aspettiamo che sia una strategy to-variable-type-..
        			if(cursor.getAttributeText(new QName("query"))!=null)
    				{
    					//qui ci aspettiamo che sia una strategy to-variable-type-query
    					AeQueryDef queryDef = new AeQueryDef();
    					queryDef.setQuery(cursor.getAttributeText(new QName("query")));
    					queryDef.setQueryLanguage(null);
    					queryDef.setLocationId(id);
    					id++;
    					queryDef.setParentXmlDef(toDef);
    					toDef.setQueryDef(queryDef);
    					aStrategyKeyTo = new AeSpecStrategyKey("to-variable-type-query");
    				}else if(cursor.toFirstChild())
    				{
    					//qui ci aspettiamo che sia una strategy to-variable-message-part-query
    					AeQueryDef queryDef = new AeQueryDef();
    					queryDef.setQuery(cursor.getTextValue());
    					queryDef.setQueryLanguage(null);
    					queryDef.setLocationId(id);
    					id++;
    					queryDef.setParentXmlDef(toDef);
    					toDef.setQueryDef(queryDef);
    					aStrategyKeyTo = new AeSpecStrategyKey("to-variable-type-query");
    					cursor.toParent();
    				}else
    				{
    					aStrategyKeyTo = new AeSpecStrategyKey("to-variable-type");
    				}
        		
        		}
        	}
	        toDef.setStrategyKey(aStrategyKeyTo);
	        toDef.setParentXmlDef(copyDef);
	        copyDef.setToDef(toDef);
	        assignDef.addCopyDef(copyDef);
	        //qui dalla fromDef devo creare la implementazione della from in base alla strategy scelta
	        AeFromBase fromImpl = null;
	        if(fromDef.getStrategyKey().getStrategyName().equals("from-variable-type"))
	        {
	        	fromImpl= new AeFromVariableType(fromDef);
	        }else if(fromDef.getStrategyKey().getStrategyName().equals("from-variable-type-query"))
	        {
	        	fromImpl= new AeFromVariableTypeWithQuery(fromDef);
	        }else if(fromDef.getStrategyKey().getStrategyName().equals("from-variable-message-part-query"))
	        {
	        	fromImpl= new AeFromVariableMessagePartWithQuery(fromDef);
	        }else if(fromDef.getStrategyKey().getStrategyName().equals("from-variable-message-part"))
	        {
	        	fromImpl= new AeFromVariableMessagePart(fromDef);
	        }else if(fromDef.getStrategyKey().getStrategyName().equals("from-variable-message"))
	        {
	        	fromImpl= new AeFromVariableMessage(fromDef);
	        }else if(fromDef.getStrategyKey().getStrategyName().equals("from-variable-element-query"))
	        {
	        	fromImpl= new AeFromVariableElementWithQuery(fromDef);
	        }else if(fromDef.getStrategyKey().getStrategyName().equals("from-variable-element"))
	        {
	        	fromImpl= new AeFromVariableElement(fromDef);
	        }else if(fromDef.getStrategyKey().getStrategyName().equals("from-property-type"))
	        {
	        	fromImpl= new AeFromPropertyType(fromDef);
	        }else if(fromDef.getStrategyKey().getStrategyName().equals("from-property-message"))
	        {
	        	fromImpl= new AeFromPropertyMessage(fromDef);
	        }else if(fromDef.getStrategyKey().getStrategyName().equals("from-property-element"))
	        {
	        	fromImpl= new AeFromPropertyElement(fromDef);
	        }else if(fromDef.getStrategyKey().getStrategyName().equals("from-partnerLink"))
	        {
	        	fromImpl= new AeFromPartnerLink(fromDef);
	        }else if(fromDef.getStrategyKey().getStrategyName().equals("from-literal"))
	        {
	        	fromImpl= new AeFromLiteral(fromDef);
	        }else if(fromDef.getStrategyKey().getStrategyName().equals("from-expression"))
	        {
	        	fromImpl= new AeFromExpression(fromDef);
	        }else System.out.println("errore nella creazione della fromImpl");
	        
	        //qui dalla toDef devo creare la implementazione della to in base alla strategy scelta
	        AeToBase toImpl = null;
	        if(toDef.getStrategyKey().getStrategyName().equals("to-variable-type"))
	        {
	        	toImpl = new  AeToVariableType(toDef);
	        }else if(toDef.getStrategyKey().getStrategyName().equals("to-variable-type-query"))
	        {
	        	toImpl = new  AeToVariableTypeWithQuery(toDef);
	        }else if(toDef.getStrategyKey().getStrategyName().equals("to-variable-message-part-query"))
	        {
	        	toImpl = new  AeToVariableMessagePartWithQuery(toDef);
	        }else if(toDef.getStrategyKey().getStrategyName().equals("to-variable-message-part"))
	        {
	        	toImpl = new  AeToVariableMessagePart(toDef);
	        }else if(toDef.getStrategyKey().getStrategyName().equals("to-variable-message"))
	        {
	        	toImpl = new  AeToVariableMessage(toDef);
	        }else if(toDef.getStrategyKey().getStrategyName().equals("to-variable-element-query"))
	        {
	        	toImpl = new  AeToVariableElementWithQuery(toDef);
	        }else if(toDef.getStrategyKey().getStrategyName().equals("to-variable-element"))
	        {
	        	toImpl = new  AeToVariableElement(toDef);
	        }else if(toDef.getStrategyKey().getStrategyName().equals("to-property-type"))
	        {
	        	toImpl = new  AeToPropertyType(toDef);
	        }else if(toDef.getStrategyKey().getStrategyName().equals("to-property-message"))
	        {
	        	toImpl = new  AeToPropertyMessage(toDef);
	        }else if(toDef.getStrategyKey().getStrategyName().equals("to-property-element"))
	        {
	        	toImpl = new  AeToPropertyElement(toDef);
	        }else if(toDef.getStrategyKey().getStrategyName().equals("to-partnerLink"))
	        {
	        	toImpl = new  AeToPartnerLink(toDef);
	        }else System.out.println("errore nella creazione della toImpl");
	        
	        
        	AeCopyOperation copy = new AeCopyOperation(copyDef, assign.getCopyOperationContext());
        	copy.setFrom((IAeFrom) fromImpl);
	        copy.setTo((IAeTo) toImpl);
        	assign.addCopyOperation((IAeAssignOperation) copy);
        	
        }while(cursorCopy.toNextSibling());
        
        parentDef.setActivityDef(assignDef);
	    parent.setActivity(assign);
		
	}
	
	private void aggiungiReceive(String at_target,String at_nuova,AeActivityImpl aeAct)
	{
		
		AeActivityImpl parent = (AeActivityImpl) aeAct;
		
		//questo solo in caso in cui il padre sia una sequence
		List itParent = ((AeActivitySequenceImpl)parent).getMActivities();
		if((at_target.equals(""))||(at_target==null))
		{
			aggiungiReceivePosizione(itParent,parent,at_nuova,-1);
		}else
		for(int i=0;i<itParent.size();i++)
		{
			if((((AeActivityImpl)itParent.get(i)).getLocationPath().equals(at_target)))
			{
				aggiungiReceivePosizione(itParent,parent,at_nuova,i);
			}
		}
	}
	private void aggiungiReceivePosizione(List itParent,AeActivityImpl parent,String at_nuova,int i)
	{
		
		XmlObject xmlObject = null;
	       
		try {
			xmlObject = XmlObject.Factory.parse(at_nuova);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		XmlCursor cursor = xmlObject.newCursor();
		cursor.toNextToken();
		aggiungiReceivePosizioneCursor(itParent,parent,i,cursor);
	}
	
	private void aggiungiReceivePosizioneCursor(List itParent,AeActivityImpl parent,int i,XmlCursor cursor)
	{
		
		AeActivityReceiveDef receiveDef = new AeActivityReceiveDef();
		
		AeActivitySequenceDef parentDef = (AeActivitySequenceDef)parent.getDefinition();
		
		AeActivityReceiveImpl receive = new AeActivityReceiveImpl(receiveDef, (IAeActivityParent) parent);
		receiveDef.setLocationId(id);
		receive.setLocationId(id);
		id++;
		String nomeReceive = cursor.getAttributeText(new QName("name"));
		String receivePath = parentDef.getLocationPath()+"/receive[@name='"+nomeReceive+"']";
		if(cursor.getAttributeText(new QName("messageExchange")) != null)
		{
			receiveDef.setMessageExchange(cursor.getAttributeText(new QName("messageExchange")));
		}else
		{
			receiveDef.setMessageExchange("");	
		}
		receive.setLocationPath(receivePath);
		receiveDef.setLocationPath(receivePath);
	    receiveDef.setName(nomeReceive);
	    receiveDef.setOperation(cursor.getAttributeText(new QName("operation")));
	    receiveDef.setParentXmlDef(parentDef);
	    
	    if(cursor.getAttributeText(new QName("createInstance")).equals("yes"))
	    {
	    	receiveDef.setCreateInstance(true);   
	    }else 
	    {
	    	receiveDef.setCreateInstance(false);
	    }
	    receiveDef.setPartnerLink(cursor.getAttributeText(new QName("partnerLink")));
	    if(cursor.getAttributeText(new QName("portType")) != null)
		{
	    	String QNamePT[] = cursor.getAttributeText(new QName("portType")).split(":");
	    	String prefixPT = QNamePT[0];
	    	String portType = QNamePT[1];
	    	receiveDef.setPortType(new QName(parentDef.getNamespace(prefixPT),portType));
		}
	    if(cursor.getAttributeText(new QName("variable")) != null)
	    {
	    	AeVariableDef variabile = parent.getProcess().getVariable(parent.getProcess().getLocationPath(), cursor.getAttributeText(new QName("variable"))).getDefinition();
	    	receiveDef.setConsumerMessagePartsMap(variabile.getMessageParts());
	    	receiveDef.setVariable(cursor.getAttributeText(new QName("variable")));
	    	if(variabile.getMessageType()!=null)
    		{
	    		receiveDef.setMessageDataConsumerStrategy("message-variable");
    		}else if(variabile.getElement()!=null)
    		{
    			receiveDef.setMessageDataConsumerStrategy("element-variable");
    		}						    	
	    }
	    receiveDef.setProducerMessagePartsMap(null);
	    if(cursor.toFirstChild())
	    	do
	    	{
	    		if(cursor.getName().getLocalPart().equals("fromParts"))
	    		{
	    			
	    			AeFromPartsDef fromPartsDef = new AeFromPartsDef();
	    			fromPartsDef.setLocationId(id);
	    			id++;
	    			String fromPartsDefPath = receivePath + "/fromParts";
	    			fromPartsDef.setLocationPath(fromPartsDefPath);
	    			fromPartsDef.setParentXmlDef(receiveDef);
	    			boolean notFirstPart = false;
	    			cursor.toFirstChild();
	    			int countPart=2;
	    			do
	    			{
	    				AeFromPartDef fromPartDef = new AeFromPartDef();
	    				fromPartDef.setToVariable(cursor.getAttributeText(new QName("toVariable")));
	    				fromPartDef.setLocationId(id);
	    				id++;
	    				String fromPartDefPath = fromPartsDefPath + "/fromPart";
				        if(notFirstPart==false)
				        	notFirstPart=true;
				        else
				        {
				        	fromPartDefPath = fromPartDefPath + "["+countPart+"]";
				        	countPart++;
				        }
	    				
	    				fromPartDef.setLocationPath(fromPartDefPath);
	    				fromPartDef.setParentXmlDef(fromPartsDef);
	    				fromPartDef.setPart(cursor.getAttributeText(new QName("part")));
	    				fromPartsDef.addFromPartDef(fromPartDef);
	    				
	    			}while(cursor.toNextSibling());
	    			receiveDef.setMessageDataConsumerStrategy("from-parts");
	    			receiveDef.setFromPartsDef(fromPartsDef);
	    			cursor.toParent();
		    	}else if(cursor.getName().getLocalPart().equals("correlations"))
		    	{
		    		AeCorrelationsDef correlationsDef = new AeCorrelationsDef();
		    		correlationsDef.setLocationId(id);
		    		id++;
		    		correlationsDef.setParentXmlDef(receiveDef);
		    		String correlationsDefPath = receivePath + "/correlations";
		    		correlationsDef.setLocationPath(correlationsDefPath);
		    		boolean notFirstCorrelation = false;
	    			cursor.toFirstChild();
	    			int countCorrelation=2;
	    			do
	    			{
	    				AeCorrelationDef correlationDef = new AeCorrelationDef();
	    				correlationDef.setCorrelationSetName(cursor.getAttributeText(new QName("set")));
	    				correlationDef.setLocationId(id);
	    				
	    				id++;
	    				if(cursor.getAttributeText(new QName("initiate")) != null)
	    					correlationDef.setInitiate(cursor.getAttributeText(new QName("initiate")));
	    				else correlationDef.setInitiate("no");
	    				correlationDef.setParentXmlDef(correlationsDef);
	    				
	    				String correlationDefPath = correlationsDefPath + "/correlation";
				        if(notFirstCorrelation==false)
				        	notFirstCorrelation=true;
				        else
				        {
				        	correlationDefPath = correlationDefPath + "["+countCorrelation+"]";
				        	countCorrelation++;
				        }
				        correlationDef.shouldAlreadyBeInitiated();
	    				correlationDef.setLocationPath(correlationDefPath);
	    				correlationsDef.addCorrelationDef(correlationDef);
	    				
	    			}while(cursor.toNextSibling());
	    			receiveDef.setCorrelationsDef(correlationsDef);
	    			
	    			AeIMACorrelations correlations = new AeIMACorrelations(correlationsDef, receive);
	    			correlations.setFilter(AeIMACorrelations.INITIALIZED);
	    			receive.setRequestCorrelations(correlations);
	    			cursor.toParent();
	    			
		    	}
	    	
	    	
	    	}while(cursor.toNextSibling());
	    cursor.toParent();
	    
	   
	     
	    IAeMessageDataConsumer messageDataConsumer = createMessageConsumer(receiveDef);
	    receive.setMessageDataConsumer(messageDataConsumer);
	    IAeMessageValidator MESSAGE_VALIDATOR = new AeBPELMessageDataValidator(false);
	    
	   	 
	    receive.setMessageValidator(MESSAGE_VALIDATOR);
	    parent.getProcess().addBpelObject(receivePath, receive);
	    ((AeProcessDef)parent.getProcess().getDefinition()).addMappings(receive);
	    
	    parentDef.getActivityDefsList().add(i+1, receiveDef);
	    itParent.add(i+1,receive);
	 
	}
	
	
	private void aggiungiReceiveOnMessage(AeOnMessage parent,XmlCursor cursor)
	{
		
		AeActivityReceiveDef receiveDef = new AeActivityReceiveDef();
		AeActivityReceiveImpl receive = new AeActivityReceiveImpl(receiveDef, (IAeActivityParent) parent);
		
		AeOnMessageDef parentDef = (AeOnMessageDef)parent.getDefinition();
		
		receiveDef.setLocationId(id);
		receive.setLocationId(id);
		id++;
		String nomeReceive = cursor.getAttributeText(new QName("name"));
		String receivePath = parentDef.getLocationPath()+"/receive[@name='"+nomeReceive+"']";
		if(cursor.getAttributeText(new QName("messageExchange")) != null)
		{
			receiveDef.setMessageExchange(cursor.getAttributeText(new QName("messageExchange")));
		}else
		{
			receiveDef.setMessageExchange("");	
		}
		receive.setLocationPath(receivePath);
		receiveDef.setLocationPath(receivePath);
	    receiveDef.setName(nomeReceive);
	    receiveDef.setOperation(cursor.getAttributeText(new QName("operation")));
	    receiveDef.setParentXmlDef(parentDef);
	    
	    if(cursor.getAttributeText(new QName("createInstance")).equals("yes"))
	    {
	    	receiveDef.setCreateInstance(true);   
	    }else 
	    {
	    	receiveDef.setCreateInstance(false);
	    }
	    receiveDef.setPartnerLink(cursor.getAttributeText(new QName("partnerLink")));
	    if(cursor.getAttributeText(new QName("portType")) != null)
		{
	    	String QNamePT[] = cursor.getAttributeText(new QName("portType")).split(":");
	    	String prefixPT = QNamePT[0];
	    	String portType = QNamePT[1];
	    	receiveDef.setPortType(new QName(parentDef.getNamespace(prefixPT),portType));
		}
	    if(cursor.getAttributeText(new QName("variable")) != null)
	    {
	    	AeVariableDef variabile = parent.getProcess().getVariable(parent.getProcess().getLocationPath(), cursor.getAttributeText(new QName("variable"))).getDefinition();
	    	receiveDef.setConsumerMessagePartsMap(variabile.getMessageParts());
	    	receiveDef.setVariable(cursor.getAttributeText(new QName("variable")));
	    	if(variabile.getMessageType()!=null)
    		{
	    		receiveDef.setMessageDataConsumerStrategy("message-variable");
    		}else if(variabile.getElement()!=null)
    		{
    			receiveDef.setMessageDataConsumerStrategy("element-variable");
    		}						    	
	    }
	    receiveDef.setProducerMessagePartsMap(null);
	    if(cursor.toFirstChild())
	    	do
	    	{
	    		if(cursor.getName().getLocalPart().equals("fromParts"))
	    		{
	    			
	    			AeFromPartsDef fromPartsDef = new AeFromPartsDef();
	    			fromPartsDef.setLocationId(id);
	    			id++;
	    			String fromPartsDefPath = receivePath + "/fromParts";
	    			fromPartsDef.setLocationPath(fromPartsDefPath);
	    			fromPartsDef.setParentXmlDef(receiveDef);
	    			boolean notFirstPart = false;
	    			cursor.toFirstChild();
	    			int countPart=2;
	    			do
	    			{
	    				AeFromPartDef fromPartDef = new AeFromPartDef();
	    				fromPartDef.setToVariable(cursor.getAttributeText(new QName("toVariable")));
	    				fromPartDef.setLocationId(id);
	    				id++;
	    				String fromPartDefPath = fromPartsDefPath + "/fromPart";
				        if(notFirstPart==false)
				        	notFirstPart=true;
				        else
				        {
				        	fromPartDefPath = fromPartDefPath + "["+countPart+"]";
				        	countPart++;
				        }
	    				
	    				fromPartDef.setLocationPath(fromPartDefPath);
	    				fromPartDef.setParentXmlDef(fromPartsDef);
	    				fromPartDef.setPart(cursor.getAttributeText(new QName("part")));
	    				fromPartsDef.addFromPartDef(fromPartDef);
	    				
	    			}while(cursor.toNextSibling());
	    			receiveDef.setMessageDataConsumerStrategy("from-parts");
	    			receiveDef.setFromPartsDef(fromPartsDef);
	    			cursor.toParent();
		    	}else if(cursor.getName().getLocalPart().equals("correlations"))
		    	{
		    		AeCorrelationsDef correlationsDef = new AeCorrelationsDef();
		    		correlationsDef.setLocationId(id);
		    		id++;
		    		correlationsDef.setParentXmlDef(receiveDef);
		    		String correlationsDefPath = receivePath + "/correlations";
		    		correlationsDef.setLocationPath(correlationsDefPath);
		    		boolean notFirstCorrelation = false;
	    			cursor.toFirstChild();
	    			int countCorrelation=2;
	    			do
	    			{
	    				AeCorrelationDef correlationDef = new AeCorrelationDef();
	    				correlationDef.setCorrelationSetName(cursor.getAttributeText(new QName("set")));
	    				correlationDef.setLocationId(id);
	    				
	    				id++;
	    				if(cursor.getAttributeText(new QName("initiate")) != null)
	    					correlationDef.setInitiate(cursor.getAttributeText(new QName("initiate")));
	    				else correlationDef.setInitiate("no");
	    				correlationDef.setParentXmlDef(correlationsDef);
	    				
	    				String correlationDefPath = correlationsDefPath + "/correlation";
				        if(notFirstCorrelation==false)
				        	notFirstCorrelation=true;
				        else
				        {
				        	correlationDefPath = correlationDefPath + "["+countCorrelation+"]";
				        	countCorrelation++;
				        }
				        correlationDef.shouldAlreadyBeInitiated();
	    				correlationDef.setLocationPath(correlationDefPath);
	    				correlationsDef.addCorrelationDef(correlationDef);
	    				
	    			}while(cursor.toNextSibling());
	    			receiveDef.setCorrelationsDef(correlationsDef);
	    			
	    			AeIMACorrelations correlations = new AeIMACorrelations(correlationsDef, receive);
	    			correlations.setFilter(AeIMACorrelations.INITIALIZED);
	    			receive.setRequestCorrelations(correlations);
	    			cursor.toParent();
	    			
		    	}
	    	
	    	
	    	}while(cursor.toNextSibling());
	    cursor.toParent();
	    
	   
	     
	    IAeMessageDataConsumer messageDataConsumer = createMessageConsumer(receiveDef);
	    receive.setMessageDataConsumer(messageDataConsumer);
	    IAeMessageValidator MESSAGE_VALIDATOR = new AeBPELMessageDataValidator(false);
	    
	   	 
	    receive.setMessageValidator(MESSAGE_VALIDATOR);
	    parent.getProcess().addBpelObject(receivePath, receive);
	    ((AeProcessDef)parent.getProcess().getDefinition()).addMappings(receive);
	    parentDef.setActivityDef(receiveDef);
	    parent.setActivity(receive);
	 
	}
	
	private void aggiungiInvoke(String at_target,String at_nuova,AeActivityImpl aeAct)
	{
		AeActivitySequenceImpl parent = (AeActivitySequenceImpl) aeAct;
		
		//questo solo in caso in cui il padre sia una sequence
		List itParent = ((AeActivitySequenceImpl)parent).getMActivities();
		if((at_target.equals(""))||(at_target==null))
		{
			aggiungiInvokePosizione(itParent,parent,at_nuova,-1);
		}
		else
		for(int i=0;i<itParent.size();i++)
		{
			if((((AeActivityImpl)itParent.get(i)).getLocationPath().equals(at_target)))
			{ 
				aggiungiInvokePosizione(itParent,parent,at_nuova,i);
			}
				
		}
	}
	private void aggiungiInvokePosizione(List itParent,AeActivityImpl parent,String at_nuova,int i)
	{
		
	
		XmlObject xmlObject = null;
	       
		try {
			xmlObject = XmlObject.Factory.parse(at_nuova);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
			
		XmlCursor cursor = xmlObject.newCursor();
		cursor.toNextToken();	
		aggiungiInvokePosizioneCursor(itParent,parent,i,cursor);
		
	}
	private void aggiungiInvokePosizioneCursor(List itParent,AeActivityImpl parent,int i,XmlCursor cursor)
	{
		AeActivityInvokeDef invokeDef = new AeActivityInvokeDef();
		AeActivitySequenceDef parentDef = (AeActivitySequenceDef)parent.getDefinition();
		AeActivityInvokeImpl invoke = new AeActivityInvokeImpl(invokeDef, (IAeActivityParent) parent);
			
		invokeDef.setLocationId(id);
		invoke.setLocationId(id);
		id++;
		String nomeInvoke = cursor.getAttributeText(new QName("name"));
		String invokePath = parentDef.getLocationPath()+"/invoke[@name='"+nomeInvoke+"']";
		invokeDef.setLocationPath(invokePath);
		invoke.setLocationPath(invokePath);
		//System.out.println(invokePath);
		//System.out.println(id);
	    invokeDef.setName(nomeInvoke);
	    invokeDef.setOperation(cursor.getAttributeText(new QName("operation")));
	    invokeDef.setParentXmlDef(parentDef);
	    invokeDef.setPartnerLink(cursor.getAttributeText(new QName("partnerLink")));
	    if(cursor.getAttributeText(new QName("portType")) != null)
		{
	    	String QNamePT[] = cursor.getAttributeText(new QName("portType")).split(":");
	    	String prefixPT = QNamePT[0];
	    	String portType = QNamePT[1];
	    	invokeDef.setPortType(new QName(parent.getProcess().getDefinition().getNamespace(prefixPT),portType));
		}
	    //nota con inputVariable va associato il producer
	    if(cursor.getAttributeText(new QName("inputVariable")) != null)
	    {
	    	AeVariableDef variabile = parent.getProcess().getVariable(parent.getProcess().getLocationPath(), cursor.getAttributeText(new QName("inputVariable"))).getDefinition();
	    	invokeDef.setProducerMessagePartsMap(variabile.getMessageParts());
	    	invokeDef.setInputVariable(cursor.getAttributeText(new QName("inputVariable")));
	    	if(variabile.getMessageType()!=null)
    		{
	    		invokeDef.setMessageDataProducerStrategy("message-variable");
    		}else if(variabile.getElement()!=null)
    		{
    			invokeDef.setMessageDataProducerStrategy("element-variable");
    		}						    	
	    } 
	    //nota con outputVariable va associato il consumer
		if(cursor.getAttributeText(new QName("outputVariable")) != null)
	    {
	    	AeVariableDef variabile = parent.getProcess().getVariable(parent.getProcess().getLocationPath(), cursor.getAttributeText(new QName("outputVariable"))).getDefinition();
	    	invokeDef.setConsumerMessagePartsMap(variabile.getMessageParts());
	    	invokeDef.setOutputVariable(cursor.getAttributeText(new QName("outputVariable")));
	    	if(variabile.getMessageType()!=null)
    		{
	    		invokeDef.setMessageDataConsumerStrategy("message-variable");
    		}else if(variabile.getElement()!=null)
    		{
    			invokeDef.setMessageDataConsumerStrategy("element-variable");
    		}						    	
	    }
	    
	    if(cursor.toFirstChild())
	    	do
	    	{
	    		if(cursor.getName().getLocalPart().equals("toParts"))
	    		{
	    			
	    			AeToPartsDef toPartsDef = new AeToPartsDef();
	    			toPartsDef.setLocationId(id);
	    			id++;
	    			String toPartsDefPath = invokePath + "/toParts";
	    			toPartsDef.setLocationPath(toPartsDefPath);
	    			toPartsDef.setParentXmlDef(invokeDef);
	    			boolean notFirstPart = false;
	    			cursor.toFirstChild();
	    			int countPart=2;
	    			do
	    			{
	    				AeToPartDef toPartDef = new AeToPartDef();
	    				toPartDef.setFromVariable(cursor.getAttributeText(new QName("fromVariable")));
	    				toPartDef.setLocationId(id);
	    				id++;
	    				String toPartDefPath = toPartsDefPath + "/toPart";
				        if(notFirstPart==false)
				        	notFirstPart=true;
				        else
				        {
				        	toPartDefPath = toPartDefPath + "["+countPart+"]";
				        	countPart++;
				        }
	    				
	    				toPartDef.setLocationPath(toPartDefPath);
	    				toPartDef.setParentXmlDef(toPartsDef);
	    				toPartDef.setPart(cursor.getAttributeText(new QName("part")));
	    				toPartsDef.addToPartDef(toPartDef);
	    				
	    			}while(cursor.toNextSibling());
	    			invokeDef.setMessageDataProducerStrategy("to-parts");
	    			invokeDef.setToPartsDef(toPartsDef);
	    			cursor.toParent();
		    	}else if(cursor.getName().getLocalPart().equals("fromParts"))
	    		{
	    			
	    			AeFromPartsDef fromPartsDef = new AeFromPartsDef();
	    			fromPartsDef.setLocationId(id);
	    			id++;
	    			String fromPartsDefPath = invokePath + "/fromParts";
	    			fromPartsDef.setLocationPath(fromPartsDefPath);
	    			fromPartsDef.setParentXmlDef(invokeDef);
	    			boolean notFirstPart = false;
	    			cursor.toFirstChild();
	    			int countPart=2;
	    			do
	    			{
	    				AeFromPartDef fromPartDef = new AeFromPartDef();
	    				fromPartDef.setToVariable(cursor.getAttributeText(new QName("toVariable")));
	    				fromPartDef.setLocationId(id);
	    				id++;
	    				String fromPartDefPath = fromPartsDefPath + "/fromPart";
				        if(notFirstPart==false)
				        	notFirstPart=true;
				        else
				        {
				        	fromPartDefPath = fromPartDefPath + "["+countPart+"]";
				        	countPart++;
				        }
	    				
	    				fromPartDef.setLocationPath(fromPartDefPath);
	    				fromPartDef.setParentXmlDef(fromPartsDef);
	    				fromPartDef.setPart(cursor.getAttributeText(new QName("part")));
	    				fromPartsDef.addFromPartDef(fromPartDef);
	    				
	    			}while(cursor.toNextSibling());
	    			invokeDef.setMessageDataConsumerStrategy("from-parts");
	    			invokeDef.setFromPartsDef(fromPartsDef);
	    			cursor.toParent();
		    	}else if(cursor.getName().getLocalPart().equals("correlations"))
		    	{
		    		AeCorrelationsDef correlationsDef = new AeCorrelationsDef();
		    		correlationsDef.setLocationId(id);
		    		id++;
		    		correlationsDef.setParentXmlDef(invokeDef);
		    		String correlationsDefPath = invokePath + "/correlations";
		    		correlationsDef.setLocationPath(correlationsDefPath);
		    		boolean notFirstCorrelation = false;
	    			cursor.toFirstChild();
	    			int countCorrelation=2;
	    			do
	    			{
	    				AeCorrelationDef correlationDef = new AeCorrelationDef();
	    				correlationDef.setCorrelationSetName(cursor.getAttributeText(new QName("set")));
	    				correlationDef.setLocationId(id);
	    				id++;
	    				if(cursor.getAttributeText(new QName("pattern")) != null)
	    				{
	    					AeCorrelationPatternType patternType = new AeCorrelationPatternType((cursor.getAttributeText(new QName("pattern"))));
	    					correlationDef.setPattern(patternType);
	    				}
	    				if(cursor.getAttributeText(new QName("initiate")) != null)
	    					correlationDef.setInitiate(cursor.getAttributeText(new QName("initiate")));
	    				else correlationDef.setInitiate("no");
	    				correlationDef.setParentXmlDef(correlationsDef);
	    				//correlationDef.setPattern(aPattern) da vedere se serve
	    				String correlationDefPath = correlationsDefPath + "/correlation";
				        if(notFirstCorrelation==false)
				        	notFirstCorrelation=true;
				        else
				        {
				        	correlationDefPath = correlationDefPath + "["+countCorrelation+"]";
				        	countCorrelation++;
				        }
	    				correlationDef.setLocationPath(correlationDefPath);
	    				correlationsDef.addCorrelationDef(correlationDef);
	    				
	    			}while(cursor.toNextSibling());
	    			invokeDef.setCorrelationsDef(correlationsDef);
	    			AeCorrelationsPatternImpl correlationsRequest = new AeCorrelationsPatternImpl(correlationsDef, invoke,true);
	    			
	    			AeCorrelationsPatternImpl correlationsResponse = new AeCorrelationsPatternImpl(correlationsDef, invoke,false);
	    			invoke.setRequestCorrelations(correlationsRequest);
	    			invoke.setResponseCorrelations(correlationsResponse);
	    			cursor.toParent();
	    			
		    	}
	    	
	    	
	    	}while(cursor.toNextSibling());
	    cursor.toParent();
	    
	   
	    IAeMessageDataConsumer messageDataConsumer = createMessageConsumer(invokeDef);
	    IAeMessageDataProducer messageDataProducer = createMessageProducer(invokeDef);
	    invoke.setMessageDataProducer(messageDataProducer);
	   
	    

	    invoke.setMessageDataConsumer(messageDataConsumer);
	    IAeMessageValidator MESSAGE_VALIDATOR = new AeBPELMessageDataValidator(false);
	    invoke.setMessageValidator(MESSAGE_VALIDATOR);
	    parent.getProcess().addBpelObject(invokePath, invoke);
	    ((AeProcessDef)parent.getProcess().getDefinition()).addMappings(invoke);
	   	   
	    parentDef.getActivityDefsList().add(i+1, invokeDef);
	    itParent.add(i+1,invoke);
	   
	}
	
	private void aggiungiInvokeOnMessage(AeOnMessage parent,XmlCursor cursor)
	{
		AeActivityInvokeDef invokeDef = new AeActivityInvokeDef();
		AeOnMessageDef parentDef = (AeOnMessageDef)parent.getDefinition();
		AeActivityInvokeImpl invoke = new AeActivityInvokeImpl(invokeDef, (IAeActivityParent) parent);
			
		invokeDef.setLocationId(id);
		invoke.setLocationId(id);
		id++;
		String nomeInvoke = cursor.getAttributeText(new QName("name"));
		String invokePath = parentDef.getLocationPath()+"/invoke[@name='"+nomeInvoke+"']";
		invokeDef.setLocationPath(invokePath);
		invoke.setLocationPath(invokePath);
	    invokeDef.setName(nomeInvoke);
	    invokeDef.setOperation(cursor.getAttributeText(new QName("operation")));
	    invokeDef.setParentXmlDef(parentDef);
	    invokeDef.setPartnerLink(cursor.getAttributeText(new QName("partnerLink")));
	    if(cursor.getAttributeText(new QName("portType")) != null)
		{
	    	String QNamePT[] = cursor.getAttributeText(new QName("portType")).split(":");
	    	String prefixPT = QNamePT[0];
	    	String portType = QNamePT[1];
	    	invokeDef.setPortType(new QName(parent.getProcess().getDefinition().getNamespace(prefixPT),portType));
		}
	    //nota con inputVariable va associato il producer
	    if(cursor.getAttributeText(new QName("inputVariable")) != null)
	    {
	    	AeVariableDef variabile = parent.getProcess().getVariable(parent.getProcess().getLocationPath(), cursor.getAttributeText(new QName("inputVariable"))).getDefinition();
	    	invokeDef.setProducerMessagePartsMap(variabile.getMessageParts());
	    	invokeDef.setInputVariable(cursor.getAttributeText(new QName("inputVariable")));
	    	if(variabile.getMessageType()!=null)
    		{
	    		invokeDef.setMessageDataProducerStrategy("message-variable");
    		}else if(variabile.getElement()!=null)
    		{
    			invokeDef.setMessageDataProducerStrategy("element-variable");
    		}						    	
	    } 
	    //nota con outputVariable va associato il consumer
		if(cursor.getAttributeText(new QName("outputVariable")) != null)
	    {
	    	AeVariableDef variabile = parent.getProcess().getVariable(parent.getProcess().getLocationPath(), cursor.getAttributeText(new QName("outputVariable"))).getDefinition();
	    	invokeDef.setConsumerMessagePartsMap(variabile.getMessageParts());
	    	invokeDef.setOutputVariable(cursor.getAttributeText(new QName("outputVariable")));
	    	if(variabile.getMessageType()!=null)
    		{
	    		invokeDef.setMessageDataConsumerStrategy("message-variable");
    		}else if(variabile.getElement()!=null)
    		{
    			invokeDef.setMessageDataConsumerStrategy("element-variable");
    		}						    	
	    }
	    
	    if(cursor.toFirstChild())
	    	do
	    	{
	    		if(cursor.getName().getLocalPart().equals("toParts"))
	    		{
	    			
	    			AeToPartsDef toPartsDef = new AeToPartsDef();
	    			toPartsDef.setLocationId(id);
	    			id++;
	    			String toPartsDefPath = invokePath + "/toParts";
	    			toPartsDef.setLocationPath(toPartsDefPath);
	    			toPartsDef.setParentXmlDef(invokeDef);
	    			boolean notFirstPart = false;
	    			cursor.toFirstChild();
	    			int countPart=2;
	    			do
	    			{
	    				AeToPartDef toPartDef = new AeToPartDef();
	    				toPartDef.setFromVariable(cursor.getAttributeText(new QName("fromVariable")));
	    				toPartDef.setLocationId(id);
	    				id++;
	    				String toPartDefPath = toPartsDefPath + "/toPart";
				        if(notFirstPart==false)
				        	notFirstPart=true;
				        else
				        {
				        	toPartDefPath = toPartDefPath + "["+countPart+"]";
				        	countPart++;
				        }
	    				
	    				toPartDef.setLocationPath(toPartDefPath);
	    				toPartDef.setParentXmlDef(toPartsDef);
	    				toPartDef.setPart(cursor.getAttributeText(new QName("part")));
	    				toPartsDef.addToPartDef(toPartDef);
	    				
	    			}while(cursor.toNextSibling());
	    			invokeDef.setMessageDataProducerStrategy("to-parts");
	    			invokeDef.setToPartsDef(toPartsDef);
	    			cursor.toParent();
		    	}else if(cursor.getName().getLocalPart().equals("fromParts"))
	    		{
	    			
	    			AeFromPartsDef fromPartsDef = new AeFromPartsDef();
	    			fromPartsDef.setLocationId(id);
	    			id++;
	    			String fromPartsDefPath = invokePath + "/fromParts";
	    			fromPartsDef.setLocationPath(fromPartsDefPath);
	    			fromPartsDef.setParentXmlDef(invokeDef);
	    			boolean notFirstPart = false;
	    			cursor.toFirstChild();
	    			int countPart=2;
	    			do
	    			{
	    				AeFromPartDef fromPartDef = new AeFromPartDef();
	    				fromPartDef.setToVariable(cursor.getAttributeText(new QName("toVariable")));
	    				fromPartDef.setLocationId(id);
	    				id++;
	    				String fromPartDefPath = fromPartsDefPath + "/fromPart";
				        if(notFirstPart==false)
				        	notFirstPart=true;
				        else
				        {
				        	fromPartDefPath = fromPartDefPath + "["+countPart+"]";
				        	countPart++;
				        }
	    				
	    				fromPartDef.setLocationPath(fromPartDefPath);
	    				fromPartDef.setParentXmlDef(fromPartsDef);
	    				fromPartDef.setPart(cursor.getAttributeText(new QName("part")));
	    				fromPartsDef.addFromPartDef(fromPartDef);
	    				
	    			}while(cursor.toNextSibling());
	    			invokeDef.setMessageDataConsumerStrategy("from-parts");
	    			invokeDef.setFromPartsDef(fromPartsDef);
	    			cursor.toParent();
		    	}else if(cursor.getName().getLocalPart().equals("correlations"))
		    	{
		    		AeCorrelationsDef correlationsDef = new AeCorrelationsDef();
		    		correlationsDef.setLocationId(id);
		    		id++;
		    		correlationsDef.setParentXmlDef(invokeDef);
		    		String correlationsDefPath = invokePath + "/correlations";
		    		correlationsDef.setLocationPath(correlationsDefPath);
		    		boolean notFirstCorrelation = false;
	    			cursor.toFirstChild();
	    			int countCorrelation=2;
	    			do
	    			{
	    				AeCorrelationDef correlationDef = new AeCorrelationDef();
	    				correlationDef.setCorrelationSetName(cursor.getAttributeText(new QName("set")));
	    				correlationDef.setLocationId(id);
	    				id++;
	    				if(cursor.getAttributeText(new QName("pattern")) != null)
	    				{
	    					AeCorrelationPatternType patternType = new AeCorrelationPatternType((cursor.getAttributeText(new QName("pattern"))));
	    					correlationDef.setPattern(patternType);
	    				}
	    				if(cursor.getAttributeText(new QName("initiate")) != null)
	    					correlationDef.setInitiate(cursor.getAttributeText(new QName("initiate")));
	    				else correlationDef.setInitiate("no");
	    				correlationDef.setParentXmlDef(correlationsDef);
	    				//correlationDef.setPattern(aPattern) da vedere se serve
	    				String correlationDefPath = correlationsDefPath + "/correlation";
				        if(notFirstCorrelation==false)
				        	notFirstCorrelation=true;
				        else
				        {
				        	correlationDefPath = correlationDefPath + "["+countCorrelation+"]";
				        	countCorrelation++;
				        }
	    				correlationDef.setLocationPath(correlationDefPath);
	    				correlationsDef.addCorrelationDef(correlationDef);
	    				
	    			}while(cursor.toNextSibling());
	    			invokeDef.setCorrelationsDef(correlationsDef);
	    			AeCorrelationsPatternImpl correlationsRequest = new AeCorrelationsPatternImpl(correlationsDef, invoke,true);
	    			
	    			AeCorrelationsPatternImpl correlationsResponse = new AeCorrelationsPatternImpl(correlationsDef, invoke,false);
	    			invoke.setRequestCorrelations(correlationsRequest);
	    			invoke.setResponseCorrelations(correlationsResponse);
	    			cursor.toParent();
	    			
		    	}
	    	
	    	
	    	}while(cursor.toNextSibling());
	    cursor.toParent();
	    
	   
	    IAeMessageDataConsumer messageDataConsumer = createMessageConsumer(invokeDef);
	    IAeMessageDataProducer messageDataProducer = createMessageProducer(invokeDef);
	    invoke.setMessageDataProducer(messageDataProducer);
	   
	    

	    invoke.setMessageDataConsumer(messageDataConsumer);
	    IAeMessageValidator MESSAGE_VALIDATOR = new AeBPELMessageDataValidator(false);
	    invoke.setMessageValidator(MESSAGE_VALIDATOR);
	    parent.getProcess().addBpelObject(invokePath, invoke);
	    ((AeProcessDef)parent.getProcess().getDefinition()).addMappings(invoke);
	   	   
	    parentDef.setActivityDef(invokeDef);
	    parent.setActivity(invoke);
		
	   
	}
	
	
	private void aggiungiReply(String at_target,String at_nuova,AeActivityImpl aeAct)
	{
		AeActivityImpl parent = (AeActivityImpl) aeAct;
		
		//questo solo in caso in cui il padre sia una sequence
		List itParent = ((AeActivitySequenceImpl)parent).getMActivities();
		if((at_target.equals(""))||(at_target==null))
		{
			aggiungiReplyPosizione(itParent,parent,at_nuova,-1); 
		}
		else
		for(int i=0;i<itParent.size();i++)
		{
			if((((AeActivityImpl)itParent.get(i)).getLocationPath().equals(at_target)))
			{ 
				aggiungiReplyPosizione(itParent,parent,at_nuova,i); 
			}
		}
	}
	private void aggiungiReplyPosizione(List itParent,AeActivityImpl parent,String at_nuova,int i)
	{
		XmlObject xmlObject = null;
	       
		try {
			xmlObject = XmlObject.Factory.parse(at_nuova);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
			
		XmlCursor cursor = xmlObject.newCursor();
		cursor.toNextToken();
		aggiungiReplyPosizioneCursor(itParent,parent,i,cursor); 
		
	}
	private void aggiungiReplyPosizioneCursor(List itParent,AeActivityImpl parent,int i,XmlCursor cursor)
	{
		AeActivityReplyDef replyDef = new AeActivityReplyDef();
		AeActivitySequenceDef parentDef = (AeActivitySequenceDef)parent.getDefinition();
		AeActivityReplyImpl reply = new AeActivityReplyImpl(replyDef, (IAeActivityParent) parent);
		reply.setLocationId(id);
		replyDef.setLocationId(id);
		id++;
		String nomeReply = cursor.getAttributeText(new QName("name"));
		String replyPath = parentDef.getLocationPath()+"/reply[@name='"+nomeReply+"']";
		if(cursor.getAttributeText(new QName("messageExchange")) != null)
		{
			replyDef.setMessageExchange(cursor.getAttributeText(new QName("messageExchange")));
		}else
		{
			replyDef.setMessageExchange("");	
		}
			
		replyDef.setLocationPath(replyPath);
		reply.setLocationPath(replyPath);
		if(cursor.getAttributeText(new QName("faultName")) != null)
		{
			String QNameFN[] = cursor.getAttributeText(new QName("faultName")).split(":");
		    String prefixFN = QNameFN[0];
		    String faultName = QNameFN[1];
			replyDef.setFaultName(new QName(prefixFN, faultName));
		}
	    replyDef.setName(nomeReply);
	    replyDef.setOperation(cursor.getAttributeText(new QName("operation")));
	    replyDef.setParentXmlDef(parentDef);
	    replyDef.setPartnerLink(cursor.getAttributeText(new QName("partnerLink")));
	    if(cursor.getAttributeText(new QName("portType")) != null)
		{
	    	String QNamePT[] = cursor.getAttributeText(new QName("portType")).split(":");
	    	String prefixPT = QNamePT[0];
	    	String portType = QNamePT[1];
	    	replyDef.setPortType(new QName(parentDef.getNamespace(prefixPT),portType));
		}
	    if(cursor.getAttributeText(new QName("variable")) != null)
	    {
	    	AeVariableDef variabile = parent.getProcess().getVariable(parent.getProcess().getLocationPath(), cursor.getAttributeText(new QName("variable"))).getDefinition();
	    	replyDef.setProducerMessagePartsMap(variabile.getMessageParts());
	    	replyDef.setVariable(cursor.getAttributeText(new QName("variable")));
	    	if(variabile.getMessageType()!=null)
    		{
	    		replyDef.setMessageDataProducerStrategy("message-variable");
    		}else if(variabile.getElement()!=null)
    		{
    			replyDef.setMessageDataProducerStrategy("element-variable");
    		}						    	
	    }
	    replyDef.setConsumerMessagePartsMap(null);
	    if(cursor.toFirstChild())
	    	do
	    	{
	    		if(cursor.getName().getLocalPart().equals("toParts"))
	    		{
	    			
	    			AeToPartsDef toPartsDef = new AeToPartsDef();
	    			toPartsDef.setLocationId(id);
	    			id++;
	    			String toPartsDefPath = replyPath + "/toParts";
	    			toPartsDef.setLocationPath(toPartsDefPath);
	    			toPartsDef.setParentXmlDef(replyDef);
	    			boolean notFirstPart = false;
	    			cursor.toFirstChild();
	    			int countPart=2;
	    			do
	    			{
	    				AeToPartDef toPartDef = new AeToPartDef();
	    				toPartDef.setFromVariable(cursor.getAttributeText(new QName("fromVariable")));
	    				toPartDef.setLocationId(id);
	    				id++;
	    				String toPartDefPath = toPartsDefPath + "/toPart";
				        if(notFirstPart==false)
				        	notFirstPart=true;
				        else
				        {
				        	toPartDefPath = toPartDefPath + "["+countPart+"]";
				        	countPart++;
				        }
	    				
	    				toPartDef.setLocationPath(toPartDefPath);
	    				toPartDef.setParentXmlDef(toPartsDef);
	    				toPartDef.setPart(cursor.getAttributeText(new QName("part")));
	    				toPartsDef.addToPartDef(toPartDef);
	    				
	    			}while(cursor.toNextSibling());
	    			replyDef.setMessageDataProducerStrategy("to-parts");
	    			replyDef.setToPartsDef(toPartsDef);
	    			cursor.toParent();
		    	}else if(cursor.getName().getLocalPart().equals("correlations"))
		    	{
		    		AeCorrelationsDef correlationsDef = new AeCorrelationsDef();
		    		correlationsDef.setLocationId(id);
		    		id++;
		    		correlationsDef.setParentXmlDef(replyDef);
		    		String correlationsDefPath = replyPath + "/correlations";
		    		correlationsDef.setLocationPath(correlationsDefPath);
		    		boolean notFirstCorrelation = false;
	    			cursor.toFirstChild();
	    			int countCorrelation=2;
	    			do
	    			{
	    				AeCorrelationDef correlationDef = new AeCorrelationDef();
	    				correlationDef.setCorrelationSetName(cursor.getAttributeText(new QName("set")));
	    				correlationDef.setLocationId(id);
	    				id++;
	    				if(cursor.getAttributeText(new QName("initiate")) != null)
	    					correlationDef.setInitiate(cursor.getAttributeText(new QName("initiate")));
	    				else correlationDef.setInitiate("no");
	    				correlationDef.setParentXmlDef(correlationsDef);
	    				//correlationDef.setPattern(aPattern) da vedere se serve
	    				String correlationDefPath = correlationsDefPath + "/correlation";
				        if(notFirstCorrelation==false)
				        	notFirstCorrelation=true;
				        else
				        {
				        	correlationDefPath = correlationDefPath + "["+countCorrelation+"]";
				        	countCorrelation++;
				        }
	    				correlationDef.setLocationPath(correlationDefPath);
	    				correlationsDef.addCorrelationDef(correlationDef);
	    				
	    			}while(cursor.toNextSibling());
	    			replyDef.setCorrelationsDef(correlationsDef);
	    			AeCorrelationsImpl correlations = new AeCorrelationsImpl(correlationsDef, reply);
	    			reply.setResponseCorrelations(correlations);
	    			cursor.toParent();
	    			
		    	}
	    	
	    	
	    	}while(cursor.toNextSibling());
	    cursor.toParent();
	    
	   
	     
	    IAeMessageDataProducer messageDataProducer = createMessageProducer(replyDef);
	    reply.setMessageDataProducer(messageDataProducer);
	    IAeMessageValidator MESSAGE_VALIDATOR = new AeBPELMessageDataValidator(false);
	    
	    reply.setMessageValidator(MESSAGE_VALIDATOR);
	    parent.getProcess().addBpelObject(replyPath, reply);
	    ((AeProcessDef)parent.getProcess().getDefinition()).addMappings(reply);
	   	
	    parentDef.getActivityDefsList().add(i+1, replyDef);
	    itParent.add(i+1,reply);
	   
	}
	
	private void aggiungiReplyOnMessage(AeOnMessage parent,XmlCursor cursor)
	{
		AeActivityReplyDef replyDef = new AeActivityReplyDef();
		AeOnMessageDef parentDef = (AeOnMessageDef)parent.getDefinition();
		AeActivityReplyImpl reply = new AeActivityReplyImpl(replyDef, (IAeActivityParent) parent);
		reply.setLocationId(id);
		replyDef.setLocationId(id);
		id++;
		String nomeReply = cursor.getAttributeText(new QName("name"));
		String replyPath = parentDef.getLocationPath()+"/reply[@name='"+nomeReply+"']";
		if(cursor.getAttributeText(new QName("messageExchange")) != null)
		{
			replyDef.setMessageExchange(cursor.getAttributeText(new QName("messageExchange")));
		}else
		{
			replyDef.setMessageExchange("");	
		}
			
		replyDef.setLocationPath(replyPath);
		reply.setLocationPath(replyPath);
		if(cursor.getAttributeText(new QName("faultName")) != null)
		{
			String QNameFN[] = cursor.getAttributeText(new QName("faultName")).split(":");
		    String prefixFN = QNameFN[0];
		    String faultName = QNameFN[1];
			replyDef.setFaultName(new QName(prefixFN, faultName));
		}
	    replyDef.setName(nomeReply);
	    replyDef.setOperation(cursor.getAttributeText(new QName("operation")));
	    replyDef.setParentXmlDef(parentDef);
	    replyDef.setPartnerLink(cursor.getAttributeText(new QName("partnerLink")));
	    if(cursor.getAttributeText(new QName("portType")) != null)
		{
	    	String QNamePT[] = cursor.getAttributeText(new QName("portType")).split(":");
	    	String prefixPT = QNamePT[0];
	    	String portType = QNamePT[1];
	    	replyDef.setPortType(new QName(parentDef.getNamespace(prefixPT),portType));
		}
	    if(cursor.getAttributeText(new QName("variable")) != null)
	    {
	    	AeVariableDef variabile = parent.getProcess().getVariable(parent.getProcess().getLocationPath(), cursor.getAttributeText(new QName("variable"))).getDefinition();
	    	replyDef.setProducerMessagePartsMap(variabile.getMessageParts());
	    	replyDef.setVariable(cursor.getAttributeText(new QName("variable")));
	    	if(variabile.getMessageType()!=null)
    		{
	    		replyDef.setMessageDataProducerStrategy("message-variable");
    		}else if(variabile.getElement()!=null)
    		{
    			replyDef.setMessageDataProducerStrategy("element-variable");
    		}						    	
	    }
	    replyDef.setConsumerMessagePartsMap(null);
	    if(cursor.toFirstChild())
	    	do
	    	{
	    		if(cursor.getName().getLocalPart().equals("toParts"))
	    		{
	    			
	    			AeToPartsDef toPartsDef = new AeToPartsDef();
	    			toPartsDef.setLocationId(id);
	    			id++;
	    			String toPartsDefPath = replyPath + "/toParts";
	    			toPartsDef.setLocationPath(toPartsDefPath);
	    			toPartsDef.setParentXmlDef(replyDef);
	    			boolean notFirstPart = false;
	    			cursor.toFirstChild();
	    			int countPart=2;
	    			do
	    			{
	    				AeToPartDef toPartDef = new AeToPartDef();
	    				toPartDef.setFromVariable(cursor.getAttributeText(new QName("fromVariable")));
	    				toPartDef.setLocationId(id);
	    				id++;
	    				String toPartDefPath = toPartsDefPath + "/toPart";
				        if(notFirstPart==false)
				        	notFirstPart=true;
				        else
				        {
				        	toPartDefPath = toPartDefPath + "["+countPart+"]";
				        	countPart++;
				        }
	    				
	    				toPartDef.setLocationPath(toPartDefPath);
	    				toPartDef.setParentXmlDef(toPartsDef);
	    				toPartDef.setPart(cursor.getAttributeText(new QName("part")));
	    				toPartsDef.addToPartDef(toPartDef);
	    				
	    			}while(cursor.toNextSibling());
	    			replyDef.setMessageDataProducerStrategy("to-parts");
	    			replyDef.setToPartsDef(toPartsDef);
	    			cursor.toParent();
		    	}else if(cursor.getName().getLocalPart().equals("correlations"))
		    	{
		    		AeCorrelationsDef correlationsDef = new AeCorrelationsDef();
		    		correlationsDef.setLocationId(id);
		    		id++;
		    		correlationsDef.setParentXmlDef(replyDef);
		    		String correlationsDefPath = replyPath + "/correlations";
		    		correlationsDef.setLocationPath(correlationsDefPath);
		    		boolean notFirstCorrelation = false;
	    			cursor.toFirstChild();
	    			int countCorrelation=2;
	    			do
	    			{
	    				AeCorrelationDef correlationDef = new AeCorrelationDef();
	    				correlationDef.setCorrelationSetName(cursor.getAttributeText(new QName("set")));
	    				correlationDef.setLocationId(id);
	    				id++;
	    				if(cursor.getAttributeText(new QName("initiate")) != null)
	    					correlationDef.setInitiate(cursor.getAttributeText(new QName("initiate")));
	    				else correlationDef.setInitiate("no");
	    				correlationDef.setParentXmlDef(correlationsDef);
	    				//correlationDef.setPattern(aPattern) da vedere se serve
	    				String correlationDefPath = correlationsDefPath + "/correlation";
				        if(notFirstCorrelation==false)
				        	notFirstCorrelation=true;
				        else
				        {
				        	correlationDefPath = correlationDefPath + "["+countCorrelation+"]";
				        	countCorrelation++;
				        }
	    				correlationDef.setLocationPath(correlationDefPath);
	    				correlationsDef.addCorrelationDef(correlationDef);
	    				
	    			}while(cursor.toNextSibling());
	    			replyDef.setCorrelationsDef(correlationsDef);
	    			AeCorrelationsImpl correlations = new AeCorrelationsImpl(correlationsDef, reply);
	    			reply.setResponseCorrelations(correlations);
	    			cursor.toParent();
	    			
		    	}
	    	
	    	
	    	}while(cursor.toNextSibling());
	    cursor.toParent();
	    
	   
	     
	    IAeMessageDataProducer messageDataProducer = createMessageProducer(replyDef);
	    reply.setMessageDataProducer(messageDataProducer);
	    IAeMessageValidator MESSAGE_VALIDATOR = new AeBPELMessageDataValidator(false);
	    
	    reply.setMessageValidator(MESSAGE_VALIDATOR);
	    parent.getProcess().addBpelObject(replyPath, reply);
	    ((AeProcessDef)parent.getProcess().getDefinition()).addMappings(reply);
	   	
	    parentDef.setActivityDef(replyDef);
	    parent.setActivity(reply);
	}
	
	private void replaceSequence(String at_target,String at_nuova,AeActivityImpl aeAct)
	{
		AeBusinessProcess parent = (AeBusinessProcess) aeAct;
		XmlObject xmlObject = null;
	      
		try {
			xmlObject = XmlObject.Factory.parse(at_nuova);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
					
		XmlCursor cursor = xmlObject.newCursor();
		cursor.toNextToken();
		AeActivitySequenceDef sequenceDef = new AeActivitySequenceDef();
		AeProcessDef parentDef = (AeProcessDef)parent.getDefinition();
		AeActivitySequenceImpl sequence = new AeActivitySequenceImpl(sequenceDef, (IAeActivityParent) parent);

		
		sequenceDef.setLocationId(id);
		sequence.setLocationId(id);
		id++;
				
		String nomeSequence= cursor.getAttributeText(new QName("name"));
		String sequencePath = parentDef.getLocationPath()+"/sequence[@name='"+nomeSequence+"']";
		sequenceDef.setLocationPath(sequencePath);
		sequence.setLocationPath(sequencePath);
		sequenceDef.setParentXmlDef(parentDef);
		
		parentDef.replaceActivityDef(parentDef.getActivityDef(), sequenceDef);
		parent.replaceActivity(sequence);
		//ora devo riempire la sequence che ho appena creato
		cursor.toFirstChild();
		
		int k=-1;
		do
		{
			XmlCursor cursorAttività=cursor.newCursor();
			List itParentSequence = sequence.getMActivities();
			//System.out.println(cursor.getName().getLocalPart());
			if(cursor.getName().getLocalPart().equals("assign"))
			{
				aggiungiAssignPosizioneCursor(itParentSequence, sequence, k, cursor);
			}else if (cursor.getName().getLocalPart().equals("reply"))
			{
				aggiungiReplyPosizioneCursor(itParentSequence, sequence, k, cursor);
			}else if (cursor.getName().getLocalPart().equals("receive"))
			{
				//System.out.println("dentro l'if della receive");
				//System.out.println(cursor.getName().getLocalPart());
				//System.out.println("cursor.getAttributeText "+cursorAttività.getAttributeText(new QName("createInstance")));
				aggiungiReceivePosizioneCursor(itParentSequence, sequence, k, cursorAttività);
			}else if (cursor.getName().getLocalPart().equals("invoke"))
			{
				aggiungiInvokePosizioneCursor(itParentSequence, sequence, k, cursor);
			}else if(cursor.getName().getLocalPart().equals("sequence"))
			{
				//aggiungiSequence(at_target,at_nuova,aeAct);
			}
			k++;
		}while(cursor.toNextSibling());
		 
	}
	//suppongo che la sequence possa stare dentro il process iniziale da estendere a tutte le attività che 
	//possono contenere una sequence
	private void aggiungiSequence(String at_target,String at_nuova,AeActivityImpl aeAct)
	{
		AeActivityImpl parent = (AeActivityImpl) aeAct;
		//questo solo in caso in cui il padre sia una sequence
		List itParent = ((AeActivitySequenceImpl)parent).getMActivities();
		if((at_target.equals(""))||(at_target==null))
		{
			aggiungiSequencePosizione(itParent,parent,at_nuova,-1);
		}
		else
		for(int i=0;i<itParent.size();i++)
		{
			
			if((((AeActivityImpl)itParent.get(i)).getLocationPath().equals(at_target)))
			{ 
				aggiungiSequencePosizione(itParent,parent,at_nuova,i);
				
			}//fine if su attivita target
		}//fine for sulle attivita del padre

	}
	private void aggiungiSequencePosizione(List itParent,AeActivityImpl parent,String at_nuova,int i)
	{
			
		XmlObject xmlObject = null;
		      
		try {
			xmlObject = XmlObject.Factory.parse(at_nuova);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
					
		XmlCursor cursor = xmlObject.newCursor();
		cursor.toNextToken();
		aggiungiSequencePosizioneCursor(itParent,parent,i,cursor);
	}
	private void aggiungiSequenceOnMessage(AeOnMessage parent,XmlCursor cursor)	
	{
		AeActivitySequenceDef sequenceDef = new AeActivitySequenceDef();
		AeOnMessageDef parentDef = (AeOnMessageDef)parent.getDefinition();
		AeActivitySequenceImpl sequence = new AeActivitySequenceImpl(sequenceDef, (IAeActivityParent) parent);

		
		sequenceDef.setLocationId(id);
		sequence.setLocationId(id);
		id++;
				
		String nomeSequence= cursor.getAttributeText(new QName("name"));
		String sequencePath = parentDef.getLocationPath()+"/sequence[@name='"+nomeSequence+"']";
		sequenceDef.setName(nomeSequence);
		sequenceDef.setLocationPath(sequencePath);
		sequence.setLocationPath(sequencePath);
		sequenceDef.setParentXmlDef(parentDef);
		
		
		//ora devo riempire la sequence che ho appena creato
		cursor.toFirstChild();
		
		int k=-1;
		do
		{
			XmlCursor cursorAttività=cursor.newCursor();
			List itParentSequence = sequence.getMActivities();
			//System.out.println(cursor.getName().getLocalPart());
			if(cursor.getName().getLocalPart().equals("assign"))
			{
				aggiungiAssignPosizioneCursor(itParentSequence, sequence, k, cursor);
			}else if (cursor.getName().getLocalPart().equals("reply"))
			{
				aggiungiReplyPosizioneCursor(itParentSequence, sequence, k, cursor);
			}else if (cursor.getName().getLocalPart().equals("receive"))
			{
				//System.out.println("dentro l'if della receive");
				//System.out.println(cursor.getName().getLocalPart());
				//System.out.println("cursor.getAttributeText "+cursorAttività.getAttributeText(new QName("createInstance")));
				aggiungiReceivePosizioneCursor(itParentSequence, sequence, k, cursorAttività);
			}else if (cursor.getName().getLocalPart().equals("invoke"))
			{
				aggiungiInvokePosizioneCursor(itParentSequence, sequence, k, cursor);
			}else if(cursor.getName().getLocalPart().equals("sequence"))
			{
				aggiungiSequencePosizioneCursor(itParentSequence, sequence, k, cursor);
			}
			k++;
		}while(cursor.toNextSibling());
		parent.getProcess().addBpelObject(sequencePath, sequence);
		((AeProcessDef)parent.getProcess().getDefinition()).addMappings(sequence);
		   	
		parentDef.setActivityDef(sequenceDef);
	    parent.setActivity(sequence);
		
	}
	
	private void aggiungiSequencePosizioneCursor(List itParent,AeActivityImpl parent,int i,XmlCursor cursor)	
	{
		AeActivitySequenceDef sequenceDef = new AeActivitySequenceDef();
		AeActivitySequenceDef parentDef = (AeActivitySequenceDef)parent.getDefinition();
		AeActivitySequenceImpl sequence = new AeActivitySequenceImpl(sequenceDef, (IAeActivityParent) parent);
		sequenceDef.setLocationId(id);
		sequence.setLocationId(id);
		id++;
				
		String nomeSequence= cursor.getAttributeText(new QName("name"));
		String sequencePath = parentDef.getLocationPath()+"/sequence[@name='"+nomeSequence+"']";
		sequenceDef.setName(nomeSequence);
		sequenceDef.setLocationPath(sequencePath);
		sequence.setLocationPath(sequencePath);
		sequenceDef.setParentXmlDef(parentDef);
		
		
		//ora devo riempire la sequence che ho appena creato
		cursor.toFirstChild();
		
		int k=-1;
		do
		{
			XmlCursor cursorAttività=cursor.newCursor();
			List itParentSequence = sequence.getMActivities();
			//System.out.println(cursor.getName().getLocalPart());
			if(cursor.getName().getLocalPart().equals("assign"))
			{
				aggiungiAssignPosizioneCursor(itParentSequence, sequence, k, cursor);
			}else if (cursor.getName().getLocalPart().equals("reply"))
			{
				aggiungiReplyPosizioneCursor(itParentSequence, sequence, k, cursor);
			}else if (cursor.getName().getLocalPart().equals("receive"))
			{
				//System.out.println("dentro l'if della receive");
				//System.out.println(cursor.getName().getLocalPart());
				//System.out.println("cursor.getAttributeText "+cursorAttività.getAttributeText(new QName("createInstance")));
				aggiungiReceivePosizioneCursor(itParentSequence, sequence, k, cursorAttività);
			}else if (cursor.getName().getLocalPart().equals("invoke"))
			{
				aggiungiInvokePosizioneCursor(itParentSequence, sequence, k, cursor);
			}else if(cursor.getName().getLocalPart().equals("sequence"))
			{
				aggiungiSequencePosizioneCursor(itParentSequence, sequence, k, cursor);
			}
			k++;
		}while(cursor.toNextSibling());
		parent.getProcess().addBpelObject(sequencePath, sequence);
		((AeProcessDef)parent.getProcess().getDefinition()).addMappings(sequence);
		   	
		parentDef.getActivityDefsList().add(i+1, sequenceDef);
	    itParent.add(i+1,sequence);
		
	}
	
	private void aggiungiPick(String at_target,String at_nuova,AeActivityImpl aeAct)
	{
		
		AeActivityImpl parent = (AeActivityImpl) aeAct;
		
		//questo solo in caso in cui il padre sia una sequence
		List itParent = ((AeActivitySequenceImpl)parent).getMActivities();
		if((at_target.equals(""))||(at_target==null))
		{
			aggiungiPickPosizione(itParent,parent,at_nuova,-1);
		}else
		for(int i=0;i<itParent.size();i++)
		{
			if((((AeActivityImpl)itParent.get(i)).getLocationPath().equals(at_target)))
			{
				aggiungiPickPosizione(itParent,parent,at_nuova,i);
			}
		}
	}
	
	
	private void aggiungiPickPosizione(List itParent,AeActivityImpl parent,String at_nuova,int i)
	{
		XmlObject xmlObject = null;
	       
		try {
			xmlObject = XmlObject.Factory.parse(at_nuova);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		XmlCursor cursor = xmlObject.newCursor();
		cursor.toNextToken();  
		aggiungiPickPosizioneCursor(itParent,parent,i,cursor);
		
	}
	private void aggiungiPickPosizioneCursor(List itParent,AeActivityImpl parent,int i,XmlCursor cursor)
	{

		//System.out.println("\n\n\n\nsono nel metodo aggiungiPickCursor\n\n\n\n");
		AeActivityPickDef pickDef = new AeActivityPickDef();
		
		AeActivitySequenceDef parentDef = (AeActivitySequenceDef)parent.getDefinition();
		
		AeActivityPickImpl pick = new AeActivityPickImpl(pickDef, (IAeActivityParent) parent);
		pickDef.setLocationId(id);
		pick.setLocationId(id);
		id++;
		String nomePick = cursor.getAttributeText(new QName("name"));
		String pickPath = parentDef.getLocationPath()+"/pick[@name='"+nomePick+"']";
		
		pick.setLocationPath(pickPath);
		pickDef.setLocationPath(pickPath);
	    pickDef.setName(nomePick);
	   
	    pickDef.setParentXmlDef(parentDef);
	    
	    if(cursor.getAttributeText(new QName("createInstance"))!=null)
	    {
		    if(cursor.getAttributeText(new QName("createInstance")).equals("yes"))
		    {
		    	pickDef.setCreateInstance(true);   
		    }else 
		    {
		    	pickDef.setCreateInstance(false);
		    }
	    }else pickDef.setCreateInstance(false);
	    cursor.toFirstChild();
		do
		{
			 XmlCursor cursorOnMessage = cursor.newCursor();
			aggiungiOnMessageCursor(pick, cursorOnMessage);
		}while(cursor.toNextSibling());
		parent.getProcess().addBpelObject(pickPath, pick);
		((AeProcessDef)parent.getProcess().getDefinition()).addMappings(pick);
		   	
		parentDef.getActivityDefsList().add(i+1, pickDef);
	    itParent.add(i+1,pick);
	
	}
	
	
	private void aggiungiOnMessage(String at_target,String at_nuova,AeActivityImpl aeAct)
	{
		XmlObject xmlObject = null;
	       
		try {
			xmlObject = XmlObject.Factory.parse(at_nuova);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		XmlCursor cursor = xmlObject.newCursor();
		cursor.toNextToken();  
		aggiungiOnMessageCursor(aeAct,cursor);
	}
	
	private void aggiungiOnMessageCursor(AeActivityImpl aeAct,XmlCursor cursor)
	{
		//System.out.println("\n\n\n\nsono nel metodo aggiungiOnMessageCursor\n\n\n\n");
		AeActivityPickImpl parent = (AeActivityPickImpl) aeAct;
		//questo solo in caso in cui il padre sia una sequence
		
		AeOnMessageDef onMessageDef = new AeOnMessageDef();
		AeOnMessage onMessage = new AeOnMessage(onMessageDef,parent);
		AeActivityPickDef parentDef = (AeActivityPickDef)parent.getDefinition();
		//parentDef.removeOnMessageDef((AeOnMessageDef)parentDef.getOnMessageDefs().next());
		//parent.removeMessage((AeOnMessage)parent.getChildrenForStateChange().next());
		onMessageDef.setLocationId(id);
		onMessage.setLocationId(id);
		id++;
		
		Iterator mesIt = parentDef.getOnMessageDefs();
		int n=0;
		while(mesIt.hasNext())
		{
			mesIt.next();
			n++;
		}
		
		String onMessagePath = parentDef.getLocationPath()+"/onMessage";
		if(n!=0)
		{
			n++;
			onMessagePath += "["+n+"]";
		}
		if(cursor.getAttributeText(new QName("messageExchange")) != null)
		{
			onMessageDef.setMessageExchange(cursor.getAttributeText(new QName("messageExchange")));
		}else
		{
			onMessageDef.setMessageExchange("");	
		}
		
		onMessageDef.setLocationPath(onMessagePath);
		onMessage.setLocationPath(onMessagePath);
		onMessageDef.setOperation(cursor.getAttributeText(new QName("operation")));
	    onMessageDef.setParentXmlDef(parentDef);
	    
        onMessageDef.setPartnerLink(cursor.getAttributeText(new QName("partnerLink")));
	    if(cursor.getAttributeText(new QName("portType")) != null)
		{
	    	String QNamePT[] = cursor.getAttributeText(new QName("portType")).split(":");
	    	String prefixPT = QNamePT[0];
	    	String portType = QNamePT[1];
	    	onMessageDef.setPortType(new QName(parent.getProcess().getDefinition().getNamespace(prefixPT),portType));
		}
	    if(cursor.getAttributeText(new QName("variable")) != null)
	    {
	    	AeVariableDef variabile = parent.getProcess().getVariable(parent.getProcess().getLocationPath(), cursor.getAttributeText(new QName("variable"))).getDefinition();
	    	onMessageDef.setConsumerMessagePartsMap(variabile.getMessageParts());
	    	onMessageDef.setVariable(cursor.getAttributeText(new QName("variable")));
	    	if(variabile.getMessageType()!=null)
    		{
	    		onMessageDef.setMessageDataConsumerStrategy("message-variable");
    		}else if(variabile.getElement()!=null)
    		{
    			onMessageDef.setMessageDataConsumerStrategy("element-variable");
    		}						    	
	    }
	    if(cursor.toFirstChild())
	    	do
	    	{
	    		if(cursor.getName().getLocalPart().equals("fromParts"))
	    		{
	    			
	    			AeFromPartsDef fromPartsDef = new AeFromPartsDef();
	    			fromPartsDef.setLocationId(id);
	    			id++;
	    			String fromPartsDefPath = onMessagePath + "/fromParts";
	    			fromPartsDef.setLocationPath(fromPartsDefPath);
	    			fromPartsDef.setParentXmlDef(onMessageDef);
	    			boolean notFirstPart = false;
	    			cursor.toFirstChild();
	    			int countPart=2;
	    			do
	    			{
	    				AeFromPartDef fromPartDef = new AeFromPartDef();
	    				fromPartDef.setToVariable(cursor.getAttributeText(new QName("toVariable")));
	    				fromPartDef.setLocationId(id);
	    				id++;
	    				String fromPartDefPath = fromPartsDefPath + "/fromPart";
				        if(notFirstPart==false)
				        	notFirstPart=true;
				        else
				        {
				        	fromPartDefPath = fromPartDefPath + "["+countPart+"]";
				        	countPart++;
				        }
	    				
	    				fromPartDef.setLocationPath(fromPartDefPath);
	    				fromPartDef.setParentXmlDef(fromPartsDef);
	    				fromPartDef.setPart(cursor.getAttributeText(new QName("part")));
	    				fromPartsDef.addFromPartDef(fromPartDef);
	    				
	    			}while(cursor.toNextSibling());
	    			onMessageDef.setMessageDataConsumerStrategy("from-parts");
	    			onMessageDef.setFromPartsDef(fromPartsDef);
	    			cursor.toParent();
		    	}else if(cursor.getName().getLocalPart().equals("correlations"))
		    	{
		    		AeCorrelationsDef correlationsDef = new AeCorrelationsDef();
		    		correlationsDef.setLocationId(id);
		    		id++;
		    		correlationsDef.setParentXmlDef(onMessageDef);
		    		String correlationsDefPath = onMessagePath + "/correlations";
		    		correlationsDef.setLocationPath(correlationsDefPath);
		    		boolean notFirstCorrelation = false;
	    			cursor.toFirstChild();
	    			int countCorrelation=2;
	    			do
	    			{
	    				AeCorrelationDef correlationDef = new AeCorrelationDef();
	    				correlationDef.setCorrelationSetName(cursor.getAttributeText(new QName("set")));
	    				correlationDef.setLocationId(id);
	    				
	    				id++;
	    				if(cursor.getAttributeText(new QName("initiate")) != null)
	    					correlationDef.setInitiate(cursor.getAttributeText(new QName("initiate")));
	    				else correlationDef.setInitiate("no");
	    				correlationDef.setParentXmlDef(correlationsDef);
	    				
	    				String correlationDefPath = correlationsDefPath + "/correlation";
				        if(notFirstCorrelation==false)
				        	notFirstCorrelation=true;
				        else
				        {
				        	correlationDefPath = correlationDefPath + "["+countCorrelation+"]";
				        	countCorrelation++;
				        }
				        correlationDef.shouldAlreadyBeInitiated();
	    				correlationDef.setLocationPath(correlationDefPath);
	    				correlationsDef.addCorrelationDef(correlationDef);
	    				
	    			}while(cursor.toNextSibling());
	    			onMessageDef.setCorrelationsDef(correlationsDef);
	    			
	    			AeIMACorrelations correlations = new AeIMACorrelations(correlationsDef, onMessage);
	    			correlations.setFilter(AeIMACorrelations.INITIALIZED);
	    			onMessage.setRequestCorrelations(correlations);
	    			cursor.toParent();
	    			
		    	}else if(cursor.getName().getLocalPart().equals("assign"))
				{
					aggiungiAssignOnMessage(onMessage, cursor);
				}else if (cursor.getName().getLocalPart().equals("reply"))
				{
					aggiungiReplyOnMessage(onMessage, cursor);
				}else if (cursor.getName().getLocalPart().equals("receive"))
				{
					aggiungiReceiveOnMessage(onMessage, cursor);
				}else if (cursor.getName().getLocalPart().equals("invoke"))
				{
					aggiungiInvokeOnMessage(onMessage, cursor);
				}else if(cursor.getName().getLocalPart().equals("sequence"))
				{
					aggiungiSequenceOnMessage(onMessage, cursor);
				}
	    	
	    	}while(cursor.toNextSibling());
	    cursor.toParent();
	    
	   
	     
	    IAeMessageDataConsumer messageDataConsumer = createMessageConsumer(onMessageDef);
	    onMessage.setMessageDataConsumer(messageDataConsumer);
	    IAeMessageValidator MESSAGE_VALIDATOR = new AeBPELMessageDataValidator(false);
	    onMessage.setMessageValidator(MESSAGE_VALIDATOR);
	    parent.getProcess().addBpelObject(onMessagePath, onMessage);
	    ((AeProcessDef)parent.getProcess().getDefinition()).addMappings(onMessage);
	    parentDef.addOnMessageDef(onMessageDef);
	    parent.addMessage(onMessage);
	   
	}
	
	private String getNomeAttivita(String at_target)
	{
		String nomeAttivita = "assign";
		
		
		
		return nomeAttivita;
		
		
		
	}
	private IAeMessageDataConsumer createMessageConsumer(IAeMessageDataConsumerDef aDef)
	{
	      IAeMessageDataConsumer messageDataConsumer;

	      if (IAeMessageDataStrategyNames.MESSAGE_VARIABLE.equals(aDef.getMessageDataConsumerStrategy()) ||
	            IAeMessageDataStrategyNames.ELEMENT_VARIABLE.equals(aDef.getMessageDataConsumerStrategy()))
	      {
	         messageDataConsumer = new AeVariableMessageDataConsumer();
	      }
	      else if (IAeMessageDataStrategyNames.FROM_PARTS.equals(aDef.getMessageDataConsumerStrategy()))
	      {
	         messageDataConsumer = new AeFromPartsMessageDataConsumer();
	      }
	      else
	      {
	         messageDataConsumer = new AeNoopMessageDataConsumer();
	      }
	      return messageDataConsumer;
	}

	private IAeMessageDataProducer createMessageProducer(IAeMessageDataProducerDef aDef)
	   {
	      IAeMessageDataProducer messageDataProducer;

	      String producerStrategy = aDef.getMessageDataProducerStrategy();

	      if (IAeMessageDataStrategyNames.ELEMENT_VARIABLE.equals(producerStrategy) ||
	            IAeMessageDataStrategyNames.MESSAGE_VARIABLE.equals(producerStrategy))
	      {
	         messageDataProducer = new AeVariableMessageDataProducer();
	      }
	      else if (IAeMessageDataStrategyNames.TO_PARTS.equals(producerStrategy))
	      {
	         messageDataProducer = new AeToPartsMessageDataProducer();
	      }
	      else
	      {
	         // TODO (MF) might want to consider throwing an Error or RuntimeException here if there is no strategy set.
	         // This is something that we'll catch during static analysis but it's
	         // possible that it could break as a result of some WSDL change
	         messageDataProducer = new AeEmptyMessageDataProducer();
	      }
	      return messageDataProducer;
	   }
}