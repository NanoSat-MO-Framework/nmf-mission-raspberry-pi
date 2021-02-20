#!/bin/bash
cd ${0%/*}
java -classpath "libs/*" esa.mo.nmf.provider.NanoSatMOSupervisorRaspberryPiImpl
