package org.activebpel.rt.bpel.impl.instrumentation;

public class Record {
	private Integer cod_proc;
	private String version;
	private Integer istanze_attive;
	private String dest_path;
	private String fileName;
	public Record()
	{
		
	}
	public Record(Integer cod_proc,String version, Integer istanze_attive,String dest_path,String fileName)
	{
		this.cod_proc = cod_proc;
		this.version = version;
		this.istanze_attive = istanze_attive;
		this.dest_path = dest_path;
		this.fileName = fileName;
	}
	
	public void setCod_proc(Integer cod_proc) {
		this.cod_proc = cod_proc;
	}
	public Integer getCod_proc() {
		return cod_proc;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getVersion() {
		return version;
	}
	public void setIstanze_attive(Integer istanze_attive) {
		this.istanze_attive = istanze_attive;
	}
	public Integer getIstanze_attive() {
		return istanze_attive;
	}
	public void setDest_path(String dest_path) {
		this.dest_path = dest_path;
	}
	public String getDest_path() {
		return dest_path;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileName() {
		return fileName;
	}

}
