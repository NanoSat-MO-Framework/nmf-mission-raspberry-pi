# NanoSat MO Framework - Mission: Raspberry Pi
This repository contains the implementation of the NanoSat MO Framework to run on a Raspberry Pi.
The NMF on the Raspberry Pi can be configured to run with or without a simulator attached to it. The simulator provides most of the platform functionalities accessible to the experimenter. The simulator allows running NMF applications without an access to a real satellite hardware.

The official website of the NanoSat MO Framework is available [here].

## Why should you install the NMF on a Raspberry Pi?

The NanoSat MO Framework is expected to run on advanced on-board computers available on the latest satellites. These new satellites typically have ARM architectures with Linux-based Operating Systems, usually built with the Yocto project.
The installation of the NMF on a Raspberry Pi provides a cheap testbed environment for trying out the software during development which has similar characteristics to the cutting-edge ESA radiation-hardened on-board computers flying in space.

# Configuring the Raspberry Pi

Note: This guide was created using a Raspberry Pi 3B with 1 GB of RAM and Raspbian 10 (Buster) OS.

1. By default, the root password is disabled on the Raspberry Pi. Set a root password:
```bash
sudo passwd
```

2. Enable ssh on the Raspberry Pi

3. Allow ssh access for root:
	- Edit the **/etc/ssh/sshd_config** file:
```bash
sudo nano /etc/ssh/sshd_config
```
	- Replace the line **"#PermitRootLogin prohibit-password"** with **"PermitRootLogin yes"**.
	- Save and exit (CTRL+O, CTRL+X)
	- Restart SSH:
```bash
sudo service ssh restart
```

# Building the project

1. Follow the Readme instructions on the NanoSat MO Framework project (select "phi-sat-2" branch):
[NanoSat MO Framework](https://github.com/esa/nanosat-mo-framework/tree/phi-sat-2)

2. Clone the this (nmf-mission-raspberry-pi) repository:
```
https://github.com/NanoSat-MO-Framework/nmf-mission-raspberry-pi.git
```

4. Go to the space-file-system project and edit the IP Address to match the Raspberry Pi machine on:
```
                        <configuration>
                            <fromDir>${mission.outputdir}/${out.folder.nmf}</fromDir>
                            <url>scp://root@192.168.178.33/</url>
                            <toDir>${out.folder.nmf}</toDir>
                        </configuration>
```

5. ==This step will not work if the Raspberry Pi was not configured as presented in the section above.== build the **nmf-mission-raspberry-pi** project. During compilation, the space-file-system project will request the root password of the Raspberry Pi machine in order to be execute the files transfer.
```
mvn clean install
```

# Installing the NMF on your Raspberry Pi and running it

These are a continuation of the steps above:

1. Install Java on the Raspberry Pi if not installed yet
```bash
sudo apt-get install openjdk-8-jdk
```

2. Execute the following commmand on the /nanosat-mo-framework folder:
```
cd /nanosat-mo-framework/
sudo chmod +x fresh_install.sh
sudo ./fresh_install.sh
```

==Warning==: If you get the error "sudo: unable to execute ./fresh_install.sh: No such file or directory", it is because the file was generated in a Windows machine and has CRLF ending. Fix it with:
```
sudo apt-get install dos2unix
sudo dos2unix fresh_install.sh
sudo ./fresh_install.sh
```

3. The installation should output the message: "Success! The NanoSat MO Framework was installed!". Otherwise, please read the error and fix it accordingly.

4. Start the NanoSat MO Supervisor with the command:
```
sudo ./start_supervisor.sh
```

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
