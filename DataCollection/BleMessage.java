import java.io.*;
import java.util.*;




public class BleMessage{
	static String [] classes = {"uppercut","jab","hook","noise"};
	static String [] headers = {"uc","jab","hook","noise"};
	public final static int MASK = 0xff;
	int time;
	int ctrl;
	int high_g_x;
	int high_g_y;
	int high_g_z;
	int low_g_x;
	int low_g_y;
	int low_g_z;
	int gyro_x;
	int gyro_y;
	int gyro_z;
	double magnitude;
	
	public BleMessage(byte[] data){
		time = data[0];
		ctrl = data[1];
		high_g_x = data[2];
		high_g_y = data[3];
		high_g_z = data[4];
		low_g_x = data[5];
		low_g_y = data[6];
		low_g_z = data[7];
		gyro_x = data[8];
		gyro_y = data[9];
		gyro_z = data[10];
		magnitude = Math.sqrt((low_g_x*low_g_x)+(low_g_y*low_g_y)+(low_g_z*low_g_z));
	}
	

	public String toString(){
		String ret;
		ret=time+",";
		ret+=ctrl+",";
		ret+=high_g_x+",";
		ret+=high_g_y+",";
		ret+=high_g_z+",";
		ret+=low_g_x+",";
		ret+=low_g_y+",";
		ret+=low_g_z+",";
		ret+=gyro_x+",";
		ret+=gyro_y+",";
		ret+=gyro_z+",";
		ret+=magnitude+",";
		return ret;
	}
	
	public static void printCSV(String file){
		try{
			RandomAccessFile in = new RandomAccessFile(file, "r");
			byte[] data = new byte[13];
			
			while (in.read(data, 0, 13)==13){
					BleMessage msg= new BleMessage(data);
					System.out.println(msg);
			}
		}
		catch(FileNotFoundException e){
			System.out.println("File not found.");
		}
		catch(IOException e){
			System.out.println("IO Exception");
		}
	}
	
	public static void testPunchDetection(int numUnder){
		int trueCount;
		int trueTotal;
		int wrongTotal=0;
		for(int i=0;i<classes.length;i++){
			trueTotal=0;
			for(int j=1;j<=20;j++){
				trueCount=0;
				try{
					RandomAccessFile in = new RandomAccessFile("chrisPunches\\"+classes[i]+"\\"+headers[i]+" ("+j+")", "r");
					System.out.println("File: "+headers[i]+" ("+j+")");
					byte[] data = new byte[13];
					boolean isPunch =false;
					
					while (in.read(data, 0, 13)==13){
						BleMessage msg= new BleMessage(data);
						isPunch=false;
						if(msg.magnitude >= 10.0){
							List <BleMessage> punchData=new ArrayList<BleMessage>();
							boolean punchOver=false;
							int underCount=0;
							while(!punchOver&&in.read(data, 0, 13)==13){
								if(underCount>=numUnder){
									punchOver=true;
								}
								punchData.add(msg);
								msg= new BleMessage(data);
								if (msg.magnitude<10.0){
									underCount++;
								}
								else{
									underCount=0;
								}
								if(msg.magnitude>50.0){
									isPunch=true;
								}
							}
							BleMessage[] punchArray = new BleMessage[ punchData.size() ];
							punchData.toArray( punchArray );
							punchOver=false;
							Punch punch=new Punch(punchArray);
							if(isPunch){
								punch.isPunch=true;
								trueCount++;
								//System.out.println(punch);
								
							}
							
							
						}
					}
					int wrong = (trueCount!=1)?Math.abs(trueCount-1):0;
					wrongTotal+=wrong;
					System.out.println(trueCount);
					
				}
				catch(FileNotFoundException e){
					System.out.println("File not found.");
				}
				catch(IOException e){
					System.out.println("IO Exception");
				}		
			}
			System.out.println(classes[i]+" Misclassifed punches "+ wrongTotal);
			wrongTotal=0;
			
		}
	}
	
	/*public static void main(String [] args){
		for(int i=0;i<classes.length;i++){
			for(int j=1;j<=20;j++){
				try{
					RandomAccessFile in = new RandomAccessFile("chrisPunches\\"+classes[i]+"\\"+headers[i]+" ("+j+")", "r");
					System.out.println("File: "+headers[i]+" ("+j+")");
					byte[] data = new byte[13];
					boolean isPunch =false;
					
					while (in.read(data, 0, 13)==13){
						BleMessage msg= new BleMessage(data);
						isPunch=false;
						if(msg.magnitude >= 10.0){
							List <BleMessage> punchData=new ArrayList<BleMessage>();
							boolean punchOver=false;
							int underCount=0;
							while(!punchOver&&in.read(data, 0, 13)==13){
								if(underCount>=Integer.valueOf(args[0])){
									punchOver=true;
								}
								punchData.add(msg);
								msg= new BleMessage(data);
								if (msg.magnitude<10.0){
									underCount++;
								}
								else{
									underCount=0;
								}
								if(msg.magnitude>50.0){
									isPunch=true;
								}
							}
							BleMessage[] punchArray = new BleMessage[ punchData.size() ];
							punchData.toArray( punchArray );
							punchOver=false;
							Punch punch=new Punch(punchArray);
							if(isPunch){
								punch.isPunch=true;
								//System.out.println(punch);
								PrintWriter writer = new PrintWriter("PunchData\\"+classes[i]+"\\"+classes[i]+"("+j+").csv");
								writer.print(punch);
								writer.close();
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
			
		}
		
		
	
		
		
	}*/
}