package org.example.model;

public class Client {
    private int id;
    private String name;
    private String dni;

    //constructores
    public Client() {
    }

    public Client(String name, String dni) {
        this.name = name;
        this.dni = dni;
    }

    public Client(int id, String name, String dni) {
        this.id = id;
        this.name = name;
        this.dni = dni;
    }

    //getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

}
