# Distributed Record Processing System

This project aims to develop a distributed system for processing structured records. The system consists of a server component that handles client requests by accessing a flat file containing records of the same structure. The server is capable of processing multiple client requests concurrently and supports operations such as reading, deleting, adding, and updating records.

## Features

- **Server**: The main server component responsible for handling client connections and processing their requests.
- **Client Handling**: Each client connection is managed by a separate thread to enable concurrent request processing.
- **Record Database**: Provides functionality for accessing and manipulating records stored in a flat file.
- **Supported Operations**: The server supports operations such as reading, deleting, adding, and updating records.

## Getting Started

Follow these instructions to set up and run the distributed record processing system:

### Prerequisites

- Java Development Kit (JDK) installed on your machine.
- Maven build tool for compiling and packaging the project.
- Docker (optional) for containerization of the application.

### Installation

1. Clone the repository to your local machine:

```bash
git clone https://github.com/Nice0Man/JavaServerOS.git
```

2. Navigate to the project directory:
```bash
cd record-processing-system 
```
3. Build the project using Maven:
```bash
mvn clean package
```

### Usage

1. Start the server by running the generated JAR file:

```bash
java -jar target/RecordProcessingServer.jar
```
2. The server will start listening for client connections on the specified port.

3. Clients can connect to the server and send requests to perform operations on records.

### Docker Support (Optional)

To run the application inside a Docker container, follow these steps:

1. Build the Docker image:
```bash
docker build -t record-processing-server .
```
2. Run the Docker container:
```bash
docker run -p 8080:8080 record-processing-server
```

## Configuration

- The server configuration, such as the listening port and file paths, can be modified in the `application.properties` file located in the `src/main/resources` directory.

## Testing

The project includes unit tests to verify the functionality of the server and database components. You can run the tests using Maven:

```bash
mvn test
```

## Contributing

Contributions to this project are welcome. Please fork the repository, make changes, and submit a pull request. For major changes, please open an issue first to discuss the proposed changes.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgements

- This project is inspired by the need for a scalable and efficient system for processing structured records in a distributed environment.

## Support

For any questions or assistance, please contact [e.krotov03@gmail.com].
