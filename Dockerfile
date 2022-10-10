# Build and run the NMF Raspberry Pi mission.
#
# author: Nikolai Wiegand
#

FROM maven:3.8.6-openjdk-8 AS nmfMainBuilder

WORKDIR /nmf/

# clone NMF main repo and switch to phi-sat-2 branch
RUN git clone https://github.com/esa/nanosat-mo-framework.git
RUN cd /nmf/nanosat-mo-framework && git checkout phi-sat-2

# build phi-sat-2 branch of NMF main repo 
RUN cd /nmf/nanosat-mo-framework && mvn install 
RUN cd /nmf/nanosat-mo-framework/sdk && mvn install



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

# copy files from previous build and install the NMF Supervisor
COPY --from=nmfRpiMissionBuilder /nmf/space-file-system/target/space-file-system/nanosat-mo-framework/ /nanosat-mo-framework/
RUN chmod +x fresh_install.sh && ./fresh_install.sh

# start NMF Supervisor
# EXPOSE 1024
CMD /nanosat-mo-framework/start_supervisor.sh