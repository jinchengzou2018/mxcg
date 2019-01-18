package com.mxcg.db.jpa;

import java.io.Serializable;
import java.util.List;

import com.mxcg.core.Result;
import com.mxcg.jpa.FindRequest;
import org.springframework.data.domain.Page;


public interface JpaAccess<T, ID extends Serializable>
{
    Result<T> save(T entity);

    Result<Boolean> delete(ID id);

    Result<T> findOne(ID id);

    Result<List<T>> findAll();

    Result<List<T>> findAll(Iterable<ID> ids);

    Result<Page<T>> findAll(FindRequest<T> request);
}
