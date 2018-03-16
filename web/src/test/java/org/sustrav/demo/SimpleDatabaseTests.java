package org.sustrav.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.sustrav.demo.data.PlaceRepository;
import org.sustrav.demo.data.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional()
@Commit
public class SimpleDatabaseTests {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Test
    public void simpleUseCase() {
        userRepository.findAll();
    }

    private void persist(Object obj) {
        em.persist(obj);
        em.flush();
    }
}
