package myclasses;

public class SeveurState {
	
	private String path;
	private StateLabel statLabel=StateLabel.closed;
	private int cptRequetesFermes=0;
	private  int cptTimeOut=1;
	

	public SeveurState(String path) {
		super();
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public int getCptRequetesFermes() {
		return cptRequetesFermes;
	}

	public int getCptTimeOut() {
		return cptTimeOut;
	}

	public StateLabel getStatLabel() {
		return statLabel;
	}

	public void setStatLabel(StateLabel statLabel) {
		this.statLabel = statLabel;
	}

	public  void setCptRequetesFermes(int cptRequetesFermes) {
		this.cptRequetesFermes = cptRequetesFermes;
	}

	public void setCptTimeOut(int cptTimeOut) {
		this.cptTimeOut = cptTimeOut;
	}
	public void incCptTimeOut() {
		++cptTimeOut;
	}
	
	public void incCptRequetesFermes() {
		++cptRequetesFermes;
	}
}
