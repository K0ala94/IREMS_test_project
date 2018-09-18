package controllers;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class FlightsWebController {
	
	private String str = "kiskutya";
	public String test(){
		return "hello irems";
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}

}
