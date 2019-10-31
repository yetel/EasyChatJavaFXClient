package com.easychat.fx.support;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public enum UiBaseService {

	INSTANCE;

	/**
	 * 将任务转移给fxapplication线程延迟执行
	 *
	 * @param task
	 */
	public void runTaskInFxThread(Runnable task) {
		Platform.runLater(task);
	}

	public void printErrorMsg(Stage stage, String reason) {
		UiBaseService.INSTANCE.runTaskInFxThread(()->{
			Label errorMsg = (Label) stage.getScene().getRoot().lookup("#errorMsg");
			errorMsg.setTextFill(Color.RED);
			errorMsg.setAlignment(Pos.CENTER);
			errorMsg.setText(reason);
		});
	}

	public void printMsg(Stage stage, String reason) {
		UiBaseService.INSTANCE.runTaskInFxThread(()->{
			Label errorMsg = (Label) stage.getScene().getRoot().lookup("#errorMsg");
			errorMsg.setTextFill(Color.GREEN);
			errorMsg.setAlignment(Pos.CENTER);
			errorMsg.setText(reason);
		});
	}
}