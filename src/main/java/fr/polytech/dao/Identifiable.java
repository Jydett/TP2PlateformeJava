package fr.polytech.dao;

public interface Identifiable<Id> {
    Id getId();
    void setId(Id id);
}
