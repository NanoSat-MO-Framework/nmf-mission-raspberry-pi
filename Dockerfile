# This Dockerfile can be used to create a Docker base image for the 
# nmf supervisor. VERY WIP!
#
# author: Nikolai Wiegand
#
FROM ubuntu:latest

WORKDIR /nanosat-mo-framework

RUN apt update && apt install openssh-server sudo openjdk-8-jdk -y
RUN service ssh start

EXPOSE 22

CMD ["/usr/sbin/sshd","-D"]

