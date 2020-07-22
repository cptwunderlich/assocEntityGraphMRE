# Entity Graph Problem MRE
Entity Graph on selected association Minimum Reproducing Example.

When selecting an association and applying an Entity Graph, the attributes will be resolved
relative to the Entity in the FROM clause, not the actually selected Entity:

    EntityGraph<Group> eg = em.createEntityGraph(Group.class);
    eg.addAttributeNodes("permissions");


    List res = em.createQuery("SELECT u.groups FROM User u WHERE u.id = ?1")
            .setParameter(1, u.getId())
            .setHint(GraphSemantic.FETCH.getJpaHintName(), eg)
            .getResultList();

Yields:

> org.hibernate.QueryException: query specified join fetching, but the owner of the fetched association was not present in the select list [FromElement{explicit,collection join,fetch join,fetch non-lazy properties,classAlias=u,role=com.example.assocFetchGraph.persistence.User.permissions,tableName={none},tableAlias=permission3_,origin=null,columns={,className=null}}]
