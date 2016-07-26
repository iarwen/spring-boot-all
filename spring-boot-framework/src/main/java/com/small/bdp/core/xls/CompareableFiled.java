package com.small.bdp.core.xls;

import java.lang.reflect.Field;

public class CompareableFiled implements Comparable<CompareableFiled>{
		private Field field;
		private int order;
		private int scale;
	 
		private String displayName;
		/**
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(CompareableFiled o) {
			return this.order-o.order;
		}

		public int getOrder() {
			return order;
		}

		public void setOrder(int order) {
			this.order = order;
		}

		public Field getField() {
			return field;
		}

		public void setField(Field field) {
			this.field = field;
		}

		public int getScale() {
			return scale;
		}

		public void setScale(int scale) {
			this.scale = scale;
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}
}

