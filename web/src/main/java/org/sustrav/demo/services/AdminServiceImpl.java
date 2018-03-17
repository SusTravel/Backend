package org.sustrav.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sustrav.demo.data.UserAuthorityRepository;
import org.sustrav.demo.data.UserRepository;
import org.sustrav.demo.data.VisitedPlaceRepository;
import org.sustrav.demo.data.model.User;

@Service
public class AdminServiceImpl implements AdminService {


    @Autowired
    private VisitedPlaceRepository visitedPlaceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuthorityRepository authorityRepository;

    @Override
    public void removeUserInfo(Long id) {
        User user = userRepository.findById(id);
        if (user == null)
            return;

        visitedPlaceRepository.deleteVisitedPlaceForUser(id);
        userRepository.delete(id);
    }
}
