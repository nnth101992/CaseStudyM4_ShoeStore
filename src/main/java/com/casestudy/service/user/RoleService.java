package com.casestudy.service.user;

import com.casestudy.model.Role;
import com.casestudy.service.Service;

public interface RoleService extends Service<Role> {
    Role findByName(String name);
}
