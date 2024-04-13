import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.nio.file.Files;
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
public class ExpanseTracker1_1 {
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

    public static void setPassword(String username, String password){
        File admin = new File("admin.txt");
        try{
            FileWriter writer = new FileWriter(admin,true);
            writer.append(username+" "+password+" \n");
            writer.close();
        }
        catch (Exception e){
            System.out.println(e);
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
            System.out.println(e);
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
                    writer.write(username +" "+ newPassword +" \n");
                } else {
                    writer.write(line + "\n");
                }
            }
            bufferedReader.close();
            writer.close();
        }
        catch (Exception e){
            System.out.println(e);
        }
        try {
            Files.delete(adminFile.toPath());
            Files.move(tempFile.toPath(), adminFile.toPath());
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
    public static void main(String[] args) {

    }
}
