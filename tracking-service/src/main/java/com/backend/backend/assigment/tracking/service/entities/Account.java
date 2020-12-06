package com.backend.backend.assigment.tracking.service.entities;

import javax.persistence.*;

@Entity
@Table(name = "account")
@NamedQueries({
        @NamedQuery(
                name = "Account.findAccounts",
                query = "SELECT a " +
                        "FROM Account a"
        )
})
public class Account {
    @Id
    private String id;
    private String name;
    private boolean isActive;

    public Account() {}

    public Account(String id, String name, boolean isActive) {
        this.id = id;
        this.name = name;
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
