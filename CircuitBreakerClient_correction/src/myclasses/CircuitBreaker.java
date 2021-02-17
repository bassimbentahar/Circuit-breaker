package myclasses;

import java.net.URI;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

public class CircuitBreaker {

	private static HashMap<String, SeveurState> serviceState = new HashMap<String, SeveurState>();

	/**
	 * Au debut le circuit est fermé
	 * Si timeout le circuit se ferme
	 * Ã  la 5eme requete il passe a  semi-ouvert ensuite il laisse passer 3
	 *  requetes si 3 timeoutes il ouvre
	 */
	public HttpResponse execute(HttpUriRequest request, HttpClient client) throws Exception {

		URI uri = request.getURI();
		String path = getFullPath(uri);
		
		
		// apres 4 requetes que le circtuit est ferme
		// Verifier si l'uri existe deja dans la HashMap
		if (!serviceState.containsKey(path)) {
			serviceState.put(path, new SeveurState(path));
		}
		

		if ((serviceState.get(path)).getCptRequetesFermes() == 5 && checkState(uri).equals(StateLabel.open)) {
			half_open(uri);
			System.out.println("Au 5 eme appel il ouvre le circuit a moitier");
			(serviceState.get(path)).setCptRequetesFermes(0);
		}
		
//		fermé
		if (checkState(uri).equals(StateLabel.closed)) {
			System.out.println(" Le ciruit breaker est fermé");
			HttpResponse response;
			try {
				response = client.execute(request);
				System.out.println(response.getStatusLine().getStatusCode());
				if (response.getStatusLine().getStatusCode() >= 500)
					open(uri);

			} catch (Exception e) {
				open(uri);
				System.out.println(" Le ciruit breaker est  ouvert");
				throw e;
			}
		} else if (checkState(uri).equals(StateLabel.half_open)) {// ouvert a moitie
			System.out.println("--------------");
			HttpResponse response;
			try {
				response = client.execute(request);
				System.out.println(response.getStatusLine().getStatusCode());
				close(uri);
				if (response.getStatusLine().getStatusCode() >= 500)
					open(uri);

			} catch (Exception e) {
				System.out.println("Circuit moitier ouvert timeout N° =" + (serviceState.get(path)).getCptTimeOut());
				(serviceState.get(path)).incCptTimeOut();
				if ((serviceState.get(path)).getCptTimeOut() == 4) {
					open(uri);
					System.out.println(" au 3eme timeouts le circuit breaker s'ouvre ");
					(serviceState.get(path)).setCptTimeOut(1);
				}
				throw e;
			}
		} // ouvert
		else {
			(serviceState.get(path)).incCptRequetesFermes();
			throw new CircuitBreakerException();
		}

		return null;
	}

	// retourne l'etat d'uri envoye
	private StateLabel checkState(URI uri) {
		String path = getFullPath(uri);
		if (serviceState.containsKey(path)) {
			if ((serviceState.get(path)).getStatLabel().equals(StateLabel.open)) {
				return StateLabel.open;
			} else if ((serviceState.get(path)).getStatLabel().equals(StateLabel.half_open)) {
				return StateLabel.half_open;
			}
		}
		return StateLabel.closed;
	}

	// set the circuit breaker to open for the service
	private void open(URI uri) {
		String path = getFullPath(uri);
		(serviceState.get(path)).setStatLabel(StateLabel.open);
	}

	// set the circuit breaker to close for the service
	private void close(URI uri) {
		String path = getFullPath(uri);
		(serviceState.get(path)).setStatLabel(StateLabel.closed);
	}

	// set the circuit breaker to close for the service
	private void half_open(URI uri) {
		String path = getFullPath(uri);
		(serviceState.get(path)).setStatLabel(StateLabel.half_open);
	}

	// returns the full path of the uri (removing query parameters)
	private String getFullPath(URI uri) {
		String path = uri.toString();
		int index = path.indexOf("?");
		if (index > 1)
			path = path.substring(0, index);
		return path;
	}
}
