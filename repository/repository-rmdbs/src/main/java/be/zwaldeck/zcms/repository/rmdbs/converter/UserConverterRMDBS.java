package be.zwaldeck.zcms.repository.rmdbs.converter;

import be.zwaldeck.zcms.repository.api.model.User;
import be.zwaldeck.zcms.repository.rmdbs.domain.UserDB;
import org.springframework.stereotype.Component;

@Component
public class UserConverterRMDBS implements DBConverter<User, UserDB> {

    @Override
    public UserDB toDB(User entity, boolean update) {
        var db = new UserDB();

        if (update) {
            db.setId(entity.getId());
            db.setCreatedAt(entity.getCreatedAt());
        }

        db.setUsername(entity.getUsername());
        db.setRoles(entity.getRoles());
        db.setPassword(entity.getPassword());

        return db;
    }

    @Override
    public User fromDB(UserDB db) {
        var entity = new User();
        entity.setId(db.getId());
        entity.setUsername(db.getUsername());
        entity.setPassword(db.getPassword());
        entity.setRoles(db.getRoles());
        entity.setCreatedAt(db.getCreatedAt());
        entity.setUpdatedAt(db.getUpdatedAt());

        return entity;
    }
}
