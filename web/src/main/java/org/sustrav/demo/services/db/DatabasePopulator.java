package org.sustrav.demo.services.db;


import javax.transaction.Transactional;

public interface DatabasePopulator {
    @Transactional
    void populateDatabase();
}
