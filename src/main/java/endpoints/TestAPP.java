package endpoints;

import com.server.app.ServerRunnableApplication;
import com.server.app.annotation.ServerApplication;

@ServerApplication
public class TestAPP {
    public static void main(String[] args) {
        ServerRunnableApplication.run(TestAPP.class, args);
    }
}
