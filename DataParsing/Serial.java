import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 
import java.util.*;
import java.io.PrintWriter;
import java.io.File;


public class Serial implements SerialPortEventListener {
	
	SerialPort serialPort;
	boolean isPunch =false;
	List <BleMessage> punchData;
	boolean punchOver=false;
	boolean punchStart=false;
	static String [] classes = {"uppercut","jab","hook","noise"};
	String punchType="uppercut";
	
	int underCount=0;
	int punchCount =0;
  
	
	
        /** The port we're normally going to use. */
	private static final String PORT_NAMES[] = { 
			"COM8",
			// Windows
	};
	/**
	* A BufferedReader which will be fed by a InputStreamReader 
	* converting the bytes into characters 
	* making the displayed results codepage independent
	*/
	private BufferedReader input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 100;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 115200;

	public void initialize() {
                // the next line is for Raspberry Pi and 
                // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
                //System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");

		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		//First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				
				char [] inputLine = new char [13];
				while(input.read(inputLine,0,13)==13){
					BleMessage msg = new BleMessage(new String(inputLine).getBytes());
					
					//System.out.println(msg);
					if(punchStart&&!punchOver){
						if(underCount>=70){
							punchOver=true;
						}	
						
						punchData.add(msg);
						if (msg.magnitude<10.0){
							underCount++;
						}
						
						else{
							underCount=0;
						}
						
						if(msg.magnitude>70.0){
							isPunch=true;
						}
					}
					if(punchStart&&punchOver){
						if(isPunch){
							BleMessage[] punchArray = new BleMessage[ punchData.size() ];
							punchData.toArray( punchArray );
							punchOver=false;
							Punch punch=new Punch(punchArray);
							punch.isPunch=true;
							System.out.println("PUNCH DETECTED");
							System.out.println("SPEED : "+punch.getSpeed()+"m/s");
							System.out.println("FORCE : "+punch.getForce()+"G");
							punchCount++;
							
							PrintWriter writer = new PrintWriter("PunchData\\"+punchType+"\\"+punchType+"("+punchCount+").csv");
							writer.print(punch);
							writer.close();
	
							isPunch=false;
							//Classify Punch Here or print it to a file
						}
						punchStart=false;
						punchOver=false;
					}
					if (msg.magnitude>10.0&&!punchStart){
						punchStart=true;
						punchData=new ArrayList<BleMessage>();
						punchData.add(msg);
					}
				}
				
			
					
				
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}

	public static void main(String[] args) throws Exception {
		Serial main = new Serial();
		main.initialize();
		File dir = new File("PunchData\\"+main.punchType);
		  File[] directoryListing = dir.listFiles();
		  if (directoryListing != null) {
		    for (File child : directoryListing) {
		       main.punchCount++;
		    }
		  } 
		Thread t=new Thread() {
			public void run() {
				//the following line will keep this app alive for 1000 seconds,
				//waiting for events to occur and responding to them (printing incoming messages to console).
				try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
			}
		};
		t.start();
		System.out.println("Started");
	}
}