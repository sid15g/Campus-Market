ifdef SystemRoot
	RM = del /Q
	CLASS = \*.class
	XML = \*.xml
	
else
	ifeq ($(shell uname), Linux)
		RM = rm -rf
		CLASS = /*.class
		XML = /*.xml
	endif
endif

JCC = javac
JFLAGS = -Xlint:all -g

Start.class: Start.java	
	$(JCC) $(JFLAGS) Start.java
	
jar:
	jar cmf Manifest.txt campus-market.jar Start.class com data errors handle modules org settings view
	
clean:
	$(RM) campus-market.jar
	$(RM) Start.class
	$(RM) data$(CLASS)
	$(RM) errors$(CLASS)
	$(RM) handle$(CLASS)
	$(RM) modules$(CLASS)
	$(RM) settings$(CLASS)
	$(RM) settings$(XML)
	$(RM) view$(CLASS)	