# Short description 
This web application offers a secure sharing files with precise user access control. Users can effortlessly upload files and designate specific users within the system who are authorized to access them.
Upon successful login and authentication, users can manage their uploaded files, The interface is straightforward, enabling users to upload files and select recipients with ease.
The application ensures security through encryption, utilizing AES encryption for uploaded files. Only authorized users with proper access permissions can download and view files in their original, decrypted form. Unauthorized attempts to download files result in encrypted downloads, maintaining data security. Furthermore, this application will keep growing as I learn. I might add more features and even upgrade the interface using React.js. It's an ongoing process to make it better and more user-friendly.



# Technologies Used
This web application is built using Java Spring, PostgreSQL, Thymeleaf, and Amazon S3.

Java Spring: Enables the server-side functionality of the web application, including the web layer (controllers), service layer, and repositories.
PostgreSQL: Manages metadata of users, files, their relationships, and access permissions through the relational database PostgreSQL.
Amazon S3: Used for storing files uploaded by users on the internet.
Thymeleaf: Responsible for the user interface.
Git & Github: Used for version control and application development.
# Application Structure
  This application follows a layered architecture to minimize dependencies, consisting of three layers: Web Layer, Service Layer, and Repository Layer.

## Web Layer
Contains controllers, including LogInController, MainPageController, and StorageController.

### LogInController
Handles user login and registration requests.
### MainPageController
Displays the homepage where users manage their files.
### StorageController
Processes requests related to uploading, downloading, and deleting files.
## Service Layer 
Responsible for business logic and data processing provided by the repository. It consists of interfaces with corresponding implementations:

### Permission Service
```
void addPermission(List<User> userList, String fileName) - Through this method, we add who should have permission to access the file we are attaching. In other words, we add the user to the control list.

boolean checkPermission(User user, String fileName) - Returns true or false depending on whether a given user has access to the given file.
```
### Storage Service
```
String uploadFile(MultipartFile file) - The file passed as an argument is converted into an array of bytes, which is encrypted using the AESUtil class to generate a key. Then, through the amazonS3 service, the encrypted bytes are attached to the Amazon file storage, and the key for further decryption of the attached file is saved in our local database.

File convertMultiPartFileToFile(MultipartFile file) - Used to convert a MultipartFile into a File class.

String deleteFile(String fileName) - This method invokes the amazonS3 service to delete the file from the corresponding Amazon bucket.

byte[] downloadFile(String fileName) - First, the object/file we want to download is searched for and retrieved into an object of the S3Object class. Then, the content of this object is placed into an array of bytes, and then it is decrypted with the key stored in the database, allowing the user to view the content of the file in its original form.

List<String> listAllFiles() - A simple method that returns the names of all files attached to the AmazonS3 bucket.
```
### User Service
```
User create(String name, String surname, String email, String password) - The parameters passed by the controller are used to create our User. Before saving the password in the database, it is encrypted using Bcrypt. The object is created, and then saved in the database using userRepository.save().

boolean authenticate(String email, String password) - Returns true or false depending on whether the user exists (not null) and if their credentials match those in the database.

boolean isValidPassword(String password) - During user registration, using RegEx, it checks if the password entered by the user is strong and valid enough.

boolean isValidEmail(String email) - Similar to the password, it checks if the email contains '@' and if it already exists in the database using regular expressions.

boolean checkPasswords(String password, String repeatedPassword) - During user registration, it confirms the entered password.

int mailVerification(String user_email) - Generates a random 4-digit PIN that is sent to the email address entered by the user for verification. This is done using Java Mail Sender.

void generateOtp(User account) - Generates a 4-digit code for two-factor authentication when the user logs in, which is sent via email.

User findUser(String email) - Method that returns the user based on the given email address.

List<User> listAll() - Returns all users existing in the database using userRepository.

List<User> findUsersById(List<Long> userIds) - Returns all users based on their IDs.

User findUserById(Long userId) - Returns only one user based on the ID.
```
## Repository Layer: 
Provides necessary queries to be executed on the database. It includes interfaces extended with JpaRepository interface from Spring Data JPA:

### Permission Repository
```
List<Permission> findByFileNameAndUsers(String fileName, User user) - Returns a list of permissions that a given user has for the specified file.

Permission findByFileName(String fileName) - Returns a Permission object based on the given file name.
```
### User Repository
```
User findUserByEmail(String email) - Retrieves the user based on the provided email address.

List<User> findByIdIn(List<Long> ids) - Returns all users based on the provided list of IDs.
```
## Model 
This folder contains the models of our application, which are definitions of tables in the database.

## Security
Here lies the AESUtil class, which provides methods for encryption, decryption, and generating a SecretKey required for encrypting the files uploaded.

## Config
All configurations of Beans used in the application are located within the Config folder.

### Templates
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
