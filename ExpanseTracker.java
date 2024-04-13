import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class ExpanseTracker {
    public static void add(File file) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Expense Amount: ");
        int expanse = scanner.nextInt();
        System.out.println("Category: (food, travel, education, investement, others)");
        Scanner sc = new Scanner(System.in);
        String note = sc.nextLine();
        Date date = new Date();
        try {
            FileWriter myWriter = new FileWriter(file, true);
            myWriter.append(date + " " + expanse + "," + note + "\n");
            myWriter.close();
        } catch (Exception e) {
            System.out.println(e);
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
            System.out.println(e);
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
            System.out.println(e);
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
            System.out.println(e);
        }
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            int index = data.indexOf(",");
            if (data.substring(index + 1, data.length()).equals(category)) {
                System.out.println(data);
                int expanseIndex = data.indexOf(",", 30);
                categoryExpanses += Integer.parseInt(data.substring(29, expanseIndex));
            }
        }
        System.out.println("Total Category wise Expanses: " + categoryExpanses);
        myReader.close();
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

    public static String otp(){
        int randomRanges[][] = {{48,57},{65,90},{97,122}};
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i=0;i<5;i++){
            int dice = random.nextInt(0,randomRanges.length);
            otp.append((char)random.nextInt(randomRanges[dice][0],randomRanges[dice][1]));
        }
        return otp.toString();
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
            System.out.println("email verification successfull!!");
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
        File newUserFile = new File(email.substring(0,email.indexOf("@"))+".txt");
        return newUserFile;
    }

    public static void main(String[] args) {
        File myFile = signup();
        while (true) {
            System.out.println("1. Add an Expanse");
            System.out.println("2. Show Expanses till now");
            System.out.println("3. Last Month Expanse");
            System.out.println("4. Last Year Expanse");
            System.out.println("5. Expanses after a specific date(specify the date)");
            System.out.println("6. Exit");
            System.out.println("7. Clear History");
            System.out.println("8. categorised expanses(specify the category)");
            Scanner sc = new Scanner(System.in);
            int input = sc.nextInt();
            switch (input) {
                case 1:
                    add(myFile);
                    break;
                case 2:
                    show(myFile);
                    break;
                case 3:
                    showLastMonth(myFile);
                    break;
                case 4:
                    showLastyear(myFile);
                    break;
                case 5:
                    System.out.println("Enter the date in \"EEE MMM dd HH:mm:ss z yyyy formate:\" ");
                    Scanner scanner = new Scanner(System.in);
                    String dateInput = scanner.nextLine();
                    SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                    try {
                        Date specificDate = formatter.parse(dateInput);
                        System.out.println("Expanse from " + specificDate);
                        show(myFile, specificDate);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    break;
                case 6:
                    return;
                case 7:
                    System.out.println("Are you sure you want to clear all data? (y/n)");
                    Scanner sc2 = new Scanner(System.in);
                    String answer = sc2.next();
                    if (answer.equals("y") || answer.equals("Y")) {
                        try {
                            FileWriter fileWriter = new FileWriter(myFile);
                            fileWriter.write("");
                            fileWriter.close();
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    } else {
                        continue;
                    }
                    break;
                case 8:
                    Scanner sc3 = new Scanner(System.in);
                    String category = sc3.next();
                    showCategory(myFile, category);
                    break;
                default:
                    System.out.println("Please Enter a valid input");
                    break;
            }
        }

    }
}
