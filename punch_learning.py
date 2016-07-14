# The gist:
# 1. The header for the arff file is constructed
# 2. Basically the script goes through every file and grabs the highest value and the time it occurs
# 2a. It tracks time by just counting an iterator
# 3. It offsets the times so that the peak that occurs first occurs at time 0
# 4. It writes this data to the arff file, and adds the class each datum falls into
# 5. This arff file can then be run through weka
#
# NOTES:
# This pulls roughly 90% classification accuracy with Naive Bayes and 10-Fold cross validation
# It does an awful job of classifying noise, but afaik we only have two samplesof noise, so more samples would probably help


# FORMAT FOR MINING
out = open("punch_data.arff", "w")
out.write("@relation punches\n\n")
out.write("@attribute high_g_x numeric\n")
out.write("@attribute high_g_x_time numeric\n")
out.write("@attribute high_g_y numeric\n")
out.write("@attribute high_g_y_time numeric\n")
out.write("@attribute high_g_z numeric\n")
out.write("@attribute high_g_z_time numeric\n")
out.write("@attribute low_g_x numeric\n")
out.write("@attribute low_g_x_time numeric\n")
out.write("@attribute low_g_y numeric\n")
out.write("@attribute low_g_y_time numeric\n")
out.write("@attribute low_g_z numeric\n")
out.write("@attribute low_g_z_time numeric\n")
out.write("@attribute gyro_x numeric\n")
out.write("@attribute gyro_x_time numeric\n")
out.write("@attribute gyro_y numeric\n")
out.write("@attribute gyro_y_time numeric\n")
out.write("@attribute gyro_z numeric\n")
out.write("@attribute gyro_z_time numeric\n")
out.write("@attribute punch_type {jab, hook, uppercut, noise}\n")
out.write("\n@data\n")

import os
directorynames = {"jab/", "hook/", "uppercut/", "noise/"}
for directory in directorynames:
	for file in os.listdir(directory):
		# Open a file
		filename = directory +file
		fo = open(filename, "r")
		#fo = open("hook/hook(2).csv", "r")
		#print "Name of the file: ", fo.name
		#print "Closed or not : ", fo.closed
		#print "Opening mode : ", fo.mode
		#print "Softspace flag : ", fo.softspace

		# FIND GLOBAL SENSOR MAX
		# name = (time, max_val)
		max_high_g_x = [0, -999]
		max_high_g_y = [0, -999]
		max_high_g_z = [0, -999]
		max_low_g_x  = [0, -999]
		max_low_g_y  = [0, -999]
		max_low_g_z  = [0, -999]
		max_gyro_x   = [0, -999]
		max_gyro_y   = [0, -999]
		max_gyro_z   = [0, -999]

		count = 0
		for line in fo:
		   count+=1
		   if (float(line.split(',')[2])>float(max_high_g_x[1])):
		      max_high_g_x[0] = count
		      max_high_g_x[1] = float(line.split(',')[2])

		   if (float(line.split(',')[3])>float(max_high_g_y[1])):
		      max_high_g_y[0] = count
		      max_high_g_y[1] = float(line.split(',')[3])

		   if (float(line.split(',')[4])>float(max_high_g_z[1])):
		      max_high_g_z[0] = count
		      max_high_g_z[1] = float(line.split(',')[4])

		   if (float(line.split(',')[5])>float(max_low_g_x[1])):
		      max_low_g_x[0] = count
		      max_low_g_x[1] = float(line.split(',')[5])

		   if (float(line.split(',')[6])>float(max_low_g_y[1])):
		      max_low_g_y[0] = count
		      max_low_g_y[1] = float(line.split(',')[6])

		   if (float(line.split(',')[7])>float(max_low_g_z[1])):
		      max_low_g_z[0] = count
		      max_low_g_z[1] = float(line.split(',')[7])

		   if (float(line.split(',')[8])>float(max_gyro_x[1])):
		      max_gyro_x[0] = count
		      max_gyro_x[1] = float(line.split(',')[8])

		   if (float(line.split(',')[9])>float(max_gyro_y[1])):
		      max_gyro_y[0] = count
		      max_gyro_y[1] = float(line.split(',')[9])

		   if (float(line.split(',')[10])>float(max_gyro_z[1])):
		      max_gyro_z[0] = count
		      max_gyro_z[1] = float(line.split(',')[10])

		fo.close()
		# OFFSET TO EQUALIZE TIMER
		offset = min(max_high_g_x[0],max_high_g_y[0],max_high_g_z[0],max_low_g_x[0],max_low_g_y[0],max_low_g_z[0],max_gyro_x[0],max_gyro_y[0],max_gyro_z[0])

		max_high_g_x[0] -=offset
		max_high_g_y[0] -=offset
		max_high_g_z[0] -=offset
		max_low_g_x[0]  -=offset
		max_low_g_y[0]  -=offset
		max_low_g_z[0]  -=offset
		max_gyro_x[0]   -=offset
		max_gyro_y[0]   -=offset
		max_gyro_z[0]   -=offset

#		print "\nMax high g x is: " + str(max_high_g_x[1]) + ", occuring at: " + str(max_high_g_x[0])
#		print "Max high g y is: "   + str(max_high_g_y[1]) + ", occuring at: " + str(max_high_g_y[0])
#		print "Max high g z is: "   + str(max_high_g_z[1]) + ", occuring at: " + str(max_high_g_z[0])
#		print "Max low g x is: "    + str(max_low_g_x[1])  + ", occuring at: " + str(max_low_g_x[0])
#		print "Max low g y is: "    + str(max_low_g_y[1])  + ", occuring at: " + str(max_low_g_y[0])
#		print "Max low g z is: "    + str(max_low_g_z[1])  + ", occuring at: " + str(max_low_g_z[0])
#		print "Max gyro x is: "     + str(max_gyro_x[1])   + ", occuring at: " + str(max_gyro_x[0])
#		print "Max gyro y is: "     + str(max_gyro_y[1])   + ", occuring at: " + str(max_gyro_y[0])
#		print "Max gyro z is: "     + str(max_gyro_z[1])   + ", occuring at: " + str(max_gyro_z[0])

		# WRITE PUNCH METRICS TO ARFF FILE
		s = str(max_high_g_x[1])+","+str(max_high_g_x[0])+","+str(max_high_g_y[1])+","+str(max_high_g_y[0])+","+str(max_high_g_z[1])+","+str(max_high_g_z[0])+","+str(max_low_g_x[1])+","+str(max_low_g_x[0])+","+str(max_low_g_y[1])+","+str(max_low_g_y[0])+","+str(max_low_g_z[1])+","+str(max_low_g_z[0])+","+str(max_gyro_x[1])+","+str(max_gyro_x[0])+","+str(max_gyro_y[1])+","+str(max_gyro_y[0])+","+str(max_gyro_z[1])+","+str(max_gyro_z[0])+","+directory[:-1]+"\n"
		out.write(s)
