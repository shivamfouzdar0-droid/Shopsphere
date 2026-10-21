package com.shopsphere.network.model;

import java.io.Serializable;

public class NetworkRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String command; // GET_PRODUCT, GET_STOCK, SEARCH_PRODUCT, LIST_ALL
    private String argument;

    public NetworkRequest() {}

    public NetworkRequest(String command, String argument) {
        this.command = command;
        this.argument = argument;
    }

    public String getCommand() { return command; }
    public void setCommand(String command) { this.command = command; }

    public String getArgument() { return argument; }
    public void setArgument(String argument) { this.argument = argument; }
}
