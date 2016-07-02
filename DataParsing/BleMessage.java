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
		time = data[0] & MASK;
		ctrl = data[1] & MASK;
		high_g_x = data[2] & MASK;
		high_g_y = data[3] & MASK;
		high_g_z = data[4] & MASK;
		low_g_x = data[5] & MASK;
		low_g_y = data[6] & MASK;
		low_g_z = data[7] & MASK;
		gyro_x = data[8] & MASK;
		gyro_y = data[9] & MASK;
		gyro_z = data[10] & MASK;
	}
	
	public String toString(){
		String ret;
		ret="Time = "+time+"\n";
		ret+="Control = "+ctrl+"\n";
		ret+="High G X = "+high_g_x+"\n";
		ret+="High G Y = "+high_g_y+"\n";
		ret+="High G Z = "+high_g_z+"\n";
		ret+="Low G X = "+low_g_x+"\n";
		ret+="Low G Y = "+low_g_y+"\n";
		ret+="Low G Z = "+low_g_z+"\n";
		ret+="Gyro X = "+gyro_x+"\n";
		ret+="Gyro Y = "+gyro_y+"\n";
		ret+="Gyro Z = "+gyro_z+"\n";
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