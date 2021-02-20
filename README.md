# NanoSat MO Framework - Mission: Raspberry Pi
This repository contains the implementation of the NanoSat MO Framework to run on a Raspberry Pi.

The content of this repository is mostly relevant for a deployment on a Raspberry Pi.

During the development it is recommended to use the NMF SDK (released as a source in the main NMF repository on [GitHub]).
The SDK is based on a simulator, providing most of the platform functionalities accessible to the experimenter. Simulator allows running NMF applications without an access to a real satellite hardware.

The official website of the NanoSat MO Framework is available [here].

# Building the project
You can build this project for two scenarios. In the first scenario you want a minimal build to put onto the Raspberry Pi. You can produce this build by just running 'mvn install' in the root directory of 
this repository. The second scenario allows you to test and simulate your apps in order to validate their correctness and later connect to your apps on the spacecraft using the Ground MO Proxy. 
This is done by providing a separate build profile which can be invoked by running 'mvn install -Pground' inside the main directory of this repository.

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
