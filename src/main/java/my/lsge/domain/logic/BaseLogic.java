package my.lsge.domain.logic;

import my.lsge.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaseLogic {
    @Autowired
    protected UserRepository userRepository;
}
