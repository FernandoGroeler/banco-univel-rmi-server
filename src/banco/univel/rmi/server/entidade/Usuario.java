package banco.univel.rmi.server.entidade;

import java.time.Instant;

public class Usuario {
	private int id;
	private String nome;
	private double saldo;
	private Instant dataSaque;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public double getSaldo() {
		return saldo;
	}
	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	public Instant getDataSaque() {
		return dataSaque;
	}
	public void setDataSaque(Instant dataSaque) {
		this.dataSaque = dataSaque;
	}
}