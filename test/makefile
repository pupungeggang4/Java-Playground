compile:
	javac -cp .:lib/* HelloWorld.java

generate-linux-x64-jar:
	javac -cp .:lib/* HelloWorld.java
	jar -cvmf manifest-linux-x64.txt main.jar lib HelloWorld.class

generate-linux-x64-bin:
	javac -cp .:lib/* HelloWorld.java
	jar -cvmf manifest-linux-x64.txt main.jar HelloWorld.class
	native-image -jar main.jar main

dummy:
