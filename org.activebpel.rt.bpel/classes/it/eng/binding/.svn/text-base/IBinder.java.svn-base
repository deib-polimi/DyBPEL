package it.eng.binding;

import it.eng.binding.impl.AeBindingRule;

import java.util.Map;

/**
 *@author Paolo Zampognaro Engineering Ingegneria Informatica spa
 **/
public interface IBinder {

	/**
	 * Adds a new binding rule to the specified process partner role, if a
	 * binding rule with the same condition (@see
	 * it.eng.binding.impl.IAeBindingRule#getCondition()) does not yet exist. It
	 * will update the binding information of the existing rule
	 * otherwise.
	 * 
	 */

	public void addPartnerRoleBindingRule(String processID,
			String partnerRoleName, IAeBindingRule bindingRule);

	/**
	 * 
	 * Adds a new abstract binding rule to the specified process partner role,
	 * if a binding rule with the same condition (@see
	 * it.eng.binding.impl.IAeAbstractBindingRule#getCondition()) does not yet exist. It
	 * will update the abstract binding information of the existing rule
	 * otherwise.
	 */
	public void addPartnerRoleAbstractBindingRule(String processID,
			String partnerRoleName, IAeAbstractBindingRule abstractBindingRule);

	/**
	 * Removes the partner role binding rule (@see
	 * it.eng.binding.impl.AeBindingRule) with the specified condition.
	 * 
	 */
	public void removePartnerRoleRule(String processID,
			String partnerRoleName, Map<String, String> ruleCondition);

	/**
	 * Returns the binding rules (@see it.eng.binding.impl.IAeBindingRule) of the
	 * specified process partner role.
	 */
	public IAeBindingRule[] getPartnerRoleBindingRules(String processID,
			String partnerRoleName);

	/**
	 * Returns the abstract binding rules (@see it.eng.binding.impl.IAeAbstractBindingRule) of the
	 * specified process partner role.
	 */
	public IAeAbstractBindingRule[] getPartnerRoleAbstractBindingRules(
			String processID, String partnerRoleName);

}
