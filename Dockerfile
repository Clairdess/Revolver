FROM openjdk:11

COPY src/Revolver.java Revolver.java

RUN javac Revolver.java

CMD ["java", "Revolver"]