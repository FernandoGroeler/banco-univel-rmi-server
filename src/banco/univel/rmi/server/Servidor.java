package banco.univel.rmi.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import banco.univel.rmi.remotebase.IRemoteBancoUnivel;
import banco.univel.rmi.server.entidade.Usuario;

public class Servidor implements IRemoteBancoUnivel {
	private List<Usuario> list = new ArrayList<>();
	
	public static void main(String[] args) {
		System.out.println("Construindo Servidor Remoto");
		Servidor servidor = new Servidor();
		
		try {
			IRemoteBancoUnivel stub = (IRemoteBancoUnivel)UnicastRemoteObject.exportObject(servidor, 0);
			Registry registry = LocateRegistry.getRegistry(9876);
			registry.bind("servidor_banco_univel", stub);
			System.out.println("Servidor iniciado...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Boolean usuarioExiste(int id) {
		if (list.size() > 0) {
			for (Usuario usuario: list) {
				if (usuario.getId() == id){
					return true;
				} else {
					return false;
				}
			}
		}
		
		return false;
	}
	
	private Usuario getUsuario(int id) {
		try {
			cadastrarUsuario(id, "Usuario" + id);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return list.get((id - 1));
	}

	@Override
	public void cadastrarUsuario(int id, String nome) throws RemoteException {
		if (!usuarioExiste(id)) {
			Usuario usuario = new Usuario();
			usuario.setId(id);
			usuario.setNome(nome);
			list.add(usuario);
		}
	}

	@Override
	public void deposito(int idUsuario, double valor) throws RemoteException {
		Usuario usuario = getUsuario(idUsuario);
		double saldoAtual = usuario.getSaldo();
		usuario.setSaldo(saldoAtual += valor);
	}

	@Override
	public double saldo(int idUsuario) throws RemoteException {
		Usuario usuario = getUsuario(idUsuario);
		return usuario.getSaldo();
	}

	@Override
	public void saque(int idUsuario, double valor) throws RemoteException {
		Usuario usuario = getUsuario(idUsuario);
		double saldoAtual = usuario.getSaldo();
		
		Instant inicio = Instant.now();
		
		if (usuario.getDataSaque() != null) {
			inicio = usuario.getDataSaque();
		} 
		
		Instant fim = Instant.now();
		Duration duracao = Duration.between(fim, inicio);
		long miliseg = duracao.toMillis();
		
		if (miliseg > 20000) {
			usuario.setSaldo(saldoAtual -= valor);	
		} else {
			System.out.println("Saque só pode ser realizado após 2 minutos");
		}
	}
}
