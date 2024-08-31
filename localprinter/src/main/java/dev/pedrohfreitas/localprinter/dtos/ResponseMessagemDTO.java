package dev.pedrohfreitas.localprinter.dtos;

public class ResponseMessagemDTO {
	String titulo;
	String msg;
	String typeMsg;
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getTypeMsg() {
		return typeMsg;
	}
	public void setTypeMsg(String typeMsg) {
		this.typeMsg = typeMsg;
	}
}
