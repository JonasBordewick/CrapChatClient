package com.itsanoj.chat;

public class LoginException extends Exception{

    public LoginException(){
        super("Your Nickname is already taken, try another one.");
    }
}
