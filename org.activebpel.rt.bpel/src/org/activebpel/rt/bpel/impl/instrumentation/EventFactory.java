package org.activebpel.rt.bpel.impl.instrumentation;

import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.activebpel.rt.bpel.IAeEndpointReference;
import org.activebpel.rt.bpel.def.activity.AeActivityInvokeDef;
import org.activebpel.rt.bpel.def.activity.AeActivityReceiveDef;
import org.activebpel.rt.bpel.def.activity.AeActivityReplyDef;
import org.activebpel.rt.bpel.impl.AePartnerLink;
import org.activebpel.rt.bpel.impl.activity.AeActivityImpl;
import org.activebpel.rt.bpel.impl.activity.AeActivityInvokeImpl;
import org.activebpel.rt.bpel.impl.activity.AeActivityReceiveImpl;
import org.activebpel.rt.bpel.impl.activity.AeActivityReplyImpl;
import org.slasoi.common.eventschema.Entity;
import org.slasoi.common.eventschema.EventContextType;
import org.slasoi.common.eventschema.EventIdType;
import org.slasoi.common.eventschema.EventInstance;
import org.slasoi.common.eventschema.EventMetaDataType;
import org.slasoi.common.eventschema.EventNotifier;
import org.slasoi.common.eventschema.EventPayloadType;
import org.slasoi.common.eventschema.EventSource;
import org.slasoi.common.eventschema.EventTime;
import org.slasoi.common.eventschema.InteractionEventType;
import org.slasoi.common.eventschema.ObjectFactory;
import org.slasoi.common.eventschema.ServiceSourceType;


public class EventFactory {
	
	private static JAXBContext context = createContext();
	


	public static String createReplyEvent(AeActivityReplyImpl aeAct, String processStartOperation) {
		
		String output = null;
		
		AeActivityReplyImpl reply = (AeActivityReplyImpl)aeAct;
		
		AeActivityReplyDef def = (AeActivityReplyDef)reply.getDefinition();
		
		ObjectFactory factory = new ObjectFactory();
		
		//create event
		EventInstance event = factory.createEventInstance();
		
		EventIdType eventID = factory.createEventIdType();
		event.setEventID(eventID);
						
		//set eventID content
		long generatedEventID =UUID.randomUUID().getMostSignificantBits();
		event.getEventID().setID(generatedEventID);
		event.getEventID().setEventTypeID("ServiceOperationCallEndEvent");
		
		
		//set event timestamps
		
		EventContextType eventContext = factory.createEventContextType();
		event.setEventContext(eventContext);
		EventTime eventTime = factory.createEventTime();
		event.getEventContext().setTime(eventTime);
		
		try {
			event.getEventContext().getTime().setCollectionTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
			event.getEventContext().getTime().setTimestamp(new Date().getTime());
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//set event notifier
		
		EventNotifier eventNotifier = factory.createEventNotifier();
		event.getEventContext().setNotifier(eventNotifier);
		
		event.getEventContext().getNotifier().setName("sla@soi_bpel_instrumentation");

		InetAddress address=null;
		String ip = null;
		try {
			address = InetAddress.getLocalHost();
			ip = address.getHostAddress();
			event.getEventContext().getNotifier().setIP(ip);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
						
		long port = 8080;
		event.getEventContext().getNotifier().setPort(port);
		
		//set source information
		
		AePartnerLink plink = aeAct.findPartnerLink(def.getPartnerLink());
		IAeEndpointReference myEPR = plink.getMyReference();
		IAeEndpointReference yourEPR = plink.getPartnerReference();
		
		EventSource eventSource = factory.createEventSource();
		event.getEventContext().setSource(eventSource);
		ServiceSourceType serviceSource = factory.createServiceSourceType();
		event.getEventContext().getSource().setSwServiceLayerSource(serviceSource);
		Entity sender = factory.createEntity();
		event.getEventContext().getSource().getSwServiceLayerSource().setSender(sender);
		
		Entity receiver = factory.createEntity();
		event.getEventContext().getSource().getSwServiceLayerSource().setReceiver(receiver);
		
		String sourceEprString = aeAct.getProcess().getEngine().getURNResolver().getURL(myEPR.getAddress());
		
		String processName = aeAct.getProcess().getName().getLocalPart();

		event.getEventContext().getSource().getSwServiceLayerSource().getSender().setName(processName);
		event.getEventContext().getSource().getSwServiceLayerSource().getSender().setIp(ip);
		event.getEventContext().getSource().getSwServiceLayerSource().getSender().setPort(port);
		event.getEventContext().getSource().getSwServiceLayerSource().getSender().setProcessID(String.valueOf(aeAct.getProcess().getProcessId()));
		event.getEventContext().getSource().getSwServiceLayerSource().getSender().setServiceEPR(sourceEprString);
		
		//set destination information
		
		String yourEPRName=null;
		if (yourEPR.getServiceName()==null) {
			yourEPRName = "Anonymous";
		}
		
		String destinationEprString = aeAct.getProcess().getEngine().getURNResolver().getURL(yourEPR.getAddress());
		
		event.getEventContext().getSource().getSwServiceLayerSource().getReceiver().setName(yourEPRName);
		event.getEventContext().getSource().getSwServiceLayerSource().getReceiver().setIp(yourEPR.getAddress());
		event.getEventContext().getSource().getSwServiceLayerSource().getReceiver().setPort(8080);
		event.getEventContext().getSource().getSwServiceLayerSource().getReceiver().setServiceEPR(destinationEprString);
		
		AeActivityReplyImpl replyActivity = (AeActivityReplyImpl)aeAct;
		String opName = ((AeActivityReplyDef)replyActivity.getDefinition()).getOperation();
		
		EventPayloadType eventPayload = factory.createEventPayloadType();
		event.setEventPayload(eventPayload);
		InteractionEventType ievent = factory.createInteractionEventType();
		event.getEventPayload().setInteractionEvent(ievent);
		
		event.getEventPayload().getInteractionEvent().setOperationID(generatedEventID);
		event.getEventPayload().getInteractionEvent().setOperationName(opName);
		event.getEventPayload().getInteractionEvent().setStatus("RES-A");
		
//		ArgumentList arguments = factory.createArgumentList();
//		
//		IAeVariable var = aeAct.findVariable(def.getVariable());
//		
//		if (var == null) System.out.println("Problem with variable extraction.");
//		
//		if (var.isElement() && var.hasData()) {
//			//Variabile e' di tipo element
//			//System.out.println("**************VARIABLE IS ELEMENT*************");
//		}
//		else if (var.isMessageType() && var.hasMessageData()) {
//			//Variabile e' di tipo message
//		   //System.out.println("**************VARIABLE IS MESSAGE*************");
//			IAeMessageData message = null;
//			try {
//				message = var.getMessageData();
//			//	System.out.println("Message: " + message.toString());
//				Iterator i = message.getPartNames();
//				while (i.hasNext()) {
//					String name = (String)i.next();
//				//	System.out.println("Part name: " + name);
//					Object o = message.getData(name);
//				//	System.out.println(o.getClass());
//					ArgumentType argument = factory.createArgumentType();
//					SimpleArgument s = null;
//					
//					if (o instanceof String) {
//						s = factory.createSimpleArgument();
//						s.setArgName(name);
//						s.setDirection("OUT");
//						s.setArgType("String");
//						s.setValue((String)o);
//					}
//					else if (o instanceof Integer) {
//						s = factory.createSimpleArgument();
//						s.setArgName(name);
//						s.setDirection("OUT");
//						s.setArgType("Integer");
//						s.setValue((Integer)o);
//					}
//					else if (o instanceof DocumentImpl){
//						
//					//	System.out.println("Starting document treatment.");
//						s = factory.createSimpleArgument();
//						
//						DocumentImpl doc = (DocumentImpl)o;								
//						String rootXML = convertXMLToString(doc);
//					//	System.out.println("rootXML is: " + rootXML);
//
//						s.setArgName(doc.getDocumentElement().getNodeName());
//						s.setDirection("OUT");
//						s.setValue("");
//						s.setArgType("String");							
//				}
//					
//				if (s!=null) {
//					argument.setSimple(s);
//					arguments.getArgument().add(argument);
//				}
//
//					
//					
//			}
//			} catch (AeUninitializedVariableException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}
//		else if (var.isType()) {
//			//Variabile e' di tipo type
//		//	System.out.println("**************VARIABLE IS TYPE*************");
//		}
//		
//		event.getEventPayload().getInteractionEvent().setParameters(arguments);
		
		EventMetaDataType meta1 = factory.createEventMetaDataType();
		EventMetaDataType meta2 = factory.createEventMetaDataType();
		
		
		meta1.setKey("eventSourceDefinitionId");
		String defID = aeAct.getProcess().getName().getLocalPart() + "_" + processStartOperation + "_SOC_Def";
		meta1.setValue(defID);
		
		meta2.setKey("eventSourceInstanceId");
		meta2.setValue(String.valueOf(aeAct.getProcess().getProcessId()));
		
		event.getEventMetadata().add(meta1);
		event.getEventMetadata().add(meta2);
		
		// set report time
		try {
			event.getEventContext().getTime().setReportTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			output = eventMarshaller(event);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
		return output;
	}
	
	/**
	 * Pointcut per gestire l'emissione della variabile di output della invoke 
	 * @param aeAct
	 */
	

	public static String createInvokeEvent(AeActivityImpl aeAct, boolean response, String processStartOperation) {
		
		String output = null;
		
		AeActivityInvokeImpl reply = (AeActivityInvokeImpl)aeAct;
		
		AeActivityInvokeDef def = (AeActivityInvokeDef)reply.getDefinition();
		
		ObjectFactory factory = new ObjectFactory();
		
		//create event
		EventInstance event = factory.createEventInstance();
		
		EventIdType eventID = factory.createEventIdType();
		event.setEventID(eventID);
						
		//set eventID content
		long generatedEventID =UUID.randomUUID().getMostSignificantBits();
		event.getEventID().setID(generatedEventID);
		
		if (response){
			event.getEventID().setEventTypeID("ReferenceOperationCallEndEvent");
		}
		else{
			event.getEventID().setEventTypeID("ReferenceOperationCallStartEvent");
		}

		//set event timestamps
		
		EventContextType eventContext = factory.createEventContextType();
		event.setEventContext(eventContext);
		EventTime eventTime = factory.createEventTime();
		event.getEventContext().setTime(eventTime);
		
		try {
			event.getEventContext().getTime().setCollectionTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
			event.getEventContext().getTime().setTimestamp(new Date().getTime());
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		
		//set event notifier
		
		EventNotifier eventNotifier = factory.createEventNotifier();
		event.getEventContext().setNotifier(eventNotifier);
		
		event.getEventContext().getNotifier().setName("sla@soi_bpel_instrumentation");

		InetAddress address=null;
		String ip = null;
		try {
			address = InetAddress.getLocalHost();
			ip = address.getHostAddress();
			event.getEventContext().getNotifier().setIP(ip);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
						
		long port = 8080;
		event.getEventContext().getNotifier().setPort(port);
		
		
		if (response) {
			//set source information
			
			AePartnerLink plink = aeAct.findPartnerLink(def.getPartnerLink());
			IAeEndpointReference myEPR = plink.getMyReference();
			IAeEndpointReference yourEPR = plink.getPartnerReference();
			
			EventSource eventSource = factory.createEventSource();
			event.getEventContext().setSource(eventSource);
			ServiceSourceType serviceSource = factory.createServiceSourceType();
			event.getEventContext().getSource().setSwServiceLayerSource(serviceSource);
			Entity sender = factory.createEntity();
			event.getEventContext().getSource().getSwServiceLayerSource().setSender(sender);
			
			Entity receiver = factory.createEntity();
			event.getEventContext().getSource().getSwServiceLayerSource().setReceiver(receiver);
			
			String sourceEprString = aeAct.getProcess().getEngine().getURNResolver().getURL(yourEPR.getAddress());
			
			String processName = aeAct.getProcess().getName().getLocalPart();
	
			String yourEPRName = null;
			
			if (yourEPR.getServiceName() == null) {
				yourEPRName = "Anonymous";
			}
			
			event.getEventContext().getSource().getSwServiceLayerSource().getSender().setName(yourEPRName);
			event.getEventContext().getSource().getSwServiceLayerSource().getSender().setIp(yourEPR.getAddress());
			event.getEventContext().getSource().getSwServiceLayerSource().getSender().setPort(8080);
			event.getEventContext().getSource().getSwServiceLayerSource().getSender().setServiceEPR(sourceEprString);
			
			//set destination information
			
			String destinationEprString = aeAct.getProcess().getEngine().getURNResolver().getURL(myEPR.getAddress());			
			
			event.getEventContext().getSource().getSwServiceLayerSource().getReceiver().setName(processName);
			event.getEventContext().getSource().getSwServiceLayerSource().getReceiver().setIp(ip);
			event.getEventContext().getSource().getSwServiceLayerSource().getReceiver().setPort(port);
			event.getEventContext().getSource().getSwServiceLayerSource().getReceiver().setProcessID(String.valueOf(aeAct.getProcess().getProcessId()));
			event.getEventContext().getSource().getSwServiceLayerSource().getReceiver().setServiceEPR(destinationEprString);
	
		}
		else {

			//set source information
			
			AePartnerLink plink = aeAct.findPartnerLink(def.getPartnerLink());
			IAeEndpointReference myEPR = plink.getMyReference();
			IAeEndpointReference yourEPR = plink.getPartnerReference();
			
			EventSource eventSource = factory.createEventSource();
			event.getEventContext().setSource(eventSource);
			ServiceSourceType serviceSource = factory.createServiceSourceType();
			event.getEventContext().getSource().setSwServiceLayerSource(serviceSource);
			Entity sender = factory.createEntity();
			event.getEventContext().getSource().getSwServiceLayerSource().setSender(sender);
			
			Entity receiver = factory.createEntity();
			event.getEventContext().getSource().getSwServiceLayerSource().setReceiver(receiver);
			
			String sourceEprString = aeAct.getProcess().getEngine().getURNResolver().getURL(myEPR.getAddress());
			
			String processName = aeAct.getProcess().getName().getLocalPart();

			event.getEventContext().getSource().getSwServiceLayerSource().getSender().setName(processName);
			event.getEventContext().getSource().getSwServiceLayerSource().getSender().setIp(ip);
			event.getEventContext().getSource().getSwServiceLayerSource().getSender().setPort(port);
			event.getEventContext().getSource().getSwServiceLayerSource().getSender().setProcessID(String.valueOf(aeAct.getProcess().getProcessId()));
			event.getEventContext().getSource().getSwServiceLayerSource().getSender().setServiceEPR(sourceEprString);
			
			//set destination information
			
			String destinationEprString = aeAct.getProcess().getEngine().getURNResolver().getURL(yourEPR.getAddress());	
			
			String yourEPRName = null;
			if (yourEPR.getServiceName() == null) {
				yourEPRName = "Anonymous";
			}
			
			event.getEventContext().getSource().getSwServiceLayerSource().getReceiver().setName(yourEPRName);
			event.getEventContext().getSource().getSwServiceLayerSource().getReceiver().setIp(yourEPR.getAddress());
			event.getEventContext().getSource().getSwServiceLayerSource().getReceiver().setPort(8080);
			event.getEventContext().getSource().getSwServiceLayerSource().getReceiver().setServiceEPR(destinationEprString);
			
		}
		

		AeActivityInvokeImpl invokeActivity = (AeActivityInvokeImpl)aeAct;
		String opName = ((AeActivityInvokeDef)invokeActivity.getDefinition()).getOperation();
		
		EventPayloadType eventPayload = factory.createEventPayloadType();
		event.setEventPayload(eventPayload);
		InteractionEventType ievent = factory.createInteractionEventType();
		event.getEventPayload().setInteractionEvent(ievent);
		
		event.getEventPayload().getInteractionEvent().setOperationID(generatedEventID);
		event.getEventPayload().getInteractionEvent().setOperationName(opName);
		
		if (response){
			event.getEventPayload().getInteractionEvent().setStatus("RES-A");
		}
		else{
			event.getEventPayload().getInteractionEvent().setStatus("REQ-A");
		}
		
//		ArgumentList arguments = factory.createArgumentList();
//		
//		
//		IAeVariable var;
//		
//		if (response){
//			var = aeAct.findVariable(def.getOutputVariable());
//		}
//		else{
//			var = aeAct.findVariable(def.getInputVariable());
//		}
//		
//		if (var == null) System.out.println("Problem with variable extraction.");
//		
//		if (var.isElement() && var.hasData()) {
//			//System.out.println("**************VARIABLE IS ELEMENT*************");
//		}
//		else if (var.isMessageType() && var.hasMessageData()) {
//		   //System.out.println("**************VARIABLE IS MESSAGE*************");
//			IAeMessageData message = null;
//			try {
//				message = var.getMessageData();
//				Iterator i = message.getPartNames();
//				while (i.hasNext()) {
//					String name = (String)i.next();
//				//	System.out.println("Part name: " + name);
//					Object o = message.getData(name);
//				//	System.out.println(o.getClass());
//					ArgumentType argument = factory.createArgumentType();
//					SimpleArgument s = null;		
//					if (o instanceof String) {
//						s = factory.createSimpleArgument();
//						s.setArgName(name);
//						s.setDirection("OUT");
//						s.setArgType("String");
//						s.setValue((String)o);
//					}
//					else if (o instanceof Integer) {
//						s = factory.createSimpleArgument();
//						s.setArgName(name);
//						s.setDirection("OUT");
//						s.setArgType("Integer");
//						s.setValue((Integer)o);
//					}
//					else if (o instanceof DocumentImpl){
//						
//					//	System.out.println("Starting document treatment.");
//						s = factory.createSimpleArgument();
//						
//						DocumentImpl doc = (DocumentImpl)o;								
//						String rootXML = convertXMLToString(doc);
//					//	System.out.println("rootXML is: " + rootXML);
//
//						s.setArgName(doc.getDocumentElement().getNodeName());
//						s.setDirection("OUT");
//						s.setValue("");
//						s.setArgType("String");							
//				}
//					
//				if (s!=null) {
//					argument.setSimple(s);
//					arguments.getArgument().add(argument);
//				}
//
//					
//					
//			}
//			} catch (AeUninitializedVariableException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}
//		else if (var.isType()) {
//			//Variabile e' di tipo type
//		//	System.out.println("**************VARIABLE IS TYPE*************");
//		}
//		
//		event.getEventPayload().getInteractionEvent().setParameters(arguments);
		
		EventMetaDataType meta1 = factory.createEventMetaDataType();
		EventMetaDataType meta2 = factory.createEventMetaDataType();
		
		meta1.setKey("eventSourceDefinitionId");
		
		String defID =  aeAct.getProcess().getName().getLocalPart() + "_" + processStartOperation + "_SOC_Def";
		meta1.setValue(defID);
		
		meta2.setKey("eventSourceInstanceId");
		meta2.setValue(String.valueOf(aeAct.getProcess().getProcessId()));
		
		event.getEventMetadata().add(meta1);
		event.getEventMetadata().add(meta2);
		
		// set report time
		try {
			event.getEventContext().getTime().setReportTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			output = eventMarshaller(event);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
		return output;
	}
	

	public static String createReceiveEvent(AeActivityImpl aeAct,String processStartOperation) {
		
		String output = null;
	
		AeActivityReceiveDef def = (AeActivityReceiveDef)aeAct.getDefinition();
		
		ObjectFactory factory = new ObjectFactory();
		
		//create event
		EventInstance event = factory.createEventInstance();
		
		EventIdType eventID = factory.createEventIdType();
		event.setEventID(eventID);
						
		//set eventID content
		long generatedEventID =UUID.randomUUID().getMostSignificantBits();
		event.getEventID().setID(generatedEventID);
		
		String eventIDType = "ServiceOperationCallStartEvent";
		event.getEventID().setEventTypeID(eventIDType);
		
		
		//set event timestamps
		
		EventContextType eventContext = factory.createEventContextType();
		event.setEventContext(eventContext);
		EventTime eventTime = factory.createEventTime();
		event.getEventContext().setTime(eventTime);
		
		try {
			event.getEventContext().getTime().setCollectionTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
			event.getEventContext().getTime().setTimestamp(new Date().getTime());
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//set event notifier
		
		EventNotifier eventNotifier = factory.createEventNotifier();
		event.getEventContext().setNotifier(eventNotifier);
		
		event.getEventContext().getNotifier().setName("sla@soi_bpel_instrumentation");
						
					
		InetAddress address=null;
		String ip = null;
		try {
			address = InetAddress.getLocalHost();
			ip = address.getHostAddress();
			event.getEventContext().getNotifier().setIP(ip);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
						
		long port = 8080;
		event.getEventContext().getNotifier().setPort(port);
		
		//set source information
		
		AePartnerLink plink = aeAct.findPartnerLink(def.getPartnerLink());
		IAeEndpointReference myEPR = plink.getMyReference();
		IAeEndpointReference yourEPR = plink.getPartnerReference();
		
		EventSource eventSource = factory.createEventSource();
		event.getEventContext().setSource(eventSource);
		ServiceSourceType serviceSource = factory.createServiceSourceType();
		event.getEventContext().getSource().setSwServiceLayerSource(serviceSource);
		Entity sender = factory.createEntity();
		event.getEventContext().getSource().getSwServiceLayerSource().setSender(sender);
		Entity receiver = factory.createEntity();
		event.getEventContext().getSource().getSwServiceLayerSource().setReceiver(receiver);
		
		String destEprString = aeAct.getProcess().getEngine().getURNResolver().getURL(myEPR.getAddress());
		
		String sourceEprString = aeAct.getProcess().getEngine().getURNResolver().getURL(yourEPR.getAddress());
		
		String processName = aeAct.getProcess().getName().getLocalPart();
		
		String yourEPRName = null;
		
		if (yourEPR.getServiceName() == null) {
			yourEPRName = "Anonymous";
		}
		
		event.getEventContext().getSource().getSwServiceLayerSource().getSender().setName(yourEPRName);
		event.getEventContext().getSource().getSwServiceLayerSource().getSender().setIp(yourEPR.getAddress());
		event.getEventContext().getSource().getSwServiceLayerSource().getSender().setPort(8080);
		event.getEventContext().getSource().getSwServiceLayerSource().getSender().setServiceEPR(sourceEprString);
		
		//set destination information
		
		event.getEventContext().getSource().getSwServiceLayerSource().getReceiver().setName(processName);
		event.getEventContext().getSource().getSwServiceLayerSource().getReceiver().setIp(ip);
		event.getEventContext().getSource().getSwServiceLayerSource().getReceiver().setPort(port);
		event.getEventContext().getSource().getSwServiceLayerSource().getReceiver().setProcessID(String.valueOf(aeAct.getProcess().getProcessId()));
		event.getEventContext().getSource().getSwServiceLayerSource().getReceiver().setServiceEPR(destEprString);
		
		AeActivityReceiveImpl receive = (AeActivityReceiveImpl)aeAct;
		String opName = ((AeActivityReceiveDef)receive.getDefinition()).getOperation();
		
		EventPayloadType eventPayload = factory.createEventPayloadType();
		event.setEventPayload(eventPayload);
		InteractionEventType ievent = factory.createInteractionEventType();
		event.getEventPayload().setInteractionEvent(ievent);
		
		event.getEventPayload().getInteractionEvent().setOperationID(generatedEventID);
		event.getEventPayload().getInteractionEvent().setOperationName(opName);
		event.getEventPayload().getInteractionEvent().setStatus("REQ-A");
		
//		ArgumentList arguments = factory.createArgumentList();
//		
//		IAeVariable var = aeAct.findVariable(def.getVariable());
//		
//		if (var == null) System.out.println("Problem with variable extraction.");
//
//		
//		if (var.isElement() && var.hasData()) {
//			//Variabile e' di tipo element
//		//	System.out.println("**************VARIABLE IS ELEMENT*************");
//			
//			
//		}
//		else if (var.isMessageType() && var.hasMessageData()) {
//		//	System.out.println("**************VARIABLE IS MESSAGE*************");
//			IAeMessageData message = null;
//			try {
//				message = var.getMessageData();
//				Iterator i = message.getPartNames();
//				while (i.hasNext()) {
//					String name = (String)i.next();
//				//	System.out.println("Part name: " + name);
//					Object o = message.getData(name);
//				//	System.out.println(o.getClass());
//					ArgumentType argument = factory.createArgumentType();
//					SimpleArgument s = null;
//					
//					if (o instanceof String) {
//						s = factory.createSimpleArgument();
//						s.setArgName(name);
//						s.setDirection("IN");
//						s.setArgType("String");
//						s.setValue((String)o);
//					}
//					else if (o instanceof Integer) {
//						s = factory.createSimpleArgument();
//						s.setArgName(name);
//						s.setDirection("IN");
//						s.setArgType("Integer");
//						s.setValue((Integer)o);
//					}
//					else if (o instanceof DocumentImpl){
//						
//				//		System.out.println("Starting document treatment.");
//						s = factory.createSimpleArgument();
//						
//						DocumentImpl doc = (DocumentImpl)o;								
//						String rootXML = convertXMLToString(doc);
//			//			System.out.println("rootXML is: " + rootXML);
//
//						s.setArgName(doc.getDocumentElement().getNodeName());
//						s.setDirection("IN");
//						s.setValue("");
//						s.setArgType("String");							
//				}
//					
//				if (s!=null) {
//					argument.setSimple(s);
//					arguments.getArgument().add(argument);
//				}
//
//					
//					
//			}
//			} catch (AeUninitializedVariableException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}
//		else if (var.isType()) {
//			//Variabile e' di tipo type
//		//	System.out.println("**************VARIABLE IS TYPE*************");
//		}
//		
//		event.getEventPayload().getInteractionEvent().setParameters(arguments);
		
		
		EventMetaDataType meta1 = factory.createEventMetaDataType();
		EventMetaDataType meta2 = factory.createEventMetaDataType();
		
		meta1.setKey("eventSourceDefinitionId");
		String defID = processName + "_" + processStartOperation + "_SOC_Def";
		meta1.setValue(defID);
					
		meta2.setKey("eventSourceInstanceId");
		meta2.setValue(String.valueOf(aeAct.getProcess().getProcessId()));
		
		event.getEventMetadata().add(meta1);
		event.getEventMetadata().add(meta2);
		
		// set report time
		try {
			event.getEventContext().getTime().setReportTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			output = eventMarshaller(event);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
		return output;
	}
	
private static String eventMarshaller(EventInstance event) throws JAXBException{
		
		Marshaller marsh = null;
		try {
			marsh = context.createMarshaller();
			marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	
		JAXBElement<EventInstance> instance = (new ObjectFactory()).createEvent(event);
		
		StringWriter output = new StringWriter();
		
	    marsh.marshal(instance, output);
		
		String message = output.toString();	
		
		return message;
	}

private static JAXBContext createContext() {
	// TODO Auto-generated method stub
	
	JAXBContext slasoiContext = null;
	try {
		slasoiContext = JAXBContext.newInstance("org.slasoi.common.eventschema");
	} catch (JAXBException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return slasoiContext;
}

}
