package project.baptisteq.projectlillenopendata.utils;


import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import project.baptisteq.projectlillenopendata.bdd.DaoSession;
import project.baptisteq.projectlillenopendata.bdd.StarFields;
import project.baptisteq.projectlillenopendata.bdd.StarFieldsDao;
import project.baptisteq.projectlillenopendata.controller.ApplicationProjectLilleOpenData;

public class StarUtils {

    /**
     * Ajout nun
     * @param recordId
     * @return
     */
    public static Long addStarFields(String recordId) {
        StarFieldsDao starFieldsDao = getStarFieldsDao();
        StarFields starFields = new StarFields(recordId);
        starFieldsDao.insert(starFields);
        resetCache();
        return starFields.getId();
    }

    private static StarFieldsDao getStarFieldsDao() {
        final DaoSession daoSession = ApplicationProjectLilleOpenData.getDaoSession();
        return daoSession.getStarFieldsDao();
    }

    private static void resetCache() {
        ApplicationProjectLilleOpenData.getDaoSession().clear();
    }

    public static void deleteStarFields(String recordId) {
        StarFieldsDao starFieldsDao = getStarFieldsDao();
        DeleteQuery<StarFields> dq = starFieldsDao.queryBuilder().where(StarFieldsDao.Properties.RecordId.eq(recordId)).buildDelete();
        dq.executeDeleteWithoutDetachingEntities();
        resetCache();

    }

    public static List<StarFields> getAllStarFields() {
        StarFieldsDao starFieldsDao = getStarFieldsDao();
        return starFieldsDao.loadAll();
    }

    /**
     * Retourne vrai si la table StarFields comporte un élément avec recordId === recordId
     *
     * @param recordId
     * @return
     */
    public static boolean isStarFields(String recordId) {
        StarFieldsDao starFieldsDao = getStarFieldsDao();

        QueryBuilder<StarFields> qb = starFieldsDao.queryBuilder().where(StarFieldsDao.Properties.RecordId.eq(recordId));
        return !qb.list().isEmpty();
    }

}
