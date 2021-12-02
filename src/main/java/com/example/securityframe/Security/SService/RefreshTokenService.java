package com.example.securityframe.Security.SService;

import com.example.securityframe.AuxiliaryClasses.StaticMethods;
import com.example.securityframe.DAO.RefreshTokenDAO;
import com.example.securityframe.DAO.RoleDAO;
import com.example.securityframe.Entity.Manager;
import com.example.securityframe.Entity.RefreshToken;
import com.example.securityframe.Entity.Role;
import com.example.securityframe.Service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.securityframe.Security.SecurityConstants.*;

@Service
public class RefreshTokenService {

    @Autowired
    JWTokenService jwTokenService;
    @Autowired
    ManagerService managerService;
    @Autowired
    RoleDAO roleDAO;
    @Autowired
    RefreshTokenDAO refreshTokenDAO;

    /** Создание нового refresh-токена */
    public RefreshToken createRTbyUserLogin(String login){
        Manager manager = managerService.findByLogin(login);

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setManager_id(manager.getId());
        refreshToken.setExpiryDate(System.currentTimeMillis()+(EXPIRATION_TIME_OF_RT));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = save(refreshToken);

        return refreshToken;
    }

    /** Нахождение объекта refreshToken в БД, через его поле token */
    public RefreshToken findByToken(String refreshToken){
        return refreshTokenDAO.findByToken(refreshToken);
    }


    /** Верификация refresh-токена */
    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(System.currentTimeMillis()) <= 0) {
            refreshTokenDAO.delete(refreshToken);
            return null;
        }

        // Обновляю время просрочки и ставлю новый токен
        refreshToken.setExpiryDate(System.currentTimeMillis() + (EXPIRATION_TIME_OF_RT));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = save(refreshToken);
        return refreshToken;
    }

    public RefreshToken save(RefreshToken refreshToken){
        refreshTokenDAO.save(refreshToken);
        return refreshTokenDAO.findByToken(refreshToken.getToken());
    }

    /** Удаление всех токенов, принадлежащих конкретному пользователю */
    public void deleteAllByUser(Manager manager) {
        List<RefreshToken> list = refreshTokenDAO.findAllByUserEntity(manager);
        for(RefreshToken refreshToken: list)
            refreshTokenDAO.delete(refreshToken);
    }


    /**
     * Обновление refresh-токена и jwt-токена (от клиента нужны они оба. В ответе в хедер запихнутся 2 новых)
     * @code 201 - Created
     * @code 432 - Refresh token doesn't exist
     * @code 433 - Refresh token was expired
     * */
    public void refreshToken(HttpServletRequest request, HttpServletResponse response){

        String rToken = request.getHeader(HEADER_RT_STRING);
        String jwToken = request.getHeader(HEADER_EXPIRED_JWT_STRING);
        if(rToken!=null && jwToken!=null){
            RefreshToken refreshToken = findByToken(rToken);
            if(refreshToken == null){
                StaticMethods.createResponse(request, response,
                        432,"Refresh token doesn't exist");
                String login = jwTokenService.decodeJWT(jwToken);
                Manager manager = managerService.findByLogin(login);
                if(manager !=null){
                    deleteAllByUser(manager);
                }
                return;
            }
            refreshToken = verifyExpiration(refreshToken);
            if(refreshToken == null){
                StaticMethods.createResponse(request, response,
                        433,"Refresh token was expired");
                return;
            }

            Long manager_id = refreshToken.getManager_id();
            Manager manager = managerService.findById(manager_id);
            List<Role> roles = roleDAO.findByManagerId(manager_id);

            String token = jwTokenService.createJWT(manager.getEmail(), roles.get(0).getRole());
            response.addHeader(HEADER_JWT_STRING, TOKEN_PREFIX+token);
            response.addHeader(HEADER_RT_STRING, refreshToken.getToken());

            //Устанавливаем, какие хедеры может видеть фронт
            response.addHeader("Access-Control-Expose-Headers", HEADER_JWT_STRING+","+HEADER_RT_STRING);

            StaticMethods.createResponse(request, response,
                    HttpServletResponse.SC_CREATED, "Created");

        }

    }

}
