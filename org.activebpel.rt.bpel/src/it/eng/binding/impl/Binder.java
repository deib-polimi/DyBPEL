package it.eng.binding.impl;

import it.eng.binding.IAeAbstractBindingRule;
import it.eng.binding.IAeBindingRule;
import it.eng.binding.IBinder;
import it.eng.binding.IBinderInvocation;
import it.eng.ws.Operation;
import it.eng.ws.Service;
import it.eng.ws.Wsdl;
import it.eng.ws.ri.WS;
import it.eng.ws.WsdlLoadingException;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Persistence;
import javax.persistence.Transient;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.SOAPException;

import org.w3c.dom.Document;

/**
 *@author Paolo Zampognaro Engineering Ingegneria Informatica spa
 * 
 *         Class responsible to manage the binding rules (@see
 *         it.eng.binding.impl.AeBindingRule) of the invoke activities of all
 *         processes after they have been deployed. It is also responsible to
 *         execute abstract invoke activities (i.e. invoke activity with no
 *         associated binding information) and invoke activities for which
 *         different binding information have been registered with respect to
 *         that ones provided at deployment time.
 **/
@Entity 
public class Binder implements IBinderInvocation, IBinder {  
	public static Logger logger = Logger
			.getLogger("it.eng.binding.impl.Binder");

	@SuppressWarnings("unused")   
	@Id
	@Column(name = "BINDER_ID")
	private int id = 0;
	private static EntityManagerFactory emf;
	private static EntityManager em;
	private static String pu = "puSWSToplink";

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "binder")
	@MapKey(name = "partnerRoleKey")
	private Map<String, ProcessPartnerRole> processPartnerRoles;

	@Transient
	DocumentBuilderFactory documentBuilderFactory = null;

	protected Binder() {
		processPartnerRoles = new HashMap<String, ProcessPartnerRole>();
		documentBuilderFactory = DocumentBuilderFactory.newInstance();
	}

	/**
	 * Returns the unique instance of the Binder if it already exists.Create the
	 * instance and return otherwise.
	 */
	public static Binder init() {
		emf = Persistence.createEntityManagerFactory(pu);
		em = emf.createEntityManager();
		em.getTransaction().begin();
		Binder bd = em.find(Binder.class, 0);

		if (bd != null) {
			em.getTransaction().commit();
			return bd;
		} else {
			bd = new Binder();
			em.persist(bd);
			em.getTransaction().commit();
			return bd;
		}
	}

	public Map<QName, Document[]> invoke(String processID,
			String partnerRoleName, Map processVariables,
			Document[] reqMsgParts, String pnrLinkEprAddr, String wsdlBaseURI,
			InputStream wsdlInputStream, String serviceName, String portName,
			String operationName, QName portTypeName)
			throws WsdlLoadingException {
		logger.entering(this.getClass().getName(), "invoke", new Object[] {
				processID, partnerRoleName, processVariables, reqMsgParts });
		System.out.println("\n\n");
		logger.info("*********** --> ACCESS TO THE BINDER...");


		if (invokeIsAbstract(pnrLinkEprAddr)) {
//			logger.info("*********** --> ABSTRACT INVOKE ACTIVITY...");
//
//			/*
//			 * It is needed to build the abstractService (SWS) used by
//			 * partnerLink of the invoke activity under execution.
//			 */
//			logger
//					.info("*********** --> ABSTRACT SERVICE CREATION STARTING...");
//			AbstractService as = SWS.createAbstractService(wsdlBaseURI,
//					wsdlInputStream, pnrLinkEprAddr, serviceName, portName,
//					operationName, portTypeName);
//			logger
//					.info("*********** --> ABSTRACT SERVICE CREATION FINISHED...");
//
//			/*
//			 * Abstract-Step 1 Retrieve SLAT associated to the abstract invoke
//			 */
//			logger.info("*********** --> RETRIEVING SLAT URL...");
//			AeBindingRule[] abRules = getPartnerRoleAbstractBindingRules(
//					processID, partnerRoleName);
//			AeBindingRule abRule = getAbstractRule(abRules, processVariables);
//			String slatURL = abRule.getSlatURL();
//
//			/*
//			 * Abstract-Step 2 Use SLAT to discover a list of compatible SLATs
//			 * from register
//			 */
//			logger.info("*********** --> DISCOVERING A LIST OF SLATs...");
//
//			/*
//			 * Abstract-Step 3 Select a SLAT from the list
//			 */
//			logger.info("*********** --> SELECTING A SLAT FROM LIST...");
//
//			/*
//			 * Abstract-Step 4 Achievement of a SLA via negotiation:get the EPR
//			 * information from the SLA
//			 */
//			logger.info("*********** --> NEGOTIATING A SLA...");
//
//			/*
//			 * Abstract-Step 5 Set the TODO Continue from here
//			 */
			return null;
		} else {
			/*
			 * <<Concrete>>Step 1 Get the binding rule for the specified
			 * processID,invokePath,processVariables if one exists. The binding info 
			 * coming from deployment will be used otherwise.
			 */
			Wsdl wsdl = null;
			Service s = null;
			Operation op = null;
			Document[] respMsgParts_ArrayOfDoc = null;
			
			logger.info("*********** --> RETRIEVING BINDING RULE...");
			AeBindingRule rule = getBindingRuleByPS(processID, partnerRoleName,
					processVariables);
			logger.info("*********** --> RETRIEVING FINISHED...");
			
			if (rule == null){
				logger.info("*********** --> NO BINDING RULE FOR ROLE "+partnerRoleName+" OF PROCESS "+processID);
				/*
				 * <<Concrete>>Step 2 Delegate invoke to ENG-API invoker...
				 */
				System.out.println("\n\n");
				logger.info("*********** --> WSDL LOADING...");
				wsdl = WS.loadWsdl(pnrLinkEprAddr);
				logger.info("***********--> WSDL LOADING FINISHED... \n\n");
				logger.info("serviceName: "+serviceName+" portName: "+portName+" operationName: "+operationName);
				s = wsdl.getService(serviceName);
				op = s.getPort(portName).getOperation(operationName);
				try {
					logger.info("***********--> INVOKING...");
					respMsgParts_ArrayOfDoc = op.invoke(reqMsgParts);
					logger.info("***********--> INVOKING FINISHED...");
					logger.info("***********--> EXIT FROM BINDER...\n\n");
				} catch (SOAPException e) {
					e.printStackTrace();
				}
			}
			else{
				logger.info("*********** --> BINDING RULE FOR ROLE "+partnerRoleName+" OF PROCESS "+processID);
				String wsdlURL = rule.getWsdlAddress();
				String serviceNameFromRule = rule.getServiceName();
				String portNameFromRule = rule.getPortName();
				/*
				 * <<Concrete>>Step 2 Delegate invoke to ENG-API invoker...
				 */
				System.out.println("\n\n");
				logger.info("*********** --> WSDL LOADING..."); 
				try {
					wsdl = WS.loadWsdl(wsdlURL); 

					logger.info("***********--> SERVICE NAME FROM RULE.:"
							+ serviceNameFromRule + "\n\n");
					logger.info("***********--> PORT NAME FROM RULE.:"
							+ portNameFromRule + "\n\n");
					s = wsdl.getService(serviceNameFromRule);
					op = s.getPort(portNameFromRule).getOperation(operationName);
					logger.info("***********--> WSDL LOADING FINISHED... \n\n");
					logger.info("***********--> INVOKING...");
					respMsgParts_ArrayOfDoc = op.invoke(reqMsgParts);
				} catch (WsdlLoadingException e) {
					e.printStackTrace();
				} catch (SOAPException e) {
					e.printStackTrace();
				}
				logger.info("***********--> INVOKING FINISHED...");
				logger.info("***********--> EXIT FROM BINDER...\n\n");		
			}
			QName respMsqQname = new QName(wsdl.getTargetNamespace(), op
					.getOutputMessage().getLocalName());
			Map<QName, Document[]> responseMessage = new HashMap<QName, Document[]>();
			logger.info("*****--> responseQname.:"+respMsqQname);
			responseMessage.put(respMsqQname, respMsgParts_ArrayOfDoc);
			return responseMessage;
		}
	}

	public void addPartnerRoleBindingRule(String processID,
			String partnerRoleName, IAeBindingRule bindingRule) {

		if (bindingRule == null)
			logger.info("BINDING_RULE is null!!");
		else {
			logger.info("PROCESSID.: " + processID);
			logger.info("PARTNER_ROLE_NAME.: " + partnerRoleName);
			logger.info("BINDING_RULE_ENDPOINT.: "
					+ bindingRule.getWsdlAddress());
			logger.info("BINDING_RULE_CONDITION.: "
					+ ((AeBindingRule) bindingRule).getConditionAsString());
		}
		addPartnerRoleRule(processID, partnerRoleName,
				(AeBindingRule) bindingRule);
	}

	public void addPartnerRoleAbstractBindingRule(String processID,
			String partnerRoleName, IAeAbstractBindingRule abstractBindingRule) {
		if (abstractBindingRule == null)
			logger.info("ABSTRACT BINDING_RULE is null!!");
		else {
			logger.info("PROCESSID.: " + processID);
			logger.info("PARTNER_ROLE_NAME.: " + partnerRoleName);
			logger.info("SLAT_URL.: " + abstractBindingRule.getSlatURL());
			logger.info("BINDING_RULE_CONDITION.: "
					+ ((AeBindingRule) abstractBindingRule)
							.getConditionAsString());
		}
		addPartnerRoleRule(processID, partnerRoleName,
				(AeBindingRule) abstractBindingRule);
	}

	public AeBindingRule[] getPartnerRoleBindingRules(String processID,
			String partnerRoleName) {
		logger.info("Sono nel Binder!!");
		em.getTransaction().begin();
		// - Retrieving of the partner role
		ProcessPartnerRole ppr = em.find(ProcessPartnerRole.class, processID
				+ partnerRoleName);
		if (ppr == null)
			throw new RuntimeException(
					"Unable to find the specified partner role !");
		// - Retrieving of binding rules
		AeBindingRule[] rulesArray = ppr.getBindingRules();
		logger.info("rulesArray.length --> " + rulesArray.length);
		logger.info("rulesArray[0].getConditionAsString()--> "
				+ rulesArray[0].getConditionAsString());
		logger.info("rulesArray[0].getCondition()--> "
				+ rulesArray[0].getCondition());
		em.getTransaction().commit();
		return rulesArray;
	}

	public AeBindingRule[] getPartnerRoleAbstractBindingRules(String processID,
			String partnerRoleName) {
		logger.info("Sono nel Binder!!");
		em.getTransaction().begin();
		// - Retrieving of the invoke activity
		ProcessPartnerRole ppr = em.find(ProcessPartnerRole.class, processID
				+ partnerRoleName);
		if (ppr == null)
			throw new RuntimeException(
					"Unable to find the specified invoke activity !");
		// - Retrieving of binding rules
		AeBindingRule[] rulesArray = ppr.getAbstractBindingRules();
		logger.info("rulesArray.length --> " + rulesArray.length);
		logger.info("rulesArray[0].getConditionAsString()--> "
				+ rulesArray[0].getConditionAsString());
		logger.info("rulesArray[0].getCondition()--> "
				+ rulesArray[0].getCondition());
		em.getTransaction().commit();
		return rulesArray;
	}

	public void removePartnerRoleRule(String processID, String partnerRoleName,
			Map<String, String> ruleCondition) {
		logger.info("Sono nel Binder!!");
		if (ruleCondition == null)
			logger.info("RULE_CONDITION is null!!");
		em.getTransaction().begin();
		// - Retrieving of partner role
		ProcessPartnerRole ppr = em.find(ProcessPartnerRole.class, processID
				+ partnerRoleName);
		if (ppr == null) {
			em.getTransaction().commit();
			throw new RuntimeException(
					"Unable to find the specified partner role !");
		}
		// - Retrieving of binding rule
		String conditionAsString = new TreeMap<String, String>(ruleCondition)
				.toString();
		logger.info("conditionAsString della request.: " + conditionAsString);
		AeBindingRule br = ppr.getRules().get(conditionAsString);
		if (br == null)
			throw new RuntimeException(
					"Unable to find the binding rule with the specified condition !");
		ppr.getRules().remove(conditionAsString);
		em.remove(br);
		em.getTransaction().commit();
	}

	/**
	 * Private method which states if the invoke activity under execution is
	 * abstract.
	 */
	private boolean invokeIsAbstract(String pnrLinkEprAddr) {
		return pnrLinkEprAddr.startsWith("urn:variableWS");
	}

	/**
	 * Private method which return the binding rule, of the specified partner role, 
	 * matching the process status.
	 * Return null if the specified partner role is null.
	 */
	private AeBindingRule getBindingRuleByPS(String processID,
			String partnerRoleName, Map<String, String> processVariables) {
		logger.entering(this.getClass().getName(), "getEndpoint", new Object[] {
				processID, partnerRoleName, processVariables });
		ProcessPartnerRole ppr = processPartnerRoles.get(processID
				+ partnerRoleName);
		if (ppr == null)
			return null;
		else {
			String processVariablesAsString = null;
			/*
			 * If the user has not specified values for all process status variables
			 * [e.g processVariablesAsString={customerID=, pippoID=}] then 
			 * it must be retrieved a rule with an empty map[e.g {}]
			 */

			boolean isEmpty=true;
			Set<String> keySet = processVariables.keySet();
			for(String key: keySet){
				String value = processVariables.get(key);
				logger.info("key.:"+key+" value.:"+value);
				if (!value.equals("")){
					isEmpty=false;
					break;
				}
			}
			if(!isEmpty){
				processVariablesAsString =  new TreeMap<String, String>(
						processVariables).toString();
			}
			else{
				processVariablesAsString="{}";
			}
			return ppr.getRules().get(processVariablesAsString);
		}
	}

	/**
	 * Private method which returns the abstract rule with specified condition.
	 */
	private AeBindingRule getAbstractRule(AeBindingRule[] abRules, Map condition) {
		for (int i = 0; i < abRules.length; i++) {
			if(abRules[i].getCondition().equals(condition))
				return abRules[i];
		}
		return null;
	}

	private void addPartnerRoleRule(String processID, String partnerRoleName,
			AeBindingRule bindingRule) {
		em.getTransaction().begin();
		// - Retrieving the partner role
		ProcessPartnerRole ppr = em.find(ProcessPartnerRole.class, processID
				+ partnerRoleName);
		if (ppr == null) {
			logger.info("PartnerRole is null");
			ppr = new ProcessPartnerRole(processID, partnerRoleName,
					(AeBindingRule) bindingRule, this);
			processPartnerRoles.put(ppr.getPartnerRoleKey(), ppr);
			//em.getTransaction().commit();
		} else{
			ppr.addRule(bindingRule);
			//em.getTransaction().commit();
		}
		em.getTransaction().commit();

	}
}
