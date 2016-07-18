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
		
		while(raw_data[i].low_g_x != -128 && i<raw_data.length-1){
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
				velx += (raw_data[i].time - raw_data[i-1].time)/1000.0 * ((acceleration.get(i)[0]+acceleration.get(i-1)[0])/2.0);
				vely +=(raw_data[i].time - raw_data[i-1].time)/1000.0 * ((acceleration.get(i)[1]+acceleration.get(i-1)[1])/2.0);
				velz += (raw_data[i].time - raw_data[i-1].time)/1000.0 * ((acceleration.get(i)[2]+acceleration.get(i-1)[2])/2.0);
			}
			i++;
		}
		speed = Math.sqrt(square(velx)+square(vely)+square(velz));
		return speed;
	}
	
	public void fixTimes(){
		int offset=0;
		int [] time = new int[raw_data.length];
		raw_data[0].time=0;
		int t=0;
		for (int i=1;i<raw_data.length;i++){
			if(i!=raw_data.length-1&&raw_data[i].time==63&&raw_data[i-1].time<63 && raw_data[i+1].time>63){
				raw_data[i]=raw_data[i-1];
			}
			if((raw_data[i-1].time<0 && raw_data[i].time >= 0) || ((raw_data[i-1].time>=0) && (raw_data[i].time<0))){
				offset+=128;
			}
			if(raw_data[i].time<0){
				t =128+raw_data[i].time;
			}
			else if(raw_data[i].time >=0){
				//nothing
				t=raw_data[i].time;
			}
			t+=offset;
			time[i]=t;
			
		}
		for (int i=1;i<raw_data.length;i++){
			raw_data[i].time = time[i];
			System.out.println(time[i]);
		}
		
		
	}
	
	private double square(double input){
		return input*input;	
	}
	
	public double getForce(){
		//Find the maximum in High G Accel magnitude
		double max =0;
		for(int i=0;i<raw_data.length;i++){
			double magnitude = Math.sqrt(square((double)raw_data[i].high_g_x*0.78125));
			if (magnitude>max){
				max=magnitude;
			}
		}
		return max;
	}
	
	public double[] getFeatures(){
		
		double high_g_x_mean = 0.0;
		double high_g_y_mean = 0.0;
		double high_g_z_mean = 0.0;
		double low_g_x_mean  = 0.0;
		double low_g_y_mean  = 0.0;
		double low_g_z_mean  = 0.0;
		double gyro_x_mean   = 0.0;
		double gyro_y_mean   = 0.0;
		double gyro_z_mean   = 0.0;
		double high_g_x_var = 0.0;
		double high_g_y_var = 0.0;
		double high_g_z_var = 0.0;
		double low_g_x_var  = 0.0;
		double low_g_y_var  = 0.0;
		double low_g_z_var  = 0.0;
		double gyro_x_var   = 0.0;
		double gyro_y_var   = 0.0;
		double gyro_z_var   = 0.0;

		int count=0;
		
		for(int i=0;i<raw_data.length;i++){
			count++;
			
			high_g_x_mean +=raw_data[i].high_g_x;
			high_g_y_mean +=raw_data[i].high_g_y;
			high_g_z_mean +=raw_data[i].high_g_z;
			low_g_x_mean +=raw_data[i].low_g_x;
			low_g_y_mean +=raw_data[i].low_g_y;
			low_g_z_mean +=raw_data[i].low_g_z;
			gyro_x_mean +=raw_data[i].gyro_x;
			gyro_y_mean +=raw_data[i].gyro_y;
			gyro_z_mean +=raw_data[i].gyro_z;
			
			high_g_x_var +=  square(raw_data[i].high_g_x);
			high_g_y_var += square(raw_data[i].high_g_x);
			high_g_z_var += square(raw_data[i].high_g_x);
			low_g_x_var += square(raw_data[i].low_g_x);
			low_g_y_var += square(raw_data[i].low_g_y);
			low_g_z_var += square(raw_data[i].low_g_z);
			gyro_x_var +=  square(raw_data[i].gyro_x);
			gyro_y_var += square(raw_data[i].gyro_y);
			gyro_z_var += square(raw_data[i].gyro_z);
		}
		high_g_x_mean /=count;
		high_g_y_mean /=count;
		high_g_z_mean /=count; 
		low_g_x_mean /=count;
		low_g_y_mean /=count;
		low_g_z_mean /=count;
		gyro_x_mean /=count;
		gyro_y_mean /=count;
		gyro_z_mean /=count;
		
		high_g_x_var =  (high_g_x_var/count) - square(high_g_x_mean);
		high_g_y_var = (high_g_y_var/count) - square(high_g_y_mean);
		high_g_z_var = (high_g_z_var/count) - square(high_g_z_mean);
		low_g_x_var = (low_g_x_var/count) - square(low_g_x_mean);
		low_g_y_var = (low_g_y_var/count) - square(low_g_y_mean);
		low_g_z_var = (low_g_z_var/count) - square(low_g_z_mean);
		gyro_x_var = (gyro_x_var/count) - square(gyro_x_mean);
		gyro_y_var = (gyro_y_var/count) - square(gyro_y_mean);
		gyro_z_var = (gyro_z_var/count) - square(gyro_z_mean);
		
		double high_g_x_skew_t = 0.0;
		double high_g_x_skew_b = 0.0;
		double high_g_y_skew_t = 0.0;
		double high_g_y_skew_b = 0.0;
		double high_g_z_skew_t = 0.0;
		double high_g_z_skew_b = 0.0;
		double low_g_x_skew_t  = 0.0;
		double low_g_x_skew_b  = 0.0;
		double low_g_y_skew_t  = 0.0;
		double low_g_y_skew_b  = 0.0;
		double low_g_z_skew_t  = 0.0;
		double low_g_z_skew_b  = 0.0;
		double gyro_x_skew_t   = 0.0;
		double gyro_x_skew_b   = 0.0;
		double gyro_y_skew_t   = 0.0;
		double gyro_y_skew_b   = 0.0;
		double gyro_z_skew_t   = 0.0;
		double gyro_z_skew_b   = 0.0;
		double high_g_x_kurt_t = 0.0;
		double high_g_y_kurt_t = 0.0;
		double high_g_z_kurt_t = 0.0;
		double low_g_x_kurt_t  = 0.0;
		double low_g_y_kurt_t  = 0.0;
		double low_g_z_kurt_t  = 0.0;
		double gyro_x_kurt_t   = 0.0;
		double gyro_y_kurt_t   = 0.0;
		double gyro_z_kurt_t   = 0.0;
		
		double high_g_x_skew = 0.0;
		double high_g_y_skew = 0.0;
		double high_g_z_skew = 0.0;
		double low_g_x_skew = 0.0;
		double low_g_y_skew = 0.0;
		double low_g_z_skew = 0.0;
		double gyro_x_skew = 0.0;
		double gyro_y_skew = 0.0;
		double gyro_z_skew = 0.0;
		
		double high_g_x_kurt = 0.0;
		double high_g_y_kurt = 0.0;
		double high_g_z_kurt = 0.0;
		double low_g_x_kurt = 0.0;
		double low_g_y_kurt = 0.0;
		double low_g_z_kurt = 0.0;
		double gyro_x_kurt = 0.0;
		double gyro_y_kurt = 0.0;
		double gyro_z_kurt = 0.0;
		
		for(int i=0;i<raw_data.length;i++){
			high_g_x_skew_t += Math.pow(raw_data[i].high_g_x - high_g_x_mean, 3);
			high_g_x_skew_b += Math.pow(raw_data[i].high_g_x - high_g_x_mean, 2);
			high_g_y_skew_t += Math.pow(raw_data[i].high_g_y - high_g_y_mean, 3);
			high_g_y_skew_b += Math.pow(raw_data[i].high_g_y - high_g_y_mean, 2);
			high_g_z_skew_t += Math.pow(raw_data[i].high_g_z - high_g_z_mean, 3);
			high_g_z_skew_b += Math.pow(raw_data[i].high_g_z - high_g_z_mean, 2);
			
			low_g_x_skew_t += Math.pow(raw_data[i].low_g_x - low_g_x_mean, 3);
			low_g_x_skew_b += Math.pow(raw_data[i].low_g_x - low_g_x_mean, 2);
			low_g_y_skew_t += Math.pow(raw_data[i].low_g_y - low_g_y_mean, 3);
			low_g_y_skew_b += Math.pow(raw_data[i].low_g_y - low_g_y_mean, 2);
			low_g_z_skew_t += Math.pow(raw_data[i].low_g_z - low_g_z_mean, 3);
			low_g_z_skew_b += Math.pow(raw_data[i].low_g_z - low_g_z_mean, 2);
			
			gyro_x_skew_t += Math.pow(raw_data[i].gyro_x - gyro_x_mean, 3);
			gyro_x_skew_b += Math.pow(raw_data[i].gyro_x - gyro_x_mean, 2);
			gyro_y_skew_t += Math.pow(raw_data[i].gyro_y - gyro_y_mean, 3);
			gyro_y_skew_b += Math.pow(raw_data[i].gyro_y - gyro_y_mean, 2);
			gyro_z_skew_t += Math.pow(raw_data[i].gyro_z - gyro_z_mean, 3);
			gyro_z_skew_b += Math.pow(raw_data[i].gyro_z - gyro_z_mean, 2);
			
			high_g_x_kurt_t += Math.pow(raw_data[i].high_g_x-high_g_x_mean, 4);
			high_g_y_kurt_t += Math.pow(raw_data[i].high_g_y-high_g_y_mean, 4);
			high_g_z_kurt_t += Math.pow(raw_data[i].high_g_z-high_g_z_mean, 4);
			
			low_g_x_kurt_t += Math.pow(raw_data[i].low_g_x-low_g_x_mean, 4);
			low_g_y_kurt_t += Math.pow(raw_data[i].low_g_y-low_g_y_mean, 4);
			low_g_z_kurt_t += Math.pow(raw_data[i].low_g_z-low_g_z_mean, 4);
			
			gyro_x_kurt_t += Math.pow(raw_data[i].gyro_x-gyro_x_mean, 4);
			gyro_y_kurt_t += Math.pow(raw_data[i].gyro_y-gyro_y_mean, 4);
			gyro_z_kurt_t += Math.pow(raw_data[i].gyro_z-gyro_z_mean, 4);
			
				
		}

		high_g_x_skew = (high_g_x_skew_t/count) / Math.pow(high_g_x_skew_b/(count-1.0), 1.5);
		high_g_y_skew = (high_g_y_skew_t/count) / Math.pow(high_g_y_skew_b/(count-1.0), 1.5);
		high_g_z_skew = (high_g_z_skew_t/count) / Math.pow(high_g_z_skew_b/(count-1.0), 1.5);
		low_g_x_skew = (low_g_x_skew_t/count) / Math.pow(low_g_x_skew_b/(count-1.0), 1.5);
		low_g_y_skew = (low_g_y_skew_t/count) / Math.pow(low_g_y_skew_b/(count-1.0), 1.5);
		low_g_z_skew = (low_g_z_skew_t/count) / Math.pow(low_g_z_skew_b/(count-1.0), 1.5);
		gyro_x_skew = (gyro_x_skew_t/count) / Math.pow(gyro_x_skew_b/(count-1.0), 1.5);
		gyro_y_skew = (gyro_y_skew_t/count) / Math.pow(gyro_y_skew_b/(count-1.0), 1.5);
		gyro_z_skew = (gyro_z_skew_t/count) / Math.pow(gyro_z_skew_b/(count-1.0), 1.5);
		
		high_g_x_kurt = (high_g_x_kurt_t/count) / Math.pow( (high_g_x_skew_b/(count-1.0)), 2 );
		high_g_y_kurt = (high_g_y_kurt_t/count) / Math.pow( (high_g_y_skew_b/(count-1.0)), 2 );
		high_g_z_kurt = (high_g_z_kurt_t/count) / Math.pow( (high_g_z_skew_b/(count-1.0)), 2 );
		low_g_x_kurt  = (low_g_x_kurt_t/count)  / Math.pow( (low_g_x_skew_b/(count-1.0)) , 2 );
		low_g_y_kurt  = (low_g_y_kurt_t/count)  / Math.pow( (low_g_y_skew_b/(count-1.0)) , 2 );
		low_g_z_kurt  = (low_g_z_kurt_t/count)  / Math.pow( (low_g_z_skew_b/(count-1.0)) , 2 );
		gyro_x_kurt   = (gyro_x_kurt_t/count)   / Math.pow( (gyro_x_skew_b/(count-1.0))  , 2 );
		gyro_y_kurt   = (gyro_y_kurt_t/count)   / Math.pow( (gyro_y_skew_b/(count-1.0))  , 2 );
		gyro_z_kurt   = (gyro_z_kurt_t/count)   / Math.pow( (gyro_z_skew_b/(count-1.0))  , 2 );

		double [] ret = {high_g_x_mean,
							high_g_y_mean,
							high_g_z_mean,
							low_g_x_mean,
							low_g_y_mean,
							low_g_z_mean,
							gyro_x_mean,
							gyro_y_mean,
							gyro_z_mean,
							high_g_x_var,
							high_g_y_var,
							high_g_z_var,
							low_g_x_var,
							low_g_y_var,
							low_g_z_var,
							gyro_x_var,
							gyro_y_var,
							gyro_z_var,
							high_g_x_skew,
							high_g_y_skew,
							high_g_z_skew,
							low_g_x_skew,
							low_g_y_skew,
							low_g_z_skew,
							gyro_x_skew,
							gyro_y_skew,
							gyro_z_skew,
							high_g_x_kurt,
							high_g_y_kurt,
							high_g_z_kurt,
							low_g_x_kurt,
							low_g_y_kurt,
							low_g_z_kurt,
							gyro_x_kurt,
							gyro_y_kurt,
							gyro_z_kurt};
		return ret;
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