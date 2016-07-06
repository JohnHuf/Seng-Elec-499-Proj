import java.io.*;



public class BleMessage{
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
		return ret;
	}
	
	public static void main(String [] args){
		try{
			RandomAccessFile in = new RandomAccessFile("in.txt", "r");
			byte[] data = new byte[13];
			
			while (in.read(data, 0, 13)==13){
				if (data[12]==0x0A && data[11]==0x0D){
					BleMessage msg= new BleMessage(data);
					System.out.println(msg);
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