package dal;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entities.LogMessage;

@Dependent
public class LogFacade extends DBFacade<LogMessage> {
	
	@PersistenceContext
	private EntityManager em;

	public LogFacade(Class<LogMessage> eclass) {
		super(eclass);
	}
	
	public LogFacade(){
		super(LogMessage.class);
	}

	@Override
	protected EntityManager getEm() {
		return em;
	}

}
