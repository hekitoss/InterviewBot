import org.apache.log4j.Logger;

public class Application {
    private static final Logger log = Logger.getLogger(Application.class);

    public static void main(String[] args) {
        Bot bot = new Bot("interviewQuestionsAndAnswersBot", "5290032224:AAH0vRrLNMQ66AY4SafBpw83YbIgs2bJUAk");
        bot.botConnect();
    }
}
