package dao;

import entity.Goodsentity;
import entity.Userentity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sweet on 15-6-6.
 */
@Repository
public class GoodsDao {
    private EntityManager em;

    @PersistenceContext
    void setEm(EntityManager entityManager) {this.em = entityManager;}

    public Goodsentity findGoodsById(int id) {
        return em.find(Goodsentity.class, id);
    }

    public List<Goodsentity> findLatestGoods() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Goodsentity> c = cb.createQuery(Goodsentity.class);
        Root<Goodsentity> goods = c.from(Goodsentity.class);
        c.select(goods);
        c.where(cb.equal(goods.get("status"), "2"));
        c.orderBy(cb.desc(goods.get("startTime")));

        TypedQuery<Goodsentity> query = em.createQuery(c);
        query.setMaxResults(8);

        List<Goodsentity> results = query.getResultList();

        if (results.isEmpty()) {
            return null;
        } else {
            return results;
        }
    }

    public List<Goodsentity> findGoods(String keyWord, String type_id, int page) {
        TypedQuery<Goodsentity> query;
        if (type_id.equals("000000")) {
            query = em.createQuery("select g from Goodsentity g where g.status = '2' and g.name like :name", Goodsentity.class);
            query.setParameter("name", "%"+keyWord+"%");
        } else {
            query = em.createQuery("select g from Goodsentity g where g.status = '2' and g.typeId = :typeid and g.name like :name", Goodsentity.class);
            query.setParameter("typeid", type_id);
            query.setParameter("name", "%"+keyWord+"%");
        }

        query.setFirstResult(18*(page-1));
        query.setMaxResults(18);

        List<Goodsentity> results = query.getResultList();

        return results;
    }

    public int createAppGoods(Goodsentity goodsentity) {
        em.persist(goodsentity);
        em.flush();

        return goodsentity.getGoodsId();
    }
}
