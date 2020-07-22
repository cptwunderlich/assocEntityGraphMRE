package com.example.assocFetchGraph;

import com.example.assocFetchGraph.persistence.Group;
import com.example.assocFetchGraph.persistence.Permission;
import com.example.assocFetchGraph.persistence.User;
import org.hibernate.graph.GraphSemantic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import java.util.List;

@DataJpaTest
class AssocFetchGraphApplicationTests {

	@Autowired
	private EntityManager em;

	private User u;
	private Group g1, g2;

	@BeforeEach
	void prepare() {
		g1 = new Group();
		g1.addPermission(Permission.BAR);
		em.persist(g1);

		g2 = new Group();
		g2.addPermission(Permission.BAZ);
		em.persist(g2);

		u = new User();
		em.persist(u);
		u.addGroup(g1);
		u.addGroup(g2);
	}

	@Test
	void fetchAssocWithNamedFetchGraph() {

		List res = em.createQuery("SELECT u.groups FROM User u WHERE u.id = ?1")
				.setParameter(1, u.getId())
				.setHint(GraphSemantic.FETCH.getJpaHintName(), em.getEntityGraph(Group.ENTITY_GRAPH))
				.getResultList();

		Assertions.assertTrue(res.containsAll(List.of(g1, g2)));
	}

	@Test
	void fetchAssocWithNamedFetchGraphAndJoin() {

		List res = em.createQuery("SELECT g FROM User u JOIN u.groups g WHERE u.id = ?1")
				.setParameter(1, u.getId())
				.setHint(GraphSemantic.FETCH.getJpaHintName(), em.getEntityGraph(Group.ENTITY_GRAPH))
				.getResultList();

		Assertions.assertTrue(res.containsAll(List.of(g1, g2)));
	}

	@Test
	void fetchAssocWithAdhocFetchGraph() {

		EntityGraph<Group> eg = em.createEntityGraph(Group.class);
		eg.addAttributeNodes("permissions");

		List res = em.createQuery("SELECT u.groups FROM User u WHERE u.id = ?1")
				.setParameter(1, u.getId())
				.setHint(GraphSemantic.FETCH.getJpaHintName(), eg)
				.getResultList();

		Assertions.assertTrue(res.containsAll(List.of(g1, g2)));
	}

	@Test
	void fetchAssocWithFetchJoins() {
		List res = em.createQuery("SELECT g FROM User u JOIN u.groups g JOIN FETCH g.permissions WHERE u.id = ?1")
				.setParameter(1, u.getId())
				.getResultList();

		Assertions.assertTrue(res.containsAll(List.of(g1, g2)));
	}
}
