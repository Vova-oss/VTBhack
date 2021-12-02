package com.example.securityframe.Security;

import com.example.securityframe.DAO.ManagerDAO;
import com.example.securityframe.DAO.RoleDAO;
import com.example.securityframe.Entity.Manager;
import com.example.securityframe.Entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    ManagerDAO managerDAO;
    @Autowired
    RoleDAO roleDAO;


    /** Внутрений метод фильтров Security (JWTAuthenticationFilter /attemptAuthentication)
     *  для проверки существования пользователя в БД */
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Manager manager = managerDAO.findByEmail(login);

        // Если пользователя не существует или он не подтвердил свой телефонный номер
        if(manager == null){
            throw new UsernameNotFoundException(login);
        }

        //Херобора для превращения нашего листа с ролями в Collection <? extends GrantedAuthority>
        //Взял от сюда: https://www.youtube.com/watch?v=m5FAo5Oa6ag&t=3818s время 30:20

        List<Role> roles = roleDAO.findByManagerId(manager.getId());

        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole()))
                .collect(Collectors.toList());

        return new User(manager.getEmail(), manager.getPassword(), authorities);
    }



}
