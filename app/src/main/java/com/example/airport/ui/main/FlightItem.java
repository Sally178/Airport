package com.example.airport.ui.main;

public class FlightItem {
    private String ID;
    private String original;
    private String destination;
    private String pass;
    private String planned;
    private String terminal;
    private String status;

    public FlightItem(){
        super();
        ID="";
        original="";
        destination="";
        pass="";
        planned="";
        terminal="";
        status="";
    }
    public FlightItem(String ID,String original,String destination,String pass, String planned,String terminal,String status){
        super();
        this.ID=ID;
        this.original=original;
        this.destination=destination;
        this.pass=pass;
        this.planned=planned;
        this.terminal=terminal;
        this.status=status;
    }

    public String getId(){
        return ID;
    }
    public void setId(String ID){
        this.ID=ID;
    }
    public String getOriginal(){
        return original;
    }
    public void setOriginal(String original) {
        this.original = original;
    }
    public String getDestination(){return destination;}
    public void setDestination(String destination){this.destination=destination;}
    public String getPass(){return pass;}
    public void setPass(String pass){this.pass=pass;}
    public String getPlanned(){return planned;}
    public void setPlanned(String planned){this.planned=planned;}
    public String getTerminal(){return terminal;}
    public void setTerminal(String terminal){this.terminal=terminal;}
    public String getStatus(){return status;}
    public void setStatus(String status){this.status=status;}
}

