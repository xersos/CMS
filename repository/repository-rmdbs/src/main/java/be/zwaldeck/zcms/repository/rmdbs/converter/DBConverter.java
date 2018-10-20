package be.zwaldeck.zcms.repository.rmdbs.converter;

public interface DBConverter<API, DB> {
    DB toDB(API entity, boolean update);
    API fromDB(DB db);
}
