package dal;

import javax.persistence.EntityManager;

public abstract class DBFacade<T> {

	private Class<T> entityClass;
	
	public DBFacade(Class<T> eclass)
	{
		entityClass = eclass;
	}
	
	protected abstract EntityManager getEm();
	
	public void create(T entity)
	{
		getEm().persist(entity);
	}
	
	public void update(T entity)
	{
		getEm().merge(entity);
	}
	
	public void remove(T entity)
	{
		getEm().remove(getEm().merge(entity));
	}
	
	public T find(Object id) {
		return getEm().find(entityClass, id);
	}
}
