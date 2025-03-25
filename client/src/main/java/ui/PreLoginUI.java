package ui;
import java.util.Scanner;

import exception.ResponseException;
import model.AuthData;
import model.UserData;
import server.ServerFacade;

public class PreLoginUI {
    ServerFacade server;
    Scanner scanner;

    public PreLoginUI(ServerFacade server) {
        this.server = server;
        this.scanner = new Scanner(System.in);
    }

    public void run(){
        System.out.println("Welcome to 240 Chess! Type 'Help' to get started!");

        while (true){
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();

            switch (line){
                case "help":
                    System.out.println("\nAvailable Commands:");
                    System.out.println("  help - Show this help menu");
                    System.out.println("  quit - Exit the program");
                    System.out.println("  login <USERNAME> <PASSWORD> - Log in with an existing account");
                    System.out.println("  register <USERNAME> <PASSWORD> <EMAIL> - Create a new account\n");
                    break;
                case "quit":
                    System.out.println("Quitting...");
                    return;
                case "login":
                    login();
                    break;
                case "register":
                    register();
                    break;
                default:
                    System.out.println("Invalid command. Type 'help' for options.");
            }
        }

    }

    public void login(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            UserData user = new UserData(username, password, null);

            AuthData authData = server.login(user);
            System.out.println("Login successful!");

            PostLoginUI postLoginUI = new PostLoginUI(authData, server);
            postLoginUI.run();
        } catch (ResponseException e) {
            System.out.println("Login failed: " + e.getMessage());
        }

    }

    public void register(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Username: ");
        String username = scanner.nextLine();

        System.out.println("Password: ");
        String password = scanner.nextLine();

        System.out.println("Email: ");
        String email = scanner.nextLine();

        try {
            UserData user = new UserData(username, password, email);
            AuthData authData = server.register(user);
            System.out.println("Register successful!");

            PostLoginUI postLoginUI = new PostLoginUI(authData, server);
            postLoginUI.run();

        } catch (ResponseException e) {
            System.out.println("Register failed: " + e.getMessage());
        }
    }

}
