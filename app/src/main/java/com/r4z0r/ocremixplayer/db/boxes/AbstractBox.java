package com.r4z0r.ocremixplayer.db.boxes;

import com.r4z0r.ocremixplayer.db.ObjectBox;
import com.r4z0r.ocremixplayer.db.models.RemixObj;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.Property;

public abstract class AbstractBox<T> {
    protected BoxStore boxStore;
    protected Class<T> typeParameterClass;

    public AbstractBox() {
        typeParameterClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.boxStore = ObjectBox.get();
        this.getBox = boxStore.boxFor(typeParameterClass);
    }

    protected Property<T> getIdProperty() {
        return getBox.getEntityInfo().getIdProperty();
    }

    protected Box<T> getBox;

    public void put(T entity) {
        getBox.put(entity);
    }

    public void putAll(Collection<T> entities) {
        getBox.put(entities);
    }

    public T get(long id) {
        return getBox.get(id);
    }

    public long count() {
        return getBox.count();
    }

    public void remove(T entity) {
        getBox.remove(entity);
    }

    public void remove(long id) {
        getBox.remove(id);
    }

    public void removeAll() {
        getBox.removeAll();
    }

    public Collection<T> getAll() {
        return getBox.getAll();
    }

    public long getId(T entity) {
        return getBox.getId(entity);
    }

    public boolean contains(long id) {
        return getBox.contains(id);
    }
}
