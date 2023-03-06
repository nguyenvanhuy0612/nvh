#FROM alpine/git:2.36.3 as git_builder
#WORKDIR /auto
#RUN git config --global http.sslVerify false
#RUN git clone https://dtranvan:xxxx@135.8.44.31/scm/ixoutreach/outbound-test-automation.git
#FROM maven:3.8.6-eclipse-temurin-17-alpine
#WORKDIR /auto/outbound-test-automation/outbound
#COPY --from=git_builder /auto/outbound-test-automation/outbound .
#RUN mvn dependency:tree

FROM maven:3.8.6-eclipse-temurin-17-alpine
WORKDIR /automation
COPY . .
CMD ["mvn", "install"]