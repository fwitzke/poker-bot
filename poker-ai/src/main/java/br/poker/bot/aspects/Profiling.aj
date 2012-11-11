package br.poker.bot.aspects;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.Signature;

import br.poker.util.Logger;

public aspect Profiling {
	private static Map<String, Long> methodTimes = new HashMap<String, Long>();;

	pointcut table_updates() : execution(* *.update*(*));

	before() : table_updates(){
		long start = System.nanoTime();
		methodTimes.put(thisJoinPoint.getSignature().getName(), start);
	}

	after() returning() : table_updates() {
		long end = System.nanoTime();
		Long start = methodTimes.get(thisJoinPoint.getSignature().getName());
		if (start == null)
			start = System.nanoTime();
		long time = ((end - start) / 1000) / 1000;
		
		Signature signature = thisJoinPoint.getSignature();
		String methodName = signature.getName();
		String className = signature.getDeclaringTypeName();
		Logger.log("[Profiling] Ended " + className + "#" + methodName + " in " + time + "ms");
	}
}
