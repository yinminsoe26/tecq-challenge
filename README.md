# [Prerequisties]
Install the following software in your pc.

1.JDK 17

2.VisualStudio Code and node js

3.Intellij

4.Docker Deskstop

5.Git

6.Postman

7.Apache Maven

8.Go to Microsoft Store and install Ubuntu 20.4


# [Run Docker Image ]

## Run Steps For Docker Image
A. To pull docker image and run from docker hub, please run the below command in Ubuntu 20.4.

docker run --name employee-app -p 8081:8081 yinminsoe26/tecq-employee-app:1.0.0

B. run and test the endpoints according to user stories

## Run Steps For Manual Testing for endpoints by using Postman

A. import tecq-challenge\AcceptanceCriteria\ManualTestTools into postman

B. run and test the endpoints according to user stories


## Run Steps To run in Browser with below url

A. For UI, please run the below command.

http://localhost:8081/

B. For Api Status, please run the below command.

http://localhost:8081/status


# [Steps By Steps to build docker image]
1.clone from github

git clone https://github.com/yinminsoe26/tecq-challenge.git

## Run Steps For Frontend [Angular(employee-app)]

2.1. open the command prompt or open employee-app in visual studio code and terminal

2.2. go to the clone directory and change directory as bellows.

cd employee-app

2.3. To build for dist/employee-app, run the bellow command.

ng build --configuration production

2.4. To run only in visual studio code, run the bellow command.

ng serve --open

## Run Steps For Backend Api(employee-service)
3.1. open the command prompt

3.2. go to the clone directory and change directory as bellows.

cd employee-service

3.3. maven build

mvn package

Note: 
 Please run (mvn clean) before the steps ( 2.3. ) and resource plugin of pom.xml will copy (2.3.) result to include in jar file.

3.4. goto target directory and run jar as bellows.

java -jar tecq-employee-app:1.0.0.jar


## Run Steps For Docker Image(employee-service)

4.1. start Docker desktop

4.2. open Ubuntu

4.3. To build docker image, run the two below commands.

cd /mnt/d/tecq-challenge/employee-service

docker build -t yinminsoe26/tecq-employee-app:1.0.0 .

4.4. To run the docker image, run the bellow commands.

docker run --name employee-app -p 8081:8081 tecq-employee-app:1.0.0

4.5 tag and push docker image to docker hub

docker tag yinminsoe26/tecq-employee-app:1.0.0 yinminsoe26/tecq-employee-app:1.0.0

docker push yinminsoe26/tecq-employee-app:1.0.0


## Run Steps For Manual Testing for endpoints by using Postman

5.1. import tecq-challenge\AcceptanceCriteria\ManualTestTools into postman

5.2. run and test the endpoints according to user stories



## Oustanding

6.1. Navigation Drawer

6.2. To handle server errors & show status message/error in message area 

6.3. Still in progress for upload csv UI & logic to call server

6.4. User Story 4 and 5
