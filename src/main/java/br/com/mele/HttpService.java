package br.com.mele;

import java.io.IOException;
import java.util.logging.Logger;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpService {
	private final OkHttpClient httpClient = new OkHttpClient();
	Logger logger = Logger.getLogger(HttpService.class.getName());

	public void sendGet() throws Exception {

		Request request = new Request.Builder().url("http://ml.gswapp.com/RFE_API/token")
				.addHeader("custom-key", "mkyong") // add request headers
				.addHeader("User-Agent", "OkHttp Bot").addHeader("Content-Type", "application/x-www-form-urlencoded")
				.build();

		try {
			Response response = httpClient.newCall(request).execute();
			if (!response.isSuccessful())
				throw new IOException("Unexpected code " + response);

			// Get response body
			System.out.println(response.body().string());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public void sendPost() throws Exception {
		logger.info("Enviando dados");
		// form parameters
		RequestBody formBody = new FormBody.Builder().add("username", "usrws_ml").add("password", "Nkw0@1K#N98")
				.add("grant_type", "password").build();

		Request request = new Request.Builder().url("http://ml.gswapp.com/RFE_API/token")
				.addHeader("User-Agent", "OkHttp Bot").post(formBody).build();

		try {

			Response response = httpClient.newCall(request).execute();
			if (!response.isSuccessful()) {
				logger.info("Erro : " + response);
				throw new IOException("Unexpected code " + response);

			} else {
				logger.info("Sucesso : " + response);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
