# Use an official OpenJDK runtime as the base image
FROM openjdk:19-jdk



# Copy the executable JAR file from the target directory to the container
ADD ./Let-s-Code-0.0.1-SNAPSHOT.jar Let-s-Code-0.0.1-SNAPSHOT.jar

# Expose the port that the application will listen on
EXPOSE 8080

# Define the command to run your Spring Boot application    
CMD ["java", "-jar", "Let-s-Code-0.0.1-SNAPSHOT.jar"]
