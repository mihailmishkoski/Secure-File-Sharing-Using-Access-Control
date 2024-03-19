# Technologies Used
This web application is built using Java Spring, PostgreSQL, Thymeleaf, and Amazon S3.

Java Spring: Enables the server-side functionality of the web application, including the web layer (controllers), service layer, and repositories.
PostgreSQL: Manages metadata of users, files, their relationships, and access permissions through the relational database PostgreSQL.
Amazon S3: Used for storing files uploaded by users on the internet.
Thymeleaf: Responsible for the user interface.
Git & Github: Used for version control and application development.
# Application Structure
  This application follows a layered architecture to minimize dependencies, consisting of three layers: Web Layer, Service Layer, and Repository Layer.

## Web Layer: 
Contains controllers, including LogInController, MainPageController, and StorageController.

### LogInController: 
Handles user login and registration requests.
### MainPageController: 
Displays the homepage where users manage their files.
### StorageController:
Processes requests related to uploading, downloading, and deleting files.
## Service Layer: 
Responsible for business logic and data processing provided by the repository. It consists of interfaces with corresponding implementations:

### Permission Service
### Storage Service
### User Service
### Repository Layer: Provides necessary queries to be executed on the database. It includes interfaces extended with JpaRepository interface from Spring Data JPA:

## Permission Repository
### User Repository
### Additional
## Model: 
This folder contains the models of our application, which are definitions of tables in the database.

### Security:
Here lies the AESUtil class, which provides methods for encryption, decryption, and generating a SecretKey required for encrypting the files uploaded.

### Config:
All configurations of Beans used in the application are located within the Config folder.

### Templates:
Within this folder are all static HTML pages used by our application to display data.

## How It Works?
Initially, the user encounters the registration page, after which, if registered successfully, logs into the application via email authentication. Then, the user has the option to upload a file and select which registered users in the system will have access to the uploaded file.

In the background, when a user logs in, relevant data from the database is converted into the User model. After authentication, the user can upload files, download files (if permission is granted), and delete files only if permission is granted.

When uploading a file, the controller responsible for that URL executes the POST request and calls the data storage service from AmazonS3. Before the file is uploaded, encryption is performed, and the key is stored in the local database using the Permission Repository.

When deleting files, the controller responsible for this POST request calls the method for deleting the file from the AmazonS3 storage service, but also deletes the accesses and its key from the database.

For downloading a file, the controller responsible for this GET request calls the getObject method from the AmazonS3 service. Then, the content of this object is cast into a byte array, which is decrypted with the key obtained through the Permission Repository. After this series of operations, the downloaded file is ready for use.

With the help of this architecture, where the controller communicates with the services that interact with the repository, which is directly connected to the database, a low dependency of this web application for securely sharing files over the internet is achieved, which further facilitates the solution and opens up many possibilities for implementing new functionalities.

## File Management
Amazon Simple Storage Service (S3) is used for file management. It is an object storage service that offers scalability, excellent availability of attached data, high security, and solid performance. This service is designed for storing and retrieving any amount of data from anywhere. For the purposes of this project, this service is configured to be used only by one user from one device.
