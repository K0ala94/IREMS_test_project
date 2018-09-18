package interceptors;

import java.lang.reflect.Parameter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import dal.LogFacade;
import entities.LogMessage;
import utility.DateTimeUtil;

public class LoggingInterceptor {
	
	private final Logger logger = Logger.getLogger(LoggingInterceptor.class.getName());
	
	@Inject
	private LogFacade logRepository;
	
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
		
		logRepository.create(log);
		
		logger.log(Level.ALL, message + " at " + logTime);
		
		return ctx.proceed();
	}
}
