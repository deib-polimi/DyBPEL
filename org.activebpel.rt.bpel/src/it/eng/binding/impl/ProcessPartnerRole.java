package it.eng.binding.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;

/**
 *@author Paolo Zampognaro Engineering Ingegneria Informatica spa
 **/
@Entity
public class ProcessPartnerRole {
	
    @Id
	private String partnerRoleKey = null;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "partnerRole")
    @MapKey(name = "conditionAsString")
	private Map<String,AeBindingRule> rules;
    

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH,
			CascadeType.MERGE })
	private Binder binder;
	
	public ProcessPartnerRole() {
		
	}
	
    public ProcessPartnerRole(String processID, String partnerRoleName, AeBindingRule bindingRule, Binder binder) {
    	partnerRoleKey = processID+partnerRoleName;
    	rules = new HashMap<String, AeBindingRule>();
    	bindingRule.setPartnerRole(this);
		rules.put(bindingRule.getConditionAsString(), bindingRule);
		this.binder = binder;
	}
    
    String getPartnerRoleKey() {
		return partnerRoleKey;
	}
    
	Map<String, AeBindingRule> getRules() {
		return rules;
	}
	
	AeBindingRule[] getBindingRules(){
		Iterator<AeBindingRule> it =rules.values().iterator();
		List <AeBindingRule>bindingRulesList = new ArrayList<AeBindingRule>();
		for (; it.hasNext();) {
			AeBindingRule rule = (AeBindingRule) it.next();
			if(!rule.isAbstract())
				bindingRulesList.add(rule);
		}
		return bindingRulesList.toArray(new AeBindingRule[0]);
	}
	
	AeBindingRule[] getAbstractBindingRules(){
		Iterator<AeBindingRule> it =rules.values().iterator();
		List<AeBindingRule> abstractBindingRulesList = new ArrayList<AeBindingRule>();
		for (; it.hasNext();) {
			AeBindingRule rule = (AeBindingRule) it.next();
			if(rule.isAbstract())
				abstractBindingRulesList.add(rule);
		}
		return abstractBindingRulesList.toArray(new AeBindingRule[0]);
	}

	void addRule(AeBindingRule bindingRule){
		//- Binding rules with same condition cannot be associated to the same invoke activity.
		AeBindingRule br = rules.get(bindingRule.getConditionAsString());
		if(br!=null){
			if(!br.isAbstract()){
			br.setWsdlAddress(bindingRule.getWsdlAddress());
			br.setServiceName(bindingRule.getServiceName());
			br.setPortName(bindingRule.getPortName());
			}
			else{
			br.setSlatURL(bindingRule.getSlatURL());
			br.setBpelVariable(bindingRule.getBpelVariable());
			br.setMultiple(bindingRule.isMultiple());
			}
		}
		else{
			bindingRule.setPartnerRole(this);
	    	rules.put(bindingRule.getConditionAsString(), bindingRule);			
		}
    }
}
