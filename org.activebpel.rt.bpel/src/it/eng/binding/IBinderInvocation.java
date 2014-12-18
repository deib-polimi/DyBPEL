package it.eng.binding;

import it.eng.ws.WsdlLoadingException;

import java.io.InputStream;
import java.util.Map;

import javax.xml.namespace.QName;

import org.w3c.dom.Document;

/**
 *@author Paolo Zampognaro Engineering Ingegneria Informatica spa
 **/
public interface IBinderInvocation {

	/**
	 * 
	 * Invokes the component service referred by the BPEL invoke activity
	 * located at the specified path of the specified process and for a specific
	 * customer. Binding information of component service, provided at
	 * deployment time, are passed if ones exist.
	 * 
	 * @param processID
	 * @param partnerRoleName
	 * @param reqMsgParts
	 *            XML Documents of request message.
	 * @param pnrLinkEprAddr
	 *            address of endpoint reference from binding information.
	 * @param wsdlBaseURI
	 * @param wsdlInputStream
	 * @param serviceName
	 *            service name from deployment information (if one exists)
	 * @param portName
	 *            port name from deployment information (if one exists)
	 * @param operationName
	 * @param portTypeName
	 * @param processVaraiables
	 * @return A Map containing the QName and XML Documents of the response
	 *         message.
	 * @throws WsdlLoadingException
	 */
	public Map<QName, Document[]> invoke(String processID,
			String partnerRoleName, Map processVariables,
			Document[] reqMsgParts, String pnrLinkEprAddr, String wsdlBaseURI,
			InputStream wsdlInputStream, String serviceName, String portName,
			String operationName, QName portTypeName)
			throws WsdlLoadingException;

}
