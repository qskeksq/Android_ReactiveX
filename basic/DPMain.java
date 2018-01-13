import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DPMain {
	
	public static void main(String[] args) {
		// subject�� ���꽺���忡�� �ֱ������� client�� ȣ���Ѵ�.
		Subject server = new Subject();
		server.start();
		
		ClientDaemon cd = new ClientDaemon(server);
		cd.run();
		
	}

}

class Client1 implements Subject.IObserver {

	@Override
	public void notice() {
		System.out.println("Ŭ���̾�Ʈ 1�� ������� �ݿ�");
		
	}
	
}

class Client2 implements Subject.IObserver {

	@Override
	public void notice() {
		System.out.println("Ŭ���̾�Ʈ 2�� ������� �ݿ�");
		
	}
	
}

class Subject extends Thread{

	List<IObserver> clients = new ArrayList<>();
	
	public void run() {
		Random random = new Random();
		while(true) {
			System.out.println("subject�� �޽����� �����߽��ϴ�");
			for(IObserver observer : clients) {
				observer.notice();
			}
			try {
				Thread.sleep((random.nextInt(10)+1)*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public interface IObserver {
		void notice();
	}
}

class ClientDaemon {
	Subject server;
	
	public ClientDaemon(Subject server) {
		this.server = server;
	}

	public void run() {
		int count = 0;
		while(true) {
			count++;
			if(count%2 == 0) {
				server.clients.add(new Client1());
			} else if(count%2 ==1) {
				server.clients.add(new Client2());
			}
			Random random = new Random();
			try {
				Thread.sleep((random.nextInt(5)+1)*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}


