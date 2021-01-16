package com.w1761267;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.util.*;


public class PremierLeagueManager implements LeagueManager{

    final static public int MAX_CLUBS = 20; //In premier league championship only 20 clubs plays
    private List<FootballClub> clubs = new ArrayList<>(); //arraylist to hold all the registered(add) clubs in the premier league
    private List<PremierLeagueMatch> matchesPlayed = new ArrayList<>(); // hold all the matches played


    @Override 
    public void addFootballClub(FootballClub fbc){
        //add football clubs
        if(clubs.size() < MAX_CLUBS) {
            if(!checkClubPresence(fbc)){
                clubs.add(fbc);
                System.out.println(fbc.getClubName() + " got successfully added.");
            }else{
                System.out.println(fbc.getClubName() + " has been already added to the premier league");
            }

        }else{
            System.out.println("Sorry "+ fbc.getClubName() + "cannot be added !, Premier league clubs can have only 20 clubs");
        }

    }

    @Override
    public void deleteFootballClub(FootballClub fbc){
        //delete football club

        for (FootballClub f : clubs) {
            if (f.equals(fbc)) {
                clubs.remove(f);
                System.out.println(fbc.getClubName() + " successfully relegated from the league.");
                break;
            }
        }

    }

    @Override
    public void displayFootballClubStats(FootballClub fbc){
        //display football club stats

        String format = "%-17s%03d%n";


        for(FootballClub f : clubs) {
            if(f.equals(fbc)){
                System.out.println("\nClub Name :" + f.getClubName());
                System.out.println("Club ID :" + f.getFootballClubID());
                System.out.println("Club location :"+f.getLocation());
                System.out.println("\n---- | "+f.getClubName()+" Stats | ----\n");
                System.out.printf(format, "No. of Matches :", f.getNumMatches());
                System.out.printf(format, "Points :", f.getPoints());
                System.out.printf(format, "Goals Scored :", f.getGoalsScored());
                System.out.printf(format, "Goals Conceded :", f.getGoalsConceded());
                System.out.printf(format, "Goal Difference :", f.getGoalDifference());
                System.out.printf(format, "Clean Sheets :", f.getCleanSheets());
                System.out.printf(format, "Yellow cards :", f.getYellowCards());
                System.out.printf(format, "Red cards :", f.getRedCards());
                System.out.printf(format, "Wins :", f.getWins());
                System.out.printf(format, "Losses :", f.getLosses());
                System.out.printf(format, "Draws :", f.getDraws());
            }
        }

    }

    @Override
    public void displayPremierLeagueTable(){
        System.out.println("\n---- | Premier League table | ----\n");
        //display premier league matches played - Table mode
        PointsComparator pc = new PointsComparator();
        Collections.sort(clubs, pc);

        int nameSize = 13;
        for(FootballClub f : clubs) {
            if(f.getClubName().length() > nameSize){
                nameSize = f.getClubName().length();
            }
        }
        nameSize += 2; // to add 2 spaces
        String clubSize = "%-2s%02d%-2s%-" + nameSize +"s%-5s%03d%-6s%03d%-15s%n";

        String field1 = "Rank";
        String field2 = "Football Club";
        String field3 = "Points";
        String field4 = "Goal Difference";

        String fieldTopic = "%-4s%-2s%-"+ nameSize + "s%-2s%-6s%-2s%-15s%n";

        String dashClubs = "";
        for(int i = 0; i < nameSize; i++) {
            dashClubs = dashClubs +"-";
        }
        System.out.printf(fieldTopic, "----", "  ", dashClubs, "  ", "------", "  ", "---------------");
        System.out.printf(fieldTopic, field1, "  ", field2, "  ", field3, "  ", field4);
        System.out.printf(fieldTopic, "----", "  ", dashClubs, " ", "------", "  ", "---------------");

        for(int i =0; i < clubs.size(); i++) {
            System.out.printf(clubSize, "  ", i+1, "  ", clubs.get(i).getClubName(), "  ", clubs.get(i).getPoints(), "  ",
                    clubs.get(i).getGoalDifference(), "  ");
        }

    }

    @Override
    public void addPlayedMatch(MatchDate matchDate, FootballClub firstClub, FootballClub secClub){

        PremierLeagueMatch plMatch = new PremierLeagueMatch();
        plMatch.setMatchDate(matchDate);

        //setting the values for match

        if(firstClub.getGoalsScored() > secClub.getGoalsScored()){
            firstClub.incrementWins(); //increase the number of wins by 1
            firstClub.setPoints(3);
            secClub.incrementLosses(); //increase the number of losses by 1
            secClub.setPoints(0);
            plMatch.setWonTeam(firstClub);
            plMatch.setLossTeam(secClub);
            plMatch.setDraw(false);
            System.out.println(firstClub.getClubName() + " won");

        }else if(firstClub.getGoalsScored() < secClub.getGoalsScored()){
            firstClub.incrementLosses();
            firstClub.setPoints(0);
            secClub.incrementWins();
            secClub.setPoints(3);
            plMatch.setWonTeam(secClub);
            plMatch.setLossTeam(firstClub);
            plMatch.setDraw(false);
            System.out.println(secClub.getClubName() + " won");
        }else{
            plMatch.setDraw(true);
            firstClub.incrementDraws();
            firstClub.setPoints(1);
            secClub.incrementDraws();
            secClub.setPoints(1);
        }

        plMatch.setTwoClubs(firstClub, secClub);
        matchesPlayed.add(plMatch);

        //updating the overall stats of football clubs throwout the season in the premier league
        updateFootballClub(firstClub);
        updateFootballClub(secClub);
    }

    //additional - display played match statistics
    public void displayMatchesPlayed(){
        //display match statistics - Table mode
        int firstTeam = 0;
        int secTeam = 0;
        for(PremierLeagueMatch match : matchesPlayed) {
            if(match.getTwoClubs()[0].getClubName().length() > firstTeam)
                firstTeam = match.getTwoClubs()[0].getClubName().length();
            if(match.getTwoClubs()[1].getClubName().length() > secTeam)
                secTeam = match.getTwoClubs()[1].getClubName().length();
        }
        System.out.println("\n---- | Matches Played | ----");
        String format = "%-10s%-2s%-" + firstTeam + "s%-1s%01d%-2s%-2s%-2s%-"+ secTeam + "s%-1s%01d%n";
        for(PremierLeagueMatch match : matchesPlayed) {
            System.out.printf(format,
                    match.getMatchDate().getDate(), " ",
                    match.getTwoClubs()[0].getClubName(), " ",
                    match.getTwoClubs()[0].getPoints(), " ", "vs", " ",
                    match.getTwoClubs()[1].getClubName(), " ",
                    match.getTwoClubs()[1].getPoints());
        }
    }

    public void loadData() throws IOException{
        //loads saved data in the file to the program
        //exception handling, to avoid the error when the program run for the first time.
        //error handling: ObjectInput stream dosen't have any binary data to read.

        try {
            FileInputStream footballFileIn = new FileInputStream("footballclubinfo.txt");
            FileInputStream matchFileIn = new FileInputStream("footballmatchinfo.txt");

            ObjectInputStream footballObjectIn = new ObjectInputStream(footballFileIn);
            ObjectInputStream matchObjectIn = new ObjectInputStream(matchFileIn);

            while (true) {
                try {
                    FootballClub fClub = (FootballClub) footballObjectIn.readObject();
                    clubs.add(fClub);
                    //System.out.println("Object loaded");

                } catch (IOException | ClassNotFoundException e) {
                    //System.out.println("Class not found error.");
                    break;
                }
            }

            while (true) {
                try {
                    PremierLeagueMatch match = (PremierLeagueMatch) matchObjectIn.readObject();
                    matchesPlayed.add(match);
                    //System.out.println("Object loaded");

                } catch (IOException | ClassNotFoundException e) {
                    //System.out.println("Class not found error.");
                    break;
                }
            }
            System.out.println("File data loaded");
            footballObjectIn.close();
            matchObjectIn.close();
            footballFileIn.close();
            matchFileIn.close();
        }catch(EOFException e){
            System.out.println("EOFException error");
        }catch (FileNotFoundException e){
            System.out.println("file not found");
        }


        //System.out.println("\nNote: Data successfully loaded. You resume from where you saved.\n");

    }

    public void saveData() throws IOException{
        //saves the program data to files.
        File footballFile = new File("footballclubinfo.txt");
        File matchFile = new File("footballmatchinfo.txt");

        FileOutputStream footballFileOut = new FileOutputStream(footballFile);
        FileOutputStream matchFileOut = new FileOutputStream(matchFile);

        ObjectOutputStream footballObjectOut = new ObjectOutputStream(footballFileOut);
        ObjectOutputStream matchObjectOut = new ObjectOutputStream(matchFileOut);

        for(FootballClub f : clubs) {
            footballObjectOut.writeObject(f);
        }

        for(PremierLeagueMatch pmMatch : matchesPlayed) {
            matchObjectOut.writeObject(pmMatch);
        }


        footballObjectOut.flush();
        matchObjectOut.flush();
        footballFileOut.close();
        matchFileOut.close();
        footballObjectOut.close();
        matchObjectOut.close();

    }

    //other functions

    public void updateFootballClub(FootballClub fbc) {
        //This function is for updating clubs for every single match played.

        for(FootballClub f : clubs){
            if(f.equals(fbc)){
                f.addGoalsScored(fbc.getGoalsScored());
                f.addGoalsConceded(fbc.getGoalsConceded());
                f.addCleanSheets(fbc.getCleanSheets());
                f.addRedCards(fbc.getRedCards());
                f.addYellowCards(fbc.getYellowCards());
                f.addWins(fbc.getWins());
                f.addLosses(fbc.getLosses());
                f.addDraws(fbc.getDraws());
                f.addPoints(fbc.getPoints());
                f.incrementNumMatches();
            }
        }
    }

    public TableView getStatTable(){
        //Setting up the table view
        TableView statTableView = new TableView();

        TableColumn<FootballClub, String> column1 = new TableColumn<>("Club Name");
        column1.setCellValueFactory(new PropertyValueFactory<>("clubName"));

        TableColumn<FootballClub, String> column2 = new TableColumn<>("Location");
        column2.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<FootballClub, Integer> column3 = new TableColumn<>("Matches");
        column3.setCellValueFactory(new PropertyValueFactory<>("numMatches"));

        TableColumn<FootballClub, Integer> column4 = new TableColumn<>("Points");
        column4.setCellValueFactory(new PropertyValueFactory<>("points"));

        TableColumn<FootballClub, Integer> column5 = new TableColumn<>("Wins");
        column5.setCellValueFactory(new PropertyValueFactory<>("wins"));

        TableColumn<FootballClub, Integer> column6 = new TableColumn<>("Losses");
        column6.setCellValueFactory(new PropertyValueFactory<>("losses"));

        TableColumn<FootballClub, Integer> column7 = new TableColumn<>("Draws");
        column7.setCellValueFactory(new PropertyValueFactory<>("draws"));

        TableColumn<FootballClub, Integer> column8 = new TableColumn<>("Goals Scored");
        column8.setCellValueFactory(new PropertyValueFactory<>("goalsScored"));

        statTableView.getColumns().addAll(column1, column2, column3, column4, column5, column6,
                column7, column8);

        for(FootballClub f : clubs){
            statTableView.getItems().add(new FootballClub(
                    f.getClubName(),
                    f.getLocation(),
                    f.getNumMatches(),
                    f.getPoints(),
                    f.getWins(),
                    f.getLosses(),
                    f.getDraws(),
                    f.getGoalsScored()
            ));
        }

        return statTableView;
    }

    public TableView getMatches(TextField sField, Button sBtn){
        TableView matchesTableView = new TableView();
        ObservableList<Map<String, Object>> items = FXCollections.observableArrayList();

        Map<String, Object> matchItem;
        DateComparator dateComparator = new DateComparator();
        Collections.sort(matchesPlayed, dateComparator);


        for(PremierLeagueMatch match : matchesPlayed){
            matchItem = new HashMap<>();
            matchItem.put("date", match.getMatchDate().getDate());
            matchItem.put("time", match.getMatchDate().getTime());
            matchItem.put("teamOne", match.getTwoClubs()[0].getClubName());
            matchItem.put("teamTwo", match.getTwoClubs()[1].getClubName());
            items.add(matchItem);

        }
        matchesTableView.setItems(items);

        sField.textProperty().addListener((observable, oldValue, newValue) ->{
            sBtn.setOnAction(e -> {
                matchesTableView.setItems(filterList(newValue));
            });

        });

        TableColumn<Map, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new MapValueFactory<>("date"));

        TableColumn<Map, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(new MapValueFactory<>("time"));

        TableColumn<Map, String> tOneColumn = new TableColumn<>("Team One");
        tOneColumn.setCellValueFactory(new MapValueFactory<>("teamOne"));

        TableColumn<Map, String> tTwoColumn = new TableColumn<>("Team Two");
        tTwoColumn.setCellValueFactory(new MapValueFactory<>("teamTwo"));

        matchesTableView.getColumns().addAll(dateColumn, timeColumn, tOneColumn, tTwoColumn);

        return matchesTableView;
    }

    //to find the match by date
    public boolean searchFinderMatch(PremierLeagueMatch match, String searchText){
        //check for the date in the formats 1/1/2020
        return (match.getMatchDate().getDate().contains(searchText.toLowerCase()));
    }

    public ObservableList<Map<String, Object>> filterList(String searchText){
        List<Map<String, Object>> filteredList = new ArrayList<>();
        Map<String, Object> matchItem;
        for(PremierLeagueMatch match : matchesPlayed){
            if(searchFinderMatch(match, searchText)) {
                matchItem = new HashMap<>();
                matchItem.put("date", match.getMatchDate().getDate());
                matchItem.put("time", match.getMatchDate().getTime());
                matchItem.put("teamOne", match.getTwoClubs()[0].getClubName());
                matchItem.put("teamTwo", match.getTwoClubs()[1].getClubName());
                filteredList.add(matchItem);
            }
        }
        return FXCollections.observableList(filteredList);
    }



    public boolean checkClubPresence(FootballClub fbc){
        // this function is to check the presence of the club
        // usages -> check similarities & validate
        return clubs.contains(fbc);
    }
    //return the number of the football clubs
    public int getFootballClubsSize(){
        return this.clubs.size();
    }

    public FootballClub getFootballClub(int index){
        return clubs.get(index);
    }




}
