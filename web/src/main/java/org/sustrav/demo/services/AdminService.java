package org.sustrav.demo.services;

import javax.transaction.Transactional;

public interface AdminService {
    @Transactional
    void removeUserInfo(Long id);
}
