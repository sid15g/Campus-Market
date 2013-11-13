
What is Campus Market?
Campus Market is an handy application for easing small scale marketing. 
The developers being students knew the importance of this software and hence have developed this for general purpose use. It has been developed to fulfil every type of user need like buying, selling and bidding.
The portability of it allows it to be used at any campus or university or organization safely and independently. 

How does it work?
To make it portable, the server setting has been left for the ADMIN of the particular campus. Initially ADMIN has to enter the server setting i.e. tell the software where its local database is located. Creation of database is to be done by the ADMIN on its server using the sql file provided.
As soon as the server settings is entered, the application saves it as xml, with encrypted details and would never ask for server settings again.
Note that the application runs on local network requiring IP addresses. So the server settings include the IP address of the server for the users to connect to the server.
The users will not be shown the server settings for security reasons.

How to run?

	ADMIN:
	Use the make file to first clean the class files:
		make clean
	Then compile the code by
		make
	Then use command promt or terminal to run the java application. At this time application will ask for server settings.
	After entering server settings a file named cloudSettings.xml would be created
	Now use the command
		make jar
	to create the jar file, and supply the jar files to the users
	
	USERS:
	Use command Prompt or Terminal to run the command
		java -jar <filename>.jar