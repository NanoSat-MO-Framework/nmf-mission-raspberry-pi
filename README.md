# NanoSat MO Framework - Mission: Raspberry Pi
This repository contains the implementation of the NanoSat MO Framework to run on a Raspberry Pi.
The NMF on the Raspberry Pi can be configured to run with or without a simulator attached to it. The simulator provides most of the platform functionalities accessible to the experimenter. The simulator allows running NMF applications without an access to a real satellite hardware.

The project must be compiled in the development machine and will be transfered to the Raspberry Pi (testing machine) via scp during compilation. Then, one must install it, and run it.
The file transfer is directly part of the compilation in order to facilitate the delivery of the files to the Raspberry Pi during the development process: **Develop, Compile, Run!**

The official website of the NanoSat MO Framework is available [here].

## Why should you install the NMF on a Raspberry Pi?

The NanoSat MO Framework is expected to run on advanced on-board computers available on the latest satellites. These new satellites typically have ARM architectures with Linux-based Operating Systems, usually built with the Yocto project.

The installation of the NMF on a Raspberry Pi provides a cheap testbed environment for trying out the software. This is particular useful because of the similar characteristics of the Raspberry Pi to the cutting-edge ESA radiation-hardened on-board computers flying in space. Development and debugging can be quickly performed.

# Configuring the Raspberry Pi

**Note: This guide was created using a Raspberry Pi 3B with 1 GB of RAM and Raspbian 10 (Buster) OS.**

1. By default, the root password is disabled on the Raspberry Pi. Set a root password: ```sudo passwd```

2. Enable SSH on the Raspberry Pi: ```sudo raspi-config```  (Interface Options -> SSH)

3. Allow SSH access for root:
	- Edit the **/etc/ssh/sshd_config** file: ```sudo nano /etc/ssh/sshd_config ```
	- Replace the line: **#PermitRootLogin prohibit-password** with **PermitRootLogin yes**
	- Save and exit (CTRL+O, CTRL+X)
	- Restart SSH: ```sudo service ssh restart```

# Building the project

1. Follow the Readme instructions on the [NanoSat MO Framework](https://github.com/esa/nanosat-mo-framework/) project before continuing to the next steps

* This mission is currently working only with the phi-sat-2 branch, that adds some dependencioes the dev branch does not provide. to switch to this branch do `git checkout phi-sat-2` in the nanosat-mo-framework directory.
* run `mvn install` both in the project root and in the sdk folder  


2. Clone this (nmf-mission-raspberry-pi) repository:
```
git clone https://github.com/NanoSat-MO-Framework/nmf-mission-raspberry-pi.git
```

3. Go to the pom file of space-file-system project and edit the IP Address to match the Raspberry Pi machine on:
```xml
		<configuration>
			<fromDir>${mission.outputdir}/${out.folder.nmf}</fromDir>
			<url>scp://root@192.168.178.33/</url>
			<toDir>${out.folder.nmf}</toDir>
		</configuration>
```
* use `scpexe://` instead of `scp://` if you want to use your system's ssh system instead of the java ssh2 libraries. Recommended on Linux and Mac.

4. Build the **nmf-mission-raspberry-pi** project:
```
mvn clean install
```

5. During compilation, the space-file-system project will request the root password of the Raspberry Pi remote machine in order to be execute the files transfer.

**Warning:** This step will not work if the Raspberry Pi was not configured as presented in the section above.

# Installing the NMF on your Raspberry Pi and running it

These are a continuation of the steps above:

1. Install Java on the Raspberry Pi if not installed yet
```bash
sudo apt-get install openjdk-8-jdk
```

2. Execute the following commmands on the /nanosat-mo-framework folder:
```
cd /nanosat-mo-framework/
sudo chmod +x fresh_install.sh
sudo ./fresh_install.sh
```

**Warning**: If you get the error "sudo: unable to execute ./fresh_install.sh: No such file or directory", it is because the file was generated in a Windows machine and has CRLF ending. Fix it with:
```
sudo apt-get install dos2unix
sudo dos2unix fresh_install.sh
sudo ./fresh_install.sh
```

3. The installation should output the message: "Success! The NanoSat MO Framework was installed!". Otherwise, please read the error and fix it accordingly.

4. Start the NanoSat MO Supervisor with the command: ```sudo ./start_supervisor.sh```

# Docker
This mission comes with a Dockerfile that allows you to deploy the NanoSat MO Framework in a Docker container. This allows for multiple NanoSat segments to be deployed on one machine, which can be useful for testing, development or simulation purposes. Use the following steps to create and start a container with the name _nmf-supervisor_:

1. Clone this repository and navigate to its root directory.
2. Build Docker image: ```sudo docker build -t nmf/raspberry-pi .``` 
3. Run a Docker container: ```sudo docker run --name nmf-supervisor nmf/raspberry-pi```

To access the container, find its IP address: ```sudo docker inspect nmf-supervisor | grep IPAddress```

More information on how to use Docker can be found in the [official Docker documentation](https://docs.docker.com/).

# Bugs Reporting
Bug Reports can be submitted on: [Bug Reports]

Or directly in the respective source code repository.

# License
This software is released under MIT license, available [here](LICENSE).

	
[NMFImage]: http://nanosat-mo-framework.github.io/img/NMF_logo_1124_63.png
[NanoSat MO Framework]: https://nanosat-mo-framework.github.io/
[here]: https://nanosat-mo-framework.github.io/
[GitHub]: https://github.com/esa/nanosat-mo-framework
[Bug Reports]: https://github.com/esa/nanosat-mo-framework/issues
