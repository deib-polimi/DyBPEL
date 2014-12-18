package it.eng.binding.impl;

import it.eng.binding.IAeAbstractBindingRule;
import it.eng.binding.IAeBindingRule;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.Transient;

/**
 *@author Paolo Zampognaro Engineering Ingegneria Informatica spa. 
 *
 * This class provides implementation for a (abstract) binding rule.
 **/


@Entity
public class AeBindingRule implements IAeBindingRule, IAeAbstractBindingRule{

	@SuppressWarnings("unused")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "RULE_ID")
	private Long id;
	@SuppressWarnings("unused")
	private ProcessPartnerRole partnerRole = null;     

	/*
	 * Condition
	 */
	private String conditionAsString;
	@Transient
	private Map<String, String> condition;
	/*
	 * Binding Information
	 */
	private String wsdlAddress;
	private String serviceName;
	private String portName;
	/*
	 * Abstract Information
	 */
    private String slatURL;
    private String bpelVariable;
    private boolean isMultiple;
    
    private boolean isAbstract;

	public AeBindingRule() {
	}
	
	/*
	 * Constructor used to create a binding rule
	 */
	public AeBindingRule(Map<String, String> condition, String wsdlAddress,
			String serviceName, String portName) {
		this.condition = condition;
		this.wsdlAddress = wsdlAddress;
		this.serviceName = serviceName;
		this.portName = portName;
		this.isAbstract = false;
		this.conditionAsString = new TreeMap<String, String>(condition)
		.toString();
	}
	
	/*
	 * Constructor used to create an abstract binding rule
	 */
	public AeBindingRule(Map<String, String> condition, String slatURL, String bpelVariable,
			boolean isMultiple) {
		this.condition = condition;
		this.slatURL = slatURL;
		this.bpelVariable = bpelVariable;
		this.isMultiple = isMultiple;
		this.isAbstract = true;
		this.conditionAsString = new TreeMap<String, String>(condition)
		.toString();
	}

	public Map<String, String> getCondition() {
		return condition;
	}

	/**
	 * Returns the condition part of this rule as string.
	 */
	public String getConditionAsString() {
		return conditionAsString;
	}

	public String getWsdlAddress() {
		return wsdlAddress;
	}

	public void setWsdlAddress(String wsdlAddress) {
		this.wsdlAddress = wsdlAddress;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}
	
	public String getSlatURL() {
		return slatURL;
	}

	public void setSlatURL(String slatURL) {
		this.slatURL = slatURL;
	}

	public String getBpelVariable() {
		return bpelVariable;
	}

	public void setBpelVariable(String bpelVariable) {
		this.bpelVariable = bpelVariable;
	}

	public boolean isMultiple() {
		return isMultiple;
	}

	public void setMultiple(boolean isMultiple) {
		this.isMultiple = isMultiple;
	}
	
	public boolean isAbstract() {
		return isAbstract;
	}

	void setPartnerRole(ProcessPartnerRole partnerRole) {
		this.partnerRole = partnerRole;
	}

	@PostLoad
	void fillConditionMap() {
		//condition = new TreeMap<String, String>();
		condition = new HashMap<String, String>();
		System.out.println("Nel postLoad prima di riempire la mappa: condition.: "+condition);
		fillMap(condition, conditionAsString.substring(1));
		System.out.println("Nel postLoad dopo riempto la mappa: condition.: "+condition);
	}

	/*
	 * example of condition as string {customerID=AUCHAN,creditCardType=VISA}
	 */
	private void fillMap(Map<String, String> condition, String conditionAsString) {
		int index1 = conditionAsString.indexOf('=');
		if(index1==-1){
			//La mappa è vuota
			return;
		}
		else{
			String key = conditionAsString.substring(0, index1);
			String value;
			int index2;
			if (conditionAsString.indexOf(',') != -1) {
				index2 = conditionAsString.indexOf(',');
				value = conditionAsString.substring(index1 + 1, index2);
				condition.put(key, value);
				fillMap(condition, conditionAsString.substring(index2 + 1));
			} else {
				index2 = conditionAsString.indexOf('}');
				value = conditionAsString.substring(index1 + 1, index2);
				condition.put(key, value);
			}
		}

	}

}
