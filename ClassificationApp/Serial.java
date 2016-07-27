import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 
import java.util.*;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;


public class Serial implements SerialPortEventListener {
	
	SerialPort serialPort;
	boolean isPunch =false;
	List <BleMessage> punchData;
	boolean punchOver=false;
	boolean punchStart=false;
	long start=0;
	
	
	static GuiWindow window;
	static Learning model;
	
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
					
					if(window !=null && System.currentTimeMillis()-start>=1000){
						window.setBgRed();
						start=0;
					}
					
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
							Punch punch=new Punch(punchArray);
							punch.isPunch=true;
							System.out.println("PUNCH DETECTED");
							
							punch.fixTimes();
							double [] features = punch.getFeatures();
							String cls =model.getFit(features);
							window.setClass(cls.substring(0, 1).toUpperCase() + cls.substring(1));
						
							window.setAccel(punch.getForce());
							window.setSpeed(punch.getSpeed());
							
							
							window.setBgGreen();
							start=System.currentTimeMillis();
							
							punchCount++;
							window.setCount(punchCount);
							/*
							PrintWriter writer = new PrintWriter("PunchData\\"+cls+"\\"+cls+"("+punchCount+").csv");
							writer.print(punch);
							writer.close();
	*/
							isPunch=false;
							//Classify Punch Here or print it to a file
						}
						
						punchStart=false;
						punchOver=false;
						underCount=0;
					}
					if (msg.magnitude>10.0&&!punchStart){
						punchStart=true;
						punchData=new ArrayList<BleMessage>();
						punchData.add(msg);
					}
				}
				
			
					
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}
	
	public static void getArff(){
		String[] dirs = {"punchdata/C/jab/", "punchdata/C/hook/", "punchdata/C/uppercut/",
				  "punchdata/J/jab/", "punchdata/J/hook/", "punchdata/J/uppercut/",
				  "punchdata/F/jab/", "punchdata/F/hook/", "punchdata/F/uppercut/",
				  "punchdata/B/jab/", "punchdata/B/hook/",
				  "punchdata/A/jab/", "punchdata/A/hook/", "punchdata/A/uppercut/",
				  "punchdata/R/jab/", "punchdata/R/hook/", "punchdata/R/uppercut/"};
		try{
		PrintWriter out = new PrintWriter( "dataset.arff" );
		
		String ret = "";
		ret+="@relation punch1\n\n";
		
		
		ret+="@attribute name {";
		for (int i=1;i<=327;i++){
			ret+="Punch"+i+",";
		}
		ret+="}\n";
		ret+="@attribute bag relational\n";
		ret+="  @attribute time numeric\n";
		ret+="  @attribute high_g_x numeric\n";
		ret+="  @attribute high_g_y numeric\n";
		ret+="  @attribute high_g_z numeric\n";
		ret+="  @attribute low_g_x numeric\n";
		ret+="  @attribute low_g_y numeric\n";
		ret+="  @attribute low_g_z numeric\n"; 
		ret+="  @attribute gyro_x numeric\n"; 
		ret+="  @attribute gyro_y numeric\n"; 
		ret+="  @attribute gyro_z numeric\n";
	//	ret+="  @attribute magnitude numeric\n";
		ret+="@end bag\n";
		ret+="@attribute punch_type {jab, hook, uppercut, noise}\n\n";
		ret+="@data\n";
		int count=0;
		out.write(ret);
		for(int i=0;i<dirs.length;i++){
			File dir = new File(dirs[i]);
			File[] directoryListing = dir.listFiles();
			if (directoryListing != null) {
			    for (File child : directoryListing) {
			    	count++;
			    	BufferedReader br = new BufferedReader(new FileReader(child));
			    	    String line;
			    	    ret="";
			    	    ret+="Punch"+count+",\"";
			    	    line = br.readLine();
			    	    while (line != null) {
			    	       // process the line.
			    	    	
			    	    	String [] values = line.split(",");
			    	    	ret+=values[0];
			    	    	ret+=",";
			    	    	ret+=values[2];
			    	    	ret+=",";
			    	    	ret+=values[3];
			    	    	ret+=",";
			    	    	ret+=values[4];
			    	    	ret+=",";
			    	    	ret+=values[5];
			    	    	ret+=",";
			    	    	ret+=values[6];
			    	    	ret+=",";
			    	    	ret+=values[7];
			    	    	ret+=",";
			    	    	ret+=values[8];
			    	    	ret+=",";
			    	    	ret+=values[9];
			    	    	ret+=",";
			    	    	ret+=values[10];
			    	    //	ret+=",";
			    	    //	ret+=values[11];
			    	    	line = br.readLine();
			    	    	if(line!=null){
			    	    		ret+="\\n";
			    	    	}
			    	    }
			    	    
			    	    ret+="\",";
			    	    if(child.getName().charAt(0)=='j'){
			    	    	ret+="jab\n";
			    	    }
			    	    else if	(child.getName().charAt(0)=='h'){
			    	    	ret+="hook\n";	    	    
			    	    }
			    	    else if	(child.getName().charAt(0)=='u'){
			    	    	ret+="uppercut\n";			    	    
			    	    }
			    	    out.write(ret);
			    	}
			
			    }
			 }
		}    	
		catch(FileNotFoundException e){
			System.out.println("File not found.");
		}
		catch(IOException e){
			System.out.println("IO Exception");
		}
	}
	

	public static void main(String[] args) throws Exception {
		
		Serial main = new Serial();
		main.initialize();
		  

		Thread t2=new Thread() {
			public void run() {
				//the following line will keep this app alive for 1000 seconds,
				//waiting for events to occur and responding to them (printing incoming messages to console).
				try {
					model = new Learning();
					window = new GuiWindow();
					window.getFrmBoxingMetricsDemo().setVisible(true);
					window.setCount(0);
					window.setClass("No Punch");
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {Thread.sleep(2000000000);} catch (InterruptedException ie) {}
				
			}
		};
		//t1.start();
		t2.start();
		System.out.println("Started");
	}
}