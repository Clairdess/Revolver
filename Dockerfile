FROM openjdk:11

COPY src/main/Revolver.java Revolver.java

RUN javac Revolver.java

CMD ["java", "Revolver.java"]