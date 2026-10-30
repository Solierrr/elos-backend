package util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import config.Env;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class EnviarEmail extends Env {

  //Constantes utilizadas pelo codigo
  private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";
  private static final int SUCESSO = 201;

  public boolean enviarEmail(String destinatario, String assunto, String corpo) {
    String apiKey = getBrevoApiKey();
    String remetente = getEmailUsuario();

    JsonObject sender = new JsonObject();
    sender.addProperty("name", "Elos");
    sender.addProperty("email", remetente);

    JsonObject to = new JsonObject();
    to.addProperty("email", destinatario);

    JsonArray toArray = new JsonArray();
    toArray.add(to);

    JsonObject body = new JsonObject();
    body.add("sender", sender);
    body.add("to", toArray);
    body.addProperty("subject", assunto);
    body.addProperty("htmlContent", corpo);

    try {
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request =
              HttpRequest.newBuilder()
                      .uri(URI.create(BREVO_API_URL))
                      .header("accept", "application/json")
                      .header("api-key", apiKey)
                      .header("content-type", "application/json")
                      .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                      .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      System.out.println("Enviando e-mail =)");
      return response.statusCode() == SUCESSO;
    } catch (IOException | InterruptedException exception) {
      System.out.println("Erro ao enviar e-mail");
      return false;
    }
  }
}