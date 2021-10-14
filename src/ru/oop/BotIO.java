package ru.oop;

public class BotIO {
    public void printResponse(Response botResponse) {
        System.out.println(botResponse.getResponseString());

    }

    public Response sendRequest(String query) {
        BotLogic botLogic = new BotLogic();
        return botLogic.requestHandler(query);
    }
}
