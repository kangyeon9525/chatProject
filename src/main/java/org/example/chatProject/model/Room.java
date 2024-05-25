// src/main/java/com/example/chat/model/Room.java
package org.example.chatProject.model;

import java.util.HashSet;
import java.util.Set;

public class Room {
    private String name;
    private Set<String> users = new HashSet<>();

    public Room(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void addUser(String user) {
        users.add(user);
    }

    public void removeUser(String user) {
        users.remove(user);
    }
}
