# todolist
This project is a simple Todolist application that includes both a React frontend and a Spring Boot backend. The frontend is responsible for the user interface, while the backend handles the API logic and database operations.


Project Structure
The project consists of two main parts:

Backend: Spring Boot application for managing the todolist.
Frontend: React application for the user interface.
Both parts are located in the same repository, under separate directories:

/To-Do-List – Contains the Spring Boot backend.
/todolist – Contains the React frontend.
Prerequisites
Before running the project, make sure you have the following tools installed on your machine:

Node.js (for the React frontend):

Download and install it from Node.js official website.
Java 17 or higher (for the Spring Boot backend):

Download and install it from OpenJDK official website.
Maven (for Spring Boot dependencies):

Download and install it from Maven official website.
Git (for cloning repositories and version control):

Download and install it from Git official website.
1. Clone the Repository
Clone the repository to your local machine:

git clone[ https://github.com/your-username/todolist-app.git](https://github.com/ShyamShu/todolist)


## **2. Setup and Run Backend (Spring Boot)**
Navigate to the backend directory:

bash
Copy code
cd backend
Build the Spring Boot application:

Run the following Maven command to install the necessary dependencies and build the project:

bash
Copy code
mvn clean install
Run the Spring Boot application:

You can now run the backend application with this command:

bash
Copy code
mvn spring-boot:run
The backend should now be running on http://localhost:8080.


## **3. Setup and Run Frontend (React)**
Navigate to the frontend directory:

bash
Copy code
cd ../frontend
Install React project dependencies:

Run the following command to install all necessary npm dependencies:

bash
Copy code
npm install
Start the React development server:

Run the following command to start the React frontend:

bash
Copy code
npm start
The frontend should now be running on http://localhost:3000.
