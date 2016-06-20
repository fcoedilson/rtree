package br.edu.fa7.rtree.bean;

public class CrudStateBean extends BaseStateBean {

	public static final String SEARCH = "SEARCH";
	public static final String SAVE = "SAVE";
	public static final String UPDATE = "UPDATE";
	public static final String DELETE = "DELETE";

	public boolean isSearchState() {
		return SEARCH.equals(getCurrentState());
	}

	public boolean isSaveState() {
		return SAVE.equals(getCurrentState());
	}

	public boolean isUpdateState() {
		return UPDATE.equals(getCurrentState());
	}

	public boolean isDeleteState() {
		return DELETE.equals(getCurrentState());
	}

	public String prepareSave() {
		setCurrentBean(currentBeanName());
		setCurrentState(SAVE);
		return SUCCESS;
	}

	public String prepareUpdate() {
		setCurrentBean(currentBeanName());
		setCurrentState(UPDATE);
		return SUCCESS;
	}

	public String prepareDelete() {
		setCurrentBean(currentBeanName());
		setCurrentState(DELETE);
		return SUCCESS;
	}

	public String view(){
		setCurrentBean(currentBeanName());
		return search();
	}

	public String edit(){
		setCurrentBean(currentBeanName());
		return search();
	}

	public String save() {
		setCurrentBean(currentBeanName());
		return search();
	}

	public String update() {
		setCurrentBean(currentBeanName());
		return search();
	}

	public String delete() {
		setCurrentBean(currentBeanName());
		return search();
	}

	public String search() {
		setCurrentBean(currentBeanName());
		setCurrentState(SEARCH);
		return SUCCESS;
	}

	public String searchSort() {
		setCurrentBean(currentBeanName());
		setCurrentState(SEARCH);
		return SUCCESS;
	}

	public String searchStatus() {
		setCurrentBean(currentBeanName());
		setCurrentState(SEARCH);
		return SUCCESS;
	}
}