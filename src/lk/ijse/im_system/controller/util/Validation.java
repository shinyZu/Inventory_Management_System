package lk.ijse.im_system.controller.util;

import javafx.scene.control.TextField;

import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public interface Validation {

    void mapValidations();

    default Object validateFields(LinkedHashMap<TextField,Pattern> map) {

        for (TextField keyTextField : map.keySet()) {
            Pattern valuePatten = map.get(keyTextField);

            if (!valuePatten.matcher(keyTextField.getText()).matches()) { // if the inserted text doesn't match with pattern
                if (!keyTextField.getText().isEmpty()) {
                    keyTextField.getStylesheets().clear();
                    keyTextField.getStylesheets().add("lk/ijse/im_system/view/assets/styles/invalidInput.css");
                }
                return keyTextField;

            }

            keyTextField.getStylesheets().clear();
            keyTextField.getStylesheets().add("lk/ijse/im_system/view/assets/styles/validInput.css");

        }
        return true;
    }

    default Object validateFormFields(LinkedHashMap<TextField, Pattern> mapNewWardDetails) {

        for (TextField keyTextField : mapNewWardDetails.keySet()) {
            Pattern valuePatten = mapNewWardDetails.get(keyTextField);

            if (!valuePatten.matcher(keyTextField.getText()).matches()) { // if the inserted text doesn't match with pattern
                if (!keyTextField.getText().isEmpty()) {
                    keyTextField.setStyle("-fx-border-color: red; -fx-background-color: transparent; -fx-border-radius: 3;");
                }
                return keyTextField;

            }
            keyTextField.setStyle("-fx-border-color: green; -fx-background-color: transparent; -fx-border-radius: 3;");

        }
        return true;

    }
}
