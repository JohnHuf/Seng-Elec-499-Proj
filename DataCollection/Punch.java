import java.io.*;
import java.util.*;




public class Punch{
	static String [] classes = {"uppercut","jab","hook","noise"};
	static String [] headers = {"uc","jab","hook","noise"};
	BleMessage [] raw_data;
	int speed;
	boolean isPunch;
	public Punch(BleMessage [] data){
		raw_data = data;
	}
	
	private double rawToRad(double input){
		return input*Math.PI/128.0;
	}
	
	private double rawToMs2(double input){
		return input*9.81*16/127;
	}
	
	public double getSpeed(){	
		//1. find the spike in low G X and use only punch data prior to it
		//2. Convert accelerometer values to m/s^2 ( low G 127 == 16G, 0 = 0G)
		//3. Convert gyro values to radians (127= pi radians, -128 = -pi rads, 0 = 0 rads)
		//4. Compute mapped acceleration by applying rotation matrix to acceleration values
		//5. Compute velocity by integrating acceleration
		//6. Take the magnitude to get speed in meters per second
		
		
		int i=0;
		List <Double []> acceleration =new ArrayList<Double []>();
		Double[] accel=new Double[3];
		double xrad = 0;
		double yrad = 0;
		double zrad = 0;
		double cosx=0;
		double cosy=0;
		double cosz=0;
		double sinx=0;
		double siny=0;
		double sinz=0;
		double accx=0;
		double accy=0;
		double accz=0;
		double velx=0;
		double vely=0;
		double velz=0;
		double speed=0;
		
		while(raw_data[i].low_g_x != -128){
			xrad=rawToRad(raw_data[i].gyro_x);
			yrad=rawToRad(raw_data[i].gyro_y);
			zrad=rawToRad(raw_data[i].gyro_z);
			
			cosx=Math.cos(xrad);
			sinx=Math.sin(xrad);
			cosy=Math.cos(yrad);
			siny=Math.sin(yrad);
			cosz=Math.cos(zrad);
			sinz=Math.sin(zrad);
			
			accx=rawToMs2(raw_data[i].low_g_x);
			accy=rawToMs2(raw_data[i].low_g_y);
			accz=rawToMs2(raw_data[i].low_g_z);
			
			
			
			accel[0]=accx*(cosz*cosy)+accy*(cosz*siny*sinx-cosx*sinz)+accz*(sinx*sinz+cosx*cosz*siny);
			accel[1]=accx*(cosy*sinz)+accy*(cosx*cosz+sinx*sinz*siny)+accz*(cosx*sinz*siny-cosz*sinx);
			accel[2]=accx*(-1.0*siny)+accy*(cosy*sinx)+accz*(cosx*cosy);
			accel[2]-=9.81;
					
			acceleration.add(accel);
			
			if (i>0){
				velx += (raw_data[i].time - raw_data[i-1].time) * ((acceleration.get(i)[0]+acceleration.get(i-1)[0])/2);
				vely +=(raw_data[i].time - raw_data[i-1].time) * ((acceleration.get(i)[1]+acceleration.get(i-1)[1])/2);
				velz += (raw_data[i].time - raw_data[i-1].time) * ((acceleration.get(i)[2]+acceleration.get(i-1)[2])/2);
			}
			i++;
		}
		speed = Math.sqrt(square(velx)+square(vely)+square(velz));
		return speed;
	}
	
	private void fixTimes(){
		int offset=0;
		for (int i=0;i<raw_data.length;i++){
			if(raw_data[i].time==63){
				continue;
			}
			if((raw_data[i-1].time<0 && raw_data[i].time >= 0) || (raw_data[i-1].time>=0) && (raw_data[i].time<0)){
				offset+=128;
			}
			if(raw_data[i].time<0){
				raw_data[i].time =128+raw_data[i].time;
			}
			if(raw_data[i].time >=0){
				//nothing
			}
			raw_data[i].time+=offset;
		}
	}
	
	private double square(double input){
		return input*input;	
	}
	
	public double getForce(){
		//Find the maximum in High G Accel magnitude
		double max =0;
		for(int i=0;i<raw_data.length;i++){
			double magnitude = square(raw_data[i].high_g_x*0.78125)+square(raw_data[i].high_g_y*0.78125)+square(raw_data[i].high_g_z*0.78125);
			if (magnitude>max){
				max=magnitude;
			}
		}
		return max;
	}
	
	public String toString(){
		String ret = "";
		for(int i=0;i<raw_data.length;i++){
			ret+=raw_data[i];
			ret+="\n";
		}
		return ret;
	}
}