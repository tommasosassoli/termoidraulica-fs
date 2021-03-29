package fatture.model.serialization;

import java.util.Collection;

public abstract class AbstractSerialization<T> {
	
	public AbstractSerialization() {
		
	}
	
	public abstract Collection<T> getCollection();
	
	public abstract boolean isEmpty();
}
