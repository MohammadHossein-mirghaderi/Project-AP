import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

public class Client {

  public static void main(String[] args) throws IOException {
    Scanner scanner = new Scanner(System.in);
    while (true) {
      System.out.print("Enter your command : ");
      String command = scanner.nextLine();
      if (command.equals("home")) {
        goHome(scanner);
      } else if (command.equals("get ads")) {
        getAds(scanner);
      } else if (command.equals("add ads")) {
        addAds(scanner);
      } else if (command.equals("get profile")) {
        getProfile(scanner);
      } else {
        System.out.println("Invalid command");
      }
    }
  }

  // go home
  public static void goHome(Scanner scanner) {
    try {
      URL url = new URL("http://127.0.0.1:8000/api/");
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      int responseCode = connection.getResponseCode();
      if (responseCode == 200) {
        BufferedReader in = new BufferedReader(
          new InputStreamReader(connection.getInputStream())
        );
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
        }
        in.close();
        System.out.println(response.toString());
      } else {
        System.out.println("Failed to retrieve ads list");
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //get all ads
  public static void getAds(Scanner scanner) {
    try {
      URL url = new URL("http://127.0.0.1:8000/api/ads");
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      int responseCode = connection.getResponseCode();
      if (responseCode == 200) {
        BufferedReader in = new BufferedReader(
          new InputStreamReader(connection.getInputStream())
        );
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
        }
        in.close();
        System.out.println(response.toString());
      } else {
        System.out.println("Failed to retrieve ads list");
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //add an ads
  public static void addAds(Scanner scanner) {
    String apiUrl = "http://127.0.0.1:8000/api/ads";
    String name, desc, price, place, contacts, status;
    String input;
    //get ad information from user
    System.out.println("Enter ad name:");
    name = scanner.nextLine();
    System.out.println("Enter ad description:");
    desc = scanner.nextLine();
    System.out.println("Enter ad price:");
    price = scanner.nextLine();
    System.out.println("Enter ad place:");
    place = scanner.nextLine();
    System.out.println("Enter ad contacts:");
    contacts = scanner.nextLine();
    System.out.println("Enter ad status:");
    status = scanner.nextLine();
    //create json object
    String jsonInputString =
      "{\"name\":\"" +
      name +
      "\",\"desc\":\"" +
      desc +
      "\",\"price\":\"" +
      price +
      "\",\"place\":\"" +
      place +
      "\",\"contacts\":\"" +
      contacts +
      "\",\"status\":\"" +
      status +
      "\"}";
    try {
      //create url object and open connection
      URL url = new URL(apiUrl);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setDoOutput(true);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");
      //write json object to output stream
      OutputStream os = conn.getOutputStream();
      os.write(jsonInputString.getBytes());
      os.flush();
      //read response from server
      BufferedReader br = new BufferedReader(
        new InputStreamReader(conn.getInputStream())
      );
      while ((input = br.readLine()) != null) {
        System.out.println(input);
      }
      conn.disconnect();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //get a profile
  public static void getProfile(Scanner scanner) {
    try {
      URL url = new URL("http://127.0.0.1:8000/api/profile");
      // get username
      System.out.println("Enter username:");
      String username = scanner.nextLine();

      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setRequestProperty("username", username);

      int responseCode = connection.getResponseCode();

      // print repsponse
      System.out.println(responseCode);

      if (responseCode == 200) {
        BufferedReader in = new BufferedReader(
          new InputStreamReader(connection.getInputStream())
        );
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
        }
        in.close();
        System.out.println(response.toString());
      } else {
        System.out.println("Failed to retrieve profile");
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
