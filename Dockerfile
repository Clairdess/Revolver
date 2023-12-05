FROM openjdk:11

COPY src/Revolver.java Main.java

RUN javac Main.java

CMD ["java", "Main"]