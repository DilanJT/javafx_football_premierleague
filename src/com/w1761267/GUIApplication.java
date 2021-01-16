package com.w1761267;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;



public class GUIApplication {
    Scene mainScene, statScene, matchesScene;
    Stage window;

    public void dashBoard(PremierLeagueManager pManager){
        window = new Stage();
        window.setTitle("w1761267-Premier League Championship");
        // GUI window
        // initial screen or the dashboard screen of the application

        // includes the [Button] displayTeamStats option
        // includes the [Button] button to add a randomly played match by pressing it
        // includes the [Button] to displayPlayed matches option

        Button matchBtn = new Button("Display played matches");
        matchBtn.setPrefSize(350, 50);
        matchBtn.setOnAction(e -> {
            displayPlayedMatch(pManager);
            window.setScene(matchesScene);
        });

        Label label1 = new Label("Premier League Championship");
        label1.setStyle("-fx-font-size: 40");
        Button statBtn = new Button("Display Football Club statistics");
        statBtn.setPrefSize(350, 50);
        statBtn.setOnAction(e -> {
            displayTeamsStats(pManager);
            window.setScene(statScene);
        });


        Alert a = new Alert(AlertType.NONE);

        Button genMatchBtn = new Button("Generate a Random match");
        genMatchBtn.setPrefSize(350, 50);
        genMatchBtn.setOnAction(e -> {
            //checking if the football clubs exist more than one to play a match
            if(pManager.getFootballClubsSize() > 1) {
                addRandomlyPlayedMatch(pManager, a);
                a.setAlertType(AlertType.INFORMATION);
                a.show();
            }else{
                a.setAlertType(AlertType.ERROR);
                a.setContentText("There are not enough teams registered to play a match.");
                a.show();
            }
        });

        Button backtoConsole = new Button("Back to Console");
        backtoConsole.setPrefSize(350, 50);
        backtoConsole.setOnAction(e -> window.close());

        //Layout1 - children are laid out in vertical column
        VBox layout1 = new VBox(20);
        layout1.getChildren().addAll(label1, statBtn, matchBtn, genMatchBtn, backtoConsole);
        layout1.setAlignment(Pos.CENTER);
        layout1.setStyle("-fx-base: #1a2040;" +
                "-fx-accent: #222940 ;" +
                "-fx-default-button: #37638c ;" +
                "-fx-focus-color: #6387a6;" +
                "-fx-faint-focus-color: #9ccbd9;");

        mainScene = new Scene(layout1, 800, 600);
        window.setScene(mainScene);
        window.showAndWait();

    }

    public void displayTeamsStats(PremierLeagueManager pManager){
        // GUI window
        // display all the teams and their stats descending order according to their points
        // option to sort according to the goals scored in descending order
        // option to sort according to the largest number of wins


        //button to go back to the main scene(window)
        Label label1 = new Label("Stats!");
        label1.setStyle("-fx-font-size: 30");
        Label labelNote = new Label("Note: Click any field header if you want to display entries in " +
                "descending or ascending order in the values of that field ");
        Button btnBack = new Button("Go back");
        btnBack.setOnAction(e -> window.setScene(mainScene));

        //layout for display team stats table
        VBox statsLayout = new VBox(20);
        statsLayout.getChildren().addAll(label1, labelNote, pManager.getStatTable(), btnBack);
        statsLayout.setAlignment(Pos.CENTER);
        statsLayout.setStyle("-fx-base: #1a2040;" +
                "-fx-accent: #222940 ;" +
                "-fx-default-button: #37638c ;" +
                "-fx-focus-color: #6387a6;" +
                "-fx-faint-focus-color: #9ccbd9;");
        statScene = new Scene(statsLayout, 800, 600);
    }

    public void addRandomlyPlayedMatch(PremierLeagueManager pManager, Alert a){
        // Logic for the button to create a randomly played match

        int day = (int) (Math.random() * (31 - 1 + 1) + 1);
        int month = (int) (Math.random() * (12 - 1 + 1) + 1);
        int year = (int) (Math.random() * (2021 - 2020 + 1) + 2020);
        int hour = (int) (Math.random() * (23 - 0 + 1) + 0);
        int min = (int) (Math.random() * (59 - 0 + 1) + 0);

        MatchDate randomDate = new MatchDate(day, month, year, hour, min);

        FootballClub firstTeam = new FootballClub();
        FootballClub secondTeam = new FootballClub();

        int firstTeamIndex = (int) (Math.random() * ((pManager.getFootballClubsSize() - 1) - 0 + 1) + 0);
        int secTeamIndex = (int) (Math.random() * ((pManager.getFootballClubsSize() - 1) - 0 + 1) + 0);
        while(secTeamIndex == firstTeamIndex){
            secTeamIndex = (int) (Math.random() * ((pManager.getFootballClubsSize() - 1) - 0 + 1) + 0);
        }

        firstTeam.setClubName(pManager.getFootballClub(firstTeamIndex).getClubName());
        firstTeam.setFootballClubID(pManager.getFootballClub(firstTeamIndex).getFootballClubID());

        secondTeam.setClubName(pManager.getFootballClub(secTeamIndex).getClubName());
        secondTeam.setFootballClubID(pManager.getFootballClub(secTeamIndex).getFootballClubID());

        clubMatchData(firstTeam);
        clubMatchData(secondTeam);

        pManager.addPlayedMatch(randomDate, firstTeam, secondTeam);
        a.setContentText("Successfully generated a match with " + firstTeam.getClubName() + " & " +
                secondTeam.getClubName());
    }

    public void displayPlayedMatch(PremierLeagueManager pManager){
        // GUI window
        // display the played matches in ascending order
        // option [Text Box] to search with the date where the match has played

        Label label1 = new Label("Matches played");
        label1.setStyle("-fx-font-size: 30");

        HBox searchLayout = new HBox(20);
        searchLayout.setAlignment(Pos.CENTER);
        TextField dateField = new TextField();

        Button searchDateBtn = new Button("Search");


        searchLayout.getChildren().addAll(dateField, searchDateBtn);


        Button btnBack = new Button("Go back");
        btnBack.setOnAction(e -> window.setScene(mainScene));

        //layout for display played matches table
        VBox matchesLayout = new VBox(20);
        matchesLayout.getChildren().addAll(label1, searchLayout, pManager.getMatches(dateField, searchDateBtn), btnBack);
        matchesLayout.setAlignment(Pos.CENTER);
        matchesLayout.setStyle("-fx-base: #1a2040;" +
                "-fx-accent: #222940 ;" +
                "-fx-default-button: #37638c ;" +
                "-fx-focus-color: #6387a6;" +
                "-fx-faint-focus-color: #9ccbd9;");
        matchesScene = new Scene(matchesLayout, 800, 600);
    }

    //other functions
    public void clubMatchData(FootballClub team){

        //generating random numbers for the match for a single team
        int goalsScored = (int) (Math.random() * (50 - 0 + 1) + 0);
        int goalsConceded = (int) (Math.random() * (goalsScored - 0 + 1) + 0);
        int yellowCards = (int) (Math.random() * (46 - 0 + 1) + 0);
        int redCards = (int) (Math.random() * (4 - 0 + 1) + 0);

        team.setGoalsScored(goalsScored);
        team.setGoalsConceded(goalsConceded);
        team.setYellowCards(yellowCards);
        team.setRedCards(redCards);
        if(goalsConceded == 0) {
            team.setCleanSheets(1);
        }

    }


}
