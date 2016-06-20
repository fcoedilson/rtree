package br.edu.fa7.rtree.bean;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.edu.fa7.rtree.util.Util;

@Scope("session")
@Component("controlBean")
public class ControlStateBean extends BaseStateBean implements Serializable {


	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ControlStateBean.class);
	
	public String voltar() {
		if (Util.getSession() != null) {
			Util.getSession().invalidate();
		}
		return SUCCESS;
	}
	
	public boolean isPontoBean(){
		return PontoBean.class.getSimpleName().equals(getCurrentBean());
	}

}