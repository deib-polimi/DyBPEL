package org.activebpel.rt.bpel.impl.instrumentation;

public class ModificheEffettuate {
	private Integer procId;
	private Integer cod_adp;
	public ModificheEffettuate(Integer procId,Integer cod_adp)
	{
		this.procId = procId;
		this.cod_adp = cod_adp;		
	}
	public void setProcId(Integer procId) {
		this.procId = procId;
	}
	public Integer getProcId() {
		return procId;
	}
	public void setCod_adp(Integer cod_adp) {
		this.cod_adp = cod_adp;
	}
	public Integer getCod_adp() {
		return cod_adp;
	}

}
