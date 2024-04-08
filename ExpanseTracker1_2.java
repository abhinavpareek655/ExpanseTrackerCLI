import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.nio.file.Files;
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
public class ExpanseTracker1_2 {
    public static String otp(){
        int[][] randomRanges = {{48,57},{65,90},{97,122}};
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i=0;i<5;i++){
            int dice = random.nextInt(0,randomRanges.length);
            otp.append((char)random.nextInt(randomRanges[dice][0],randomRanges[dice][1]));
        }
        return otp.toString();
    }
    public static void sendEmail(String message, String subject, String to, String from){
        String host = "smtp.gmail.com";
        Properties properties = System.getProperties();
        System.out.println("PROPERTIES: "+properties);

        properties.put("mail.smtp.host",host);
        properties.put("mail.smtp.port","465");
        properties.put("mail.smtp.ssl.enable","true");
        properties.put("mail.smtp.auth","true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("2022btcse002@curaj.ac.in","#$1000000");
            }
        });

        session.setDebug(true);

        MimeMessage m = new MimeMessage(session);
        try {
            m.setFrom(from);
            m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            m.setSubject(subject);
            m.setText(message);
            Transport.send(m);
            System.out.println("Sent!!");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public static File signup() {
        System.out.println("Email id");
        Scanner sc = new Scanner(System.in);
        String email = sc.next();
        String otp = otp();
        sendEmail(otp,"Email authentication",email,"2022btcse002@curaj.ac.in");
        System.out.println("Check your mail box for otp (check spam also)");
        String userInput = sc.next();
        if(userInput.equals(otp)){
            System.out.println("email verification successfully!!");
        }
        else{
            while(!userInput.equals(otp)){
                System.out.println("opt does not match! Try again / change mail(Type \"c\")");
                userInput = sc.next();
                if(userInput.equals("c")){
                    signup();
                    break;
                }
            }
        }
        String username = email.substring(0,email.indexOf("@"));
        File newUserFile = new File(username+".txt");
        try {
            System.out.println("Set new password: ");
            Scanner input = new Scanner(System.in);
            String password = input.next();
            setPassword(username,password);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return newUserFile;
    }
    public static void add(File file) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Expense Amount: ");
        int expanse = scanner.nextInt();
        System.out.println("Category: (food, travel, education, investment, others)");
        Scanner sc = new Scanner(System.in);
        String note = sc.nextLine();
        Date date = new Date();
        try {
            FileWriter myWriter = new FileWriter(file, true);
            myWriter.append(String.valueOf(date)).append(" ").append(String.valueOf(expanse)).append(",").append(note).append("\n");
            myWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String username = file.toPath().toString().substring(0,file.toPath().toString().indexOf("."));
        if(getLastMonth(file)>=getBudget(username)){
            System.out.println("Alert Budget exceeded");
            System.out.println("Current Budget: "+getBudget(username));
            System.out.println("Current Monthly Expanses: "+getLastMonth(file));
        }
    }
    public static void show(File file) {
        int totalExpanse = 0;
        try {
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                int index = data.indexOf(",", 30);
                totalExpanse += Integer.parseInt(data.substring(29, index));
                System.out.println(data);
            }
            myReader.close();
            System.out.println("Total Expanse: " + totalExpanse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void show(File file, Date date) {
        int expanseFromDate = 0;
        try {
            Scanner myReader = new Scanner(file);
            String nextLine;

            String dateInString;
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            File tempAccessFile = new File(file.getAbsolutePath());
            Scanner tempReadScanner = new Scanner(tempAccessFile);
            String refDate = tempReadScanner.nextLine().substring(0, 29);
            tempReadScanner.close();

            Date dateInFormat = formatter.parse(refDate);

            while (dateInFormat.before(date)) {
                nextLine = myReader.nextLine();
                dateInString = nextLine.substring(0, 29);
                dateInFormat = formatter.parse(dateInString);
            }
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
                int index = data.indexOf(",", 30);
                expanseFromDate += Integer.parseInt(data.substring(29, index));
            }
            myReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Total Expanses till now from given date: " + expanseFromDate);
    }
    public static void showLastMonth(File file) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.add(Calendar.DATE, -1);
        Date lastMonthDate = calendar.getTime();
        show(file, lastMonthDate);
    }
    public static int getLastMonth(File file){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.add(Calendar.DATE, -1);
        Date lastMonthDate = calendar.getTime();
        return getExpanses(file, lastMonthDate);
    }
    public static int getExpanses(File file , Date date){
        int expanseFromDate = 0;
        try {
            Scanner myReader = new Scanner(file);
            String nextLine;

            String dateInString;
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            File tempAccessFile = new File(file.getAbsolutePath());
            Scanner tempReadScanner = new Scanner(tempAccessFile);
            String refDate = tempReadScanner.nextLine().substring(0, 29);
            tempReadScanner.close();

            Date dateInFormat = formatter.parse(refDate);

            while (dateInFormat.before(date)) {
                nextLine = myReader.nextLine();
                dateInString = nextLine.substring(0, 29);
                dateInFormat = formatter.parse(dateInString);
            }
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                int index = data.indexOf(",", 30);
                expanseFromDate += Integer.parseInt(data.substring(29, index));
            }
            myReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return expanseFromDate;
    }
    public static void showLastyear(File file) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        calendar.add(Calendar.DATE, -1);
        Date lastYearDate = calendar.getTime();
        show(file, lastYearDate);
    }
    public static void showCategory(File file, String category) {
        int categoryExpanses = 0;
        Scanner myReader = null;
        try {
            myReader = new Scanner(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (true) {
            assert myReader != null;
            if (!myReader.hasNextLine()) break;
            String data = myReader.nextLine();
            int index = data.indexOf(",");
            if (data.substring(index + 1).equals(category)) {
                System.out.println(data);
                int expanseIndex = data.indexOf(",", 30);
                categoryExpanses += Integer.parseInt(data.substring(29, expanseIndex));
            }
        }
        System.out.println("Total Category wise Expanses: " + categoryExpanses);
        myReader.close();
    }
    public static void setPassword(String username, String password){
        int budget = Integer.MAX_VALUE;
        File admin = new File("admin.txt");
        try{
            FileWriter writer = new FileWriter(admin,true);
            writer.append(username).append(" ").append(password).append(" ").append(String.valueOf(budget)).append(" \n");
            writer.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public static String getPassword(String username){
        File admin = new File("admin.txt");
        String data="";
        try {
            Scanner reader = new Scanner(admin);
            data = reader.nextLine();
            String names = data.substring(0,data.indexOf(" "));
            while(reader.hasNextLine() && !names.equals(username)){
                data = reader.nextLine();
                names = data.substring(0,data.indexOf(" "));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return data.substring(data.indexOf(" ")+1,data.indexOf(" ",data.indexOf(" ")+1));
    }
    public synchronized static void updatePassword(String username, String newPassword){
        File adminFile = new File("admin.txt");
        File tempFile = new File("temp.txt");
        try {
            FileReader reader = new FileReader(adminFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            FileWriter writer = new FileWriter(tempFile);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String names = line.substring(0,line.indexOf(" "));
                if (names.equals(username)) {
                    int budget = getBudget(names);
                    writer.write(username +" "+ newPassword +" "+budget+" \n");
                    System.out.println("done!");
                } else {
                    writer.write(line + "\n");
                }
            }
            bufferedReader.close();
            writer.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {
            Files.delete(adminFile.toPath());
            Files.move(tempFile.toPath(), adminFile.toPath());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public synchronized static void setBudget(String username,String budget){
        File adminFile = new File("admin.txt");
        File tempFile = new File("temp.txt");
        try {
            FileReader reader = new FileReader(adminFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            FileWriter writer = new FileWriter(tempFile);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String names = line.substring(0,line.indexOf(" "));
                String data = line.substring(0,line.indexOf(" ",line.indexOf(" ")+1));
                if (names.equals(username)) {
                    writer.write(data + " " +budget +" \n");
                } else {
                    writer.write(line + "\n");
                }
            }
            bufferedReader.close();
            writer.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {
            Files.delete(adminFile.toPath());
            Files.move(tempFile.toPath(), adminFile.toPath());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public static int getBudget(String username){
        int budget=0;
        File adminFile = new File("admin.txt");
        try {
            FileReader reader = new FileReader(adminFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String names = line.substring(0,line.indexOf(" "));
                int Index = line.indexOf(" ", line.indexOf(" ") + 1) + 1;
                String data = line.substring(Index, line.indexOf(" ", Index));
                if (names.equals(username)) {
                    budget = Integer.parseInt(data);
                }
            }
            bufferedReader.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return budget;
    }

    public static File login(String username,String password){
        if(getPassword(username).equals(password)){
            File userfile = new File(username+".txt");
            System.out.println("Successfully logged in!");
            return userfile;
        }
        else{
            System.out.println("Wrong Password!");
            return null;
        }
    }
    public static void main(String[] args) {
        System.out.println("log in(1) / Sign up(2)");
        Scanner sc = new Scanner(System.in);
        int answer = sc.nextInt();
        File userFile=null;
        String currentUser=null;
        switch (answer) {
            case 1 -> {
                System.out.println("Username: ");
                String username = sc.next();
                System.out.println("Password: ");
                String password = sc.next();
                userFile = login(username, password);
                currentUser = username;
                System.out.println("Welcome " +
                        currentUser);
            }
            case 2 -> {
                userFile = signup();
                currentUser = userFile.toPath().toString().substring(0,userFile.toPath().toString().indexOf("."));
                System.out.println("Welcome " +
                        currentUser);
            }
            default -> System.out.println("Enter a valid input!");
        }
        while(true) {
            System.out.println("""
                    Actions:\s
                    1. Add an Expanse
                    2. Show Expanses till now
                    3. Last Month Expanse
                    4. Last Year Expanse
                    5. Expanses after a specific date(specify the date)
                    6. Set Budget
                    7. login as different User
                    8. Signup as new User
                    9. Change password
                    10. Categorised expanses(specify the category)
                    11. Clear History
                    12. Exit
                    """);
            int input = sc.nextInt();
            switch (input){
                case 1-> add(userFile);
                case 2-> show(userFile);
                case 3-> showLastMonth(userFile);
                case 4-> showLastyear(userFile);
                case 5-> {
                    System.out.println("Enter the date in \"EEE MMM dd HH:mm:ss z yyyy format:\" ");
                    Scanner scanner = new Scanner(System.in);
                    String dateInput = scanner.nextLine();
                    SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                    try {
                        Date specificDate = formatter.parse(dateInput);
                        System.out.println("Expanse from " + specificDate);
                        show(userFile, specificDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                case 6->{
                    System.out.println("Your current Budget is " +
                            getBudget(currentUser));
                    System.out.println("Set new Budget:");
                    String budget = sc.next();
                    setBudget(currentUser,budget);
                }
                case 7->{
                    System.out.println("Username: ");
                    String username = sc.next();
                    System.out.println("Password: ");
                    String password = sc.next();
                    userFile = login(username, password);
                    currentUser = username;
                    System.out.println("Welcome " +
                            currentUser);
                }
                case 8->{
                    userFile = signup();
                    currentUser = userFile.toPath().toString().substring(0,userFile.toPath().toString().indexOf("."));
                    System.out.println("Welcome " +
                            currentUser);
                }
                case 9->{
                    System.out.println("New password:");
                    String newPassword = sc.next();
                    updatePassword(currentUser,newPassword);
                }
                case 10->{
                    String category = sc.next();
                    showCategory(userFile, category);
                }
                case 11->{
                    System.out.println("Are you sure you want to clear all data? (y/n)");
                    String response = sc.next();
                    if (response.equals("y") || response.equals("Y")) {
                        try {
                            assert userFile != null;
                            FileWriter fileWriter = new FileWriter(userFile);
                            fileWriter.write("");
                            fileWriter.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                case 12-> {
                    return;
                }
                default -> System.out.println("Please Enter a Valid Input!");
            }
        }
    }
}
