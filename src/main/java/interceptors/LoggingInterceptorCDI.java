package interceptors;

import java.lang.reflect.Parameter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;

import dal.LogFacade;
import entities.LogMessage;
import utility.DateTimeUtil;

@Interceptor
@Logged
public class LoggingInterceptorCDI {
	
	private final Logger logger = Logger.getLogger(LoggingInterceptorCDI.class.getName());
	
	@Inject
	private LogFacade logRepository;
	@Resource
	private UserTransaction utx;
	
	//CDI beans are not transactional by defualt se a transactional is needed
	public void saveLog(LogMessage log){
		
		try {
			utx.begin();
			logRepository.create(log);
			utx.commit();
		} catch (Exception e) {
			logger.log(Level.INFO, "Error during transaction demarcation in LoggingINterceptorCDI");
		}
		
	}
	
	@AroundInvoke
	public Object logMethodInvocation(InvocationContext ctx) throws Exception{

		//Collecting data about invoked method through invocationcontext
		final StringBuilder sb = new StringBuilder();
		sb.append(ctx.getMethod().getName());
		sb.append("( ");
		List<Parameter> params = Arrays.asList(ctx.getMethod().getParameters());
		params.stream().forEach( p -> sb.append(p.toString() + ","));
		sb.append(")");
		sb.append(" was called from class: ");
		sb.append(ctx.getMethod().getDeclaringClass().getName());
		String message = sb.toString();
		
		//time of the call
		SimpleDateFormat sdf = new SimpleDateFormat(DateTimeUtil.DATE_FORMAT_SECONDS);
		String logTime =sdf.format(new Date());
		
		//create entity then save it to db (LOGS table)
		LogMessage log = new LogMessage();
		log.setMessage(message);
		log.setTimeStamp(logTime);
		
		saveLog(log);
		
		return ctx.proceed();
	}

}
