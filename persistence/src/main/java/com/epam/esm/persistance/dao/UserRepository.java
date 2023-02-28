package com.epam.esm.persistance.dao;

import com.epam.esm.persistance.dao.support.page.PagingRepository;
import com.epam.esm.persistance.entity.User;
import java.util.UUID;

public interface UserRepository extends PagingRepository<User, UUID> {
}
