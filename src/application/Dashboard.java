package application;

import javafx.event.ActionEvent;

public interface Dashboard {
    void userInfo(String name, String user, String type);
    void logout(ActionEvent event);
}
