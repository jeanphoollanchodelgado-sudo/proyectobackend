package com.epiis.apirfds220261.staticdata;

public enum EnumProcess {
	PENDING("Pendiente de revisión"),
	SEEN("Visto"),
	REFUSED("Rechazado"),
	COORDINATION("En coordinación"),
	CLOSE("Cerrado");
	
	private String value;
	
	EnumProcess(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return this.value;
	}
}