# NanoSat MO Framework - Mission: Raspberry Pi
This repository contains the implementation of the NanoSat MO Framework to run on a Raspberry Pi.

The content of this repository is mostly relevant for a deployment on a Raspberry Pi.

During the development it is recommended to use the NMF SDK (released as a source in the main NMF repository on [GitHub]).
The SDK is based on a simulator, providing most of the platform functionalities accessible to the experimenter. Simulator allows running NMF applications without an access to a real satellite hardware.

The official website of the NanoSat MO Framework is available [here].

# Building the project

1. Clone the NanoSat MO Framework and switch to the "phi-sat-2 branch":
```
https://github.com/esa/nanosat-mo-framework.git
```

2. Clone the nmf-mission-raspberry-pi repository:
```
https://github.com/NanoSat-MO-Framework/nmf-mission-raspberry-pi.git
```

3. Go to the space-file-system project and edit the IP Address to the one of the Raspberry Pi machine on the following line:
```
https://github.com/NanoSat-MO-Framework/nmf-mission-raspberry-pi/blob/master/space-file-system/pom.xml#L151
```

4. Compile both projects in the same order as above. During compilation, the nmf-mission-raspberry-pi will request the root password to be able to execute the file transfer.

# Installing the NMF on your Raspberry Pi

These are a continuation of the steps above:

5. Login as root and execute the following commmand on the /nanosat-mo-framework folder:
```
./fresh_install.sh
```

6. The installation should output the message: "Success! The NanoSat MO Framework was installed!". Otherwise, please read the error and fix it accordingly.

7. Start the NanoSat MO Supervisor with the command:
```
./start_supervisor.sh
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
