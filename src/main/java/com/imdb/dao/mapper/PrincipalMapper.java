package com.imdb.dao.mapper;

import com.imdb.dao.impl.Name;
import com.imdb.dao.impl.Principal;
import com.imdb.dao.impl.PrincipalId;
import com.imdb.dao.impl.Title;
import com.imdb.util.ImdbRecordUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PrincipalMapper implements ImdbMapper<Principal> {


    @Override
    public Principal map(Map<String, String> readMap) {
        Principal.PrincipalBuilder builder = Principal.builder()
                .category(readMap.get("category"))
                .characters(readMap.get("characters"));
        if (!ImdbRecordUtil.isNull(readMap.get("job"))) builder.job(readMap.get("job"));
        builder.id(new PrincipalId(
                readMap.get("tconst"),
                Integer.parseInt(readMap.get("ordering"))
        ));
        return builder.build();
    }
}
