FROM openjdk:8

WORKDIR /opt/Android/Sdk

ADD https://dl.google.com/android/repository/sdk-tools-linux-4333796.zip .
RUN unzip sdk-tools-linux-4333796.zip
RUN echo y | ./tools/bin/sdkmanager "build-tools;28.0.3"

WORKDIR /progressbutton

COPY . .
RUN ./gradlew build
