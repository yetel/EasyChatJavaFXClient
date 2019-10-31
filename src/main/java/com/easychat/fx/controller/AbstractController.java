package com.easychat.fx.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public abstract class AbstractController implements Initializable {
    @FXML
    protected Label errorMsg;
}
