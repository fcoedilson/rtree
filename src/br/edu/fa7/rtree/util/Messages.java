package br.edu.fa7.rtree.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Messages {
	
	public static void addInfo(String message) {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
	}

	public static void addWarn(String message) {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_WARN,
						message, null));
	}

	public static void addError(String message) {
		FacesContext.getCurrentInstance()
				.addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								message,
								null));
	}

	public static void addFatal(String message) {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_FATAL,
						message, null));
	}
}
