package com.subhasis.devisbase.commands;

public interface iCommands {	
	
	public void process(String command);
	public boolean evaluate(String command);
	public boolean validateCommand(String command);
	
}
