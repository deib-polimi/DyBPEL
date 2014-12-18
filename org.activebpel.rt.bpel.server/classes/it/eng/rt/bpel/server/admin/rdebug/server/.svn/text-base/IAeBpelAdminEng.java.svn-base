package it.eng.rt.bpel.server.admin.rdebug.server;

import it.eng.binding.IAeAbstractBindingRule;
import it.eng.binding.IAeBindingRule;

import java.rmi.RemoteException;
import java.util.Map;

/**
 *@author Paolo Zampognaro Engineering Ingegneria Informatica spa
 **/

public interface IAeBpelAdminEng {

	/**
	 * Add a binding rule to the specified process partner role
	 * 
	 * @param aProcessID
	 * @param aPartnerRoleName
	 * @param aBindingRule
	 * @throws RemoteException
	 */
	public void addPartnerRoleBindingRule(String aProcessID,
			String aPartnerRoleName, IAeBindingRule aBindingRule)
			throws RemoteException;

	/**
	 * Add an abstract binding rule to the specified process partner role
	 * 
	 * @param aProcessID
	 * @param aPartnerRoleName
	 * @param aAbstractBindingRule
	 * @throws RemoteException
	 */
	public void addPartnerRoleAbstractBindingRule(String aProcessID,
			String aPartnerRoleName, IAeAbstractBindingRule aAbstractBindingRule)
			throws RemoteException;

	/**
	 * Remove the rule with the specific condition associated to the
	 * specified partner role.
	 * 
	 * @param aProcessID
	 * @param aPartnerRoleName
	 * @param ruleCondition
	 * @throws RemoteException
	 */
	public void removePartnerRoleRule(String aProcessID,
			String aPartnerRoleName, Map aRuleCondition) throws RemoteException;

	/**
	 * Return the binding rules of the specified partner role.
	 * 
	 * @param aProcessID
	 * @param aPartnerRoleName
	 * @throws RemoteException
	 */
	public IAeBindingRule[] getPartnerRoleBindingRules(String aProcessID,
			String aPartnerRoleName) throws RemoteException;
	
	/**
	 * Return the rules of the specified partner role.
	 * 
	 * @param aProcessID
	 * @param aPartnerRoleName
	 * @throws RemoteException
	 */
	public IAeAbstractBindingRule[] getPartnerRoleAbstractBindingRules(String aProcessID,
			String aPartnerRoleName) throws RemoteException;
}
