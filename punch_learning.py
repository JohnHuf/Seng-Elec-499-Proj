# The gist:
# 1. The header for the arff file is constructed
# 2. Basically the script goes through every file and calculates the mean, var, skewness, and kurtosis
# 3. Time is not factored in
# 4. It writes this data to the arff file, and adds the class each datum falls into
# 5. This arff file can then be run through weka
#

# FORMAT FOR MINING
out = open("punch_data.arff", "w")
out.write("@relation punches\n\n")
# Write headers for mean
out.write("@attribute high_g_x_mean numeric\n")
out.write("@attribute high_g_y_mean numeric\n")
out.write("@attribute high_g_z_mean numeric\n")
out.write("@attribute low_g_x_mean numeric\n")
out.write("@attribute low_g_y_mean numeric\n")
out.write("@attribute low_g_z_mean numeric\n")
out.write("@attribute gyro_x_mean numeric\n")
out.write("@attribute gyro_y_mean numeric\n")
out.write("@attribute gyro_z_mean numeric\n")
# Write headers for var
out.write("@attribute high_g_x_var numeric\n")
out.write("@attribute high_g_y_var numeric\n")
out.write("@attribute high_g_z_var numeric\n")
out.write("@attribute low_g_x_var numeric\n")
out.write("@attribute low_g_y_var numeric\n")
out.write("@attribute low_g_z_var numeric\n")
out.write("@attribute gyro_x_var numeric\n")
out.write("@attribute gyro_y_var numeric\n")
out.write("@attribute gyro_z_var numeric\n")
# Write headers for skew
out.write("@attribute high_g_x_skew numeric\n")
out.write("@attribute high_g_y_skew numeric\n")
out.write("@attribute high_g_z_skew numeric\n")
out.write("@attribute low_g_x_skew numeric\n")
out.write("@attribute low_g_y_skew numeric\n")
out.write("@attribute low_g_z_skew numeric\n")
out.write("@attribute gyro_x_skew numeric\n")
out.write("@attribute gyro_y_skew numeric\n")
out.write("@attribute gyro_z_skew numeric\n")
# Write headers for skew
out.write("@attribute high_g_x_kurt numeric\n")
out.write("@attribute high_g_y_kurt numeric\n")
out.write("@attribute high_g_z_kurt numeric\n")
out.write("@attribute low_g_x_kurt numeric\n")
out.write("@attribute low_g_y_kurt numeric\n")
out.write("@attribute low_g_z_kurt numeric\n")
out.write("@attribute gyro_x_kurt numeric\n")
out.write("@attribute gyro_y_kurt numeric\n")
out.write("@attribute gyro_z_kurt numeric\n")
# Write header for punch type
out.write("@attribute punch_type {jab, hook, uppercut}\n")
out.write("\n@data\n")

import os
directorynames = {"punchdata/C/jab/", "punchdata/C/hook/", "punchdata/C/uppercut/",
		  "punchdata/J/jab/", "punchdata/J/hook/", "punchdata/J/uppercut/",
		  "punchdata/F/jab/", "punchdata/F/hook/", "punchdata/F/uppercut/",
		  "punchdata/B/jab/", "punchdata/B/hook/",
		  "punchdata/A/jab/", "punchdata/A/hook/", "punchdata/A/uppercut/",
		  "punchdata/R/jab/", "punchdata/R/hook/", "punchdata/R/uppercut/"}
for directory in directorynames:
	for file in os.listdir(directory):
		# Open a file
		filename = directory +file
		fo = open(filename, "r")

		# Instantiate mean and var variables to 0
		high_g_x_mean = 0.0
		high_g_y_mean = 0.0
		high_g_z_mean = 0.0
		low_g_x_mean  = 0.0
		low_g_y_mean  = 0.0
		low_g_z_mean  = 0.0
		gyro_x_mean   = 0.0
		gyro_y_mean   = 0.0
		gyro_z_mean   = 0.0
		high_g_x_var = 0.0
		high_g_y_var = 0.0
		high_g_z_var = 0.0
		low_g_x_var  = 0.0
		low_g_y_var  = 0.0
		low_g_z_var  = 0.0
		gyro_x_var   = 0.0
		gyro_y_var   = 0.0
		gyro_z_var   = 0.0

		count = 0.0
		# Manipulate Data
		for line in fo:
			count += 1.0

			# Find means
			high_g_x_mean += float(line.split(',')[2])
			high_g_y_mean += float(line.split(',')[3])
			high_g_z_mean += float(line.split(',')[4])
			low_g_x_mean  += float(line.split(',')[5])
			low_g_y_mean  += float(line.split(',')[6])
			low_g_z_mean  += float(line.split(',')[7])
			gyro_x_mean   += float(line.split(',')[8])
			gyro_y_mean   += float(line.split(',')[9])
			gyro_z_mean   += float(line.split(',')[10])

			# Find variance
			high_g_x_var += float(line.split(',')[2]) * float(line.split(',')[2])
			high_g_y_var += float(line.split(',')[3]) * float(line.split(',')[3])
			high_g_z_var += float(line.split(',')[4]) * float(line.split(',')[4])
			low_g_x_var  += float(line.split(',')[5]) * float(line.split(',')[5])
			low_g_y_var  += float(line.split(',')[6]) * float(line.split(',')[6])
			low_g_z_var  += float(line.split(',')[7]) * float(line.split(',')[7])
			gyro_x_var   += float(line.split(',')[8]) * float(line.split(',')[8])
			gyro_y_var   += float(line.split(',')[9]) * float(line.split(',')[9])
			gyro_z_var   += float(line.split(',')[10]) * float(line.split(',')[10])
		
		# Calculate mean for each
		high_g_x_mean /= count
		high_g_y_mean /= count
		high_g_z_mean /= count
		low_g_x_mean  /= count
		low_g_y_mean  /= count
		low_g_z_mean  /= count
		gyro_x_mean   /= count
		gyro_y_mean   /= count
		gyro_z_mean   /= count

		# Calculate variance for each
		high_g_x_var = (high_g_x_var/count) - (high_g_x_mean*high_g_x_mean)
		high_g_y_var = (high_g_y_var/count) - (high_g_y_mean*high_g_y_mean)
		high_g_z_var = (high_g_z_var/count) - (high_g_z_mean*high_g_z_mean)
		low_g_x_var  = (low_g_x_var/count)  - (low_g_x_mean*low_g_x_mean)
		low_g_y_var  = (low_g_y_var/count)  - (low_g_y_mean*low_g_y_mean)
		low_g_z_var  = (low_g_z_var/count)  - (low_g_z_mean*low_g_z_mean)
		gyro_x_var   = (gyro_x_var/count)   - (gyro_x_mean*gyro_x_mean)
		gyro_y_var   = (gyro_y_var/count)   - (gyro_y_mean*gyro_y_mean)
		gyro_z_var   = (gyro_z_var/count)   - (gyro_z_mean*gyro_z_mean)

		# Calculate skewness

		# Initialize skewness and kurtosis components to 0.0
		high_g_x_skew_t = 0.0
		high_g_x_skew_b = 0.0
		high_g_y_skew_t = 0.0
		high_g_y_skew_b = 0.0
		high_g_z_skew_t = 0.0
		high_g_z_skew_b = 0.0
		low_g_x_skew_t  = 0.0
		low_g_x_skew_b  = 0.0
		low_g_y_skew_t  = 0.0
		low_g_y_skew_b  = 0.0
		low_g_z_skew_t  = 0.0
		low_g_z_skew_b  = 0.0
		gyro_x_skew_t   = 0.0
		gyro_x_skew_b   = 0.0
		gyro_y_skew_t   = 0.0
		gyro_y_skew_b   = 0.0
		gyro_z_skew_t   = 0.0
		gyro_z_skew_b   = 0.0
		high_g_x_kurt_t = 0.0
		high_g_y_kurt_t = 0.0
		high_g_z_kurt_t = 0.0
		low_g_x_kurt_t  = 0.0
		low_g_y_kurt_t  = 0.0
		low_g_z_kurt_t  = 0.0
		gyro_x_kurt_t   = 0.0
		gyro_y_kurt_t   = 0.0
		gyro_z_kurt_t   = 0.0

		fo.close()
		filename = directory +file
		fo = open(filename, "r")
		for line2 in fo:

			# Find top and bottom skew chunks
			high_g_x_skew_t += pow(( float(line2.split(',')[2]) - high_g_x_mean ),3)
			high_g_x_skew_b += pow(( float(line2.split(',')[2]) - high_g_x_mean ),2)
			high_g_y_skew_t += pow(( float(line2.split(',')[3]) - high_g_x_mean ),3)
			high_g_y_skew_b += pow(( float(line2.split(',')[3]) - high_g_x_mean ),2)
			high_g_z_skew_t += pow(( float(line2.split(',')[4]) - high_g_x_mean ),3)
			high_g_z_skew_b += pow(( float(line2.split(',')[4]) - high_g_x_mean ),2)
			low_g_x_skew_t  += pow(( float(line2.split(',')[5]) - high_g_x_mean ),3)
			low_g_x_skew_b  += pow(( float(line2.split(',')[5]) - high_g_x_mean ),2)
			low_g_y_skew_t  += pow(( float(line2.split(',')[6]) - high_g_x_mean ),3)
			low_g_y_skew_b  += pow(( float(line2.split(',')[6]) - high_g_x_mean ),2)
			low_g_z_skew_t  += pow(( float(line2.split(',')[7]) - high_g_x_mean ),3)
			low_g_z_skew_b  += pow(( float(line2.split(',')[7]) - high_g_x_mean ),2)
			gyro_x_skew_t   += pow(( float(line2.split(',')[8]) - high_g_x_mean ),3)
			gyro_x_skew_b   += pow(( float(line2.split(',')[8]) - high_g_x_mean ),2)
			gyro_y_skew_t   += pow(( float(line2.split(',')[9]) - high_g_x_mean ),3)
			gyro_y_skew_b   += pow(( float(line2.split(',')[9]) - high_g_x_mean ),2)
			gyro_z_skew_t   += pow(( float(line2.split(',')[10]) - high_g_x_mean ),3)
			gyro_z_skew_b   += pow(( float(line2.split(',')[10]) - high_g_x_mean ),2)

			# Find top kurtosis chunk (bottom is *_skew_b)
			high_g_x_kurt_t += pow(( float(line2.split(',')[2]) - high_g_x_mean ),4)
			high_g_y_kurt_t += pow(( float(line2.split(',')[3]) - high_g_x_mean ),4)
			high_g_z_kurt_t += pow(( float(line2.split(',')[4]) - high_g_x_mean ),4)
			low_g_x_kurt_t  += pow(( float(line2.split(',')[5]) - high_g_x_mean ),4)
			low_g_y_kurt_t  += pow(( float(line2.split(',')[6]) - high_g_x_mean ),4)
			low_g_z_kurt_t  += pow(( float(line2.split(',')[7]) - high_g_x_mean ),4)
			gyro_x_kurt_t   += pow(( float(line2.split(',')[8]) - high_g_x_mean ),4)
			gyro_y_kurt_t   += pow(( float(line2.split(',')[9]) - high_g_x_mean ),4)
			gyro_z_kurt_t   += pow(( float(line2.split(',')[10]) - high_g_x_mean ),4)

		# Finish skewness calculation
		high_g_x_skew = 0.0
		high_g_y_skew = 0.0
		high_g_z_skew = 0.0
		low_g_x_skew  = 0.0
		low_g_y_skew  = 0.0
		low_g_z_skew  = 0.0
		gyro_x_skew   = 0.0
		gyro_y_skew   = 0.0
		gyro_z_skew   = 0.0

		high_g_x_skew = (high_g_x_skew_t/count) / pow( (high_g_x_skew_b/(count-1.0)), (3/2) )
		high_g_y_skew = (high_g_y_skew_t/count) / pow( (high_g_y_skew_b/(count-1.0)), (3/2) )
		high_g_z_skew = (high_g_z_skew_t/count) / pow( (high_g_z_skew_b/(count-1.0)), (3/2) )
		low_g_x_skew  = (low_g_x_skew_t/count)  / pow( (low_g_x_skew_b/(count-1.0)) , (3/2) )
		low_g_y_skew  = (low_g_y_skew_t/count)  / pow( (low_g_y_skew_b/(count-1.0)) , (3/2) )
		low_g_z_skew  = (low_g_z_skew_t/count)  / pow( (low_g_z_skew_b/(count-1.0)) , (3/2) )
		gyro_x_skew   = (gyro_x_skew_t/count)   / pow( (gyro_x_skew_b/(count-1.0))  , (3/2) )
		gyro_y_skew   = (gyro_y_skew_t/count)   / pow( (gyro_y_skew_b/(count-1.0))  , (3/2) )
		gyro_z_skew   = (gyro_z_skew_t/count)   / pow( (gyro_z_skew_b/(count-1.0))  , (3/2) )

		# Finish kurtosis calculation
		high_g_x_kurt = (high_g_x_kurt_t/count) / pow( (high_g_x_skew_b/(count-1.0)), 2 )
		high_g_y_kurt = (high_g_y_kurt_t/count) / pow( (high_g_y_skew_b/(count-1.0)), 2 )
		high_g_z_kurt = (high_g_z_kurt_t/count) / pow( (high_g_z_skew_b/(count-1.0)), 2 )
		low_g_x_kurt  = (low_g_x_kurt_t/count)  / pow( (low_g_x_skew_b/(count-1.0)) , 2 )
		low_g_y_kurt  = (low_g_y_kurt_t/count)  / pow( (low_g_y_skew_b/(count-1.0)) , 2 )
		low_g_z_kurt  = (low_g_z_kurt_t/count)  / pow( (low_g_z_skew_b/(count-1.0)) , 2 )
		gyro_x_kurt   = (gyro_x_kurt_t/count)   / pow( (gyro_x_skew_b/(count-1.0))  , 2 )
		gyro_y_kurt   = (gyro_y_kurt_t/count)   / pow( (gyro_y_skew_b/(count-1.0))  , 2 )
		gyro_z_kurt   = (gyro_z_kurt_t/count)   / pow( (gyro_z_skew_b/(count-1.0))  , 2 )
			
		fo.close()

		# WRITE PUNCH METRICS TO ARFF FILE
		s = str(high_g_x_mean) +","+str(high_g_y_mean) +","+str(high_g_z_mean)+","+str(low_g_x_mean)  +","+str(low_g_y_mean)  +","+str(low_g_z_mean)+","+str(gyro_x_mean)   +","+str(gyro_y_mean)   +","+str(gyro_z_mean)+","+str(high_g_x_var)  +","+str(high_g_y_var)  +","+str(high_g_z_var)+","+str(low_g_x_var)   +","+str(low_g_y_var)   +","+str(low_g_z_var)+","+str(gyro_x_var)    +","+str(gyro_y_var)    +","+str(gyro_z_var) +","+str(high_g_x_skew) +","+str(high_g_y_skew) +","+str(high_g_z_skew)+","+str(low_g_x_skew)  +","+str(low_g_y_skew)  +","+str(low_g_z_skew)+","+str(gyro_x_skew)   +","+str(gyro_y_skew)   +","+str(gyro_z_skew)+","+str(high_g_x_kurt) +","+str(high_g_y_kurt) +","+str(high_g_z_kurt)+","+str(low_g_x_kurt)  +","+str(low_g_y_kurt)  +","+str(low_g_z_kurt)+","+str(gyro_x_kurt)   +","+str(gyro_y_kurt)   +","+str(gyro_z_kurt)+","+directory[12:-1]+"\n"

		out.write(s)
