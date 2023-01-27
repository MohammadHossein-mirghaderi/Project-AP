import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;

public class Main {

  public static void main(String[] args) throws Exception {
    HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
    server.createContext("/api/", new MyHandler());
    server.createContext("/api/ads", new AdHandler());
    server.createContext("/api/register", new RegisterHandler());
    server.createContext("/api/login", new LoginHandler());
    server.createContext("/api/profile", new ProfileHandler());
    // server.createContext("/api/products", new ProductsHandler(connection));
    server.setExecutor(null);
    server.start();
  }
}
class MyHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange t) throws IOException {
    String response = "welcome to Divar Cli v1.0.0!";
    t.sendResponseHeaders(200, response.length());
    OutputStream os = t.getResponseBody();
    os.write(response.getBytes());
    os.close();
  }
}

class RegisterHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange t) throws IOException {
    if (t.getRequestMethod().equalsIgnoreCase("POST")) {
      InputStream is = t.getRequestBody();

      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      StringBuilder sb = new StringBuilder();
      String line = null;
      while ((line = reader.readLine()) != null) {
        sb.append(line);
      }
      String postData = sb.toString();

      int nameStartIndex = postData.indexOf("username") + 9;
      int nameEndIndex = postData.indexOf("-", nameStartIndex);
      String username = postData.substring(nameStartIndex, nameEndIndex).trim();

      int passwordStartIndex = postData.indexOf("password") + 9;
      int passwordEndIndex = postData.indexOf("-", passwordStartIndex);
      String password = postData
        .substring(passwordStartIndex, passwordEndIndex)
        .trim();

      int emailStartIndex = postData.indexOf("email") + 6;
      int emailEndIndex = postData.indexOf("-", emailStartIndex);
      String email = postData.substring(emailStartIndex, emailEndIndex).trim();

      boolean isValid = true;
      String message = "";
      // validate the username
      if (username.contains("@#$%^&*()_+|}{:?><,./;'[]-=~`")) {
        isValid = false;
        message = "Username cannot contain special characters such as @.";
      }
      // check if the username already exists in the database
      if (isValid && checkUsernameExist(username)) {
        isValid = false;
        message = "This username already exists";
      }
      // validate the password
      if (isValid && !validatePassword(password)) {
        isValid = false;
        message =
          "Password must be at least 8 characters including numbers and English lowercase letters and At least two letters a or one number in enamel 2";
      }
      // validate the email
      if (isValid && !validateEmail(email)) {
        isValid = false;
        message = "Invalid email format";
      }
      if (isValid) {
        if (isValid) {
          addUser(username, password,email);
          message = "user added successfully";
        }

        t.sendResponseHeaders(200, message.length());
        OutputStream os = t.getResponseBody();
        os.write(message.getBytes());
        os.close();
      } else {
        t.sendResponseHeaders(400, message.length());
        OutputStream os = t.getResponseBody();
        os.write(message.getBytes());
        os.close();
      }
    }
  }

  private boolean checkUsernameExist(String username) {
    try {
      File file = new File("users.json");
      FileReader fileReader = new FileReader(file);
      try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
        String ads;
        while ((ads = bufferedReader.readLine()) != null) {
          int nameStartIndex = ads.indexOf("username") + 11;
          int nameEndIndex = ads.indexOf(",", nameStartIndex) - 1;
          String n = ads.substring(nameStartIndex, nameEndIndex).trim();
          //print the name
          System.out.println(n);
          if (n.equals(username)) {
            return true;
          }
        }
      }
      return false;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  private boolean validatePassword(String password) {
    // password must be at least 8 characters, including numbers and English lowercase letters
    // At least two letters a or one number in enamel 2
    if (password.length() < 8) {
      return false;
    }
    int countNumber = 0;
    int countLowerCase = 0;
    int countA = 0;
    for (int i = 0; i < password.length(); i++) {
      char c = password.charAt(i);
      if (c >= '0' && c <= '9') {
        countNumber++;
      }
      if (c >= 'a' && c <= 'z') {
        countLowerCase++;
      }
      if (c == 'a' || c == 'A') {
        countA++;
      }
    }
    if (countNumber < 2 && countA < 2) {
      return false;
    }
    if (countLowerCase == 0) {
      return false;
    }
    return true;
  }

  private boolean validateEmail(String email) {
    String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    return email.matches(regex);
  }

  private static void addUser(
    String username,
    String password,
    String email
  ) throws IOException {
    try {
      File file = new File("users.json");
      FileWriter fileWriter = new FileWriter(file, true);
      BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
      String token = UUID.randomUUID().toString();

      // Convert the Ad object to a JSON string
      String adJson =
        "{" +
        "\"username\":" +
        "\"" +
        username +
        "\"" +
        "," +
        "\"password\":" +
        "\"" +
        password +
        "\"" +
        "," +
        "\"token\":" +
        "\"" +
        token +
        "\"" +
        "," +
        "\"email\":" +
        "\"" +
        email +
        "\"" +
        "}";

      bufferedWriter.write(adJson);
      bufferedWriter.newLine();
      bufferedWriter.close();
      System.out.println("Ad added successfully to the json file.");
    } catch (IOException e) {
      System.out.println(
        "An error occurred while trying to add the ad to the json file."
      );
      e.printStackTrace();
    }
  }
}


class LoginHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange t) throws IOException {
    if (t.getRequestMethod().equalsIgnoreCase("POST")) {
      InputStream is = t.getRequestBody();

      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      StringBuilder sb = new StringBuilder();
      String line = null;
      while ((line = reader.readLine()) != null) {
        sb.append(line);
      }
      String postData = sb.toString();

      int nameStartIndex = postData.indexOf("username") + 9;
      int nameEndIndex = postData.indexOf("-", nameStartIndex);
      String username = postData.substring(nameStartIndex, nameEndIndex).trim();

      int passwordStartIndex = postData.indexOf("password") + 9;
      int passwordEndIndex = postData.indexOf("-", passwordStartIndex);
      String password = postData
        .substring(passwordStartIndex, passwordEndIndex)
        .trim(); 

      

      boolean isValid = true;
      String message = "";

      if (!validateUser(username,password)) {
        isValid = false;
        message = "This username or password incorrect!";
      }  
      if (isValid) {
        if (isValid) {
          message = "Login successfully!";
        }

        t.sendResponseHeaders(200, message.length());
        OutputStream os = t.getResponseBody();
        os.write(message.getBytes());
        os.close();
      } else {
        t.sendResponseHeaders(400, message.length());
        OutputStream os = t.getResponseBody();
        os.write(message.getBytes());
        os.close();
      }
    }
  }

  private boolean validateUser(String username,String password) {
    try {
      File file = new File("users.json");
      FileReader fileReader = new FileReader(file);
      try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
        String ads;
        while ((ads = bufferedReader.readLine()) != null) {
          int usernameStartIndex = ads.indexOf("username") + 11;
          int usernameEndIndex = ads.indexOf(",", usernameStartIndex) - 1;
          String n = ads.substring(usernameStartIndex, usernameEndIndex).trim();


          int passwordStartIndex = ads.indexOf("password") + 11;
          int passwordEndIndex = ads.indexOf(",", passwordStartIndex) - 1;
          String p = ads.substring(passwordStartIndex, passwordEndIndex).trim();  

          if (n.equals(username) && p.equals(password) ) {
            return true;
          }
        }
      }
      return false;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
   
}

class ProfileHandler implements HttpHandler {
  @Override
  public void handle(HttpExchange t) throws IOException {
    if (t.getRequestMethod().equalsIgnoreCase("GET")) {
      String query = t.getRequestURI().getQuery();
      String username = query.substring(query.indexOf("=") + 1);

      String userProfile = getUserProfile(username);
      t.sendResponseHeaders(200, userProfile.length());
      OutputStream os = t.getResponseBody();
      os.write(userProfile.getBytes());
      os.close();
    } else if (t.getRequestMethod().equalsIgnoreCase("POST")) {
      InputStream is = t.getRequestBody();
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      StringBuilder sb = new StringBuilder();
      String line = null;
      while ((line = reader.readLine()) != null) {
        sb.append(line);
      }
      String postData = sb.toString();
      int nameStartIndex = postData.indexOf("username") + 9;
      int nameEndIndex = postData.indexOf("-", nameStartIndex);
      String username = postData.substring(nameStartIndex, nameEndIndex).trim();
      int emailStartIndex = postData.indexOf("email") + 6;
      int emailEndIndex = postData.indexOf("-", emailStartIndex);
      String email = postData.substring(emailStartIndex, emailEndIndex).trim();

      updateUserProfile(username, email);
      String userProfile = getUserProfile(username);
      t.sendResponseHeaders(200, userProfile.length());
      OutputStream os = t.getResponseBody();
      os.write(userProfile.getBytes());
      os.close();
    }
  }
  private String getUserProfile(String username) {
    try { 
      File file = new File("users.json");
      FileReader fileReader = new FileReader(file);
      try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
        String ads;
        while ((ads = bufferedReader.readLine()) != null) {
          int usernameStartIndex = ads.indexOf("username") + 11;
          int usernameEndIndex = ads.indexOf(",", usernameStartIndex) - 1;
          String n = ads.substring(usernameStartIndex, usernameEndIndex).trim();
          //print username
          
          if (n.equals(username)) {
            int emailStartIndex = ads.indexOf("email") + 8;
            int emailEndIndex = ads.indexOf("}", emailStartIndex) - 1;
            String email = ads.substring(emailStartIndex, emailEndIndex).trim();
            System.out.println(email);
            return "{\"username\":\"" + username + "\",\"email\":\"" + email + "\"}";
          }else{
            return "{\"username\":\"" + username + "\",\"email\":\"" + "\"}";
          }
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }
  // update user profile
  private void updateUserProfile(String username, String email) {
    try { 
      File file = new File("users.json");
      FileReader fileReader = new FileReader(file);
      try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
        String ads;
        while ((ads = bufferedReader.readLine()) != null) {
          int usernameStartIndex = ads.indexOf("username") + 11;
          int usernameEndIndex = ads.indexOf(",", usernameStartIndex) - 1;
          String n = ads.substring(usernameStartIndex, usernameEndIndex).trim();
          if (n.equals(username)) {
            int emailStartIndex = ads.indexOf("email") + 8;
            int emailEndIndex = ads.indexOf("}", emailStartIndex) - 1;
            String oldEmail = ads.substring(emailStartIndex, emailEndIndex).trim();
            String newAds = ads.replace(oldEmail, email);
            try (FileWriter fileWriter = new FileWriter(file)) {
              fileWriter.write(newAds);
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
class AdHandler implements HttpHandler {

  private final String filePath = "ads.json";

  @Override
  public void handle(HttpExchange t) throws IOException {
    boolean isValid = true;
    String message = "";
    if (t.getRequestMethod().equalsIgnoreCase("POST")) {
      try {
        InputStream is = t.getRequestBody();

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
          sb.append(line);
        }
        String postData = sb.toString();

        //print

        int nameStartIndex = postData.indexOf("name") + 7;
        int nameEndIndex = postData.indexOf(",", nameStartIndex) - 1;
        String name = postData.substring(nameStartIndex, nameEndIndex).trim();

        int descStartIndex = postData.indexOf("description") + 12;
        int descEndIndex = postData.indexOf(",", descStartIndex) - 1;
        String description = postData
          .substring(descStartIndex, descEndIndex)
          .trim();

        int priceStartIndex = postData.indexOf("price") + 6;
        int priceEndIndex = postData.indexOf(",", priceStartIndex) - 1;

        String price = postData
          .substring(priceStartIndex, priceEndIndex)
          .trim();

        int placeStartIndex = postData.indexOf("place") + 6;
        int placeEndIndex = postData.indexOf(",", placeStartIndex) - 1;
        String place = postData
          .substring(placeStartIndex, placeEndIndex)
          .trim();

        int contactsStartIndex = postData.indexOf("contacts") + 9;
        int contactsEndIndex = postData.indexOf(",", contactsStartIndex) - 1;

        String contacts = postData
          .substring(contactsStartIndex, contactsEndIndex)
          .trim();

        System.out.println(name);
        System.out.println(description);
        System.out.println(price);
        System.out.println(place);
        System.out.println(contacts);


        // validate the inputs
        if (name == null || name.isEmpty()) {
          isValid = false;
          message = "Name is required";
        } else if (description == null || description.isEmpty()) {
          isValid = false;
          message = "Description is required";
        } else if (price == null || price.isEmpty()) {
          isValid = false;
          message = "Price is required";
        } else if (place == null || place.isEmpty()) {
          isValid = false;
          message = "Place is required";
        } else if (contacts == null || contacts.isEmpty()) {
          isValid = false;
          message = "Contacts is required";
        }

        if (!checkName(name)) {
          isValid = false;
          message = "this name currently exists. try another one";
        }

        if (isValid) {
          addAd(name, description, price, place, contacts);
          message = "Ad saved successfully";
        }

        // send response
        t.sendResponseHeaders(200, message.length());
        OutputStream os = t.getResponseBody();
        os.write(message.getBytes());
        os.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    if (t.getRequestMethod().equalsIgnoreCase("GET")) {
      String response = getAdList();
      t.sendResponseHeaders(200, response.length());
      OutputStream os = t.getResponseBody();
      os.write(response.getBytes());
      os.close();
    }
  }

  private boolean checkName(String name) {
    // check if the name already exists
    try {
      // read file and check if the name exists
      File file = new File("ads.json");
      FileReader fileReader = new FileReader(file);
      try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
        StringBuffer stringBuffer = new StringBuffer();
        String ads;
        while ((ads = bufferedReader.readLine()) != null) {
          int nameStartIndex = ads.indexOf("name") + 7;
          int nameEndIndex = ads.indexOf(",", nameStartIndex) - 1;
          String n = ads.substring(nameStartIndex, nameEndIndex).trim();
          //print the name
          System.out.println(n);
          if (n.equals(name)) {
            return false;
          }
        }
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  private String getAdList() {
    try {
      File file = new File("ads.json");
      FileReader fileReader = new FileReader(file);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      StringBuffer stringBuffer = new StringBuffer();
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        stringBuffer.append(line);
      }
      return stringBuffer.toString();
    } catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }

  private static void addAd(
    String name,
    String description,
    String price,
    String place,
    String contacts
  ) throws IOException {
    try {
      File file = new File("ads.json");
      FileWriter fileWriter = new FileWriter(file, true);
      BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

      // Convert the Ad object to a JSON string
      String adJson =
        "{" +
        "\"name\":" +
        "\"" +
        name +
        "\"" +
        "," +
        "\"desc\":" +
        "\"" +
        description +
        "\"" +
        "," +
        "\"price\":" +
        "\"" +
        price +
        "\"" +
        "," +
        "\"place\":" +
        "\"" +
        place +
        "\"" +
        "," +
        "\"contacts\":" +
        "\"" +
        contacts +
        "\"" +
        "}";

      bufferedWriter.write(adJson);
      bufferedWriter.newLine();
      bufferedWriter.close();
      System.out.println("Ad added successfully to the json file.");
    } catch (IOException e) {
      System.out.println(
        "An error occurred while trying to add the ad to the json file."
      );
      e.printStackTrace();
    }
  }
}
