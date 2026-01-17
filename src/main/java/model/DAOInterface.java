package model;

import java.util.List;

public interface DAOInterface<T> {
	T selectById (String id);
	List<T> selectAll ();
	boolean insert (T t);
	boolean update (T t);
	boolean delete (T t);
}
