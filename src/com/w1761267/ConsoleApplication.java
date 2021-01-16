package com.w1761267;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleApplication extends Application {

    static PremierLeagueManager pManager = new PremierLeagueManager();
    final static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        launch(args);
    }


    public static void displayMenu(){
        //display the menu functions

        System.out.println("|1| Press 1 to add a football club");
        System.out.println("|2| Press 2 to delete(relegate) a football club");
        System.out.println("|3| Press 3 to display statistics of a club");
        System.out.println("|4| Press 4 to display the premier league table");
        System.out.println("|5| Press 5 to add a played match");
        System.out.println("|6| Press 6 to display the matches played");
        System.out.println("|g| Press g to invoke the GUI application.");
        System.out.println("|q| Press q to quit the program");

    }

    public static void addFootballClub(){
        //adding a football club

        System.out.print("Enter name of the football club :");
        String clubName = sc.next();
        System.out.print("Enter the location of the football club :");
        String location = sc.next();
        System.out.print("Enter the football club ID :");
        int clubID = checkForInputMismatch("club ID");

        FootballClub fbc = new FootballClub(clubName, location, clubID);
        pManager.addFootballClub(fbc);
    }

    public static void deleteFootballClub(){
        //delete a football club
        System.out.print("Enter the football club name you want to delete :");
        String clubName = sc.next();
        System.out.print("Enter the football club ID :");
        int clubID = checkForInputMismatch("club ID");

        FootballClub fbc = new FootballClub(clubName, clubID);
        while(true) {
            if (pManager.checkClubPresence(fbc)) {
                pManager.deleteFootballClub(fbc);
                break;
            } else if(!pManager.checkClubPresence(fbc)) {
                System.out.println("Sorry, there is no such club present in the premier league. Try again.");
                System.out.print("Enter the club name you want to delete :");
                clubName = sc.next();
                System.out.print("Enter the club ID :");
                clubID = checkForInputMismatch("club ID");
                fbc = new FootballClub(clubName, clubID);
            }
        }


    }

    public static void displayFootballClubStats(){
        //display the club stats

        System.out.print("Enter the name of the club :");
        String clubName = sc.next();
        System.out.print("Enter the numerical Football Club ID :");
        int footballClubID = checkForInputMismatch("club ID");

        FootballClub fbc = new FootballClub(clubName, footballClubID);

        while(true) {
            if(pManager.checkClubPresence(fbc)){
                pManager.displayFootballClubStats(fbc);
                break;
            }else {
                System.out.println("The entered name is not registered to this league. Please try again...");
                System.out.print("Name of club :");
                clubName = sc.next();
                System.out.print("Football Club ID :");
                footballClubID = checkForInputMismatch("club ID");
                fbc = new FootballClub(clubName, footballClubID);
            }
        }
    }

    public static void addPlayedMatch(){
        //add a played match with the statistics
        System.out.println("Enter the date of the match played :");
        System.out.print("Day :");
        int day = checkForInputMismatch("Day");
        System.out.print("Month :");
        int month = checkForInputMismatch("Month");
        System.out.print("Year :");
        int year = checkForInputMismatch("Year");

        System.out.println("Enter the time of the match played :");
        System.out.print("Hour :");
        int hour = checkForInputMismatch("Hour");
        System.out.print("Minute :");
        int minute = checkForInputMismatch("Minute");

        MatchDate pMatch = new MatchDate(day, month, year, hour, minute);

        FootballClub firstTeam = new FootballClub();
        FootballClub secondTeam = new FootballClub();

        System.out.println("\nEnter the clubs and their played match stats.\n");

        clubMatchData(firstTeam, "first");
        clubMatchData(secondTeam, "second");

        pManager.addPlayedMatch(pMatch, firstTeam, secondTeam);

    }

    //other functions

    public static void clubMatchData(FootballClub team, String name){

        //asking the data of the first team
        System.out.println("Enter the "+ name + " team data");
        System.out.print("Team name :");
        String teamName = sc.next();
        System.out.print("Enter team ID :");
        int footballClubID = checkForInputMismatch("club ID");

        team.setClubName(teamName);
        team.setFootballClubID(footballClubID);

        while(true){
            if(pManager.checkClubPresence(team)){
                System.out.print("goals scored :");
                int goalsScored = checkForInputMismatch("goals scored");
                System.out.print("goals conceded :");
                int goalsConceded = checkForInputMismatch("goals conceded");
                System.out.print("yellow cards :");
                int yellowCards = checkForInputMismatch("yellow cards");
                System.out.print("Red cards :");
                int redCards = checkForInputMismatch("red cards");

                team.setGoalsConceded(goalsConceded);
                team.setGoalsScored(goalsScored);
                team.setYellowCards(yellowCards);
                team.setRedCards(redCards);
                if(goalsConceded == 0)
                    team.addCleanSheets(1);

                break;
            }else{
                System.out.println("The entered name is not registered to this league. Please try again...");
                System.out.print("Name of club :");
                teamName = sc.next();
                System.out.print("Football Club ID :");
                footballClubID = checkForInputMismatch("club ID");
                team.setClubName(teamName);
                team.setFootballClubID(footballClubID);
            }
        }
    }

    public static int checkForInputMismatch(String var){
        int num;
        while(true) {
            try {
                num = sc.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.print("Please enter a valid numerical value for " + var + " again :");
                sc.nextLine();
            }
        }

        return num;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        GUIApplication guiApplication = new GUIApplication();


        pManager.loadData();

        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println("Welcome to the Premier League Manager.");
        System.out.println("--------------------------------------");
        System.out.println();

        displayMenu();
        System.out.print("Enter your option here :");
        String menuOption = sc.next();


        //covert to switch

        while (true) {
            if (menuOption.equals("1")) {
                addFootballClub();
            } else if (menuOption.equals("2")) {
                deleteFootballClub();
            } else if (menuOption.equals("3")) {
                displayFootballClubStats();
            } else if (menuOption.equals("4")) {
                pManager.displayPremierLeagueTable();
            } else if (menuOption.equals("5")) {
                addPlayedMatch();
            } else if (menuOption.equals("6")) {
                pManager.displayMatchesPlayed();
            } else if (menuOption.equals("g")) {
                guiApplication.dashBoard(pManager);
            } else if (menuOption.equalsIgnoreCase("q")) {
                System.out.println("Good bye...");
                pManager.saveData();
                break;
            } else {
                System.out.println("You entered a wrong option");
            }

            System.out.println();
            displayMenu();
            System.out.print("Enter your option here :");
            menuOption = sc.next();
        }

    }


}
