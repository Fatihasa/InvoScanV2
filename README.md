# InvoScan

## Project Description

**InvoScan** is a modern web application that helps companies validate invoices for customs clearance.  
Users can upload their invoice Excel files, and InvoScan checks all important rules automatically—such as addresses, HS codes, country and currency codes, and gross weight.  
The system uses artificial intelligence for address and HS code validation, making the process faster, more accurate, and reducing manual work and human errors.

## Features

- Upload and validate invoice Excel files
- AI-powered address and HS code checking (DeepSeek and InvoAI)
- Checks for country, currency, gross weight, procedure number, article sequence, and more
- Detailed error reports and user-friendly interface
- Copy all errors with one click
- Batch invoice upload and automated processing (planned)
- Admin analytics and reporting (planned)
- Integration with external customs/ERP systems (planned)

## Screenshots

**Login Page:**  
![image](https://github.com/user-attachments/assets/3c91589e-a692-4aea-b019-03b525d74f4d)

**File Upload:**  
![image](https://github.com/user-attachments/assets/aea4c517-f205-414b-8daf-3a368f00ee3d)

**Validation Results:**  
![image](https://github.com/user-attachments/assets/5e8570e4-7017-4165-b049-7ef84c90a7d5)

**HS Code Suggestion:**  
![image](https://github.com/user-attachments/assets/1e34400a-86e8-4bfe-b30f-1bf4aa5bef40)

**Error Messages:**  
![image](https://github.com/user-attachments/assets/ae10d4fd-93bb-474e-9bff-6f0a1c954e2d)

## Technologies Used

- **Java 17** – Main programming language
- **Spring Boot** – Backend framework
- **Thymeleaf** – Template engine for web pages
- **Apache POI** – Excel file reading/writing
- **H2 Database** – Local development database
- **Firebase Authentication** – User login and authentication
- **DeepSeek API** – AI-powered address validation
- **HTML, CSS, JavaScript** – Frontend development
- **Maven** – Build and dependency management
- **Git** – Version control

## Getting Started / Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Fatihasa/InvoScanV2.git
   cd InvoScanV2
2. **Add your Firebase service account key**
    ```bash
  Place your invoscan_firebase.json in:
  src/main/resources/static/

⚠️ Never commit your secret key to GitHub!

See invoscan_firebase.example.json for a template.

(Optional) Set up DeepSeek or other API keys

Add your API keys as required for address or HS code validation.

Build and run the project

bash
Copy
mvn clean install
mvn spring-boot:run
Open your browser:
Visit http://localhost:8080

## Test Login Credentials

To test the application, you can use the following example login:

- **Email:** admin@invoscan.com  
- **Password:** admin123

## Test Invoice Documents for uploading files

[B-OVLU1912801 104.xlsx](https://github.com/user-attachments/files/20745999/B-OVLU1912801.104.xlsx)

[A-880-39146446-27.xlsx](https://github.com/user-attachments/files/20746001/A-880-39146446-27.xlsx)

[FFAU2720208-A.xlsx](https://github.com/user-attachments/files/20746002/FFAU2720208-A.xlsx)



