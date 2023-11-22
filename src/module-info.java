module VacationPlanner {
	requires javafx.controls;
	requires org.json;
	requires java.net.http;
	requires java.sql;
	requires com.fasterxml.jackson.databind;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	requires java.desktop;
	requires org.controlsfx.controls;
	
	opens application to javafx.graphics, javafx.fxml;
}
