package it.eng.binding.impl;

import it.eng.binding.IBinderInvocation;
import it.eng.ws.WsdlLoadingException;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.activebpel.rt.bpel.AeWSDLDefHelper;
import org.activebpel.rt.bpel.IAeEndpointReference;
import org.activebpel.rt.bpel.IAeVariable;
import org.activebpel.rt.bpel.impl.AeBusinessProcess;
import org.activebpel.rt.bpel.impl.AePartnerLink;
import org.activebpel.rt.bpel.impl.AeUninitializedVariableException;
import org.activebpel.rt.bpel.impl.queue.AeInvoke;
import org.activebpel.rt.wsdl.IAeContextWSDLProvider;
import org.activebpel.rt.wsdl.def.AeBPELExtendedWSDLDef;
import org.activebpel.wsio.AeWebServiceMessageData;
import org.activebpel.wsio.IAeWebServiceResponse;
import org.activebpel.wsio.invoke.AeInvokeResponse;
import org.activebpel.wsio.invoke.IAeInvoke;
import org.activebpel.wsio.invoke.IAeInvokeHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *@author Paolo Zampognaro Engineering Ingegneria Informatica spa
 **/
public class EngInvokeHandler implements IAeInvokeHandler {

	public static Logger logger = Logger
			.getLogger("it.eng.binding.impl.EngInvokeHandler");

	public EngInvokeHandler() {
	}

	public IAeWebServiceResponse handleInvoke(IAeInvoke invoke, String queryData) {
		System.out.println("\n");
		logger.entering(this.getClass().getName(), "handleInvoke",
				new Object[] { invoke, queryData });
		logger.info("*********** --> ACCESS TO ENG INVOKE HANDLER...");
		logger.info("Called from process "
				+ invoke.getProcessName().getLocalPart());

		/*
		 * Step. 1 Retrieving, from invoke variable, the information to pass to
		 * the Binder. (This information are necessary to the Binder to retrieve
		 * specific endpoint). Such information are: 
		 * 1. process identifier and partner role: to identify a specific set o binding rules. 
		 * 2. input message variables: they are needed to identify a specific binding rule.
		 */
		String processID = invoke.getProcessName().getLocalPart();
		
		AeBusinessProcess processInstance = ((AeInvoke) invoke).getProcessInstance();
		String partnerLinkPath = invoke.getPartnerLink();
		AePartnerLink partnerLink = processInstance.findProcessPartnerLink(partnerLinkPath);
		String partnerRoleName = partnerLink.getPartnerRole();
		
		IBinderInvocation binder = Binder.init();

//		/*
//		 * Below: access to the message which started the process instance.
//		 */
//		logger.info("process.isExecuting() "
//				+ processInstance.isExecuting());
//		logger.info("process.getProcessInitiator() "
//				+ processInstance.getProcessInitiator());
//		AeInboundReceive startMessage=processInstance.getCreateMessageCopy();
//		Iterator<String> partNames = startMessage.getMessageData().getPartNames();
//
//		 for (; partNames.hasNext();) {
//			String partName = (String) partNames.next();
//			logger.info("partName " + partName);
//			logger.info("partValue "+ startMessage.getMessageData().getData(partName));
//		}
//		 
//		 /*
//		  * Below: access to the input message for this invoke activity.
//		  */
//		 Set keys = invoke.getInputMessageData().getMessageData().keySet();
//		 for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
//			String partName = (String) iterator.next();
//			logger.info("partName " + partName);
//			logger.info("partValue "+ startMessage.getMessageData().getData(partName));
//		}
		 
		 /*
		  * Below: access to the global variables which hold the process status.
		  * Note: Actually the process definition foreseen a single global variable named customerID 
		  */
		Map<String, String> processStatus = new HashMap<String, String>();
		Iterator it = processInstance.getVariableContainer().iterator();
		for (; it.hasNext();) {
			IAeVariable var = (IAeVariable) it.next();
			if (var.isElement()) {
				try {
					processStatus.put(var.getName(), var.getElementData()
							.getTextContent());

				} catch (AeUninitializedVariableException e) {
					throw new RuntimeException(e);
				}
				logger.info("PROCESS_STATUS.:"+new TreeMap<String, String>(processStatus).toString());
			}
		}
		 

		/*
		 * Step. 2 Builds DOM Documents (one for each part of the input message)
		 * from information contained in the BPEL invoke.
		 */
		Map reqMsgParts = invoke.getInputMessageData().getMessageData();
		int reqMsgPartsNumber = reqMsgParts.size();
		logger.info("Request message part number.: " + reqMsgPartsNumber);
		Set keySet = reqMsgParts.keySet();
		Iterator partsNamesIterator = keySet.iterator();
		// The Document[] for the parts.
		Document[] reqMsgParts_ArrayOfDoc = new Document[reqMsgPartsNumber];
		int partsCounter = 0;
		Document partDoc = null;
		while (partsNamesIterator.hasNext()) {
			String partName = (String) partsNamesIterator.next();
			Object object = reqMsgParts.get(partName);
			// 1. object is a Document
			if (object instanceof Document) {
				partDoc = (Document) object;
				System.out.println(Util.domToString(partDoc));
			} else {
				// 2. object is a simple type (e.g. String, int, float)
				// Build the Document for the part..
				partDoc = Util.getNewW3CDocument();
				// root element must have the name of the part.
				Element root = partDoc.createElement(partName);
				partDoc.appendChild(root);
				root.appendChild(partDoc.createTextNode(object.toString()));

			}
			reqMsgParts_ArrayOfDoc[partsCounter] = partDoc;
			partsCounter++;
		}

		/*
		 * Step. 3 Delegate invocation to the Binder...
		 */
		IAeEndpointReference endpointReference = ((AeInvoke) invoke)
				.getPartnerReference();

		String pnrLinkEprAddr = endpointReference.getAddress();
		IAeContextWSDLProvider contextProvider = ((AeInvoke) invoke)
				.getProcessInstance().getProcessPlan();
		AeBPELExtendedWSDLDef wsdl = AeWSDLDefHelper
				.getWSDLDefinitionForPortType(contextProvider, invoke
						.getPortType());
		String wsdlBaseURI = wsdl.getLocator().getBaseURI();
		InputStream wsdlInputStream = wsdl.getLocator().getBaseInputSource()
				.getByteStream();
		/*
		 * Il nome dell'elemento <wsdl:service> e <wsdl:port> è recuperato dal
		 * pdd che, infatti, serve a descrivere le binding info
		 * dell'endpointReference di un partnerRole. Esse sono descritte usando
		 * ws-addressing la cui sintassi di base è la seguente:
		 * <wsa:EndpointReference> <wsa:Address>s:anyURI</wsa:Address>
		 * <wsa:ServiceName PortName="portname">s:Service </wsa:ServiceName>
		 * </wsa:EndpointReference>
		 */
		String serviceName = endpointReference.getServiceName().getLocalPart();
		String portName = endpointReference.getServicePort();
		String operationName = invoke.getOperation();
		QName portTypeName = invoke.getPortType();

		logger.info("serviceName.: " + serviceName);
		logger.info("portName.: " + portName);
		logger.info("operationName.: " + operationName);
		logger.info("portTypeName.: " + portTypeName.getLocalPart());

		Map<QName, Document[]> responseMessageMap;
		try {
			responseMessageMap = binder.invoke(processID, partnerRoleName,
					processStatus, reqMsgParts_ArrayOfDoc, pnrLinkEprAddr,
					wsdlBaseURI, wsdlInputStream, serviceName, portName, operationName, portTypeName);
		} catch (WsdlLoadingException e) {
			throw new RuntimeException(e.getMessage());
		}
		QName respMsgQname = responseMessageMap.keySet().iterator().next();
		Document[] respMsgParts_ArrayOfDoc = responseMessageMap
				.get(respMsgQname);

		/*
		 * Step. 4 Build the response message in the format required by
		 * activBPEL.
		 */
		AeInvokeResponse respMsg = new AeInvokeResponse();

		Map<String, Document> respMsgParts_MapOfDoc = new HashMap<String, Document>();
		logger.info("Response message part number.: "
				+ respMsgParts_ArrayOfDoc.length);
		for (int i = 0; i < respMsgParts_ArrayOfDoc.length; i++) {
			String partName = respMsgParts_ArrayOfDoc[i].getDocumentElement()
					.getLocalName();
			respMsgParts_MapOfDoc.put(partName, respMsgParts_ArrayOfDoc[i]);
			logger.info("Document associated to the part " + partName);
			System.out.println(Util.domToString(respMsgParts_ArrayOfDoc[i]));
		}
		logger.info("***********--> EXITING FROM ENG INVOKER HANDLER\n\n");
		AeWebServiceMessageData msgData = new AeWebServiceMessageData(
				respMsgQname, respMsgParts_MapOfDoc);
		respMsg.setMessageData(msgData);

		/*
		 * Step. 4 Return the response message back to the calling process
		 */
		logger.exiting(this.getClass().getName(), "handleInvoke", respMsg);
		return respMsg;   
	}

}
