package mapi;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.google.gson.Gson;

public class MaineMaven {

	Data data;

	public static void mainMenue() {

		System.out.println("***********************************************************");
		System.out.println(" 1- connect To Data Base ");
		System.out.println(" 2- print json Api( ");
		System.out.println(" 3- create Author Table ");
		System.out.println(" 4- insert Into Table Author");
		System.out.println(" 0- Exit ");
		System.out.println("***********************************************************");

	}

	public static void jsonApi() throws Throwable, InterruptedException {

		HttpClient client = HttpClient.newHttpClient();

		System.out.println(" printing (API) information  ");

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://universities.hipolabs.com")).build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		System.out.printf(response.body());

	}

	static void connectToDataBase() throws Throwable, IllegalAccessException, ClassNotFoundException {

		Connection connection;

		try {

			// Create a connection to the database

			String serverName = "localhost";

			String url = "jdbc:sqlserver://localhost:1433;databaseName=MavenDataBase;encrypt=true;trustServerCertificate=true";
			String user = "sa";
			String pass = "root";
			Driver driver = (Driver) Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
			DriverManager.registerDriver(driver);

			connection = DriverManager.getConnection(url, user, pass);

			System.out.println("Successfully Connected to the database!" + " MavenDataBase ");

		} catch (SQLException e) {

			System.out.println("Could not connect to the database " + e.getMessage());
		}

	}

	public static void createAuthorTable() throws Throwable, IllegalAccessException, ClassNotFoundException {

		String url = "jdbc:sqlserver://localhost:1433;databaseName=MavenDataBase;encrypt=true;trustServerCertificate=true";
		String user = "sa";
		String pass = "root";

		try (Connection conn = DriverManager.getConnection(url, user, pass); Statement stmt = conn.createStatement();) {
			String sql = "CREATE TABLE Author " + "( Id int PRIMARY KEY IDENTITY(1,1) ,"
					+ " website VARCHAR(500) not null," + " name VARCHAR(500)," + " github VARCHAR(500) ,"
					+ " example VARCHAR(500) ,)";

			stmt.executeUpdate(sql);
			System.out.println("Created table in given database...");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void insertIntoTableAuthor() throws Throwable, InterruptedException {

		String url = "jdbc:sqlserver://localhost:1433;databaseName=MavenDataBase;encrypt=true;trustServerCertificate=true";
		String user = "sa";
		String pass = "root";

		Scanner scanner = new Scanner(System.in);

		HttpRequest request2 = HttpRequest.newBuilder()

				.uri(URI.create("http://universities.hipolabs.com")).build();

		HttpResponse<String> response2;

		HttpClient client = HttpClient.newHttpClient();

		System.out.println(" printing (API) information  ");

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://universities.hipolabs.com")).build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		System.out.println(response.body());
		Gson gson = new Gson();
		Data data = new Gson().fromJson(response.body(), Data.class);
		System.out.println(data);

		String sql = "Insert into Author values( '" + data.getAuthor().getWebsite() + "','" + data.getAuthor().getName()
				+ "','" + data.getGithub() + "','" + data.getExample() + "')";

		Connection con;

		try {

			Driver driver = (Driver) Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
			// Registering drivers
			DriverManager.registerDriver(driver);

			// Reference to connection interface
			con = DriverManager.getConnection(url, user, pass);

			// Creating a statement
			Statement st = con.createStatement();

			// Executing query
			int m = st.executeUpdate(sql);
			if (m >= 1)
				System.out.println("inserted successfully : " + sql);
			else
				System.out.println("insertion failed");

			// Closing the connections
			con.close();
		}

//		 Catch block to handle exceptions
		catch (Exception ex) {
			// Display message when exceptions occurs
			System.err.println(ex);
		}
	}

	public static void toExit() {

		System.out.println("***********");
		System.out.println("Good Bay");
		System.out.println("***********");

	}

	public static void byDefault() {

		System.out.println("plase Enter correct choise");

	}

	public static void main(String[] args) throws InterruptedException, Throwable {

		Scanner sc = new Scanner(System.in);

		boolean exit = true;

		do {

			mainMenue();
			int option = sc.nextInt();

			switch (option) {

			case 1:
				connectToDataBase();
				break;

			case 2:
				jsonApi();
				break;

			case 3:
				createAuthorTable();

				break;

			case 4:
				insertIntoTableAuthor();

				break;

			case 0:
				toExit();
				exit = false;

				break;

			default:

				byDefault();
			}

		} while (exit);
	}
}
