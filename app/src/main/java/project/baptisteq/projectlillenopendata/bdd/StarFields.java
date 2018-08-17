package project.baptisteq.projectlillenopendata.bdd;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Représente un Fields mis en favori
 * Pas de relation, l'objet est atomique
 * On stocke un ID auto généré et un recordID unique afin de le matcher avec les données OPEN DATA
 */

@Entity(
        // If you have more than one schema, you can tell greenDAO
        // to which schema an entity belongs (pick any string as a name).
        //schema = "myschema",

        // Flag to make an entity "active": Active entities have update,
        // delete, and refresh methods.
        active = true,

        // Specifies the name of the table in the database.
        // By default, the name is based on the entities class name.
        //nameInDb = "AWESOME_USERS",

        // Define indexes spanning multiple columns here.
        //indexes = {
        //      @Index(value = "name DESC", unique = true)
        //},

        // Flag if the DAO should create the database table (default is true).
        // Set this to false, if you have multiple entities mapping to one table,
        // or the table creation is done outside of greenDAO.
        //createInDb = false,

        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        //generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true
)
public class StarFields {

    @Id(autoincrement = true)
    private Long id;
    private String recordId;

    public StarFields(String recordId) {
        this.recordId = recordId;
    }

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1340413091)
    private transient StarFieldsDao myDao;

    @Generated(hash = 808752372)
    public StarFields(Long id, String recordId) {
        this.id = id;
        this.recordId = recordId;
    }

    @Generated(hash = 2091211361)
    public StarFields() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecordId() {
        return this.recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1038231182)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getStarFieldsDao() : null;
    }


}
