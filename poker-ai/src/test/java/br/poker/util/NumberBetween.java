package br.poker.util;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class NumberBetween<T> extends BaseMatcher<T> {
		private final T lowerBoundary;
		private final T upperBoundary;
		private T object;

		public static <T> Matcher<T> between(T lowerBoundary, T upperBoundary) {
			return new NumberBetween<T>(lowerBoundary, upperBoundary);
		}

		public NumberBetween(T lowerBoundary, T upperBoundary) {
			this.lowerBoundary = lowerBoundary;
			this.upperBoundary = upperBoundary;
		}

		@Override
		public void describeTo(Description desc) {
			desc.appendText(object + " between [" + lowerBoundary + "," + upperBoundary + "]" );
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean matches(Object object) {
			this.object = (T) object;
			if(object instanceof Double) {
				double obj = (Double)object;
				return obj >= (Double)this.lowerBoundary && obj <= (Double)this.upperBoundary; 
			}
			return false;
		}
	}