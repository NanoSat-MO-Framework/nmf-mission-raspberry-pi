# Build and run the NMF Raspberry Pi mission.
#
# author: N Wiegand
#

FROM maven:3.8.6-openjdk-8 AS nmfMainBuilder

WORKDIR /nmf/

# clone NMF main repo and switch to phi-sat-2 branch
RUN git clone https://github.com/esa/nanosat-mo-framework.git
RUN cd /nmf/nanosat-mo-framework && git checkout v4.0

RUN sed -i 's/<module>camera-acquisitor-system-ground<\/module>/<!--<module>camera-acquisitor-system-ground<\/module>-->/g' /nmf/nanosat-mo-framework/sdk/examples/ground/pom.xml

# build phi-sat-2 branch of NMF main repo 
RUN cd /nmf/nanosat-mo-framework && mvn install

# build some apps
RUN cd /nmf/nanosat-mo-framework/sdk/examples/space/edge-ai && mvn clean install -Pgenerate-nmf-package
RUN cd /nmf/nanosat-mo-framework/sdk/examples/space/benchmark && mvn clean install -Pgenerate-nmf-package
RUN cd /nmf/nanosat-mo-framework/sdk/examples/space/gps && mvn clean install -Pgenerate-nmf-package
RUN cd /nmf/nanosat-mo-framework/sdk/examples/space/camera && mvn clean install -Pgenerate-nmf-package
RUN cd /nmf/nanosat-mo-framework/sdk/examples/space/hello-world-full && mvn clean install -Pgenerate-nmf-package
RUN cd /nmf/nanosat-mo-framework/sdk/examples/space/publish-clock && mvn clean install -Pgenerate-nmf-package


FROM maven:3.8.6-openjdk-8 AS nmfRpiMissionBuilder

WORKDIR /nmf/

COPY . .

# copy NMF main libs from previous build
RUN mkdir /root/.m2
COPY --from=nmfMainBuilder /root/.m2/ /root/.m2

# build NMF Raspberry Pi mission without SSH deployment
RUN sed -i 's/<skip>false/<skip>true/g' /nmf/space-file-system/pom.xml
RUN mvn install



FROM adoptopenjdk/openjdk8:alpine-jre AS nmfRpiMissionRunner

WORKDIR /nanosat-mo-framework/

RUN apk update && apk add sudo bash

# copy files from previous build and install the NMF Supervisor
COPY --from=nmfRpiMissionBuilder /nmf/space-file-system/target/space-file-system/nanosat-mo-framework/ /nanosat-mo-framework/
RUN apk add --no-cache sudo

RUN chmod +x fresh_install_busybox.sh && ./fresh_install_busybox.sh

# copy apps
COPY --from=nmfMainBuilder /nmf/nanosat-mo-framework/sdk/examples/space/edge-ai/target/*.nmfpack /nanosat-mo-framework/packages
COPY --from=nmfMainBuilder /nmf/nanosat-mo-framework/sdk/examples/space/benchmark/target/*.nmfpack /nanosat-mo-framework/packages
COPY --from=nmfMainBuilder /nmf/nanosat-mo-framework/sdk/examples/space/gps/target/*.nmfpack /nanosat-mo-framework/packages
COPY --from=nmfMainBuilder /nmf/nanosat-mo-framework/sdk/examples/space/camera/target/*.nmfpack /nanosat-mo-framework/packages
COPY --from=nmfMainBuilder /nmf/nanosat-mo-framework/sdk/examples/space/hello-world-full/target/*.nmfpack /nanosat-mo-framework/packages
COPY --from=nmfMainBuilder /nmf/nanosat-mo-framework/sdk/examples/space/publish-clock/target/*.nmfpack /nanosat-mo-framework/packages

# copy kepler elements configuration template for orbit simulation
COPY ./space-file-system/src/main/resources/orbital-dynamics-configuration/* ./

# configure orbital dynamics and start NMF Supervisor
CMD /nanosat-mo-framework/configure_orbital_dynamics.sh && su nmf-admin -c /nanosat-mo-framework/start_supervisor.sh
