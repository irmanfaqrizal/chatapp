import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.TimerTask;

public class handlerScheduleHistory extends TimerTask {

    private List<String> tmpMessages;

    public handlerScheduleHistory (List<String> messages) {
        this.tmpMessages = messages;
    }

    @Override
    public void run() {
        try {
            FileWriter fw = new FileWriter("history.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            new PrintWriter("history.txt").close(); // empty the content 1st
            PrintWriter out = new PrintWriter(bw);
            for (String message : this.tmpMessages) {
                out.println(message);
            }
            out.close();
        } catch (Exception e) {}
    }
}