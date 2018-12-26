FROM ubuntu:18.04

ENV ANDROID_HOME /opt/Android/Sdk

WORKDIR /opt/Android/Sdk

RUN apt update && apt install -y qemu-kvm libvirt-bin ubuntu-vm-builder bridge-utils openjdk-8-jdk

ADD https://dl.google.com/android/repository/sdk-tools-linux-4333796.zip .
RUN unzip sdk-tools-linux-4333796.zip
RUN echo y | ./tools/bin/sdkmanager "build-tools;28.0.3" "platform-tools" "platforms;android-28" "emulator"
RUN echo y | ./tools/bin/sdkmanager "system-images;android-28;default;x86"
RUN echo no | ./tools/bin/avdmanager create avd -n emuTest -k "system-images;android-28;default;x86"

WORKDIR /progressbutton

COPY . .
RUN ./gradlew build
CMD /opt/Android/Sdk/emulator/emulator -avd emuTest -noaudio -no-boot-anim -gpu off && ./android-wait-for-emulator && ./gradlew connectedAndroidTest